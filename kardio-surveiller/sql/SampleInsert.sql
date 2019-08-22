INSERT INTO environment VALUES (14,'Dev','Development',NULL,NULL,NULL,NULL,NULL,NULL,0,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO component VALUES (15,'AWS',NULL,NULL,1,0,NULL,'Y'),(16,'Status Page',NULL,15,1,0,'K8s','Y'),(17,'Kube',NULL,NULL,1,0,NULL,'Y'),(18,'Kubernetes Page',NULL,17,1,0,'K8s','Y'),(19,'Kardio',NULL,NULL,1,0,NULL,'Y'),(20,'Kardio doc',NULL,19,1,0,'Mesos','Y');

INSERT INTO health_check VALUES (1,16,1,1,1,3,0,1,NULL,NULL,0,NULL),(2,18,1,1,1,3,0,NULL,NULL,NULL,0,NULL),(3,16,1,14,1,3,0,NULL,NULL,NULL,0,NULL),(4,18,1,14,1,3,0,NULL,NULL,NULL,0,NULL),(5,20,1,1,1,3,0,NULL,NULL,NULL,0,NULL);

INSERT INTO health_check_param VALUES (1,1,'URL','https://status.aws.amazon.com/'),(2,2,'URL','https://kubernetes.io/'),(3,3,'URL','https://status.aws.amazon.com/'),(4,4,'URL','https://kubernetes.io/'),(5,5,'URL','http://kardio.io/');

