package at.fwd.file_scanner.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import at.fwd.file_scanner.dto.FileMatchDTO;

public interface ScanFilePlugin {

	public FileMatchDTO readFile(File file, Map<String, Pattern> patternMap, List<String> whitelist,
			PrintWriter pw, Map<String, Integer> dataCategorySensitivityMap) throws IOException;
	
	public List<String> getSupportedFiletypes();
	
}
