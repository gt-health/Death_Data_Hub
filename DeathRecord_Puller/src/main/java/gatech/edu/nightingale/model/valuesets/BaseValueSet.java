package gatech.edu.nightingale.model.valuesets;

import java.util.Map;
import java.util.TreeMap;

import org.hl7.fhir.dstu3.model.CodeableConcept;

public class BaseValueSet {
	public Map<String, CodeableConcept> map;

	public BaseValueSet() {
		map = new TreeMap<String, CodeableConcept>(String.CASE_INSENSITIVE_ORDER);
	}
	
	public CodeableConcept get(String key) {
		return map.get(key);
	}
}
