import java.time.format.DateTimeFormatter;

/**
 * @author krzysztof
 */
public class DateTimeUtils
{
	public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern( "yyyyMMdd" );
	public static final DateTimeFormatter HHMM = DateTimeFormatter.ofPattern( "HHmm" );
	public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );
	public static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern( "HH:mm" );
	public static final DateTimeFormatter YYYYMMDDHHMM = DateTimeFormatter.ofPattern( "yyyyMMddHHmm" );
	public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
}
