package service;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import config.PerformanceConfig;
import model.PerformanceResult;

public class CsvExporter {

    public static void export(String fileName, PerformanceResult result, List<String> urls, PerformanceConfig config) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            // 1️⃣ Informações do teste
            writer.println("Informação,Valor");
            writer.println("Usuarios," + config.usuarios);
            writer.println("Repeticoes por usuario," + config.repeticoes);
            writer.println("Ramp-up segundos," + config.rampUpSegundos);
            writer.println("Duracao segundos," + config.duracaoSegundos);
            writer.println("Tempo medio maximo esperado (ms)," + config.tempoMedioMax);
            writer.println("P95 maximo (ms)," + config.p95Max);
            writer.println("Erro maximo (%)," + config.erroMax);
            writer.println("Throughput minimo req/s," + config.throughputMin);
            writer.println(); // linha em branco

            // 2️⃣ Detalhe das requisições
            writer.println("URL,Repeticao,Tempo_ms,Status_Code");
            int indexGlobal = 0;
            for (String url : urls) {
                for (int i = 0; i < config.repeticoes; i++) {
                    if (indexGlobal >= result.tempos.size()) break;
                    long tempo = result.tempos.get(indexGlobal);
                    int status = result.statusCodes.get(indexGlobal);
                    writer.println(url + "," + (i + 1) + "," + tempo + "," + status);
                    indexGlobal++;
                }
            }
            writer.println(); // linha em branco

            // 3️⃣ Resumo por URL
            writer.println("Resumo por URL");
            writer.println("URL,Tempo_medio_ms,P95_ms,P99_ms,Taxa_Erro_percent,Throughput_req_por_segundo");
            indexGlobal = 0;
            double totalThroughput = 0;
            int totalRequests = 0;
            for (String url : urls) {
                int end = Math.min(indexGlobal + config.repeticoes, result.tempos.size());
                List<Long> tempos = result.tempos.subList(indexGlobal, end);
                List<Integer> statusCodes = result.statusCodes.subList(indexGlobal, end);

                double tempoMedio = tempos.stream().mapToLong(Long::longValue).average().orElse(0);
                long p95 = getPercentil(tempos, 95);
                long p99 = getPercentil(tempos, 99);
                long erros = statusCodes.stream().filter(s -> s >= 400).count();
                double taxaErro = erros * 100.0 / statusCodes.size();

                // Throughput por URL
                double tempoTotalSegundos = tempos.stream().mapToLong(Long::longValue).sum() / 1000.0;
                double throughput = tempoTotalSegundos > 0 ? tempos.size() / tempoTotalSegundos : 0;

                totalThroughput += throughput * tempos.size(); // ponderação
                totalRequests += tempos.size();

                writer.println(url + "," + tempoMedio + "," + p95 + "," + p99 + "," + taxaErro + "," + throughput);

                indexGlobal += config.repeticoes;
            }
            writer.println(); // linha em branco

            // 4️⃣ Resumo geral do teste
            writer.println("Resumo geral do teste");
            writer.println("Tempo_medio_ms,P95_ms,P99_ms,Taxa_Erro_percent,Throughput_req_por_segundo,Resultado_SLA");

            // Throughput geral ponderado
            double throughputGeral = totalRequests > 0 ? totalThroughput / totalRequests : 0;

            boolean slaPassou = result.tempoMedio <= config.tempoMedioMax
                    && result.p95 <= config.p95Max
                    && result.taxaErro <= config.erroMax
                    && throughputGeral >= config.throughputMin;

            writer.println(result.tempoMedio + "," + result.p95 + "," + result.p99 + "," + result.taxaErro + "," 
                    + throughputGeral + "," + (slaPassou ? "TESTE PASSOU" : "TESTE FALHOU"));

            writer.flush();
            System.out.println("CSV completo gerado com sucesso: " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long getPercentil(List<Long> lista, int percentil) {
        if (lista.isEmpty()) return 0;
        List<Long> ordenado = lista.stream().sorted().toList();
        int index = (int) Math.ceil(percentil / 100.0 * lista.size()) - 1;
        return ordenado.get(Math.max(index, 0));
    }
}