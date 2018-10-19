# FHIR Controller

This project is a prototype FHIR_Controller for the CDC-DDH project. It uses
the domain services to pull a list of FHIR endpoints, and searches for relvant
patient information for each of them. the FHIR_Controller also has scheduling
capabilities in order to repeatable check the endpoints for any new information

## Requirements

FHIR_Controller uses crontab for it's scheduling component, and thus must be installed upon a linux machine. FHIR_Controller is written using annotation based springframework-boot, and uses a postgres database backend. We use maven to compile the project, and deploy the war artifact in either tomcat 7 or tomcat 8.

## Installing

Please ensure that maven and java-jdk 7.0 or higher is installed

```
sudo apt-get install -y java-jdk-8 maven
```

FHIR_Controller relies on a custom common library called ecr_javalib that must also be built
```
cd ecr_javalib
mvn clean install -DskipTests
```

Once the common library is built, compile the main project.
```
cd ..
mvn clean install -DskipTests
```

To deploy the project, copy the war artifact into your tomcat webapp directoy
```
cp target/FHIR_Controller-0.0.1-SNAPSHOT.war $CATALINA_BASE/webapps
```

## Configuring

If you are handling network configuration manually, you must edit the main configuration file found at src/main/resources/application.properties
```
#These are the connection information to the database that contains the ECRJob information
spring.datasource.url = jdbc:postgresql://localhost:5432/OMOP # This must change to your local database 
spring.datasource.username = postgres
spring.datasource.password = postgres
spring.datasource.driver-class-name = org.postgresql.Driver
spring.jpa.hibernate.ddl-auto = validate
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect

#The location of the crontab folder that contains the cron jobs to run
scheduler.scriptFolder = ~/crontab-utilities
#User who will register the crons
scheduler.scriptUser = root

#WebURL context patch such as www.myserver.com/FHIRController
server.contextPath = /FHIRController

# For single endpoint connections
FHIR.client.serverBaseUrl = http://smart-wip.hdap.gatech.edu:8080/gt-fhir-webapp/base
# The base URL for the PHCR_Controller
PHCR.client.serverBaseURL = http://localhost:8888/PHCR_Controller-0.0.1-SNAPSHOT

#The URL to the domainservices machine
domainservices.url = http://domainservice:80/
#A username, password combo to login to the domain services
domainservices.passwords.myDSUser = myDSPassword

#Common logging packages
logging.level.gatech.edu.FHIRController=DEBUG
logging.level.gatech.edu.common=DEBUG
```

### REST API

In order to manually request a FHIR collection, you may run
```
GET http://www.fhircontroller.com/FHIRGET?id=1
```

In order to schedule a new job, simply POST to the same endpoint with a cronstring (https://crontab.guru/)
```
POST http://www.fhircontroller.com/FHIRGET?id=1&cron="0 0 * * *"
```
