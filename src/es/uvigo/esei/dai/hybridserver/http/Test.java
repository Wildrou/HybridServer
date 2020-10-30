package es.uvigo.esei.dai.hybridserver.http;

public class Test {

	
	
	
	public static void main(String[] args) {
		
		String test = "/hello/world.html?country=Spain&province=Ourense&city=Ourense";
		
		String[] resource_chain_array = test.split("/");
		
		System.out.print("posicion 0:" +resource_chain_array[1]);
	}
}
