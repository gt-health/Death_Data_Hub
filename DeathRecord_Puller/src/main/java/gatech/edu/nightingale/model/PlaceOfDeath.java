package gatech.edu.nightingale.model;

import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.StringType;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IExtension;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.ElementUtil;

@Block()
public class PlaceOfDeath extends BaseIdentifiableElement implements IExtension {
	private static final long serialVersionUID = 1L;

	@Child(name = "PlaceOfDeathTypeExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-decedent-PlaceOfDeathType-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR PlaceOfDeathType Extension")
	private CodeableConcept placeOfDeathTypeExtension;
	@Child(name = "FacilityNameExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-decedent-FacilityName-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR FacilityName Extension")
	private StringType facilityNameExtension;
	@Child(name = "PostalAddressExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/shr-core-PostalAddress-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR PostalAddress Extension")
	private PostalAddress postalAddressExtension;

	public CodeableConcept getPlaceOfDeathTypeExtension() {
		return placeOfDeathTypeExtension;
	}

	public void setPlaceOfDeathTypeExtension(CodeableConcept placeOfDeathTypeExtension) {
		this.placeOfDeathTypeExtension = placeOfDeathTypeExtension;
	}

	public StringType getFacilityNameExtension() {
		return facilityNameExtension;
	}

	public void setFacilityNameExtension(StringType facilityNameExtension) {
		this.facilityNameExtension = facilityNameExtension;
	}

	public PostalAddress getPostalAddressExtension() {
		return postalAddressExtension;
	}

	public void setPostalAddressExtension(PostalAddress postalAddressExtension) {
		this.postalAddressExtension = postalAddressExtension;
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return ElementUtil.isEmpty(placeOfDeathTypeExtension,facilityNameExtension,postalAddressExtension);
	}
}
