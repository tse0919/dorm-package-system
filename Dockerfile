# 第一階段：使用 Maven 進行編譯打包
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# 第二階段：使用輕量級 Java 環境執行程式
FROM eclipse-temurin:21-jre-jammy
COPY --from=build /target/package-collection-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]