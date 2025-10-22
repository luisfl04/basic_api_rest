# *************** CONSTRUINDO PACOTE DA APLICAÇÃO *******************************************


# Imagens usadas na construção do container:
FROM maven:3.8.5-openjdk-17 AS build

# Diretório de trabalho da aplicação:
WORKDIR /app

# Copiando arquivos relacionados as dependências da aplicação:
COPY ./pom.xml .
COPY ./.mvn/ .mvn/
COPY ./src/ src/

# Empacontando a aplicação:
RUN mvn package -DskipTests


# ********************************* EXECUTANDO PACOTE *********************************************


# Imagem leve, apenas com jre
FROM openjdk:17-jdk-slim

# Diretório de trabalho
WORKDIR app/

# Copiando o arquivo '.jar' gerado na etapa de build:
COPY --from=build /app/target/basic_api_rest-1.0-SNAPSHOT.jar app.jar

# Definindo porta para acesso à aplicação:
EXPOSE 8080

# Executando pacote:
ENTRYPOINT ["java", "-jar", "app.jar"]






