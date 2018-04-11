package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import server.Conversation;
import server.Handler;

import model.PrIS;
import model.persoon.Student;
import model.presentie.Presentie;
import model.les.Les;

public class RoosterController implements Handler {
	
	private PrIS informatieSysteem;
	private ArrayList<Les> lessen;

	public RoosterController(PrIS infoSys) {
		informatieSysteem = infoSys;
		lessen = new ArrayList<Les>();

	}
	
	public void handle(Conversation conversation) {
		System.out.println(conversation.getRequestedURI());
		if (conversation.getRequestedURI().startsWith("/rooster/ophalen")) {
			try {
				ophalen(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ophalen(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println("object" + lJsonObjIn);
		try {
			String klas = lJsonObjIn.getString("klas");
			String datum = lJsonObjIn.getString("datumPicked");
			
			informatieSysteem.getRooster(klas, datum);
			conversation.sendJSONMessage(informatieSysteem.getRooster(klas, datum));

		} catch (NullPointerException e) {
			conversation.sendJSONMessage("{'error': 'NullPointerException'}");
		}
		
	}
}
