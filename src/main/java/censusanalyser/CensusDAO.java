package censusanalyser;

public class CensusDAO  {
	public String state;
	public String stateCode;
	public String stateId;
	public double totalArea;
	public double populationDensity;
	public int population;

	public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
		state = indiaCensusCSV.state;
		totalArea = indiaCensusCSV.areaInSqKm;
		populationDensity = indiaCensusCSV.densityPerSqKm;
		population = indiaCensusCSV.population;
	}

	public CensusDAO(IndiaStateCodeCSV indiaStateCodeCSV) {
		stateCode = indiaStateCodeCSV.stateCode;
	}

	public CensusDAO(USCensusCSV censusCSV) {
		state = censusCSV.state;
		stateId = censusCSV.stateId;
		totalArea = censusCSV.totalArea;
		populationDensity = censusCSV.populationDensity;
		population = censusCSV.population;
	}
}
