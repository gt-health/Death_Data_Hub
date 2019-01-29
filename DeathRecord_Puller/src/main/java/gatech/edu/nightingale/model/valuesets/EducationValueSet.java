package gatech.edu.nightingale.model.valuesets;

public class EducationValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "8th grade", "PHC1448",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Some highschool", "PHC1449",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Highschool", "PHC1450",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Some college", "PHC1451",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Associate Degree", "PHC1452",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Bachelor's Degree", "PHC1453",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Master's Degree", "PHC1454",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Doctorate", "PHC1455",
				"http://github.com/nightingaleproject/fhirDeathRecord/sdr/decedent/cs/EducationCS");
		ValueSetUtil.addMapping(map, "Unknown", "UNK", "http://hl7.org/fhir/v3/NullFlavor");
	}
}
