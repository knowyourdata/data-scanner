package at.fwd.file_scanner.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import at.fwd.file_scanner.api.ScanFilePlugin;
import at.fwd.file_scanner.dto.FileMatchDTO;
import at.fwd.file_scanner.util.ScannerUtil;

public class ReadPdfPlugin implements ScanFilePlugin {

	private static final Logger log = Logger.getLogger(ReadPdfPlugin.class);
	
	public FileMatchDTO readFile(File file, Map<String, Pattern> patternMap, List<String> whitelist,
			PrintWriter pw, Map<String, Integer> dataCategorySensitivityMap) throws IOException {
		String filename = file.getName();
		Integer numberOfMatches = 0;
		Integer sensitivityLevel = 0;
		
		log.info("reading pdf file: " + filename);
		
		FileInputStream input = new FileInputStream(file);
		
		try {
			PdfReader reader = new PdfReader(input);
			
			int numberOfPages = reader.getNumberOfPages();
			
			for (int i = 1; i <= numberOfPages;i++ ) {
				String text = PdfTextExtractor.getTextFromPage(reader, i);
		        
				FileMatchDTO dto = ScannerUtil.matchLineContent(file, patternMap, text, whitelist,
						 pw, dataCategorySensitivityMap);
				
				numberOfMatches +=dto.getMatchCount();
				if (dto.getSensitivityLevel() > sensitivityLevel) {
					sensitivityLevel = dto.getSensitivityLevel();
				}
			}
			
			// TODO read pdf line by line..
	//		int pageNum = 1;
	//		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
	//		ItextPdfRenderListener listener = new	ItextPdfRenderListener();
	//		RenderListener renderListener = parser.processContent (pageNum, listener);
		
		} catch (NoClassDefFoundError e) {
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
		fileTypeList.add("pdf");
		return fileTypeList;
	}
	
	
}

