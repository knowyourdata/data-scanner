package at.fwd.data_scanner.dto;

import java.util.ArrayList;
import java.util.List;

public class MappedTableDTO {

	List<MappedColumn> columnList = new ArrayList<MappedColumn>();
	
	Integer maxSensitivityLevel;
	
	TableDTO tableDTO;
	
	String application;

	public List<MappedColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<MappedColumn> columnList) {
		this.columnList = columnList;
	}

	public Integer getMaxSensitivityLevel() {
		return maxSensitivityLevel;
	}

	public void setMaxSensitivityLevel(Integer maxSensitivityLevel) {
		this.maxSensitivityLevel = maxSensitivityLevel;
	}

	public TableDTO getTableDTO() {
		return tableDTO;
	}

	public void setTableDTO(TableDTO tableDTO) {
		this.tableDTO = tableDTO;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}
	
	
	
}
