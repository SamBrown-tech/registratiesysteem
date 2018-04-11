package model.presentie;

public class Presentie {
	
	private int studentnummer;
	private String datum;
	private String startTijd;
	private String eindTijd;
	private boolean presentie;	
	private String les;
	private String klascode;
	
	public Presentie(int studentnummer, String datum, String startTijd, String eindTijd, boolean presentie, String les, String klascode) {
		
		this.studentnummer = studentnummer;
		this.datum = datum;
		this.startTijd = startTijd;
		this.eindTijd = eindTijd;
		this.presentie = presentie;
		this.les = les;
		this.klascode = klascode;		
	}
	
	public int getStudentnummer() {
		return studentnummer;
	}
	
	public void setStudentnummer(int studentnummer) {
		this.studentnummer = studentnummer;
	}
	public String getDatum() {
		return datum;
	}
	public void setDatum(String datum) {
		this.datum = datum;
	}
	
	public String getStartTijd() {
		return startTijd;
	}
	
	public void setStartTijd(String startTijd) {
		this.startTijd = startTijd;
	}
	
	public String getEindTijd() {
		return eindTijd;
	}
	
	public void setEindTijd(String eindTijd) {
		this.eindTijd = eindTijd;
	}
	
	public boolean getPresentie() {
		return presentie;
	}
	
	public void setPresentie(boolean presentie) {
		this.presentie = presentie;
	}
	
	public String getLes() {
		return les;
	}
	
	public void setLes(String les) {
		this.les = les;
	}
	
	public String getKlas() {
		return klascode;
	}
	public void setKlas(String klas) {
		this.klascode = klas;
	}
}