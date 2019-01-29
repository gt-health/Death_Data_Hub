package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "CauseOfDeathCondition", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-CauseOfDeathCondition")
public class CauseOfDeathCondition extends Condition {
	private static final long serialVersionUID = 1L;

	public CauseOfDeathCondition() {
		super();
		this.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
	}

	public CauseOfDeathCondition(Reference subject, DateTimeType onset) {
		this();
		this.setSubject(subject);
		this.setOnset(onset);
	}

}