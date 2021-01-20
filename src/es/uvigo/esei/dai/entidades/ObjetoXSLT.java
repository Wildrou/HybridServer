package es.uvigo.esei.dai.entidades;

public class ObjetoXSLT {
	String content;
	String uuid_xsd;
	
	
	public ObjetoXSLT(String content, String uuid_xsd) {
		this.content = content;
		this.uuid_xsd = uuid_xsd;
	}
	
	
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUuid_xsd() {
		return uuid_xsd;
	}
	public void setUuid_xsd(String uuid_xsd) {
		this.uuid_xsd = uuid_xsd;
	}
	
	
}
