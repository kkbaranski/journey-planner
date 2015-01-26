package carrier;

import carrier.server.ApplicationServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author krzysztof
 */
public class Application
{
	public static void main( String[] args ) {
		logger.info( "==CARRIER START==" );

		new Thread( new ApplicationServer() ).start();

		logger.info( "==CARRIER STOP==" );
	}

	private static final Logger logger = LogManager.getLogger( Application.class );
}
