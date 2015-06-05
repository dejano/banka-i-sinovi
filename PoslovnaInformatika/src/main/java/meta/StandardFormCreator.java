package meta;

import gui.standard.form.Form;

import java.sql.SQLException;

public class StandardFormCreator {

	public static Form getStandardForm(String formName) throws SQLException{
		FormMetaData fmd = JsonHelper.unmarshall(formName + ".json");
		
		return new Form(fmd);
	}
	
	private StandardFormCreator(){}
}
