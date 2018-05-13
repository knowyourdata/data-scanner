package at.fwd.data_scanner.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import at.fwd.data_scanner.config.MessagesConfig;

public class ConfigService {

	public Map<String, String> readClassificationMap() throws IOException {
		
		// read packaged classifications 
		InputStream input = this.getClass().getResourceAsStream("/classification-default.properties");
		
		Map<String, String> map = new HashMap<>();
		readClassificationsFromInputStream(input, map);
		
		// read custom classifications if available
		input = this.getClass().getResourceAsStream("/classification.properties");
		if (input!=null) {
			readClassificationsFromInputStream(input, map);
				
		}
		
		return map;
		
	}


	public void loadConfigMessages(Properties messagesProperties) throws IOException {
		InputStream inputMessages = this.getClass().getResourceAsStream("/messages-default.properties");
		InputStreamReader inputMessagesReader = new InputStreamReader(inputMessages, "UTF-8");
		messagesProperties.load(inputMessagesReader);
		
		inputMessages = this.getClass().getResourceAsStream("/messages.properties");
		if (inputMessages!=null) {
			inputMessagesReader = new InputStreamReader(inputMessages, "UTF-8");
			Properties customMessagesProperties = new Properties();
			customMessagesProperties.load(inputMessagesReader);
			for (Object key : customMessagesProperties.keySet()) {
				messagesProperties.put(key, customMessagesProperties.getProperty((String)key));
			}
		}
	}

	
	private void readClassificationsFromInputStream(InputStream input, Map<String, String> map) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	
		    	if (!line.startsWith("#")) {
		    		
		    		if (line.contains("=")) {
		    			
		    			String matcher = StringUtils.substringBefore(line, "=");
		    			matcher = matcher.trim();
		    			String category = StringUtils.substringAfter(line, "=");
		    			category = category.trim();
		    			
		    			map.put(matcher, category);
		    		}
		    		
		    	}
		    	
		    }
		}
	}
	
	public Map<String, Integer> readDataCategorySensitivityMap() throws IOException {
		Map<String, Integer> datacategorySensitivityMap = new HashMap<String, Integer>();
		
		InputStream input = this.getClass().getResourceAsStream("/datacategory_sensitivity-default.properties");
		
		Properties props = new Properties();
		props.load(input);
		
		addSensitivityLevels(datacategorySensitivityMap, props);
		
		input = this.getClass().getResourceAsStream("/datacategory_sensitivity.properties");
		if(input!=null) {
			props = new Properties();
			props.load(input);
			
			addSensitivityLevels(datacategorySensitivityMap, props);
				
		}
		
		return datacategorySensitivityMap;
	}

	private void addSensitivityLevels(Map<String, Integer> datacategorySensitivityMap, Properties props) {
		for (Object key : props.keySet()) {
			Integer sensitivityLevel = Integer.valueOf((String)props.get(key));
			datacategorySensitivityMap.put((String)key, sensitivityLevel);
			
		}
	}
	
	public MessagesConfig readConfigHeadlines(Properties props) {
		
		MessagesConfig configHeadlineDTO = new MessagesConfig();
		
		configHeadlineDTO.setSheetOverview(props.getProperty("sheet.overview"));
		configHeadlineDTO.setSheetDetails(props.getProperty("sheet.detail"));
		configHeadlineDTO.setSheetDataDictionary(props.getProperty("sheet.data_dictionary"));
		
		configHeadlineDTO.setApplication(props.getProperty("headline.application"));
		configHeadlineDTO.setTablename(props.getProperty("headline.tablename"));
		configHeadlineDTO.setColumnname(props.getProperty("headline.columnname"));
		configHeadlineDTO.setDatacategory(props.getProperty("headline.datacategory"));
		configHeadlineDTO.setDatacategories(props.getProperty("headline.datacategories"));
		configHeadlineDTO.setMaximumSensitivityLevel(props.getProperty("headline.sensitivity_level.maximum"));
		configHeadlineDTO.setSensitivityLevel(props.getProperty("headline.sensitivity_level"));
		
		configHeadlineDTO.setColumns(props.getProperty("headline.columns"));
		
		configHeadlineDTO.setTypeName(props.getProperty("headline.typeName"));
		configHeadlineDTO.setColumnSize(props.getProperty("headline.columnSize"));
		configHeadlineDTO.setRemarks(props.getProperty("headline.remarks"));
		configHeadlineDTO.setJsonHeadlineRoot(props.getProperty("json.headline.root"));
		
		for (Object key : props.keySet()) {
			String keyStr = (String)key;
			
			if (keyStr.startsWith("datacategory.")) {
				String datacategoryKey = StringUtils.substringAfter(keyStr, "datacategory.");
				configHeadlineDTO.getDatacategoryLabels().put(datacategoryKey, props.getProperty(keyStr));
			}
			
			
		}
		
		
		return configHeadlineDTO;
	}
	
}
