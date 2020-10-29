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
		this.status = null;
		this.version = "";
		this.content = "";
		this.parameters = new LinkedHashMap<>();
	}

	public HTTPResponseStatus getStatus() {
		// TODO Auto-generated method stub
		return this.status;
	}

	public void setStatus(HTTPResponseStatus status) {
		this.status = status;
	}

	public String getVersion() {
		// TODO Auto-generated method stub
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		// TODO Auto-generated method stub
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, String> getParameters() {
		// TODO Auto-generated method stub
		return this.parameters;
	}

	public String putParameter(String name, String value) {
		// TODO Auto-generated method stub
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
		// parameters_list.addAll(this.parameters.keySet());
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
			pw.write("\n");

			pw.write(this.listParameters().toString());
			pw.write("\n");

			if (this.content.length() != 0) {
				pw.write(this.content);
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
