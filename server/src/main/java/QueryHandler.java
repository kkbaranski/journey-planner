import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingResourceException;

/**
 * @author krzysztof
 */
public class QueryHandler implements Runnable
{


	public QueryHandler( Socket clientSocket ) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		logger.info( "Query handler started." );
		DistancesAccessor distances = new DistancesAccessor();
		distances.openDatabase();

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

			error = !distances.existsCity( line1 );
			error |= !distances.existsCity( line2 );

			if( error ) {
				ServerUtils.print( printWriter, ResponseCodes.NOTEXISTS );
				return;
			}

			Query query = new Query.Builder().setFrom( line1 )
			                                 .setTo( line2 )
			                                 .setDate( line3, DateTimeUtils.YYYYMMDD )
			                                 .setTime( line4, DateTimeUtils.HHMM )
			                                 .build();

			List<String> path = new PathCalculator( query.getFrom(), query.getTo() ).calculate();
			if( path == null || path.size() < 2 ) {
				ServerUtils.print( printWriter, ResponseCodes.NOCONNECTION );
				return;
			}

			logger.info( "PATH:" );
			for( String s : path ) logger.info( "  " + s );

			Route route = new RouteSearch( path ).search( LocalDateTime.of( query.getDate(), query.getTime() ) );

			logger.info( "I have route!" );
			ServerUtils.print( printWriter, route.getStatus() );
			if( route.getStatus() == ResponseCodes.OK ) {
				ServerUtils.print( printWriter, route );
			}


		} catch( MissingResourceException e ) {
			logger.error( e.getMessage(), e );
			ServerUtils.print( printWriter, ResponseCodes.BADQUERY.toString() );
		} catch( SocketTimeoutException e ) {
			logger.error( e.getMessage(), e );
			ServerUtils.print( printWriter, ResponseCodes.TIMEOUT.toString() );
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
			ServerUtils.print( printWriter, ResponseCodes.IOERROR.toString() );
		} finally {
			try {
				if( input != null ) input.close();
				if( output != null ) output.close();
				distances.closeDatabase();
			} catch( IOException e ) {
				logger.error( e.getMessage(), e );
			}
		}
	}


	private final Socket clientSocket;
	private static final Logger logger = LogManager.getLogger( QueryHandler.class );
}
