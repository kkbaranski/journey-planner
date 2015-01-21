import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author krzysztof
 */
public class Application
{
	public static void main( String[] args ) {
		logger.info( "arguments:" );
		for( String s : args ) logger.info( "  " + s );
		logger.info( "==START==" );

		new Thread( new ApplicationServer() ).start();


		logger.info( "==STOP==" );
	}

	private static final Logger logger = LogManager.getLogger( Application.class );
}
