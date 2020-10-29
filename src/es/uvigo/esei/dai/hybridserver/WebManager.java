package es.uvigo.esei.dai.hybridserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebManager {

private Map<String,String> webs;

public WebManager() {
	this.webs = new HashMap<>();
	this.webs.put("0001", "<!DOCTYPE html>\r\n" + 
			"<html>\r\n" + 
			"    <head>\r\n" + 
			"        <!-- head definitions go here -->\r\n" + 
			"    </head>\r\n" + 
			"    <body>\r\n" + 
			"         <strong>PAGINA 1</strong>\r\n" + 
			"    </body>\r\n" + 
			"</html>");
	this.webs.put("0002", "<!DOCTYPE html>\r\n" + 
			"<html>\r\n" + 
			"    <head>\r\n" + 
			"        <!-- head definitions go here -->\r\n" + 
			"    </head>\r\n" + 
			"    <body>\r\n" + 
			"        <strong>PAGINA 2</strong>\r\n" + 
			"    </body>\r\n" + 
			"</html>");
	this.webs.put("0003", "<!DOCTYPE html>\r\n" + 
			"<html>\r\n" + 
			"    <head>\r\n" + 
			"        <!-- head definitions go here -->\r\n" + 
			"    </head>\r\n" + 
			"    <body>\r\n" + 
			"       <strong>PAGINA 3</strong>\r\n" + 
			"    </body>\r\n" + 
			"</html>");
	
    
}	
	
public String getWeb(String uuid) throws Exception {
	 if(this.checkUuid(uuid))
		 throw new Exception("No se encuentra el uuid");
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
