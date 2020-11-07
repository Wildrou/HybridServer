package es.uvigo.ese.dai.controller;

import java.util.List;

import es.uvigo.esei.dai.dojo3.EmployeeNotFoundException;
import es.uvigo.esei.dai.dojo3.SearchType;
import es.uvigo.esei.dai.dojo3.model.dao.EmployeesDAO;
import es.uvigo.esei.dai.dojo3.model.entity.Employee;
import es.uvigo.esei.dai.hybridserver.dao.PagesDAO;
import es.uvigo.esei.dai.model.Page;

public class DefaultPagesController implements PagesController {
	private final PagesDAO dao;
	
	public DefaultPagesController(PagesDAO dao) {
		this.dao = dao;
	}

	@Override
	public Page get(int id) throws PageNotFoundException {
		return this.dao.get(id);
	}

	@Override
	public void create(Page page) {
		this.dao.create(page);
	}
	
	@Override
	public void update(Page page) throws PageNotFoundException {
		this.dao.update(page);
	}

	@Override
	public void delete(int id) throws PageNotFoundException {
		this.dao.delete(id);
	}

	@Override
	public List<Page> list() {
		return this.dao.list();
	}


}
