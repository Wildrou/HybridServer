package es.uvigo.esei.dai.controller;



import java.util.ArrayList;

import es.uvigo.esei.dai.hybridserver.NotFoundException;


public interface PagesController {
	
	public String getWeb(String uuid) throws NotFoundException;
	public  boolean checkUuid(String uuid);
	public  ArrayList<String> webList();
	public   String createUuid();
	public  void delete(String uuid) throws NotFoundException;
	public  String  putPage(String [] content);
	
	

}
