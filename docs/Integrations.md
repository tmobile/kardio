
## Integrations
Following are the integration points in Kardio
- Kubernetes/Marathon -To perform health checks on Services, Kardio
   integrates with Kubernetes and Marathon to auto-discover Health Check
   endpoints.  
- Prometheus - Kardio integrates Prometheus to get metrics
   shown in the Counters and Dashboard.  Slack/
- Email - For Alerting capability, Kardio needs to connect with Slack and an SMTP server.

This document describes how Kardio integrates with the various external systems and how to configure the integration.

### Kubernetes

Kardio integrates with Kubernetes to monitor all the services deployed on it.

#### Per Environment Configuration

Each Environment in Kardio can represent a Kubernetes Cluster. The Kubernetes endpoint and credentials can be added/edited using the Admin UI in the Environments Tab. 
![Add new Environment](./images/admin-addenv.png)
#### Common Configuration

-   Configure the Kubernetes endpoints and paths in kardio-surveiller/config.properties:  

> k8s.api.login.api =  https://auth.DOMAIN.com/v1/login
> k8s.api.path.deployment = /apis/apps/v1/deployments
> k8s.api.path.ingress = /apis/extensions/v1beta1/ingresses
> k8s.api.path.service = /api/v1/services
> k8s.api.path.pods = /api/v1/pods

#### Performing Health Checks

The Kubernetes backup job, a Surveiller job, reads all the services deployed in Kubernetes and stores the details in the database for further health monitoring of these services.

Each microservice in Kardio is uniquely identified using a combination of the namespace and deployment name (example: css-prd-duck-sd-w2/conducktor-api) in the Kubernetes cluster. The matchLabels in the deployment are compared with the selectors in the service to uniquely identify the services of that deployment. The specific service name is used to find the respective ingress for the service.

**Note:**  In case of services which are already in the system for the specific environment but not present in the JSON file (suspended or removed services), the system performs a soft delete; the services are not selected for performing health checks.  
  

**Scenario 1**

**Conditions:**  The application should have a service and ingress associated with it; the ingress should contain the host name. Also, the health check path should be configured in the liveness probe.

**Action:**  The system combines the host name in the ingress file and the health check path, and loads this URL to the respective table for further health check. The system performs the health check of the service once in every minute using the health check URL.  
  
**Scenario 2**

**Condition:**  The application has an ingress file, but the liveness probe does not contain a health check path associated with it.

**Action:**  The system takes the external host name as the health check URL and stores it in the database table. The health check is performed using this URL.

**  
Scenario 3**

**Condition:** The application does not have a service or an ingress file associated with it.

**Action:** The system checks the value of replicas in the _deployment.yaml_ file and marks the service as up or down accordingly.

### Marathon

Kardio integrates with Marathon to read all the services deployed on it. Kardio expects services to have a label named `HEALTHCHECK` label if they need be monitored. The value of the `HEALTHCHECK` label should be an HTTP endpoint that returns the health status of the service via HTTP status codes. The health check endpoints should be accessible by Kardio without authentication. Kardio checks the health check endpoint every minute to update the service status. If a health check fails three consecutive times, it is marked as unhealthy.

##### Configuring Marathon Integration

In order to get the list of services from Marathon, Kardio uses the Marathon Apps API. The Marathon API and Credentials can be added using the Admin UI in the Environments Tab. Each `Environment` can have its own Marathon cluster configured. This can be done when a new Environment is created or by editing an existing Environment. Admin UI is only accessible for Authenticated users. Please see [Authentication]() for more details

![Add new Environment](./images/admin-addenv.png)

The services in Marathon can be optionally categorized as Application Services and Platform Services(or Infra Components). To mark certain services as Platform services, edit the `transform.json` in `kardio-surveiller/config` and add the service name to the `convertToInfra` list.

##### Performing the health check

The Marathon backup job is a surveillor job that is run once in every one hour. This Cron job loads the application details into the database from the Marathon JSON file. This JSON file is stored in the database.

Script Path: _/bin/gdm/surveiller/runMarathonBackUp.sh_

The Marathon backup job is executed as follows:

This job calls the respective environment's Marathon APIs to get the Marathon application JSON file; the API URLs are configured in the marathon_url field in the Environment table. The JSON file contains the details of all the applications deployed on the specific environment. The following are the major steps involved in the iteration of the JSON file:

1.  The backup job finds the application name and service name from the JSON file by splitting the ID. The first three letters in the ID indicates the application name and the remaining part of the ID is the service name.
2.  For the health check URL, the system looks for `HEALTHCHECK` within the Labels. If the health check URL cannot be found, then the system checks for tasksRunning and tasksUnhealthy. The system compares the values of tasksRunning and tasksUnhealthy; if the task counts are equal, the service will be marked as down, if the count of tasksUnhealthy is 0, then the system marks the service as up. The data is stored into the respective database tables based on the above conditions.
3.  In case of services which are already in the system for the specific environment but not present in the JSON file (suspended or removed services), the system performs a soft delete; the services are not selected for performing health checks.

### Configuring Services Outside Kubernetes and Marathon*

Health check of services outside Kubernetes and Marathon can be achieved through manual configuration of the health check endpoint. Out of the box, Kardio supports health checks of REST endpoints, TCP Port checks, etc. External health checks are completely extendable using health check handlers (see  [Health Check Handlers](http://todo/)). Kardio provides an option to add multiple health checks per service if required.

## Slack Channel Integration

For a user to receive Slack channel alerts on subscribing, you need to perform the following configurations:

1.  **API Configuration:**  Slack APIs allow you to integrate complex services with Slack to go beyond the integrations that are provided out of the box.  
    To ensure subscription to a Slack channel, check the API configuration in the `kardio-api/config.properties` file and ensure that the Slack channel URL, the Slack channel authentication token and the Slack web hook validation URL are configured correctly:  
      
    slack.url =  https://TEAM.slack.com/api/
    slack.auth.token = abcd-1234567-1234567890123-12345678-abc123abc123abc1234  
    slack.webhook.validateurl =  https://hooks.slack.com/services/ABCD123  
      
    **Note:**  For the API to validate the URL, the Slack web hook validation URL should correspond to the Slack web hook URL generated by the user on the  **Global Subscriptions**  page, using the incoming web hooks page.  
      
    
2.  **Surveillor Configuration:**  In order to facilitate sending alerts when a service is down, configure the Slack channel URL and the Slack channel authentication token in `kardio-surveiller/config.properties`:  
      
    slack.url = https://TEAM.slack.com/api/
    slack.auth.token = abcd-1234567-1234567890123-12345678-abc123abc123abc1234  
