package es.uvigo.esei.dai.hybridserver;

public class NotFoundException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private String uuid;


	public  NotFoundException(String err) {
		super(err);
	}
	
	
}
