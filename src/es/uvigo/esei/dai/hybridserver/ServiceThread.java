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

public class ServiceThread implements Runnable {
private Socket cliente;
private DefaultPagesController controller;
	public ServiceThread(Socket cliente, DefaultPagesController controller) {
		
	this.cliente = cliente;
	this.controller= controller;
}

	@Override
	public void run() {
      
		try(Socket socket_cliente= this.cliente){
			
			
			Reader rd = new InputStreamReader(socket_cliente.getInputStream());
			Writer wr = new OutputStreamWriter(socket_cliente.getOutputStream());
			HTTPResponse http_response = new HTTPResponse();  
			HTTPRequest http_request = new HTTPRequest(rd);
			try {
			
			HTTPRequestMethod method = http_request.getMethod();
			String uuid;
			String web_content;
			System.out.println("EL NOMBRE DEL RECURSINI ES: "+http_request.getResourceName());
               
			if(http_request.getResourceChain().equals("/")) {
			http_response.setVersion(http_request.getHttpVersion());
			http_response.setStatus(HTTPResponseStatus.S200);
			http_response.setContent("<html> <head><title>Hybrid Server</title>"
					+ "</head><body><h1>Hybrid Server</h1> <a href=\"/html\">html</a></body></html>");
			System.out.println("YEPAAAAAAAAAAAAA");
			http_response.print(wr);
				
			}else {
			if(!http_request.getResourceName().equals("html"))
				throw new BadRequestException("El nombre del recurso no es correcto, no es html");
	          
			
			
			switch(method) {
			
			
			case GET: 
			  if(!http_request.getResourceParameters().containsKey("uuid")){
				  web_content= controller.webList();
				  http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
				  http_response.setStatus(HTTPResponseStatus.S200);
				  
			  }else {
			 
				  try {
			  uuid = http_request.getResourceParameters().get("uuid");
		      web_content = controller.getWeb(uuid);
			  
		      http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
		      http_response.setStatus(HTTPResponseStatus.S200);
			  }catch(NotFoundException e) {
			  
			  web_content = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" + 
				 		"<html>\r\n" + 
				 		"<head>\r\n" + 
				 		"   <title>404 Not Found</title>\r\n" + 
				 		"</head>\r\n" + 
				 		"<body>\r\n" + 
				 		"   <h1>Not Found</h1>\r\n" + 
				 		"   <p>The requested URL /t.html was not found on this server.</p>\r\n" + 
				 		"</body>\r\n" + 
				 		"</html>";
			  
			  http_response.setStatus(HTTPResponseStatus.S404);
			  http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length)); 
			    }
			  
			  }
			  http_response.setVersion(http_request.getHttpVersion());
			  http_response.putParameter("Content-Type", "text-html"); 
			  http_response.setContent(web_content);
			
			  http_response.print(wr);
			
				
			break;
			
			case POST:
				
				try {
				if(http_request.getContentLength() == 0)
					throw new BadRequestException("El contenido de la pagina esta vacio");
				if(http_request.getResourceParameters().containsKey("html")) {
					
	            System.out.println("El contenido antes del SUBSTRING ES: "+http_request.getContent());
			    //String content= http_request.getContent().substring(5, http_request.getContent().length());
			    String content= http_request.getResourceParameters().get("html");
	            content= controller.putPage(content);
				http_response.setContent(content);
				http_response.setStatus(HTTPResponseStatus.S200);
				http_response.setVersion(http_request.getHttpVersion());
				http_response.print(wr);
				}else {
					
					throw new BadRequestException("El recurso no es html");
				}
				}catch(BadRequestException e) {
					
					http_response.setStatus(HTTPResponseStatus.S400);
					web_content = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" + 
					 		"<html>\r\n" + 
					 		"<head>\r\n" + 
					 		"   <title>400 Bad Request </title>\r\n" + 
					 		"</head>\r\n" + 
					 		"<body>\r\n" + 
					 		"   <h1>Not Found</h1>\r\n" + 
					 		"   <p>The requested URL /t.html was not found on this server.</p>\r\n" + 
					 		"</body>\r\n" + 
					 		"</html>";
					  
					  http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
					  http_response.setVersion(http_request.getHttpVersion());
					  http_response.print(wr);
				}
			
				
				
			break;
			
			case DELETE:
		  
				
				try {
					
			if((uuid = http_request.getResourceParameters().get("uuid")) != null ) {
		
			controller.delete(uuid);	
			web_content = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" + 
			 		"<html>\r\n" + 
			 		"<head>\r\n" + 
			 		"   <title>Page Deleted </title>\r\n" + 
			 		"</head>\r\n" + 
			 		"<body>\r\n" + 
			 		"   <h1>Not Found</h1>\r\n" + 
			 		"   <p>The requested URL /t.html was not found on this server.</p>\r\n" + 
			 		"</body>\r\n" + 
			 		"</html>";	
			http_response.setStatus(HTTPResponseStatus.S200);
			http_response.setContent(web_content);	
			http_response.setVersion(http_request.getHttpVersion());
			http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
			http_response.print(wr);
				
			}else {
				throw new BadRequestException("There's no uuid parameter");
			}
			
				}catch (NotFoundException e) {
					
				StringBuilder sb = new StringBuilder();
				String html1= "<!DOCTYPE html>\r\n" + 
						"<html>\r\n" + 
						"    <head>\r\n" + 
						"        <!-- head definitions go here -->\r\n" + 
						"    </head>\r\n" + 
						"    <body>\r\n" + 
						"       <strong>Page with uuid=";
				String html2= "</strong>\\r\\n\" + \r\n" + 
						"			\"    </body>\\r\\n\" + \r\n" + 
						"			\"</html>\"";
				
				  sb.append(html1).append(e.getUuid()).append(html2);
				  web_content= sb.toString();
				  http_response.setStatus(HTTPResponseStatus.S404);
				  http_response.setVersion(http_request.getHttpVersion());
				  http_response.putParameter("Content-Length", Integer.toString(sb.toString().getBytes().length));
				  http_response.print(wr);
			}catch (BadRequestException e) {
				http_response.setStatus(HTTPResponseStatus.S400);
				web_content = "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" + 
				 		"<html>\r\n" + 
				 		"<head>\r\n" + 
				 		"   <title>400 Bad Request </title>\r\n" + 
				 		"</head>\r\n" + 
				 		"<body>\r\n" + 
				 		"   <h1>Not Found</h1>\r\n" + 
				 		"   <p>The requested URL /t.html was not found on this server.</p>\r\n" + 
				 		"</body>\r\n" + 
				 		"</html>";
				  
				  http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
				  http_response.setVersion(http_request.getHttpVersion());
				  http_response.print(wr);
			}
			
				
				
			break;
			
			
			default:
			
			break;
			
			
			}
		    
			}
			}catch  (BadRequestException e) {
				http_response.setVersion(http_request.getHttpVersion());
				http_response.setStatus(HTTPResponseStatus.S400);
				System.out.println("YEPAAAAAAAAAAAAA");
				http_response.print(wr);
				
			}catch  (RuntimeException e) {
				http_response.setVersion(http_request.getHttpVersion());
				http_response.setStatus(HTTPResponseStatus.S500);
				http_response.print(wr);
				
			}
			catch (IOException e) {
				http_response.setVersion(http_request.getHttpVersion());
				http_response.setStatus(HTTPResponseStatus.S500);
				http_response.print(wr);
			
			}
		
		
		
		}catch (HTTPParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		

	}

}
