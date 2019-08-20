#!/bin/sh
cd /surveiller
java -jar -Xms512m -Xmx4096m -Dlog4j.configuration=file:./log4j-k8sTpsDataLoad.properties kardio-surveiller.jar dok8stpsdataload
                                                                           
