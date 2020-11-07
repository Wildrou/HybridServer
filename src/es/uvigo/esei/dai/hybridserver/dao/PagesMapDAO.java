package es.uvigo.esei.dai.hybridserver.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import es.uvigo.esei.dai.hybridserver.*;


public class PagesMapDAO implements PagesDAO{
private Map<String,String> webs;

public PagesMapDAO(Map <String,String> pages)  {
    this.webs = new HashMap<>();
	this.webs.putAll(pages);
}
	
public  String getWeb(String uuid) throws NotFoundException {
	 if(!checkUuid(uuid))
		 throw new NotFoundException("No se encuentra el uuid");
	return this.webs.get(uuid);
	
	
	
}

public  boolean checkUuid(String uuid) {
	
	
	return  webs.containsKey(uuid);
}

public  String webList() {
	StringBuilder sb = new StringBuilder();

	for (Map.Entry<String, String> entry : webs.entrySet()) {
		sb.append("<p><a href=\"html?uuid=").append(entry.getKey()).append("\">").append(entry.getKey()).append("</a></p>\n");
	}
	
	
	return sb.toString();
	
}

public   String createUuid() {
	

	UUID randomUuid = UUID.randomUUID();
	String uuid = randomUuid.toString();
	
	
	return uuid;
	
	
}


public  void delete(String uuid) throws NotFoundException {
	
	if(webs.remove(uuid) == null)
		throw new NotFoundException("404 Page not found",uuid);
	
	

	
	
}

public  String  putPage(String content) {
	
	String uuid= createUuid();
	webs.put(uuid,content);
	return "<a href=\"html?uuid=" + uuid + "\">" + uuid + "</a>";
	
}


}
