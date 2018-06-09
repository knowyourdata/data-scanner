package at.fwd.file_scanner.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

import at.fwd.file_scanner.dto.ResultDTO;

public class ScannerFileService_Test {
	
	ScannerFileService service = new ScannerFileService();
	
	@Test
	public void test() throws IOException {
		ResultDTO resultDTO = service.run("testdata");
		assertNotNull(resultDTO);
		assertEquals(Long.valueOf(10L), resultDTO.getTotalFileCount());
		assertEquals(Long.valueOf(17L), resultDTO.getTotalMatchCount());
		
		assertEquals(Long.valueOf(10L), resultDTO.getTotalScannedFilesCount());
		assertEquals(Long.valueOf(0L), resultDTO.getTotalUnsupportedFileCount());
		assertEquals(Long.valueOf(0L), resultDTO.getSkippedFileCount());
		
		
	}

}
