package at.fwd.data_scanner.service.output;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import at.fwd.data_scanner.config.MessagesConfig;
import at.fwd.data_scanner.dto.MappedTableDTO;

public class OutputService {


	public void writeResults(Map<String, Integer> datacategorySensitivityMap, MessagesConfig configHeadlineDTO,
			String excelOutputFilePath, String jsonOutputFilePath, String jsonByApplicationFilename,
			String jsonByDatacategoryFilename, List<MappedTableDTO> mappedTableDTOList) {
		if (StringUtils.isNotEmpty(excelOutputFilePath)) {
			ExcelOutputService outputService = new ExcelOutputService();
			outputService.writeExcel(datacategorySensitivityMap, configHeadlineDTO, mappedTableDTOList, excelOutputFilePath);
				
		}
		
		
		if (StringUtils.isNotEmpty(jsonOutputFilePath)) {

			JsonOutputService jsonOutputService = new JsonOutputService();
			jsonOutputService.writeOutput(datacategorySensitivityMap, configHeadlineDTO, mappedTableDTOList, jsonOutputFilePath + File.separator + jsonByDatacategoryFilename);
			
			jsonOutputService.writeOutputForApplications(datacategorySensitivityMap, configHeadlineDTO, mappedTableDTOList, jsonOutputFilePath + File.separator + jsonByApplicationFilename);

		}
	}
	
}
