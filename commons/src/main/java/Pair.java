/**
 * @author krzysztof
 */
public class Pair<A extends Comparable<A>, B extends Comparable<B>> implements Comparable<Pair<A,B>>
{
	public final A first;
	public final B second;

	public Pair( A first, B second ) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int compareTo( Pair<A,B> o ) {
		if( this.first.equals( o.first ) ) return this.second.compareTo( o.second );
		return this.first.compareTo( o.first );
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;

		Pair pair = ( Pair ) o;

		return first.equals( pair.first ) && second.equals( pair.second );

	}

	@Override
	public int hashCode() {
		int result = first.hashCode();
		result = 31 * result + second.hashCode();
		return result;
	}
}
