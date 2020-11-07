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

import es.uvigo.esei.dai.controller.*;
import es.uvigo.esei.dai.hybridserver.dao.*;


public class HybridServer {
	private static final int SERVICE_PORT = 8888;
	private Thread serverThread;
	private boolean stop;
	private ExecutorService threadPool;
	private DefaultPagesController controller;
	
	public HybridServer() {
		
		this.controller= new DefaultPagesController(new PagesDBDAO(null));
		this.serverThread = new Thread();
	
		
	}
	
	public HybridServer(Map<String, String> pages) {
		this.controller= new DefaultPagesController(new PagesMapDAO(pages));
		this.serverThread = new Thread();
		
		
		
	}

	public HybridServer(Properties properties) {
		
		this.controller= new DefaultPagesController(new PagesDBDAO(properties));
		this.serverThread = new Thread();
		
		
	}

	public int getPort() {
		return SERVICE_PORT;
	}
	
	public void start() {
		this.serverThread = new Thread() {
			@Override
			public void run() {
				try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {
					
					threadPool = Executors.newFixedThreadPool(50);
					
					while (true) {
						    Socket socket = serverSocket.accept();
							if (stop) break;	 
							  threadPool.execute(new ServiceThread(socket,controller));
							 
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
		
		try (Socket socket = new Socket("localhost", SERVICE_PORT)) {
			// Esta conexión se hace, simplemente, para "despertar" el hilo servidor
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
	}
}
