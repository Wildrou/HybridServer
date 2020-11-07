package es.uvigo.esei.dai.hybridserver.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import es.uvigo.esei.dai.hybridserver.*;
public class PagesMapDAO {
private static Map<String,String> webs;

public PagesMapDAO(Map <String,String> pages) {
    webs = new HashMap<>();
	webs.putAll(pages);
}
	
public static String getWeb(String uuid) throws NotFoundException {
	 if(!checkUuid(uuid))
		 throw new NotFoundException("No se encuentra el uuid");
	return webs.get(uuid);
	
	
	
}

public static boolean checkUuid(String uuid) {
	
	
	return  webs.containsKey(uuid);
}

public static String webList() {
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
	for (Map.Entry<String, String> entry : webs.entrySet()) {
		sb.append("<p><a href=\"").append(entry.getValue()).append("\">").append(entry.getKey()).append("</a></p>");
	}
	sb.append(html2);
	
	return sb.toString();
	
}

public  static String createUuid() {
	

	UUID randomUuid = UUID.randomUUID();
	String uuid = randomUuid.toString();
	
	
	return uuid;
	
	
}


public static void delete(String uuid) throws NotFoundException {
	
	if(webs.remove(uuid) == null)
		throw new NotFoundException("404 Page not found",uuid);
	
	

	
	
}

public static String  putPage(String content) {
	String uuid= createUuid();
	webs.put(uuid, content);
	System.out.println("El contenido metido en web manager es: "+content);
	return "<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>";
	
}
}
