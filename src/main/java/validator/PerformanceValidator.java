package validator;

import config.PerformanceConfig;
import model.PerformanceResult;

public class PerformanceValidator {

    public static boolean validar(PerformanceResult result, PerformanceConfig config) {

        boolean passou = true;

        // Tempo médio
        if (result.tempoMedio > config.tempoMedioMax) {
            System.out.println("❌ Tempo médio acima do SLA: " + result.tempoMedio);
            passou = false;
        }

        // P95
        if (result.p95 > config.p95Max) {
            System.out.println("❌ P95 acima do SLA: " + result.p95);
            passou = false;
        }

        // Taxa de erro
        if (result.taxaErro > config.erroMax) {
            System.out.println("❌ Taxa de erro acima do SLA: " + result.taxaErro + "%");
            passou = false;
        }

        // Throughput
        if (result.throughput < config.throughputMin) {
            System.out.println("❌ Throughput abaixo do SLA: " + result.throughput);
            passou = false;
        }

        if (passou) {
            System.out.println("✅ TESTE PASSOU!");
        } else {
            System.out.println("❌ TESTE FALHOU!");
        }

        return passou;
    }
}