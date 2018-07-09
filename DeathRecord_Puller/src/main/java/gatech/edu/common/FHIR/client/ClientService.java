package gatech.edu.common.FHIR.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Claim;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.Rest;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Coverage;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Procedure;
import ca.uhn.fhir.model.dstu2.resource.RelatedPerson;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import gatech.edu.STIECR.JSON.Name;

@Service
@Configuration
@ConfigurationProperties(prefix="FHIR.client")
public class ClientService {
	
	private static final Logger log = LoggerFactory.getLogger(ClientService.class);
	
	protected String serverBaseUrl;
	protected String vAserverBaseUrl;
	protected String currentBaseUrl;
	protected static final FhirContext ctx = FhirContext.forDstu2();
	protected IGenericClient client;
	protected Map<String,RestResource> resourceCapabilityMap = new HashMap<String,RestResource>();
	protected Boolean lenientParsing = Boolean.FALSE;
	//TODO: Figure out how to search for source_ids, not new ids.
	@Autowired 
	public ClientService(){
		int timeout = 300*1000; // Timeout in 5 min.
		ClientService.ctx.getRestfulClientFactory().setSocketTimeout(timeout);
		ClientService.ctx.getRestfulClientFactory().setConnectionRequestTimeout(timeout);
		ClientService.ctx.getRestfulClientFactory().setConnectTimeout(timeout);
		ClientService.ctx.getRestfulClientFactory().setPoolMaxTotal(10);
		if(lenientParsing) {
			ClientService.ctx.setParserErrorHandler(new LenientErrorHandler());
		}
	}
	
	public ClientService(String serverBaseUrl) {
		this();
		this.serverBaseUrl= serverBaseUrl;	
	}
	
	public void initializeClient() {
		client = ctx.newRestfulGenericClient(serverBaseUrl);
		currentBaseUrl=serverBaseUrl;
		initializeCapabilityMap();
	}
	
	public void initializeVaClient() {
		client = ctx.newRestfulGenericClient("https://www.freedomstream.io/CDCArgonaut/api/");
		currentBaseUrl=vAserverBaseUrl;
		initializeCapabilityMap();
	}
	
	private void initializeCapabilityMap() {
		List<RestResource> resources = getConformanceStatementResources();
		for(RestResource aResource : resources) {
			String key = aResource.getType();
			resourceCapabilityMap.put(key, aResource);
		}
	}
	
	public List<RestResource> getConformanceStatementResources() {
		Conformance conformance = client.capabilities().ofType(Conformance.class).execute();
		List<RestResource> returnResourceList = new ArrayList<RestResource>();
		for(Rest rest: conformance.getRest()) {
			returnResourceList.addAll(rest.getResource());
		}
		return returnResourceList;
	}
	
