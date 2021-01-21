package es.uvigo.esei.dai.utils;
public class Gitanada {

	public static void main(String[] args) {
		
		String nuestro="<html> <head><title>Hybrid Server</title></head><body><h1>Hybrid Server</h1><a href=\"html?uuid=afccb544-aa59-4b6a-8fa5-ab462e4ff116\">afccb544-aa59-4b6a-8fa5-ab462e4ff116</a></body></html>";
		String jato = "<a href=\"html?uuid=afccb544-aa59-4b6a-8fa5-ab462e4ff116\">afccb544-aa59-4b6a-8fa5-ab462e4ff116</a>";
		
		
		System.out.println(nuestro.contains(jato));
		
       // String escaped = StringEscapeUtils.escapeJava("<html> <head><title>Hybrid Server</title></head><body><h1>Hybrid Server</h1><p><a href=\"html?uuid=3aff2f9c-0c7f-1111-99ad-27a0cf1af137\\\">3aff2f9c-0c7f-1111-99ad-27a0cf1af137</a></p><p><a href=\"html?uuid=6df1047e-cf19-1111-8cf3-38f5e53f7725\\\">6df1047e-cf19-1111-8cf3-38f5e53f7725</a></p>)");
	}
 

}


