package at.fwd.file_scanner.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import at.fwd.file_scanner.api.ScanFilePlugin;
import at.fwd.file_scanner.dto.ConfigDTO;
import at.fwd.file_scanner.dto.FileMatchDTO;
import at.fwd.file_scanner.dto.ResultDTO;

public class ScannerFileService {

	private static final Logger log = Logger.getLogger(ScannerFileService.class);

	Map<String, ScanFilePlugin> pluginMap = new HashMap<String, ScanFilePlugin>();;
	
	Map<String, Integer> datacategorySensitivityMap = new HashMap<String, Integer>();
	
	ScannerPluginService scannerPluginService = new ScannerPluginService();
	
	ConfigService configService = new ConfigService();
	
	OutputService outputService = new OutputService();
	
	ConfigDTO configDTO;
	
	ResultDTO resultDTO;
	
	PrintWriter pw;
	
	Long totalFileCount = 0L;
	
	Long totalScannedFilesCount = 0L;
	
	Long totalMatchCount = 0L;

	Map<String, Long> unsupportedFileTypesCountMap = new HashMap<String, Long>();
	
	Long skippedFileCount = 0L;
	
	Map<String, FileMatchDTO> matchesPerFile = new HashMap<String, FileMatchDTO>();

		
	public ResultDTO run(String path) throws IOException {
		
		configDTO = configService.readConfig();
		outputService.setConfigDTO(configDTO);
		configDTO.setWhitelist(configService.readWhitelist());
		configDTO.setExcludePathList(configService.readExcludePathList());
		
		datacategorySensitivityMap = configService.readDataCategorySensitivityMap();
		
		pluginMap = scannerPluginService.readPlugins();
		
		Map<String, Pattern> patternMap = configService.buildPatternMap();
		
		String outputFilepath = configDTO.getOutputPath() + File.separator + configDTO.getLogFilename();
		log.info("outputFilepath: " + outputFilepath);
		pw = new PrintWriter(new FileOutputStream(new File(outputFilepath)));
		
		File filePath = new File(path);
		
		int depth = -1;
		processFile(patternMap, filePath, depth);
		
		log.info("totalFileCount: " + totalFileCount);
		log.info("totalScannedFilesCount: " + totalScannedFilesCount);
		
		log.info("total number of matches: " + totalMatchCount);
		
		log.info("skipped files due to size: " + skippedFileCount);
		
		log.info("unsupported file types: " + unsupportedFileTypesCountMap.size());
		log.info("unsupportedFileTypes: " + unsupportedFileTypesCountMap.keySet());
		Long totalUnsupportedFileCount = 0L;
		for (String filetype : unsupportedFileTypesCountMap.keySet()) {
			Long filecount = unsupportedFileTypesCountMap.get(filetype);
			log.debug("filetype: " + filetype + " filecount: " + filecount);
			totalUnsupportedFileCount += filecount;
		}
		log.info("totalUnsupportedFileCount: " +totalUnsupportedFileCount);
		
		log.debug("matches per file");
		
		for (String filepath : matchesPerFile.keySet()) {
			FileMatchDTO matches = matchesPerFile.get(filepath);
			log.debug("filepath: " + filepath + ": " + matches);
		}
		
		outputService.convertIntoTree(matchesPerFile);
		
		pw.close();
		
		ResultDTO resultDTO = new ResultDTO();
		resultDTO.setTotalFileCount(totalFileCount);
		resultDTO.setTotalUnsupportedFileCount(totalUnsupportedFileCount);
		resultDTO.setTotalScannedFilesCount(totalScannedFilesCount);
		resultDTO.setSkippedFileCount(skippedFileCount);
		resultDTO.setTotalMatchCount(totalMatchCount);
		
		return resultDTO;
		
	}



	private void processFile(Map<String, Pattern> patternMap, File filePath, int depth) throws IOException {
		depth++;
		
		log.debug("  checking file or directory: " + filePath.getName());
		
		if (filePath.isFile()) {
			log.debug("  reading file: " + filePath.getName());
			readFile(patternMap, filePath);
			
			
		} else {
			log.debug("depth: " + depth + ", current path: " + filePath.getAbsolutePath());
			log.debug("  reading directory: " + filePath.getName());
			
			File[] files = filePath.listFiles();
			if (files!=null) {
				log.debug("number of files: " + files.length);
				for (File file : files) {
					log.debug("file: " + file.getName());
					log.debug("depth: " +depth);
					log.debug("maxdepth: " + configDTO.getMaxdepth());
					if (depth < configDTO.getMaxdepth()) {
						log.debug("reading file or directory");
						
						processFile(patternMap, file, depth);	
					}
					
				}
				
			}
			
			
			
		}
	}


	private void readFile(Map<String, Pattern> patternMap, File file) throws IOException {
		String filename = file.getName();
		
		System.out.print(".");
		
		if (!matchesExcludePath(file.getAbsolutePath(), configDTO.getExcludePathList())) {
		
			log.debug("reading file: " + filename);
			totalFileCount++;
			
			if (configDTO.getMaxFileSizeBytes()==-1 || file.length() < configDTO.getMaxFileSizeBytes() ) {
				log.debug("reading file: " + file.getAbsolutePath());
				
				String filetype = StringUtils.substringAfterLast(filename, ".");
				log.debug("filetype: " + filetype);
				ScanFilePlugin plugin = pluginMap.get(filetype);
				if (plugin!=null) {
					totalScannedFilesCount++;
					
					log.debug("plugin found!");
					FileMatchDTO fileMatchDTO = plugin.readFile(file, patternMap, configDTO.getWhitelist(), pw, datacategorySensitivityMap);
					int numberOfMatches = fileMatchDTO.getMatchCount();
					int sensitivityLevel = fileMatchDTO.getSensitivityLevel();
					log.debug("sensitivityLevel: " + sensitivityLevel);
					
					String absolutePath = file.getAbsolutePath();
	
					if (fileMatchDTO.getMatchCount()>0) {
	
						FileMatchDTO matchDTO = matchesPerFile.get(absolutePath);
						
						// add to matchCount
						Integer matchCount = null;
						if (matchDTO==null) {
							matchDTO = new FileMatchDTO();
							matchCount = fileMatchDTO.getMatchCount();
						} else {
							matchCount = matchDTO.getMatchCount() + fileMatchDTO.getMatchCount();
						}
						
						matchDTO.setMatchCount(matchCount);
						
						// increase sensitivity Level
						if (sensitivityLevel > matchDTO.getSensitivityLevel()) {
							matchDTO.setSensitivityLevel(sensitivityLevel);
								
						}
						
						matchesPerFile.put(absolutePath, matchDTO);
						
						this.totalMatchCount += numberOfMatches;
					}
						
					
				} else {
					log.debug("unsupported file type: " + filetype);
					Long count = unsupportedFileTypesCountMap.get(filetype);
					if (count==null) {
						count = 1L;
					} else {
						count++;
					}
					
					unsupportedFileTypesCountMap.put(filetype, count);
					
				}
				
				
			} else {
				skippedFileCount++;
				log.debug("Skipping file " + file.getAbsolutePath() + ", size: " + file.length());
			} 
			
		} else {
			log.info("Skipping excluded filePath " + file.getAbsolutePath() );
		}
		
	}
	
	private boolean matchesExcludePath(String path, List<String> excludePathList) {
		
		for (String excludePath: excludePathList ) {
			if (path.contains(excludePath)) {
				return true;
			}
			
		}
		
		
		return false;
		
	}

	
}
