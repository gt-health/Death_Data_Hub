package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "DetailsOfInjury", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-DetailsOfInjury")
public class DetailsOfInjury extends Observation {
	private static final long serialVersionUID = 1L;

	@Child(name = "PlaceOfInjuryExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-PlaceOfInjury-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR PlaceOfInjury Extension")
	private StringType placeOfInjuryExtension;
	@Child(name = "PostalAddressExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/shr-core-PostalAddress", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR PostalAddress Extension")
	private PostalAddress postalAddressExtension;

	public DetailsOfInjury() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "11374-6","")));
	}

	public DetailsOfInjury(Reference subject, DateTimeType effectiveDateTime, StringType placeOfInjury,
			PostalAddress postalAddress, StringType valueString) {
		this();
		this.setSubject(subject);
		this.setEffective(effectiveDateTime);
		this.setPlaceOfInjuryExtension(placeOfInjury);
		this.setPostalAddressExtension(postalAddress);
		this.setValue(valueString);
	}

	public StringType getPlaceOfInjuryExtension() {
		return placeOfInjuryExtension;
	}

	public void setPlaceOfInjuryExtension(StringType placeOfInjuryExtension) {
		this.placeOfInjuryExtension = placeOfInjuryExtension;
	}

	public PostalAddress getPostalAddressExtension() {
		return postalAddressExtension;
	}

	public void setPostalAddressExtension(PostalAddress postalAddressExtension) {
		this.postalAddressExtension = postalAddressExtension;
	}
}