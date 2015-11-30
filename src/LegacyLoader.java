import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class LegacyLoader extends HttpServlet {

	static JSONObject jsonData = new JSONObject();

	public void init() {

		Thread th = new Thread() {
			public void run() {
				int counter = 2;

				DateFormat dateFormat = new SimpleDateFormat("YYYYMMDDHHmm");
				JSONObject jsonMessage = new JSONObject();
				String aktiivne = null;
				String keel = null;
				String xl_keel = null;
				String xl = null;
				ArrayList<String> overRide = null;
				int logimine = 1;

				while (true) {
					try {
						// TODO: Lisada logimine iga jsonMessage lisamise järgi
						counter++;
						jsonMessage = new JSONObject();
						String url = "http://people.proekspert.ee/ak/data_" + (counter % 9 + 1) + ".txt";
						if (logimine > 0)
							System.out.println("Loading from: " + url);
						String raw = null;
						if ((raw = readData(url)) != null) {
							System.out.println(raw);

							jsonMessage.put("Päringu id", (counter % 9 + 1));

							jsonMessage.put("Telefoni number", raw.substring(1, 21));
							jsonMessage.put("Aktiivne", aktiivne = raw.substring(0, 1));
							if (logimine > 1)
								System.out.println("Aktiivne?");
							if (aktiivne.equals("A")) {
								jsonMessage.put("Teenus aktiivne kuni", dateFormat.parse(raw.substring(24, 36)));

								if (logimine > 1)
									System.out.println("Aktiivne");

								if (raw.substring(22, 23).equals("I"))
									keel = "inglise keel";
								else if (raw.substring(22, 23).equals("E"))
									keel = "eesti keel";
								jsonMessage.put("Teenuse keel", keel);

								if (logimine > 1)
									System.out.println("Keel sai kirja");

								if (raw.substring(23, 24).equals("I"))
									xl_keel = "inglise keeles";
								else if (raw.substring(23, 24).equals("E"))
									xl_keel = "eesti keeles";

								if (logimine > 1)
									System.out.println("XL keel sai kirja");

								if (raw.substring(21, 22).equals("E"))
									xl = "mitteaktiivne";
								else if (raw.substring(21, 22).equals("J"))
									xl = "aktiivne (" + raw.substring(36, 38) + ":" + raw.substring(38, 40) + " - "
											+ raw.substring(40, 42) + ":" + raw.substring(42, 44) + ") " + xl_keel;
								jsonMessage.put("XL-teenus", xl);

								if (logimine > 1)
									System.out.println("Override");

								if (raw.substring(44, 45).equals("E"))
									;
								else if (raw.substring(44, 45).equals("K")) {
									overRide = new ArrayList<String>();
									for (int i = 0; i < 16; i++)
										overRide.add((String) raw.subSequence(44 + i * 0 * 8, 44 + (i + 1) * 0 * 8));
									jsonMessage.put("overRide", overRide);

								}

							}

						} else if (logimine > 1)
							System.out.println("raw == NULL");

						if (logimine > 1)
							System.out.println("JSON omistamine");
						jsonData = jsonMessage;

						if (logimine > 1)
							System.out.println("Sleep");

						Thread.sleep(5 * 60 * 5);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NullPointerException e) {
						System.out.println("NULL pointer exc.");
					}
				}
			}
		};
		th.setDaemon(true);
		th.start();

		// return;

	}

	/*
	 * readData pärib etteantud url-lt teksti ning tagastab selle väljakutsunud
	 * meetodile.
	 */
	private String readData(String url) throws IOException {
		URL legacy;
		try {
			legacy = new URL(url);
		} catch (MalformedURLException e) {
			return url + " ei ole korrektne URL!";
			// e.printStackTrace();
		}
		URLConnection lc;
		try {
			lc = legacy.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(lc.getInputStream()));
			String inputLine;
			String response = new String();

			while ((inputLine = in.readLine()) != null) {

				response = response + inputLine;
			}
			in.close();
			return response;
		} catch (IOException e) {
			return null;
		} catch (NullPointerException e) {
			System.out.println("NULL Pointer Andmete lugemisel!");
			return null;
		}

	}

}
