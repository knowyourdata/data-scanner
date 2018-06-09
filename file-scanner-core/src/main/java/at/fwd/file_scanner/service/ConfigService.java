package at.fwd.file_scanner.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import at.fwd.file_scanner.dto.ConfigDTO;

public class ConfigService {

	private static final Logger log = Logger.getLogger(ConfigService.class);
	
	public ConfigDTO readConfig() throws IOException {
		ConfigDTO dto = new ConfigDTO();
		
		String whitelistPath = "/config.properties";
		InputStream input = this.getClass().getResourceAsStream(whitelistPath);
		Properties props = new Properties();
		props.load(input);
		
		// processing
		dto.setMaxdepth(Integer.valueOf(props.getProperty("maxdepth")));
		dto.setMaxFileSizeBytes(Long.valueOf(props.getProperty("maxFileSizeBytes")));
		
		// output
		dto.setOutputPath(props.getProperty("outputPath"));
		dto.setFilesTreeTextFilename(props.getProperty("filesTreeTextFilename"));
		dto.setFilesTreeJsonFilename(props.getProperty("filesTreeJsonFilename"));
		dto.setLogFilename(props.getProperty("logFilename"));

		return dto;
	}
	
	public List<String> readWhitelist() throws IOException{
		List<String> whitelist = new ArrayList<String>();
		
		log.info("reading whitelist");
		String whitelistPath = "/whitelist.properties";
		InputStream input = this.getClass().getResourceAsStream(whitelistPath);
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	
		    	if (line.length()>0 ) {
		    		if (!line.startsWith("#")) {
			    		
			    		whitelist.add(line);
			    		
			    	}
			    		
		    	}
		    	
		    }
		}
		
		log.info("whitelist: " + whitelist.size());
		
		return whitelist;
	}
	
	public List<String> readExcludePathList() throws IOException{
		List<String> excludePathList = new ArrayList<String>();
		
		log.info("reading excludePathList");
		String whitelistPath = "/exclude_paths.properties";
		InputStream input = this.getClass().getResourceAsStream(whitelistPath);
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	
		    	if (line.length()>0 ) {
		    		if (!line.startsWith("#")) {
			    		
		    			excludePathList.add(line);
			    		
			    	}
			    		
		    	}
		    	
		    }
		}
		
		log.info("excludePathList: " + excludePathList.size());
		
		return excludePathList;
	}
	public Map<String, Pattern> buildPatternMap () throws IOException{
		Properties props = new Properties();
		InputStream input = this.getClass().getResourceAsStream("/data_regex.properties");
		props.load(input);
		
		Map<String, Pattern> patternMap = new HashMap<String, Pattern>();
		
		// build Map of Patterns
		for (Object key : props.keySet()) {
			String keyStr = (String)key;
			patternMap.put(keyStr, Pattern.compile(props.getProperty(keyStr)));
		}
		
		return patternMap;
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

}
