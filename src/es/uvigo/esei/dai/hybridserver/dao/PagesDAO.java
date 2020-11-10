package es.uvigo.esei.dai.hybridserver.dao;


import es.uvigo.esei.dai.hybridserver.NotFoundException;

public interface PagesDAO {

	public String getWeb(String uuid) throws NotFoundException;
	public  boolean checkUuid(String uuid);
	public  String webList();
	public   String createUuid();
	public  void delete(String uuid) throws NotFoundException;
	public  String  putPage(String content);
	
	
	
}
