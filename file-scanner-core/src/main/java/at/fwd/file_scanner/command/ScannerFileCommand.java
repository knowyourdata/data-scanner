package at.fwd.file_scanner.command;

import java.io.IOException;

import at.fwd.file_scanner.service.ScannerFileService;

public class ScannerFileCommand {

	public static void main (String[] args) {
		
		ScannerFileService service = new ScannerFileService();
		try {
			if (args.length==0) {
				System.out.println("Please enter the path to scan");
				
			} else {
				String path = args[0];		
				service.run(path);
					
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
