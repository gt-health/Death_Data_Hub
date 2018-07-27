
package gatech.edu.DeathRecordPuller.Controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
public class DeathRecordController {

	private static final Logger log = LoggerFactory.getLogger(DeathRecordController.class);

	ClientService FHIRClient;
	WriteClientService storageClient;
	
	@Autowired
	public DeathRecordController(ClientService FHIRClient) {
		this.FHIRClient = FHIRClient;
		this.FHIRClient.initializeClient();
	}

	@RequestMapping(value = "/DeathRecord", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ECR> DeathRecord(@RequestParam(value = "id") String id) {
		HttpStatus returnStatus = HttpStatus.OK;
		ECR ecr = new ECR();
		Bundle returnBundle = new Bundle();
		IdDt idType = new IdDt(id);
		if(id.equals("185601V825290") || id.equals("185602V825292") || id.equals("185603V825293") || id.equals("185604V825294") || id.equals("185605V825295")) {
			FHIRClient.initializeVaClient();
		}
		else {
			FHIRClient.initializeClient();
		}
		Patient patient = FHIRClient.getPatient(idType);
		returnBundle.addEntry().setResource(patient).setFullUrl(patient.getId().getValue());
		RestTemplate EDRSEndpoint = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode body = objectMapper.createObjectNode();
		
		getFHIRRecords(ecr, returnBundle, FHIRClient.getClient());
		
		/*handleEDRSBody(ecr,body);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAcceptCharset(Collections.singletonList("UTF-8"));
		HttpEntity<ObjectNode> entity = new HttpEntity<ObjectNode>(body, headers);
		EDRSEndpoint.postForObject("http://deathdatahub1.hdap.gatech.edu:3000", body, JsonObject.class);*/
		
		HttpHeaders responseHeaders = new HttpHeaders();
		List<HttpMethod> allowedMethods = new ArrayList<HttpMethod>();
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.PUT);
		allowedMethods.add(HttpMethod.DELETE);		
		
		
		List<String> allowedHeaders = new ArrayList<String>();
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("Accept");
		allowedHeaders.add("Accept-Encoding");
		allowedHeaders.add("Accept-Language");
		allowedHeaders.add("X-Requested-With");
		allowedHeaders.add("remember-me");
		
		responseHeaders.setAccessControlAllowOrigin("*");
		responseHeaders.setAccessControlAllowMethods(allowedMethods);
		responseHeaders.setAccessControlAllowHeaders(allowedHeaders);
		responseHeaders.setAccessControlAllowCredentials(true);
		responseHeaders.setAccessControlMaxAge(3600L);
		return new ResponseEntity<ECR>(ecr, responseHeaders, returnStatus);
	}

	void getFHIRRecords(ECR ecr, Bundle fhirPatientBundle, IGenericClient client) {

		// Patient ecrPatient = ecr.getPatient();
		log.info("Handling a patient bundle with total patients:" + fhirPatientBundle.getTotal());
		List<RestResource> availableResources = FHIRClient.getConformanceStatementResources();
		List<RestResource> searchableResources = new ArrayList<RestResource>();
		for (RestResource resource : availableResources) {
			for (RestResourceInteraction interaction : resource.getInteraction()) {
				if (TypeRestfulInteractionEnum.SEARCH_TYPE.getCode().equals(interaction.getCode()) || resource.getType().equals("Patient")) {
					searchableResources.add(resource);
					log.debug("ADDING RESOURCE TYPE= " + resource.getType());
					break;
				}
			}
		}

		// Cycle through the patients and process each one at a time
		for (Entry entry : fhirPatientBundle.getEntry()) {

			List<ca.uhn.fhir.model.dstu2.resource.Patient> linkedPatients = new ArrayList<ca.uhn.fhir.model.dstu2.resource.Patient>();

			ca.uhn.fhir.model.dstu2.resource.Patient patient = (ca.uhn.fhir.model.dstu2.resource.Patient) entry
					.getResource();

			linkedPatients.add(patient); // Add initial patient to linked patients.
			for (Link link : patient.getLink()) {
				ca.uhn.fhir.model.dstu2.resource.Patient otherPat = (ca.uhn.fhir.model.dstu2.resource.Patient) link
						.getOther().loadResource(client);
				if (otherPat != null) {
					if (otherPat.getId().isLocal()) {
						// Remove Local # from Patient.
						otherPat.setId(otherPat.getId().getIdPart().replaceFirst("#", ""));
					}
					linkedPatients.add(otherPat); // Add Linked Patient
				}
			}

			// Cycle through the bundle and linked patients
			for (ca.uhn.fhir.model.dstu2.resource.Patient curPatient : linkedPatients) {
				// Cycle through the creatable resources
				for (RestResource resource : searchableResources) {

					log.info("Getting for " + resource.getType() + " for patient with id="
							+ curPatient.getId().getIdPart());

					// For all available resources
					switch (resource.getType()) {
					case "Condition":
						handleConditions(ecr, curPatient.getId());
						break;
					case "Claim":
						handleClaims(ecr, curPatient.getId());
						break;
					case "Encounter":
						handleEncounters(ecr, curPatient.getId());
						break;
					case "Immunization":
						handleImmunizations(ecr, curPatient.getId());
						break;
					case "MedicationAdministration":
						handleMedicationAdministrations(ecr, curPatient.getId());
						break;
					case "MedicationDispense":
						handleMedicationDispenses(ecr, curPatient.getId());
						break;
					case "MedicationOrder":
						handleMedicationOrders(ecr, curPatient.getId());
						break;
					case "MedicationStatement":
						handleMedicationStatements(ecr, curPatient.getId());
						break;
					case "Observation":
						handleObservation(ecr, curPatient.getId());
						break;
					case "Patient":
						handlePatient(ecr, curPatient);
						break;
					case "Practictioner":
						for (ResourceReferenceDt practitionerRef : patient.getCareProvider()) {
							handlePractitioner(ecr, practitionerRef);
						}
						break;
					case "Procedure":
						handleProcedure(ecr, patient.getId());
						break;
					case "RelatedPersons":
						handleRelatedPersons(ecr, patient.getId());
						break;
					}
					for (ResourceReferenceDt practitionerRef : curPatient.getCareProvider()) {
						handlePractitioner(ecr, practitionerRef);
					}
					// TODO: Handle ingressing visits correctly
					// TODO: Handle All Observations correctly
				}
			}
		}

		// DT - If date of onset is empty then look for the triggering lab result that
		// is positive and use it as the date.

		updateDateOfOnset(ecr);
	}

