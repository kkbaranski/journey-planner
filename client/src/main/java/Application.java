import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author krzysztof
 */
public class Application
{
	public static void main( String[] args ) {
		logger.info( "==CLIENT START==" );

		Utils.printStartPage();
		do {
			Query query = getQuery();
			logger.info( query );

			Route route = RouteUtils.getRoute( query );
		} while( nextSearch() );


		logger.info( "==CLIENT STOP==" );
	}

	private static Query getQuery() {
		System.out.println( "" );
		System.out.println( "    SEARCH CONNECTION    " );
		System.out.println( "  ---------------------  " );
		String from = getFrom();
		String to = getTo();
		LocalDate date = getDate();
		LocalTime time = getTime();
		return new Query.Builder( from, to ).setDate( date ).setTime( time ).build();
	}

	private static String getFrom() {
		String result;
		boolean ok;
		do {
			String line = Utils.getLineFromStdin( "  FROM" );
			ok = Validator.checkFrom( line );
			if( !ok ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid city name. Try again. Example: Zakopane" );
			}
			result = line;
		} while( !ok );
		return result;
	}

	private static String getTo() {
		String result;
		boolean ok;
		do {
			String line = Utils.getLineFromStdin( "    TO" );
			ok = Validator.checkTo( line );
			if( !ok ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid city name. Try again. Example: Gdansk" );
			}
			result = line;
		} while( !ok );
		return result;
	}

	private static LocalDate getDate() {
		LocalDate result = null;
		boolean ok;
		do {
			String line = Utils.getLineFromStdin( "  DATE" );
			ok = Validator.checkIsoDate( line );
			if( !ok ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid date format. Try again. Example: " + LocalDate.now().format( dateFormatter ) );
			} else {
				result = LocalDate.parse( line, dateFormatter );
			}
		} while( !ok );
		return result;
	}

	private static LocalTime getTime() {
		LocalTime result = null;
		boolean ok;
		do {
			String line = Utils.getLineFromStdin( "  TIME" );
			ok = Validator.checkIsoTime( line );
			if( !ok ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid time format. Try again. Example: " + LocalTime.now().format( timeFormatter ) );
			} else {
				result = LocalTime.parse( line, timeFormatter );
			}
		} while( !ok );
		return result;
	}

	private static boolean nextSearch() {
		String ret = Utils.getLineFromStdin( "Would you like to find a new connection? If you want, type 'yes'" );
		return ret.equals( "yes" );
	}



	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern( "HH:mm" );
	private static final Logger logger = LogManager.getLogger( Application.class );
}
