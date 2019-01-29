package gatech.edu.nightingale.model;

import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

@ResourceDef(name = "DeathRecordContents", profile = "https://nightingaleproject.github.io/fhir-death-record/guide/StructureDefinition-sdr-deathRecord-DeathRecordContents.html")
public class DeathRecordContents extends Composition {
	private static final long serialVersionUID = 1L;

	DeathRecordContents() {
		super();
		this.setType(new CodeableConcept().addCoding(new Coding("http://loinc.org", "64297-5","")));
		this.addSection();
	}

	public SectionComponent addSection() {
		SectionComponent section = new SectionComponent();
		section.setCode(new CodeableConcept().addCoding(new Coding("http://loinc.org", "64297-5","")));
		return section;
	}

	public void addEntry(Reference reference) {
		List<Reference> entries = this.getSection().get(0).getEntry();
		entries.add(reference);
	}
}