	protected void updateDateOfOnset(ECR ecr) {
		if (StringUtils.isBlank(ecr.getPatient().getdateOfOnset())) {
			for (LabOrderCode labcode : ecr.getPatient().getlabOrderCode()) {
				for (LabResult labresult : labcode.getLaboratory_Results()) {
					if (labresult.getValue().toLowerCase().contains("positive")) {
						if (!StringUtils.isBlank(labresult.getDate())) {
							log.info("LabResult --- Found onset date of: " + labresult.getDate());
							ecr.getPatient().setdateOfOnset(labresult.getDate());
							break;
						}
					}
				}
			}
		}
	}

	void handlePatient(ECR ecr, ca.uhn.fhir.model.dstu2.resource.Patient patient) {
		log.info("PATIENT -- Working with patient:"+ patient.toString());
		if(!patient.getName().isEmpty()) {
			log.info("PATIENT -- Patient name found!");
			log.info("PATIENT -- Patient name 0:" + patient.getName().get(0).toString());
			log.info("PATIENT -- Patient name 0 family:" + patient.getName().get(0).getFamily().get(0).toString());
			log.info("PATIENT -- Patient name 0 given:" + patient.getName().get(0).getGiven().get(0).toString());
			HumanNameDt nameDt = patient.getName().get(0);
			Name name = new Name();
			name.setfamily(nameDt.getFamily().get(0).getValue());
			name.setgiven(nameDt.getGiven().get(0).getValue());
			ecr.getPatient().setname(name);
		}
		ecr.getPatient().setbirthDate(patient.getBirthDate().toString());
		IDatatype deceasedValue = patient.getDeceased();
		if (deceasedValue != null && deceasedValue instanceof DateDt) {
			ecr.getPatient().setdeathDate(DateUtil.dateToStdString(((DateDt) deceasedValue).getValue()));
		}
		ecr.getPatient().setsex(patient.getGender());
		if(!patient.getAddress().isEmpty()) {
			ecr.getPatient().setstreetAddress(HAPIFHIRUtil.addressToString(patient.getAddress().get(0)));	
		}
		//Support for extended race and ethnicity
		for(ExtensionDt extension : patient.getUndeclaredExtensions()) {
			if(extension.getUrl().equals("http://fhir.org/guides/argonaut/StructureDefinition/argo-race")) {
				ExtensionDt valueExtension = extension.getExtension().get(0);
				ecr.getPatient().setrace(new CodeableConcept("http://fhir.org/guides/argonaut/StructureDefinition/argo-race",valueExtension.getValue().toString(),valueExtension.getValue().toString()));
			}
			if(extension.getUrl().equals("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity")) {
				ExtensionDt valueExtension = extension.getExtension().get(0);
				ecr.getPatient().setethnicity(new CodeableConcept("http://fhir.org/guides/argonaut/StructureDefinition/argo-ethnicity",valueExtension.getValue().toString(),valueExtension.getValue().toString()));
			}
		}
		
		/*for(IdentifierDt identifier : patient.getIdentifier()) {
			if(identifier.getSystem().equals("http://hl7.org/fhir/sid/us-ssn")) {
				body.put("snn.snn1", identifier.getValue().substring(0, 3));
				body.put("snn.snn2", identifier.getValue().substring(3, 2));
				body.put("snn.snn3", identifier.getValue().substring(5, 4));
			}
		}*/
	}

	void handleRelatedPersons(ECR ecr, IdDt IdDt) {
		Bundle relatedPersons = FHIRClient.getRelatedPersons(IdDt);
		do {
			for (Entry entry : relatedPersons.getEntry()) {
				RelatedPerson relatedPerson = (RelatedPerson) entry.getResource();
				Name nameToSearch = new Name(relatedPerson.getName().getFamily().get(0).getValue(),
						relatedPerson.getName().getGiven().get(0).getValue());
				ParentGuardian ecrParentGuardian = ecr.findParentGuardianWithName(nameToSearch);
				if (ecrParentGuardian == null) {
					ecrParentGuardian = new ParentGuardian();
					ecrParentGuardian.setname(nameToSearch);
					updateParentGuardian(ecrParentGuardian, relatedPerson);
					ecr.getPatient().getparentsGuardians().add(ecrParentGuardian);
				} else {
					updateParentGuardian(ecrParentGuardian, relatedPerson);
				}
			}
			relatedPersons = FHIRClient.getNextPage(relatedPersons);
		} while (relatedPersons != null);
	}

	void handlePractitioner(ECR ecr, ResourceReferenceDt refDt) {
		Practitioner provider = FHIRClient.getPractictioner(refDt.getId());
		Provider ecrProvider = new Provider();
		ecrProvider.setaddress(provider.getAddress().get(0).getText());
		ecrProvider.setcountry(provider.getAddress().get(0).getCountry());
		for (ContactPointDt contact : provider.getTelecom()) {
			if (contact.getSystem().equals("Phone") && ecrProvider.getphone().isEmpty()) {
				ecrProvider.setphone(contact.getValue());
			} else if (contact.getSystem().equals("Email") && ecrProvider.getemail().isEmpty()) {
				ecrProvider.setemail(contact.getValue());
			}
		}
		// Update or add to the current provider list
		if (ecr.getProvider().contains(ecrProvider)) {
			for (Provider originalProvider : ecr.getProvider()) {
				if (originalProvider.equals(ecrProvider))
					originalProvider.update(ecrProvider);
			}
		} else {
			ecr.getProvider().add(ecrProvider);
		}
	}

