
# Kardio Setup

This document explains the building blocks of Kardio and how to build and run Kardio form Code.

## Components

Kardio has three components:

*   **Kardio-UI**: This layer fetches the data from the API layer as ajax calls and renders it in the browser. The UI component can be deployed on a static web server or on AWS CloudFront.
*   **Kardio-API**: The API layer interacts with the backend database and provides data in JSON format for the UI layer. The API component is built as a Spring boot app and can run as a Docker container.
*   **Kardio-Surveiller**: The Surveiller runs periodically and performs health checks and purges old data. Surveiller is designed to run as Cron Jobs and is recommended to be run as a Docker container.

### Kardio-UI

The UI layer is separated from the API layer for making the **Kardio** application scalable. The UI code can be packaged as a war or can be deployed as a static site in any web server. The UI labels and the URL of the API layers are configurable so that the API layer can be deployed independently on a separate cluster/

The main pages of the UI layer are:

*   **Dashboard:** This is the landing page of the application which displays the current status of the configured services. 
*   **History:** This screen displays the service status for the past seven days for all the services.
*   **API Dashboard:** The API dashboard shows the number of services and containers/pods deployed on Kubernetes/Marathon as a graph for a selected time period. 

For more information about the UI screens, see the [Kardio User Guide](./user_guide.md).

 **Administration Page**: Using this page, the user can:

*   Add/Edit services/health check names
*   Add/Edit health check configurations
*   Add Global Subscriptions for all the services in an environment
*   Edit the environment descriptions and K8S/Marathon credentials
*   Configure the counters

### API

The API layer is a spring boot application that runs on JRE 1.7 or higher. This layer contains all the APIs to list or update Kardio data/configurations. The API layer is stateless (session independent) and can be scaled easily. 

### Surveiller

Surveiller is a java jar file that must be scheduled to perform the following tasks:

*   Perform the heath check for all the configured services and save the result in the database. This must be scheduled to run every minute.
*   Load the counter data from different sources and save it in the database. 
*   Pull the Marathon data for all the configured environments and load the database. Recommended to schedule every hour.
*   Delete old data from the database using the purge job. Recommended to schedule to run once per day.

Setting Up Kardio
-----------------

### Prerequisites to build and run Kardio

1.  JDK 1.7 or higher
2.  git client
3.  Apache Maven 3+
4. Tomcat 7.x (Optional)
5.  MySQL 5.6
6.  Docker

**Note:** You need to run the build and start commands either as a _root_ user or with _sudo_ permission.

### Getting the source code

