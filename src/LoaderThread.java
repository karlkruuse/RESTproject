import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class LoaderThread extends Thread {
	public void run() {
		int counter = 2;

		DateFormat dateFormatFrom = new SimpleDateFormat("YYYYMMDDHHmm");
		JSONObject jsonMessage = new JSONObject();
		String aktiivne = null;
		String keel = null;
		String xl_keel = null;
		String xl = null;
		ArrayList<String> overRideNimed = null;
		ArrayList<String> overRideNumbrid = null;

		while (true) {
			try {
				counter++;
				jsonMessage = new JSONObject();
				String url = "http://people.proekspert.ee/ak/data_" + (counter % 9 + 1) + ".txt";
				String raw = null;
				if ((raw = readData(url)) != null) {

					jsonMessage.put("ParinguId", (counter % 9 + 1));
					jsonMessage.put("TelefoniNumber", raw.substring(1, 21));
					jsonMessage.put("Aktiivne", aktiivne = raw.substring(0, 1));
					if (aktiivne.equals("A")) {
						jsonMessage.put("TeenusAktiivneKuni", "aktiivne kuni: " + dateFormatFrom.parse(raw.substring(24, 36)));

						if (raw.substring(22, 23).equals("I"))
							keel = "inglise keel";
						else if (raw.substring(22, 23).equals("E"))
							keel = "eesti keel";
						jsonMessage.put("TeenuseKeel", keel);

						if (raw.substring(23, 24).equals("I"))
							xl_keel = "inglise keeles";
						else if (raw.substring(23, 24).equals("E"))
							xl_keel = "eesti keeles";

						if (raw.substring(21, 22).equals("E"))
							xl = "mitteaktiivne";
						else if (raw.substring(21, 22).equals("J"))
							xl = "aktiivne (" + raw.substring(36, 38) + ":" + raw.substring(38, 40) + " - "
									+ raw.substring(40, 42) + ":" + raw.substring(42, 44) + ") " + xl_keel;
						jsonMessage.put("XLteenus", xl);

						if (raw.substring(44, 45).equals("E"));
						else if (raw.substring(44, 45).equals("K")) {
							overRideNimed = new ArrayList<String>();
							overRideNumbrid = new ArrayList<String>();
							for (int i = 0; i < 8; i++)
								if (!raw.substring(44 + i * 15, 44 + (i + 1) * 15).trim().equals(""))
										overRideNumbrid.add((String) raw.substring(44 + i * 15, 44 + (i + 1) * 15));
							for (int i = 0; i < 8; i++)
								if (!raw.substring(164 + i * 20, 164 + (i + 1) * 20).trim().equals(""))
										overRideNimed.add((String) raw.subSequence(164 + i * 20, 164 + (i + 1) * 20));
							jsonMessage.put("overRideNumbrid", overRideNumbrid);
							jsonMessage.put("overRideNimed", overRideNimed);

						}

					}
					else jsonMessage.put("TeenusAktiivneKuni", "mitte aktiivne");

				} 
				LegacyLoader.jsonData = jsonMessage;


				Thread.sleep(5000);
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
			}
		}
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