	void handleMedicationAdministrations(ECR ecr, IdDt IdDt) {
		Bundle medications = FHIRClient.getMedicationAdministrations(IdDt);
		do {
			for (Entry entry : medications.getEntry()) {
				CodeableConcept ecrCode = new CodeableConcept();
				MedicationAdministration medicationAdministration = (MedicationAdministration) entry.getResource();
				gatech.edu.STIECR.JSON.Medication ecrMedication = new gatech.edu.STIECR.JSON.Medication();
				log.info("MEDICATIONADMINISTRATION --- Trying medicationAdministration: "
						+ medicationAdministration.getId());
				IDatatype medicationCodeUntyped = medicationAdministration.getMedication();
				log.info("MEDICATIONADMINISTRATION --- medication code element class: "
						+ medicationCodeUntyped.getClass());
				if (medicationCodeUntyped instanceof CodeableConceptDt) {
					CodeableConceptDt code = (CodeableConceptDt) medicationCodeUntyped;
					log.info("MEDICATIONADMINISTRATION --- Trying code with this many codings: "
							+ code.getCoding().size());
					for (CodingDt coding : code.getCoding()) {
						log.info("MEDICATIONADMINISTRATION --- Trying coding: " + coding.getDisplay());
						CodeableConcept concept = FHIRCoding2ECRConcept(coding);
						log.info("MEDICATIONADMINISTRATION --- Translated to ECRconcept:" + concept.toString());
						ecrMedication.setCode(concept.getcode());
						ecrMedication.setSystem(concept.getsystem());
						ecrMedication.setDisplay(concept.getdisplay());
						ecrCode.setcode(concept.getcode());
						ecrCode.setsystem(concept.getsystem());
						ecrCode.setdisplay(concept.getdisplay());
					}
				}
				if (!medicationAdministration.getDosage().isEmpty()) {
					Dosage ecrDosage = new Dosage();
					ecrDosage.setValue(medicationAdministration.getDosage().getQuantity().getValue().toString());
					ecrDosage.setUnit(medicationAdministration.getDosage().getQuantity().getUnit());
					ecrMedication.setDosage(ecrDosage);
				}
				if (!medicationAdministration.getEffectiveTime().isEmpty()) {
					ecrMedication.setDate(HAPIFHIRUtil.getDate(medicationAdministration.getEffectiveTime()).toString());
				}
				log.info("MEDICATIONADMINISTRATION --- ECRCode: " + ecrCode);
				if (!ecr.getPatient().getMedicationProvided().contains(ecrMedication)) {
					log.info("MEDICATIONADMINISTRATION --- Found New Entry: " + ecrCode);
					ecr.getPatient().getMedicationProvided().add(ecrMedication);
				} else {
					log.info("MEDICATIONADMINISTRATION --- Didn't Match or found duplicate! " + ecrCode);
				}

				if (!medicationAdministration.getReasonGiven().isEmpty()) {
					for (CodeableConceptDt reason : medicationAdministration.getReasonGiven()) {
						handleSingularConditionConceptCode(ecr, reason);
					}
				}
			}
			medications = FHIRClient.getNextPage(medications);
		} while (medications != null);
	}

	void handleMedicationDispenses(ECR ecr, IdDt IdDt) {
		Bundle medications = FHIRClient.getMedicationDispenses(IdDt);
		do {
			for (Entry entry : medications.getEntry()) {
				CodeableConcept ecrCode = new CodeableConcept();
				MedicationDispense medicationDispense = (MedicationDispense) entry.getResource();
				gatech.edu.STIECR.JSON.Medication ecrMedication = new gatech.edu.STIECR.JSON.Medication();
				log.info("MEDICATIONDISPENSE --- Trying medicationDispense: " + medicationDispense.getId());

				IDatatype medicationCodeUntyped = medicationDispense.getMedication();
				if (medicationCodeUntyped == null && medicationDispense.getAuthorizingPrescription() != null) {
					medicationCodeUntyped = ((MedicationOrder) medicationDispense.getAuthorizingPrescription().get(0)
							.getResource()).getMedication();
				}
				if (medicationCodeUntyped == null) {
					log.info("MEDICATIONDISPENSE --- FAILED TO FIND MEDICATION - SKIPPING!!");
					continue;
				}
				log.info("MEDICATIONDISPENSE --- medication code element class: " + medicationCodeUntyped.getClass());

				CodeableConceptDt code = null;

				if (medicationCodeUntyped instanceof CodeableConceptDt) {
					code = (CodeableConceptDt) medicationCodeUntyped;
				} else if (medicationCodeUntyped instanceof ResourceReferenceDt) {
					code = ((Medication) ((ResourceReferenceDt) medicationCodeUntyped).getResource()).getCode();
				}
				if (code != null) {
					log.info("MEDICATIONDISPENSE --- Trying code with this many codings: " + code.getCoding().size());
					for (CodingDt coding : code.getCoding()) {
						log.info("MEDICATIONDISPENSE --- Trying coding: " + coding.getDisplay());
						CodeableConcept concept = FHIRCoding2ECRConcept(coding);

						log.info("\n----------> MEDICATIONDISPENSE --- Translated to ECRconcept:" + concept.toString());
						ecrMedication.setCode(concept.getcode());
						ecrMedication.setSystem(concept.getsystem());
						ecrMedication.setDisplay(concept.getdisplay());
						ecrCode.setcode(concept.getcode());
						ecrCode.setsystem(concept.getsystem());
						ecrCode.setdisplay(concept.getdisplay());

					}
					for (ca.uhn.fhir.model.dstu2.resource.MedicationDispense.DosageInstruction dosageInstruction : medicationDispense
							.getDosageInstruction()) {
						Dosage ecrDosage = new Dosage();
						IDatatype doseUntyped = dosageInstruction.getDose();
						if (doseUntyped != null) {
							log.info("MEDICATIONDISPENSE --- Found Dosage: " + doseUntyped.toString());
							if (doseUntyped instanceof SimpleQuantityDt) {
								SimpleQuantityDt doseTyped = (SimpleQuantityDt) doseUntyped;
								log.info("MEDICATIONDISPENSE --- Dosage is of SimpleQuentityDt Type");
								ecrDosage.setValue(doseTyped.getValue().toString());
								ecrDosage.setUnit(doseTyped.getUnit());
								ecrMedication.setDosage(ecrDosage);
							}
							String periodUnit = dosageInstruction.getTiming().getRepeat().getPeriodUnits();
							BigDecimal period = dosageInstruction.getTiming().getRepeat().getPeriod();
							Integer frequency = dosageInstruction.getTiming().getRepeat().getFrequency();
							String commonFrequency = "" + frequency + " times per " + period + " " + periodUnit;
							log.info("MEDICATIONDISPENSE --- Found Frequency: " + commonFrequency);
							ecrMedication.setFrequency(commonFrequency);
						} else {
							log.info("MEDICATIONDISPENSE --- Not Found");
						}
					}
					Date timeDispensed = medicationDispense.getWhenHandedOver();
					log.info("MEDICATIONDISPENSE --- Found Handed Over Date: " + timeDispensed);
					if (timeDispensed != null) {
						ecrMedication.setDate(DateUtil.dateTimeToStdString(timeDispensed));
					}
					log.info("MEDICATIONDISPENSE --- ECRCode: " + ecrCode);
					if (!ecr.getPatient().getMedicationProvided().contains(ecrMedication)) {
						log.info("=======>MEDICATIONDISPENSE --- Found New Entry and added to ECR: " + ecrCode);
						ecr.getPatient().getMedicationProvided().add(ecrMedication);
					} else {
						log.info("MEDICATIONDISPENSE --- Didn't Match or found duplicate! " + ecrCode);
					}
				} else {
					log.info("FAILED TO FIND MEDICATION CODE.");
				}
			}
			medications = FHIRClient.getNextPage(medications);
		} while (medications != null);
	}

