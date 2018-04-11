package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import model.PrIS;
import model.klas.Klas;
import model.persoon.Student;
import server.Conversation;
import server.Handler;

public class CollectieveinzageController implements Handler {
	private PrIS informatieSysteem;

	public CollectieveinzageController(PrIS infoSys) {
		informatieSysteem = infoSys;
	}

	public void handle(Conversation conversation) {

		if (conversation.getRequestedURI().startsWith("/collectieveinzage")) {
			try {
				ophalen(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else if (conversation.getRequestedURI().startsWith("/presentie")){
			try {
				ophalenPresentieC(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
			}
		}
	}

	private void ophalen(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println("stuurt object" + lJsonObjIn);
		try {
			String klas = lJsonObjIn.getString("klas");
			String studentenArray = informatieSysteem.getStudents(klas, "", "", 1);
			System.out.println("SKUUURT123" + studentenArray);
			conversation.sendJSONMessage(studentenArray);

		} catch (NullPointerException e) {
			conversation.sendJSONMessage("{'error': 'NullPointerException'}");
		}

	}
	
	private void ophalenPresentieC(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String klas = lJsonObjIn.getString("klas");
		System.out.println(klas);
		String data = informatieSysteem.getPresentieC(klas);
		
		conversation.sendJSONMessage(data);

	}
}
