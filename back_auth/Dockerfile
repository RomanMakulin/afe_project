FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/auth-0.0.1-SNAPSHOT.jar app.jar

# Копируем truststore в контейнер
COPY resources/truststore.jks /app/truststore.jks

EXPOSE 8095

# Устанавливаем переменную окружения для использования truststore
ENV JAVA_OPTS="-Duser.timezone=Europe/Moscow -Djavax.net.ssl.trustStore=/app/truststore.jks -Djavax.net.ssl.trustStorePassword=changeit"

# Запускаем приложение
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]