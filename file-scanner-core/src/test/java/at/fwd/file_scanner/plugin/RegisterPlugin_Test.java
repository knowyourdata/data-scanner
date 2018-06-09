package at.fwd.file_scanner.plugin;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import at.fwd.file_scanner.api.ScanFilePlugin;
import at.fwd.file_scanner.service.ScannerPluginService;

public class RegisterPlugin_Test {

	ScannerPluginService service = new ScannerPluginService();
	
	@Test
	public void test() throws IOException {
		Map<String, ScanFilePlugin> map = service.readPlugins();
		assertEquals(13, map.keySet().size());
		
	}
	
}
