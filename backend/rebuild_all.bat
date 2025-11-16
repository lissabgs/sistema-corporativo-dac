@echo off
ECHO =================================================
ECHO SCRIPT DE BUILD COMPLETO (DAC - BACKEND)
ECHO Este script vai limpar, compilar (pulando testes)
ECHO e recriar todos os containers do Docker.
ECHO =================================================

ECHO.
ECHO --- PASSO 1: COMPILANDO MICROSSERVICOS MAVEN ---
ECHO.

ECHO Compilando ms-autenticacao...
cd ms-autenticacao
call mvn clean package -DskipTests
cd ..

ECHO Compilando ms-usuarios...
cd ms-usuarios
call mvn clean package -DskipTests
cd ..

ECHO Compilando ms-cursos...
cd ms-cursos
call mvn clean package -DskipTests
cd ..

ECHO Compilando ms-avaliacoes...
cd ms-avaliacoes
call mvn clean package -DskipTests
cd ..

ECHO Compilando ms-progresso...
cd ms-progresso
call mvn clean package -DskipTests
cd ..

ECHO Compilando ms-gamificacao...
cd ms-gamificacao
call mvn clean package -DskipTests
cd ..

ECHO Compilando ms-notificacoes...
cd ms-notificacoes
call mvn clean package -DskipTests
cd ..

ECHO.
ECHO --- PASSO 2: REINICIANDO DOCKER COMPOSE ---
ECHO.
ECHO Parando e removendo volumes antigos (docker-compose down -v)...
docker-compose down -v

ECHO.
ECHO Subindo e recriando todos os containers (docker-compose up --build)...
docker-compose up --build

ECHO.
ECHO =================================================
ECHO PROCESSO CONCLUIDO!
ECHO =================================================
pause