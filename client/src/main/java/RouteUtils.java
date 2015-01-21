/**
 * @author krzysztof
 */
public class RouteUtils
{

	public static Route getRoute( Query query ) {
		Route route = new QueryProtocol( query ).getRoute();
		return null;
	}
}
