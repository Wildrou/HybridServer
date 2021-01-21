package es.uvigo.esei.dai.controller;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.uvigo.esei.dai.entidades.ObjetoXSLT;
import es.uvigo.esei.dai.entidades.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.NotFoundException;
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
		ArrayList<String> uuids = this.dao.webList();
		
		if (!this.serverConfigList.isEmpty()) {
            try {
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                	System.out.println("Traga hasta aquí");
                	System.out.println(this.dao.getContentType());
                    List<String> uuidServerList = server.listUuid(this.dao.getContentType());
                    System.out.println("En este servidor cogemos: "+uuidServerList.toString());
                    for (String uuid : uuidServerList) {
                        uuids.add(uuid);
                    }
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		}
		return uuids;
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
	
	public Boolean checkXSDExists(String uuid) {

		Boolean exists = false;
		exists= ((XSLTDBDAO)this.dao).getXSDUuid(uuid);
		System.out.println("El uuid del xsd del post es"+ uuid);
	
	
		if (!this.serverConfigList.isEmpty() && exists == false) {
            try {
            	String xsd;
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                    xsd = server.getXSD(uuid);
                    if(xsd != null)
                    	return true;
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		}
		return exists;
		
	}
	
	public List<String> getXSLT(String xslt_uuid) {
		System.out.println("Uuid es antes de pasar de LOCAL :"+xslt_uuid);
		List<String> xslt= ((XMLDBDAO)this.dao).getXSLT(xslt_uuid);
		if (!this.serverConfigList.isEmpty() && xslt.isEmpty()) {
            try {
            	System.out.println("Uuid es :"+xslt_uuid);
                HybridServerClient ws = new HybridServerClient(this.serverConfigList);
                List<HybridServerService> hybridServerServiceList = ws.getServers();
                for (HybridServerService server : hybridServerServiceList) {
                	System.out.println("Uuid xslt es dentro del for "+xslt_uuid);
                    xslt = server.getXSLT(xslt_uuid);
                    System.out.println("El contenido de la lista xslt en el for es: "+xslt.toString());
                    if(!xslt.isEmpty())
                    	return xslt;
                }
            } catch (MalformedURLException e) {
                System.err.println("Malformed Url");
            }
		}
	
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