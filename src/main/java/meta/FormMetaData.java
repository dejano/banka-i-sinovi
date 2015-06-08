package meta;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class FormMetaData {

	private String title;
	private String tableName;
	private String tableLabel;

	private List<NextMetaData> nextData = new ArrayList<>();

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

	public List<NextMetaData> getNextData() {
		return nextData;
	}

	public void setNextData(List<NextMetaData> nextData) {
		this.nextData = nextData;
	}
}
