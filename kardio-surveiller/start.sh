#!/bin/sh
SUDO=''
if (( $EUID != 0 )); then
    SUDO='sudo'
fi

rm -rf dist
mkdir dist
cp config/* dist/
cp target/kardio-surveiller.jar dist/
cp target/dependency-jars dist/
pwd=`pwd`
sed -i "s|/surveiller|$pwd/dist|g" "dist/surveiller-cron.jobs"

for filename in dist/*.sh; do
    [ -e "$filename" ] || continue
    sed -i "s|/surveiller|$pwd/dist|g" "$filename"
done

$SUDO cp dist/surveiller-cron.jobs /etc/cron.d/surveiller-cron
$SUDO crontab -l > existing_cron
$SUDO cp existing_cron new_cron
$SUDO bash -c "grep 'surveiller' dist/surveiller-cron.jobs >> new_cron"
$SUDO crontab new_cron
echo "Started kardio-surveiller."
