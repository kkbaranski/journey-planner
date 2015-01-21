/**
 * @author krzysztof
 */
public class Validator
{
	public static boolean checkFrom(String input) {
		return checkCity( input );
	}

	public static boolean checkTo(String input) {
		return checkCity( input );
	}

	public static boolean checkDate(String input) {
		return onlyNumbers( input );
	}

	public static boolean checkTime(String input) {
		return onlyNumbers( input );
	}

	private static boolean checkCity(String input) {
		return input.length() < MAX_CHARS && input.matches( "[A-Za-z- ]+" );
	}

	private static boolean onlyNumbers(String input) {
		return input.length() < MAX_CHARS && input.matches( "[0-9]+" );
	}

	private static final int MAX_CHARS = 50;
}
