package gatech.edu.nightingale.model.valuesets;

public class DispositionValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "Donation", "449951000124101", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Burial", "449971000124106", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Cremation", "449961000124104", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Entombment", "449931000124108", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Removal from State", "449941000124103", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Hospital Disposition", "455401000124109", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Unknown", "UNK", "http://hl7.org/fhir/v3/NullFlavor");
		ValueSetUtil.addMapping(map, "Other", "OTH", "http://hl7.org/fhir/v3/NullFlavor");
	}
}
