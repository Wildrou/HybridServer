package es.uvigo.esei.dai.hybridserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebManager {

private Map<String,String> webs;

public WebManager() {
	this.webs = new HashMap<>();
	this.webs.put("6df1047e-cf19-4a83-8cf3-38f5e53f7725", "This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725.");
	this.webs.put("79e01232-5ea4-41c8-9331-1c1880a1d3c2", "This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2.");
	this.webs.put("a35b6c5e-22d6-4707-98b4-462482e26c9e", "This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e.");
	this.webs.put("3aff2f9c-0c7f-4630-99ad-27a0cf1af137", "This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137.");
	this.webs.put("77ec1d68-84e1-40f4-be8e-066e02f4e373", "This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373.");
	this.webs.put("8f824126-0bd1-4074-b88e-c0b59d3e67a3", "This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3.");
	this.webs.put("c6c80c75-b335-4f68-b7a7-59434413ce6c", "This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c.");
	this.webs.put("f959ecb3-6382-4ae5-9325-8fcbc068e446", "This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446.");
	this.webs.put("2471caa8-e8df-44d6-94f2-7752a74f6819", "This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819.");
	this.webs.put("fa0979ca-2734-41f7-84c5-e40e0886e408", "This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408.");
    
}	
	
public String getWeb(String uuid) throws NotFoundException {
	 if(!this.checkUuid(uuid))
		 throw new NotFoundException("No se encuentra el uuid");
	return this.webs.get(uuid);
	
	
	
}

public boolean checkUuid(String uuid) {
	
	
	return this.webs.containsKey(uuid);
}

public String webList() {
	StringBuilder sb = new StringBuilder();
	String html1="<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" + 
	 		"<html>\r\n" + 
	 		"<head>\r\n" + 
	 		"   <title>404 Not Found</title>\r\n" + 
	 		"</head>\r\n" + 
	 		"<body>\r\n" + 
	 		"   <h1>Not Found</h1>\r\n" + 
	 		"   ";
	
	String html2="\r\n" + 
	 		"</body>\r\n" + 
	 		"</html>";
	sb.append(html1);
	for (Map.Entry<String, String> entry : this.webs.entrySet()) {
		sb.append("<p><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></p>");
	}
	sb.append(html2);
	
	return sb.toString();
	
}

public String createUuid() {
	

	UUID randomUuid = UUID.randomUUID();
	String uuid = randomUuid.toString();
	
	
	return uuid;
	
	
}


public void delete(String uuid) throws NotFoundException {
	
	if(this.webs.remove(uuid) == null)
		throw new NotFoundException("404 Page not found",uuid);
	
	

	
	
}
}
