package gatech.edu.DeathRecordPuller.Controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.mapper.Mapper;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Dosage;
import org.hl7.fhir.dstu3.model.Timing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.AgeDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.composite.RangeDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu2.resource.Conformance.RestResourceInteraction;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.valueset.TypeRestfulInteractionEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import gatech.edu.STIECR.JSON.ECR;
import gatech.edu.common.FHIR.client.ClientService;

@CrossOrigin()
@RestController
public class NeCODController {
	private static final Logger log = LoggerFactory.getLogger(DeathRecordController.class);

	ClientService FHIRClient;
	private ObjectMapper objectMapper;
	private IParser jsonParser;

	@Autowired
	public NeCODController(ClientService FHIRClient) {
		this.FHIRClient = FHIRClient;
		this.FHIRClient.initializeVaClient();
		this.objectMapper = new ObjectMapper();
		this.objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		this.jsonParser = FHIRClient.getContext().newJsonParser().setPrettyPrint(true);
	}
	
	@RequestMapping(value = "/GetConditionMedication", method = RequestMethod.GET, produces = "application/json")
	public JsonNode GetConditionMedication(@RequestParam(value = "id") String id) {
		log.info("Pulling Patient Coverage Medications");
		JsonNode root = JsonNodeFactory.instance.objectNode(); 
		ObjectNode top = JsonNodeFactory.instance.objectNode();
		ObjectNode leaf = top.putObject("GetConditionMedicationResult");
		leaf = leaf.putObject("data");
		leaf.putArray("conditionList");
		leaf.putArray("medicationList");
		root = top;
		if(id.equals("185601V825290") || id.equals("185602V825292") || id.equals("185603V825293") || id.equals("185604V825294") || id.equals("185605V825295")) {
			IdDt patientId = new IdDt(id);
			//Get the FHIR records
			getFHIRRecords(root,patientId);
		}
		else {
			//Return an error saying that patient ID is invalid
		}
		ObjectNode cursor = (ObjectNode) root.get("GetConditionMedicationResult");
		cursor = cursor.putObject("meta");
		cursor.put("code", "200");
		cursor.put("timestamp", new Timestamp(System.currentTimeMillis()).toString());
		return root;
	}
	
	public JsonNode getFHIRRecords(JsonNode root,IdDt patientId) {

		List<RestResource> availableResources = FHIRClient.getConformanceStatementResources();
		List<RestResource> searchableResources = new ArrayList<RestResource>();
		for (RestResource resource : availableResources) {
			for (RestResourceInteraction interaction : resource.getInteraction()) {
				if (TypeRestfulInteractionEnum.SEARCH_TYPE.getCode().equals(interaction.getCode())) {
					searchableResources.add(resource);
					log.debug("Get records. Found resource " + resource.getType() + " that is searchable.");
					break;
				}
			}
		}
		
		// Cycle through the searchable resources
		for (RestResource resource : searchableResources) {

			// For all available resources
			switch (resource.getType()) {
			case "Condition":
				handleConditions(root, patientId);
				break;
			case "MedicationStatement":
				handleMedicationStatement(root, patientId);
				break;
			// TODO: Handle ingressing visits correctly
			// TODO: Handle All Observations correctly
			}
		}
		return root;
	}
	
