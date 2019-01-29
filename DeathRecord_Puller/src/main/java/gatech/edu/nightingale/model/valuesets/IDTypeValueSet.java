package gatech.edu.nightingale.model.valuesets;

public class IDTypeValueSet extends BaseValueSet {
	public void init() {
		ValueSetUtil.addMapping(map, "DL", "DL", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "PPN", "PPN", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "BRN", "BRN", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "MR", "MR", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "MCN", "MCN", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "EN", "EN", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "TAX", "TAX", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "NIIP", "NIIP", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "PRN", "PRN", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "MD", "MD", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "DR", "DR", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "ACSN", "ACSN", "http://hl7.org/fhir/v2/0203");
		ValueSetUtil.addMapping(map, "UDI", "UDI", "http://hl7.org/fhir/identifier-type");
		ValueSetUtil.addMapping(map, "SNO", "SNO", "http://hl7.org/fhir/identifier-type");
		ValueSetUtil.addMapping(map, "SB", "SB", "http://hl7.org/fhir/identifier-type");
		ValueSetUtil.addMapping(map, "PLAC", "PLAC", "http://hl7.org/fhir/identifier-type");
		ValueSetUtil.addMapping(map, "FILL", "FILL", "http://hl7.org/fhir/identifier-type");
	}
}