	void handleMedicationOrders(ECR ecr, IdDt IdDt) {
		Bundle medications = FHIRClient.getMedicationOrders(IdDt);
		do {
			for (Entry entry : medications.getEntry()) {
				CodeableConcept ecrCode = new CodeableConcept();
				MedicationOrder medicationOrder = (MedicationOrder) entry.getResource();
				gatech.edu.STIECR.JSON.Medication ecrMedication = new gatech.edu.STIECR.JSON.Medication();
				log.info("MEDICATIONORDER --- Trying medicationOrder: " + medicationOrder.getId());
				IDatatype medicationCodeUntyped = medicationOrder.getMedication();
				log.info("MEDICATIONORDER --- medication code element class: " + medicationCodeUntyped.getClass());

				CodeableConceptDt code = null;

				if (medicationCodeUntyped instanceof CodeableConceptDt) {
					code = (CodeableConceptDt) medicationCodeUntyped;
				} else if (medicationCodeUntyped instanceof ResourceReferenceDt) {
					try {
						ResourceReferenceDt medicationReference = (ResourceReferenceDt) medicationCodeUntyped;
						if(!medicationReference.getReference().isEmpty()) {
							log.info("MEDICATIONORDER --- medication reference Id: " + medicationReference.getReference());
							Medication baseMedication = FHIRClient.getMedicationReference(medicationReference.getReference());
							code = baseMedication.getCode();
						}
						else if(medicationReference.getDisplay() != null) {
							log.info("MEDICATIONORDER --- medication reference display only: " + medicationReference.getDisplay());
							code = new CodeableConceptDt();
							CodingDt singleDisplayCoding = new CodingDt();
							singleDisplayCoding.setDisplay(medicationReference.getDisplay());
							code.addCoding(new CodingDt());
						}
					}
					catch(InternalErrorException | ResourceNotFoundException e) {
						
					}
				}
				if (code != null) {
					log.info("MEDICATIONORDER --- Trying code with this many codings: " + code.getCoding().size());
					for (CodingDt coding : code.getCoding()) {
						log.info("MEDICATIONORDER --- Trying coding: " + coding.getDisplay());
						CodeableConcept concept = FHIRCoding2ECRConcept(coding);
						log.info("MEDICATIONORDER --- Translated to ECRconcept:" + concept.toString());
						ecrMedication.setCode(concept.getcode());
						ecrMedication.setSystem(concept.getsystem());
						ecrMedication.setDisplay(concept.getdisplay());
						ecrCode.setcode(concept.getcode());
						ecrCode.setsystem(concept.getsystem());
						ecrCode.setdisplay(concept.getdisplay());
					}
				}
				for (DosageInstruction dosageInstruction : medicationOrder.getDosageInstruction()) {
					Dosage ecrDosage = new Dosage();
					IDatatype doseUntyped = dosageInstruction.getDose();
					if (doseUntyped != null) {
						log.info("MEDICATIONORDER --- Found Dosage: " + doseUntyped.toString());
						if (doseUntyped instanceof SimpleQuantityDt) {
							SimpleQuantityDt doseTyped = (SimpleQuantityDt) doseUntyped;
							log.info("MEDICATIONORDER --- Dosage is of SimpleQuentityDt Type");
							ecrDosage.setValue(doseTyped.getValue().toString());
							ecrDosage.setUnit(doseTyped.getUnit());
							ecrMedication.setDosage(ecrDosage);
						}
						String periodUnit = dosageInstruction.getTiming().getRepeat().getPeriodUnits();
						BigDecimal period = dosageInstruction.getTiming().getRepeat().getPeriod();
						Integer frequency = dosageInstruction.getTiming().getRepeat().getFrequency();
						// String commonFrequency= "" + frequency + " times per " + period + " " +
						// periodUnit;
						// log.info("MEDICATIONORDER --- Found Frequency: " + commonFrequency);
						// ecrMedication.setFrequency(commonFrequency);
					} else {
						log.info("MEDICATIONORDER --- DOSE NOT FOUND.");
					}
				}

				log.info("MEDICATIONORDER --- ECRCode: " + ecrCode);
				if (!ecr.getPatient().getMedicationProvided().contains(ecrMedication)) {
					log.info("MEDICATIONORDER --- Found New Entry: " + ecrCode);
					ecr.getPatient().getMedicationProvided().add(ecrMedication);
				} else {
					log.info("MEDICATIONORDER --- Didn't Match or found duplicate! " + ecrCode);
				}
				// String periodUnit =
				// dosageInstruction.getTiming().getRepeat().getPeriodUnits();
				// BigDecimal period = dosageInstruction.getTiming().getRepeat().getPeriod();
				// Integer frequency = dosageInstruction.getTiming().getRepeat().getFrequency();
				// /*String commonFrequency= "" + frequency + " times per " + period + " " +
				// periodUnit;
				// log.info("MEDICATIONORDER --- Found Frequency: " + commonFrequency);
				// ecrMedication.setFrequency(commonFrequency); */

				PeriodDt period = medicationOrder.getDispenseRequest().getValidityPeriod();
				if ( period != null && period.getStart() != null ) {
					log.info("MEDICATIONORDER --- Found Validity Period: " + period.getStart().toLocaleString());
					ecrMedication.setDate(period.getStart().toString());
				}
				log.info("MEDICATIONORDER --- ECRCode: " + ecrCode);
				if (!ecr.getPatient().getMedicationProvided().contains(ecrMedication)) {
					log.info("MEDICATIONORDER --- Found New Entry: " + ecrCode);
					ecr.getPatient().getMedicationProvided().add(ecrMedication);
				} else {
					log.info("MEDICATIONORDER --- Didn't Match or found duplicate! " + ecrCode);
				}
				if (medicationOrder.getReason() != null && !medicationOrder.getReason().isEmpty()) {
					if (medicationOrder.getReason() instanceof CodeableConceptDt) {
						handleSingularConditionConceptCode(ecr, (CodeableConceptDt) medicationOrder.getReason());
					}
					if (medicationOrder.getReason() != null && !medicationOrder.getReason().isEmpty()) {
						if (medicationOrder.getReason() instanceof CodeableConceptDt) {
							handleSingularConditionConceptCode(ecr, (CodeableConceptDt) medicationOrder.getReason());
						} else
						if (medicationOrder.getReason() instanceof ResourceReferenceDt) {
							handleSingularCondition(ecr, (ResourceReferenceDt) medicationOrder.getReason());
						}
					}
				} else {
					log.info("MEDICATIONORDER --- Didn't Match  " + ecrCode);
				}
			}
			medications = FHIRClient.getNextPage(medications);
		} while (medications != null);
	}

