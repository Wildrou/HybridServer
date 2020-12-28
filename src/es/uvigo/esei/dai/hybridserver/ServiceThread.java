package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import es.uvigo.esei.dai.controller.DefaultPagesController;
import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import utils.HTMLUtils;

public class ServiceThread implements Runnable {
	private Socket cliente;
	private DefaultPagesController controller;

	public ServiceThread(Socket cliente, DefaultPagesController controller) {

		this.cliente = cliente;
		this.controller = controller;
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
					http_response.setContent("<html> <head><title>Hybrid Server</title>"
							+ "</head><body><p>Miguel Arias Perez</p><p>Victor Otero Cabaleiro</p><h1>Hybrid Server</h1> <a href=\"/html\">html</a></body></html>");
					http_response.print(wr);

				} else {
					if (!http_request.getResourceName().equals("html"))
						throw new BadRequestException("The resource names doest not match html or any valid resource");

					switch (method) {

					case GET:
						if (!http_request.getResourceParameters().containsKey("uuid")) {
							web_content = HTMLUtils.generateHTMLWebs(controller.webList());
							http_response.setContent(web_content);

						} else {

							try {
								uuid = http_request.getResourceParameters().get("uuid");
								web_content = controller.getWeb(uuid);
								http_response.setContent(web_content);
								
							} catch (NotFoundException e) {

			
								http_response.setStatus(HTTPResponseStatus.S404);
							
							}

						}
						http_response.setVersion(http_request.getHttpVersion());
						http_response.print(wr);

						break;

					case POST:

						try {
							if (http_request.getContentLength() == 0)
								throw new BadRequestException("The content is empty");
							if (http_request.getResourceParameters().containsKey("html")) {
								
								String content = http_request.getResourceParameters().get("html");
								content = HTMLUtils.generateNewPageLink(controller.putPage(content));
								http_response.putParameter("Content-Type","text/html");
								http_response.setContent(content);
							} else {

								throw new BadRequestException("The resource name is not html");
							}
						} catch (BadRequestException e) {

							http_response.setStatus(HTTPResponseStatus.S400);
					

						}
						http_response.setVersion(http_request.getHttpVersion());
						http_response.putParameter("Content-Type","text/html");
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
						http_response.putParameter("Content-Type","text/html");
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

}
