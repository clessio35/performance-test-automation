package main;

import java.util.Arrays;
import java.util.List;

import config.PerformanceConfig;
import model.PerformanceResult;
import service.CsvExporter;
import service.PerformanceService;

public class Main {

    public static void main(String[] args) {

        // 1️⃣ Configuração do teste
        PerformanceConfig config = new PerformanceConfig();

        // 2️⃣ URLs a serem testadas
        List<String> urls = Arrays.asList(
                "https://jsonplaceholder.typicode.com/posts",
                "https://jsonplaceholder.typicode.com/comments"
        );

        try {
            // 3️⃣ Executar teste de performance
            PerformanceService performanceService = new PerformanceService(config);
            PerformanceResult result = performanceService.executarTeste(urls);

            // 4️⃣ Validar SLA direto aqui
            boolean slaPassou = result.tempoMedio <= config.tempoMedioMax
                    && result.p95 <= config.p95Max
                    && result.taxaErro <= config.erroMax
                    && result.throughput >= config.throughputMin;

            // 5️⃣ Exibir resultados no console
            System.out.println("\n===== RESULTADO DO TESTE =====");
            System.out.printf("Tempo médio: %.2f ms%n", result.tempoMedio);
            System.out.println("P95: " + result.p95 + " ms");
            System.out.println("P99: " + result.p99 + " ms");
            System.out.printf("Taxa de erro: %.2f %% %n", result.taxaErro);
            System.out.printf("Throughput: %.2f req/s%n", result.throughput);

            System.out.println("\n===== VALIDAÇÃO SLA =====");
            if (slaPassou) {
                System.out.println("✅ TESTE PASSOU!");
            } else {
                System.out.println("❌ TESTE FALHOU!");
            }

            // 6️⃣ Gerar CSV completo
            String fileName = "relatorio_performance.csv";
            CsvExporter.export(fileName, result, urls, config);

            // 7️⃣ Exit code para GitHub Actions
            if (!slaPassou) {
                System.exit(1); // falha se SLA não passou
            } else {
                System.exit(0); // sucesso
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // falha se ocorreu exceção
        }
    }
}