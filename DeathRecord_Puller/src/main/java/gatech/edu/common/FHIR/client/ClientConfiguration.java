package gatech.edu.common.FHIR.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="FHIR.client")
public class ClientConfiguration {
	private String serverBaseUrl;
	private String dstuVersion;
	private Boolean lenientParsing = Boolean.FALSE;

	public String getServerBaseUrl() {
		return serverBaseUrl;
	}

	public void setServerBaseUrl(String serverBaseUrl) {
		this.serverBaseUrl = serverBaseUrl;
	}
	
	public String getDstuVersion() {
		return dstuVersion;
	}
	
	public void setDstuVersion(String dstuVersion) {
		this.dstuVersion = dstuVersion;
	}
	
	public Boolean getLenientParsing() {
		return lenientParsing;
	}
	
	public void setLenientParsing(Boolean lenientParsing) {
		this.lenientParsing = lenientParsing;
	}
	
}