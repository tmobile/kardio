#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

rm -rf dist
mkdir dist
$SUDO mkdir -p /kardio-api

cp target/kardio-api.jar dist/
$SUDO cp -r config /kardio-api/

#sudo needed to open port
$SUDO java -jar dist/kardio-api.jar > kardio-api.out 2>&1 &
echo "Started kardio-api."
