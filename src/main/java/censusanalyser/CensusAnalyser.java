package censusanalyser;

import com.google.gson.Gson;
import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    Map<String, CensusDAO> censusMap;

    public CensusAnalyser() { }

    public int loadIndiaCensusData(String... csvFilePath) throws CensusAnalyserException {
	    censusMap = new CensusLoader().loadCensusData(IndiaCensusCSV.class, csvFilePath);
	    return censusMap.size();
    }

    public int loadUSCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusMap = new CensusLoader().loadCensusData(USCensusCSV.class, csvFilePath);
        return censusMap.size();
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sort( censusComparator, censusDAOList);
        String sortedStateCensus = new Gson().toJson(censusDAOList);
        return sortedStateCensus;
    }

    public String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.population );
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder( censusComparator, censusDAOList);
        String sortedPopulationCensus = new Gson().toJson(censusDAOList);
        return sortedPopulationCensus;
    }

    public String getStatePopulationDensityWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap== null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.populationDensity);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder( censusComparator, censusDAOList);
        String sortedPopulationDensityCensus = new Gson().toJson(censusDAOList);
        return sortedPopulationDensityCensus;
    }

    public String getStateAreaWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.totalArea);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder( censusComparator, censusDAOList);
        String sortedAreaWiseCensus = new Gson().toJson(censusDAOList);
        return sortedAreaWiseCensus;
    }

    public String getStateCodeWiseSortedData() throws CensusAnalyserException {
        if (censusMap  == null || censusMap .size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.stateCode);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sort( censusComparator, censusDAOList);
        String sortedStateCodeWiseCensus = new Gson().toJson(censusDAOList);
        return sortedStateCodeWiseCensus;
    }

    public String getUSCensusStateWiseSortedData() throws CensusAnalyserException {
        if (censusMap  == null || censusMap .size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> usCensusComparator = Comparator.comparing(census -> census.state);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder(usCensusComparator, censusDAOList);
        String sortedUSCensusByState = new Gson().toJson(censusDAOList);
        return sortedUSCensusByState;
    }

    private static <E> List<E> sort(Comparator<E> censusComparator, List<E> censusList) {
        for (int i = 0; i < censusList.size()-1; i++) {
            for (int j =0; j< censusList.size() -i -1; j++) {
                E census1 = censusList.get(j);
                E census2 = censusList.get(j+1);
                if (censusComparator.compare(census1, census2) > 0){
                    censusList.set(j, census2);
                    censusList.set(j+1, census1);
                }
            }
        }
        return censusList;
    }

    private static <E> List<E> sortInDescendingOrder(Comparator<E> censusComparator, List<E> censusList) {
        for (int i = 0; i < censusList.size()-1; i++) {
            for (int j =0; j< censusList.size() -i -1; j++) {
                E census1 = censusList.get(j);
                E census2 = censusList.get(j+1);
                if (censusComparator.compare(census1, census2) < 0){
                    censusList.set(j, census2);
                    censusList.set(j+1, census1);
                }
            }
        }
        return censusList;
    }
}


