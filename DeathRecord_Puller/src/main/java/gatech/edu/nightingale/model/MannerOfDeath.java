package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "MannerOfDeath", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-MannerOfDeath")
public class MannerOfDeath extends Observation {
	private static final long serialVersionUID = 1L;

	// TODO: Add valueset checking value
	// https://nightingaleproject.github.io/fhir-death-record/guide/ValueSet-sdr-causeOfDeath-MannerOfDeathVS.html
	public MannerOfDeath() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "69449-7","")));
	}

	public MannerOfDeath(Reference subject, CodeableConcept valueCodeableConcept) {
		this();
		this.setSubject(subject);
		this.setValue(valueCodeableConcept);
	}

}