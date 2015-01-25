import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @author krzysztof
 */
public class PathCalculator
{
	private final String from;
	private final String to;
	private final DistancesAccessor2 accessor;

	PriorityQueue<Pair<Integer,String>> queue;
	HashSet<String> visited;
	Hashtable<String,Integer> distances;
	Hashtable<String,String> previousCity;


	public PathCalculator( String from, String to ) {
		accessor = new DistancesAccessor2();
		accessor.openDatabase();
		if( !accessor.existsCity( from ) || !accessor.existsCity( to ) )
			throw new RuntimeException( "Either " + from + " or " + to + " does not exist!" );
		this.from = from;
		this.to = to;
	}

	public List<String> calculate() {
		queue = new PriorityQueue<>();
		visited = new HashSet<>();
		distances = new Hashtable<>();
		previousCity = new Hashtable<>();

		distances.put( from, 0 );
		queue.add( new Pair<>( 0, from ) );
		while( !queue.isEmpty() ) {
			String nextCity = queue.poll().second;
			logger.trace( "NEXT CITY = '" + nextCity + "'" );
			if( !visited.contains( nextCity ) ) {
				logger.trace( "  not visited" );
				visited.add( nextCity );
				for( String neighbour : accessor.getNeighbours( nextCity ) ) {
					logger.trace( "    d( " + neighbour + " ) = " + d( neighbour ) );
					logger.trace( "    d( " + nextCity + " ) = " + d( nextCity ) );
					logger.trace( "    distanceBetween( " +
					             nextCity +
					             ", " +
					             neighbour +
					             " ) = " +
					             accessor.distanceBetween( nextCity, neighbour ) );
					if( d( neighbour ) > d( nextCity ) + accessor.distanceBetween( nextCity, neighbour ) ) {
						logger.trace( "      RELAX!" );
						distances.put( neighbour, d( nextCity ) + accessor.distanceBetween( nextCity, neighbour ) );
						queue.add( new Pair<>( d( neighbour ), neighbour ) );
						previousCity.put( neighbour, nextCity );
					}
				}
			}
		}

		if( d( to ) == DistancesAccessor2.INFINITY ) {
			logger.warn( "No connection between " + from + " and " + to );
			return null;
		}

		LinkedList<String> result = new LinkedList<>();
		String tmpCity = to;
		while( !tmpCity.equals( from ) ) {
			result.addFirst( tmpCity );
			tmpCity = previousCity.get( tmpCity );
		}
		result.addFirst( from );
		return result;
	}

	private Integer d( String city ) {
		if( !distances.containsKey( city ) ) return DistancesAccessor2.INFINITY;
		return distances.get( city );
	}

	private static final Logger logger = LogManager.getLogger( PathCalculator.class );
}
