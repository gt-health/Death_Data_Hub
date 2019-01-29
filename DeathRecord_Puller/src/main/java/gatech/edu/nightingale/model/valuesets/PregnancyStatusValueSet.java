package gatech.edu.nightingale.model.valuesets;

public class PregnancyStatusValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "PHC1260", "PHC1260",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/causeOfDeath/cs/PregnancyStatusCS");
		ValueSetUtil.addMapping(map, "PHC1261", "PHC1261",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/causeOfDeath/cs/PregnancyStatusCS");
		ValueSetUtil.addMapping(map, "PHC1262", "PHC1262",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/causeOfDeath/cs/PregnancyStatusCS");
		ValueSetUtil.addMapping(map, "PHC1263", "PHC1263",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/causeOfDeath/cs/PregnancyStatusCS");
		ValueSetUtil.addMapping(map, "PHC1264", "PHC1264",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/causeOfDeath/cs/PregnancyStatusCS");
		ValueSetUtil.addMapping(map, "NA", "NA", "http://hl7.org/fhir/v3/NullFlavor");
	}
}