/**
 * @author krzysztof
 */
public enum ResponseCodes
{
	OK,
	BADFORMAT,
	TIMEOUT,
	BADQUERY,
	NOTFOUND,
	FOUND,
	NOTEXISTS,
	IOERROR,
	UNKNOWNERROR,
	NOCONNECTION;

	public String reason() {
		switch( this ) {
			case BADFORMAT:
				return "Invalid date or city format";
			case BADQUERY:
				return "Invalid query";
			case FOUND:
				return "Route found";
			case IOERROR:
				return "Problem with connection";
			case NOCONNECTION:
				return "Connections between cities does not exist";
			case NOTFOUND:
				return "Route not found";
			case NOTEXISTS:
				return "City name not exists";
			case OK:
				return "Success";
			case TIMEOUT:
				return "Connection timeout";
			case UNKNOWNERROR:
				return "Unknown error";
			default:
				return "Unknown reason";

		}
	}
}
