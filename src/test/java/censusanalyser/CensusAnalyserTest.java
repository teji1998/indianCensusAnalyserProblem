package censusanalyser;

import org.junit.Assert; import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH =  "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_TYPE_PATH =  "./src/test/resources/IndiaStateCensusData.txt";
    private static final String INVALID_DELIMITER_FILE_PATH =  "./src/test/resources/invalidDelimitersIndiaStateCensusData.csv";
    private static final String INVALID_HEADER_FILE_PATH =  "./src/test/resources/invalidHeadersIndiaStateCensusData.csv";

    //To obtain the number of record
    @Test
    public void givenIndianCensusDataCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29,numOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    //To test for a wrong file path
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongPath_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule =  ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    //To test for a wrong file type
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongFileType_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule =  ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_TYPE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To test for invalid delimiters
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongDelimiters_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule =  ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadIndiaCensusData(INVALID_DELIMITER_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To test for invalid headers
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongHeaders_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadIndiaCensusData(INVALID_HEADER_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }
}
