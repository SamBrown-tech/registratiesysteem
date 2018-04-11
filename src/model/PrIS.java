package model;

import java.util.ArrayList;

import model.klas.Klas;
import model.persoon.Docent;
import model.persoon.Student;
import model.presentie.Presentie;
import model.les.Les;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

public class PrIS {
	private ArrayList<Docent> deDocenten;
	private ArrayList<Student> deStudenten;
	private ArrayList<Klas> deKlassen;
	private ArrayList<Les> deLessen;
	private ArrayList<Presentie> dePresentie;

	/**
	 * De constructor maakt een set met standaard-data aan. Deze data moet nog
	 * uitgebreidt worden met rooster gegevens die uit een bestand worden ingelezen,
	 * maar dat is geen onderdeel van deze demo-applicatie!
	 *
	 * De klasse PrIS (PresentieInformatieSysteem) heeft nu een meervoudige
	 * associatie met de klassen Docent, Student, Vakken en Klassen Uiteraard kan
	 * dit nog veel verder uitgebreid en aangepast worden!
	 *
	 * De klasse fungeert min of meer als ingangspunt voor het domeinmodel. Op dit
	 * moment zijn de volgende methoden aanroepbaar:
	 *
	 * String login(String gebruikersnaam, String wachtwoord) Docent
	 * getDocent(String gebruikersnaam) Student getStudent(String gebruikersnaam)
	 * ArrayList<Student> getStudentenVanKlas(String klasCode)
	 *
	 * Methode login geeft de rol van de gebruiker die probeert in te loggen, dat
	 * kan 'student', 'docent' of 'undefined' zijn! Die informatie kan gebruikt
	 * worden om in de Polymer-GUI te bepalen wat het volgende scherm is dat getoond
	 * moet worden.
	 *
	 */
	public PrIS() {
		deDocenten = new ArrayList<Docent>();
		deStudenten = new ArrayList<Student>();
		deKlassen = new ArrayList<Klas>();
		deLessen = new ArrayList<Les>();
		dePresentie = new ArrayList<Presentie>();

		// Inladen klassen
		vulKlassen(deKlassen);
		vulPresentie(dePresentie);

		// Inladen studenten in klassen
		vulStudenten(deStudenten, deKlassen);

		// Inladen docenten
		vulDocenten(deDocenten);
		vulLessen(deLessen);

	} // Einde Pris constructor

