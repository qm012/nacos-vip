FROM java:8

MAINTAINER tang.xiaosheng@qq.com

ENV TZ=Asia/Shanghai \
    BASE_DIR="/home/nacos-vip" \
    JVM_XMS="1g" \
    JVM_XMX="2g" \
    JVM_XMN="1g"

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR $BASE_DIR

ADD ./target/nacos-vip-1.0.2.jar ./nacos-vip.jar

RUN mkdir -p logs \
	&& cd logs \
	&& touch catalina.out \
	&& ln -sf /dev/stdout catalina.out \
	&& ln -sf /dev/stderr catalina.out

EXPOSE 8849

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPT} -Xms${JVM_XMS} -Xmx${JVM_XMX} -Xmn${JVM_XMN} -Djava.security.egd=file:/dev/./urandom -jar nacos-vip.jar > logs/catalina.out "]