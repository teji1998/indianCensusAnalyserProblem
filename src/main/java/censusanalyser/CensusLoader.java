package censusanalyser;

import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {

	public Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country country, String... csvFilePath) throws CensusAnalyserException {
		if (country.equals(CensusAnalyser.Country.INDIA))
			return this.loadCensusData(IndiaCensusCSV.class, csvFilePath);
		else if (country.equals(CensusAnalyser.Country.US))
			return this.loadCensusData(USCensusCSV.class, csvFilePath);
		else throw new CensusAnalyserException("Invalid Country", CensusAnalyserException.ExceptionType.NOT_A_VALID_COUNTRY);
	}

	Map<String, CensusDAO> censusMap = new HashMap<String, CensusDAO>();
	private <E> Map<String, CensusDAO> loadCensusData(Class<E> censusCSVClass, String... csvFilePath) throws CensusAnalyserException {
		try {
			Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));
			ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			Iterator<E> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
			Iterable<E> censusCSVIterable = () -> censusCSVIterator;
			if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")){
				StreamSupport.stream(censusCSVIterable.spliterator(),false)
						.map(IndiaCensusCSV.class::cast)
						.forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
			} else if (censusCSVClass.getName().equals("censusanalyser.USCensusCSV")){
				StreamSupport.stream(censusCSVIterable.spliterator(),false)
						.map(USCensusCSV.class::cast)
						.forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
			}
			if (csvFilePath.length == 1) {
				return censusMap;
			}
			this.loadIndiaStateCode(censusMap, csvFilePath[1]);
			return censusMap;
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
		} catch (CSVBuilderException e) {
			throw new CensusAnalyserException(e.getMessage(), e.type.name());
		}
	}

	private int loadIndiaStateCode(Map<String, CensusDAO> censusMap, String csvFilePath) throws CensusAnalyserException {
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
			throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
		} catch (CSVBuilderException e) {
			throw new CensusAnalyserException(e.getMessage(), e.type.name());
		}
	}
}