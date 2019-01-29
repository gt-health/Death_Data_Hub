package gatech.edu.DeathRecordPuller.ID.service;

import java.util.HashMap;
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

@Service
@Configuration
@ConfigurationProperties(prefix="IDService")
@Primary
public class IDService {
	String idServiceURL;
	RestTemplate restTemplate;
	public IDService() {
		restTemplate = new RestTemplate();
	}
	
	public IDEntry getIDEntry(String caseNumber,String name,String family,String given){
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(idServiceURL).path("search/").build();
		Map<String,String> params = new HashMap<String,String>();
		if(!caseNumber.isEmpty()) {
			params.put("case-number", caseNumber);
		}
		if(!family.isEmpty()) {
			params.put("family", family);
		}
		if(!given.isEmpty()) {
			params.put("given", given);
		}
		if(!name.isEmpty()) {
			params.put("name", name);
		}
		IDEntry output = restTemplate.getForEntity(uriComponents.toUri().toString(), IDEntry.class,params).getBody();
		return output;
	}
}