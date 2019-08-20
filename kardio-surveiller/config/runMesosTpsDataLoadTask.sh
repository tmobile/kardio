#!/bin/sh
cd /surveiller
java -jar -Xms512m -Xmx4096m -Dlog4j.configuration=file:./log4j-mesosTpsDataLoad.properties kardio-surveiller.jar domesostpsdataload
                                                                           