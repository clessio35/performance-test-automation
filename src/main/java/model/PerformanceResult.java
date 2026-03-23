package model;

import java.util.ArrayList;
import java.util.List;

public class PerformanceResult {
	
	public List<Long> tempos = new ArrayList<>();		//lista com todos os tempos de resposta em ms
	public List<Integer> statusCodes = new ArrayList<>(); 	//lista de codigos de status code retornados
	
	//Metricas calculadas
	public double tempoMedio;
	public long p95;
	public long p99;
	public double taxaErro;
	public double throughput; 		//requisições por segundo
	
	public PerformanceResult() {
	}
	
	public void adicionarResultado(long tempo, int statusCode){
		tempos.add(tempo);
		statusCodes.add(statusCode);
	}
}
