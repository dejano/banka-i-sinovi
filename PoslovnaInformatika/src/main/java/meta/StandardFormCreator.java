package meta;

import gui.standard.form.StandardForm;

import java.sql.SQLException;

public class StandardFormCreator {

	public static StandardForm getStandardForm(String formName) throws SQLException{
		FormMetaData fmd = JsonHelper.unmarshall(formName + ".json");
		
		return new StandardForm(fmd);
	}
	
	private StandardFormCreator(){}
}
