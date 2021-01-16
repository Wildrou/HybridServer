/**
 *  HybridServer
 *  Copyright (C) 2020 Miguel Reboiro-Jato
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLConfigurationLoader {
	public Configuration load(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", XMLConstants.W3C_XML_SCHEMA_NS_URI);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new SimpleErrorHandler());
		
		int httpPort = 0;
		int numClients = 0;
		String webServiceURL = null;
		
		String dbUser = null;
		String dbPassword = null;
		String dbURL = null;
		
		List<ServerConfiguration> servers = new LinkedList<>();

		
		NodeList rootNodes = builder.parse(new File("configuration.xml")).getChildNodes();
		Element configurationElement = null;
		
		for (int i = 0; i < rootNodes.getLength(); i++) {

            Node item = rootNodes.item(i);

            if (item.getNodeName().equals("configuration")) {
            	configurationElement = (Element) item;
                break;
            }
        }
		
		NodeList configurationChildren = configurationElement.getChildNodes();

        for (int i = 0; i < configurationChildren.getLength(); i++) {

            Node confItem = configurationChildren.item(i);

            if (confItem.getNodeName().equals("connections")) {
            	
            	NodeList connectionsChildren = confItem.getChildNodes();
            	
            	for (int j = 0; j < connectionsChildren.getLength(); j++) {
            		Node connItem = connectionsChildren.item(i);
            		
            		if(connItem.getNodeName().equals("http"))
            			httpPort = Integer.parseInt(connItem.getTextContent());
            		else if(connItem.getNodeName().equals("webservice"))
            			webServiceURL = connItem.getTextContent();
            		else if(connItem.getNodeName().equals("numClients"))
            			httpPort = Integer.parseInt(connItem.getTextContent());
            	}
            }
            
            else if (confItem.getNodeName().equals("database")) {
            	
            	NodeList databaseChildren = confItem.getChildNodes();
            	
            	for (int j = 0; j < databaseChildren.getLength(); j++) {
            		Node databaseItem = databaseChildren.item(i);
            		
            		if(databaseItem.getNodeName().equals("user"))
            			dbUser = databaseItem.getTextContent();
            		else if(databaseItem.getNodeName().equals("password"))
            			dbPassword = databaseItem.getTextContent();
            		else if(databaseItem.getNodeName().equals("url"))
            			dbURL = databaseItem.getTextContent();
            	}
            }
            
            else if (confItem.getNodeName().equals("servers")) {
            	
            	NodeList serversChildren = confItem.getChildNodes();
            	
            	for (int j = 0; j < serversChildren.getLength(); j++) {
            		Node serverItem = serversChildren.item(i);
            		
            		NamedNodeMap serverAttributes= serverItem.getAttributes();
            		
            		servers.add(new ServerConfiguration(serverAttributes.getNamedItem("name").getTextContent(),
            				serverAttributes.getNamedItem("wsdl").getTextContent(),
            				serverAttributes.getNamedItem("namespace").getTextContent(),
            				serverAttributes.getNamedItem("service").getTextContent(),
            				serverAttributes.getNamedItem("httpAddress").getTextContent()));
            	}
            }

        }
        Configuration conf = new Configuration(httpPort,numClients,webServiceURL,dbUser,dbPassword,dbURL,servers);
		return null;
	}
}

