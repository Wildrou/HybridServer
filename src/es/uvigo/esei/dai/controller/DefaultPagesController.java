package es.uvigo.esei.dai.controller;


import java.util.ArrayList;

import es.uvigo.esei.dai.hybridserver.NotFoundException;
import es.uvigo.esei.dai.hybridserver.dao.PagesDAO;
import es.uvigo.esei.dai.hybridserver.dao.XSLTDBDAO;

public class DefaultPagesController implements PagesController {
	private final PagesDAO dao;
	
	public DefaultPagesController(PagesDAO dao) {
		this.dao = dao;
	}

	@Override
	public String getWeb(String uuid) throws NotFoundException {
	
		 return this.dao.getWeb(uuid);
	}

	@Override
	public boolean checkUuid(String uuid) {
	
		return this.dao.checkUuid(uuid);
	}

	@Override
	public ArrayList<String> webList() {
		
		return this.dao.webList();
	}

	@Override
	public String createUuid() {
		
		return this.dao.createUuid();
	}

	@Override
	public void delete(String uuid) throws NotFoundException {
		
		this.dao.delete(uuid);
		
	}

	@Override
	public String putPage(String [] content) {
		
		return this.dao.putPage(content);
	}
	
	public XSLTDBDAO getDAO() {
		
		return (XSLTDBDAO) this.dao;
		
	}
}