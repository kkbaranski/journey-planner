package client.protocols;

import client.config.Config;
import commons.journey.Query;
import commons.journey.Route;
import commons.journey.Segment;
import commons.utils.DateTimeUtils;
import commons.utils.ResponseCodes;
import commons.utils.ServerUtils;
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

	public Route getRoute() {
		route = new Route( ResponseCodes.NOTFOUND );
		try {
			openSSLServerSocked();

			InputStream input = serverSocket.getInputStream();
			OutputStream output = serverSocket.getOutputStream();

			BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( input ) );
			PrintWriter printWriter = new PrintWriter( new OutputStreamWriter( output ) );

			sendQuery( printWriter );

			String line = bufferedReader.readLine();
			ResponseCodes response = ResponseCodes.valueOf( line );
			route.setStatus( response );

			if( response != ResponseCodes.OK ) return route;
			line = bufferedReader.readLine();
			int numberOfSegments = Integer.parseInt( line );

			for( int i = 0; i < numberOfSegments; ++i ) {
				Segment.Builder segmentBuilder = new Segment.Builder();
				segmentBuilder.setCarrier( bufferedReader.readLine() );
				segmentBuilder.setFrom( bufferedReader.readLine() );
				segmentBuilder.setTo( bufferedReader.readLine() );
				segmentBuilder.setStartDate( LocalDate.parse( bufferedReader.readLine(), DateTimeUtils.YYYYMMDD ) );
				segmentBuilder.setStartTime( LocalTime.parse( bufferedReader.readLine(), DateTimeUtils.HHMM ) );
				segmentBuilder.setStopDate( LocalDate.parse( bufferedReader.readLine(), DateTimeUtils.YYYYMMDD ) );
				segmentBuilder.setStopTime( LocalTime.parse( bufferedReader.readLine(), DateTimeUtils.HHMM ) );
				route.addSegment( segmentBuilder.build() );
			}
		} catch( RuntimeException e ) {
			logger.error( e.getMessage(), e );
			route.setStatus( ResponseCodes.UNKNOWNERROR );
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
			route.setStatus( ResponseCodes.IOERROR );
		}
		return route;
	}

	private void openSSLServerSocked() throws IOException {
		logger.info( "Opening SSL server socked..." );
		SSLSocketFactory sslsocketfactory = ( SSLSocketFactory ) SSLSocketFactory.getDefault();
		this.serverSocket = sslsocketfactory.createSocket( serverAddress, serverPort );
		logger.info( "SSL Socked opened" );
	}

	private void openServerSocked() throws IOException {
		logger.info( "Opening server socked..." );
		this.serverSocket = new Socket( serverAddress, serverPort );
		logger.info( "SSL Socked opened" );
	}

	private void sendQuery( PrintWriter printWriter ) {
		ServerUtils.print( printWriter, query.getFrom() );
		ServerUtils.print( printWriter, query.getTo() );
		ServerUtils.print( printWriter, query.getDate().format( DateTimeUtils.YYYYMMDD ) );
		ServerUtils.print( printWriter, query.getTime().format( DateTimeUtils.HHMM ) );
	}

	private Route route;
	private final Query query;
	private Socket serverSocket;
	private final int serverPort = Config.serverPort();
	private final String serverAddress = Config.serverAddress();
	private static final Logger logger = LogManager.getLogger( QueryProtocol.class );
}
