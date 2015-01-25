import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author krzysztof
 */
public class Application
{
	public static void main( String[] args ) {
		logger.info( "==SERVER START==" );

		new Thread( new ApplicationServer() ).start();

		logger.info( "==SERVER STOP==" );
	}

	private static final Logger logger = LogManager.getLogger( Application.class );
}
