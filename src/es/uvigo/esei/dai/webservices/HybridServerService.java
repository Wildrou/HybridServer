package es.uvigo.esei.dai.webservices;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;


@WebService
public interface HybridServerService {
     @WebMethod
	public List<String> listUuid(String resource_type);
     @WebMethod
	public String getContent(String uuid,String resource_type);
     @WebMethod
	public String getXSD(String uuid);
     @WebMethod
	public List<String> getXSLT(String uuid);
	

	
	
}
