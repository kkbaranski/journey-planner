package commons.journey;


import commons.utils.ResponseCodes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author krzysztof
 */
public class Route implements Iterable<Segment>
{
	public Route() {
		status = ResponseCodes.NOTFOUND;
	}

	public Route( ResponseCodes status ) {
		this.status = status;
	}

	public Route( Segment segment ) {
		this.status = ResponseCodes.OK;
		addSegment( segment );
	}

	public Route addSegment( Segment segment ) {
		this.route.add( segment );
		return this;
	}

	public Route addRoute( Route nextRoute ) {
		for( Segment s : nextRoute ) addSegment( s );
		return this;
	}

	public ResponseCodes getStatus() {
		return status;
	}

	public Segment getLastSegment() {
		if( numberOfSegments() > 0 ) return route.get( numberOfSegments() - 1 );
		return null;
	}

	public Segment getFirstSegment() {
		if( numberOfSegments() > 0 ) return route.get( 0 );
		return null;
	}

	public LocalDateTime getBeginDateTime() {
		if( numberOfSegments() == 0 ) return null;
		return getFirstSegment().getStartDateTime();
	}

	public LocalDateTime getFinishDateTime() {
		if( numberOfSegments() == 0 ) return null;
		return getLastSegment().getStopDateTime();
	}

	public int numberOfSegments() {
		return this.route.size();
	}

	@Override
	public Iterator<Segment> iterator() {
		return new Iterator<Segment>()
		{
			Iterator<Segment> iterator = route.iterator();

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Segment next() {
				return iterator.next();
			}
		};
	}

	private final List<Segment> route = new ArrayList<>();
	private ResponseCodes status;

	public void setStatus( ResponseCodes status ) {
		this.status = status;
	}

	public long getTotalTime() {
		return Duration.between( getBeginDateTime(), getFinishDateTime() ).getSeconds();
	}
}
