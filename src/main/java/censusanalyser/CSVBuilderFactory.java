package censusanalyser;

import com.customCSV.ICSVBuilder;
import com.customCSV.OpenCSVBuilder;

public class CSVBuilderFactory {
	public static ICSVBuilder createCSVBuilder() {
		return new OpenCSVBuilder();
	}
}
