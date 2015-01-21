import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author krzysztof
 */
public class RouteSearch
{
	public RouteSearch( List<String> cities ) {
		this.cities = cities;
	}

	public Route search() {
		if( cities == null || cities.size() < 2 ) {
			logger.error( "Incorrect set of cities" );
			return null;
		}

		Route result = new Route();
		int size = cities.size();
		String startCity = cities.get(0);
		String stopCity = cities.get( size-1 );


		return result;
	}

	private Route search(int startCityIndex, int stopCityIndex) {
		//todo
		return null;
	}

	private final List<String> cities;
	private static final Logger logger = LogManager.getLogger( RouteSearch.class );

}
