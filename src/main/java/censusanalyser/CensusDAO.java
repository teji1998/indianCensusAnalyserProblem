package censusanalyser;

public class CensusDAO  {

	public String stateCode;
	public String state;
	public int population;
	public double populationDensity;
	public double totalArea;

	public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
		state = indiaCensusCSV.state;
		totalArea = indiaCensusCSV.areaInSqKm;
		populationDensity = indiaCensusCSV.densityPerSqKm;
		population = indiaCensusCSV.population;
	}

	public CensusDAO(IndiaStateCodeCSV indiaStateCodeCSV){
		stateCode = indiaStateCodeCSV.stateCode;
	}

	public CensusDAO(USCensusCSV usCensusCSV) {
		state = usCensusCSV.state;
		stateCode = usCensusCSV.stateId;
		population = usCensusCSV.population;
		populationDensity = usCensusCSV.populationDensity;
		totalArea = usCensusCSV.totalArea;

	}
}

