package gatech.edu.common.FHIR.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Claim;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu2.resource.Coverage;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Procedure;
import ca.uhn.fhir.model.dstu2.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.gclient.ICreateTyped;
import ca.uhn.fhir.rest.gclient.IParam;

@Service
@Configuration
@ConfigurationProperties(prefix="FHIR.writeclient")
public class WriteClientService extends ClientService{
	
	public Bundle writeResourcesWithTransactionBundle(Bundle bundle) {
		bundle.setType(BundleTypeEnum.TRANSACTION);
		Bundle transactionResponse = client.transaction()
		.withBundle(bundle)
		.execute();
		return transactionResponse;
	}
	
	public Bundle writeResourcesWithBatchBundle(Bundle bundle) {
		bundle.setType(BundleTypeEnum.BATCH);
		Bundle batchResponse = client.transaction()
		.withBundle(bundle)
		.execute();
		return batchResponse;
	}
	
	public void writeCondition(Condition condition) {
		ICreateTyped createOperation = client.create()
		.resource(condition);
		List<RestResourceSearchParam> SPcapabilities = resourceCapabilityMap.get(Condition.RES_ID).getSearchParam();
		
		if(!condition.getId().isEmpty() && SPcapabilities.contains(Condition.IDENTIFIER)) {
			createOperation.conditional()
			.where(Condition.IDENTIFIER.exactly().identifier(condition.getIdentifierFirstRep()));
		}
		createOperation.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeClaim(Claim claim) {
		
		client.create()
		.resource(claim)
		.execute();
	}
		
	//TODO: Add conditional writes based on identifier
	public void writeCoverage(Coverage coverage) {
		client.create()
		.resource(coverage)
		.execute();
	}
		
	//TODO: Add conditional writes based on identifier
	public void writeEncounter(Encounter encounter) {
		client.create()
		.resource(encounter)
		.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeImmunization(Immunization immunization) {
		client.create()
		.resource(immunization)
		.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeMedicationAdministration(MedicationAdministration medicationAdministration) {
		client.create()
		.resource(medicationAdministration)
		.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeMedicationDispense(MedicationDispense medicationDispense) {
		client.create()
		.resource(medicationDispense)
		.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeMedicationOrder(MedicationOrder medicationOrder) {
		client.create()
		.resource(medicationOrder)
		.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeMedicationStatement(MedicationStatement medicationStatement) {
		client.create()
		.resource(medicationStatement)
		.execute();
	}
		
	//TODO: Add conditional writes based on identifier
	public void writeObservation(Observation observation) {
		client.create()
		.resource(observation)
		.execute();
	}
		
	//TODO: Add conditional writes based on identifier
	public void writePatient(Patient patient) {
		client.create()
		.resource(patient)
		.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeProcedure(Procedure procedure) {
		client.create()
		.resource(procedure)
		.execute();
	}
		
	//TODO: Add conditional writes based on identifier
	public void writePractitioner(Practitioner practitioner) {
			client.create()
			.resource(practitioner)
			.execute();
	}
	
	//TODO: Add conditional writes based on identifier
	public void writeRelatedPerson(RelatedPerson relatedPerson) {
		client.create()
		.resource(relatedPerson)
		.execute();
	}
}