#!/bin/bash

# Diretório base do projeto
BASE_DIR=$(pwd)
ERRORS=0  # Variável para rastrear erros
declare -a ERROR_MESSAGES  # Array para armazenar mensagens de erro


# Remove o diretório volume do localstack
echo "🗑️ Removendo diretório volume do LocalStack..."
rm -rf ./volume
echo "✅ Diretório volume removido com sucesso"
echo "-------------------------------------------"

# Inicia o docker-compose
echo "🐳 Iniciando serviços Docker Compose..."
docker-compose up -d
echo "✅ Serviços Docker Compose iniciados com sucesso"
echo "-------------------------------------------"

# Aguarda o LocalStack inicializar completamente
echo "⏳ Aguardando LocalStack inicializar (5 segundos)..."
sleep 5
echo "✅ LocalStack inicializado"
echo "-------------------------------------------"

# Pergunta ao usuário se deseja destruir a infraestrutura antes de aplicar
read -p "Deseja destruir a infraestrutura existente antes de aplicar as mudanças? (s/n): " DESTROY_INFRA

manage_terraform() {
    local dir=$1
    local description=$2

    echo "🔧 Gerenciando Terraform em: $dir ($description)"
    cd "$BASE_DIR/$dir" || return 1

    if [ "$DESTROY_INFRA" == "s" ]; then
            echo "🗑️ Removendo arquivos e diretórios Terraform..."
            rm -rf .terraform
            rm -f .terraform*
            rm -f terraform.*
            echo "✅ Arquivos Terraform removidos com sucesso"
    fi

    echo "Inicializando Terraform..."
    if ! tflocal init; then
        ERROR_MESSAGES+=("❌ Falha ao inicializar Terraform em: $dir")
        ((ERRORS++))
        return 1
    fi

    echo "Aplicando configuração..."
    if ! tflocal apply -auto-approve; then
        ERROR_MESSAGES+=("❌ Falha ao aplicar configuração em: $dir")
        ((ERRORS++))
        return 1
    fi

    echo "Reaplicando para garantir as tags (segunda execução)..."
    if ! tflocal apply -auto-approve; then
        ERROR_MESSAGES+=("❌ Falha ao reaplicar configuração em: $dir")
        ((ERRORS++))
        return 1
    fi

    echo "✅ Terraform gerenciado com sucesso em: $dir"
    echo "-------------------------------------------"
    return 0
}

# Deploy do ECR primeiro
echo "🚀 Criando repositórios ECR..."
manage_terraform "../infra-ecr" "Repositórios ECR"

# Configuração das Lambdas com seus respectivos caminhos do Dockerfile
declare -A LAMBDA_CONFIGS=(
    ["../lambda-client-car-creation-event-processor"]="lambda-creation-event-processor:lambda"
    ["../lambda-fetch-car-info"]="lambda-fetch-car-info:src"
    ["../lambda-fetch-client-info"]="lambda-fetch-client-info:src"
    ["../lambda-invoice-processor"]="lambda-invoice-processor:src"
    ["../lambda-start-workflow"]="lambda-start-workflow:src"
    ["../lambda-send-mail"]="lambda-send-mail:src"
)


# Função para fazer build e push da imagem Lambda
build_and_push_lambda() {
    local lambda_dir=$1
    local config=$2

    # Separa o nome da imagem e o caminho do Dockerfile
    IFS=':' read -r image_name dockerfile_path <<< "$config"

    echo "🏗️ Building e pushing Lambda: $image_name (Dockerfile path: $dockerfile_path)"

    # Vai para o diretório da Lambda
    cd "$BASE_DIR/${lambda_dir}" || exit

    # Build da imagem (usando o caminho específico do Dockerfile)
    echo "Building Docker image..."
    docker build -t "joaovictorsg/$image_name" "./$dockerfile_path"

    # Tag da imagem
    echo "Tagging image..."
    docker tag "joaovictorsg/$image_name:latest" "000000000000.dkr.ecr.us-east-1.localhost.localstack.cloud:4566/joaovictorsg/$image_name:latest"

    # Push da imagem
    echo "Pushing image..."
    docker push "000000000000.dkr.ecr.us-east-1.localhost.localstack.cloud:4566/joaovictorsg/$image_name:latest"

    echo "✅ Lambda $image_name built and pushed successfully"
    echo "-------------------------------------------"
}


# Build e push de todas as Lambdas
echo "🚀 Iniciando build e push das Lambdas..."
for lambda_dir in "${!LAMBDA_CONFIGS[@]}"; do
    build_and_push_lambda "$lambda_dir" "${LAMBDA_CONFIGS[$lambda_dir]}"
done


# Lista de diretórios de infraestrutura
INFRA_DIRS=(
    "../infra-vpc"
    "../infra-secrets"
    "../infra-parameters"
    "../infra-databases"
    "../infra-s3"
    "../infra-sqs"
    "../infra-sns"
    "../infra-ses"
    "../lambda-client-car-creation-event-processor/infra"
    "../lambda-fetch-car-info/infra"
    "../lambda-fetch-client-info/infra"
    "../lambda-invoice-processor/infra"
    "../lambda-send-mail/infra"
    "../car-api/infra"
    "../client-api/infra"
    "../sales-api/infra"
    "../infra-step-function"
    "../lambda-start-workflow/infra"
)

# Executa tflocal apply em cada diretório na ordem especificada
for dir in "${INFRA_DIRS[@]}"; do
    if ! manage_terraform "$dir" "Infraestrutura"; then
        echo "⚠️ Houve um erro no diretório $dir, mas continuando com o próximo..."
        continue
    fi
done

# Relatório final
echo "-------------------------------------------"
echo "📋 Relatório Final de Execução"
echo "-------------------------------------------"

if [ $ERRORS -gt 0 ]; then
    echo "🚨 Processo completado com $ERRORS erro(s)!"
    echo "Lista de erros encontrados:"
    for error in "${ERROR_MESSAGES[@]}"; do
        echo "$error"
    done
else
    echo "🎉 Todas as implantações foram concluídas com sucesso!"
fi

exit $ERRORS