import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.MissingResourceException;

/**
 * @author krzysztof
 */
public class QueryHandler implements Runnable
{
	private final Socket clientSocket;

	public QueryHandler( Socket clientSocket ) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		logger.info( "Query handler started." );

		InputStream input = null;
		OutputStream output = null;

		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		try {

			input = clientSocket.getInputStream();
			output = clientSocket.getOutputStream();

			bufferedReader = new BufferedReader( new InputStreamReader( input ) );
			printWriter = new PrintWriter( new OutputStreamWriter( output ) );

			Query.Builder queryBuilder = new Query.Builder();

			boolean error;
			error = getFrom( queryBuilder, printWriter, bufferedReader );
			if(error) return;
			error = getTo( queryBuilder, printWriter, bufferedReader );
			if(error) return;
			error = getDate( queryBuilder, printWriter, bufferedReader );
			if(error) return;
			error = getTime( queryBuilder, printWriter, bufferedReader );
			if(error) return;

			Query query = queryBuilder.build();
			logger.info( query );

			String response = new QueryExecutor( query ).execute();
			if( response == null ) {
				logger.info( "No passing record" );
				print( printWriter, ResponseCodes.NOTFOUND.toString() );
			}


		} catch( MissingResourceException e ) {
			logger.error( "Query cannot be built" );
			print( printWriter, ResponseCodes.BADQUERY.toString() );
		} catch( SocketTimeoutException e ) {
			logger.error( "Query handler timeout" );
			print( printWriter, ResponseCodes.TIMEOUT.toString() );
		} catch( IOException e ) {
			logger.error( "Query handler exception" );
			e.printStackTrace();
		} finally {
			try {
				if( input != null ) input.close();
				if( output != null ) output.close();
			} catch( IOException e ) {
				logger.error( "Problem with closing streams" );
				e.printStackTrace();
			}
		}
	}

	private boolean getFrom( Query.Builder queryBuilder, PrintWriter printWriter, BufferedReader bufferedReader ) throws IOException {
		String line = bufferedReader.readLine();
		if( !Validator.checkFrom( line ) ) {
			logger.error( "Validation error for '" + line + "'" );
			print( printWriter, ResponseCodes.BADFORMAT.toString() );
			return true;
		}
		queryBuilder.setFrom( line );
		logger.info( "OK from: " + line );
		print( printWriter, ResponseCodes.OK.toString() );
		return false;
	}

	private boolean getTo( Query.Builder queryBuilder, PrintWriter printWriter, BufferedReader bufferedReader ) throws IOException {
		String line = bufferedReader.readLine();
		if( !Validator.checkTo( line ) ) {
			logger.error( "Validation error for '" + line + "'" );
			print( printWriter, ResponseCodes.BADFORMAT.toString() );
			return true;
		}
		queryBuilder.setTo( line );
		logger.info( "OK to: " + line );
		print( printWriter, ResponseCodes.OK.toString() );
		return false;
	}

	private boolean getDate( Query.Builder queryBuilder, PrintWriter printWriter, BufferedReader bufferedReader ) throws IOException {
		String line = bufferedReader.readLine();
		if( !Validator.checkDate( line ) ) {
			logger.error( "Validation error for '" + line + "'" );
			print( printWriter, ResponseCodes.BADFORMAT.toString() );
			return true;
		}
		LocalDate date;
		try {
			date = LocalDate.parse( line, dateFormatter );
		} catch( DateTimeParseException e ) {
			logger.error( "Date cannot be parsed [" + line + "]" );
			print( printWriter, ResponseCodes.BADFORMAT.toString() );
			return true;
		}
		queryBuilder.setDate( date );
		logger.info( "OK date: " + date );
		print( printWriter, ResponseCodes.OK.toString() );
		return false;
	}

	private boolean getTime( Query.Builder queryBuilder, PrintWriter printWriter, BufferedReader bufferedReader ) throws IOException {
		String line = bufferedReader.readLine();
		if( !Validator.checkTime( line ) ) {
			logger.error( "Validation error for '" + line + "'" );
			print( printWriter, ResponseCodes.BADFORMAT.toString() );
			return true;
		}
		LocalTime time;
		try {
			time = LocalTime.parse( line, timeFormatter );
		} catch( DateTimeParseException e ) {
			logger.error( "Time cannot be parsed [" + line + "]" );
			print( printWriter, ResponseCodes.BADFORMAT.toString() );
			return true;
		}
		queryBuilder.setTime( time );
		logger.info( "OK time: " + time );
		print( printWriter, ResponseCodes.OK.toString() );
		return false;
	}

	public void print( PrintWriter writer, String text ) {
		if( writer == null ) return;
		synchronized( writer ) {
			writer.println( text );
			writer.flush();
		}
	}

	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( "yyyyMMdd" );
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern( "HHmm" );
	private static final Logger logger = LogManager.getLogger( QueryHandler.class );
}
