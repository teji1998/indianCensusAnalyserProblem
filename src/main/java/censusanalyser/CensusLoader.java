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

	public  <E> Map<String, CensusDAO> loadCensusData(String csvFilePath, Class<E> censusCSVClass) throws CensusAnalyserException {
		Map<String, CensusDAO> censusMap = new HashMap<String, CensusDAO>();
		try {
			Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
			ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
			Iterator<E> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
			Iterable<E> censusCSVIterable = () -> censusCSVIterator;
			if(censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")){
				StreamSupport.stream(censusCSVIterable.spliterator(),false)
						.map(IndiaCensusCSV.class::cast)
						.forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
			} else if(censusCSVClass.getName().equals("censusanalyser.USCensusCSV")){
				StreamSupport.stream(censusCSVIterable.spliterator(),false)
						.map(USCensusCSV.class::cast)
						.forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
			}
			return censusMap;
		} catch (IOException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
		} catch (RuntimeException e) {
			throw new CensusAnalyserException(e.getMessage(),
					CensusAnalyserException.ExceptionType.INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER);
		} catch (CSVBuilderException e) {
			throw new CensusAnalyserException(e.getMessage(),e.type.name());
		}
	}
}
