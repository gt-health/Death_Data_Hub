package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "MedicalExaminerContacted", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-MedicalExaminerContacted")
public class MedicalExaminerContacted extends Observation {
	private static final long serialVersionUID = 1L;

	// TODO: Add valueset checking value
	// https://nightingaleproject.github.io/fhir-death-record/guide/ValueSet-sdr-causeOfDeath-MannerOfDeathVS.html
	public MedicalExaminerContacted() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "74497-9","")));
	}

	public MedicalExaminerContacted(Reference subject, BooleanType valueBoolean) {
		this();
		this.setSubject(subject);
		this.setValue(valueBoolean);
	}

}