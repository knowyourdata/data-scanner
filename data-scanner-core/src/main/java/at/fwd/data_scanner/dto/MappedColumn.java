package at.fwd.data_scanner.dto;

public class MappedColumn {

	String application;
	
	String tablename;
	
	ColumnDTO columnDTO;
	
	String classification;
	

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	

	public ColumnDTO getColumnDTO() {
		return columnDTO;
	}

	public void setColumnDTO(ColumnDTO columnDTO) {
		this.columnDTO = columnDTO;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	
	
	
}
