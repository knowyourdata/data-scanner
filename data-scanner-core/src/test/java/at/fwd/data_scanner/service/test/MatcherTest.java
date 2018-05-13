package at.fwd.data_scanner.service.test;

import static org.junit.Assert.*;

import org.junit.Test;

/*
 * Tests for Regex for matching
 */
public class MatcherTest {

	@Test
	public void test() {
		String matcher = ".*email.*"; 
		String matchColumnName = "email_address";
		assertTrue(matchColumnName.matches(matcher));
	}
	
}
