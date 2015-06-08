package app;

import gui.standard.form.Form;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import meta.StandardFormCreator;

public class App {
	public static void main(String[] args) {
		try {
			Form form = StandardFormCreator.getStandardForm("videoteka");
//			Map<String, String> nccv = new HashMap<>();
//			nccv.put("SIFRA_VIDEOTEKE", "1");
//			Form form = StandardFormCreator.getNextStandardForm("film", nccv);
			form.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
