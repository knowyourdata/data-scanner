package at.fwd.file_scanner.dto;

import java.util.List;

public class ConfigDTO {

	private Integer maxdepth;
	
	private Long maxFileSizeBytes;
	
	private String outputPath;
	
	private String logFilename;
	
	private String filesTreeTextFilename;
	
	private String filesTreeJsonFilename;
	
	private List<String> whitelist;
	
	private List<String> excludePathList;
	

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	

	public String getLogFilename() {
		return logFilename;
	}

	public void setLogFilename(String logFilename) {
		this.logFilename = logFilename;
	}

	public String getFilesTreeTextFilename() {
		return filesTreeTextFilename;
	}

	public void setFilesTreeTextFilename(String filesTreeTextFilename) {
		this.filesTreeTextFilename = filesTreeTextFilename;
	}

	public String getFilesTreeJsonFilename() {
		return filesTreeJsonFilename;
	}

	public void setFilesTreeJsonFilename(String filesTreeJsonFilename) {
		this.filesTreeJsonFilename = filesTreeJsonFilename;
	}

	public Integer getMaxdepth() {
		return maxdepth;
	}

	public void setMaxdepth(Integer maxdepth) {
		this.maxdepth = maxdepth;
	}

	public Long getMaxFileSizeBytes() {
		return maxFileSizeBytes;
	}

	public void setMaxFileSizeBytes(Long maxFileSizeBytes) {
		this.maxFileSizeBytes = maxFileSizeBytes;
	}

	public List<String> getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(List<String> whitelist) {
		this.whitelist = whitelist;
	}

	public List<String> getExcludePathList() {
		return excludePathList;
	}

	public void setExcludePathList(List<String> excludePathList) {
		this.excludePathList = excludePathList;
	}

	
}
