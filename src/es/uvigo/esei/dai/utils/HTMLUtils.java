package es.uvigo.esei.dai.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class HTMLUtils {

	public static final String DEFAULT_PAGE="<html> <head><title>Hybrid Server</title>"
			+ "</head><body><p>Miguel Arias Perez</p><p>Victor Otero Cabaleiro</p>"
			+ "<h1>Hybrid Server</h1> <a href=\"/html\">html</a></body></html>";
	

	
	
	
	public static String generateHTMLWebs(ArrayList<String> uuids) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <head><title>Hybrid Server</title></head><body>"
				+ "<h1>Hybrid Server</h1>");
		for (String uuid : uuids) {
			sb.append("<p><a href=\"html?uuid=").append(uuid).append("\">").append(uuid).append("</a></p>\n");
		}

		sb.append("</body></html>");
		
		
		return sb.toString();
		
	}
	
	
	
	public static String generateNewPageLink(String uuid,String resource_name) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <head><title>Hybrid Server</title></head><body>"
				+ "<h1>Hybrid Server</h1>");
		sb.append("<a href=\"").append(resource_name).append("?uuid=" + uuid + "\">" + uuid + "</a>");
		sb.append("</body></html>");
		
		return sb.toString();
		
	}
	
	public static String generateDeletePage(String uuid,String resource_name) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <head><title>Hybrid Server</title></head><body>"
				+ "<h1>Hybrid Server</h1>");
		sb.append("<p>Se ha borrado el ").append(resource_name).append(" con el uuid: ").append(uuid).append("</p>");
		sb.append("</body></html>");
		
		return sb.toString();
		
		
		
		
	}
	
	
	
	
}
