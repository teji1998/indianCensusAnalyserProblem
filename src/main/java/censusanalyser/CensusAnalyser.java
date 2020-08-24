package censusanalyser;

import com.google.gson.Gson;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;


public class CensusAnalyser {

    List<IndiaCensusDAO> censusList = null;

    public CensusAnalyser() {
        this.censusList = new ArrayList<IndiaCensusDAO>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> censusFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while (censusFileIterator.hasNext()) {
                this.censusList.add(new IndiaCensusDAO(censusFileIterator.next()));
            }
            return this.censusList.size();
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
            Iterator<IndiaStateCodeCSV> stateCodeFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            while (stateCodeFileIterator.hasNext()) {
                this.censusList.add(new IndiaCensusDAO(stateCodeFileIterator.next()));
            }
            return this.censusList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
        }catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        censusList = this.sort(censusList, censusComparator);
        String sortedStateCensus = new Gson().toJson(this.censusList);
        return sortedStateCensus;
    }

    public String getPopulationWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.population );
        censusList = this.sortInDescendingOrder(censusList, censusComparator);
        String sortedPopulationCensus = new Gson().toJson(this.censusList);
        return sortedPopulationCensus;
    }

    public String getStatePopulationDensityWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.densityPerSqKm);
        censusList = this.sortInDescendingOrder(censusList, censusComparator);
        String sortedPopulationDensityCensus = new Gson().toJson(this.censusList);
        return sortedPopulationDensityCensus;
    }

    public String getStateAreaWiseSortedCensusData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.areaInSqKm);
        censusList = this.sortInDescendingOrder(censusList, censusComparator);
        String sortedAreaWiseCensus = new Gson().toJson(censusList);
        return sortedAreaWiseCensus;
    }

    public String getStateCodeWiseSortedData() throws CensusAnalyserException {
        if (censusList == null || censusList.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusDAO> censusComparator = Comparator.comparing(census -> census.stateCode);
        censusList = this.sort(censusList, censusComparator);
        String sortedStateCode = new Gson().toJson(censusList);
        return sortedStateCode;
    }

    private static <E> int getCount(Iterator<E> iterator){
        Iterable<E> csvIterable = () -> iterator;
        return (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
    }

    private static <E> List<E> sort(List<E> censusList, Comparator<E> censusComparator) {
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

    private static <E> List<E> sortInDescendingOrder(List<E> censusList, Comparator<E> censusComparator) {
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


