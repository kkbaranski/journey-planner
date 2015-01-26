package commons.utils;

import commons.journey.Route;
import commons.journey.Segment;

import java.io.PrintWriter;

/**
 * @author krzysztof
 */
public class ServerUtils
{
	public static void print( PrintWriter printWriter, Segment response ) {
		print( printWriter, response.getCarrier() );
		print( printWriter, response.getFrom() );
		print( printWriter, response.getTo() );
		print( printWriter, response.getStartDate().format( DateTimeUtils.YYYYMMDD ) );
		print( printWriter, response.getStartTime().format( DateTimeUtils.HHMM ) );
		print( printWriter, response.getStopDate().format( DateTimeUtils.YYYYMMDD ) );
		print( printWriter, response.getStopTime().format( DateTimeUtils.HHMM ) );
	}

	public static void print( PrintWriter printWriter, Route route ) {
		print( printWriter, "" + route.numberOfSegments() );
		for( Segment segment : route ) {
			print( printWriter, segment );
		}
	}

	public static void print( PrintWriter printWriter, ResponseCodes code ) {
		print( printWriter, code.toString() );
	}

	public static void print( PrintWriter writer, String text ) {
		if( writer == null ) return;
		synchronized( writer ) {
			writer.println( text );
			writer.flush();
		}
	}
}