	public Bundle getPatientUsingIdentifierAndOrganization(String identifier,String organization) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting everything with identifier="+identifier+" and organization="+organization);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Patient.class)
					.where(Patient.IDENTIFIER.exactly().identifier(identifier))
					.and(Patient.ORGANIZATION.hasId(organization))
					.include(Patient.INCLUDE_LINK)
					.returnBundle(Bundle.class)
					.execute();
		}
		catch (BaseServerResponseException e) {
			
		}
		log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		return results;
	}
	
	public Bundle getRelatedPersons(String personName) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting related person with personName="+personName);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(RelatedPerson.class)
					.where(RelatedPerson.NAME.matches().value(personName))
					.returnBundle(Bundle.class)
					.execute();
		}
		catch (BaseServerResponseException e) {
			
		}
		log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		return results;
	}
	
	public Bundle getRelatedPersons(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting related person with patientId="+patientId);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(RelatedPerson.class)
					.where(RelatedPerson.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
		}
		catch (BaseServerResponseException e) {
			
		}
		log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		return results;
	}
	
	public Bundle getPatient(String personName) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting patient with personName="+personName);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Patient.class)
					.where(Patient.NAME.matches().value(personName))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		
		return results;
	}
	
	public Patient getPatient(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting patient with patientId="+patientId);
		Patient results = new Patient();
		try {
			results = client.read()
					.resource(Patient.class)
					.withId(patientId)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Bundle getPatient(Name personName) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting patient with personName="+personName);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Patient.class)
					.where(Patient.GIVEN.matches().value(personName.getgiven()))
					.and(Patient.FAMILY.matches().value(personName.getfamily()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Bundle getPatientByIdentifier(String identifierSystem, String identifierValue) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting patient by identifier with identifierSystem="+ identifierSystem+ " identifierValue="+identifierValue);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Patient.class)
					.where(Patient.IDENTIFIER.exactly().systemAndIdentifier(identifierSystem, identifierValue))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Practitioner getPractictioner(IdDt practitionerId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting practictioner with practitionerId="+practitionerId);
		Practitioner results = new Practitioner();
		try {
			results = client.read()
					.resource(Practitioner.class)
					.withId(practitionerId)
					.execute();
			log.info("Found :"+results.getName().toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	//TODO: Figure out how to search for coverages
	/*public Bundle getCoverages() {  NOTE: Can't search via subscriber
		Bundle results = client.search()
				.forResource(Coverage.class)
				.where(Coverage.)
				.withId(patientId)
				.execute();
		return results;
	}*/
	
	public Bundle getImmunizations(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting immunizations with patientId="+patientId);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Immunization.class)
					.where(Immunization.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Bundle getProcedures(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting procedures with patientId="+patientId);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Procedure.class)
					.where(Procedure.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Bundle getEncounters(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting encounters with patientId="+patientId);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Encounter.class)
					.where(Encounter.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Bundle getConditions(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting conditions with patientId="+patientId);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Condition.class)
					.where(Condition.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Bundle getObservations(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting observations with patientId="+patientId);
		Bundle results = new Bundle();
		try {
			results = client.search()
					.forResource(Observation.class)
					.where(Observation.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+ctx.newJsonParser().encodeResourceToString(results));
		}
		catch(BaseServerResponseException e) {
			
		}
		return results;
	}
	
	public Medication getMedicationReference(IdDt medicationId) {
		Medication returnMedication = new Medication();
		try {
			returnMedication = client.read()
					.resource(Medication.class)
					.withId(medicationId)
					.execute();
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnMedication;
	}
	
	public Bundle getMedications(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting medications with patientId="+patientId);
		Bundle resultsA = new Bundle();
		try {
			resultsA = client.search()
					.forResource(MedicationAdministration.class)
					.where(MedicationAdministration.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
		}
		catch(BaseServerResponseException e) {
			
		}
		Bundle resultsD = new Bundle();
		try {
			resultsD = client.search()
					.forResource(MedicationDispense.class)
					.where(MedicationDispense.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
		}
		catch(BaseServerResponseException e) {
			
		}
		Bundle resultsO = new Bundle();
		try {
			resultsO = client.search()
					.forResource(MedicationOrder.class)
					.where(MedicationOrder.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
		}
		catch(BaseServerResponseException e) {
			
		}
		Bundle resultsM = new Bundle();
		try {
			resultsM = client.search()
					.forResource(MedicationStatement.class)
					.where(MedicationStatement.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
		}
		catch(BaseServerResponseException e) {
			
		}
		List<Bundle> medBundles = new ArrayList<Bundle>();
		medBundles.add(resultsA);
		medBundles.add(resultsD);
		medBundles.add(resultsO);
		medBundles.add(resultsM);
		Bundle returnBundle = new Bundle();
		for(Bundle medBundle : medBundles) {
			for(Entry entry : medBundle.getEntry()) {
				returnBundle.addEntry(entry);
			}
		}
		log.info("Found :"+returnBundle.toString());
		return returnBundle;
	}
	
	public Bundle getMedicationAdministrations(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting medications administrations with patientId="+patientId);
		Bundle returnBundle = new Bundle();
		try {
			returnBundle = client.search()
					.forResource(MedicationAdministration.class)
					.where(MedicationAdministration.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+returnBundle.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnBundle;
	}
	
	public Bundle getMedicationDispenses(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting medications dispenses with patientId="+patientId);
		Bundle returnBundle = new Bundle();
		try {
			returnBundle = client.search()
					.forResource(MedicationDispense.class)
					.where(MedicationDispense.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+returnBundle.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnBundle;
	}
	
	public Bundle getMedicationOrders(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting medications orders with patientId="+patientId);
		Bundle returnBundle = new Bundle();
		try {
			returnBundle = client.search()
					.forResource(MedicationOrder.class)
					.where(MedicationOrder.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+returnBundle.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnBundle;
	}
	
	public Bundle getMedicationStatements(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting medications orders with patientId="+patientId);
		Bundle returnBundle = new Bundle();
		try {
			returnBundle = client.search()
					.forResource(MedicationStatement.class)
					.where(MedicationStatement.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+returnBundle.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnBundle;
	}
	
	//NOT A Useful patient-centric call
	//Can search by provider group however
	public Bundle getCoverages(IdDt providerId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting coverages for providerId="+providerId);
		Bundle returnBundle = new Bundle();
		try {
			returnBundle = client.search()
					.forResource(Coverage.class)
					.where(Coverage.ISSUER.hasId(providerId.getValue()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+returnBundle.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnBundle;
	}
	
	public Bundle getClaims(IdDt patientId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting claims for patientId="+patientId);
		Bundle returnBundle = new Bundle();
		try {
			returnBundle = client.search()
					.forResource(Claim.class)
					.where(Claim.PATIENT.hasId(patientId.getIdPart()))
					.returnBundle(Bundle.class)
					.execute();
			log.info("Found :"+returnBundle.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnBundle;
	}
	
	public Condition getConditionById(IdDt conditionId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting condition with id="+conditionId);
		Condition returnCondition = new Condition();
		try {
			returnCondition = client.read()
					.resource(Condition.class)
					.withId(conditionId)
					.execute();
			log.info("Found :"+returnCondition.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnCondition;
	}
	
	public Coverage getCoverageById(IdDt coverageId) {
		log.info("serverBaseUrl="+currentBaseUrl);
		log.info("Getting coverage with id="+coverageId);
		Coverage returnCoverage = new Coverage();
		try {
			returnCoverage = client.read()
					.resource(Coverage.class)
					.withId(coverageId)
					.execute();
			log.info("Found :"+returnCoverage.toString());
		}
		catch(BaseServerResponseException e) {
			
		}
		return returnCoverage;
	}
	
	public Bundle getNextPage(Bundle bundle) {
		if(bundle.getLink(IBaseBundle.LINK_NEXT) != null) {
			return client.loadPage().next(bundle).execute();
		}
		else
			return null;
	}
	
	public IdDt transfrom2Id(Integer IDInt) {
		BigDecimal bigDecId = new BigDecimal(IDInt);
		return new IdDt(bigDecId);
	}
	
	public IdDt transfrom2Id(int IDInt) {
		BigDecimal bigDecId = new BigDecimal(IDInt);
		return new IdDt(bigDecId);
	}

	public String getServerBaseUrl() {
		return serverBaseUrl;
	}

	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}
	
	public String getVAServerBaseUrl() {
		return vAserverBaseUrl;
	}

	public void setVAServerBaseUrl(String serverBaseUrl) {
		this.vAserverBaseUrl = serverBaseUrl;
	}
	
	public IGenericClient getClient() {
		return client;
	}
}
