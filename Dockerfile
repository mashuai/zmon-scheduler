FROM registry.opensource.zalan.do/stups/openjdk:latest

EXPOSE 8085 5005

COPY target/zmon-scheduler-1.0-SNAPSHOT.jar /zmon-scheduler.jar
COPY target/scm-source.json /

CMD java $JAVA_OPTS $(java-dynamic-memory-opts) -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar zmon-scheduler.jar
