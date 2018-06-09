package at.fwd.file_scanner.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import at.fwd.file_scanner.api.ScanFilePlugin;
import at.fwd.file_scanner.dto.FileMatchDTO;
import at.fwd.file_scanner.util.ScannerUtil;

public class ReadExcelPlugin implements ScanFilePlugin {

	private static final Logger log = Logger.getLogger(ReadExcelPlugin.class);
	
	public FileMatchDTO readFile(File file, Map<String, Pattern> patternMap, List<String> whitelist,
			PrintWriter pw, Map<String, Integer> dataCategorySensitivityMap) throws IOException {
		
		String filename = file.getName();
		FileInputStream input = new FileInputStream(file);
		
		Integer numberOfMatches = 0;
		Integer sensitivityLevel = 0;
		
		FileMatchDTO dto;
		
		try {
			Workbook workbook = WorkbookFactory.create(file);
			
			log.debug("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

			int sheets = workbook.getNumberOfSheets();
			
			for (int i = 0; i < sheets; i++) {
				Sheet sheet = workbook.getSheetAt(i);
				
				String name = sheet.getSheetName();
				dto= ScannerUtil.matchLineContent(file, patternMap, name, whitelist,
						 pw, dataCategorySensitivityMap);
				
				numberOfMatches +=dto.getMatchCount();
				if (dto.getSensitivityLevel() > sensitivityLevel) {
					sensitivityLevel = dto.getSensitivityLevel();
				}
				 
				Iterator<Row> rowIterator = sheet.rowIterator();
		        while (rowIterator.hasNext()) {
		            Row row = rowIterator.next();
		            
		            
		            for(Cell cell: row) {
		            	if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
		            		String value = cell.getStringCellValue();
		            		dto= ScannerUtil.matchLineContent(file, patternMap, value, whitelist,
									 pw, dataCategorySensitivityMap);
							
		            		numberOfMatches +=dto.getMatchCount();
		    				if (dto.getSensitivityLevel() > sensitivityLevel) {
		    					sensitivityLevel = dto.getSensitivityLevel();
		    				}
			                	
		            	} else {
		            		
		            		// TODO other cell types
		            	}
		                		
		            }
		            
		            
		        }
				
				
			}
			
			input.close();
			
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		FileMatchDTO fileMatchDTO = new FileMatchDTO();
		fileMatchDTO.setMatchCount(numberOfMatches);
		fileMatchDTO.setSensitivityLevel(sensitivityLevel);
		
		return fileMatchDTO;		
	}
	

	
	public List<String> getSupportedFiletypes() {
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("xlsx");
		fileTypeList.add("xls");
		return fileTypeList;
	}
	
	
}

