import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author krzysztof
 */
public class Application
{
	public static void main( String[] args ) {
		logger.info( "==START==" );

		if( args.length != 1 ) {
			logger.error( "args.length = " + args.length );
			return;
		}

		String databaseName = args[ 0 ];
		TimetableAccessor.setDatabaseName( databaseName );

		new Thread( new ApplicationServer() ).start();


		logger.info( "==STOP==" );
	}

	private static final Logger logger = LogManager.getLogger( Application.class );
}
