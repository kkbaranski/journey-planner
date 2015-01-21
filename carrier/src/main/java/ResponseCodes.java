/**
 * @author krzysztof
 */
public enum ResponseCodes
{
	OK("OK"),
	BADFORMAT("BAD FORMAT"),
	TIMEOUT("TIME OUT"),
	BADQUERY("BAD QUERY"),
	NOTFOUND("NOTFOUND"),
	FOUND("FOUND");

	ResponseCodes( String responseName ) {
		this.responseName = responseName;
	}

	@Override
	public String toString() {
		return responseName;
	}

	private final String responseName;

}
