package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.util.Properties;

import es.uvigo.esei.dai.controller.DefaultPagesController;
import es.uvigo.esei.dai.controller.PagesController;
import es.uvigo.esei.dai.hybridserver.dao.HTMLDBDAO;
import es.uvigo.esei.dai.hybridserver.dao.PagesDAO;
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
	private Properties properties;
    private DefaultPagesController controller;
	public ServiceThread(Socket cliente, Properties properties) {

		this.cliente = cliente;
		this.properties = properties;
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
					if (!http_request.getResourceName().equals("html") || !http_request.getResourceName().equals("xml")
							|| !http_request.getResourceName().equals("xsd")
							|| !http_request.getResourceName().equals("xslt"))

						throw new BadRequestException("The resource names doest not match html or any valid resource");
                   
					String resource_name = http_request.getResourceName();
					this.controller=getController(this.properties,resource_name );
				
                    switch (method) {

					case GET:
						if (!http_request.getResourceParameters().containsKey("uuid")) {
							web_content = HTMLUtils.generateHTMLWebs(controller.webList());
							http_response.setContent(web_content);
							http_response.putParameter("Content-Type","text/html");
						} else {

							try {
								uuid = http_request.getResourceParameters().get("uuid");
								web_content = controller.getWeb(uuid);
								http_response.setContent(web_content);

							} catch (NotFoundException e) {

								http_response.setStatus(HTTPResponseStatus.S404);

							}
							http_response.setContentType(resource_name);
						}
						http_response.setVersion(http_request.getHttpVersion());
						
						http_response.print(wr);

						break;

					case POST:

						try {
							if (http_request.getContentLength() == 0)
								throw new BadRequestException("The content is empty");
							if (http_request.getResourceParameters().containsKey("html")
									|| http_request.getResourceParameters().containsKey("xml") 
									|| http_request.getResourceParameters().containsKey("xsd")
									|| http_request.getResourceParameters().containsKey("xslt")
									) {
								String [] content_array= new String[2];
								content_array[0]= http_request.getResourceParameters().get(resource_name);
								if(http_request.getResourceName().equals("xslt")) {
									if(!http_request.getResourceParameters().containsKey("xsd"))
											throw new BadRequestException("Missing xsd parameter for xslt post");
									if(!controller.getDAO().xsdExist(http_request.getResourceParameters().get("xsd")))
									      throw new NotFoundException("There is no xsd matching the given uuid");
									else{
										content_array[1]=http_request.getResourceParameters().get("xsd");
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
            controller= new DefaultPagesController(new HTMLDBDAO(prop));
			break;

		case "xml":
			controller= new DefaultPagesController(new XMLDBDAO(prop));
			break;
			
		case "xsd":
			controller= new DefaultPagesController(new XSDDBDAO(prop));
			break;

		case "xslt":
			controller= new DefaultPagesController(new XSLTDBDAO(prop));
			break;
			
		default:
			  controller= new DefaultPagesController(new HTMLDBDAO(prop));
				break;
			

		}

		return controller;
	}
    

}
