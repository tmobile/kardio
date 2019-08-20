#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

$SUDO docker run -i -d -p 7070:7070 kardio-api
