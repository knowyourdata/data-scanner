package at.fwd.data_scanner.util;

import at.fwd.data_scanner.config.MessagesConfig;

public class DatacategoryUtil {

	public static String getLabelForDatacategory(MessagesConfig configHeadline, String classification) {
		String datacategoryLabel = classification;
		if (configHeadline.getDatacategoryLabels().containsKey(classification)) {
			datacategoryLabel = configHeadline.getDatacategoryLabels().get(classification);
		}
		return datacategoryLabel;
	}

	
}
