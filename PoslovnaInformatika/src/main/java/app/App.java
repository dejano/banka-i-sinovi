package app;

import gui.standard.form.StandardForm;

import java.sql.SQLException;
import java.util.Date;

import meta.StandardFormCreator;

public class App {
	public static void main(String[] args) {
		try {
			System.out.println(new Date());
			StandardForm form = StandardFormCreator.getStandardForm("videoteka");
			System.out.println(new Date());
			form.setVisible(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
