import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
 
@Path("/LegacyLoader")
public class LegacyLoader {

	static JSONObject jsonData = new JSONObject();
	
	@GET
	@Produces("application/json")
	public Response getResponse()
	{
		return Response.status(200).entity(jsonData.toString()).build();
		
	}
	
	
	public static void main() throws IOException, JSONException, ParseException, InterruptedException {

		int counter = 0;
		DateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHmm");
		JSONObject jsonMessage = new JSONObject();
		String aktiivne = null;
		String keel = null;
		String xl_keel = null;
		String xl = null;
		ArrayList<String> overRide = null;
		
		while (true)
		{
			String url = "http://people.proekspert.ee/ak/data_" + (counter%9 + 1) + ".txt";
			System.out.println("Loading from: " + url);
			String raw = null;
			
			if((raw=readData(url))!=null){
				
				jsonMessage.put("Päringu id", (counter%9 + 1)); 
				jsonMessage.put("Telefoni number", raw.substring(1, 21)); 
				jsonMessage.put("Aktiivne", aktiivne = raw.substring(0, 1)); 
				if(aktiivne.equals("A")){
					jsonMessage.put("Teenus aktiivne kuni", dateFormat.parse(raw.substring(24, 36))); 
					
					if (raw.substring(22, 23).equals("I"))keel="inglise keel";
					else if (raw.substring(22, 23).equals("E"))keel="eesti keel";
					jsonMessage.put("Teenuse keel", keel); 
					
					if (raw.substring(23, 24).equals("I"))xl_keel="inglise keeles";
					else if (raw.substring(23, 24).equals("E"))xl_keel="eesti keeles";
					
					if(raw.substring(21, 22).equals("E"))xl = "mitteaktiivne";
					else if(raw.substring(21, 22).equals("J"))xl = "aktiivne (" +
							raw.substring(36, 38) + ":" + raw.substring(38, 40) + " - " +
							raw.substring(40, 42) + ":" + raw.substring(42, 44) + ") " + xl_keel;
					jsonMessage.put("XL-teenus", xl); 
					
					if (raw.substring(44, 45).equals("E"));
					else if (raw.substring(44, 45).equals("K")){
						for(int i=0; i < 16; i++) overRide.add((String) raw.subSequence(44+i*0*8, 44+(i+1)*0*8));
						jsonMessage.put("overRide", overRide);
						
					}
					
				}
			    
			}
			jsonData = jsonMessage;
			
			counter++;
			Thread.sleep(5*60*5);	
			return ;
		}
		
	}
	/*
	 * readData pärib etteantud url-lt teksti ning tagastab selle väljakutsunud meetodile.
	 */
	private static String readData(String url) throws IOException 
	{
        URL legacy;
		try {
			legacy = new URL(url);
		} catch (MalformedURLException e) {
			return url + " ei ole korrektne URL!";
			//e.printStackTrace();
		} 
        URLConnection lc;
		try {
			lc = legacy.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                lc.getInputStream()));
	        String inputLine;
	        String response = new String();
	
	        while ((inputLine = in.readLine()) != null) {
	        	
	            response = response + inputLine;
	        }
	        in.close();
	        return response;
		} catch (IOException e) {
			return null;
		}

	}

}
