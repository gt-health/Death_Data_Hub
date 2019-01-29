package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.DateTimeDt;

@ResourceDef(name = "ActualOrPresumedDateOfDeath", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-ActualOrPresumedDateOfDeath")
public class ActualOrPresumedDateOfDeath extends Observation {
	private static final long serialVersionUID = 1L;

	public ActualOrPresumedDateOfDeath() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "81956-5","")));
	}

	public ActualOrPresumedDateOfDeath(DateTimeType valueDateTime) {
		this();
		this.setValue(valueDateTime);
	}
}