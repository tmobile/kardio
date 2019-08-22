#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

if [[ -z "${CATALINA_HOME}" ]]; then
  echo "Install Tomcat and set '\$CATALINA_HOME'."
  exit
fi
$SUDO mkdir -p /kardio-ui/keystore
$SUDO cp tomcat-conf/shd_keystore.jks /kardio-ui/keystore/ 
$SUDO cp tomcat-conf/*.xml $CATALINA_HOME/conf/
$SUDO sed -i "s|{CATALINA_HOME}|$CATALINA_HOME|g" "$CATALINA_HOME/conf/server.xml"
$SUDO cp target/CloudMonitorClient.war $CATALINA_HOME/webapps/
$SUDO systemctl restart tomcat
echo "Started kardio-ui"