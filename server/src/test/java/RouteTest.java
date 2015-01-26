import commons.journey.Route;
import commons.journey.Segment;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertSame;

public class RouteTest
{

	@Test
	public void testAddSegment() throws Exception {
		Segment segment1 = new Segment.Builder( "" ).setFrom( "Krakow" )
		                                            .setTo( "Lodz" )
		                                            .setStartDate( LocalDate.now() )
		                                            .setStopDate( LocalDate.now() )
		                                            .setStartTime( LocalTime.now() )
		                                            .setStopTime( LocalTime.now() )
		                                            .build();
		Segment segment2 = new Segment.Builder( "" ).setFrom( "Lodz" )
		                                            .setTo( "Warszawa" )
		                                            .setStartDate( LocalDate.now() )
		                                            .setStopDate( LocalDate.now() )
		                                            .setStartTime( LocalTime.now() )
		                                            .setStopTime( LocalTime.now() )
		                                            .build();
		Segment segment3 = new Segment.Builder( "" ).setFrom( "Warszawa" )
		                                            .setTo( "Gdansk" )
		                                            .setStartDate( LocalDate.now() )
		                                            .setStopDate( LocalDate.now() )
		                                            .setStartTime( LocalTime.now() )
		                                            .setStopTime( LocalTime.now() )
		                                            .build();

		List<Segment> segmentList = new ArrayList<>();
		segmentList.add( segment1 );
		segmentList.add( segment2 );
		segmentList.add( segment3 );

		Route route = new Route();
		route.addSegment( segment1 );
		route.addSegment( segment2 );
		route.addSegment( segment3 );

		int i = 0;
		for(Segment s : route) {
			assertSame(segmentList.get( i ), s);
			++i;
		}

	}
}