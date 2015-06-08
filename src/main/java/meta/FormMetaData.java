package meta;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FormMetaData {

	private String title;
	private String tableName;
	private String tableLabel;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableLabel() {
		return tableLabel;
	}

	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}
}
