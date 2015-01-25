import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author krzysztof
 */
public class DistancesAccessor2 implements DatabaseAccessor
{
	public static final int INFINITY = 100000;

	@Override
	public boolean openDatabase() {
		if( opened ) return false;
		logger.info( "Opening database..." );

		distanceGraph = new Hashtable<>();
		BufferedReader in = null;
		try {
			in = new BufferedReader( new FileReader( distancesFile ) );
			String line;
			while( ( line = in.readLine() ) != null ) {
				if( line.startsWith( "#" ) || line.isEmpty() ) continue;
				String[] record = line.split( ":" );

				if( record.length != 3 ) {
					logger.warn( "Problem with line '" + line + "' in distances" );
					continue;
				}

				String city1 = record[ 0 ];
				String city2 = record[ 1 ];
				int distance = Integer.parseInt( record[ 2 ] );

				if( !distanceGraph.containsKey( city1 ) ) {
					distanceGraph.put( city1, new Hashtable<>() );
				}
				if( !distanceGraph.containsKey( city2 ) ) {
					distanceGraph.put( city2, new Hashtable<>() );
				}
				distanceGraph.get( city1 ).put( city2, distance );
			}
			printGraph();
			opened = true;
			logger.info( "Database opened." );
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

	public Collection<String> getNeighbours( String city ) {
		if( distanceGraph.containsKey( city ) ) {
			logger.trace( "Neighbours of '" + city + "':" );
			for( String c : distanceGraph.get( city ).keySet() ) logger.info( "  '--> " + c );
			return distanceGraph.get( city ).keySet();
		}
		logger.trace( "City '" + city + "' has no neighbours" );
		return new ArrayList<>();
	}

	public boolean existsCity( String city ) {
		logger.info( "Checking if '" + city + "' exists: " + distanceGraph.containsKey( city ) );
		return distanceGraph.containsKey( city );
	}

	public int distanceBetween( String city1, String city2 ) {
		logger.trace( "Checking distance between '" + city1 + "' and '" + city2 + "': " );
		if( !existsCity( city1 ) || !existsCity( city2 ) ) {
			logger.warn( city1 + " or " + city1 + " does not exists -> INF" );
			return INFINITY;
		}
		if( distanceGraph.get( city1 ).containsKey( city2 ) ) {
			int result = distanceGraph.get( city1 ).get( city2 );
			logger.info( "   = " + result );
			return result;
		}
		logger.info( "   = INF" );
		return INFINITY;
	}

	private void printGraph() {
		System.out.println( "--- DISTANCES ---" );
		for( Map.Entry<String,Map<String,Integer>> entry1 : distanceGraph.entrySet() ) {
			System.out.println( entry1.getKey() + " ->" );
			for( Map.Entry<String,Integer> entry2 : entry1.getValue().entrySet() ) {
				System.out.println( "    -> " + entry2.getKey() + " - " + entry2.getValue() + "km" );
			}
		}
	}

	private static Map<String,Map<String,Integer>> distanceGraph = null;
	private static boolean opened = false;
	private static String distancesFile = "database/distances.db";
	private static final Logger logger = LogManager.getLogger( DistancesAccessor.class );

}
