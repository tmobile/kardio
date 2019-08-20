#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

$SUDO docker run -i -d -p 80:80 --name kardio-ui kardio-ui
