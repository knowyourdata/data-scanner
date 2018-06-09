package at.fwd.file_scanner.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import at.fwd.file_scanner.api.ScanFilePlugin;
import at.fwd.file_scanner.dto.FileMatchDTO;
import at.fwd.file_scanner.util.ScannerUtil;

public class ReadTextPlugin implements ScanFilePlugin {

	private static final Logger log = Logger.getLogger(ReadTextPlugin.class);
	
	public FileMatchDTO readFile(File file, Map<String, Pattern> patternMap, List<String> whitelist,
			PrintWriter pw, Map<String, Integer> dataCategorySensitivityMap) throws IOException {
		FileInputStream input = new FileInputStream(file);
		Integer numberOfMatches = 0;
		Integer sensitivityLevel = 0;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	
		    	//log.debug("line: " + line);
		    	FileMatchDTO dto = ScannerUtil.matchLineContent(file, patternMap, line, whitelist,
						 pw, dataCategorySensitivityMap);
		    	
		    	numberOfMatches += dto.getMatchCount();
		    	
		    	if (dto.getSensitivityLevel()> sensitivityLevel) {
		    		sensitivityLevel = dto.getSensitivityLevel();
		    	}
		    	
		    	//log.info("sensitivityLevel: " + sensitivityLevel);
		    	
		    }
		}
		
		FileMatchDTO fileMatchDTO = new FileMatchDTO();
		fileMatchDTO.setMatchCount(numberOfMatches);
		fileMatchDTO.setSensitivityLevel(sensitivityLevel);
		
		return fileMatchDTO;
	}
	
	
	public List<String> getSupportedFiletypes() {
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("txt");
		fileTypeList.add("sql");
		fileTypeList.add("properties");
		fileTypeList.add("xml");
		fileTypeList.add("json");
		fileTypeList.add("java");
		fileTypeList.add("php");
		fileTypeList.add("html");
		return fileTypeList;
	}
	
	
}

