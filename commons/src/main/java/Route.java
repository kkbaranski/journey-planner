import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author krzysztof
 */
public class Route implements Iterable<Segment>
{
	public Route addSegment(Segment segment) {
		this.route.add( segment );
		return this;
	}

	public int numberOfSegments() {
		return this.route.size();
	}

	@Override
	public Iterator<Segment> iterator() {
		return new Iterator<Segment>() {
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
}
