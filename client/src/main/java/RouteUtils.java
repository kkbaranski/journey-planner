/**
 * @author krzysztof
 */
public class RouteUtils
{

	public static Route getRoute( Query query ) {
		return new QueryProtocol( query ).getRoute();
	}
}
