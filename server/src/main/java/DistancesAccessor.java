import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author krzysztof
 */
public class DistancesAccessor implements DatabaseAccessor
{
	@Override
	public boolean openDatabase() {
		if(opened) return false;

		int numerator = 0;
		BufferedReader in = null;
		try {
			in = new BufferedReader( new FileReader( distancesFile ) );
			String line;
			while( ( line = in.readLine() ) != null ) {
				if( line.startsWith( "#" ) || line.isEmpty() ) continue;
				String[] record = line.split( ":" );
				if( record.length == 3 ) {
					String from = record[ 0 ];
					String to = record[ 1 ];
					int dist = Integer.parseInt( record[ 2 ] );

					int fromNr;
					int toNr;
					if( numberOfCity.containsKey( from ) ) {
						fromNr = numberOfCity.get( from );
					} else {
						numberOfCity.put( from, numerator );
						nameOfCityNumber.add( from );
						fromNr = numerator;
						distGraph.add( new HashSet<>() );
						++numerator;
						if( numerator != nameOfCityNumber.size() ) logger.error( "numerator=" +
						                                                         numerator +
						                                                         ", nameOfCityNumber.size()=" +
						                                                         nameOfCityNumber.size() );
					}
					if( numberOfCity.containsKey( to ) ) {
						toNr = numberOfCity.get( to );
					} else {
						numberOfCity.put( to, numerator );
						nameOfCityNumber.add( to );
						toNr = numerator;
						distGraph.add( new HashSet<>() );
						++numerator;
						if( numerator != nameOfCityNumber.size() ) logger.error( "numerator=" +
						                                                         numerator +
						                                                         ", nameOfCityNumber.size()=" +
						                                                         nameOfCityNumber.size() );
					}
					City newCity = new City( toNr, dist );
					distGraph.elementAt( fromNr ).add( newCity );
				} else {
					logger.warn( "Problem with line '" + line + "' in distances" );
				}
			}
			opened = true;
//			logger.info( "DIST GRAPH:" );
//			for( int i = 0; i < distGraph.size(); ++i ) {
//				logger.info( "FROM " + nameOfCityNumber.elementAt( i ) );
//				for( City c : distGraph.elementAt( i ) ) {
//					logger.info( "  TO " + nameOfCityNumber.elementAt( c.number ) + ": " + c.dist + " km" );
//				}
//			}
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

	public List<String> getNeighbours(String city) {
		List<String> ret = new ArrayList<>(  );
		if(existsCity( city )) {
			for(City c : distGraph.elementAt( numberOfCity.get( city ) )) {
				ret.add( nameOfCityNumber.elementAt( c.number ) );
			}
		}
		return ret;
	}

	public boolean existsCity(String city) {
		return numberOfCity.containsKey( city );
	}

	@Override
	public boolean closeDatabase() {
		return false;
	}

	private static boolean opened = false;
	private static Vector<String> nameOfCityNumber = new Vector<>();
	private static Map<String,Integer> numberOfCity = new HashMap<>();
	private static Vector<Set<City>> distGraph = new Vector<>();
	private static String distancesFile = "database/distances.db";
	private static final Logger logger = LogManager.getLogger( DistancesAccessor.class );

	private static class City
	{
		public int number;
		public int dist;

		public City( int number, int dist ) {
			this.number = number;
			this.dist = dist;
		}
	}
}
