#!/bin/sh
cd /surveiller
java -jar -Xms512m -Xmx6144m -Dlog4j.configuration=file:./log4j.properties kardio-surveiller.jar

