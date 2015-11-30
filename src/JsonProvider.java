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
 
@Path("/JsonProvider")
public class JsonProvider {

	static JSONObject jsonData = new JSONObject();
	
	@GET
	@Produces("application/json")
	public Response getResponse()
	{
		return Response.status(200).entity(LegacyLoader.jsonData.toString() ).build();
		
	}
}
