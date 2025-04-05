# 使用官方OpenJDK基础镜像
FROM amazoncorretto:17-al2023-jdk

# 设置工作目录
WORKDIR /app

# 复制JAR文件到容器中
COPY target/valueline-web-1.0-SNAPSHOT.jar /app/valueline-web-1.0-SNAPSHOT.jar

# 暴露应用端口（根据你的应用实际情况修改）
EXPOSE 8081

# 设置JVM参数（可选）
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动命令
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/valueline-web-1.0-SNAPSHOT.jar"]