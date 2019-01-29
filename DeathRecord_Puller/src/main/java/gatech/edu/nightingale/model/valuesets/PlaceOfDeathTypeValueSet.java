package gatech.edu.nightingale.model.valuesets;

public class PlaceOfDeathTypeValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "Natural", "38605008", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Accident", "7878000", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Suicide", "44301001", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Homicide", "27935005", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Pending Investigation", "185973002", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Could not be determined", "65037004", "http://snomed.info/sct");
	}
}
