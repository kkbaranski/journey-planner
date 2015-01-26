package client.utils;

import client.protocols.QueryProtocol;
import commons.journey.Query;
import commons.journey.Route;

/**
 * @author krzysztof
 */
public class RouteUtils
{
	public static Route getRoute( Query query ) {
		return new QueryProtocol( query ).getRoute();
	}
}
