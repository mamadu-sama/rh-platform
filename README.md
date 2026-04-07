# RH Platform: Sistema de Registo de Horas

Este projeto é um portal de recursos humanos desenvolvido para registar as horas dos funcionários de uma empresa, calcular horas extras e gerar relatórios mensais de pagamentos. É ideal para demonstração de competências no desenvolvimento de APIs RESTful utilizando a arquitetura em camadas.

## Funcionalidades

* **Gestão de Funcionários:** Criação e listagem de funcionários com definição da respetiva taxa horária.
* **Registo de Horas:** Registo diário de horas trabalhadas por cada funcionário.
* **Cálculo de Horas Extras:** O sistema assume uma jornada de 8 horas diárias. Qualquer valor superior é contabilizado como hora extra (com um acréscimo de 50% na taxa horária).
* **Relatórios Mensais:** Geração de relatórios agregados por mês, detalhando o total de horas regulares, horas extras e o montante total a pagar ao funcionário.

## Tecnologias Utilizadas

* **Java 17:** Linguagem de programação principal.
* **Spring Boot 3.2.4:** Framework para construção da aplicação e da API REST.
* **Spring Data JPA:** Abstração para persistência de dados.
* **PostgreSQL:** Base de dados relacional.
* **Docker e Docker Compose:** Ferramentas para contentorização da base de dados e simplificação do ambiente local.
* **Lombok:** Biblioteca para reduzir código repetitivo (getters, setters, construtores).

## Arquitetura do Projeto

O projeto segue uma arquitetura clássica dividida em camadas, garantindo a separação de responsabilidades:

* `controller`: Camada de exposição da API REST. Recebe os pedidos HTTP e devolve as respostas adequadas.
* `service`: Camada onde reside a lógica de negócio e os cálculos de horas e salários.
* `repository`: Interfaces de acesso à base de dados (Spring Data JPA).
* `model` (ou Entity): Classes de domínio que mapeiam diretamente as tabelas na base de dados.
* `dto` (Data Transfer Object): Objetos utilizados para transferir dados entre o cliente e a API, validando o formato de entrada.

## Como Executar Localmente

### Pré-requisitos
* Java 17 instalado
* Maven instalado
* Docker e Docker Compose instalados

### Passo a passo

1. **Clonar e configurar a Base de Dados**
   Aceda à raiz do projeto e inicie o contentor PostgreSQL (configurado para a porta `5434`):
   ```bash
   docker-compose up -d
   ```

2. **Compilar a Aplicação**
   Utilize o Maven para construir o projeto e descarregar as dependências:
   ```bash
   mvn clean install
   ```

3. **Executar a Aplicação**
   Pode iniciar a aplicação através do Maven:
   ```bash
   mvn spring-boot:run
   ```
   **Nota:** Se não tiver o Maven instalado na sua máquina, pode executar a aplicação diretamente através da sua IDE (IntelliJ IDEA, Eclipse, VS Code ou Trae). Basta abrir a classe `RhPlatformApplication.java` e clicar no botão "Run" (ou "Play") ao lado do método `main`.
   
   A API ficará disponível em `http://localhost:8080`.

## Documentação da API (Endpoints)

Abaixo encontra-se a referência completa das rotas disponíveis no sistema. Pode testar estes endpoints utilizando ferramentas como Insomnia ou Postman. Todos os pedidos são feitos para o caminho base `http://localhost:8080`.

### 👥 Funcionários (`/api/employees`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| **POST** | `/api/employees` | Cria um novo funcionário |
| **GET** | `/api/employees` | Retorna a lista de todos os funcionários |
| **GET** | `/api/employees/{id}` | Retorna os detalhes de um funcionário específico através do ID |
| **PUT** | `/api/employees/{id}` | Atualiza os dados de um funcionário existente |
| **DELETE** | `/api/employees/{id}` | Remove um funcionário do sistema |

**Exemplo de Pedido: Criar Funcionário (POST)**
```json
{
  "name": "João Silva",
  "email": "joao.silva@example.com",
  "hourlyRate": 15.50
}
```

### ⏱️ Registo de Horas (`/api/time-records`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| **POST** | `/api/time-records` | Regista horas trabalhadas para um funcionário |
| **GET** | `/api/time-records/{id}` | Retorna os detalhes de um registo de horas pelo seu ID |
| **GET** | `/api/time-records/employee/{employeeId}` | Retorna os registos de horas num intervalo de datas (requer query params `startDate` e `endDate`) |
| **PUT** | `/api/time-records/{id}` | Atualiza os dados de um registo de horas existente |
| **DELETE** | `/api/time-records/{id}` | Remove um registo de horas do sistema |

**Exemplo de Pedido: Registar Horas (POST)**
```json
{
  "employeeId": 1,
  "date": "2024-05-10",
  "hoursWorked": 10.0
}
```
*(Nota: Neste exemplo, o funcionário trabalhou 10 horas, resultando em 8 horas regulares e 2 horas extras).*

**Exemplo de Pedido: Buscar Registos por Data (GET)**
`GET /api/time-records/employee/1?startDate=2024-05-01&endDate=2024-05-31`

### 📊 Relatórios (`/api/reports`)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| **GET** | `/api/reports/monthly/{employeeId}` | Gera o relatório de pagamentos (requer query params `year` e `month`) |

**Exemplo de Pedido: Obter Relatório Mensal (GET)**
`GET /api/reports/monthly/1?year=2024&month=5`

**Resposta Esperada (JSON):**
```json
{
  "employeeId": 1,
  "employeeName": "João Silva",
  "month": "2024-05",
  "totalRegularHours": 8.0,
  "totalOvertimeHours": 2.0,
  "totalAmount": 170.50
}
```

## Tratamento de Erros

A API possui um mecanismo centralizado (`GlobalExceptionHandler`) para capturar exceções, retornando mensagens de erro claras e códigos de estado HTTP adequados em caso de validações falhadas (ex: email duplicado, formato inválido).



Este projeto demonstra boas práticas de engenharia de software, separação de responsabilidades e proficiência no ecossistema Spring Boot.
