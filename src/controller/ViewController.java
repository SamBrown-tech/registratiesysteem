package controller;

import java.io.IOException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import server.Conversation;
import server.Handler;
import com.sendgrid.*;

public class ViewController implements Handler {
	
	
	public void handle(Conversation conversation) {
		System.out.println(conversation.getRequestedURI());
		try {
			sturen(conversation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sturen(Conversation conversation) throws IOException {
		JsonObject lJsonObjectIn = (JsonObject) conversation.getRequestBodyAsJSON();
		String StudentNaam = lJsonObjectIn.getString("student");
		String EscalatieNote = lJsonObjectIn.getString("note");
		String slber = lJsonObjectIn.getString("slber");
		int emailSucces = 0;
		
		System.out.println(StudentNaam + " " + EscalatieNote);

	    Email from = new Email("wesleyvanstra@gmail.com");
	    String subject = "Escalatie betreft " + StudentNaam;

	    Email to = new Email(slber);
	    Content content = new Content("text/plain", EscalatieNote);
	    Mail mail = new Mail(from, subject, to, content);

	    SendGrid sg = new SendGrid("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");
	    Request request = new Request();
	    
	
	    try {
	      request.setBody(mail.build());
	      request.setEndpoint("mail/send");
	      request.setMethod(Method.POST);

	      Response response = sg.api(request);
	      System.out.println(response.getStatusCode());
	      System.out.println(response.getBody());
	      System.out.println(response.getHeaders());
	      emailSucces = 1;

	    } catch (IOException ex) {
	      throw ex;
	    }
		
		
	  	JsonObjectBuilder lJob = Json.createObjectBuilder(); 
	  	lJob.add("succes", emailSucces);
	   	//nothing to return use only errorcode to signal: ready!
	  	String lJsonOutStr = lJob.build().toString();
 		conversation.sendJSONMessage(lJsonOutStr);	
		
	}
}
