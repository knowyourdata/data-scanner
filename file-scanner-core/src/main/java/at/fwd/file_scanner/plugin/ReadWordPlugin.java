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
import org.apache.poi.POIXMLException;
import org.apache.poi.xwpf.model.XWPFCommentsDecorator;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlink;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import at.fwd.file_scanner.api.ScanFilePlugin;
import at.fwd.file_scanner.dto.FileMatchDTO;
import at.fwd.file_scanner.util.ScannerUtil;

public class ReadWordPlugin implements ScanFilePlugin {

	private static final Logger log = Logger.getLogger(ReadWordPlugin.class);
	
	public FileMatchDTO readFile(File file, Map<String, Pattern> patternMap, List<String> whitelist,
			PrintWriter pw, Map<String, Integer> dataCategorySensitivityMap) throws IOException {
		String filename = file.getName();
		log.info("reading word file: " + filename);
		Integer numberOfMatches = 0;
		Integer sensitivityLevel = 0;
		FileInputStream input = new FileInputStream(file);
		
		try {
			XWPFDocument document = new XWPFDocument(input);
	        //XWPFWordExtractor extract = new XWPFWordExtractor(doc);
	        //log.debug(extract.getText());
			
			
			XWPFHeaderFooterPolicy hfPolicy = document.getHeaderFooterPolicy();

			// Start out with all headers
			//extractHeaders(text, hfPolicy);
			
			boolean fetchHyperlinks = true;
			
			FileMatchDTO dto ;
			
			// First up, all our paragraph based text
			Iterator<XWPFParagraph> i = document.getParagraphsIterator();
			while(i.hasNext()) {
				XWPFParagraph paragraph = i.next();

				try {
					CTSectPr ctSectPr = null;
					if (paragraph.getCTP().getPPr()!=null) {
						ctSectPr = paragraph.getCTP().getPPr().getSectPr();
					}

					XWPFHeaderFooterPolicy headerFooterPolicy = null;

					if (ctSectPr!=null) {
						headerFooterPolicy = new XWPFHeaderFooterPolicy(document, ctSectPr);
						//extractHeaders(text, headerFooterPolicy);
					}

					// Do the paragraph text
					for(XWPFRun run : paragraph.getRuns()) {
					   //text.append(run.toString());
						
						dto = ScannerUtil.matchLineContent(file, patternMap, run.toString(), whitelist,
								 pw, dataCategorySensitivityMap);
						
						numberOfMatches += dto.getMatchCount();
						if (dto.getSensitivityLevel()>sensitivityLevel) {
							sensitivityLevel = dto.getSensitivityLevel();
						}

					   if(run instanceof XWPFHyperlinkRun && fetchHyperlinks) {
					      XWPFHyperlink link = ((XWPFHyperlinkRun)run).getHyperlink(document);
					      if(link != null) {
					         // text.append(" <" + link.getURL() + ">");
					    	  
					    	dto = ScannerUtil.matchLineContent(file, patternMap, link.getURL(), whitelist,
										 pw, dataCategorySensitivityMap);
					    	numberOfMatches += dto.getMatchCount();
							if (dto.getSensitivityLevel()>sensitivityLevel) {
								sensitivityLevel = dto.getSensitivityLevel();
							}
							   
					      }
					   }
					}

					// Add comments
					XWPFCommentsDecorator decorator = new XWPFCommentsDecorator(paragraph, null);
					dto = ScannerUtil.matchLineContent(file, patternMap, decorator.getCommentText(), whitelist,
							 pw, dataCategorySensitivityMap);
					numberOfMatches += dto.getMatchCount();
					if (dto.getSensitivityLevel()>sensitivityLevel) {
						sensitivityLevel = dto.getSensitivityLevel();
					}
					
					// Do endnotes and footnotes
					String footnameText = paragraph.getFootnoteText();
				   if(footnameText != null && footnameText.length() > 0) {
				      //text.append(footnameText + "\n");
				      
					   dto =  ScannerUtil.matchLineContent(file, patternMap, footnameText, whitelist,
								 pw, dataCategorySensitivityMap);
					   numberOfMatches += dto.getMatchCount();
						if (dto.getSensitivityLevel()>sensitivityLevel) {
							sensitivityLevel = dto.getSensitivityLevel();
						}
						
				   }

					if (ctSectPr!=null) {
						//extractFooters(text, headerFooterPolicy);
					}
				} catch (IOException e) {
					throw new POIXMLException(e);
				} catch (XmlException e) {
					throw new POIXMLException(e);
				}
			}

			// Then our table based text
			Iterator<XWPFTable> j = document.getTablesIterator();
			while(j.hasNext()) {
				//text.append(j.next().getText()).append('\n');
				dto = ScannerUtil.matchLineContent(file, patternMap, j.next().getText(), whitelist,
						 pw, dataCategorySensitivityMap);
				numberOfMatches += dto.getMatchCount();
				if (dto.getSensitivityLevel()>sensitivityLevel) {
					sensitivityLevel = dto.getSensitivityLevel();
				}
			}
			
			// Finish up with all the footers
			//extractFooters(text, hfPolicy);
			
			} catch (POIXMLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			log.info("word file done");
		
			FileMatchDTO fileMatchDTO = new FileMatchDTO();
			fileMatchDTO.setMatchCount(numberOfMatches);
			fileMatchDTO.setSensitivityLevel(sensitivityLevel);
			
			return fileMatchDTO;
		
	}
	

	
	public List<String> getSupportedFiletypes() {
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("docx");
		fileTypeList.add("doc");
		return fileTypeList;
	}
	
	
}

