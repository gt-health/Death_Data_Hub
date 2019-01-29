package gatech.edu.nightingale.model.valuesets;

public class TransportRelationshipsValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "Vehicle Driver", "236320001", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Passenger", "257500003", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Pedestrian", "257518000", "http://snomed.info/sct");
		ValueSetUtil.addMapping(map, "Other", "OTH", "http://hl7.org/fhir/v3/NullFlavor");
	}
}