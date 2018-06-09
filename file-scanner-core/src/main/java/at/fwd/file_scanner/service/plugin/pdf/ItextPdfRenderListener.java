package at.fwd.file_scanner.service.plugin.pdf;

import org.apache.log4j.Logger;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

import at.fwd.file_scanner.service.ScannerFileService;

public class ItextPdfRenderListener implements RenderListener {

	private static final Logger log = Logger.getLogger(ScannerFileService.class);
	
	
	@Override
	public void beginTextBlock() {
		
	}

	@Override
	public void renderText(TextRenderInfo renderInfo) {
		log.info("text in pdf: " + renderInfo.getText());
	}

	@Override
	public void endTextBlock() {
		
	}

	@Override
	public void renderImage(ImageRenderInfo renderInfo) {
		
	}

}
