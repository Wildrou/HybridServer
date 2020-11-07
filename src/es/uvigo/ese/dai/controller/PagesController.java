package es.uvigo.ese.dai.controller;

import java.util.List;

import es.uvigo.esei.dai.dojo3.EmployeeNotFoundException;
import es.uvigo.esei.dai.dojo3.SearchType;
import es.uvigo.esei.dai.dojo3.model.entity.Employee;
import es.uvigo.esei.dai.model.Page;

public interface PagesController {
	public void create(Page page);
	public void update(Page page) throws PageNotFoundException;
	public void delete(int id) throws PageNotFoundException;
	public Page get(int id) throws PageNotFoundException;
	
	public List<Page> list();

}
