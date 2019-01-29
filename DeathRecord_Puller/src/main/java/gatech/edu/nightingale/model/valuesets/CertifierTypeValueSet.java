package gatech.edu.nightingale.model.valuesets;

public class CertifierTypeValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "physician", "434641000124105", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "physician-pronouncer", "434651000124107", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "coroner", "310193003", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Medical Examiner", "440051000124108", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "None", "OTH", "http://hl7.org/fhir/v3/NullFlavor");
	}
}
