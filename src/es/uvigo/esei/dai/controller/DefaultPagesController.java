package es.uvigo.esei.dai.controller;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.esei.dai.entidades.XSLT;
import es.uvigo.esei.dai.hybridserver.NotFoundException;
import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.dao.PagesDAO;
import es.uvigo.esei.dai.hybridserver.dao.XMLDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.XSLTDBDAO;
import es.uvigo.esei.dai.webservices.HybridServerClient;
import es.uvigo.esei.dai.webservices.HybridServerService;

public class DefaultPagesController implements PagesController {
	private final PagesDAO dao;
	private List<ServerConfiguration> serverConfigList = new LinkedList<>();
	public DefaultPagesController(PagesDAO dao) {
		this.dao = dao;
	}
	public DefaultPagesController(PagesDAO dao,List <ServerConfiguration> configList) {
		this.dao = dao;
		this.serverConfigList= configList;
	} 
	@Override
	public String getWeb(String uuid){
		
		String content = this.dao.getWeb(uuid);
		System.out.println("El contenido es de local"+ content);
		if (!this.serverConfigList.isEmpty() && content== null) {
            try {
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                    content= server.getContent(uuid, this.dao.getContentType());
                    System.out.println("El contenido de web es: "+ content);
                    if(content != null)
                    	return content;
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		
		}
		 return content;
	}

	@Override
	public boolean checkUuid(String uuid) {
	
		return this.dao.checkUuid(uuid);
	}

	@Override
	public ArrayList<String> webList() {
		List<String> uuids = this.dao.webList();
		
		if (!this.serverConfigList.isEmpty()) {
            try {
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                    List<String> uuidServerList = server.listUuid(this.dao.getContentType());
                    for (String uuid : uuidServerList) {
                        uuids.add(uuid);
                    }
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		}
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
	
	public String getXSDUuid(String uuid) {
		String uuid_xsd;
		uuid_xsd= ((XSLTDBDAO)this.dao).getXsd(uuid);
	
	
	
		if (!this.serverConfigList.isEmpty() && uuid_xsd == null) {
            try {
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                    uuid_xsd = server.getXSD(uuid);
                    if(uuid_xsd != null)
                    	return uuid_xsd;
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		}
		return uuid_xsd;
		
	}
	
	public XSLT getXSLT(String xslt_uuid) {
	
		XSLT xslt= ((XMLDBDAO)this.dao).getXSLT(xslt_uuid);
		if (!this.serverConfigList.isEmpty() && xslt == null) {
            try {
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                    xslt = server.getXSLT(xslt_uuid);
                    if(xslt != null)
                    	return xslt;
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		}
		if(xslt== null)
			return null;
		return xslt;
		
	}
	
	public String getXSD(String uuid) {
		String content;
		content= ((XMLDBDAO)this.dao).getWeb_XSD(uuid);
		
		
		if (!this.serverConfigList.isEmpty() && content == null) {
            try {
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                    content = server.getXSD(uuid);
                    if(content != null)
                    	return content;
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		
		}
		
		return content;
		
		
	}
	
}