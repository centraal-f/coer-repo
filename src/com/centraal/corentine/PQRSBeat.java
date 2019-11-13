package com.centraal.corentine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

public class PQRSBeat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		while (true) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			PrintWriter pw = null;

			try {
				fw = new FileWriter("udr_menu.log", true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);

			try {

				System.out.println("Beating...");
				long start = Calendar.getInstance().getTimeInMillis();
				URL obj = new URL("https://us-central1-labo-corentine.cloudfunctions.net/beat");
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				con.setRequestMethod("POST");
				// con.setRequestProperty("User-Agent", USER_AGENT);

				// For POST only - START
				con.setDoOutput(true);
				OutputStream os = con.getOutputStream();
				os.write("{}".getBytes());
				os.flush();
				os.close();
				// For POST only - END
				int responseCode = con.getResponseCode();
				if (responseCode == 200) {
					System.out.println("Beat -> " + responseCode + " ("
							+ (Calendar.getInstance().getTimeInMillis() - start) + " ms.)");
					Scanner s = new Scanner(con.getInputStream()).useDelimiter("\\A");
					String outG = s.next();
					if (!outG.equals("{\"status\":\"OK\", \"passed\":[],\"released\":[],\"executed\":[]}"))
						pw.println("[" + Calendar.getInstance().getTime() + "]" + outG);
					pw.flush();
				} else {
					System.err.println("You got problems!!!");
					Scanner s = new Scanner(con.getErrorStream()).useDelimiter("\\A");
					String outG = s.next();
					pw.println("[" + Calendar.getInstance().getTime() + "]" + outG);
					pw.flush();
				}

				Thread.sleep(5000);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

	}

}
