package gatech.edu.DeathRecordPuller.ID.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import gatech.edu.DeathRecordPuller.ID.model.IDEntry;
import gatech.edu.DeathRecordPuller.ID.model.IDList;

@Service
@Primary
public class IDService {
	RestTemplate restTemplate;
	@Autowired
	IDServiceConfig iDServiceConfig;

	public IDService() {
		restTemplate = new RestTemplate();
	}
	
	public List<IDEntry> getIDEntries(String caseNumber,String medicalExaminerOffice,String name,String family,String given){
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(iDServiceConfig.getIdServiceURL()).path("search/");
		if(caseNumber != null) {
			uriComponentsBuilder.queryParam("case-number", caseNumber);
		}
		if(medicalExaminerOffice != null) {
			uriComponentsBuilder.queryParam("medical-examiner-office", medicalExaminerOffice);
		}
		if(family != null) {
			uriComponentsBuilder.queryParam("family", family);
		}
		if(given != null) {
			uriComponentsBuilder.queryParam("given", given);
		}
		if(name != null) {
			uriComponentsBuilder.queryParam("name", name);
		}
		IDList output = restTemplate.getForEntity(uriComponentsBuilder.build().toUri(), IDList.class).getBody();
		return output.getList();
	}

	public IDServiceConfig getiDServiceConfig() {
		return iDServiceConfig;
	}

	public void setiDServiceConfig(IDServiceConfig iDServiceConfig) {
		this.iDServiceConfig = iDServiceConfig;
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}