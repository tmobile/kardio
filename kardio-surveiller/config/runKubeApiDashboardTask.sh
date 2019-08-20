#!/bin/sh
cd /surveiller
java -jar -Xms512m -Xmx6144m -Dlog4j.configuration=file:./log4j-kube-apiDashboard.properties kardio-surveiller.jar doK8sApiDashboardTask
