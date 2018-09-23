package at.fwd.data_scanner.service.output;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import at.fwd.data_scanner.config.MessagesConfig;
import at.fwd.data_scanner.dto.DataCategoryDTO;
import at.fwd.data_scanner.dto.MappedColumn;
import at.fwd.data_scanner.dto.MappedTableDTO;
import at.fwd.data_scanner.util.DatacategoryUtil;
import at.fwd.data_scanner.util.sort.DynaComparator;
import at.fwd.data_scanner.util.sort.SortProperty;

public class JsonOutputService {

	private static final Logger log = Logger.getLogger(JsonOutputService.class);
	
	@SuppressWarnings("unchecked")
	public void writeOutput(Map<String, Integer> datacategorySensitivityMap, MessagesConfig configHeadline, List<MappedTableDTO> mappedTableDTOList, String filepath) {
		
		JsonObject obj = new JsonObject();
        obj.addProperty("name", configHeadline.getJsonHeadlineRoot());
        obj.addProperty("root", "true");

        List<DataCategoryDTO> datacategoryDTOList = new ArrayList<DataCategoryDTO>();
        for (String datacategory : datacategorySensitivityMap.keySet()) {
        	DataCategoryDTO dataCategoryDTO = new DataCategoryDTO();
        	dataCategoryDTO.setDatacategory(datacategory);
        	dataCategoryDTO.setSensitivityLevel(datacategorySensitivityMap.get(datacategory));
        	datacategoryDTOList.add(dataCategoryDTO);
        }
        
        DynaComparator dyna = new DynaComparator();
        SortProperty sortProperty = new SortProperty();
        sortProperty.setAttribute("sensitivityLevel");
        sortProperty.setMode(SortProperty.DESCENDING);
        dyna.addSortProperty(sortProperty);
        SortProperty sortProperty2 = new SortProperty();
        sortProperty2.setAttribute("datacategory");
        sortProperty2.setMode(SortProperty.ASCENDING);
        dyna.addSortProperty(sortProperty2);
        
        Collections.sort(datacategoryDTOList, dyna);
        
        JsonArray datacategoryList = new JsonArray();
        for (DataCategoryDTO datacategoryDTO : datacategoryDTOList) {
        	String datacategory = datacategoryDTO.getDatacategory();
        	
        	JsonObject datacategoryObj = new JsonObject();
        	datacategoryObj.addProperty("name", DatacategoryUtil.getLabelForDatacategory(configHeadline, datacategory));
        	datacategoryObj.addProperty("size", 1);
        	datacategoryObj.addProperty("sensitivityLevel", datacategoryDTO.getSensitivityLevel());
        	
        	Map<String, Map<String, MappedTableDTO>> applications = new HashMap<String, Map<String, MappedTableDTO>>();

        	log.debug("datacategory: " + datacategory);
        	log.debug("*** reading data" );
        	for (MappedTableDTO mappedTableDTO : mappedTableDTOList) {
        		
        		for (MappedColumn mappedColumn: mappedTableDTO.getColumnList()) {
        			
        			if (mappedColumn.getClassification().equals(datacategory)) {
        				
        				Map<String, MappedTableDTO> tableMap = applications.get(mappedColumn.getApplication());
        				
        				if (tableMap==null) {
        					tableMap = new HashMap<String, MappedTableDTO>() ;
        				}
        				
        				log.debug("  table: " + mappedTableDTO.getTableDTO().getTablename() + " column: " + mappedColumn.getColumnDTO().getColumnname());
        				
        				MappedTableDTO filteredMappedTableDTO = createFilteredMappedTableDTO(mappedTableDTO, tableMap);
        				
        				filteredMappedTableDTO.getColumnList().add(mappedColumn);
        				
        				
        				tableMap.put(filteredMappedTableDTO.getTableDTO().getTablename(), filteredMappedTableDTO);
        				
            			applications.put(filteredMappedTableDTO.getApplication(), tableMap);
            			
            		
            			
        		}
        		
        		}
        		
        		
        		
        	}
    
        	log.debug("*** writing data" );
        	JsonArray list = new JsonArray();
        	
        	for (String application : applications.keySet()) {
        		JsonObject applicationObj = new JsonObject();
        		log.debug("application: " + application);
            	
        		applicationObj.addProperty("name",  application);
        		applicationObj.addProperty("subnode", true);
        		applicationObj.addProperty("size", 1);
        		
        		Map<String, MappedTableDTO> tableMap = applications.get(application);
        		
        		JsonArray tablelist = new JsonArray();
        		for (String tablename : tableMap.keySet()) {
        			JsonObject tableObj = addTable(tablename);
                	
        			JsonArray columnlist = new JsonArray();
        			
        			MappedTableDTO mappedTableDTO = tableMap.get(tablename);
        			
        			List<MappedColumn> mappedColumnList = mappedTableDTO.getColumnList();
        			
        			for (MappedColumn mappedColumn : mappedColumnList) {
        				JsonObject columnObj = new JsonObject();
        				log.debug("    column: " + mappedColumn.getColumnDTO().getColumnname());
        				
    					columnObj.addProperty("name",  mappedColumn.getColumnDTO().getColumnname());
        				columnObj.addProperty("size", 1);
        				columnObj.addProperty("subnode", true);
        				columnlist.add(columnObj);	
    				
        				
        			}
        			
        			tableObj.add("children", columnlist);
        			
        			tablelist.add(tableObj);
        		}
        		
        		applicationObj.add("children", tablelist);
        		
        		list.add(applicationObj);
        	}
   	
        	
        	datacategoryObj.add("children", list);

        	datacategoryList.add(datacategoryObj);
        }
        
        obj.add("children", datacategoryList);
        
        try (
        	FileWriter file = new FileWriter(filepath)) {

        	Gson gson = new Gson();
            file.write(gson.toJson(obj));
            file.flush();
            
            System.out.println("Json by applications file written to " + filepath);

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.debug(obj);
		

	}


	protected MappedTableDTO createFilteredMappedTableDTO(MappedTableDTO mappedTableDTO,
			Map<String, MappedTableDTO> tableMap) {
		MappedTableDTO filteredMappedTableDTO = tableMap.get(mappedTableDTO.getTableDTO().getTablename());
		
		if (filteredMappedTableDTO==null) {
			filteredMappedTableDTO = new MappedTableDTO();
			filteredMappedTableDTO.setApplication(mappedTableDTO.getApplication());
			filteredMappedTableDTO.setMaxSensitivityLevel(mappedTableDTO.getMaxSensitivityLevel());
			filteredMappedTableDTO.setTableDTO(mappedTableDTO.getTableDTO());
		}
		return filteredMappedTableDTO;
	}
	

	public void writeOutputForApplications(Map<String, Integer> datacategorySensitivityMap, MessagesConfig configHeadline, List<MappedTableDTO> mappedTableDTOList, String filepath) {
		
		JsonObject obj = new JsonObject();
        obj.addProperty("name", configHeadline.getJsonHeadlineRoot());
        obj.addProperty("root", "true");

        List<String> applicationList = new ArrayList<>();
        for (MappedTableDTO mappedTableDTO : mappedTableDTOList) {
        	if (!applicationList.contains(mappedTableDTO.getApplication())) {
        		applicationList.add(mappedTableDTO.getApplication());
        	}
        }
        
        JsonArray applicationJsonArray = new JsonArray();
        for (String application : applicationList) {
        	JsonObject applicationObj = new JsonObject();
        	applicationObj.addProperty("name", application);
        	applicationObj.addProperty("application", true);
        	  
            JsonArray datacategoryList = new JsonArray();
            for (String datacategory : datacategorySensitivityMap.keySet()) {
            	JsonObject datacategoryObj = new JsonObject();
            	datacategoryObj.addProperty("name", DatacategoryUtil.getLabelForDatacategory(configHeadline, datacategory));
            	
            	Integer datacategorySensitivityLevel = datacategorySensitivityMap.get(datacategory);
            	datacategoryObj.addProperty("sensitivityLevel", datacategorySensitivityLevel);
            	
    			Map<String, MappedTableDTO> filteredTableMap = filterTableMap(mappedTableDTOList, application,
						datacategory);
        
            	if (filteredTableMap.size()>0) {
	            	log.debug("*** writing data" );
	            	
	            	
	        		JsonArray tablelist = new JsonArray();
	        		for (String tablename : filteredTableMap.keySet()) {
	        			JsonObject tableObj = addTable(tablename);
	                	
	        			JsonArray columnlist = new JsonArray();
	        			
	        			MappedTableDTO mappedTableDTO = addColumns(filteredTableMap, tablename, columnlist);
	        			
	        			log.debug("tablename: " +tablename);
	        			log.debug("datacategory: " + datacategory);
	        			log.debug("datacategorySensitivityLevel: " +datacategorySensitivityLevel);
	        			log.debug("mappedTableDTO.getMaxSensitivityLevel(): " +mappedTableDTO.getMaxSensitivityLevel());
	        			
	        			addForeignKeyReferences(datacategorySensitivityLevel, columnlist, mappedTableDTO);
	        			
	        			tableObj.add("children", columnlist);
	        			
	        			tablelist.add(tableObj);
        				
	        			
	        		}
	            		
	            	datacategoryObj.add("children", tablelist);
	
	            	datacategoryList.add(datacategoryObj);
	            	
            	}
            }
            
            applicationObj.add("children", datacategoryList);
            
            applicationJsonArray.add(applicationObj);
        }
      
        obj.add("children", applicationJsonArray);
        
        try (
        	
        	FileWriter file = new FileWriter(filepath)) {

        	Gson gson = new Gson();
            file.write(gson.toJson(obj));
            file.flush();
            
            System.out.println("Json by datacategories file written to " + filepath);

        } catch (IOException e) {
            e.printStackTrace();
        }

	}


	private JsonObject addTable(String tablename) {
		JsonObject tableObj = new JsonObject();
		tableObj.addProperty("name",  tablename);
		tableObj.addProperty("subnode", true);
		//tableObj.addProperty("size", 500);
		log.debug("  tablename: " + tablename);
		return tableObj;
	}


	private MappedTableDTO addColumns(Map<String, MappedTableDTO> filteredTableMap, String tablename,
			JsonArray columnlist) {
		MappedTableDTO mappedTableDTO = filteredTableMap.get(tablename);
		
		for (MappedColumn mappedColumn : mappedTableDTO.getColumnList()) {
			JsonObject columnObj = new JsonObject();
			
			String columnname = mappedColumn.getColumnDTO().getColumnname();
			
			log.debug("    column: " + columnname);
			
			columnObj.addProperty("name",  columnname);
			columnObj.addProperty("size", 1);
			columnObj.addProperty("subnode", true);
			
			// add data matches
			addDataMatches(mappedColumn, columnObj);
			
			columnlist.add(columnObj);
			
		}
		return mappedTableDTO;
	}


	private Map<String, MappedTableDTO> filterTableMap(List<MappedTableDTO> mappedTableDTOList, String application,
			String datacategory) {
		Map<String, MappedTableDTO> filteredTableMap = new HashMap<String, MappedTableDTO>();
		
		log.debug("datacategory: " + datacategory);
		log.debug("*** reading data" );
		for (MappedTableDTO mappedTableDTO : mappedTableDTOList) {
			
			if (mappedTableDTO.getApplication().equals(application)) {
				
				for (MappedColumn mappedColumn : mappedTableDTO.getColumnList()) {
					if(mappedColumn.getClassification().equals(datacategory)) {
						MappedTableDTO filteredMappedTableDTO = filteredTableMap.get(mappedColumn.getTablename());
							
						if (filteredMappedTableDTO==null) {
							filteredMappedTableDTO = new MappedTableDTO();
							filteredMappedTableDTO.setApplication(mappedTableDTO.getApplication());
							filteredMappedTableDTO.setMaxSensitivityLevel(mappedTableDTO.getMaxSensitivityLevel());
							filteredMappedTableDTO.setTableDTO(mappedTableDTO.getTableDTO());
						}
		    			
						log.debug("  table: " + mappedColumn.getTablename() + " column: " + mappedColumn.getColumnDTO().getColumnname());
						filteredMappedTableDTO.getColumnList().add(mappedColumn);
						
		    			filteredTableMap.put(mappedColumn.getTablename(), filteredMappedTableDTO);
		    				
						
					}
				}
				
				
			}
			
			
		}
		return filteredTableMap;
	}

	protected void addForeignKeyReferences(Integer datacategorySensitivityLevel, JsonArray columnlist, MappedTableDTO mappedTableDTO) {
		log.debug("no tracking of foreign keys supported");
	}
	
	protected void addDataMatches(MappedColumn mappedTableDTO, JsonObject jsonObject) {
		log.debug("no tracking of data matches supported");
	}

}
