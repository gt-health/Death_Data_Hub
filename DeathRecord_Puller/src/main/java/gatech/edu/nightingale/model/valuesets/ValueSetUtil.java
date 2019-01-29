package gatech.edu.nightingale.model.valuesets;

import java.util.Map;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;

public class ValueSetUtil {
	public static void addMapping(Map<String, CodeableConcept> map, String basicName, String code, String system) {
		CodeableConcept codeableConcept = new CodeableConcept().addCoding(new Coding(system, code,""));
		map.put(basicName, codeableConcept);
	}
}
