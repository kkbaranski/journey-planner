package carrier.database;

import commons.journey.Query;
import commons.journey.Segment;

/**
 * @author krzysztof
 */
public class QueryExecutor
{
	private final Query query;

	public QueryExecutor( Query query ) {
		this.query = query;
	}

	public Segment execute() {
		TimetableAccessor timetableAccessor = new TimetableAccessor();
		if( timetableAccessor.openDatabase() ) return null;
		try {
			return timetableAccessor.getCourse( query );
		} finally {
			timetableAccessor.closeDatabase();
		}
	}
}
