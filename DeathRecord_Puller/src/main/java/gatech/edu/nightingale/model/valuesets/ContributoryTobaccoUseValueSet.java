package gatech.edu.nightingale.model.valuesets;

public class ContributoryTobaccoUseValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "Yes", "373066001", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "No", "373067005", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Probably", "2931005", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Unkown", "UNK", "http://hl7.org/fhir/v3/NullFlavor");
		ValueSetUtil.addMapping(map, "Not Asked", "NASK", "http://hl7.org/fhir/v3/NullFlavor");
	}
}
