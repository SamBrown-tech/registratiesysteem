package controller;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonArray;

//import com.sendgrid.*;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.persoon.Student;
import model.presentie.Presentie;
import server.Conversation;
import server.Handler;

class PresentieController implements Handler {
	private PrIS informatieSysteem;

	public PresentieController(PrIS infoSys) {
		informatieSysteem = infoSys;

	}

	public void handle(Conversation conversation) {
		if (conversation.getRequestedURI().startsWith("/klas")) {
			try {
				ophalen(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (conversation.getRequestedURI().startsWith("/les/ophalen")) {
			try {
				ophalen_les(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (conversation.getRequestedURI().startsWith("/les/opslaan")) {
			try {
				opslaan(conversation);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// loop door arraylist..
	// get item
	// update
	// vul presentie

	/*
	 * Verstuur een POST request met klas en les, b.v: {"klas": "V1A", "les":
	 * "V1-GPASDA"} en je zal leerlingen en bijhorende presentie ontvangen.
	 * 
	 */
	private void ophalen_les(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		try {
			String klas = lJsonObjIn.getString("klas");
			String leraar = lJsonObjIn.getString("leraar");
			String data = informatieSysteem.getLessen(klas, leraar);
			conversation.sendJSONMessage(data);
		} catch (NullPointerException e) {
			conversation.sendJSONMessage("{'error': 'NullPointerException'}");
		}

	}

	private void ophalen(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		try {
			String klas = lJsonObjIn.getString("klas");
			String les = lJsonObjIn.getString("les");
			String datum = lJsonObjIn.getString("datum");
			String asd = informatieSysteem.getStudents(klas, les, datum, 0);
			conversation.sendJSONMessage(asd);

		} catch (NullPointerException e) {
			conversation.sendJSONMessage("{'error': 'NullPointerException'}");
		}

	}

	private void opslaan(Conversation conversation) throws IOException, FileNotFoundException, InterruptedException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		FileWriter fw = new FileWriter("presentie.csv", false);
		PrintWriter pw = new PrintWriter(fw);
		String datum = lJsonObjectIn.getString("datum");
		String start_tijd = lJsonObjectIn.getString("start_tijd");
		String eind_tijd = lJsonObjectIn.getString("eind_tijd");
		String les = lJsonObjectIn.getString("les");
		JsonArray presentie_array = lJsonObjectIn.getJsonArray("studenten");
		if (presentie_array != null) {
			ArrayList<Presentie> asd = new ArrayList<Presentie>();
			for (int i = 0; i < presentie_array.size(); i++) {

				JsonObject presentie_array_obj = presentie_array.getJsonObject(i);
				int studentnr = presentie_array_obj.getInt("id");
				boolean presentie = presentie_array_obj.getBoolean("attendance");

				pw.println(studentnr + "," + datum + "," + start_tijd + "," + eind_tijd + "," + presentie + "," + les);

			}

			fw.close();
			pw.close();
		}

	}
}