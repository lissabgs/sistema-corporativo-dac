# Sistema de E-learning Corporativo - Backend (Microsserviços)

Este pacote contém todos os microsserviços backend do sistema de E-learning Corporativo desenvolvido em **Java 21 / Spring Boot 3.3+** e um **API Gateway em Node.js**.

---

## 🧩 Estrutura do Projeto

```
backend/
├── api-gateway/        # Node.js com Express - roteamento e autenticação
├── ms-autenticacao/    # MongoDB - autenticação e tokens
├── ms-usuarios/        # PostgreSQL - funcionários, instrutores e departamentos
├── ms-cursos/          # PostgreSQL - cursos, módulos e materiais
├── ms-avaliacoes/      # PostgreSQL - avaliações e tentativas
├── ms-progresso/       # PostgreSQL - progresso, certificados e trilhas
├── ms-gamificacao/     # PostgreSQL + Redis - XP, badges e ranking
├── ms-notificacoes/    # MongoDB - envio e armazenamento de notificações
└── docker-compose.yml  # Orquestração de containers
```

---

## ⚙️ Pré-requisitos

- **Java 21** instalado  
- **Maven 3.9+**  
- **Docker** e **Docker Compose** instalados  
- **Node.js 20+** (caso queira executar o API Gateway fora do Docker)

---

## 🚀 Passos para Executar o Sistema Completo

### 1️⃣ Compilar os microsserviços Java

Entre em cada diretório de microserviço Spring Boot e rode o comando:

```bash
mvn clean package -DskipTests
```

Isso vai gerar um arquivo `.jar` dentro de `target/` para cada um.

---

### 2️⃣ Subir toda a stack com Docker Compose

Na raiz do projeto (onde está o `docker-compose.yml`), execute:

```bash
docker-compose up --build
```

Isso vai subir os seguintes containers:

- `postgres` (banco de dados principal)
- `mongo` (banco de autenticação e notificações)
- `redis` (cache para gamificação)
- Todos os microsserviços Java
- `api-gateway` (Node.js, porta 8080)

---

### 3️⃣ Acessar o sistema

Com tudo rodando, você pode testar os serviços individualmente:

| Serviço | Porta | URL base |
|----------|--------|----------|
| API Gateway | 8080 | http://localhost:8080 |
| Autenticação | 8081 | http://localhost:8081/health |
| Usuários | 8082 | http://localhost:8082/health |
| Cursos | 8083 | http://localhost:8083/health |
| Avaliações | 8084 | http://localhost:8084/health |
| Progresso | 8085 | http://localhost:8085/health |
| Gamificação | 8086 | http://localhost:8086/health |
| Notificações | 8087 | http://localhost:8087/health |

---

## 🧪 Testar manualmente

Para verificar se tudo está ok, acesse no navegador ou via `curl`:

```bash
curl http://localhost:8081/health
```

Deve retornar algo como:

```
OK - com.dac.autenticacao
```

---

## 🧰 Dicas

- Todos os microsserviços têm um endpoint `/health` para verificar se estão ativos.
- As conexões entre serviços já estão configuradas no `api-gateway/index.js`.
- Ajuste credenciais e URIs no `application.yml` conforme o ambiente (produção, testes, etc).
- Para parar os containers:

```bash
docker-compose down
```

---

## 👨‍💻 Autor
Arthur Dias Baptista - UFPR - TADS - DS152  
Desenvolvimento de Aplicações Corporativas (DAC) - 2025
