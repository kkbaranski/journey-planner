import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.time.format.DateTimeFormatter;

/**
 * @author krzysztof
 */
public class QueryProtocol
{
	public QueryProtocol( Query query ) {
		this.query = query;
	}

	public Route getRoute() {
		Route route = null;

		InputStream input = null;
		OutputStream output = null;

		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;

		SSLSocket serverSocket;
		logger.info( "Opening server socked..." );
		SSLSocketFactory sslsocketfactory = ( SSLSocketFactory ) SSLSocketFactory.getDefault();
		try {
			serverSocket = ( SSLSocket ) sslsocketfactory.createSocket( Config.serverAddress(), Config.serverPort() );
		} catch( IOException e ) {
			logger.error( "Cannot create SSL client socket" );
			Utils.printError( "Problem with server connection." );
			return null;
		}
		logger.info( "Socked opened." );

		try {
			input = serverSocket.getInputStream();
			output = serverSocket.getOutputStream();

			bufferedReader = new BufferedReader( new InputStreamReader( input ) );
			printWriter = new PrintWriter( new OutputStreamWriter( output ) );

			sendQuery( printWriter, bufferedReader );
			downloadRoute(printWriter, bufferedReader);
		} catch( RuntimeException e ) {

		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}

	private void downloadRoute( PrintWriter printWriter, BufferedReader bufferedReader ) {
		//todo
	}

	private boolean sendQuery( PrintWriter printWriter, BufferedReader bufferedReader ) {
		boolean error;
		error = sendFrom( printWriter, bufferedReader );
		if( error ) return true;
		error = sendTo( printWriter, bufferedReader );
		if( error ) return true;
		error = sendDate( printWriter, bufferedReader );
		if( error ) return true;
		error = sendTime( printWriter, bufferedReader );
		if( error ) return true;
		return false;
	}

	private boolean sendFrom( PrintWriter printWriter, BufferedReader bufferedReader ) {
		logger.info( "sending from = " + query.getFrom() );
		printWriter.println( query.getFrom() );
		printWriter.flush();
		String response;
		try {
			response = bufferedReader.readLine();
		} catch( IOException e ) {
			logger.error( "IOException: cannot get response" );
			return true;
		}
		ResponseCodes code = ResponseCodes.valueOf( response );
		switch( code ) {
			case OK:
				logger.info( "response: OK" );
				break;
			case BADFORMAT:
				logger.error( "response: BADFORMAT" );
				Utils.printError( "Invalid city name" );
				return true;
			case NOTEXISTS:
				logger.info( "response: NOTEXISTS" );
				Utils.printError( "City '" +query.getFrom()+"' does not exists in database" );
				return true;
			default:
				logger.error( "Unknown response code" );
				return true;
		}
		return false;
	}

	private boolean sendTo( PrintWriter printWriter, BufferedReader bufferedReader ) {
		logger.info( "sending to = " + query.getTo() );
		printWriter.println( query.getTo() );
		printWriter.flush();
		String response;
		try {
			response = bufferedReader.readLine();
		} catch( IOException e ) {
			logger.error( "IOException: cannot get response" );
			return true;
		}
		ResponseCodes code = ResponseCodes.valueOf( response );
		switch( code ) {
			case OK:
				logger.info( "response: OK" );
				break;
			case BADFORMAT:
				logger.error( "response: BADFORMAT" );
				Utils.printError( "Invalid city name" );
				return true;
			case NOTEXISTS:
				logger.info( "response: NOTEXISTS" );
				Utils.printError( "City '" +query.getTo()+"' does not exists in database" );
				return true;
			default:
				logger.error( "Unknown response code" );
				return true;
		}
		return false;
	}

	private boolean sendDate( PrintWriter printWriter, BufferedReader bufferedReader ) {
		logger.info( "sending date = " + query.getDate() );
		printWriter.println( query.getDate().format( dateFormatter ) );
		printWriter.flush();
		String response;
		try {
			response = bufferedReader.readLine();
		} catch( IOException e ) {
			logger.error( "IOException: cannot get response" );
			return true;
		}
		ResponseCodes code = ResponseCodes.valueOf( response );
		switch( code ) {
			case OK:
				logger.info( "response: OK" );
				break;
			case BADFORMAT:
				logger.error( "response: BADFORMAT" );
				Utils.printError( "Invalid date format" );
				return true;
			default:
				logger.error( "Unknown response code" );
				return true;
		}
		return false;
	}

	private boolean sendTime( PrintWriter printWriter, BufferedReader bufferedReader ) {
		logger.info( "sending time = " + query.getTime() );
		printWriter.println( query.getTime().format( timeFormatter ) );
		printWriter.flush();
		String response;
		try {
			response = bufferedReader.readLine();
		} catch( IOException e ) {
			logger.error( "IOException: cannot get response" );
			return true;
		}
		ResponseCodes code = ResponseCodes.valueOf( response );
		switch( code ) {
			case OK:
				logger.info( "response: OK" );
				break;
			case BADFORMAT:
				logger.error( "response: BADFORMAT" );
				Utils.printError( "Invalid time format" );
				return true;
			default:
				logger.error( "Unknown response code" );
				return true;
		}
		return false;
	}


	private final Query query;
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( "yyyyMMdd" );
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern( "HHmm" );
	private static final Logger logger = LogManager.getLogger( QueryProtocol.class );
}
