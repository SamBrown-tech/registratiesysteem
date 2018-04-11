package model.les;

import model.persoon.Student;

public class Les {
	private String datum;
	private String van;
	private String tot;
	private String vak;
	private String docent;
	private String lokaal;
	private String klasCode;

	public Les() {
	}

	public Les(String datum, String van, String tot, String vak, String docent, String lokaal, String klasCode) {
		this.datum = datum;
		this.van = van;
		this.tot = tot;
		this.vak = vak;
		this.docent = docent;
		this.lokaal = lokaal;
		this.klasCode = klasCode;
	}

	public String getVak() {
		return vak;
	}

	public String getVan() {
		return van;
	}

	public String getTot() {
		return tot;
	}

	public String getDocent() { // returnt gebruikersnaam van docent
		return docent;
	}

	public String getDatum() {
		return datum;
	}

	public String getKlas() {
		return klasCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj) && obj instanceof Les) {
			Les s = (Les) obj;
			if (this.docent == s.getDocent() && this.klasCode == this.getKlas()) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

}