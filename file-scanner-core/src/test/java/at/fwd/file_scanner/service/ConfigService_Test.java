package at.fwd.file_scanner.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

public class ConfigService_Test {

	ConfigService configService = new ConfigService();
	
	@Test
	public void test() throws IOException {
		Map<String, Pattern> patternMap = configService.buildPatternMap();
		assertEquals(2, patternMap.size());
		assertTrue(patternMap.keySet().contains("email"));
		
	}
	
}
