package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

import org.apache.http.HttpStatus;

import es.uvigo.esei.dai.hybridserver.http.HTTPParseException;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequest;
import es.uvigo.esei.dai.hybridserver.http.HTTPRequestMethod;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class ServiceThread implements Runnable {
private Socket cliente;

	public ServiceThread(Socket cliente) {
		
	this.cliente = cliente;
}

	@Override
	public void run() {
      
		try(Socket socket_cliente= this.cliente){
			
			
			Reader rd = new InputStreamReader(socket_cliente.getInputStream());
			Writer wr = new OutputStreamWriter(socket_cliente.getOutputStream());
			HTTPResponse http_response = new HTTPResponse();  
			HTTPRequest http_request = new HTTPRequest(rd);
			
			
			HTTPRequestMethod method = http_request.getMethod();
			String uuid;
			WebManager webs = new WebManager();
			String web_content;
			
			switch(method) {
			
			
			case GET: 
			 
			 
			 
			 
			  if(!http_request.getResourceParameters().containsKey("uuid")){
				  web_content= webs.webList();
				  http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
				  http_response.setStatus(HTTPResponseStatus.S200);
				  
			  }else {
			 
				  try {
			  uuid = http_request.getResourceParameters().get("uuid");
		      web_content = webs.getWeb(uuid);
		      http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
		      http_response.setStatus(HTTPResponseStatus.S200);
			  }catch(Exception e) {
			  
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
				
				
				
			break;
			
			case DELETE:
		  
				
				try {
					
			if((uuid = http_request.getResourceParameters().get("uuid")) != null) {
		
			webs.delete(uuid);	
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
			http_response.putParameter("Content-Length", Integer.toString(web_content.getBytes().length));
			
				
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
				  http_response.putParameter("Content-Length", Integer.toString(sb.toString().getBytes().length));
				
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
				
			}
			
				
				
			break;
			
			
			default:
			
			break;
			
			
			}
		
		
		
		
		
		
		} catch (HTTPParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		
    
		
		}
		
		
		

	}

}
