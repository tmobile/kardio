#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

$SUDO docker build -t kardio-surveiller .
