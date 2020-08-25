package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndiaStateCodeCSV {

	@CsvBindByName(column = "SrNo", required = true)
	public String SrNo;

	@CsvBindByName(column = "State Name", required = true)
	public String stateName;

	@CsvBindByName(column = "TIN", required = true)
	public String TIN;

	@CsvBindByName(column = "StateCode", required = true)
	public String stateCode;

	@Override
	public String toString() {
		return "IndiaStateCodeCSV{" +
				"SrNo='" + SrNo + '\'' +
				", state='" + stateName + '\'' +
				", TIN='" + TIN + '\'' +
				", stateCode='" + stateCode + '\'' +
				'}';
	}
}

