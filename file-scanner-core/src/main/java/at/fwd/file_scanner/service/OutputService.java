package at.fwd.file_scanner.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import at.fwd.file_scanner.dto.ConfigDTO;
import at.fwd.file_scanner.dto.FileMatchDTO;
import at.fwd.file_scanner.dto.NodeFileMatchDTO;

public class OutputService {
	
	private static final Logger log = Logger.getLogger(OutputService.class);
	
	private ConfigDTO configDTO;
	
	public void convertIntoTree(Map<String, FileMatchDTO> numberOfMatchesPerFile) {
		NodeFileMatchDTO root = new NodeFileMatchDTO();
		int level = 0;
		for (String filepath : numberOfMatchesPerFile.keySet()) {
			FileMatchDTO fileMatchDTO = numberOfMatchesPerFile.get(filepath);
		
			int matches = fileMatchDTO.getMatchCount();
			
			root.addNodeMatch(filepath, matches, level, fileMatchDTO.getSensitivityLevel());
			
		}

		
		printLogfile(root);
		
		printJson(root);
		
	}


	private void printLogfile(NodeFileMatchDTO root) {
		log.info("printing to log");
		FileOutputStream fout;
		try {
			String filepath = configDTO.getOutputPath() + File.separator + configDTO.getFilesTreeTextFilename();
        	log.info("output path: " + filepath);
			
			fout = new FileOutputStream(filepath);
			PrintWriter pw = new PrintWriter(fout);
			root.printNodes(pw);
			pw.close();
			fout.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void printJson(NodeFileMatchDTO root) {
		log.info("printing to json");
		JsonObject obj = new JsonObject();
        obj.addProperty("name", "root");
        obj.addProperty("root", "true");

        root.printJson(obj);
        
        try {
        	String filepath = configDTO.getOutputPath() + File.separator + configDTO.getFilesTreeJsonFilename();
        	log.info("output path: " + filepath);	
        		
        	FileWriter file = new FileWriter(filepath);

        	Gson gson = new Gson();
            file.write(gson.toJson(obj));
            file.flush();
            
            file.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}


	public ConfigDTO getConfigDTO() {
		return configDTO;
	}


	public void setConfigDTO(ConfigDTO configDTO) {
		this.configDTO = configDTO;
	}

	
}
