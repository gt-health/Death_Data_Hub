package gatech.edu.DeathRecordPuller.nightingale.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import gatech.edu.DeathRecordPuller.util.StatefulRestTemplate;

@Service
@Configuration
@ConfigurationProperties(prefix="nightingale")
@Primary
public class NightingaleService {
	protected String endpoint;
	protected String adminUsername;
	protected String adminPassword;
	protected StatefulRestTemplate restTemplate;
	
	public NightingaleService() {
		restTemplate = new StatefulRestTemplate();
	}
	
	public void submitEDRS(String deathRecordContents) {
		if(!restTemplate.containsCookieName("_nightingale_session")) {
			login();
		}
		String fullURLString = endpoint
				+ "/fhir/v1/death_records";
		try {
			URL url = new URL(fullURLString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		restTemplate.postForLocation(fullURLString, deathRecordContents);
	}
	
	public void login() {
		String fullURLString = "";
		try {
			fullURLString = endpoint
					+ "/users/sign_in"
					+ "?user[email]=" + URLEncoder.encode(adminUsername, "UTF-8")
					+ "&user[password]=" + URLEncoder.encode(adminPassword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			URL url = new URL(fullURLString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		restTemplate.postForLocation(fullURLString, null);
	}
	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}