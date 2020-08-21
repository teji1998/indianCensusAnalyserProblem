package censusanalyser;

public class CSVBuilderException extends Exception {
	enum ExceptionType {
		CENSUS_FILE_PROBLEM,
		INVALID_FILE_TYPE_OR_DELIMITER_OR_HEADER,
		UNABLE_TO_PARSE
	}

	ExceptionType type;

	public CSVBuilderException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}
}
