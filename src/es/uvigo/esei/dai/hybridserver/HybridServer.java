package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.Endpoint;

import es.uvigo.esei.dai.controller.*;
import es.uvigo.esei.dai.hybridserver.dao.*;
import es.uvigo.esei.dai.webservices.DefaultHybridServerService;


public class HybridServer {
	private final int servicePort;
	private Thread serverThread;
	private boolean stop;
	private ExecutorService threadPool;
	private final  int numClients;
	private final Configuration config;
	private String webService;
	private Endpoint endpnt;
	//private final Properties properties;
	
	public HybridServer() {
		this.numClients=50;
		this.servicePort=8888;
		this.config=null;
		//this.properties=null;
		
	
		
	}
	
	/*public HybridServer(Map<String, String> pages) {
		this.numClients=50;
		this.servicePort=8888;
		this.controller= new DefaultPagesController(new PagesMapDAO(pages));
	
		
		
		
	}*/

	/*public HybridServer(Properties prop) {
		this.numClients=Integer.parseInt(prop.getProperty("numClients"));
		this.servicePort=Integer.parseInt(prop.getProperty("port"));
		this.properties=prop;
		
		
	}*/
	
	public HybridServer(Configuration config) {
		this.numClients=config.getNumClients();
		this.servicePort=config.getHttpPort();
		this.config=config;
		this.webService= config.getWebServiceURL();
		//this.controller= new DefaultPagesController(new HTMLDBDAO(properties));
		
		
	}

	public int getPort() {
		return servicePort;
	}
	
	public void start() {
		if(this.webService != null) {
			this.endpnt = Endpoint.publish(this.webService, new DefaultHybridServerService(this.config));
		}
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(servicePort)) {
					
					threadPool = Executors.newFixedThreadPool(numClients);
					
					while (true) {
						    Socket socket = serverSocket.accept();
							if (stop) break;	 
							  threadPool.execute(new ServiceThread(socket,config));
							 
						}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		this.stop = false;
		this.serverThread.start();
	}
	
	public void stop() {
		this.stop = true;
		
		try (Socket socket = new Socket("localhost", servicePort)) {
			// Esta conexiÃ³n se hace, simplemente, para "despertar" el hilo servidor
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			this.serverThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		threadPool.shutdownNow();
		 
		try {
		  threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
		  e.printStackTrace();
		}
		this.serverThread = null;
		
		this.endpnt.stop();
	}
}
