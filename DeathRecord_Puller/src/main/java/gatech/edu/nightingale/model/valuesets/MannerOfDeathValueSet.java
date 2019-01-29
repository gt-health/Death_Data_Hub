package gatech.edu.nightingale.model.valuesets;

public class MannerOfDeathValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "Hospital", "63238001", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Home", "440081000124100", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Hospice", "440071000124103", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Hospital-Arrival", "16983000", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Hospital-Emergency", "450391000124102", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Nursing Home", "450381000124100", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Unknown", "UNK", "http://hl7.org/fhir/v3/NullFlavor");
		ValueSetUtil.addMapping(map, "Other", "OTH", "http://hl7.org/fhir/v3/NullFlavor");
	}
}