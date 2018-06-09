package at.fwd.file_scanner.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import at.fwd.file_scanner.api.ScanFilePlugin;

public class ScannerPluginService {

	private static final Logger log = Logger.getLogger(ScannerPluginService.class);
	
	public Map<String, ScanFilePlugin> readPlugins( ) throws IOException{
		
		log.info("loading default plugins");
		String readPluginPath = "/read_plugins-default.properties";
		InputStream input = this.getClass().getResourceAsStream(readPluginPath);
		
		Map<String, ScanFilePlugin> pluginMap = new HashMap<String, ScanFilePlugin>();
		
		registerPlugins(input, pluginMap);
		log.info("pluginMap.filestypes: " + pluginMap.keySet());
		
		log.info("loading custom plugins");
		readPluginPath = "/read_plugins.properties";
		input = this.getClass().getResourceAsStream(readPluginPath);
		if (input!=null) {
			registerPlugins(input, pluginMap);
				
		}
		
		return pluginMap;
	}


	private void registerPlugins(InputStream input, Map<String, ScanFilePlugin> pluginMap) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       // process the line.
		    	
		    	String lineTrimmed = line.trim();
		    	
		    	if (lineTrimmed.length()>0 ) {
		    		if (!lineTrimmed.startsWith("#")) {
			    		
		    			registerPlugin(pluginMap, lineTrimmed);
			    		
			    	}
			    		
		    	}
		    	
		    }
		}
	}


	private void registerPlugin(Map<String, ScanFilePlugin> pluginMap, String clazzname) {
		log.info("registering plugin for class " + clazzname);
		try {
			Class plugin = Class.forName(clazzname);
			Object pluginInstance = plugin.newInstance();
			log.info("pluginInstance " + pluginInstance);
			
			ScanFilePlugin filePluginInstance;
			
			if (pluginInstance instanceof ScanFilePlugin) {
				filePluginInstance = (ScanFilePlugin)pluginInstance;
				log.info("filePluginInstance " + filePluginInstance + " filetypes: " + filePluginInstance.getSupportedFiletypes());
				
				for (String filetype : filePluginInstance.getSupportedFiletypes()) {
					log.info("registering plugin for filetpype " + filetype);
					pluginMap.put(filetype, filePluginInstance);
				}
			} else {
				log.warn("clazz does not implement ScanFilePlugin");
			}
						
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	
}
