import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
			printRoute( route );
		} while( nextSearch() );


		logger.info( "==CLIENT STOP==" );
	}

	private static void printRoute( Route route ) {
		ResponseCodes status = route.getStatus();
		System.out.println( "" );
		if( status != ResponseCodes.OK ) {
			Utils.printError( status.reason() );
			System.out.println( "Connection has not found!" );
			return;
		} else {
			System.out.println( "Connection has found!" );
		}

		int numerator = 1;
		for( Segment seg : route ) {
			System.out.println( numerator + ") " + seg.getFrom() + " -> " + seg.getTo() );
			System.out.println( "    departure: " +
			                    LocalDateTime.of( seg.getStartDate(), seg.getStartTime() )
			                                 .format( DateTimeUtils.YYYY_MM_DD_HH_MM ) );
			System.out.println( "      arrival: " +
			                    LocalDateTime.of( seg.getStopDate(), seg.getStopTime() )
			                                 .format( DateTimeUtils.YYYY_MM_DD_HH_MM ) );
			System.out.println( "      carrier: " + seg.getCarrier() );
			++numerator;
		}

		System.out.println( "" );
		System.out.println( "___________________________________" );
		System.out.println( "  TOTAL TIME: " +
		                    ( route.getTotalTime() / 3600 ) +
		                    " hours " +
		                    ( ( route.getTotalTime() % 3600 ) / 60 ) +
		                    " minutes" );
		System.out.println( "" );
	}

	private static Query getQuery() {
		System.out.println( "" );
		System.out.println( "    SEARCH CONNECTION    " );
		System.out.println( "  ---------------------  " );
		String from = getFrom();
		String to = getTo( from );
		LocalDate date = getDate();
		LocalTime time = getTime();
		return new Query.Builder( from, to ).setDate( date ).setTime( time ).build();
	}

	private static String getFrom() {
		String result = null;
		boolean error;
		do {
			String line = Utils.getLineFromStdin( "  FROM" );
			error = Validator.checkCity( line );
			if( error ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid city name. Try again. Example: Zakopane" );
			} else {
				result = line;
			}
		} while( error );
		return result;
	}

	private static String getTo( String from ) {
		String result = null;
		boolean error;
		do {
			String line = Utils.getLineFromStdin( "    TO" );
			error = Validator.checkCity( line );
			if( error ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid city name. Try again. Example: Gdansk" );
			} else if( line.equals( from ) ) {
				error = true;
				logger.info( "The same city" );
				Utils.printError( "Target city must be different. Try again." );
			} else {
				result = line;
			}
		} while( error );
		return result;
	}

	private static LocalDate getDate() {
		LocalDate result = null;
		boolean error;
		do {
			String line = Utils.getLineFromStdin( "  DATE" );
			error = Validator.checkDate( line, DateTimeUtils.YYYY_MM_DD );
			if( error ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid date format. Try again. Example: " +
				                  LocalDate.now().format( DateTimeUtils.YYYY_MM_DD ) );
			} else {
				result = LocalDate.parse( line, DateTimeUtils.YYYY_MM_DD );
			}
		} while( error );
		return result;
	}

	private static LocalTime getTime() {
		LocalTime result = null;
		boolean error;
		do {
			String line = Utils.getLineFromStdin( "  TIME" );
			error = Validator.checkTime( line, DateTimeUtils.HH_MM );
			if( error ) {
				logger.info( "Validation error for '" + line + "'" );
				Utils.printError( "Invalid time format. Try again. Example: " +
				                  LocalTime.now().format( DateTimeUtils.HH_MM ) );
			} else {
				result = LocalTime.parse( line, DateTimeUtils.HH_MM );
			}
		} while( error );
		return result;
	}

	private static boolean nextSearch() {
		String ret = Utils.getLineFromStdin( "Would you like to find a new connection? If so, type 'yes'" );
		return ret.equals( "yes" );
	}

	private static final Logger logger = LogManager.getLogger( Application.class );
}
