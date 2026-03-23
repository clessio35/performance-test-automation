package config;

public class PerformanceConfig {
	
	//CARGA
	public int usuarios = 50;   //threads
	public int repeticoes = 10;		//request por users
	public int rampUpSegundos = 5;			//time para subir todos users ramp-up
	public int duracaoSegundos = 30;
	
	//SLA
	public int tempoMedioMax = 500; 		//tempo medio maximo em ms
	public int p95Max = 800;		//percentil 95 maximo  em ms
	public int erroMax = 1; 		//taxa maxima de erro em %
	public int throughputMin = 20; 		// Thoughput minimo request por segundo
	
}
