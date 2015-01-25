import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author krzysztof
 */
public class CarriersAccessor implements DatabaseAccessor
{
	@Override
	public boolean openDatabase() {
		if( opened ) return false;

		int numerator = 0;
		BufferedReader in = null;
		try {
			in = new BufferedReader( new FileReader( carriersFile ) );
			String line;
			while( ( line = in.readLine() ) != null ) {
				if( line.startsWith( "#" ) || line.isEmpty() ) continue;
				String[] record = line.split( ":" );
				if( record.length == 3 ) {
					String name = record[ 0 ];
					String address = record[ 1 ];
					int port = Integer.parseInt( record[ 2 ] );

					carriers.add( new Carrier( name, address, port ) );
				} else {
					logger.warn( "Problem with line '" + line + "' in distances" );
				}
			}
			opened = true;
			for( Carrier c : carriers ) logger.info( c );
		} catch( IOException e ) {
			logger.error( e.getMessage(), e );
			return true;
		} finally {
			if( in != null ) try {
				in.close();
			} catch( IOException e ) {
				logger.error( e.getMessage(), e );
			}
		}

		return false;
	}

	@Override
	public boolean closeDatabase() {
		return false;
	}

	public Collection<Carrier> getCarriers() {
		return carriers;
	}

	private final List<Carrier> carriers = new ArrayList<>();
	private boolean opened = false;
	private String carriersFile = "database/carriers.db";
	private static final Logger logger = LogManager.getLogger( DistancesAccessor.class );
}
