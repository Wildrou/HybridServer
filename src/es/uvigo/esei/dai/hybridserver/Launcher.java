package es.uvigo.esei.dai.hybridserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Launcher {
	public static void main(String[] args) {

		if (args.length == 1) {
			try (InputStream frd = new FileInputStream(args[0])) {

				Properties props = new Properties();
				props.load(frd);
				
				new HybridServer(props).start();

			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error reading the file");
			}

		}else if(args.length ==0)
		new HybridServer().start();
		else
			System.out.println("Server should be started with one config file or no arguments");
			
			
		

	}
}
