import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
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

		BufferedReader bufferedReader;
		PrintWriter printWriter = null;
		try {

			input = clientSocket.getInputStream();
			output = clientSocket.getOutputStream();
			bufferedReader = new BufferedReader( new InputStreamReader( input ) );
			printWriter = new PrintWriter( new OutputStreamWriter( output ) );

			String line1 = bufferedReader.readLine();
			String line2 = bufferedReader.readLine();
			String line3 = bufferedReader.readLine();
			String line4 = bufferedReader.readLine();

			boolean error;
			error = Validator.checkCity( line1 );
			error |= Validator.checkCity( line2 );
			error |= Validator.checkDate( line3, DateTimeUtils.YYYYMMDD );
			error |= Validator.checkTime( line4, DateTimeUtils.HHMM );

			if( error ) {
				ServerUtils.print( printWriter, ResponseCodes.BADFORMAT );
				return;
			}

			Query query = new Query.Builder().setFrom( line1 )
			                                 .setTo( line2 )
			                                 .setDate( line3, DateTimeUtils.YYYYMMDD )
			                                 .setTime( line4, DateTimeUtils.HHMM )
			                                 .build();

			Segment response = new QueryExecutor( query ).execute();
			if( response == null ) {
				logger.info( "No passing record" );
				ServerUtils.print( printWriter, ResponseCodes.NOTFOUND );
			} else {
				ServerUtils.print( printWriter, ResponseCodes.OK );
				ServerUtils.print( printWriter, response );
			}


		} catch( MissingResourceException e ) {
			logger.error( e.getMessage(), e );
			ServerUtils.print( printWriter, ResponseCodes.BADQUERY );
		} catch( SocketTimeoutException e ) {
			logger.error( e.getMessage(), e );
			ServerUtils.print( printWriter, ResponseCodes.TIMEOUT );
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
			ServerUtils.print( printWriter, ResponseCodes.IOERROR );
		} finally {
			try {
				if( input != null ) input.close();
				if( output != null ) output.close();
			} catch( IOException e ) {
				logger.error( e.getMessage(), e );
			}
		}
		logger.info( "Query handler stopped." );
	}


	private static final Logger logger = LogManager.getLogger( QueryHandler.class );
}
