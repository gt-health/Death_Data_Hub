package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.BooleanDt;

@ResourceDef(name = "AutopsyPerformed", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-AutopsyPerformed")
public class AutopsyPerformed extends Observation {
	private static final long serialVersionUID = 1L;

	public AutopsyPerformed() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "85699-7","")));
	}

	public AutopsyPerformed(BooleanType valueBoolean) {
		this();
		this.setValue(valueBoolean);
	}
}