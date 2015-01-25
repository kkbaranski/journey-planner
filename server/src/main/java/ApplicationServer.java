import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author krzysztof
 */
public class ApplicationServer implements Runnable
{
	@Override
	public void run() {
		try {
			logger.info( "Server started" );
			openServerSocket();//todo: change to SSL
			while( !isStopped() ) {
				Socket clientSocket = this.serverSocket.accept();
				clientSocket.setSoTimeout( Config.sessionTimeSeconds() );
				executeTask( new QueryHandler( clientSocket ) );
			}
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
		}

		this.service.shutdown();
		logger.info( "Server stopped." );
	}

	public synchronized void stop() {
		logger.info( "Stopping server..." );
		this.isStopped = true;
		try {
			this.serverSSLSocket.close();
		} catch( IOException e ) {
			throw new RuntimeException( "Error closing server", e );
		}
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	private void openSSLServerSocket() throws IOException {
		logger.info( "Opening SSL server socked..." );
		SSLServerSocketFactory sslserversocketfactory = ( SSLServerSocketFactory ) SSLServerSocketFactory.getDefault();
		this.serverSocket = sslserversocketfactory.createServerSocket( this.serverPort );
		logger.info( "SSL Socked opened on port: " + this.serverPort );
	}

	private void openServerSocket() throws IOException {
		logger.info( "Opening server socked..." );
		this.serverSocket = new ServerSocket( this.serverPort );
		logger.info( "Socked opened on port: " + this.serverPort );
	}

	private void executeTask( Runnable c ) {
		service.execute( c );
	}

	private final int serverPort = Config.serverPort();
	private boolean isStopped = false;
	private SSLServerSocket serverSSLSocket = null;
	private ServerSocket serverSocket = null;
	private final ExecutorService service = Executors.newFixedThreadPool( Config.maxNumberOfExecutors() );
	private static final Logger logger = LogManager.getLogger( ApplicationServer.class );
}