	public void handleConditions(JsonNode root, IdDt patientId) {
		log.info("CONDITIONS - START");
		Bundle conditions = FHIRClient.getConditions(patientId);
		for (Entry entry : conditions.getEntry()) {
			Condition condition = (Condition) entry.getResource();
			ObjectNode objectEntry = JsonNodeFactory.instance.objectNode();
			
			objectEntry.put("abatementAge", "null");
			IDatatype abatement = condition.getAbatement();
			if(abatement instanceof BooleanDt) {
				objectEntry.put("abatementDateBoolean",((BooleanDt)abatement).getValue());
			}
			else {
				objectEntry.putNull("abatementDateBoolean");
			}
			if(abatement instanceof DateTimeDt) {
				objectEntry.put("abatementDateTime",((DateTimeDt)abatement).getValueAsString());
			}
			else {
				objectEntry.putNull("abatementDateTime");
			}
			if(abatement instanceof PeriodDt) {
				objectEntry.set("abatementPeriod",objectMapper.valueToTree(abatement));
			}
			else {
				objectEntry.putNull("abatementPeriod");
			}
			if(abatement instanceof RangeDt) {
				objectEntry.set("abatementRange",objectMapper.valueToTree(abatement));
			}
			else {
				objectEntry.putNull("abatementRange");
			}
			objectEntry.putNull("asserterDate");
			if(!condition.getAsserter().isEmpty()) {
				objectEntry.put("asserter", condition.getAsserter().getDisplay().toString());
			}
			else {
				objectEntry.putNull("asserter");
			}
			if(!condition.getBodySite().isEmpty()) {
				objectEntry.set("bodySite", objectMapper.valueToTree(condition.getBodySite()));
			}
			else {
				objectEntry.putNull("bodySite");
			}
			if(!condition.getCategory().isEmpty()) {
				objectEntry.put("category", condition.getCategory().toString());
			}
			else {
				objectEntry.putNull("category");
			}
			if(!condition.getClinicalStatusElement().isEmpty()) {
				objectEntry.put("clinicalStatus", condition.getClinicalStatusElement().toString());
			}
			else {
				objectEntry.putNull("clinicalStatus");
			}
			if(!condition.getCode().isEmpty()) {
				objectEntry.set("code", objectMapper.valueToTree(condition.getCode().toString()));
			}
			else {
				objectEntry.putNull("code");
			}
			if(!condition.getEncounter().isEmpty()) {
				objectEntry.put("context", condition.getEncounter().getDisplay().toString());
			}
			else {
				objectEntry.putNull("context");
			}
			if(!condition.getEvidence().isEmpty()) {
				objectEntry.set("evidence", objectMapper.valueToTree(condition.getEvidence()));
			}
			else {
				objectEntry.putNull("evidence");
			}
			if(!condition.getIdentifier().isEmpty()) {
				objectEntry.put("identifier", condition.getIdentifier().get(0).getValue());
			}
			else {
				objectEntry.putNull("identifier");
			}
			if(condition.getNotes() != null) {
				objectEntry.put("note", condition.getNotes());
			}
			else {
				objectEntry.putNull("note");
			}
			IDatatype onset = condition.getOnset();
			if(onset instanceof AgeDt) {
				objectEntry.put("onsetAge", ((AgeDt)onset).getValue());
			}
			else {
				objectEntry.putNull("onsetAge");
			}
			if(onset instanceof DateTimeDt) {
				objectEntry.put("onsetDateTime",((DateTimeDt)onset).getValueAsString());
			}
			else {
				objectEntry.putNull("onsetDateTime");
			}
			if(onset instanceof PeriodDt) {
				objectEntry.set("onsetPeriod",objectMapper.valueToTree(onset));
			}
			else {
				objectEntry.putNull("onsetPeriod");
			}
			if(onset instanceof RangeDt) {
				objectEntry.set("onsetRange",objectMapper.valueToTree(onset));
			}
			else {
				objectEntry.putNull("onsetRange");
			}
			if(onset instanceof StringDt) {
				objectEntry.put("onsetString",((StringDt)onset).toString());
			}
			else {
				objectEntry.putNull("onsetString");
			}
			objectEntry.put("resourceType", "Condition");
			objectEntry.put("serverity",condition.getSeverity().getText());
			if(!condition.getStage().isEmpty()) {
				objectEntry.set("stage", objectMapper.valueToTree(condition.getStage()));
			}
			else {
				objectEntry.putNull("stage");
			}
			if(!condition.getPatient().isEmpty()){
				objectEntry.put("subject", condition.getPatient().getDisplay().toString());
			}
			else {
				objectEntry.putNull("subject");
			}
			if(!condition.getVerificationStatus().isEmpty()) {
				objectEntry.put("verificationStatus", condition.getVerificationStatus());
			}
			else {
				objectEntry.putNull("verificationStatus");
			}
			ArrayNode conditionList = (ArrayNode)root.path("GetConditionMedicationResult").path("data").path("conditionList");
			conditionList.add(objectEntry);
		}
	}
	
