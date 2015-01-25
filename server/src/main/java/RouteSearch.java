import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author krzysztof
 */
public class RouteSearch
{
	public RouteSearch( List<String> cities ) {
		this.carriersAccessor = new CarriersAccessor();
		this.carriersAccessor.openDatabase();
		this.cities = cities;
	}

	public Route search( LocalDateTime dateTime ) {
		if( cities == null || cities.size() < 2 ) {
			logger.error( "Incorrect set of cities" );
			return null;
		}

		Route result = new Route();
		int size = cities.size();
		String startCity = cities.get( 0 );
		String stopCity = cities.get( size - 1 );

		return search( 0, size - 1, dateTime );
	}

	private Route search( int startCityIndex, int stopCityIndex, LocalDateTime dateTime ) {
		Route route = new Route( ResponseCodes.NOTFOUND );
		if( startCityIndex >= stopCityIndex ) {
			logger.info( startCityIndex + " = startCityIndex >= stopCityIndex = " + stopCityIndex );
			return route;
		}

		Route bestRoute = new Route( ResponseCodes.NOTFOUND );
		for( int i = stopCityIndex; i > startCityIndex; --i ) {
			logger.info( "Searching direct connection: " + cities.get( startCityIndex ) + " -> " + cities.get( i ) );
			Query query = new Query.Builder().setFrom( cities.get( startCityIndex ) )
			                                 .setTo( cities.get( i ) )
			                                 .setDateTime( dateTime )
			                                 .build();

			QueryProtocol queryProtocol = new QueryProtocol( query );
			for( Carrier carrier : carriersAccessor.getCarriers() ) {
				logger.info( "> CARRIER: '" +
				             carrier.getName() +
				             "' [ address='" +
				             carrier.getAddress() +
				             "', port=" +
				             carrier.getPort() +
				             " ] :" );
				Segment segment = queryProtocol.getSegment( carrier );
				if( segment != null ) {
					logger.info( "    direct connection found:" );
					logger.info( "      " + segment );
					segment.setCarrier( carrier.getName() );
					if( i == stopCityIndex ) {
						logger.info( "        from begin to end!" );
						route = new Route( segment );
						if( bestRoute.getStatus() == ResponseCodes.NOTFOUND ||
						    bestRoute.getFinishDateTime().isAfter( route.getFinishDateTime() ) ) bestRoute = route;
					} else {
						logger.info( "        from begin to " + cities.get( i ) + "!" );
						route = search( i,
						                stopCityIndex,
						                LocalDateTime.of( segment.getStopDate(), segment.getStopTime() ) );
						if( route.getStatus() == ResponseCodes.OK ) {
							if( bestRoute.getStatus() == ResponseCodes.NOTFOUND ||
							    bestRoute.getFinishDateTime().isAfter( route.getFinishDateTime() ) ) {
								bestRoute = new Route( segment ).addRoute( route );
							}
						}
					}
				}
			}
		}

		return bestRoute;
	}

	private final List<String> cities;
	private final CarriersAccessor carriersAccessor;
	private static final Logger logger = LogManager.getLogger( RouteSearch.class );

}
