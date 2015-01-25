import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author krzysztof
 */
public class QueryProtocol
{
	public QueryProtocol( Query query ) {
		this.query = query;
	}

	public Segment getSegment( Carrier carrier ) {
		Segment.Builder segmentBuilder = new Segment.Builder();
		try {
			openServerSocked( carrier ); //todo: change to SSL

			InputStream input = serverSocket.getInputStream();
			OutputStream output = serverSocket.getOutputStream();

			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( input ) );
			PrintWriter printWriter = new PrintWriter( new OutputStreamWriter( output ) );

			sendQuery( printWriter );

			String line = bufferedReader.readLine();
			ResponseCodes response = ResponseCodes.valueOf( line );

			if( response != ResponseCodes.OK ) return null;

			segmentBuilder.setCarrier( bufferedReader.readLine() );
			segmentBuilder.setFrom( bufferedReader.readLine() );
			segmentBuilder.setTo( bufferedReader.readLine() );
			segmentBuilder.setStartDate( LocalDate.parse( bufferedReader.readLine(), DateTimeUtils.YYYYMMDD ) );
			segmentBuilder.setStartTime( LocalTime.parse( bufferedReader.readLine(), DateTimeUtils.HHMM ) );
			segmentBuilder.setStopDate( LocalDate.parse( bufferedReader.readLine(), DateTimeUtils.YYYYMMDD ) );
			segmentBuilder.setStopTime( LocalTime.parse( bufferedReader.readLine(), DateTimeUtils.HHMM ) );

			return segmentBuilder.build();
		} catch( RuntimeException | IOException e ) {
			logger.error( e.getMessage(), e );
		}
		return null;
	}

	private void openSSLServerSocked( Carrier carrier ) throws IOException {
		logger.info( "Opening SSL server socked..." );
		SSLSocketFactory sslsocketfactory = ( SSLSocketFactory ) SSLSocketFactory.getDefault();
		this.serverSocket = sslsocketfactory.createSocket( carrier.getAddress(), carrier.getPort() );
		logger.info( "SSL Socked opened" );
	}

	private void openServerSocked( Carrier carrier ) throws IOException {
		logger.info( "Opening server socked..." );
		this.serverSocket = new Socket( carrier.getAddress(), carrier.getPort() );
		logger.info( "Socked opened" );
	}

	private void sendQuery( PrintWriter printWriter ) {
		ServerUtils.print( printWriter, query.getFrom() );
		ServerUtils.print( printWriter, query.getTo() );
		ServerUtils.print( printWriter, query.getDate().format( DateTimeUtils.YYYYMMDD ) );
		ServerUtils.print( printWriter, query.getTime().format( DateTimeUtils.HHMM ) );
	}

	private final Query query;
	private Socket serverSocket;
	private static final Logger logger = LogManager.getLogger( QueryProtocol.class );
}
