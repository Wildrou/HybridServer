package es.uvigo.esei.dai.controller;



import es.uvigo.esei.dai.hybridserver.NotFoundException;
import es.uvigo.esei.dai.model.Page;

public interface PagesController {
	
	public String getWeb(String uuid) throws NotFoundException;
	public  boolean checkUuid(String uuid);
	public  String webList();
	public   String createUuid();
	public  void delete(String uuid) throws NotFoundException;
	public  String  putPage(String content);
	
	

}
