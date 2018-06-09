package at.fwd.file_scanner.dto;

public class ResultDTO {

	Long totalFileCount;
	
	Long totalScannedFilesCount;
	
	Long totalUnsupportedFileCount;
	
	Long skippedFileCount;
	
	Long totalMatchCount;

	
	public Long getTotalFileCount() {
		return totalFileCount;
	}

	public void setTotalFileCount(Long totalFileCount) {
		this.totalFileCount = totalFileCount;
	}

	public Long getTotalScannedFilesCount() {
		return totalScannedFilesCount;
	}

	public void setTotalScannedFilesCount(Long totalScannedFilesCount) {
		this.totalScannedFilesCount = totalScannedFilesCount;
	}

	public Long getTotalUnsupportedFileCount() {
		return totalUnsupportedFileCount;
	}

	public void setTotalUnsupportedFileCount(Long totalUnsupportedFileCount) {
		this.totalUnsupportedFileCount = totalUnsupportedFileCount;
	}

	public Long getSkippedFileCount() {
		return skippedFileCount;
	}

	public void setSkippedFileCount(Long skippedFileCount) {
		this.skippedFileCount = skippedFileCount;
	}

	public Long getTotalMatchCount() {
		return totalMatchCount;
	}

	public void setTotalMatchCount(Long totalMatchCount) {
		this.totalMatchCount = totalMatchCount;
	}
	

	
	
}
