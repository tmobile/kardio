#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

if [[ -z "${CATALINA_HOME}" ]]; then
  echo "Tomcat not installed. kardio not running."
  exit
fi
$SUDO rm -rf $CATALINA_HOME/webapps/CloudMonitorClient.war
echo "Stopped kardio-ui"