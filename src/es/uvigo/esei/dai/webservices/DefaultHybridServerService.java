package es.uvigo.esei.dai.webservices;

import java.util.List;

import javax.jws.WebService;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.NotFoundException;
import es.uvigo.esei.dai.hybridserver.dao.HTMLDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.PagesDAO;
import es.uvigo.esei.dai.hybridserver.dao.XMLDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.XSDDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.XSLTDBDAO;


@WebService(endpointInterface ="es.uvigo.esei.dai.webservices.HybridServerService", targetNamespace = "http://hybridserver.dai.esei.uvigo.es/", serviceName ="HybridServerService")
public class DefaultHybridServerService implements HybridServerService {

	private Configuration config;

	public DefaultHybridServerService(Configuration config) {

		this.config = config;

	}

	@Override
	public List<String> listUuid(String resource_type) {

		return getDAO(config, resource_type).webList();
	}

	@Override
	public String getContent(String uuid, String resource_type) {
		String content;
		try {
			content = getDAO(config, resource_type).getWeb(uuid);

		} catch (NotFoundException e) {
             System.err.println("Can not find the resource with uuid : "+uuid);
			return null;
		}

		return content;
	}

	@Override
	public String getXSD(String uuid) {
         
		return  new XSLTDBDAO(this.config).getXsd(uuid);
	}

	private PagesDAO getDAO(Configuration config, String resource) {
		PagesDAO dao;
		switch (resource) {

		case "html":
			dao = new HTMLDBDAO(config);
			break;

		case "xml":
			dao = new XMLDBDAO(config);
			break;

		case "xsd":
			dao = new XSDDBDAO(config);
			break;

		case "xslt":
			dao = new XSLTDBDAO(config);
			break;

		default:
			dao = new HTMLDBDAO(config);
			break;

		}

		return dao;
	}

}
