package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Practitioner;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "Certifier", profile = "https://nightingaleproject.github.io/fhir-death-record/guide/StructureDefinition-sdr-deathRecord-Certifier.html")
public class Certifier extends Practitioner {
	private static final long serialVersionUID = 1L;

	@Child(name = "certifierTypeExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-deathRecord-CertifierType-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "Certifier Type Extension")
	private StringType certifierTypeExtension;
	
	public Certifier() {
		super();
	}

	public StringType getCertifierTypeExtension() {
		return certifierTypeExtension;
	}

	public void setCertifierTypeExtension(StringType certifierTypeExtension) {
		this.certifierTypeExtension = certifierTypeExtension;
	}

}