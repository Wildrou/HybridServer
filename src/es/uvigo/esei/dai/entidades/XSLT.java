package es.uvigo.esei.dai.entidades;

public class XSLT {
	String content;
	String uuid_xsd;
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
	public XSLT(String content, String uuid_xsd) {
		super();
		this.content = content;
		this.uuid_xsd = uuid_xsd;
	}
	
	
}
