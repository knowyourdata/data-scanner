package at.fwd.data_scanner.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringUtil {

	public static List<String> splitToList(String value, String delimiter) {
		String[] result = StringUtils.split(value, delimiter);
		return Arrays.asList(result);
	}

}
