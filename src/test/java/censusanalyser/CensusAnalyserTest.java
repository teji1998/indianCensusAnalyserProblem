package censusanalyser;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_TYPE_PATH = "./src/test/resources/IndiaStateCensusData.txt";
    private static final String INVALID_DELIMITER_FILE_PATH = "./src/test/resources/invalidDelimitersIndiaStateCensusData.csv";
    private static final String INVALID_HEADER_FILE_PATH = "./src/test/resources/invalidHeadersIndiaStateCensusData.csv";
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_STATE_CODE_CSV_FILE_PATH = "./src/main/resources/IndiaStateCode.csv";
    private static final String WRONG_STATE_CODE_CSV_FILE_TYPE_PATH = "./src/test/resources/IndiaStateCode.txt";
    private static final String INVALID_STATE_CODE_CSV_DELIMITER_FILE_PATH = "./src/test/resources/invalidDelimitersIndiaStateCode.csv";
    private static final String INVALID_STATE_C0DE_CSV_HEADER_FILE_PATH = "./src/test/resources/invalidHeadersIndiaStateCode.csv";
    private static final String US_CENSUS_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";

    //To obtain the number of records in India census data
    @Test
    public void givenIndianCensusDataCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numberOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numberOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    //To test for a wrong file path
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongPath_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    //To test for a wrong file type
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongFileType_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_CSV_FILE_TYPE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To test for invalid delimiters
    @Test
    public void givenIndianCensusDataCSVFile_whenWithWrongDelimiters_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INVALID_DELIMITER_FILE_PATH);
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
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INVALID_HEADER_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To sort the India census data by states alphabetically
    @Test
    public void givenIndianCensusData_whenSortedByState_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String stateWiseSortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(stateWiseSortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("AP", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To sort the India census data populationwise from most populous one to least populous
    @Test
    public void givenIndianCensusCSVFile_whenSortedByPopulation_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String statePopulationWiseSortedCensusData = censusAnalyser.getPopulationWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(statePopulationWiseSortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(199812341, censusCSV[0].population);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To sort the India census data by population density from most populous one to least populous
    @Test
    public void givenIndianCensusCSVFile_whenSortedByPopulationDensity_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH);
            String stateWisePopulationDensitySortedCensusData = censusAnalyser.getStatePopulationDensityWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(stateWisePopulationDensitySortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Bihar", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To sort the India census data areawise from most populous one to least populous
    @Test
    public void givenIndianCensusCSVFile_whenSortedByArea_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH);
            String areaWiseSortedCensusData = censusAnalyser.getStateAreaWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(areaWiseSortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Rajasthan", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To obtain the number of record in India state code CSV file
    @Test
    public void givenIndiaStateCodeCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numberOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_STATE_CODE_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(37, numberOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    //To test for a wrong file path
    @Test
    public void givenIndianStateCodeCSVFile_whenWithWrongPath_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_STATE_CODE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    //To test for a wrong file type
    @Test
    public void givenIndianStateCodeCSVFile_whenWithWrongFileType_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_STATE_CODE_CSV_FILE_TYPE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To test for invalid delimiters
    @Test
    public void givenIndianStateCodeCSVFile_whenWithWrongDelimiters_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INVALID_STATE_CODE_CSV_DELIMITER_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To test for invalid headers
    @Test
    public void givenIndianStateCodeCSVFile_whenWithWrongHeaders_shouldThrowException() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        ExpectedException exceptionRule = ExpectedException.none();
        exceptionRule.expect(CensusAnalyserException.class);
        try {
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INVALID_STATE_C0DE_CSV_HEADER_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER, e.type);
        }
    }

    //To sort the indian census data by state code in alphabetical order
    @Test
    public void givenIndianStateCode_WhenSortedOnStateCode_ShouldReturnSortedResult() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateCodeWiseSortedData();
            IndiaStateCodeCSV[] censusCSV =  new Gson().fromJson(sortedCensusData, IndiaStateCodeCSV[].class);
            Assert.assertEquals("AD", censusCSV[0].stateCode);
        }catch (CensusAnalyserException e ) { }
    }

    //To get the number of records of US census data
    @Test
    public void givenUSCensusData_shouldReturnCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numberOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(51, numberOfRecords);
        } catch (CensusAnalyserException e) { }
    }

    //To sort US census data by state from most populous one to the least one
    @Test
    public void givenUSCensusData_whenSortedByState_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getUSCensusStateWiseSortedData();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("Wyoming", usCensusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To sort US census data populationwise from most populous state to least one
    @Test
    public void givenUSCensusData_whenSortedByPopulation_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getPopulationWiseSortedCensusData();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals(37253956, usCensusCSV[0].population);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To sort the US census data by population density from most populous to least populous one
    @Test
    public void givenUSCensusData_whenSortedByPopulationDensity_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStatePopulationDensityWiseSortedCensusData();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals("District of Columbia", usCensusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    // To sort the US census data areawise from most populous one to least populous
    @Test
    public void givenUSCensusData_whenSortedByArea_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String usCensusData = censusAnalyser.getStateAreaWiseSortedCensusData();
            USCensusCSV[] usCensusCSV = new Gson().fromJson(usCensusData, USCensusCSV[].class);
            Assert.assertEquals(1723338.01, usCensusCSV[0].totalArea, 1);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    //To find the most populous state in India and US census data on basis of population density
    @Test
    public void givenUSAndIndiaCensusData_whenSortedByPopulationDensity_shouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_CSV_FILE_PATH);
            String usCensusData = censusAnalyser.getStatePopulationDensityWiseSortedCensusData();
            USCensusCSV[] usCensusCSV =  new Gson().fromJson(usCensusData, USCensusCSV[].class);
            Assert.assertEquals("District of Columbia", usCensusCSV[0].state);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH);
            String indiaCensusData = censusAnalyser.getStatePopulationDensityWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusCSV =  new Gson().fromJson(indiaCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Bihar", indiaCensusCSV[0].state);
        } catch (CensusAnalyserException e ) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }
}
