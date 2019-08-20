#!/bin/sh
cd /surveiller
java -jar -Xms512m -Xmx1024m -Dlog4j.configuration=file:./log4j-purge.properties kardio-surveiller.jar doPurgeOldData

