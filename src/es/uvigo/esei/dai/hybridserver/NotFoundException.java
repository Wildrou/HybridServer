package es.uvigo.esei.dai.hybridserver;

public class NotFoundException extends Exception {


	/**
	 * 
	 */
	
	private String uuid;
	private static final long serialVersionUID = -4773154562918471165L;

	public  NotFoundException(String err,String uuid) {
		super(err);
		this.uuid=uuid;
	}
	public NotFoundException(String err) {
		super(err);
	}
	
	public String getUuid() {
		
		return this.uuid;
		
		
	}
}
