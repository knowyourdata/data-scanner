package at.fwd.data_scanner;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import at.fwd.data_scanner.config.MessagesConfig;
import at.fwd.data_scanner.dto.MappedTableDTO;
import at.fwd.data_scanner.dto.TableDTO;
import at.fwd.data_scanner.service.ConfigService;
import at.fwd.data_scanner.service.ProcessorService;
import at.fwd.data_scanner.service.ScannerDdlService;
import at.fwd.data_scanner.service.ScannerJdbcService;
import at.fwd.data_scanner.service.output.OutputService;
import at.fwd.data_scanner.util.StringUtil;

/**
 * Command to run the data-scanner
 *
 */
public class ScannerCommand {

	private static final Logger log = Logger.getLogger(ScannerCommand.class);
	
	public static void main(String[] args) {
		try {
			ScannerCommand scanner = new ScannerCommand();
			scanner.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void run() throws IOException {
		try {
			ConfigService configService = new ConfigService();
			
			Map<String, String> classificationMap = new HashMap<String, String>();
			classificationMap = configService.readClassificationMap();
			
			Map<String, Integer> datacategorySensitivityMap = new HashMap<String, Integer>();
			datacategorySensitivityMap = configService.readDataCategorySensitivityMap();
			
			InputStream input = this.getClass().getResourceAsStream("/config.properties");
			
			if (input!=null) {
				Properties configProperties = new Properties();
				configProperties.load(input);
				
				Properties messagesProperties = new Properties();
				configService.loadConfigMessages(messagesProperties);
				
				MessagesConfig configHeadlineDTO =  configService.readConfigHeadlines(messagesProperties);
				
				String application = configProperties.getProperty("application");
				System.out.println("application: " + application);
				
				String ddlPath= configProperties.getProperty("ddlPath");
				String excludeTablesStr = configProperties.getProperty("exclude_tables");
				List<String> excludeTables = StringUtil.splitToList(excludeTablesStr, ",");
				
				String excelOutputFilePath = configProperties.getProperty("excelOutputFilePath");
				String jsonOutputFilePath = configProperties.getProperty("jsonOutputFilePath");
				String jsonByApplicationFilename = configProperties.getProperty("jsonByApplicationFilename");
				String jsonByDatacategoryFilename = configProperties.getProperty("jsonByDatacategoryFilename");

				boolean checkForeignKeys = false;
				
				log.info("classifications: " + classificationMap.size());
				
				
				List<TableDTO> tableList = null;
				if (StringUtils.isNotEmpty(ddlPath)) {
					ScannerDdlService scannerDdlService = new ScannerDdlService();
					scannerDdlService.setClassificationMap(classificationMap);
					tableList = scannerDdlService.checkDatabase(ddlPath, excludeTables);
					
					
				} else {
					ScannerJdbcService scannerService = new ScannerJdbcService();
					
					String jdbcUrl = configProperties.getProperty("jdbcUrl");
					String username = configProperties.getProperty("username");
					String password = configProperties.getProperty("password");
					
					tableList = scannerService.checkDatabase(jdbcUrl, username, password, excludeTables, checkForeignKeys);
				}
				
				log.info("Number of tables found: " + tableList.size());
				
				ProcessorService processorService = new ProcessorService();
				List<MappedTableDTO> mappedTableDTOList = processorService.processTables(classificationMap, datacategorySensitivityMap, application, tableList);
				
				OutputService outputService = new OutputService();
				outputService.writeResults(datacategorySensitivityMap, configHeadlineDTO, excelOutputFilePath, jsonOutputFilePath,
						jsonByApplicationFilename, jsonByDatacategoryFilename, mappedTableDTOList);
				
			} else {
				System.err.println("Configuration file config.properties not found");
			}
			
						
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}




	
}

