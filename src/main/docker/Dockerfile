# A simple example showing how the vertx image can be used.

FROM java:8-jdk-alpine

# Set the location of the verticles
ENV VERTICLE_HOME /home/vertx
ENV VERTICLE_REPO simple-vertx-cluster
###
# The rest of the file should be fine.
###
RUN mkdir -p $VERTICLE_HOME
ADD ./verticles/*.tar.gz $VERTICLE_HOME
# We use the "sh -c" to turn around https://github.com/docker/docker/issues/5509 - variable not expanded
ENTRYPOINT ["sh", "-c"]
CMD ["sh $VERTICLE_HOME/$VERTICLE_REPO/simple-vertx-cluster.sh start"]