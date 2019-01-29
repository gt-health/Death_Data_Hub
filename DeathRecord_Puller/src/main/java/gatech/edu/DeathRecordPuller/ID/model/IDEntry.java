package gatech.edu.DeathRecordPuller.ID.model;

import java.util.List;

public class IDEntry {
	protected Integer id;
	protected String lastName;
	protected String firstName;
	protected String gender;
	protected String meOffice;
	protected String meCaseNumber;
	protected List<FHIRSource> listOfFhirSources;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getMeOffice() {
		return meOffice;
	}
	public void setMeOffice(String meOffice) {
		this.meOffice = meOffice;
	}
	public String getMeCaseNumber() {
		return meCaseNumber;
	}
	public void setMeCaseNumber(String meCaseNumber) {
		this.meCaseNumber = meCaseNumber;
	}
	public List<FHIRSource> getListOfFhirSources() {
		return listOfFhirSources;
	}
	public void setListOfFhirSources(List<FHIRSource> listOfFhirSources) {
		this.listOfFhirSources = listOfFhirSources;
	}
	
	public FHIRSource getFhirSource(String fhirEndpoint) {
		for(FHIRSource fhirSource: listOfFhirSources) {
			fhirSource.fhirServerUrl.equals(fhirEndpoint);
			return fhirSource;
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((listOfFhirSources == null) ? 0 : listOfFhirSources.hashCode());
		result = prime * result + ((meCaseNumber == null) ? 0 : meCaseNumber.hashCode());
		result = prime * result + ((meOffice == null) ? 0 : meOffice.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IDEntry other = (IDEntry) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (listOfFhirSources == null) {
			if (other.listOfFhirSources != null)
				return false;
		} else if (!listOfFhirSources.equals(other.listOfFhirSources))
			return false;
		if (meCaseNumber == null) {
			if (other.meCaseNumber != null)
				return false;
		} else if (!meCaseNumber.equals(other.meCaseNumber))
			return false;
		if (meOffice == null) {
			if (other.meOffice != null)
				return false;
		} else if (!meOffice.equals(other.meOffice))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "IDEntry [id=" + id + ", lastName=" + lastName + ", firstName=" + firstName + ", gender=" + gender
				+ ", meOffice=" + meOffice + ", meCaseNumber=" + meCaseNumber + ", listOfFhirSources="
				+ listOfFhirSources + "]";
	}
	
}