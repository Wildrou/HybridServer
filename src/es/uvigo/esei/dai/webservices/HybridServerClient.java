package es.uvigo.esei.dai.webservices;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import es.uvigo.esei.dai.entidades.ServerConfiguration;

public class HybridServerClient {

	
    List<HybridServerService> hybridServerServiceList;

    public HybridServerClient(List<ServerConfiguration> serverConfList) throws MalformedURLException {
        this.hybridServerServiceList= new LinkedList<HybridServerService>();
        for (ServerConfiguration server : serverConfList) {
            URL url = new URL(server.getWsdl());
            QName qname = new QName(server.getNamespace(), server.getService());

            try {
                Service service = Service.create(url, qname);
                hybridServerServiceList.add(service.getPort(HybridServerService.class));
            } catch (WebServiceException e) {
                System.err.println("Server by name: "+server.getName()+" is currently down");
            }
        }
    }

    public List<HybridServerService> getServers() {
        return this.hybridServerServiceList;
    }

}
