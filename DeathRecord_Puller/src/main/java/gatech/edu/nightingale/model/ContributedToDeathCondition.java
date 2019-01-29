package gatech.edu.nightingale.model;

import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionClinicalStatus;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "ContributedToDeathCondition", profile = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-causeOfDeath-ContributedToDeathCondition")
public class ContributedToDeathCondition extends Condition {
	private static final long serialVersionUID = 1L;

	public ContributedToDeathCondition() {
		super();
		this.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
	}

	public ContributedToDeathCondition(Reference subject, DateTimeType onset) {
		this();
		this.setSubject(subject);
		this.setOnset(onset);
	}
}