package meta;

import gui.standard.form.Form;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class StandardFormCreator {

	public static Form getStandardForm(String formName) throws SQLException{
		FormMetaData fmd = JsonHelper.unmarshall(formName + ".json");
		
		return new Form(fmd);
	}

	public static Form getNextStandardForm(String formName, Map<String,
			String> nextColumnCodeValues) throws SQLException{
		FormMetaData fmd = JsonHelper.unmarshall(formName + ".json");

		return new Form(fmd, nextColumnCodeValues);
	}
	
	private StandardFormCreator(){}
}
