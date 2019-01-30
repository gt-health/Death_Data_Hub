package gatech.edu.DeathRecordPuller.ID.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix="IDService")
public class IDServiceConfig {
	String idServiceURL;

	public String getIdServiceURL() {
		return idServiceURL;
	}

	public void setIdServiceURL(String idServiceURL) {
		this.idServiceURL = idServiceURL;
	}
	
}
