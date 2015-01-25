import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author krzysztof
 */
public class Validator
{
	public static boolean checkCity( String input ) {
		return input == null || input.length() > MAX_CHARS || !input.matches( "[A-Za-z- ]+" );
	}

	public static boolean checkDate( String input, DateTimeFormatter formatter ) {
		if( input == null || input.length() > MAX_CHARS || !input.matches( "[0-9-/]+" ) ) return true;
		try {
			LocalDate.parse( input, formatter );
			return false;
		} catch( DateTimeParseException e ) {
			return true;
		}
	}

	public static boolean checkTime( String input, DateTimeFormatter formatter ) {
		if( input == null || input.length() > MAX_CHARS || !input.matches( "[0-9-/:]+" ) ) return true;
		try {
			LocalTime.parse( input, formatter );
			return false;
		} catch( DateTimeParseException e ) {
			return true;
		}
	}

	public static boolean checkDateTime( String input, DateTimeFormatter formatter ) {
		if( input == null || input.length() > MAX_CHARS || !input.matches( "[0-9-/:]+" ) ) return true;
		try {
			LocalDateTime.parse( input, formatter );
			return false;
		} catch( DateTimeParseException e ) {
			return true;
		}
	}

	private static final int MAX_CHARS = 50;


}