> $ git clone [https://github.com/tmobile/kardio.git](https://github.com/tmobile/kardio.git) 

This will create the `kardio` directory along with the three sub-directories - `kardio-surveiller`, `kardio-api`, and `kardio-ui`.

### Configuring the Components

This section explains how to configure the components before building and running Kardio. 

##### Kardio-UI
The UI configuration is to be done in `kardio-ui/CloudMonitorClient/scripts/config/config.js` and `kardio-ui/CloudMonitorClient/scripts/properties/constants.js`. Most options are self explanatory and are changes are optional. The recommended changes are explained below.

The following variables in `kardio-ui/CloudMonitorClient/scripts/config/config.js` is used to point to the location at which the `kardio-api` component is deployed. These settings can be left as is if the API component is running on the same host as the UI component.
	
	apiHost="https://kardio.YOURDOMAIN.COM"
	apiPort="7070"
	
The `kardio-ui/CloudMonitorClient/scripts/properties/constants.js` is used to change the logo images, UI labels, table headers etc. You may need to change the following variable to get Kardio running, based on your requirements.

	// The Environment Tab that is selected when Dashboard is loaded. This has be an Environment that is present in the system.
	"defaultEnvironmentName":  "Staging"
				   


##### Kardio-API
The config file is located in `kardio-api/application.properties`  and needs to be edited before building and deploying Kardio. See [Integrations](./integrations.md) for more information on configurations for Slack, LDAP etc.

See example below:

	spring.datasource.url=jdbc:mysql://<MYSQL_HOST>:3306/<DB_NAME>   
    spring.datasource.username=<USER>   
    spring.datasource.password=<PASSWORD>

    login.enable.ldap=true   
    login.admin.username=<USER>   
    login.admin.password=<PASSWORD>   
    ldap.url=ldap://<LDAP_HOST>:<PORT>   
    ldap.domain=<DOMAIN>  
    ldap.searchBase=OU=\<OU>,DC=\<DC>,DC=\<DC>,DC=com      
    
    mail.subscribe.link=https://<KARDIO_DOMAIN>/validateemail.html
    mail.unsubscribe.link=https://<KARDIO_DOMAIN>/unsubscribe.html 
    mail.subscribe.valid.domain = <KARDIO_DOMAIN> 
    mail.from.email = <KARDIO_EMAIL> 
    
    mail.server.ip=<MAIL_SERVER>  
    mail.server.port=25      
    
    slack.url = https://<SLACK_DOMAIN>/api/slack.auth.token=<TOKEN>   
    slack.webhook.validateurl = https://hooks.slack.com/services/<***>

#####  Kardio-Surveiller

Configure the Surveiller component using the `kardio-surveiller/config.properties` file. See example below:

    db.url=jdbc:mysql://<MYSQL_HOST>:3306/<DB_NAME>
    db.username=<USER>
    db.password=<PASSWORD>
    
    surveiller.environment= OSS
    
    mail.task.exception.mail.to=admin@KARDIO_DOMAIN.com
    
    mail.server.ip=<MAIL_SERVER>  
    mail.server.port=25      
        
    slack.url = https://<SLACK_DOMAIN>/api/slack.auth.token=<TOKEN>   
    slack.webhook.validateurl = https://hooks.slack.com/services/<***>
    
    k8s.api.login.api = <K8S_LOGIN_URL>

### Setting Up the Database

Kardio supports any Relational Database that can work with Hibernate. Sample configurations are available for MySQL in the `kardio-surveiller/config.properties` and `kardio-api\application.properties`. Both property files need to be updated with your database endpoint and credentials.

Kardio is completely data driven and needs a set of tables and some data to operate. The DB scripts required to populate this data is available in `kardio-surveiller/sql` along with some sample data to demonstrate external health checks. Apply the `InitDB.sql` to apply the minimum required Data for Kardio to start.

## Building and Running Kardio
All instructions below are for a Linux based environment, but Kardio will build and run on Windows as well.

### Build
	
	git clone https://github.com/tmobile/kardio.git
	cd kardio
	
Edit any configuration files before continuing with the build. 
	
	./kardio build
	./kardio build-docker

The above procedure builds all the three components. You can also build individual components by executing the `build.sh` and `build_docker.sh` in each of the component sub directories.

### Start
Before starting, please make sure you have set-up a database as described above and the configuration files were updated with the DB endpoint and credentials before the build.

Start with Docker(Recommended)
	
	./kardio start_docker
	
This starts all the three components of Kardio in separate docker images. By default, the API will run on port 7070 and the UI on port 8080. You can also start individual components by executing the `./start-docker.sh` in each of the component sub directories.

### Stop

	./kardio stop

This stops all the three components. You can also build individual components by executing the `stop.sh` in each of the component sub directories.

### Log Files

The log files are located under `/var/log/kardio`. For the API component, a `kardio-api/kardio-api.out ` file exists, which may be useful when the start fails. For the UI, please check the Tomcat logs.

## Running from pre-built Docker Images
In case you want to try out Kardio without building from code, please follow these instructions.

Set Environment variable $MYSQL_ROOT_PASSWORD

	git clone https://github.com/tmobile/kardio.git
	cd kardio
	
	# Start a local MySQL instance. 
	docker run --name kardio-mysql -e MYSQL_ROOT_PASSWORD="$MYSQL_ROOT_PASSWORD" -d -p 3306:3306 mysql:5.6
	
	# Apply DB script
	
	docker exec -i kardio-mysql sh -c 'exec mysql -uroot -p"$MYSQL_ROOT_PASSWORD"' < ./kardio-surveiller/sql/InitDB.sql

    # Modify configuration to reflect local IP address instead of 'localhost'
    IP="$(hostname -I | cut -d' ' -f1)"
    sed -i "s/localhost/$IP/g" kardio-api/config/application.properties
    sed -i "s/localhost/$IP/g" kardio-surveiller/config/config.properties
	
	# Start 
	docker run -p 8080:80 -d tmobile/kardio-ui
	docker run -p 7070:7070 -v $PWD/kardio-api/config/application.properties:/kardio-api/config/application.properties -d tmobile/kardio-api
	docker run -v $PWD/kardio-surveiller/config/config.properties:/kardio-surveiller/config/config.properties -d tmobile/kardio-surveiller

Kardio will be available at `http://$IP:8080` where $IP is the IP of the host where the above commands were executed. Please note that running mysql in this way is not recommended. Please follow recommendations
    
Extension Points
----------------

### Health Check Handler

Kardio supports custom health checks through extensible Health Check Handlers. To implement a custom health check, extend the _getSurveillerStatus_ method in the Abstract class _SurveillerHandler_. See the sample implementations in the _com.tmobile.gdm.surveiller.handler_ package for more information.


