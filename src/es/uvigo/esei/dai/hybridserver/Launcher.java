package es.uvigo.esei.dai.hybridserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Launcher {
	public static void main(String[] args) {
		if (args.length == 1) {
			Properties prop = new Properties();
			try (InputStream input = new FileInputStream(args[0])) {
				prop.load(input);
				new HybridServer(prop).start();
				System.out.println("Servidor activo...");
			} catch (IOException ex) {
				ex.printStackTrace();
				System.err.println("Error al cargar el fichero de configuracion");
			}
			/*XMLConfigurationLoader configLoader = new XMLConfigurationLoader();
			try {
				Configuration config = configLoader.load(new File(args[0]));
				new HybridServer(config).start();
				
			}catch(Exception e) {
				
				System.out.println("Error while loading the configuration file");
							
			}
		}else if(args.length ==0)
		new HybridServer().start();
		else
			System.out.println("Server should be started with one config file or no arguments");
		*/	
			
		}

	}
}
