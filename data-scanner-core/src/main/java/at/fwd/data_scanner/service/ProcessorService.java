package at.fwd.data_scanner.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import at.fwd.data_scanner.constants.DataScannerConstants;
import at.fwd.data_scanner.dto.ColumnDTO;
import at.fwd.data_scanner.dto.MappedColumn;
import at.fwd.data_scanner.dto.MappedTableDTO;
import at.fwd.data_scanner.dto.TableDTO;

public class ProcessorService {
	
	private static final Logger log = Logger.getLogger(ProcessorService.class);
	

	public List<MappedTableDTO> processTables(Map<String, String> classificationMap, Map<String, Integer> datacategorySensitivityMap, String application, List<TableDTO> tables) {
		List<MappedTableDTO> mappedTableDTOList = new ArrayList<>();
		
		for (TableDTO tableDTO : tables) {
			log.debug("table: " + tableDTO.getTablename());
			
			Integer maxSensitivityLevel = -1;
			
			MappedTableDTO mappedTableDTO = new MappedTableDTO();
			
			
			for (ColumnDTO columnDTO : tableDTO.getColumnList()) {
				
				log.debug("   columnname: " + columnDTO.getColumnname());
				MappedColumn mappedColumn = processColumn(application, tableDTO.getTablename(), classificationMap, tableDTO, columnDTO);	
				
				if (mappedColumn!=null) {
					log.debug("** adding mappedColumn: " + tableDTO.getTablename() + ":" + mappedColumn.getColumnDTO().getColumnname());
					Integer sensitivityLevel = datacategorySensitivityMap.get(mappedColumn.getClassification());
					if (sensitivityLevel!=null && sensitivityLevel > maxSensitivityLevel) {
						maxSensitivityLevel = sensitivityLevel;
					}
					
					mappedTableDTO.getColumnList().add(mappedColumn);
				}
				
			}
			
			mappedTableDTO.setMaxSensitivityLevel(maxSensitivityLevel);
			
			mappedTableDTO.setApplication(application);
			mappedTableDTO.setTableDTO(tableDTO);
			mappedTableDTOList.add(mappedTableDTO);
		}
		
		return mappedTableDTOList;
	}

	private MappedColumn processColumn(String application, String tablename, Map<String, String> classificationMap, 
			TableDTO tableDTO,
			ColumnDTO columnDTO) {
		String columnname = columnDTO.getColumnname();
		String matchColumnName = columnname.toLowerCase();
		String classification = classificationMap.get(matchColumnName);
		
		boolean classified = false;
		if (classification!=null) {
			classified = true;
			return appendMappedColumn(application, tablename, tableDTO, columnDTO, classification);
			
		} else {
			// check with tablename
			log.debug("checking for direct match for full table and columnname");
			classification = classificationMap.get(tablename + ":" + matchColumnName);
			
			if (classification!=null) {
				return appendMappedColumn(application, tablename, tableDTO, columnDTO, classification);
			} else {
				// check using matchers
				log.debug("check using matchers");
			
				// do a regex check for expressions
				for (String matcher : classificationMap.keySet()) {
					log.debug("matcher: " + matcher);
					if (matcher.contains("*")) {
						matcher = matcher.toLowerCase();
						
						if (matcher.contains(":")) {
							log.debug("testing tablename:columnname: " + tablename  + ":" + columnname);
							log.debug("testing matcher: " + matcher);
							// table and column name have to match
							boolean tablename_matches = false;
							boolean columnname_matches = false;
							String tablename_matcher = StringUtils.substringBefore(matcher, ":");
							
							if (tablename.matches(tablename_matcher)) {
								tablename_matches = true;
							}
							
							String column_matcher = StringUtils.substringAfter(matcher, ":");
							
							if (matchColumnName.matches(column_matcher)) {
								columnname_matches = true;
							}
							
							if (tablename_matches && columnname_matches) {
								
								classified = true;
								return addMatchedColumn(application, tablename, classificationMap, tableDTO, columnDTO,
										matcher);	
							}
							
						} else {
							
							// simple column match
							if (matchColumnName.matches(matcher)) {
								classified = true;
								return addMatchedColumn(application, tablename, classificationMap, tableDTO, columnDTO,
										matcher);	
							}
							
							
						}
						
						
						
							
					}
					
				}
			}
			
		}
		
		if (!classified) {
			return appendMappedColumn(application, tablename, tableDTO, columnDTO, DataScannerConstants.DATACATEGORY_UNCLASSIFIED);	
		} else {
			return null;
		}
	}


	private MappedColumn addMatchedColumn(String application, String tablename, Map<String, String> classificationMap,
			TableDTO tableDTO, ColumnDTO columnDTO, String matcher) {
		String classification = classificationMap.get(matcher);
		return appendMappedColumn(application, tablename, tableDTO, columnDTO, classification);
		
	}

	private MappedColumn appendMappedColumn(String application, String tablename, TableDTO tableDTO, ColumnDTO columnDTO,
			String classification) {
		if (!classification.equals(DataScannerConstants.DATACATEGORY_UNCLASSIFIED)) {
			log.debug("adding matching table: " + tablename + " column: " + columnDTO.getColumnname() + " classification: " + classification);
				
		}
		
		MappedColumn mappedColumn = new MappedColumn();
		mappedColumn.setApplication(application);
		mappedColumn.setTablename(tablename);
		mappedColumn.setColumnDTO(columnDTO);
		mappedColumn.setClassification(classification);
		return mappedColumn;
	}


}
