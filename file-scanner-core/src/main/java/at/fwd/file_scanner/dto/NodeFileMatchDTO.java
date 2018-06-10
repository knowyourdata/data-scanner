package at.fwd.file_scanner.dto;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import at.fwd.file_scanner.service.OutputService;

public class NodeFileMatchDTO {

	private static final Logger log = Logger.getLogger(NodeFileMatchDTO.class);
	
	int sensitivityLevel; 
	
	int matchCount = 1;
	
	int level = 0;
	
	Map<String, NodeFileMatchDTO> map = new HashMap<String, NodeFileMatchDTO>();
	
	public void addNodeMatch(String concatenatedKey, Integer matchCount, Integer currentLevel, Integer sensitivityLevel) {
		
		
		String key = concatenatedKey;
		String restOfKey = null;
		if (concatenatedKey.contains("\\")) {
			key = StringUtils.substringBefore(concatenatedKey, "\\");
			restOfKey = StringUtils.substringAfter(concatenatedKey, "\\");
			
		}
		
		log.debug("processing key: " + key);
		log.debug("level: " + level);
		log.debug("sensitivityLevel: " + sensitivityLevel);
		
		NodeFileMatchDTO dto = map.get(key);
		if (dto==null) {
			dto = new NodeFileMatchDTO();
			dto.setMatchCount(matchCount);
			dto.setLevel(currentLevel);
			dto.setSensitivityLevel(sensitivityLevel);
			map.put(key, dto);
		} else {
			dto.setMatchCount(dto.getMatchCount() + matchCount);
			
			if (sensitivityLevel > dto.getSensitivityLevel()) {
				dto.setSensitivityLevel(sensitivityLevel);
			}
		}
		
		log.info("dto.sensitivityLevel: " + dto.getSensitivityLevel());
		
		
		if (restOfKey!=null) {

			Integer newLevel = currentLevel + 1;
			dto.addNodeMatch(restOfKey, matchCount, newLevel, sensitivityLevel);
		}
		
		
	}
	
	public void printNodes(PrintWriter pw) {
		
		for (String key : map.keySet()) {
			NodeFileMatchDTO dto = map.get(key);
			
			String line = StringUtils.repeat("  ", dto.getLevel()) + key + ": " + dto.getMatchCount() + " / sens.level: " + dto.getSensitivityLevel();
			if (pw!=null) {
				pw.println(line);
			} else {
				System.out.println(line);	
			}
			
			
			dto.printNodes(pw);
		}
		
	}
	
	public void printJson(JsonObject obj) {
		 JsonArray list = new JsonArray();
		 
	     for (String key : map.keySet()) {
			NodeFileMatchDTO dto = map.get(key);
			
			JsonObject dtoObj = new JsonObject();
			dtoObj.addProperty("name", key);
			dtoObj.addProperty("size", dto.getMatchCount());
			dtoObj.addProperty("level", dto.getLevel());
			dtoObj.addProperty("sensitivityLevel", dto.getSensitivityLevel());
        	
			dto.printJson(dtoObj);
			
        	list.add(dtoObj);
	     }
	     
	     obj.add("children", list);
	}

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public Map<String, NodeFileMatchDTO> getMap() {
		return map;
	}

	public void setMap(Map<String, NodeFileMatchDTO> map) {
		this.map = map;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSensitivityLevel() {
		return sensitivityLevel;
	}

	public void setSensitivityLevel(int sensitivityLevel) {
		this.sensitivityLevel = sensitivityLevel;
	}
	
	
	
}
