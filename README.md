# simple-vertx-cluster

It's project to used in a [Rancher](http://rancher.com/rancher/) context.

This project compiles only on Java 8 and use [hazelcast-rancher-discovery-spi](https://github.com/nerdzOrNotToBe/hazelcast-rancher-discovery-spi.git).
 
### Build instructions
  - Clone the source:

        git clone https://github.com/nerdzOrNotToBe/simple-vertx-cluster.git

  - Build ( change to your private registry in pom.xml )

        mvn deploy


## Usage
Exemple of docker-compose.yml use in Rancher
```yaml
vertx2:
  environment:
    cluster-name: backend
    stack-name: vertx1
    environment-name: Default
    rancher-api: http://10.34.0.252:8080/v1
  labels:
    io.rancher.scheduler.affinity:container_label_soft_ne: io.rancher.stack_service.name=vertx2
    io.rancher.container.pull_image: always
  tty: true
  image: 10.34.0.252:5000/spotter/simple-vertx-cluster:1.0-SNAPSHOT
  stdin_open: true
``` 