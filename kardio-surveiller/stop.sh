#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

$SUDO crontab -l | grep -v 'surveiller' | $SUDO crontab -
echo "Stopped kardio-surveiller"
