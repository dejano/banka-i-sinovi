package app;

import gui.standard.form.Form;

import java.sql.SQLException;

import meta.StandardFormCreator;

public class App {
	public static void main(String[] args) {
		try {
			Form form = StandardFormCreator.getStandardForm("ucesnici_filma");
			form.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
