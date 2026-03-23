# Performance Test Automation

Automação de testes de performance para APIs simuladas, desenvolvida em Java 17.
O objetivo é aprender e explorar estratégias de teste e otimização diretamente via código, sem depender de ferramentas externas como JMeter, Gatling ou K6.
O projeto inclui execução automatizada via GitHub Actions e geração de relatórios em CSV.

---

🚀 Objetivo






---

## 📊 Relatórios com .csv

Ao final da execução, é gerado um log em uma tabela .csv


---

## 🛠️ Tecnologias Utilizadas

| Tecnologia                  | Finalidade                                       |
|-----------------------------|--------------------------------------------------|
| Java 17                     | Linguagem base                                   |
| Maven                       | Gerenciador de dependências e build automation   |
| JUnit 4.13.2                | Framework de execução dos testes                 |
| Apache HttpClient 5.2       | Realizar requisições HTTP/HTTPS                  |
| Apache HttpClient Fluent 5.2| API fluente para simplificar chamadas HTTP       |
| Jackson Databind 2.15.2     | Serialização e desserialização JSON              |
| Commons CSV 1.10.0          | Geração e manipulação de arquivos CSV            |
| SLF4J Simple 2.0.9          | Logging simples                                  |

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

