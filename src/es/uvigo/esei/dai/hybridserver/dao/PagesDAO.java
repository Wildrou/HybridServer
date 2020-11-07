package es.uvigo.esei.dai.hybridserver.dao;

import java.util.List;

import es.uvigo.esei.dai.controller.PageNotFoundException;
import es.uvigo.esei.dai.dojo3.model.entity.Employee;
import es.uvigo.esei.dai.hybridserver.NotFoundException;

public interface PagesDAO {

	public String getWeb(String uuid) throws NotFoundException;
	public  boolean checkUuid(String uuid);
	
	
	
}
