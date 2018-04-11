package controller;

import java.io.File;

import model.PrIS;
import server.JSONFileServer;
 
public class Application {

	/**
	 * Deze klasse is het startpunt voor de applicatie. Hierin maak je een server 
	 * op een bepaalde poort (bijv. 8888). Daarna is er een PrIS-object gemaakt. Dit
	 * object fungeert als toegangspunt van het domeinmodel. Hiervandaan kan alle
	 * informatie bereikt worden.
	 * 
	 * Om het domeinmodel en de Polymer-GUI aan elkaar te koppelen zijn diverse controllers
	 * gemaakt. Er zijn meerdere controllers om het overzichtelijk te houden. Je mag zoveel
	 * controller-klassen maken als je nodig denkt te hebben. Elke controller krijgt een
	 * koppeling naar het PrIS-object om benodigde informatie op te halen.
	 * 
	 * Je moet wel elke URL die vanaf Polymer aangeroepen kan worden registreren! Dat is
	 * hieronder gedaan voor een drietal URLs. Je geeft daarbij ook aan welke controller
	 * de URL moet afhandelen.
	 * 
	 * Als je alle URLs hebt geregistreerd kun je de server starten en de applicatie in de
	 * browser opvragen! Zie ook de controller-klassen voor een voorbeeld!
	 * 
	 */
	public static void main(String[] args) {
		JSONFileServer server = new JSONFileServer(new File("webapp/app"), 8881);
		
		PrIS infoSysteem = new PrIS();
		
		SysteemDatumController systeemDatumController = new SysteemDatumController(infoSysteem);
		LoginController loginController = new LoginController(infoSysteem);
		EscalatieController escalatieController = new EscalatieController();
		MedestudentenController medestudentenController = new MedestudentenController(infoSysteem);
		PresentieController presentieController = new PresentieController(infoSysteem);
		PersoonlijkController persoonlijkController = new PersoonlijkController(infoSysteem);
		RoosterController roosterController = new RoosterController(infoSysteem);
		CollectieveinzageController cInzageController = new CollectieveinzageController(infoSysteem);
		
		server.registerHandler("/systeemdatum/lesinfo", systeemDatumController);

		server.registerHandler("/login", loginController);
		server.registerHandler("/escalatie/send", escalatieController);
		server.registerHandler("/mijninzage/ophalen/studenten", persoonlijkController);
		server.registerHandler("/mijninzage/ophaal/student", persoonlijkController);
		server.registerHandler("/rooster/ophalen", roosterController);
		server.registerHandler("/collectieveinzage", cInzageController);
		server.registerHandler("/presentie", cInzageController);

		server.registerHandler("/klas", presentieController);
		server.registerHandler("/les/ophalen", presentieController);
		server.registerHandler("/les/opslaan", presentieController);

		server.registerHandler("/student/medestudenten/ophalen", medestudentenController);
		server.registerHandler("/student/medestudenten/opslaan", medestudentenController);
		
		server.start();
	}
}