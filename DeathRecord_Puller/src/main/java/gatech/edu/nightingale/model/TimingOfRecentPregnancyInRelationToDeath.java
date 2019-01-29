package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "TimingOfRecentPregnancyInRelationToDeath", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-TimingOfRecentPregnancyInRelationToDeath")
public class TimingOfRecentPregnancyInRelationToDeath extends Observation {
	private static final long serialVersionUID = 1L;

	// TODO: Add valueset checking value
	// https://nightingaleproject.github.io/fhir-death-record/guide/ValueSet-sdr-causeOfDeath-PregnancyStatusVS.html
	public TimingOfRecentPregnancyInRelationToDeath() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "69442-2","")));
	}

	public TimingOfRecentPregnancyInRelationToDeath(Reference subject,
			CodeableConcept valueCodeableConcept) {
		this();
		this.setSubject(subject);
		this.setValue(valueCodeableConcept);
	}
}