	public void handleMedicationStatement(JsonNode root, IdDt patientId) {
		log.info("MEDICATIONSTATEMENT - START");
		Bundle conditions = FHIRClient.getMedicationStatements(patientId);
		for (Entry entry : conditions.getEntry()) {
			MedicationStatement statement = (MedicationStatement) entry.getResource();
			ObjectNode objectEntry = JsonNodeFactory.instance.objectNode();
			if(!statement.getIdentifier().isEmpty()) {
				objectEntry.put("identifier", statement.getIdentifier().get(0).getValue());
			}
			else {
				objectEntry.putNull("identifier");
			}
			objectEntry.putNull("basedOn");
			objectEntry.putNull("partOf");
			objectEntry.putNull("context");
			objectEntry.put("status", statement.getStatus());
			objectEntry.putNull("category");
			IDatatype medication = statement.getMedication();
			if(medication instanceof CodeableConceptDt) {
				objectEntry.put("medicationCodeableConcept", ((CodeableConceptDt)medication).getText());
			}
			else {
				objectEntry.putNull("medicationCodeableConcept");
			}
			if(medication instanceof ResourceReferenceDt) {
				objectEntry.put("medicationReference", ((ResourceReferenceDt) medication).getDisplay().toString());
			}
			else {
				objectEntry.putNull("medicationReference");
			}
			if(!statement.getEffective().isEmpty()) {
				IDatatype effective = statement.getEffective();
				if(effective instanceof DateTimeDt) {
					objectEntry.put("effectiveDateTime", ((DateTimeDt)effective).getValue().toString());
				}
				else {
					objectEntry.putNull("effectiveDateTime");
				}
				if(effective instanceof PeriodDt) {
					objectEntry.set("effectivePeriod",objectMapper.valueToTree(effective));
				}
				else {
					objectEntry.putNull("effectivePeriod");
				}
			}
			else {
				objectEntry.putNull("effectiveDateTime");
				objectEntry.putNull("effectivePeriod");
			}
			objectEntry.put("dateAsserted", statement.getDateAsserted().toString());
			if(!statement.getInformationSource().isEmpty()) {
				objectEntry.set("informationSource", objectMapper.valueToTree(statement.getInformationSource()));
			}
			else {
				objectEntry.putNull("informationSource");
			}
			objectEntry.put("subject", statement.getPatient().getDisplay().toString());
			objectEntry.putNull("derivedFrom");
			if(statement.getWasNotTaken() != null) {
				objectEntry.put("taken", statement.getWasNotTaken() ? "n" : "y");
			}
			else {
				objectEntry.putNull("taken");
			}
			if(!statement.getReasonNotTaken().isEmpty()) {
				objectEntry.set("reasonNotTaken", objectMapper.valueToTree(statement.getReasonNotTaken()));
			}
			else {
				objectEntry.putNull("reasonNotTaken");
			}
			IDatatype reason = statement.getReasonForUse();
			if(!reason.isEmpty()) {
				if(reason instanceof CodeableConceptDt) {
					objectEntry.set("reasonCode", objectMapper.valueToTree(reason));	
				}
				else {
					objectEntry.putNull("reasonCode");
				}
				if(reason instanceof ResourceReferenceDt) {
					objectEntry.put("reasonReference", ((ResourceReferenceDt) reason).getDisplay().toString());	
				}
				else {
					objectEntry.putNull("reasonReference");
				}
			}
			else {
				objectEntry.putNull("reasonCode");
				objectEntry.putNull("reasonReference");
			}
			if(!statement.getNote().isEmpty()) {
				objectEntry.put("note", statement.getNote());
			}
			else {
				objectEntry.putNull("note");
			}
			objectEntry.set("dosage", objectMapper.valueToTree(statement.getDosage())); //Quick dirty conversion
			
			ArrayNode statementList = (ArrayNode)root.path("GetstatementMedicationResult").path("data").path("medicationList");
			statementList.add(objectEntry);
		}
	}
	
}
