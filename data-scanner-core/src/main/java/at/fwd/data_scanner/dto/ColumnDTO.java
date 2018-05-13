package at.fwd.data_scanner.dto;

public class ColumnDTO {

	String columnname;
	
	String typename;
	
	String columnsize;
	
	String remarks;
	
	String isNullable;
	
	String isAutoIncrement;

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getColumnsize() {
		return columnsize;
	}

	public void setColumnsize(String columnsize) {
		this.columnsize = columnsize;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getIsAutoIncrement() {
		return isAutoIncrement;
	}

	public void setIsAutoIncrement(String isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	
	
}
