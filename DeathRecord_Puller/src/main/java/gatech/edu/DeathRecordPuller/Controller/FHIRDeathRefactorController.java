
package gatech.edu.DeathRecordPuller.Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.codesystems.ConditionVerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.RangeDt;
import ca.uhn.fhir.model.dstu2.composite.RatioDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SampledDataDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Claim;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceInteraction;
import ca.uhn.fhir.model.dstu2.resource.Coverage;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Immunization;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder.DosageInstruction;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Patient.Link;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.model.dstu2.resource.Procedure;
import ca.uhn.fhir.model.dstu2.resource.RelatedPerson;
import ca.uhn.fhir.model.dstu2.valueset.TypeRestfulInteractionEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.TimeDt;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import gatech.edu.DeathRecordPuller.util.HAPIFHIRUtil;
import gatech.edu.STIECR.JSON.CodeableConcept;
import gatech.edu.STIECR.JSON.Diagnosis;
import gatech.edu.STIECR.JSON.Dosage;
import gatech.edu.STIECR.JSON.ECR;
import gatech.edu.STIECR.JSON.ImmunizationHistory;
import gatech.edu.STIECR.JSON.LabOrderCode;
import gatech.edu.STIECR.JSON.LabResult;
import gatech.edu.STIECR.JSON.Name;
import gatech.edu.STIECR.JSON.ParentGuardian;
import gatech.edu.STIECR.JSON.Provider;
import gatech.edu.STIECR.JSON.utils.DateUtil;
import gatech.edu.common.FHIR.client.ClientService;
import gatech.edu.common.FHIR.client.WriteClientService;

@CrossOrigin()
@RestController
public class FHIRDeathRefactorController {

	private static final Logger log = LoggerFactory.getLogger(FHIRDeathRefactorController.class);

	ClientService FHIRClient;
	private ObjectMapper objectMapper;
	private IParser jsonParserDstu2;
	private IParser jsonParserDstu3;  
	
	@Autowired
	public FHIRDeathRefactorController(ClientService FHIRClient) {
		this.FHIRClient = FHIRClient;
		this.FHIRClient.initializeVaClient();
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		this.jsonParserDstu2 = FHIRClient.getContext().newJsonParser().setPrettyPrint(true);
		this.jsonParserDstu3 = FHIRClient.getContext().forDstu3().newJsonParser().setPrettyPrint(true);
	}

	@RequestMapping(value = "/FHIRDeath/Condition", method = RequestMethod.GET, produces = "application/json")
	public JsonNode FHIRDeathGetCondition(@RequestParam(value = "patient") String patient) {
		IdDt patientId = new IdDt(patient);
		Bundle dstu2Bundle = FHIRClient.getConditions(patientId);
		org.hl7.fhir.dstu3.model.Bundle dstu3Bundle = new org.hl7.fhir.dstu3.model.Bundle();
		for(Entry entry:dstu2Bundle.getEntry()) {
			Condition condition = (Condition)entry.getResource();
			org.hl7.fhir.dstu3.model.Condition dstu3Condition = new org.hl7.fhir.dstu3.model.Condition();
			try {
				JsonNode dstu2JSON = objectMapper.readTree(jsonParserDstu2.encodeResourceToString(condition));
			} catch (DataFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dstu3Condition.setVerificationStatus(org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus.CONFIRMED);;
			IdType subjectIdTypeDstu3 = new IdType("Patient",patient);
			org.hl7.fhir.dstu3.model.Reference subjectReferenceDstu3 = new org.hl7.fhir.dstu3.model.Reference(subjectIdTypeDstu3);
			dstu3Condition.setSubject(subjectReferenceDstu3);
			
			org.hl7.fhir.dstu3.model.CodeableConcept dstu3Code = new org.hl7.fhir.dstu3.model.CodeableConcept();
			CodingDt dstu2Coding = condition.getCode().getCoding().get(0);
			org.hl7.fhir.dstu3.model.Coding dstu3Coding = new org.hl7.fhir.dstu3.model.Coding(dstu2Coding.getSystem(),dstu2Coding.getCode(),dstu2Coding.getDisplay());
			dstu3Code.addCoding(dstu3Coding);
			dstu3Condition.setCode(dstu3Code);
			org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent bundleEntryComponent = new org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent();
			bundleEntryComponent.setResource(dstu3Condition);
			dstu3Bundle.addEntry(bundleEntryComponent);
		}
		JsonNode dstu3Json = null;
		try {
			dstu3Json = objectMapper.readTree(jsonParserDstu3.encodeResourceToString(dstu3Bundle));
			//Memory leak
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dstu3Json;
	}
	
	@RequestMapping(value = "/FHIRDeath/Patient", method = RequestMethod.GET, produces = "application/json")
	public JsonNode FHIRDeathGetPatient(@RequestParam(value = "_id") String patient) {
		JsonNode jsonPatientBundle = null;
		try {
			jsonPatientBundle = objectMapper.readTree(jsonParserDstu2.encodeResourceToString(FHIRClient.getPatient(patient)));
		} catch (DataFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonPatientBundle;
	}
	
	/*
	@RequestMapping(value = "/MedicationRequest", method = RequestMethod.GET, produces = "application/json")
	public JsonNode DeathRecord(@RequestParam(value = "patient") String patient) {
		
	}
	@RequestMapping(value = "/Observation", method = RequestMethod.GET, produces = "application/json")
	public JsonNode DeathRecord(@RequestParam(value = "patient") String patient) {
		
	}
	@RequestMapping(value = "/Procedure", method = RequestMethod.GET, produces = "application/json")
	public JsonNode DeathRecord(@RequestParam(value = "patient") String patient) {
		
	}
	*/
}
