package at.fwd.data_scanner.service.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.fwd.data_scanner.config.MessagesConfig;
import at.fwd.data_scanner.constants.DataScannerConstants;
import at.fwd.data_scanner.dto.MappedColumn;
import at.fwd.data_scanner.dto.MappedTableDTO;
import at.fwd.data_scanner.util.DatacategoryUtil;

public class ExcelOutputService {

	private static final Logger log = Logger.getLogger(ExcelOutputService.class);
	
	public void writeExcel(Map<String, Integer> datacategorySensitivityMap, MessagesConfig configHeadline, List<MappedTableDTO> tableDTOList, String filepath) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		XSSFSheet sheet = workbook.createSheet(configHeadline.getSheetOverview());
		CellStyle headlineStyle=null;
		XSSFFont font= workbook.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setBold(true);
	    font.setItalic(false);
		
	    headlineStyle=workbook.createCellStyle();
		headlineStyle.setFont(font);
		headlineStyle.setAlignment(CellStyle.ALIGN_CENTER);
		
		writeOverview(datacategorySensitivityMap, configHeadline, tableDTOList, sheet, headlineStyle);
		
		sheet = workbook.createSheet(configHeadline.getSheetDetails());

		writeDetails(sheet, datacategorySensitivityMap, configHeadline, tableDTOList, workbook, headlineStyle, false);
		
		sheet = workbook.createSheet(configHeadline.getSheetDataDictionary());
		
		writeDetails(sheet, datacategorySensitivityMap, configHeadline, tableDTOList, workbook, headlineStyle, true);
		
		try {
			FileOutputStream outputStream = new FileOutputStream(filepath);
			workbook.write(outputStream);
			outputStream.close();
			
			System.out.println("Excel file written to " + filepath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeOverview(Map<String, Integer> datacategorySensitivityMap, MessagesConfig configHeadline, List<MappedTableDTO> tableDTOList,
			XSSFSheet sheet, CellStyle headlineStyle) {
		int rowNum = 0;
		Row row = sheet.createRow(rowNum++);
		int colNum = 0;
		
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 15000);
		sheet.setColumnWidth(3, 10000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 5000);
		
		Cell cell = row.createCell(colNum++);
		cell.setCellValue(configHeadline.getApplication());
		cell.setCellStyle(headlineStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue(configHeadline.getTablename());
		cell.setCellStyle(headlineStyle);
		cell = row.createCell(colNum++);
		cell.setCellValue(configHeadline.getColumns());
		cell.setCellStyle(headlineStyle);
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getDatacategories());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getMaximumSensitivityLevel());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getRemarks());
		
		Map<String, List<MappedTableDTO>> applicationTables = new HashMap<String, List<MappedTableDTO>>();
		
		if (tableDTOList!=null) {
			for (MappedTableDTO mappedTableDTO : tableDTOList) {
				String tablename = mappedTableDTO.getTableDTO().getTablename();
				
				if (mappedTableDTO.getMaxSensitivityLevel()>-1) {
					List<MappedTableDTO> mappedTableDTOList = applicationTables.get(mappedTableDTO.getApplication());
					
					if (mappedTableDTOList==null) mappedTableDTOList = new ArrayList<MappedTableDTO>();
					
					if (!mappedTableDTOList.contains(tablename)) {
						mappedTableDTOList.add(mappedTableDTO);	
					}
					
					applicationTables.put(mappedTableDTO.getApplication(), mappedTableDTOList);
					
				}
				
				
				
				
			}
				
		}
		
		log.debug("datacategorySensitivityMap: " + datacategorySensitivityMap.keySet());
		
