# Death Data Hub
This project is the central repository for standardizing death reporting in collaboration with EDRS systems, medical data providers, and parties interested in data exchange around death records.
This project is broken in 3 major parts.

1. DeathRecord_Puller.
	DeathRecord_Puller is a service for pulling records from a FHIR_enabled server.
	FHIR (Fast Healthcare Interoperability Resources) is a standard for exchanging healthcare information electronically.
2. DeathRecord_Puller/ecr_javalib
	DeathRecord_Puller then repackages the FHIR information into an Electronic Case Record (ECR) data model. This model has been provided as a java jar to be used from the DeathReecord_Puller
3. Frontend
	Frontend is a UI package used to display DeathRecord information from the local system.
	
## Installation

All projects are installed within docker containers, and services connected using docker-compose
### Requirements
* Docker versions 16 or higher
* Docker-compose version 2

To setup the Death Data Hub service, run
```
docker-compose build
docker-compose up &
```