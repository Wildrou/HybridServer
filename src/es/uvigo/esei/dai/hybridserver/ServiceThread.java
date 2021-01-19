package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import es.uvigo.esei.dai.controller.DefaultPagesController;
import es.uvigo.esei.dai.entidades.XSLT;
import es.uvigo.esei.dai.hybridserver.dao.HTMLDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.XMLDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.XSDDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.XSLTDBDAO;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.MIME;
import es.uvigo.esei.dai.utils.HTMLUtils;

public class ServiceThread implements Runnable {
	private Socket cliente;
	//private Configuration config;
	private Properties properties;
	private DefaultPagesController controller;

	public ServiceThread(Socket cliente, Properties prop) {

		this.cliente = cliente;
		this.properties = prop;
	}

	@Override
	public void run() {

		try (Socket socket_cliente = this.cliente) {

			Reader rd = new InputStreamReader(socket_cliente.getInputStream());
			Writer wr = new OutputStreamWriter(socket_cliente.getOutputStream());
			HTTPResponse http_response = new HTTPResponse();
			HTTPRequest http_request = new HTTPRequest(rd);
			try {

				HTTPRequestMethod method = http_request.getMethod();
				String uuid;

				String web_content;

				if (http_request.getResourceChain().equals("/")) {
					http_response.setVersion(http_request.getHttpVersion());
					http_response.setContent(HTMLUtils.DEFAULT_PAGE);
					http_response.print(wr);

				} else {
					if (!http_request.getResourceName().equals("html") 
							&& !http_request.getResourceName().equals("xml")
							&& !http_request.getResourceName().equals("xsd")
							&& !http_request.getResourceName().equals("xslt"))

						throw new BadRequestException("The resource names doest not match html or any valid resource");

					String resource_name = http_request.getResourceName();
					this.controller = getController(this.properties, resource_name);

					switch (method) {

					case GET:
						if (!http_request.getResourceParameters().containsKey("uuid")) {
							web_content = HTMLUtils.generateHTMLWebs(controller.webList());
							http_response.setContent(web_content);
							http_response.putParameter("Content-Type", "text/html");
						} else {
							if (resource_name.equals("xml") && http_request.getResourceParameters().containsKey("uuid")
									&& http_request.getResourceParameters().containsKey("xslt")) {
								try {
									uuid = http_request.getResourceParameters().get("uuid");
									String xslt_uuid = http_request.getResourceParameters().get("xslt");

									String web_content_uuid = controller.getWeb(uuid);
									XSLT xslt = controller.getDAO_XML().getXSLT(xslt_uuid);
									String web_content_xsd = controller.getDAO_XML().getWeb_XSD(xslt.getUuid_xsd());

									StringReader input = new StringReader(web_content_uuid);
									StringReader xsltReader = new StringReader(xslt.getContent());
									StringReader xsdReader = new StringReader(web_content_xsd);
									
									StringWriter output = new StringWriter();

									try {
										SchemaFactory schemaFactory = SchemaFactory
												.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
										Schema schema = schemaFactory.newSchema(new StreamSource(xsdReader));
										Validator validator = schema.newValidator();
										validator.validate(new StreamSource(input));
									} catch (SAXException e) {
										http_response.setStatus(HTTPResponseStatus.S404);
									}
									TransformerFactory tFactory = TransformerFactory.newInstance();
									Transformer transformer = tFactory.newTransformer(new StreamSource(xsltReader));
									transformer.transform(new StreamSource(input), new StreamResult(output));

									http_response.setContentType("html");
									http_response.setContent(output.getBuffer().toString());


								} catch (NotFoundException e) {

									http_response.setStatus(HTTPResponseStatus.S404);

								}
							}
							else {
								uuid = http_request.getResourceParameters().get("uuid");
								web_content = controller.getWeb(uuid);
								
								http_response.setContentType(resource_name);
							}
							http_response.setVersion(http_request.getHttpVersion());

							http_response.print(wr);

						}

						break;

					case POST:

						try {
							if (http_request.getContentLength() == 0)
								throw new BadRequestException("The content is empty");
							if (http_request.getResourceParameters().containsKey("html")
									|| http_request.getResourceParameters().containsKey("xml")
									|| http_request.getResourceParameters().containsKey("xsd")
									|| http_request.getResourceParameters().containsKey("xslt")) {
								String[] content_array = new String[2];
								content_array[0] = http_request.getResourceParameters().get(resource_name);
								if (http_request.getResourceName().equals("xslt")) {
									if (!http_request.getResourceParameters().containsKey("xsd"))
										throw new BadRequestException("Missing xsd parameter for xslt post");
									if (!controller.getDAO().xsdExist(http_request.getResourceParameters().get("xsd")))
										throw new NotFoundException("There is no xsd matching the given uuid");
									else {
										content_array[1] = http_request.getResourceParameters().get("xsd");
									}

								}
								String content = HTMLUtils.generateNewPageLink(controller.putPage(content_array));
								http_response.putParameter("Content-Type", MIME.TEXT_HTML.getMime());
								http_response.setContent(content);
							} else {

								throw new BadRequestException("The resource name is not a valid one");
							}
						} catch (BadRequestException e) {

							http_response.setStatus(HTTPResponseStatus.S400);

						}
						http_response.setVersion(http_request.getHttpVersion());
						http_response.setContentType(resource_name);
						http_response.print(wr);

						break;

					case DELETE:

						try {

							if ((uuid = http_request.getResourceParameters().get("uuid")) != null) {

								controller.delete(uuid);

							} else {
								throw new BadRequestException("There's no uuid parameter");
							}

						} catch (NotFoundException e) {

							http_response.setStatus(HTTPResponseStatus.S404);
						} catch (BadRequestException e) {
							http_response.setStatus(HTTPResponseStatus.S400);
						}
						http_response.setVersion(http_request.getHttpVersion());
						http_response.print(wr);

						break;

					default:
						throw new BadRequestException("Service not implemented");

					}

				}
			} catch (BadRequestException e) {
				http_response.setVersion(http_request.getHttpVersion());
				http_response.setStatus(HTTPResponseStatus.S400);
				http_response.print(wr);

			} catch (RuntimeException e) {
				http_response.setVersion(http_request.getHttpVersion());
				http_response.setStatus(HTTPResponseStatus.S500);
				http_response.print(wr);

			} catch (IOException e) {
				http_response.setVersion(http_request.getHttpVersion());
				http_response.setStatus(HTTPResponseStatus.S500);
				http_response.print(wr);

			}

		} catch (HTTPParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private DefaultPagesController getController(Properties prop, String resource) {
		DefaultPagesController controller;
		switch (resource) {

		case "html":
			controller = new DefaultPagesController(new HTMLDBDAO(prop));
			break;

		case "xml":
			controller = new DefaultPagesController(new XMLDBDAO(prop));
			break;

		case "xsd":
			controller = new DefaultPagesController(new XSDDBDAO(prop));
			break;

		case "xslt":
			controller = new DefaultPagesController(new XSLTDBDAO(prop));
			break;

		default:
			controller = new DefaultPagesController(new HTMLDBDAO(prop));
			break;

		}

		return controller;
	}

}