	void handleMedicationStatements(ECR ecr, IdDt IdDt) {
		Bundle medications = FHIRClient.getMedicationStatements(IdDt);
		do {
			for (Entry entry : medications.getEntry()) {
				CodeableConcept ecrCode = new CodeableConcept();
				MedicationStatement medicationStatement = (MedicationStatement) entry.getResource();
				gatech.edu.STIECR.JSON.Medication ecrMedication = new gatech.edu.STIECR.JSON.Medication();
				log.info("MEDICATIONSTATEMENT  --- Trying medicationOrder: " + medicationStatement.getId());
				IDatatype medicationCodeUntyped = medicationStatement.getMedication();
				log.info("MEDICATIONSTATEMENT  --- medication code element class: " + medicationCodeUntyped.getClass());
				CodeableConceptDt code = null;
				if (medicationCodeUntyped instanceof CodeableConceptDt) {
					code = (CodeableConceptDt) medicationCodeUntyped;
				} else if (medicationCodeUntyped instanceof ResourceReferenceDt) {
					try {
						ResourceReferenceDt medicationReference = (ResourceReferenceDt) medicationCodeUntyped;
						if(!medicationReference.getReference().isEmpty()) {
							log.info("MEDICATIONSTATEMENT --- medication reference Id: " + medicationReference.getReference());
							Medication baseMedication = FHIRClient.getMedicationReference(medicationReference.getReference());
							code = baseMedication.getCode();
						}
						else if(medicationReference.getDisplay() != null) {
							log.info("MEDICATIONSTATEMENT --- medication reference display only: " + medicationReference.getDisplay());
							code = new CodeableConceptDt();
							CodingDt singleDisplayCoding = new CodingDt();
							singleDisplayCoding.setDisplay(medicationReference.getDisplay());
							code.addCoding(new CodingDt());
						}
					}
					catch(InternalErrorException | ResourceNotFoundException e) {
						
					}
				}
				
				if (code != null) {
					log.info("MEDICATIONORDER --- Trying code with this many codings: " + code.getCoding().size());
					for (CodingDt coding : code.getCoding()) {
						log.info("MEDICATIONORDER --- Trying coding: " + coding.getDisplay());
						CodeableConcept concept = FHIRCoding2ECRConcept(coding);
						log.info("MEDICATIONORDER --- Translated to ECRconcept:" + concept.toString());
						ecrMedication.setCode(concept.getcode());
						ecrMedication.setSystem(concept.getsystem());
						ecrMedication.setDisplay(concept.getdisplay());
						ecrCode.setcode(concept.getcode());
						ecrCode.setsystem(concept.getsystem());
						ecrCode.setdisplay(concept.getdisplay());
					}
				}
				if (!medicationStatement.getDosage().isEmpty()) {
					Dosage ecrDosage = new Dosage();
					IDatatype dosageQuantityUntyped = medicationStatement.getDosage().get(0).getQuantity();
					if (dosageQuantityUntyped instanceof SimpleQuantityDt) {
						SimpleQuantityDt dosageQuantity = (SimpleQuantityDt) dosageQuantityUntyped;
						ecrDosage.setValue(dosageQuantity.getValue().toString());
						ecrDosage.setUnit(dosageQuantity.getUnit().toString());
					} else
					if (dosageQuantityUntyped instanceof RangeDt) {
						RangeDt dosageRange = (RangeDt) dosageQuantityUntyped;
						BigDecimal high = dosageRange.getHigh().getValue();
						BigDecimal low = dosageRange.getLow().getValue();
						BigDecimal mean = high.add(low);
						mean = mean.divide(new BigDecimal(2));
						ecrDosage.setValue(mean.toString());
						ecrDosage.setUnit(dosageRange.getHigh().getUnit());
						ecrMedication.setDosage(ecrDosage);
					}
				}
				if (!medicationStatement.getDateAssertedElement().isEmpty()) {
					if (medicationStatement.getDateAsserted() != null) {
						String dateTimeAsString = DateUtil.dateTimeToStdString(medicationStatement.getDateAsserted());
						log.info("MEDICATIONSTATEMENT  --- Found Medication Date: " + dateTimeAsString);
						ecrMedication.setDate(dateTimeAsString);
					}
					log.info("MEDICATIONSTATEMENT  --- ECRCode: " + ecrCode);
				}
				if (!ecr.getPatient().getMedicationProvided().contains(ecrMedication)) {
					log.info("MEDICATIONSTATEMENT  --- Found New Entry: " + ecrCode);
					ecr.getPatient().getMedicationProvided().add(ecrMedication);
				} else {
					log.info("MEDICATIONSTATEMENT  --- Didn't Match or found duplicate! " + ecrCode);
				}
				if (!medicationStatement.getReasonForUse().isEmpty()) {
					if (medicationStatement.getReasonForUse() instanceof CodeableConceptDt) {
						handleSingularConditionConceptCode(ecr,
								(CodeableConceptDt) medicationStatement.getReasonForUse());
					} else
					if (medicationStatement.getReasonForUse() instanceof ResourceReferenceDt) {
						handleSingularCondition(ecr, (ResourceReferenceDt) medicationStatement.getReasonForUse());
					}
				}
			}
			medications = FHIRClient.getNextPage(medications);
		} while (medications != null);
	}

