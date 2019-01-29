package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "PostalAddress")
public class PostalAddress extends Address {
	private static final long serialVersionUID = 1L;

	@Child(name = "InsideCityLimitsExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/shr-core-InsideCityLimits-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR InsideCityLimits Extension")
	private BooleanType insideCityLimits;

	public PostalAddress() {
		super();
		this.setType(AddressType.POSTAL);
	}

	public PostalAddress(Reference subject, CodeableConcept valueCodeableConcept) {
		this();
		this.setInsideCityLimits(new BooleanType(false));
	}

	public BooleanType getInsideCityLimits() {
		return insideCityLimits;
	}

	public void setInsideCityLimits(BooleanType insideCityLimits) {
		this.insideCityLimits = insideCityLimits;
	}
	
}