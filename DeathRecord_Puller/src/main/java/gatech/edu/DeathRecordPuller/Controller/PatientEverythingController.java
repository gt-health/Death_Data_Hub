package gatech.edu.DeathRecordPuller.Controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.hl7.fhir.dstu3.hapi.ctx.FhirDstu3;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestComponent;
import org.hl7.fhir.dstu3.model.CapabilityStatement.CapabilityStatementRestOperationComponent;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.dstu2.FhirDstu2;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestOperation;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceInteraction;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceSearchParam;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IFetchConformanceUntyped;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import gatech.edu.DeathRecordPuller.Controller.config.PatientEverythingConfig;
import gatech.edu.DeathRecordPuller.ID.model.FHIRSource;
import gatech.edu.DeathRecordPuller.ID.model.IDEntry;
import gatech.edu.DeathRecordPuller.ID.service.IDService;
import gatech.edu.common.FHIR.client.ClientService;

@CrossOrigin()
@RestController
public class PatientEverythingController {
	
	protected FhirContext ctx2;
	protected FhirContext ctx3;
	protected List<IGenericClient> fhirServers;
	protected IParser jsonParser2;
	protected IParser jsonParser3;
	protected ObjectMapper mapper;
	@Autowired
	protected IDService idService;
	@Autowired
	public PatientEverythingController(PatientEverythingConfig config) {
		mapper = new ObjectMapper();
		fhirServers = new ArrayList<IGenericClient>();
		ctx2 = FhirContext.forDstu2();
		ctx2.getRestfulClientFactory().setSocketTimeout(60000);
		ctx2.getRestfulClientFactory().setConnectionRequestTimeout(36000);
		ctx2.getRestfulClientFactory().setConnectTimeout(36000);
		ctx2.getRestfulClientFactory().setPoolMaxTotal(10);
		ctx3 = FhirContext.forDstu3();
		ctx3.getRestfulClientFactory().setSocketTimeout(60000);
		ctx3.getRestfulClientFactory().setConnectionRequestTimeout(36000);
		ctx3.getRestfulClientFactory().setConnectTimeout(36000);
		ctx3.getRestfulClientFactory().setPoolMaxTotal(10);
		jsonParser2 = ctx2.newJsonParser();
		jsonParser3 = ctx3.newJsonParser();
		for(int i=0;i<config.getServerList().size();i++) {
			String serverName = config.getServerList().get(i);
			FhirContext ctx = ctx2;
			if(config.getFhirVersion().get(i).equals("stu3")) {
				ctx = ctx3;
			}
			fhirServers.add(ctx.getRestfulClientFactory().newGenericClient(serverName));
		}
	}
	/**
	 * Old version of get everything, using the same id for each endpoint
	 * @param id
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/Patient/{id}/$everything", method = RequestMethod.GET, produces = "application/json", headers= "IdMapping=false")
	public ResponseEntity<String> getPatientEverything(@PathVariable String id) throws JsonProcessingException{
		ca.uhn.fhir.model.dstu2.resource.Bundle returnBundleDstu2 = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Bundle returnBundleStu3 = new Bundle();
		for(IGenericClient endpoint: fhirServers) {
			IFhirVersion version = endpoint.getFhirContext().getVersion();
			if(version instanceof FhirDstu2) {
				ca.uhn.fhir.model.dstu2.resource.Bundle newEntries = new ca.uhn.fhir.model.dstu2.resource.Bundle(); 
				if(serverSupportsOperationDstu2(endpoint,"everything")) {
					newEntries = getEverythingDstu2(endpoint,id);
				}
				else {
					newEntries = manuallyGetEverythingDstu2(endpoint,id);
				}
				for(Entry newEntry : newEntries.getEntry()) {
					returnBundleDstu2.addEntry(newEntry);
				}
			}
			else if(version instanceof FhirDstu3) {
				if(serverSupportsOperationDstu3(endpoint,"everything")) {
					Bundle newEntries = getEverythingDstu3(endpoint,id);
					for(BundleEntryComponent newEntry : newEntries.getEntry()) {
						returnBundleStu3.addEntry(newEntry);
					}
				}
			}
		}
		Bundle convertedBundle = convertDstu2BundleToStu3Bundle(returnBundleDstu2);
		for(BundleEntryComponent newEntry : convertedBundle.getEntry()) {
			returnBundleStu3.addEntry(newEntry);
		}
		return new ResponseEntity<String>(jsonParser3.encodeResourceToString(returnBundleStu3), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/Patient/Search", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> getPatientEverythingIdServiceEnabled(@RequestParam(name= "case-number") String caseNumber,
			@RequestParam String name,@RequestParam String family,@RequestParam String given) throws JsonProcessingException{
		//Check for params
		if(name.isEmpty() && (family.isEmpty() && name.isEmpty()) && caseNumber.isEmpty()) {
			return new ResponseEntity<String>("No Params specified please use at least one of these parameters: [case-number,name,family.given]",HttpStatus.BAD_REQUEST);
		}
		ca.uhn.fhir.model.dstu2.resource.Bundle returnBundleDstu2 = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Bundle returnBundleStu3 = new Bundle();
		IDEntry idEntry = idService.getIDEntry(caseNumber,name,family,given);
		for(IGenericClient endpoint: fhirServers) {
			FHIRSource fhirSource = idEntry.getFhirSource(endpoint.getServerBase());
			if(fhirSource != null) {
				String endpointId = fhirSource.getFhirPatiendId();
				IFhirVersion version = endpoint.getFhirContext().getVersion();
				if(version instanceof FhirDstu2) {
					ca.uhn.fhir.model.dstu2.resource.Bundle newEntries = new ca.uhn.fhir.model.dstu2.resource.Bundle(); 
					if(serverSupportsOperationDstu2(endpoint,"everything")) {
						newEntries = getEverythingDstu2(endpoint,endpointId);
					}
					else {
						newEntries = manuallyGetEverythingDstu2(endpoint,endpointId);
					}
					for(Entry newEntry : newEntries.getEntry()) {
						returnBundleDstu2.addEntry(newEntry);
					}
				}
				else if(version instanceof FhirDstu3) {
					if(serverSupportsOperationDstu3(endpoint,"everything")) {
						Bundle newEntries = getEverythingDstu3(endpoint,endpointId);
						for(BundleEntryComponent newEntry : newEntries.getEntry()) {
							returnBundleStu3.addEntry(newEntry);
						}
					}
				}
			}
		}
		Bundle convertedBundle = convertDstu2BundleToStu3Bundle(returnBundleDstu2);
		for(BundleEntryComponent newEntry : convertedBundle.getEntry()) {
			returnBundleStu3.addEntry(newEntry);
		}
		return new ResponseEntity<String>(jsonParser3.encodeResourceToString(returnBundleStu3), HttpStatus.OK);
	}
	
	public boolean serverSupportsOperationDstu2(IGenericClient client,String name) {
		Conformance conformance = null;
		try {
		conformance = client.capabilities().ofType(Conformance.class).execute();
		}
		catch(InternalErrorException e) {
			return false;
		}
		for(Conformance.Rest restComponent:conformance.getRest()) {
			for(RestOperation operation:restComponent.getOperation()) {
				if(operation.getName().equals("everything"))
					return true;
			}
		}
		return false;
	}
	
	public ca.uhn.fhir.model.dstu2.resource.Bundle getEverythingDstu2(IGenericClient client,String patientId) {
		ca.uhn.fhir.model.dstu2.resource.Parameters outParams = client.operation()
		.onInstance(new IdType("Patient",patientId))
		.named("$everything")
		.withParameters(new ca.uhn.fhir.model.dstu2.resource.Parameters())
		.execute();
		
		return (ca.uhn.fhir.model.dstu2.resource.Bundle) outParams.getParameterFirstRep().getResource();
	}
	
	public ca.uhn.fhir.model.dstu2.resource.Bundle manuallyGetEverythingDstu2(IGenericClient client,String patientId) {
		ca.uhn.fhir.model.dstu2.resource.Bundle returnBundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		try {
		Patient patient = client.read()
				.resource(Patient.class)
				.withId(new IdType(patientId))
				.execute();
		returnBundle.addEntry(new Entry().setResource(patient));
		}
		catch(ResourceNotFoundException e) {
			return returnBundle;
		}
		Conformance conformance = null;
		try {
		conformance = client.capabilities().ofType(Conformance.class).execute();
		}
		catch(InternalErrorException e) {
			return new ca.uhn.fhir.model.dstu2.resource.Bundle();
		}
		for(Conformance.Rest restComponent:conformance.getRest()) {
			for(RestResource restResource:restComponent.getResource()) {
				String resourceType = restResource.getType();
				for(RestResourceInteraction restResourceInteration:restResource.getInteraction()) {
					if(restResourceInteration.getCode().equalsIgnoreCase("read")) {
						for(RestResourceSearchParam searchParam:restResource.getSearchParam()) {
							if(searchParam.getName().equalsIgnoreCase("patient") && 
									(searchParam.getType().equalsIgnoreCase("reference") || searchParam.getType().equalsIgnoreCase("string"))){
								String searchUrl = restResource.getType() + "?patient="+patientId;
								ca.uhn.fhir.model.dstu2.resource.Bundle tempBundle = client.search()
								.byUrl(searchUrl)
								.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
								.execute();
								while(tempBundle != null) {
									for(Entry newEntry : tempBundle.getEntry()) {
										returnBundle.addEntry(newEntry);
									}
									tempBundle = null;
									/*if(tempBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
										tempBundle = client.loadPage().next(tempBundle).execute();
									}
									else {
										tempBundle = null;
									}*/
								}
							}
						}
					}
				}
			}
		}
		return returnBundle;
	}
	
	public boolean serverSupportsOperationDstu3(IGenericClient client,String name) {
		CapabilityStatement capabilities = null;
		try {
		capabilities = client.capabilities().ofType(CapabilityStatement.class).execute();
		}
		catch(InternalErrorException e) {
			return false;
		}
		for(CapabilityStatementRestComponent restComponent:capabilities.getRest()) {
			if(restComponent.hasOperation()) {
				for(CapabilityStatementRestOperationComponent operComponent:restComponent.getOperation()) {
					if(operComponent.getName().equals(name))
						return true;
				}
			}
		}
		return false;
	}
	
	public Bundle getEverythingDstu3(IGenericClient client,String patientId) {
		Parameters outParams = null;
		try {
			outParams = client.operation()
			.onInstance(new IdType("Patient",patientId))
			.named("$everything")
			.withParameters(new Parameters())
			.useHttpGet()
			.execute();
		}
		catch(InternalErrorException e) {
			return new Bundle();
		}
		return (Bundle) outParams.getParameterFirstRep().getResource();
	}
	
	public Bundle convertDstu2BundleToStu3Bundle(ca.uhn.fhir.model.dstu2.resource.Bundle dstu2Bundle) {
		Bundle stu3Bundle = new Bundle();
		for(Iterator<Entry> iterator = dstu2Bundle.getEntry().iterator(); iterator.hasNext();) {
			Entry entry = iterator.next();
			if(entry.getResource() instanceof MedicationOrder) {
				MedicationRequest medicationReq = medicationOrderToMedicationRequest((MedicationOrder)entry.getResource());
				stu3Bundle.addEntry(new BundleEntryComponent().setResource(medicationReq));
				iterator.remove();
			}
		}
		String dstu2String = jsonParser2.encodeResourceToString(dstu2Bundle);
		Bundle lintedBundle = (Bundle) jsonParser3.parseResource(new StringReader(dstu2String));
		for(BundleEntryComponent bundleEntryComponent:lintedBundle.getEntry()) {
			stu3Bundle.addEntry(bundleEntryComponent);
		}
		return stu3Bundle;
	}
	
	public MedicationRequest medicationOrderToMedicationRequest(MedicationOrder medicationOrder) {
		ObjectNode jsonObject;
		try {
			jsonObject = (ObjectNode)mapper.readTree(jsonParser2.encodeResourceToString(medicationOrder));
			jsonObject.put("resourceType", "MedicationRequest");
			jsonObject.remove("dateWritten");
			jsonObject.put("subject", jsonObject.get("patient"));
			jsonObject.remove("patient");
			jsonObject.put("performer", jsonObject.get("prescriber"));
			jsonObject.remove("prescriber");
			MedicationRequest output = jsonParser3.parseResource(MedicationRequest.class,new StringReader(mapper.writeValueAsString(jsonObject.asText())));
			return output;
		}
		catch (Exception e) {
			return new MedicationRequest();
		}
	}
}
