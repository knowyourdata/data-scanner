package at.fwd.data_scanner.config;

import java.util.HashMap;
import java.util.Map;

public class MessagesConfig {

	String sheetOverview;
	String sheetDetails;
	String sheetDataDictionary;
	
	String application;
	String tablename; 
	String columnname;
	String datacategory;
	String datacategories;
	
	String columns;
	
	String sensitivityLevel;
	String maximumSensitivityLevel;
	
	String typeName;
	String columnSize;
	String remarks;
	
	String jsonHeadlineRoot;
	
	Map<String, String> datacategoryLabels = new HashMap<String, String>();
	
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public String getDatacategory() {
		return datacategory;
	}
	public void setDatacategory(String datacategory) {
		this.datacategory = datacategory;
	}
	public String getSheetOverview() {
		return sheetOverview;
	}
	public void setSheetOverview(String sheetOverview) {
		this.sheetOverview = sheetOverview;
	}
	public String getSheetDetails() {
		return sheetDetails;
	}
	public void setSheetDetails(String sheetDetails) {
		this.sheetDetails = sheetDetails;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	
	public String getSensitivityLevel() {
		return sensitivityLevel;
	}
	public void setSensitivityLevel(String sensitivityLevel) {
		this.sensitivityLevel = sensitivityLevel;
	}
	public String getMaximumSensitivityLevel() {
		return maximumSensitivityLevel;
	}
	public void setMaximumSensitivityLevel(String maximumSensitivityLevel) {
		this.maximumSensitivityLevel = maximumSensitivityLevel;
	}
	public String getDatacategories() {
		return datacategories;
	}
	public void setDatacategories(String datacategories) {
		this.datacategories = datacategories;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(String columnSize) {
		this.columnSize = columnSize;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSheetDataDictionary() {
		return sheetDataDictionary;
	}
	public void setSheetDataDictionary(String sheetDataDictionary) {
		this.sheetDataDictionary = sheetDataDictionary;
	}
	public String getJsonHeadlineRoot() {
		return jsonHeadlineRoot;
	}
	public void setJsonHeadlineRoot(String jsonHeadlineRoot) {
		this.jsonHeadlineRoot = jsonHeadlineRoot;
	}
	public Map<String, String> getDatacategoryLabels() {
		return datacategoryLabels;
	}
	public void setDatacategoryLabels(Map<String, String> datacategoryLabels) {
		this.datacategoryLabels = datacategoryLabels;
	}
	
	

	
}