	// deze method is static onderdeel van PrIS omdat hij als hulp methode
	// in veel controllers gebruikt wordt
	// een standaardDatumString heeft formaat YYYY-MM-DD
	public static Calendar standaardDatumStringToCal(String pStadaardDatumString) {
		Calendar lCal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			lCal.setTime(sdf.parse(pStadaardDatumString));
		} catch (ParseException e) {
			e.printStackTrace();
			lCal = null;
		}
		return lCal;
	}

	// deze method is static onderdeel van PrIS omdat hij als hulp methode
	// in veel controllers gebruikt wordt
	// een standaardDatumString heeft formaat YYYY-MM-DD
	public static String calToStandaardDatumString(Calendar pCalendar) {
		int lJaar = pCalendar.get(Calendar.YEAR);
		int lMaand = pCalendar.get(Calendar.MONTH) + 1;
		int lDag = pCalendar.get(Calendar.DAY_OF_MONTH);

		String lMaandStr = Integer.toString(lMaand);
		if (lMaandStr.length() == 1) {
			lMaandStr = "0" + lMaandStr;
		}
		String lDagStr = Integer.toString(lDag);
		if (lDagStr.length() == 1) {
			lDagStr = "0" + lDagStr;
		}
		String lString = Integer.toString(lJaar) + "-" + lMaandStr + "-" + lDagStr;
		return lString;
	}

	public Docent getDocent(String gebruikersnaam) {
		Docent resultaat = null;

		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				resultaat = d;
				break;
			}
		}

		return resultaat;
	}

	public Klas getKlas(String klas) { // nieuw
		for (Klas lKlas : deKlassen) {
			if (lKlas.getNaam().equals(klas)) {
				return lKlas;
			}
		}
		return null;
	}

	public ArrayList<Les> docentLessen(Docent d) { // nieuw
		ArrayList<Les> lessenDocent = new ArrayList<Les>();
		for (Les l : deLessen) {
			if (l.getDocent().equals(d)) {
				lessenDocent.add(l);
			}
		}
		return lessenDocent;
	}

	public String getLessen(String klas, String leraar) throws IOException {

		JsonArrayBuilder builder = Json.createArrayBuilder();
		JsonObjectBuilder objectbuilder = Json.createObjectBuilder();

		for (Les asd : deLessen) {

			System.out.println(asd.getDocent());
			if (asd.getDocent().equals(leraar)) {
				System.out.println(asd.getDocent() == leraar);
				objectbuilder.add("date", asd.getDatum()).add("start_time", asd.getVan()).add("end_time", asd.getTot())
						.add("name", asd.getVak());

				builder.add(objectbuilder);

				System.out.print(asd.getDocent());
				System.out.println(leraar);
			}
		}

		String JSONString = builder.build().toString();
		System.out.println(JSONString);
		return JSONString;

	}

	public String getRooster(String klas, String datum) throws IOException {

		JsonArrayBuilder builder = Json.createArrayBuilder();
		JsonObjectBuilder objectbuilder = Json.createObjectBuilder();

		for (Les les : deLessen) {
			if (les.getDatum().equals(datum) && les.getKlas().contains(klas)) {
				System.out.println("vandaag les");
				objectbuilder.add("date", les.getDatum()).add("start_time", les.getVan()).add("end_time", les.getTot())
						.add("name", les.getVak()).add("docent", les.getDocent());

				builder.add(objectbuilder);
				System.out.print(les.getDocent());
			}
		}

		String JSONString = builder.build().toString();
		System.out.println(JSONString);
		return JSONString;

	}

	public String getPresentieP(int studentenNr) {
		JsonArrayBuilder builder = Json.createArrayBuilder();
		JsonObjectBuilder objectbuilder = Json.createObjectBuilder();

		Student student = getStudent(studentenNr);
		double totaalPresentie = 0;
		double totaalAanwezig = 0;
		for (Presentie presentie : dePresentie) {
			boolean attendance = false;
			if (presentie.getStudentnummer() == student.getStudentNummer()) {
				totaalPresentie += 1;
				attendance = presentie.getPresentie();
				if (attendance == true) {
					totaalAanwezig += 1;
				}

			}

		}
		String percentageAanwezig;
		if (totaalPresentie == 0) {
			percentageAanwezig = "0";
		} else {
			percentageAanwezig = totaalAanwezig / totaalPresentie * 100 + "";
		}

		objectbuilder.add("id", student.getStudentNummer()).add("firstName", student.getVoornaam())
				.add("prefix", student.getTussenVoegsel()).add("lastName", student.getAchternaam())
				.add("percentageAanwezig", percentageAanwezig);

		builder.add(objectbuilder);

		String JSONString = builder.build().toString();
		return JSONString;
	}

	public String getPresentieC(String klas) {
		System.out.println("AS");
		JsonArrayBuilder builder = Json.createArrayBuilder();
		JsonObjectBuilder objectbuilder = Json.createObjectBuilder();
		Klas klas2 = getKlas(klas);
		System.out.println("AS");

		double totaalPresentie = 0;
		double totaalAanwezig = 0;
		double auiPresentie = 0;
		double auiAanwezig = 0;
		double gpPresentie = 0;
		double gpAanwezig = 0;
		double oodcPresentie = 0;
		double oodcAanwezig = 0;
		System.out.println("AS");
		int i = 1;
		for (Presentie presentie : dePresentie) {
			System.out.println("AS");

			boolean attendance = false;
			if (i == 1) {
				System.out.println("AS");

				if (presentie.getLes().equals("TCIF-V1AUI-17_2017")) {
					System.out.println("AS");

					totaalPresentie += 1;
					auiPresentie += 1;
					attendance = presentie.getPresentie();
					if (attendance == true) {
						totaalAanwezig += 1;
						auiAanwezig += 1;
					}
				} else if (presentie.getLes().equals("TICT-V1GP-15_2017")) {
					totaalPresentie += 1;
					gpPresentie += 1;
					attendance = presentie.getPresentie();
					if (attendance == true) {
						totaalAanwezig += 1;
						gpAanwezig += 1;
					}
				} else if (presentie.getLes().equals("TICT-V1OODC-15_2017")) {
					totaalPresentie += 1;
					oodcPresentie += 1;
					attendance = presentie.getPresentie();
					if (attendance == true) {
						totaalAanwezig += 1;
						oodcAanwezig += 1;
					}
				}
			}
		}
		System.out.println(auiAanwezig + " aui aanwezig");
		System.out.println(auiPresentie + "aui presentie");

		String percentageAanwezig;
		String percentageAanwezigAui;
		String percentageAanwezigGp;
		String percentageAanwezigOodc;
		if (totaalPresentie == 0) {
			percentageAanwezig = "0";
		} else {
			percentageAanwezig = totaalAanwezig / totaalPresentie * 100 + "";
		}

		// Percentages per vak
		if (auiPresentie == 0) {
			percentageAanwezigAui = "0";
		} else {
			percentageAanwezigAui = auiAanwezig / auiPresentie * 100 + "";
		}

		if (gpPresentie == 0) {
			percentageAanwezigGp = "0";
		} else {
			percentageAanwezigGp = gpAanwezig / gpPresentie * 100 + "";
		}

		if (oodcPresentie == 0) {
			percentageAanwezigOodc = "0";
		} else {
			percentageAanwezigOodc = oodcAanwezig / oodcPresentie * 100 + "";
		}

		objectbuilder.add("aui_percentage", percentageAanwezigAui).add("gp_percentage", percentageAanwezigGp)
				.add("oodc_percentage", percentageAanwezigOodc);

		builder.add(objectbuilder);
		String JSONString = builder.build().toString();
		System.out.println(JSONString);
		return JSONString;
	}

	public String getStudents(String bestandsnaam, String les, String datum, int keuze) throws IOException {
		vulPresentie(dePresentie);
		FileReader fileReader = new FileReader("././CSV/" + bestandsnaam + ".csv");
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		ArrayList<Student> lijst = new ArrayList<Student>();
		String regel;

		while ((regel = bufferedReader.readLine()) != null) {

			String[] regels = regel.split(",");

			Student student = new Student(regels[3], regels[2], regels[1], "geheim", "asdasdad",
					Integer.parseInt(regels[0]));
			lijst.add(student);

		}
		bufferedReader.close();

		JsonArrayBuilder builder = Json.createArrayBuilder();
		JsonObjectBuilder objectbuilder = Json.createObjectBuilder();

		for (Student student : lijst) {
			boolean attendance = false;

			for (Presentie presentie : dePresentie) {

				if (presentie.getStudentnummer() == student.getStudentNummer() && presentie.getLes().equals(les)
						&& presentie.getDatum().equals(datum)) {

					if (presentie.getPresentie() == true) {
						attendance = true;
					} else {
						attendance = false;
					}
				}

			}

			objectbuilder.add("id", student.getStudentNummer()).add("firstName", student.getVoornaam())
					.add("prefix", student.getTussenVoegsel()).add("lastName", student.getAchternaam())
					.add("attendance", attendance);

			builder.add(objectbuilder);

		}

		String JSONString = builder.build().toString();

		return JSONString;

	}

	public Student getStudent(String pGebruikersnaam) {
		// used
		Student lGevondenStudent = null;

		for (Student lStudent : deStudenten) {
			if (lStudent.getGebruikersnaam().equals(pGebruikersnaam)) {
				lGevondenStudent = lStudent;
				break;
			}
		}

		return lGevondenStudent;
	}

	public Student getStudent(int pStudentNummer) {
		// used
		Student lGevondenStudent = null;

		for (Student lStudent : deStudenten) {
			if (lStudent.getStudentNummer() == (pStudentNummer)) {
				lGevondenStudent = lStudent;
				break;
			}
		}

		return lGevondenStudent;
	}

	public String login(String gebruikersnaam, String wachtwoord) {
		for (Docent d : deDocenten) {
			if (d.getGebruikersnaam().equals(gebruikersnaam)) {
				if (d.komtWachtwoordOvereen(wachtwoord)) {
					return "docent";
				}
			}
		}

		for (Student s : deStudenten) {
			if (s.getGebruikersnaam().equals(gebruikersnaam)) {
				if (s.komtWachtwoordOvereen(wachtwoord)) {
					return "student";
				}
			}
		}

		return "undefined";
	}

	private void vulDocenten(ArrayList<Docent> pDocenten) {
		String csvFile = "././CSV/docenten.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] element = line.split(cvsSplitBy);
				String gebruikersnaam = element[0].toLowerCase();
				String voornaam = element[1];
				String tussenvoegsel = element[2];
				String achternaam = element[3];
				pDocenten.add(new Docent(voornaam, tussenvoegsel, achternaam, "geheim", gebruikersnaam, 1));

				// System.out.println(gebruikersnaam);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void vulLessen(ArrayList<Les> pLessen) {
		String csvFile = "././CSV/rooster.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] element = line.split(cvsSplitBy);
				String datum = element[0];
				String van = element[1];
				String tot = element[2];
				String vak = element[3];
				String docent = element[4];
				String lokaal = element[5];
				String klasCode = element[6];
				pLessen.add(new Les(datum, van, tot, vak, docent, lokaal, klasCode));

			}
			// System.out.println(pLessen);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void vulPresentie(ArrayList<Presentie> pPresentie) {
		String csvFile = "presentie.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] element = line.split(cvsSplitBy);
				int studentennummer = Integer.parseInt(element[0]);
				String datum = element[1];
				String startTijd = element[2];
				String eindTijd = element[3];
				boolean presentie = Boolean.parseBoolean(element[4]);
				String les = element[5];
				String klascode = element[6];

				pPresentie.add(new Presentie(studentennummer, datum, startTijd, eindTijd, presentie, les, klascode));

			}
			// System.out.println(pLessen);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void vulKlassen(ArrayList<Klas> pKlassen) {
		// TICT-SIE-VIA is de klascode die ook in de rooster file voorkomt
		// V1A is de naam van de klas die ook als file naam voor de studenten van die
		// klas wordt gebruikt
		// Klas k1 = new Klas("TICT-SIE-V1A", "V1A");
		Klas k2 = new Klas("TICT-SIE-V1B", "V1B");
		Klas k3 = new Klas("TICT-SIE-V1C", "V1C");
		Klas k4 = new Klas("TICT-SIE-V1D", "V1D");
		Klas k5 = new Klas("TICT-SIE-V1E", "V1E");
		Klas k6 = new Klas("TICT-SIE-V1F", "V1F");

		// pKlassen.add(k1);
		pKlassen.add(k2);
		pKlassen.add(k3);
		pKlassen.add(k4);
		pKlassen.add(k5);
		pKlassen.add(k6);
	}

	private void vulStudenten(ArrayList<Student> pStudenten, ArrayList<Klas> pKlassen) {
		Student lStudent;
		for (Klas k : pKlassen) {
			String csvFile = "././CSV/" + k.getNaam() + ".csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";

			try {

				br = new BufferedReader(new FileReader(csvFile));

				while ((line = br.readLine()) != null) {
					// line = line.replace(",,", ", ,");
					// use comma as separator
					String[] element = line.split(cvsSplitBy);
					String gebruikersnaam = (element[3] + "." + element[2] + element[1] + "@student.hu.nl")
							.toLowerCase();
					// verwijder spaties tussen dubbele voornamen en tussen bv van der
					gebruikersnaam = gebruikersnaam.replace(" ", "");
					String lStudentNrString = element[0];
					int lStudentNr = Integer.parseInt(lStudentNrString);
					lStudent = new Student(element[3], element[2], element[1], "geheim", gebruikersnaam, lStudentNr); // Volgorde
																														// 3-2-1
																														// =
																														// voornaam,
																														// tussenvoegsel
																														// en
																														// achternaam
					pStudenten.add(lStudent);
					k.voegStudentToe(lStudent);

					// System.out.println(gebruikersnaam);

				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

}