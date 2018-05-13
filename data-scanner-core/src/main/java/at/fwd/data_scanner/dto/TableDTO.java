package at.fwd.data_scanner.dto;

import java.util.ArrayList;
import java.util.List;

public class TableDTO {

	String tablename;
	
	String remarks;
	
	List<ColumnDTO> columnList = new ArrayList<ColumnDTO>();
	
	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public List<ColumnDTO> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnDTO> columnList) {
		this.columnList = columnList;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
	
}
