# 基础镜像
FROM openjdk:17-slim

# 作者
LABEL maintainer="Aseubel"

# 配置

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 添加应用
ADD target/weave-app.jar /weave-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /weave-app.jar $PARAMS"]