package gatech.edu.DeathRecordPuller.Controller;

import java.util.ArrayList;
import java.util.Map;

import org.hl7.fhir.dstu3.model.Address.AddressType;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Contract;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient.ContactComponent;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UnsignedIntType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UnsignedIntDt;
import ca.uhn.fhir.parser.IParser;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestAddress;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestCertifier;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestDeathRecord;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestDecedent;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestDecedent.MaritalStatusAtDeathEnum;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestRelatedPerson;
import gatech.edu.DeathRecordPuller.EDRS.model.IngestRelatedPerson.TypeEnum;
import gatech.edu.DeathRecordPuller.nightingale.service.NightingaleService;
import gatech.edu.nightingale.model.Certifier;
import gatech.edu.nightingale.model.Decedent;
import gatech.edu.nightingale.model.Disposition;
import gatech.edu.nightingale.model.Facility;
import gatech.edu.nightingale.model.PlaceOfDeath;
import gatech.edu.nightingale.model.PostalAddress;
import gatech.edu.nightingale.model.valuesets.CertifierTypeValueSet;
import gatech.edu.nightingale.model.valuesets.ContributoryTobaccoUseValueSet;
import gatech.edu.nightingale.model.valuesets.DispositionValueSet;
import gatech.edu.nightingale.model.valuesets.EducationValueSet;
import gatech.edu.nightingale.model.valuesets.IDTypeValueSet;
import gatech.edu.nightingale.model.valuesets.MannerOfDeathValueSet;
import gatech.edu.nightingale.model.valuesets.PlaceOfDeathTypeValueSet;
import gatech.edu.nightingale.model.valuesets.PregnancyStatusValueSet;
import gatech.edu.nightingale.model.valuesets.TransportRelationshipsValueSet;

@CrossOrigin()
@RestController
public class NightingaleController {
	protected static final String nullFlavorSystem = "http://hl7.org/fhir/v3/NullFlavor";
	
	protected NightingaleService nightingaleService;
	
	protected CertifierTypeValueSet certifierTypeValueSet;
	protected ContributoryTobaccoUseValueSet contributoryTobaccoUseValueSet;
	protected DispositionValueSet dispositionValueSet;
	protected EducationValueSet educationValueSet;
	protected IDTypeValueSet iDTypeValueSet;
	protected MannerOfDeathValueSet mannerOfDeathValueSet;
	protected PlaceOfDeathTypeValueSet placeOfDeathTypeValueSet;
	protected PregnancyStatusValueSet pregnancyStatusValueSet;
	protected TransportRelationshipsValueSet transportRelationshipsValueSet;
	
	protected  IParser jsonParserDstu3;
	@Autowired()
	public NightingaleController(NightingaleService nightingaleService) {
		this.nightingaleService = nightingaleService;
		certifierTypeValueSet = new CertifierTypeValueSet();
		certifierTypeValueSet.init();
		contributoryTobaccoUseValueSet = new ContributoryTobaccoUseValueSet();
		contributoryTobaccoUseValueSet.init();
		dispositionValueSet = new DispositionValueSet();
		dispositionValueSet.init();
		educationValueSet = new EducationValueSet();
		educationValueSet.init();
		iDTypeValueSet = new IDTypeValueSet();
		iDTypeValueSet.init();
		mannerOfDeathValueSet = new MannerOfDeathValueSet();
		mannerOfDeathValueSet.init();
		placeOfDeathTypeValueSet = new PlaceOfDeathTypeValueSet();
		placeOfDeathTypeValueSet.init();
		pregnancyStatusValueSet = new PregnancyStatusValueSet();
		pregnancyStatusValueSet.init();
		transportRelationshipsValueSet = new TransportRelationshipsValueSet();
		transportRelationshipsValueSet.init();
		this.jsonParserDstu3 = FhirContext.forDstu3().newJsonParser().setPrettyPrint(true);
	}
	
