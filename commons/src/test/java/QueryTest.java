import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.MissingResourceException;

import static junit.framework.TestCase.assertEquals;

public class QueryTest
{

	@Test( expected = MissingResourceException.class )
	public void testQuery1() throws Exception {
		Query query = new Query.Builder( "Krakow", "Warszawa" ).build();
	}

	@Test( expected = MissingResourceException.class )
	public void testQuery2() throws Exception {
		Query query = new Query.Builder( "Krakow", "Warszawa" ).setDate( LocalDate.now() ).build();
	}

	@Test()
	public void testQuery3() throws Exception {
		Query query = new Query.Builder( "Krakow", "Warszawa" ).setDate( LocalDate.of( 2014, 1, 1 ) )
		                                                       .setTime( LocalTime.of( 12, 14 ) )
		                                                       .build();
		assertEquals( "Query{from='Krakow', to='Warszawa', date=2014-01-01, time=12:14}", query.toString() );
	}

	@Test()
	public void testQuery4() throws Exception {
		Query query = new Query.Builder().setDate( LocalDate.of( 2014, 1, 1 ) )
		                                 .setTime( LocalTime.of( 12, 14 ) )
		                                 .setFrom( "Krakow" )
		                                 .setTo( "Warszawa" )
		                                 .build();
		assertEquals( "Query{from='Krakow', to='Warszawa', date=2014-01-01, time=12:14}", query.toString() );
	}

	@Test()
	public void testQuery5() throws Exception {
		Query query = new Query.Builder( "Krakow", "Warszawa", LocalDate.of( 2014, 1, 1 ), LocalTime.of( 12, 14 ) ).build();
		assertEquals( "Query{from='Krakow', to='Warszawa', date=2014-01-01, time=12:14}", query.toString() );
	}
}