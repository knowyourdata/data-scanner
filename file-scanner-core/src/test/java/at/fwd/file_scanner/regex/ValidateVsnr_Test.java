package at.fwd.file_scanner.regex;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import at.fwd.file_scanner.service.ConfigService;

public class ValidateVsnr_Test {
	private static final Logger log = Logger.getLogger(ValidateVsnr_Test.class);
	
	ConfigService configService = new ConfigService();
	
	Pattern vsnrPattern;
	
	@Before
	public void setUp() throws IOException {
		Map<String, Pattern> patternMap = configService.buildPatternMap();
		
		vsnrPattern = patternMap.get("social_security_number_Austria");
		assertNotNull(vsnrPattern);	
	}
	
	
	@Test
	public void test() {
		Matcher matcher = vsnrPattern.matcher("  1111111111  ");
		assertTrue(matcher.find());
		
	}
	
	@Test
	public void test_invalid_blanks_leading_only() {
		Matcher matcher = vsnrPattern.matcher("  1111111111");
		assertFalse(matcher.find());
		
	}
	

	@Test
	public void test_blanks_trailing_only() {
		Matcher matcher = vsnrPattern.matcher("1111111111  ");
		assertTrue(matcher.find());
		
	}
	@Test
	public void test_date_low() {
		Matcher matcher = vsnrPattern.matcher("  1111010100  ");
		assertTrue(matcher.find());
		
	}
	

	@Test
	public void test_max() {
		Matcher matcher = vsnrPattern.matcher("  1111311299  ");
		assertTrue(matcher.find());
		
	}
	

	@Test
	public void test_invalid_exceeded_day() {
		Matcher matcher = vsnrPattern.matcher("  1111321299  ");
		assertFalse(matcher.find());
		
	}
	

	@Test
	public void test_invalid_exceeded_month() {
		Matcher matcher = vsnrPattern.matcher("  1111311399  ");
		assertFalse(matcher.find());
		
	}
	
	@Test
	public void test_invalid_trailing_number() {
		Matcher matcher = vsnrPattern.matcher("  11113113994  ");
		assertFalse(matcher.find());
		
	}


	@Test
	public void test_invalid_trailing_number_3_digits() {
		Matcher matcher = vsnrPattern.matcher("  1526230305633  ");
		assertFalse(matcher.find());
		
	}

	
	
	@Test
	public void test_valid_trailing_letter() {
		Matcher matcher = vsnrPattern.matcher("  1111111111x  ");
		assertFalse(matcher.find());
		
	}
}
