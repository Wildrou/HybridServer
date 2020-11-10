package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class ParsingUtils {

	
	/*public void parseRequestLine(HTTPRequest req, BufferedReader br) throws HTTPParseException, IOException {
		String x;
		if(( x = br.readLine()) != null) {
		String[] chain = x.split(" ");
		
		if (chain.length != 3)
			throw new HTTPParseException("the request line does not have 3 arguments");
		else if (!isMethod(chain[0])) 
			throw new HTTPParseException("The request line method is not valid");
		
		String[] resource_chain_array = chain[1].split("/");
		if (resource_chain_array.length > 2) {

			this.path = new String[resource_chain_array.length - 1];
			for (int i = 1; i < resource_chain_array.length && resource_chain_array[i].indexOf('?') == -1; i++) {

				path[i - 1] = resource_chain_array[i];
				this.resource_name += resource_chain_array[i] + '/';
			}

			if (resource_chain_array[resource_chain_array.length - 1].indexOf('?') != -1) {

				this.resource_name += resource_chain_array[resource_chain_array.length - 1].split("\\?")[0];
				this.path[path.length - 1] = resource_chain_array[resource_chain_array.length - 1].split("\\?")[0];
				String resource_params = resource_chain_array[chain.length - 1].split("\\?")[1];
				String[] params_array = resource_params.split("&");
				for (int i = 0; i < params_array.length; i++) {
					String[] param_value = params_array[i].split("=");
					this.resourceParams.put(param_value[0], param_value[1]);

				}
			} else {

				StringBuilder sb = new StringBuilder(this.resource_name);
				sb.deleteCharAt(this.resource_name.length() - 1);
				this.resource_name = sb.toString();
			}
		} else if (!chain[1].equals("/")) {
			if (chain[1].indexOf('?') == -1) {
				this.resource_name = resource_chain_array[1];
				this.path = new String[1];
				this.path[0] = this.resource_name;
			} else {
				this.resource_name = resource_chain_array[1].split("\\?")[0];
				String resource_params = resource_chain_array[1].split("\\?")[1];

				if (resource_params.indexOf('&') != -1) {

					String[] params_array = resource_params.split("&");
					for (int i = 0; i < params_array.length; i++) {
						String[] param_value = params_array[i].split("=");
						this.resourceParams.put(param_value[0], param_value[1]);
					}
				} else {
					String[] param_value = resource_params.split("=");
					this.resourceParams.put(param_value[0], param_value[1]);
					System.out.println("Parametros valores: "+param_value[0] + param_value[1]);
				}
			}

		} else {
			this.resource_name = "";
			this.path = new String[0];
		}

		this.version = chain[2];
		
	
	
	
	
		}
	
	}
	
	
		
		
		private boolean isMethod(String s) {

			for (HTTPRequestMethod en : HTTPRequestMethod.values()) {

				if (en.name().equals(s))
					return true;

			}

			return false;
		}*/
}
