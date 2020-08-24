package censusanalyser;

import com.google.gson.Gson;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;


public class CensusAnalyser {

    List<IndiaCensusCSV> censusCSVList = null;

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException { List<IndiaCensusCSV> censusCSVList = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
             censusCSVList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
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
	        List<IndiaCensusCSV> censusCSVList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
	        return censusCSVList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
        }catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),e.type.name());
        }
    }


    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusCSVList == null || censusCSVList.size() == 0){
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
        sort(censusComparator);
        return new Gson().toJson(censusCSVList);
    }

    private void sort(Comparator<IndiaCensusCSV> csvComparator) {
        for ( int i = 0; i < censusCSVList.size() - 1; i++ ){
            for ( int j = 0; j < censusCSVList.size() - i - 1; j++ ) {
                IndiaCensusCSV census1 = censusCSVList.get(j);
                IndiaCensusCSV census2 = censusCSVList.get(j + 1);
                if ( csvComparator.compare( census1, census2 ) > 0 ) {
                    censusCSVList.set(j,census2);
                    censusCSVList.set(j+1, census1);
                }
            }
        }
    }
}