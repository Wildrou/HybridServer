package es.uvigo.esei.dai.model;

public class Page {

	private String uuid;
	private String content;
	
	
	
	
	public Page() {
		this.uuid="";
		this.content="";
	}


	public Page(String uuid, String content) {
		this.uuid = uuid;
		this.content = content;
	}
	
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
}
