#variable global que se declara despues del FROM y antes del siguiente en una compilacion de varias etapas
ARG MSVC_NAME=msvc-usuarios

# PRIMER IMAGEN, SE EJECUTA EL ENTORNO EN GENERAL DEL PROYECTO ->
FROM openjdk:17-jdk-alpine as builder

ARG MSVC_NAME

WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#RUN ./mvnw dependency:go-offline
COPY ./$MSVC_NAME/src ./src

RUN ./mvnw clean package -DskipTests

# IMAGEN msvc-usuarios ->
FROM openjdk:17-jdk-alpine

ARG MSVC_NAME

WORKDIR /app

#logs de la app
RUN mkdir ./logs

ARG TARGET_FOLDER=/app/$MSVC_NAME/target

#COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8001
#variable de ambiente (puerto interno)
ENV PORT $PORT_APP

#EXPOSE 8001
EXPOSE $PORT

#ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]

# CMD con pauta, delay para permitir que el servicio se levante despues de la BD y pueda conectar correctamente para aws ECS
#CMD sleep 20 & java -jar msvc-usuarios-0.0.1-SNAPSHOT.jar