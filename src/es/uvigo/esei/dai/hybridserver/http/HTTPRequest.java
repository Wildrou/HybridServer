package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

	private HTTPRequestMethod method;
	private String[] path;
	private String resource_name;
	private String resource_chain;
	private String version;
	private int content_length;
	private Map<String, String> resourceParams;
	private Map<String, String> headerParams;
	private String content;

	public HTTPRequest(Reader reader) throws IOException, HTTPParseException {

		BufferedReader br = new BufferedReader(reader);
		String x = br.readLine();
		this.resourceParams = new HashMap<>();
		if (x != null) {
			String[] chain = x.split(" ");
			if(esMetodo(chain[0]))
			this.method = HTTPRequestMethod.valueOf(chain[0]);
			else
				throw new HTTPParseException();
			
			
			this.resource_chain = chain[1];
			String[] resource_chain_array = chain[1].split("/");

			for (int i = 1; i < resource_chain_array.length - 1; i++) {
				path[i - 1] = resource_chain_array[i];
			}

			this.resource_name = resource_chain_array[chain.length - 1].split("\\?")[0];

			String resource_params = resource_chain_array[chain.length - 1].split("\\?")[1];
			String[] params_array = resource_params.split("&");
			for (int i = 0; i < params_array.length; i++) {
				String[] param_value = params_array[i].split("=");
				this.resourceParams.put(param_value[0], param_value[1]);

			}

			this.version = chain[2];

		}

		while ((x = br.readLine()) != null && x.trim().length() != 0) {

			this.headerParams = new HashMap<>();

			String[] header_params = x.split(":");
			this.resourceParams.put(header_params[0], header_params[1]);

		}

		if (this.headerParams.containsKey("Content-Length")) {

			this.content_length = Integer.parseInt(this.headerParams.get("Content-Length"));

		} else {
			this.content_length = 0;
		}

		if(this.content_length != 0) {
			char [] buff_contenido= new char [this.content_length];
			br.read(buff_contenido, 0, this.content_length);
			this.content= new String(buff_contenido);
			String type = headerParams.get("Content-Type");
			if (type != null && type.startsWith("application/x-www-form-urlencoded")) {
			   this.content = URLDecoder.decode(content, "UTF-8");
			}
			
		}
		
	}

	public HTTPRequestMethod getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}

	public String getResourceChain() {
		// TODO Auto-generated method stub
		return this.resource_chain;
	}

	public String[] getResourcePath() {
		// TODO Auto-generated method stub
		return this.path;
	}

	public String getResourceName() {
		// TODO Auto-generated method stub
		return this.resource_name;
	}

	public Map<String, String> getResourceParameters() {
		// TODO Auto-generated method stub
		return this.resourceParams;
	}

	public String getHttpVersion() {
		// TODO Auto-generated method stub
		return this.version;
	}

	public Map<String, String> getHeaderParameters() {
		// TODO Auto-generated method stub
		return this.headerParams;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return this.content;
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		return this.content_length;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
				.append(' ').append(this.getHttpVersion()).append("\r\n");

		for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
			sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
		}

		if (this.getContentLength() > 0) {
			sb.append("\r\n").append(this.getContent());
		}

		return sb.toString();
	}
	
	private boolean esMetodo(String s) {
		
		for(HTTPRequestMethod en: HTTPRequestMethod.values()) {
			
			if(en.name().equals(s))
				return true;
			
		}
		
		return false;
	}
}


