package gatech.edu.nightingale.model;

import java.util.List;

import org.hl7.fhir.dstu3.model.CodeableConcept;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IExtension;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;

@Block()
public class Disposition extends BaseIdentifiableElement implements IExtension{
	private static final long serialVersionUID = 1L;

	@Child(name = "DispositionTypeExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-decedent-DispositionType-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR DispositionType Extension")
	private CodeableConcept dispositionTypeExtension;
	@Child(name = "FacilityNameExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-decedent-DispositionFacility-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR DispositionFacility Extension")
	private Facility dispositionFacilityExtension;
	@Child(name = "FuneralFacilityExtension")
	@Extension(url = "http://nightingaleproject.github.io/fhirDeathRecord/StructureDefinition/sdr-decedent-FuneralFacility-extension", definedLocally = true, isModifier = false)
	@Description(shortDefinition = "SDR FuneralFacility Extension")
	private Facility funeralFacilityExtension;

	public CodeableConcept getDispositionTypeExtension() {
		return dispositionTypeExtension;
	}

	public void setDispositionTypeExtension(CodeableConcept dispositionTypeExtension) {
		this.dispositionTypeExtension = dispositionTypeExtension;
	}

	public Facility getDispositionFacilityExtension() {
		return dispositionFacilityExtension;
	}

	public void setDispositionFacilityExtension(Facility dispositionFacilityExtension) {
		this.dispositionFacilityExtension = dispositionFacilityExtension;
	}

	public Facility getFuneralFacilityExtension() {
		return funeralFacilityExtension;
	}

	public void setFuneralFacilityExtension(Facility funeralFacilityExtension) {
		this.funeralFacilityExtension = funeralFacilityExtension;
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return ElementUtil.isEmpty(dispositionTypeExtension,dispositionFacilityExtension,funeralFacilityExtension);
	}
}
