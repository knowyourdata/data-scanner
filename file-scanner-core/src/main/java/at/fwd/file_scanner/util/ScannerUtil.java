package at.fwd.file_scanner.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import at.fwd.file_scanner.dto.FileMatchDTO;

public class ScannerUtil {
	
	private static final Logger log = Logger.getLogger(ScannerUtil.class);

	public static  FileMatchDTO matchLineContent(File file, Map<String, Pattern> patternMap, String line, List<String> whitelist,
			PrintWriter pw, Map<String, Integer> dataCategorySensitivityMap) {
		Integer numberOfMatches = 0;
		Integer sensitivityLevel = 0;
		
		String filename = file.getName();
		String fileAbsolutePath = file.getAbsolutePath();
		
		for (String key : patternMap.keySet()) {
			Pattern pattern = patternMap.get(key);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String matchingValue = matcher.group();
				
				// Prüfen gegen whitelist
				if (!matchAgainstWhitelist(matchingValue, whitelist)) {
					
					
					int matchSensitivityLevel = dataCategorySensitivityMap.get(key);
					
					log.info(filename + ": possible match for " + key + ": " + matchingValue);
					pw.println(fileAbsolutePath + ";" + key + ";" + matchingValue + ";" + matchSensitivityLevel);
					
					
					if (matchSensitivityLevel > sensitivityLevel ) {
						sensitivityLevel = matchSensitivityLevel;
					}
					
					numberOfMatches++;
					
					
				}
				
			}
		}
		
		FileMatchDTO dto = new FileMatchDTO();
		dto.setMatchCount(numberOfMatches);
		dto.setSensitivityLevel(sensitivityLevel);
		
		return dto;
	}
	

	private static boolean matchAgainstWhitelist(String value, List<String> whitelist) {
		
		for (String whitelistEntry : whitelist) {
			//TODO if (value.matches(whitelistEntry)) {
			
			if (value.contains(whitelistEntry)) {
				return true;
			}
		}
		
		return false;
	}
		
}