		for( String application : applicationTables.keySet()) {
			for (MappedTableDTO mappedTableDTO : applicationTables.get(application)) {
				
					boolean columnPresent = false;
					
					Integer sensitivityLevel = new Integer(0);
					StringBuffer columnNamesBuffer = new StringBuffer();
					List<String> datacategoryList = new ArrayList<String>();
					
					for (MappedColumn col : mappedTableDTO.getColumnList()) {
						
						if (!col.getClassification().equals(DataScannerConstants.DATACATEGORY_UNCLASSIFIED)) {
							
							if (col.getApplication().equals(application) && col.getTablename().equals(mappedTableDTO.getTableDTO().getTablename())) {
								if (columnPresent) {
									columnNamesBuffer.append(", ");
								}
								columnNamesBuffer.append(col.getColumnDTO().getColumnname() );
								log.debug("column: " + col.getColumnDTO().getColumnname());
								String classification = col.getClassification();
								
								log.debug("col.getClassification(): " + classification);
								Integer columnSensitivityLevel = datacategorySensitivityMap.get(classification);
								log.debug("columnSensitivityLevel: " + columnSensitivityLevel);
								
								String label = DatacategoryUtil.getLabelForDatacategory(configHeadline, classification);
								if (!datacategoryList.contains(label)) {
									datacategoryList.add(label);
								}
								
								if (columnSensitivityLevel!=null) {
									if (columnSensitivityLevel>sensitivityLevel) {
										sensitivityLevel = columnSensitivityLevel;
									}
										
								}
								
								columnPresent = true;
							}
							
						}	
					}
					
					row = sheet.createRow(rowNum++);
					colNum = 0;
					cell = row.createCell(colNum++);
					cell.setCellValue(application);
					cell = row.createCell(colNum++);
					cell.setCellValue(mappedTableDTO.getTableDTO().getTablename());
					cell = row.createCell(colNum++);
					cell.setCellValue(columnNamesBuffer.toString());
					cell = row.createCell(colNum++);
					cell.setCellValue(StringUtils.join(datacategoryList, ", "));
					cell = row.createCell(colNum++);
					cell.setCellValue(sensitivityLevel);
					cell = row.createCell(colNum++);
					cell.setCellValue(mappedTableDTO.getTableDTO().getRemarks());
					
				
			}
		}
		
		
	}

	
	protected void writeDetails(XSSFSheet sheet, Map<String, Integer> datacategorySensitivityMap, MessagesConfig configHeadline, List<MappedTableDTO> mappedTableDTOList,
			XSSFWorkbook workbook, CellStyle headlineStyle, boolean includeUnclassified) {
		int rowNum = 0;
		
		prepareDetailHeadings(sheet, configHeadline, rowNum, headlineStyle);
		rowNum++;
		
		for (MappedTableDTO mappedTableDTO : mappedTableDTOList) {
			
			for (MappedColumn mappedColumn : mappedTableDTO.getColumnList() ) {
			
				Row row = writeDetailRow(sheet, datacategorySensitivityMap, includeUnclassified, rowNum, mappedTableDTO,
						mappedColumn);
				
				if (row!=null) {
					rowNum++;	
				}
				
			}
			
			
		}
	}

	protected Row writeDetailRow(XSSFSheet sheet, Map<String, Integer> datacategorySensitivityMap,
			boolean includeUnclassified, int rowNum, MappedTableDTO mappedTableDTO, MappedColumn mappedColumn) {
		Row row = null;
		int colNum;
		Cell cell;
		if (includeUnclassified || !mappedColumn.getClassification().equals(DataScannerConstants.DATACATEGORY_UNCLASSIFIED)) {
			row = sheet.createRow(rowNum);
			
			//System.out.println(col.getApplication() + ", " + col.getTablename() + ", " + col.getColumnDTO().getColumnname() + ", " + col.getClassification() +", " + col.getColumnDTO().getRemarks());
			colNum = 0;
			cell = row.createCell(colNum++);
			cell.setCellValue(mappedTableDTO.getApplication());
			cell = row.createCell(colNum++);
			cell.setCellValue(mappedTableDTO.getTableDTO().getTablename() );
			cell = row.createCell(colNum++);
			cell.setCellValue( mappedColumn.getColumnDTO().getColumnname());
			cell = row.createCell(colNum++);
			cell.setCellValue(mappedColumn.getClassification());
			cell = row.createCell(colNum++);
			
			Integer columnSensitivityLevel = datacategorySensitivityMap.get(mappedColumn.getClassification());
			if (columnSensitivityLevel!=null) {
				cell.setCellValue(columnSensitivityLevel);
					
			}
			cell = row.createCell(colNum++);
			cell.setCellValue(mappedColumn.getColumnDTO().getTypename());
			cell = row.createCell(colNum++);
			cell.setCellValue(mappedColumn.getColumnDTO().getColumnsize());
			cell = row.createCell(colNum++);
			cell.setCellValue(mappedColumn.getColumnDTO().getRemarks());
		}
		return row;
	}

	protected Row prepareDetailHeadings(XSSFSheet sheet, MessagesConfig configHeadline, Integer rowNum, CellStyle headlineStyle) {
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 7000);
		sheet.setColumnWidth(4, 7000);
		sheet.setColumnWidth(5, 5000);
		sheet.setColumnWidth(6, 5000);
		sheet.setColumnWidth(7, 7000);
		
		Row row = sheet.createRow(rowNum++);
		int colNum = 0;
		Cell cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getApplication());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getTablename());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getColumnname());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getDatacategory());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getSensitivityLevel());
		
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getTypeName());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getColumnSize());
		cell = row.createCell(colNum++);
		cell.setCellStyle(headlineStyle);
		cell.setCellValue(configHeadline.getRemarks());
		return row;
	}
	
	

}
