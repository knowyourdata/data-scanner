package at.fwd.data_scanner.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import at.fwd.data_scanner.dto.ColumnDTO;
import at.fwd.data_scanner.dto.TableDTO;

public class ScannerDdlService {

	private static final Logger log = Logger.getLogger(ScannerDdlService.class);
	
	private Map<String, String> classificationMap;
	
	public List<TableDTO> checkDatabase(String ddlPath, List<String> excludeTables) throws IOException {
		List<TableDTO> tableList = new ArrayList<TableDTO>();
		
		InputStream input = this.getClass().getResourceAsStream(ddlPath); 
		
		if (input!=null) {
			log.debug("reading DDL");
			tableList = new ArrayList<TableDTO>();
			
			
			try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
			    String line;
			    String tablename = null;
			    List<ColumnDTO> fieldnames = null;
			    while ((line = br.readLine()) != null) {
			        // process the line.
			    	log.debug("line: " + line);
			    	
			    	if (!line.startsWith("#")) {
			    		
			    		if (line.contains("CREATE TABLE")) {
			    			// CREATE TABLE IF NOT EXISTS `...` (
			    			String expr = StringUtils.substringAfter(line, "CREATE TABLE");
			    			log.debug("expr: " + expr);
			    			
			    			if (expr.contains("(")) {
			    				expr = StringUtils.substringBefore(line, "(");
			    				
			    				expr = expr.trim();
			    				
			    				expr = StringUtils.substringAfterLast(expr, " ");
			    				
			    				expr = clearApostrophes(expr);
			    				
			    				log.debug("*** table: " + expr);
			    				tablename = expr;
			    				fieldnames = new ArrayList<ColumnDTO>();
			    			}
			    			
			    		} else {
			    			
			    			if (tablename!=null) {
			    				
			    				boolean tableEnd = false;
			    				if (line.indexOf(")")==0) {
			    					tableEnd = true;
			    				} else {
			    					// CREATE TABLE is open
					    			String expr = line.trim();
					    			
					    			String fieldname = StringUtils.substringBefore(expr, " ");
					    			fieldname = clearApostrophes(fieldname);
					    			
					    			if (!fieldname.equals("PRIMARY") && !fieldname.equals("UNIQUE") && !fieldname.equals("KEY")) {
					    				ColumnDTO columnDTO = new ColumnDTO();
					    				columnDTO.setColumnname(fieldname);
					    				fieldnames.add(columnDTO);
						    			
					    				log.debug("* fieldname: " + fieldname);
						    				
					    			}
					    			
			    				}
			    				

				    			if (tableEnd || line.contains(";")) {
				    				if (tablename!=null) {
				    					TableDTO tableDTO = new TableDTO();
					    				tableDTO.setTablename(tablename);
					    				tableDTO.setColumnList(fieldnames);
					    				tableList.add(tableDTO);
					    
					    				tablename = null;
				    				}
				    				
				    			}
				    			
				    			
				    		}
			    			
			    			
			    		}
			    		
			    		
			    		
			    	}
			    	
			    }
			}
			
			
		} else {
			log.warn("DDL not found: " + ddlPath);
		}
		
		return tableList;
	}

	private String clearApostrophes(String expr) {
		expr = StringUtils.replace(expr, "`", "");
		return expr;
	}

	public Map<String, String> getClassificationMap() {
		return classificationMap;
	}

	public void setClassificationMap(Map<String, String> classificationMap) {
		this.classificationMap = classificationMap;
	}
		
		
}
