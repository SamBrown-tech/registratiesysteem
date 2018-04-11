package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import server.Conversation;
import server.Handler;
import com.sendgrid.*;

import model.PrIS;
import model.persoon.Student;
import model.presentie.Presentie;

public class PersoonlijkController implements Handler {
	
	private PrIS informatieSysteem;
	private ArrayList<Presentie> dePresentie;

	public PersoonlijkController(PrIS infoSys) {
		informatieSysteem = infoSys;
		dePresentie = new ArrayList<Presentie>();

	}
	
	public void handle(Conversation conversation) {
		System.out.println(conversation.getRequestedURI());
		if (conversation.getRequestedURI().startsWith("/mijninzage/ophalen/studenten")) {
			try {
				ophalen(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (conversation.getRequestedURI().startsWith("/mijninzage/ophaal/student")) {
			try {
				ophalenStudent(conversation);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void ophalen(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		System.out.println("skuurt object" + lJsonObjIn);
		try {
			String klas = lJsonObjIn.getString("klas");
			String studentenArray = informatieSysteem.getStudents(klas, "", "", 1);
			System.out.println("SKUUURT" + studentenArray);
			conversation.sendJSONMessage(studentenArray);

		} catch (NullPointerException e) {
			conversation.sendJSONMessage("{'error': 'NullPointerException'}");
		}
		
	}
	
	private void ophalenStudent(Conversation conversation) throws IOException {
		JsonObject lJsonObjIn = (JsonObject) conversation.getRequestBodyAsJSON();
		
		try {
			String nummer = lJsonObjIn.getString("student");
			int studentnr = Integer.parseInt(nummer);
			
			System.out.println(informatieSysteem.getPresentieP(studentnr));
			conversation.sendJSONMessage(informatieSysteem.getPresentieP(studentnr));

		} catch (NullPointerException e) {
			conversation.sendJSONMessage("{'error': 'NullPointerException'}");
		}
	}
}
