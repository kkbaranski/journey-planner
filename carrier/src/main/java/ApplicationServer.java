import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.*;

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
			Socket clientSocket;
			try {
				clientSocket = this.serverSocket.accept();
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
			this.serverSocket = new ServerSocket( this.serverPort );
		} catch( IOException e ) {
			throw new RuntimeException( "Cannot open port " + this.serverPort, e );
		}
		logger.info( "Socked opened." );
	}


	public void executeTask( Runnable c ) {
		service.execute( c );
	}

	private final int serverPort = Config.port();
	private boolean isStopped = false;
	private ServerSocket serverSocket = null;
	private final ExecutorService service = Executors.newFixedThreadPool( Config.maxNumberOfExecutors() );
	private static final Logger logger = LogManager.getLogger( ApplicationServer.class );
}
