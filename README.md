# Performance Test Automation

Automação de testes de performance para APIs simuladas, desenvolvida em Java 17.
O objetivo é aprender e explorar estratégias de teste e otimização diretamente via código, sem depender de ferramentas externas como JMeter, Gatling ou K6.
O projeto inclui execução automatizada e geração de relatórios CSV completos, além de exibir resultados detalhados no console.

---

🚀 Objetivo

Avaliar a performance de APIs de forma programática
Medir métricas como tempo médio, P95, P99, taxa de erro e throughput
Comparar resultados com SLA definido
Gerar relatórios completos para análise detalhada

---

## 📊 Relatórios com .csv

Ao final da execução, é gerado um log em uma tabela .csv

- Informações do teste
- Número de usuários (threads)
- Repetições por usuário
- Ramp-up
- Duração do teste
- SLA definido
- Detalhes das requisições
- URL testada
- Número da repetição
- Tempo de resposta (ms)
- Status code
- Resumo por URL
- Tempo médio
- P95 e P99
- Taxa de erro
- Throughput (req/s)
- Resumo geral do teste
- Métricas consolidadas
- Resultado do SLA (PASSOU / FALHOU)

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia                		   | Finalidade                                    						   |
|------------------------------|---------------------------------------------------------|
| Java 17                  	 		   | Linguagem base                                   					|
| Maven                       		   | Gerenciador de dependências e build automation   |
| JUnit 4.13.2                	   | Framework de execução dos testes               			  |
| Apache HttpClient 5.2       | Realizar requisições HTTP/HTTPS              			    |
| Apache HttpClient Fluent 5.2| API fluente para simplificar chamadas HTTP       |
| Jackson Databind 2.15.2     | Serialização e desserialização JSON          		    |
| Commons CSV 1.10.0          | Geração e manipulação de arquivos CSV            |
| SLF4J Simple 2.0.9        		  | Logging simples         			                       			  |

---

## ▶️ Como Executar Localmente

### ✅ Pré-requisitos

- Java 17 instalado  
- Maven 3.8+ instalado  

### ▶️ Comando de execução

```bash
mvn clean test

```

## ⚙️ Integração Contínua

- **GitHub Actions**: Automatização via workflows YAML  


---