	void handleImmunizations(ECR ecr, IdDt idDt) {
		Bundle immunizations = FHIRClient.getImmunizations(idDt);
		do {
			for (Entry entry : immunizations.getEntry()) {
				Immunization immunization = (Immunization) entry.getResource();
				ImmunizationHistory ecrImmunization = new ImmunizationHistory();
				if (immunization != null && immunization.getVaccineCode().getCoding().size() > 0) {
					ecrImmunization.setCode(immunization.getVaccineCode().getCoding().get(0).getCode());
					ecrImmunization.setSystem(immunization.getVaccineCode().getCoding().get(0).getSystem());
				} else
				if (immunization != null && StringUtils.isNotBlank(immunization.getVaccineCode().getText())) {
					ecrImmunization.setCode(immunization.getVaccineCode().getText());
				} else
				if (immunization != null && StringUtils.isNotBlank(immunization.getText().getDivAsString())) {
					ecrImmunization.setCode(immunization.getText().getDivAsString());
				}
				if(immunization.getDate() != null) {
					ecrImmunization.setDate(DateUtil.dateToStdString(immunization.getDate()));
				}
				if (!ecr.getPatient().getimmunizationHistory().contains(ecrImmunization)) {
					log.info("Adding Immunization For " + idDt.getValueAsString());
					ecr.getPatient().getimmunizationHistory().add(ecrImmunization);
				}
			}
			//immunizations = FHIRClient.getNextPage(immunizations);
			immunizations = null;
		} while (immunizations != null);
	}

	void handleConditions(ECR ecr, IdDt IdDt) {
		Bundle conditions = FHIRClient.getConditions(IdDt);
		do {
			for (Entry entry : conditions.getEntry()) {
				Condition condition = (Condition) entry.getResource();
				handleSingularCondition(ecr, condition);
			}
			conditions = FHIRClient.getNextPage(conditions);
		} while (conditions != null);
	}

	public void handleSingularConditionConceptCode(ECR ecr, CodeableConceptDt code) {
		log.info("CONDITION --- Trying code with this many codings: " + code.getCoding().size());
		for (CodingDt coding : code.getCoding()) {
			log.info("CONDITION --- Trying coding: " + coding.getDisplay());
			CodeableConcept concept = FHIRCoding2ECRConcept(coding);
			log.info("CONDITION --- Translated to ECRconcept:" + concept.toString());
			if (!ecr.getPatient().getsymptoms().contains(concept)) {
				log.info("CONDITION --- SYMPTOM MATCH!" + concept.toString());
				ecr.getPatient().getsymptoms().add(concept);
				break; // Stop once we get a codingDt that matches our list of codes.
			}
		}
	}

