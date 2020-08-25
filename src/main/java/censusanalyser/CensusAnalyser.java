package censusanalyser;

import com.google.gson.Gson;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class CensusAnalyser {
    Map<String, CensusDAO> censusMap;

    public CensusAnalyser() {
        this.censusMap = new HashMap<String, CensusDAO>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> censusCSVIterable = () -> censusCSVIterator;
            StreamSupport.stream(censusCSVIterable.spliterator(),false)
                    .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            return this.censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
        }catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public int loadIndiaStateCode(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> stateCodeCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> censusCSVIterable = () -> stateCodeCSVIterator;
            StreamSupport.stream(censusCSVIterable.spliterator(),false)
                         .filter(csvState -> censusMap.get(csvState.stateName) != null)
                         .forEach(censusCSV -> censusMap.get(censusCSV.stateName).state = censusCSV.stateCode);
            return this.censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
        }catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }


    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<USCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, USCensusCSV.class);
            Iterable<USCensusCSV> censusCSVIterable = () -> censusCSVIterator;
            StreamSupport.stream(censusCSVIterable.spliterator(),false)
                    .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            return this.censusMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
        }catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
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
        if ( censusMap== null || censusMap.size() == 0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.populationDensity);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder( censusComparator, censusDAOList);
        String sortedPopulationDensityCensus = new Gson().toJson(censusDAOList);
        return sortedPopulationDensityCensus;
    }

    public String getStateAreaWiseSortedCensusData() throws CensusAnalyserException {
        if ( censusMap == null || censusMap.size() == 0 ) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.totalArea);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder( censusComparator, censusDAOList);
        String sortedAreaWiseCensus = new Gson().toJson(censusDAOList);
        return sortedAreaWiseCensus;
    }

    public String getStateCodeWiseSortedData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.stateCode);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        this.sortInDescendingOrder( censusComparator, censusDAOList);
        String sortedStateCode = new Gson().toJson(censusDAOList);
        return sortedStateCode;
    }

    public String getUSCensusPopulationWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap  == null || censusMap .size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> usCensusComparator = Comparator.comparing(census -> census.population);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        censusDAOList = sortInDescendingOrder(usCensusComparator, censusDAOList);
        return new Gson().toJson(censusDAOList);
    }

    public String getUSCensusPopulationDensityWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap  == null || censusMap .size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> usCensusComparator = Comparator.comparing(census -> census.populationDensity);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        censusDAOList = sortInDescendingOrder(usCensusComparator, censusDAOList);
        return new Gson().toJson(censusDAOList);
    }
    public String getUSCensusAreaWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap  == null || censusMap .size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> usCensusComparator = Comparator.comparing(census -> census.totalArea);
        List<CensusDAO> censusDAOList = censusMap.values().stream().collect(Collectors.toList());
        censusDAOList = sortInDescendingOrder(usCensusComparator, censusDAOList);
        return new Gson().toJson(censusDAOList);
    }


    private static <E> List<E> sort( Comparator<E> censusComparator, List<E> censusList ) {
        for ( int i = 0; i < censusList.size()-1; i++ ) {
            for ( int j =0; j< censusList.size() -i -1; j++ ) {
                E census1 = censusList.get(j);
                E census2 = censusList.get(j+1);
                if ( censusComparator.compare( census1, census2 ) > 0 ){
                    censusList.set( j, census2 );
                    censusList.set( j+1, census1 );
                }
            }
        }
        return censusList;
    }

    private static <E> List<E> sortInDescendingOrder( Comparator<E> censusComparator, List<E> censusList ) {
        for ( int i = 0; i < censusList.size()-1; i++ ) {
            for ( int j =0; j< censusList.size() -i -1; j++ ) {
                E census1 = censusList.get(j);
                E census2 = censusList.get(j+1);
                if ( censusComparator.compare( census1, census2 ) < 0 ){
                    censusList.set( j, census2 );
                    censusList.set( j+1, census1 );
                }
            }
        }
        return censusList;
    }

}


