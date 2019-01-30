package gatech.edu.DeathRecordPuller.ID.model;

public class FHIRSource {
	protected String fhirServerUrl;
	protected String fhirSecurity;
	protected String fhirVersion;
	protected String fhirPatientId;
	protected String type;
	public String getFhirServerUrl() {
		return fhirServerUrl;
	}
	public void setFhirServerUrl(String fhirServerUrl) {
		this.fhirServerUrl = fhirServerUrl;
	}
	public String getFhirSecurity() {
		return fhirSecurity;
	}
	public void setFhirSecurity(String fhirSecurity) {
		this.fhirSecurity = fhirSecurity;
	}
	public String getFhirVersion() {
		return fhirVersion;
	}
	public void setFhirVersion(String fhirVersion) {
		this.fhirVersion = fhirVersion;
	}
	public String getFhirPatientId() {
		return fhirPatientId;
	}
	public void setFhirPatientId(String fhirPatiendId) {
		this.fhirPatientId = fhirPatiendId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fhirPatientId == null) ? 0 : fhirPatientId.hashCode());
		result = prime * result + ((fhirSecurity == null) ? 0 : fhirSecurity.hashCode());
		result = prime * result + ((fhirServerUrl == null) ? 0 : fhirServerUrl.hashCode());
		result = prime * result + ((fhirVersion == null) ? 0 : fhirVersion.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		FHIRSource other = (FHIRSource) obj;
		if (fhirPatientId == null) {
			if (other.fhirPatientId != null)
				return false;
		} else if (!fhirPatientId.equals(other.fhirPatientId))
			return false;
		if (fhirSecurity == null) {
			if (other.fhirSecurity != null)
				return false;
		} else if (!fhirSecurity.equals(other.fhirSecurity))
			return false;
		if (fhirServerUrl == null) {
			if (other.fhirServerUrl != null)
				return false;
		} else if (!fhirServerUrl.equals(other.fhirServerUrl))
			return false;
		if (fhirVersion == null) {
			if (other.fhirVersion != null)
				return false;
		} else if (!fhirVersion.equals(other.fhirVersion))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FHIRSource [fhirServerUrl=" + fhirServerUrl + ", fhirSecurity=" + fhirSecurity + ", fhirVersion="
				+ fhirVersion + ", fhirPatientId=" + fhirPatientId + ", type=" + type + "]";
	}
}
