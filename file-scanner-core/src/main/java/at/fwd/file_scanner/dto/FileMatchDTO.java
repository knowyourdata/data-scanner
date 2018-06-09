package at.fwd.file_scanner.dto;

public class FileMatchDTO {

	int matchCount = 1;
	
	int sensitivityLevel = 0;

	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public int getSensitivityLevel() {
		return sensitivityLevel;
	}

	public void setSensitivityLevel(int sensitivityLevel) {
		this.sensitivityLevel = sensitivityLevel;
	} 
	
	
	
}
