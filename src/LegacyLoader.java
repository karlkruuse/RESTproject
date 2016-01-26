
import javax.servlet.http.HttpServlet;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class LegacyLoader extends HttpServlet {

	static JSONObject jsonData = new JSONObject();

	public void init() {

		Thread th = new LoaderThread() ;
		th.setDaemon(true);
		th.start();

		// return;

	}


}
