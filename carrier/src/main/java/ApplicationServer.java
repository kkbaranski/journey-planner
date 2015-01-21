import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author krzysztof
 */
public class ApplicationServer implements Runnable
{
	@Override
	public void run() {
		logger.info( "Server started" );

		openServerSocket();
		while( !isStopped() ) {
			SSLSocket clientSocket;
			try {
				clientSocket = ( SSLSocket ) this.serverSocket.accept();
			} catch( IOException e ) {
				if( isStopped() ) {
					logger.info( "Server stopped" );
					break;
				}
				throw new RuntimeException( "Error accepting client connection", e );
			}
			try {
				clientSocket.setSoTimeout( Config.sessionTimeSeconds() );
			} catch( SocketException e ) {
				e.printStackTrace();
			}
			executeTask( new QueryHandler( clientSocket ) );
		}
		this.service.shutdown();
		logger.info( "Server stopped." );
	}

	public synchronized void stop() {
		logger.info( "Stopping server..." );
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch( IOException e ) {
			throw new RuntimeException( "Error closing server", e );
		}
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	private void openServerSocket() {
		logger.info( "Opening server socked..." );
		try {
			SSLServerSocketFactory sslserversocketfactory = ( SSLServerSocketFactory ) SSLServerSocketFactory.getDefault();
			this.serverSocket = ( SSLServerSocket ) sslserversocketfactory.createServerSocket( this.serverPort );
		} catch( IOException e ) {
			logger.error( "Cannot open port " + this.serverPort );
			return;
		}
		logger.info( "Socked opened." );
	}


	public void executeTask( Runnable c ) {
		service.execute( c );
	}

	private final int serverPort = Config.port();
	private boolean isStopped = false;
	private SSLServerSocket serverSocket = null;
	private final ExecutorService service = Executors.newFixedThreadPool( Config.maxNumberOfExecutors() );
	private static final Logger logger = LogManager.getLogger( ApplicationServer.class );
}
