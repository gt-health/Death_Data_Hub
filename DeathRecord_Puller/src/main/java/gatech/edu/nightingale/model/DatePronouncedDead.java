package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;


@ResourceDef(name = "DatePronouncedDead", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-DatePronouncedDead")
public class DatePronouncedDead extends Observation {
	private static final long serialVersionUID = 1L;

	public DatePronouncedDead() {
		super();
		this.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "80616-6","")));
	}

	public DatePronouncedDead(Reference subject, DateTimeType valueDateTime) {
		this();
		this.setSubject(subject);
		this.setValue(valueDateTime);
	}

}