#!/bin/bash
APPLICATION_DIR="."
APPLICATION_JAR="simple-vertx-cluster-1.0-SNAPSHOT-fat.jar"
APPLICATION_ARGS="-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -Dlog4j.configuration=file:./sl4j.properties"
VERTX_OPTS="-conf config.json"
JAVA="java"
OUT_FILE="${APPLICATION_DIR}"/out.log
RUNNING_PID="${APPLICATION_DIR}"/RUNNING_PID
red='\e[0;31m'
green='\e[0;32m'
yellow='\e[0;33m'
reset='\e[0m'
echoRed() { echo -e "${red}$1${reset}"; }
echoGreen() { echo -e "${green}$1${reset}"; }
echoYellow() { echo -e "${yellow}$1${reset}"; }
isrunning() {
  # Check for running app
  if [ -f "$RUNNING_PID" ]; then
    proc=$(cat $RUNNING_PID);
    if /bin/ps --pid $proc 1>&2 >/dev/null;
    then
      return 0
    fi
  fi
  return 1
}
start() {
  if isrunning; then
    echoYellow "The Vert.x application is already running"
    return 0
  fi
  pushd $APPLICATION_DIR > /dev/null
  nohup $JAVA $APPLICATION_ARGS -jar $APPLICATION_JAR $VERTX_OPTS> $OUT_FILE 2>&1 &
  echo $! > ${RUNNING_PID}
  popd > /dev/null
  if isrunning; then
    echoGreen "Vert.x Application started"
    exit 0
  else
    echoRed "The Vert.x Application has not started - check log"
    exit 3
  fi
}
restart() {
  echo "Restarting Vert.x Application"
  stop
  start
}
stop() {
  echoYellow "Stopping Vert.x Application"
  if isrunning; then
    kill `cat $RUNNING_PID`
    rm $RUNNING_PID
  fi
}
status() {
  if isrunning; then
    echoGreen "Vert.x Application is running"
  else
    echoRed "Vert.x Application is either stopped or inaccessible"
  fi
}
case "$1" in
start)
    start
;;

status)
   status
   exit 0
;;

stop)
    if isrunning; then
	stop
	exit 0
    else
	echoRed "Application not running"
	exit 3
    fi
;;

restart)
    stop
    start
;;

*)
    echo "Usage: $0 {status|start|stop|restart}"
    exit 1
esac