	@RequestMapping(value = "/EDRS", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> EDRS(@RequestBody() IngestDeathRecord input){
		Bundle deathRecord = convertIngestDeathRecordToNightingale(input);
		String deathRecordJson = jsonParserDstu3.encodeResourceToString(deathRecord);
		nightingaleService.submitEDRS(deathRecordJson);
		return new ResponseEntity<String>(deathRecordJson,HttpStatus.OK);
	}
	@RequestMapping(value = "/testEDRS", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> testEDRS(@RequestBody() IngestDeathRecord input){
		Bundle output = convertIngestDeathRecordToNightingale(input);
		return new ResponseEntity<String>(jsonParserDstu3.encodeResourceToString(output),HttpStatus.OK);
	}
	
	public Bundle convertIngestDeathRecordToNightingale(IngestDeathRecord input) {
		Bundle output = new Bundle();
		output.setType(BundleType.DOCUMENT);
		Decedent decedent = convertIngestDecedentRecordToNightingale(input.getDecedent());
		BundleEntryComponent decedentEntry = new BundleEntryComponent();
		decedentEntry.setResource(decedent);
		output.addEntry(decedentEntry);
		return output;
	}
	
	public Decedent convertIngestDecedentRecordToNightingale(IngestDecedent inputDecedent) {
		Decedent output = new Decedent();
		output.setDeceased(new BooleanType(true));
		Map<String,CodeableConcept> idMap = iDTypeValueSet.map;
		Identifier identifier = output.addIdentifier();
		identifier.setValue(inputDecedent.getId());
		identifier.setType(iDTypeValueSet.get(inputDecedent.getIdtype().getValue()));
		PostalAddress decedentAddress = convertIngestAddressToNightingale(inputDecedent.getAddress());
		output.getAddress().add(decedentAddress);
		
		for(IngestRelatedPerson relationship:inputDecedent.getRelations()) {
			ContactComponent contact = new ContactComponent();
			String relationshipValue = "N";
			if(relationship.getType().equals(TypeEnum.INFORMANT)) {
				relationshipValue = "CP";
			}
			contact.addRelationship().addCoding(new Coding(relationshipValue,"http://hl7.org/fhir/v2/0131",""));
			//TODO: Add Contact information
			HumanName name = new HumanName();
			name.setText(relationship.getName());
			if(relationship.getName().indexOf(" ") != -1) {
				ArrayList<StringType> givens = new ArrayList<StringType>();
				givens.add(new StringType(relationship.getName().substring(0,relationship.getName().indexOf(' ')-1)));
				name.setGiven(givens);
				name.setFamily(relationship.getName().substring(relationship.getName().indexOf(' ')));
			}
			contact.setName(name);
			ContactPoint telecomPoint = new ContactPoint();
			telecomPoint.setValue(relationship.getTelecom());
			contact.addTelecom(telecomPoint);
			contact.setAddress(convertIngestAddressToNightingale(relationship.getAddress()));
			output.addContact(contact);
		}
		
		output.setBirthSex(new CodeType(inputDecedent.getBirthsex().getValue()));
		//output.setEthnicity(input.getEthnicity()); TODO: Handle Ethnicity us-core correctly
		//output.setRace(input.getRace()); TODO: Handle Race us-core correctly
		output.setAgeExtension(new UnsignedIntType(inputDecedent.getAge()));
		output.setBirthplaceExtension(convertIngestAddressToNightingale(inputDecedent.getBirthplace()));
		output.setServedInArmedForcesExtension(new BooleanType(inputDecedent.isServedInArmedForces()));
		String martialStatusCodeSystem = "http://hl7.org/fhir/v3/MaritalStatus";
		if(inputDecedent.getMaritalStatusAtDeath().equals(MaritalStatusAtDeathEnum.UNK)) {
			martialStatusCodeSystem = nullFlavorSystem;
		}
		output.setMaritalStatusAtDeathExtension(new CodeableConcept().addCoding(new Coding(inputDecedent.getMaritalStatusAtDeath().getValue(),martialStatusCodeSystem,"")));
		
		PlaceOfDeath placeOfDeath = new PlaceOfDeath();
		placeOfDeath.setPlaceOfDeathTypeExtension(placeOfDeathTypeValueSet.get(inputDecedent.getPlaceOfDeathType().getValue()));
		placeOfDeath.setFacilityNameExtension(new StringType(inputDecedent.getPlaceOfDeath().getText()));
		placeOfDeath.setPostalAddressExtension(convertIngestAddressToNightingale(inputDecedent.getPlaceOfDeath()));
		output.setPlaceOfDeathExtension(placeOfDeath);
		
		Disposition disposition = new Disposition();
		disposition.setDispositionTypeExtension(dispositionValueSet.get(inputDecedent.getDisposition().getType().getValue()));
		Facility dispositionFacility = new Facility();
		dispositionFacility.setFacilityNameExtension(new StringType(inputDecedent.getDisposition().getDispositionFacility().getName()));
		dispositionFacility.setPostalAddressExtension(convertIngestAddressToNightingale(inputDecedent.getDisposition().getDispositionFacility().getLocation()));
		disposition.setDispositionFacilityExtension(dispositionFacility);
		Facility funeralFacility = new Facility();
		funeralFacility.setFacilityNameExtension(new StringType(inputDecedent.getDisposition().getFuneralFacility().getName()));
		funeralFacility.setPostalAddressExtension(convertIngestAddressToNightingale(inputDecedent.getDisposition().getFuneralFacility().getLocation()));
		disposition.setFuneralFacilityExtension(dispositionFacility);
		
		output.setEducationExtension(educationValueSet.get(inputDecedent.getEducation().getValue()));
		//TODO: Add Occupation information to model
		//output.occ
		return output;
	}
	
	public void convertIngestCertifierToNightingale(IngestCertifier inputCertifier) {
		Certifier output = new Certifier();
	}
	
	public PostalAddress convertIngestAddressToNightingale(IngestAddress inputAddress) {
		PostalAddress output = new PostalAddress();
		output.setType(AddressType.valueOf(inputAddress.getType().getValue().toUpperCase()));
		output.setText(inputAddress.getText());
		for(String line : inputAddress.getLine()) {
			output.addLine(line);
		}
		output.setCity(inputAddress.getCity());
		output.setDistrict(inputAddress.getDistrict());
		output.setState(inputAddress.getState());
		output.setPostalCode(inputAddress.getPostalCode());
		output.setCountry(inputAddress.getCountry());
		output.setInsideCityLimits(new BooleanType(inputAddress.getInsideCityLimits()));
		return output;
	}
}
