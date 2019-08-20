#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

$SUDO kill -9 `cat kardio-api.pid`
$SUDO rm -f kardio-api.pid
echo "Stopped kardio-api"
