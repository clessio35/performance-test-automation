package service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.util.Timeout;

import config.PerformanceConfig;
import model.PerformanceResult;

public class PerformanceService {

    private PerformanceConfig config;

    public PerformanceService(PerformanceConfig config) {
        this.config = config;
    }

    public PerformanceResult executarTeste(List<String> urls) throws InterruptedException {

        long inicioTeste = System.currentTimeMillis();

        PerformanceResult result = new PerformanceResult();

        // 🔒 Thread-safe (ESSENCIAL)
        result.tempos = Collections.synchronizedList(result.tempos);
        result.statusCodes = Collections.synchronizedList(result.statusCodes);

        ExecutorService executor = Executors.newFixedThreadPool(config.usuarios);

        // 🔁 Criar tarefas (requests)
        List<Callable<Void>> tasks = urls.stream()
                .flatMap(url -> IntStream.range(0, config.repeticoes)
                        .mapToObj(i -> (Callable<Void>) () -> {
                            enviarRequisicao(url, result);
                            return null;
                        }))
                .collect(Collectors.toList());

        // ⏱️ Ramp-up seguro
        int delay = tasks.isEmpty() ? 0 : config.rampUpSegundos * 1000 / tasks.size();

        for (Callable<Void> task : tasks) {
            executor.submit(() -> {
                task.call();
                return null;
            });

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executor.shutdown();
        executor.awaitTermination(config.duracaoSegundos + 10, TimeUnit.SECONDS);

        long fimTeste = System.currentTimeMillis();

        calcularMetricas(result, inicioTeste, fimTeste);

        return result;
    }

    private void enviarRequisicao(String url, PerformanceResult result) {
        long inicio = System.currentTimeMillis();
        int statusCode = 0;

        try {
            Response response = Request.get(url)
                    .connectTimeout(Timeout.ofSeconds(5))
                    .execute();

            statusCode = response.returnResponse().getCode();

        } catch (Exception e) {
            statusCode = 500;
        }

        long tempo = System.currentTimeMillis() - inicio;

        result.adicionarResultado(tempo, statusCode);
    }

    private void calcularMetricas(PerformanceResult result, long inicioTeste, long fimTeste) {

        List<Long> tempos = result.tempos;

        if (tempos.isEmpty()) return;

        // 📊 Tempo médio
        result.tempoMedio = tempos.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);

        // 📊 Percentis
        List<Long> ordenado = tempos.stream()
                .sorted()
                .collect(Collectors.toList());

        result.p95 = getPercentil(ordenado, 95);
        result.p99 = getPercentil(ordenado, 99);

        // ❌ Taxa de erro
        long erros = result.statusCodes.stream()
                .filter(code -> code >= 400)
                .count();

        if (!result.statusCodes.isEmpty()) {
            result.taxaErro = (erros * 100.0) / result.statusCodes.size();
        }

        // 🚀 Throughput real
        double duracaoSegundos = (fimTeste - inicioTeste) / 1000.0;

        if (duracaoSegundos > 0) {
            result.throughput = result.tempos.size() / duracaoSegundos;
        }
    }

    private long getPercentil(List<Long> lista, int percentil) {
        if (lista.isEmpty()) return 0;

        int index = (int) Math.ceil(percentil / 100.0 * lista.size()) - 1;
        return lista.get(Math.max(index, 0));
    }
}