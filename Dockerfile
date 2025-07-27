# =================================================================
# 阶段 1: Builder - (保持不变)
# =================================================================
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY settings.xml .
COPY pom.xml .
RUN mvn -s settings.xml dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# =================================================================
# 阶段 2: Exploded - 展开 JAR 包并创建 CDS 归档
# =================================================================
FROM eclipse-temurin:17-jre-alpine AS cds_creator
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# --- 改动：使用 -XX:SharedArchiveFile 参数明确指定输出文件 ---
RUN java -Xshare:dump -XX:SharedArchiveFile=app.jsa -cp . org.springframework.boot.loader.launch.JarLauncher && \
    echo "CDS archive command finished. Listing files:" && \
    ls -l

# =================================================================
# 阶段 3: Final - (保持不变)
# =================================================================
FROM eclipse-temurin:17-jre-alpine AS final
WORKDIR /app

COPY --from=cds_creator /app/app.jsa .
COPY --from=cds_creator /app/dependencies/ ./
COPY --from=cds_creator /app/spring-boot-loader/ ./
COPY --from=cds_creator /app/snapshot-dependencies/ ./
COPY --from=cds_creator /app/application/ ./

ENTRYPOINT ["java", "-Xshare:on", "-XX:MaxRAMPercentage=75", "org.springframework.boot.loader.launch.JarLauncher"]