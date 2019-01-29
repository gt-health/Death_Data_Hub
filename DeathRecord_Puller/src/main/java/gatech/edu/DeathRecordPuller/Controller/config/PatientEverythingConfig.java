package gatech.edu.DeathRecordPuller.Controller.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix="patientEverything")
public class PatientEverythingConfig {
	
	protected List<String> serverList;
	protected List<String> fhirVersion;
	
	public PatientEverythingConfig() {
		super();
	}

	public List<String> getServerList() {
		return serverList;
	}

	public void setServerList(List<String> serverList) {
		this.serverList = serverList;
	}

	public List<String> getFhirVersion() {
		return fhirVersion;
	}

	public void setFhirVersion(List<String> fhirVersion) {
		this.fhirVersion = fhirVersion;
	}
	
}
