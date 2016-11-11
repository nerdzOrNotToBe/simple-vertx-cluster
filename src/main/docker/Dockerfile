# A simple example showing how the vertx image can be used.

FROM java:8-jdk-alpine

# Set the location of the verticles
ENV VERTICLE_HOME /home/vertx
ENV VERTICLE_REPO simple-vertx-cluster
ENV APPLICATION_JAR="simple-vertx-cluster-1.0-SNAPSHOT-fat.jar"
ENV APPLICATION_ARGS="-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Dlog4j.configuration=file:./sl4j.properties"
ENV VERTX_OPTS="-conf config.json"
###
# The rest of the file should be fine.
###
RUN mkdir -p $VERTICLE_HOME
ADD ./verticles/*.tar.gz $VERTICLE_HOME
WORKDIR $VERTICLE_HOME/$VERTICLE_REPO
# We use the "sh -c" to turn around https://github.com/docker/docker/issues/5509 - variable not expanded
ENTRYPOINT ["sh", "-c"]
CMD ["java $APPLICATION_ARGS -jar $APPLICATION_JAR $VERTX_OPTS"]