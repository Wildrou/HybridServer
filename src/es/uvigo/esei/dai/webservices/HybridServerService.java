package es.uvigo.esei.dai.webservices;

import java.util.List;

import javax.jws.WebService;

@WebService
public interface HybridServerService {

	public List<String> listUuid(String resource_type);
	public String getContent(String uuid,String resource_type);
	public String getXSD(String uuid);
	
	

	
	
}
