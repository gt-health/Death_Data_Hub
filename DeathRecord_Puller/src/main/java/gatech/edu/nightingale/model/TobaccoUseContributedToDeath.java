package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "TobaccoUseContributedToDeath", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-TobaccoUseContributedToDeath")
public class TobaccoUseContributedToDeath extends Observation {
	private static final long serialVersionUID = 1L;

	public TobaccoUseContributedToDeath() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "69443-0","")));
	}

	public TobaccoUseContributedToDeath(Reference subject, BooleanType valueBoolean) {
		this();
		this.setSubject(subject);
		this.setValue(valueBoolean);
	}
}