	public void handleSingularCondition(ECR ecr, Condition condition) {
		log.info("CONDITION --- Trying condition: " + condition.getId());
		if (condition.getAbatement() != null) {
			Date abatementDate = HAPIFHIRUtil.getDate(condition.getAbatement());
			if (abatementDate != null & abatementDate.compareTo(new Date()) <= 0) {
				log.info("CONDITION --- Found abatement date of: " + abatementDate);
				log.info("CONDITION --- Condition is not current, ignoring condition.");
				return;
			}
		}

		Date onsetDate = HAPIFHIRUtil.getDate(condition.getOnset());
		if (onsetDate == null) {
			onsetDate = condition.getDateRecorded();
		}
		Date ecrDate = null;
		try {
			String onsetDateStr = ecr.getPatient().getdateOfOnset();
			ecrDate = DateUtil.parse(onsetDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		CodeableConceptDt code = condition.getCode();
		log.info("CONDITION --- Trying code with this many codings: " + code.getCoding().size());
		for (CodingDt coding : code.getCoding()) {
			log.info("CONDITION --- Trying coding: " + coding.getDisplay());
			CodeableConcept concept = FHIRCoding2ECRConcept(coding);
			log.info("CONDITION --- Translated to ECRconcept:" + concept.toString());
			log.info("CONDITION ---DIAGNOSIS MATCH!" + concept.toString());
			Diagnosis updatedDiagnosis = new Diagnosis();
			updatedDiagnosis.setCode(concept.getcode());
			updatedDiagnosis.setDisplay(concept.getdisplay());
			updatedDiagnosis.setSystem(concept.getsystem());
			if ((ecrDate == null && onsetDate != null)
					|| (ecrDate != null && onsetDate != null && onsetDate.before(ecrDate))) {
				log.info("CONDITION --- Found onset date of: " + onsetDate);
				log.info("CONDITION --- Eariler date than previously found. Replacing patient onset date.");
				ecr.getPatient().setdateOfOnset(DateUtil.dateTimeToStdString(onsetDate));
				updatedDiagnosis.setDate(DateUtil.dateTimeToStdString(onsetDate));
			} else {
				updatedDiagnosis.setDate(ecr.getPatient().getdateOfOnset());
			}
			ecr.getPatient().getDiagnosis().add(updatedDiagnosis);
			return;
		}
		handleSingularConditionConceptCode(ecr, code);
		// TODO: distinguish between symptom list and diagnosis list here
		// TODO: Map Pregnant from encounters
	}

	public void handleSingularCondition(ECR ecr, ResourceReferenceDt conditionReference) {
		Condition condition = FHIRClient.getConditionById(conditionReference.getReference());
		handleSingularCondition(ecr, condition);
	}

	void handleClaims(ECR ecr, IdDt IdDt) {
		Bundle claims = FHIRClient.getClaims(IdDt);
		do {
			for (Entry entry : claims.getEntry()) {
				Claim claim = (Claim) entry.getResource();
				if (!claim.getCoverage().isEmpty()) {
					log.info("CLAIMS --- Found an example of coverage:");
					Coverage coverage = FHIRClient
							.getCoverageById(claim.getCoverage().get(0).getCoverage().getReference()); // Handling only
																										// the first
																										// coverage
					CodingDt coding = coverage.getType(); // Use the first code
					log.info("CLAIMS --- Found coverage type:" + coding.getDisplay());
					ecr.getPatient().setinsuranceType(
							new CodeableConcept(coding.getCode(), coding.getSystem(), coding.getDisplay()));
				}
			}
			claims = FHIRClient.getNextPage(claims);
		} while (claims != null);
	}

	void handleEncounters(ECR ecr, IdDt IdDt) {
		Bundle encounters = FHIRClient.getEncounters(IdDt);
		do {
			for (Entry entry : encounters.getEntry()) {
				Encounter encounter = (Encounter) entry.getResource();
				for (CodeableConceptDt reason : encounter.getReason()) {
					for (CodingDt coding : reason.getCoding()) {
						CodeableConcept concept = FHIRCoding2ECRConcept(coding);
						if (concept.getsystem().equals("SNOMED CT") && !ecr.getPatient().getsymptoms().contains(concept)) {
							ecr.getPatient()
									.setvisitDateTime(DateUtil.dateTimeToStdString(encounter.getPeriod().getStart()));
						}
						// TODO: Figure out the right strategy for mapping an Onset
						// TODO: distinguish between symptom list and diagnosis list here
						// TODO: Map Pregnant from encounters
					}
				}
			}
			encounters = FHIRClient.getNextPage(encounters);
		} while (encounters != null);
	}

	void handleObservation(ECR ecr, IdDt IdDt) {
		Bundle observations = FHIRClient.getObservations(IdDt);
		do {
			for (Entry entry : observations.getEntry()) {
				Observation observation = (Observation) entry.getResource();
				CodeableConceptDt code = observation.getCode();
				for (CodingDt coding : code.getCoding()) {
					LabOrderCode labOrder = new LabOrderCode();
					labOrder.setcode(coding.getCode());
					labOrder.setdisplay(coding.getDisplay());
					labOrder.setsystem(coding.getSystem());
					LabResult labResult = new LabResult();
					IDatatype untypedValue = observation.getValue();
					if (untypedValue instanceof QuantityDt) {
						labResult.setValue(((QuantityDt) untypedValue).getValue().toString());
					} else
					if (untypedValue instanceof CodeableConcept) {
						labResult.setValue(((CodeableConcept) untypedValue).getdisplay());
					} else
					if (untypedValue instanceof StringDt) {
						labResult.setValue(((StringDt) untypedValue).toString());
					} else
					if (untypedValue instanceof RangeDt) {
						RangeDt range = (RangeDt) untypedValue;
						labResult.setValue("High:" + range.getHigh() + ";low:" + range.getLow());
					} else
					if (untypedValue instanceof RatioDt) {
						RatioDt ratio = (RatioDt) untypedValue;
						labResult.setValue(
								ratio.getNumerator().toString() + "/" + ratio.getDenominator().toString());
					} else
					if (untypedValue instanceof SampledDataDt) {
						labResult.setValue(((SampledDataDt) untypedValue).getData());
					} else
					if (untypedValue instanceof TimeDt) {
						labResult.setValue(((TimeDt) untypedValue).getValue());
					} else
					if (untypedValue instanceof DateTimeDt) {
						labResult.setValue(((DateTimeDt) untypedValue).getValueAsString());
					} else
					if (untypedValue instanceof PeriodDt) {
						PeriodDt period = (PeriodDt) untypedValue;
						labResult.setValue(
								"Start:" + period.getStart().toString() + ";End" + period.getEnd().toString());
					}
					labOrder.getLaboratory_Results().add(labResult);
				}
			}
			observations = FHIRClient.getNextPage(observations);
		} while (observations != null);
	}

	void handleProcedure(ECR ecr, IdDt IdDt) {
		Bundle procedures = FHIRClient.getProcedures(IdDt);
		do {
			for (Entry entry : procedures.getEntry()) {
				Procedure procedure = (Procedure) entry.getResource();
				if (procedure.getReason() != null && !procedure.getReason().isEmpty()) {
					if (procedure.getReason() instanceof CodeableConceptDt) {
						handleSingularConditionConceptCode(ecr, (CodeableConceptDt) procedure.getReason());
					} else
					if (procedure.getReason() instanceof ResourceReferenceDt) {
						handleSingularCondition(ecr, (ResourceReferenceDt) procedure.getReason());
					}
				}

			}
			procedures = FHIRClient.getNextPage(procedures);
		} while (procedures != null);
	}

	void updateParentGuardian(ParentGuardian pg, RelatedPerson rp) {
		for (ContactPointDt contact : rp.getTelecom()) {
			if (contact.getSystem().equals("Phone") && pg.getphone().isEmpty()) {
				pg.setphone(contact.getValue());
			} else
			if (contact.getSystem().equals("Email") && pg.getemail().isEmpty()) {
				pg.setemail(contact.getValue());
			}
		}
	}

	public void handleEDRSBody(ECR ecr, ObjectNode body) {
		body.put("armedForcesService.amredForces.Service", "No");
		body.put("autopsyAvailableToCompleteCauseOfDeath.autopsyAvailableToCompleteCauseOfDeath", "No");
		body.put("autopsyPerformed.autopsyPerformed", "No");
		body.put("certifierType.certifierType", "Physician (Pronouncer and Certifier)");
		
		body.put("cod.immediate", ecr.getPatient().getDiagnosis().get(0) == null ? "Example Immediate COD" : ecr.getPatient().getDiagnosis().get(0).getDisplay());
		body.put("cod.immediateInt","minutes");
		body.put("cod.under1", ecr.getPatient().getDiagnosis().get(1) == null ? "Example Immediate COD" : ecr.getPatient().getDiagnosis().get(1).getDisplay());
		body.put("cod.under1Int","2 hours");
		body.put("cod.under2", ecr.getPatient().getDiagnosis().get(2) == null ? "Example Immediate COD" : ecr.getPatient().getDiagnosis().get(2).getDisplay());
		body.put("cod.under2Int","6 months");
		body.put("cod.under3", ecr.getPatient().getDiagnosis().get(3) == null ? "Example Immediate COD" : ecr.getPatient().getDiagnosis().get(3).getDisplay());
		body.put("cod.under3Int","15 years");
		
		body.put("dateOfBirth.dateOfBirth", ecr.getPatient().getDiagnosis().get(3) == null ? "Example Immediate COD" : ecr.getPatient().getbirthDate().isEmpty() ? "1970-01-01" : ecr.getPatient().getbirthDate());
		body.put("dateOfBirth.dateOfDeath", "2018-05-04");
		body.put("datePronouncedDead.datePronouncedDead", "2018-05-04");
		body.put("deathResultedFromInjuryAtWork.deathResultedFromInjuryAtWork", "No");
		
		AddressDt address = HAPIFHIRUtil.stringToAddress(ecr.getPatient().getstreetAddress());
		body.put("decedentAddress.city", address.getCity().isEmpty() ? "1 Example Street" : address.getCity());
		body.put("decedentAddress.state",  address.getState().isEmpty() ? "Massachusetts" : address.getState());
		body.put("decedentAddress.zip",  address.getPostalCode().isEmpty() ? "02101" : address.getPostalCode());
		body.put("decedentName.firstName", ecr.getPatient().getname().getgiven());
		body.put("decedentName.lastName", ecr.getPatient().getname().getfamily());
		body.put("decedentName.middleName", "Middle");
		
		body.put("detailsOfInjury", "Example details of injury");
		body.put("detailsOfInjuryDate.detailsOfInjuryDate", "2018-05-04");
		body.put("detailsOfInjuryLocation.city", address.getCity().isEmpty() ? "Boston" : address.getCity());
		body.put("detailsOfInjuryLocation.state", address.getState().isEmpty() ? "Massachusetts" : address.getState());
		body.put("detailsOfInjuryLocation.street", address.getLine().get(0).isEmpty() ? "1 Example Street" : address.getLine().get(0).toString());
		body.put("detailsOfInjuryLocation.zip", address.getPostalCode().isEmpty() ? "02101" : address.getPostalCode());
		body.put("detailsOfInjuryTime.detailsOfInjuryTime", "12:00");
		body.put("didTobaccoUseContributeToDeath.didTobaccoUseContributeToDeath", "No");
		body.put("education.education", "High School Diploma");
		body.put("FuneralFacility.city", "Boston");
		body.put("FuneralFacility.name", "Example Funeral Home");
		body.put("FuneralFacility.state", "Massachusetts");
		body.put("FuneralFacility.street", "2 Example Street");
		body.put("FuneralFacility.zip", "02101");
		body.put("hispanicOrigin.hispanicOrigin.option", ecr.getPatient().getethnicity().getdisplay().equals("Hispanic") ? "Yes" : "No");
		body.put("hispanicOrigin.hispanicOrigin.specify", "");
		body.put("ifTransInjury.ifTransInjury", "Other");
		body.put("kindOfBuisness.kindOfBusiness", "Example kind of business");
		body.put("locationOfDeath.city", address.getCity().isEmpty() ? "1 Example Street" : address.getCity());
		body.put("locationOfDeath.name", "Residence");
		body.put("locationOfDeath.state", address.getState().isEmpty() ? "Massachusetts" : address.getState());
		body.put("locationOfDeath.street", address.getLine().get(0).isEmpty() ? "1 Example Street" : address.getLine().get(0).toString());
		body.put("locationOfDeath.zip", address.getPostalCode().isEmpty() ? "02101" : address.getPostalCode());
		body.put("mannerOfDeath.mannerOfDeath", "Accident");
		body.put("maritalStatus.maritalStatue", "Never married");
		body.put("meOrCoronerContacted.meOrCoronerContacted", "No");
		body.put("methodOfDisposition.methodOfDisposition", "Burial");
		body.put("motherName.lastName", ecr.getPatient().getname().getfamily());
		body.put("personCompletingCauseOfDeathAddress.city", "Bedford");
		body.put("personCompletingCauseOfDeathAddress.state", "Massachusetts");
		body.put("personCompletingCauseOfDeathAddress.street", "100 Example St.");
		body.put("personCompletingCauseOfDeathAddress.zip", "01730");
		body.put("personCompletingCauseOfDeathName.firstName", "Example");
		body.put("personCompletingCauseOfDeathName.lastName", "Doctor");
		body.put("personCompletingCauseOfDeathName.middleName", "Middle");
		body.put("placeOfBirth.city", "Boston");
		body.put("placeOfBirth.country", "United States");
		body.put("placeOfBirth.state", "Massachusetts");
		body.put("pregnancyStatus.pregnanyStatus", "Not pregnant within past year");
		body.put("race.race.option", "Known");
		body.put("race.race.specify", "[\\\"White\\\",\\\"Native Hawaiian or Other Pacific Islander\\\",\\\"Asian\\\",\\\"American Indian or Alaskan Native\\\",\\\"Black or African American\\\"]");
		body.put("sex.sex", ecr.getPatient().getsex());
		//ssn allready taken care of
		body.put("timeOfDeath", "12:00");
		body.put("timePronouncedDead.timePronouncedDead", "12:00");
		body.put("usualOccupation.usualOccupation", "Example usual occupation");
	}
	public static CodeableConcept FHIRCoding2ECRConcept(CodingDt fhirCoding) {
		CodeableConcept ecrConcept = new CodeableConcept();
		ecrConcept.setcode(fhirCoding.getCode() == null ? "" : fhirCoding.getCode());
		ecrConcept.setsystem(fhirCoding.getSystem() == null ? "" : fhirCoding.getSystem());
		if(fhirCoding.getSystem() != null && !fhirCoding.getSystem().isEmpty()) {
			if (fhirCoding.getSystem().equals("http://snomed.info/sct")) {
				ecrConcept.setsystem("SNOMED CT");
			} else
			if (fhirCoding.getSystem().equals("http://www.nlm.nih.gov/research/umls/rxnorm")) {
				ecrConcept.setsystem("RxNorm");
			}
		}
		ecrConcept.setdisplay(fhirCoding.getDisplay());
		return ecrConcept;
	}
}
