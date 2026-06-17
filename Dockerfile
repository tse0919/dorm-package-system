# 第一階段：使用 Maven 進行編譯打包
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# 第二階段：使用輕量級 Java 環境執行程式
FROM eclipse-temurin:21-jre-jammy
# 密技：用 *.jar 自動匹配檔名，不管它叫什麼名字都能抓到
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]