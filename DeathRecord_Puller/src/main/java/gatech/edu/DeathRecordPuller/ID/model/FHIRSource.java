package gatech.edu.DeathRecordPuller.ID.model;

public class FHIRSource {
	protected String fhirServerUrl;
	protected String fhirSecurity;
	protected String fhirPatiendId;
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
	public String getFhirPatiendId() {
		return fhirPatiendId;
	}
	public void setFhirPatiendId(String fhirPatiendId) {
		this.fhirPatiendId = fhirPatiendId;
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
		result = prime * result + ((fhirPatiendId == null) ? 0 : fhirPatiendId.hashCode());
		result = prime * result + ((fhirSecurity == null) ? 0 : fhirSecurity.hashCode());
		result = prime * result + ((fhirServerUrl == null) ? 0 : fhirServerUrl.hashCode());
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
		if (fhirPatiendId == null) {
			if (other.fhirPatiendId != null)
				return false;
		} else if (!fhirPatiendId.equals(other.fhirPatiendId))
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
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FHIRSource [fhirServerUrl=" + fhirServerUrl + ", fhirSecurity=" + fhirSecurity + ", fhirPatiendId="
				+ fhirPatiendId + ", type=" + type + "]";
	}
	
}
