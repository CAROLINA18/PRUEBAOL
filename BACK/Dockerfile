#  imagen base de Java
FROM openjdk:17-jdk-slim

#  directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado por Spring Boot al contenedor
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que la aplicación Spring Boot se ejecuta
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]