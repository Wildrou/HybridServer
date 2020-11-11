package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {

	private HTTPResponseStatus status;
	private String version;
	private String content;
	private Map<String, String> parameters;

	public HTTPResponse() {
		this.status =HTTPResponseStatus.S200;
		this.version = "";
		this.content = "";
		this.parameters = new LinkedHashMap<>();
	}

	public HTTPResponseStatus getStatus() {
		return this.status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		return this.parameters;
	}

	public String putParameter(String name, String value) {
		String previous_value = null;
		if (this.containsParameter(name))
			previous_value = parameters.get(name);
		this.parameters.put(name, value);
		return previous_value;
	}

	public boolean containsParameter(String name) {
		return this.parameters.containsKey(name);
	}

	public String removeParameter(String name) {
		String previous_value = null;
		if (this.containsParameter(name))
			previous_value = parameters.get(name);
		this.parameters.remove(name);
		return previous_value;
	}

	public void clearParameters() {
		this.parameters.clear();
	}

	public List<String> listParameters() {
		List<String> parameters_list = new ArrayList<>();
		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			parameters_list.add(entry.getKey() + ": " + entry.getValue());
		}
		return parameters_list;
	}

	public void print(Writer writer) throws IOException {
		try (PrintWriter pw = new PrintWriter(writer)) {
			pw.write(this.version);
			pw.write(" ");
			String code = Integer.toString(this.status.getCode());
			pw.write(code);
			pw.write(" ");
			pw.write(this.status.getStatus());
			
			pw.write("\r\n");
             if(!this.listParameters().isEmpty()) {
			pw.write(this.listParameters().toString());
			pw.write("\r\n");
             }
			if (this.content.length() != 0) {
				pw.write("Content-Length: "+this.content.length());
				pw.write("\r\n\r\n");
				
				pw.write(this.content);
			}else {
				pw.write("\r\n");
			}
			pw.flush();

		}

	}

	@Override
	public String toString() {
		final StringWriter writer = new StringWriter();

		try {
			this.print(writer);
		} catch (IOException e) {
		}

		return writer.toString();
	}
}
