import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.MissingResourceException;

/**
 * @author krzysztof
 */
public class Segment
{
	public static class Builder
	{
		public Builder() {}

		public Builder( String carrier ) {
			this.carrier = carrier;
		}

		public Segment build() {
			if( carrier == null ) {
				logger.error( "build query error: missing field 'carrier'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "carrier" );
			}

			if( from == null ) {
				logger.error( "build query error: missing field 'from'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "from" );
			}

			if( to == null ) {
				logger.error( "build query error: missing field 'to'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "to" );
			}

			if( startDate == null ) {
				logger.error( "build query error: missing field 'startDate'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "startDate" );
			}

			if( stopDate == null ) {
				logger.error( "build query error: missing field 'stopDate'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "stopDate" );
			}

			if( startTime == null ) {
				logger.error( "build query error: missing field 'startTime'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "startTime" );
			}

			if( stopTime == null ) {
				logger.error( "build query error: missing field 'stopTime'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "stopTime" );
			}

			return new Segment( carrier, from, to, startDate, stopDate, startTime, stopTime );
		}

		public Builder setCarrier( String carrier ) {
			this.carrier = carrier;
			return this;
		}

		public Builder setFrom( String from ) {
			this.from = from;
			return this;
		}

		public Builder setTo( String to ) {
			this.to = to;
			return this;
		}

		public Builder setStartDate( LocalDate startDate ) {
			this.startDate = startDate;
			return this;
		}

		public Builder setStopDate( LocalDate stopDate ) {
			this.stopDate = stopDate;
			return this;
		}

		public Builder setStartTime( LocalTime startTime ) {
			this.startTime = startTime;
			return this;
		}

		public Builder setStopTime( LocalTime stopTime ) {
			this.stopTime = stopTime;
			return this;
		}

		private String carrier = null;
		private String from = null;
		private String to = null;
		private LocalDate startDate = null;
		private LocalDate stopDate = null;
		private LocalTime startTime = null;
		private LocalTime stopTime = null;
		private static final Logger logger = LogManager.getLogger( Segment.class );
	}

	private Segment( String carrier,
	                 String from,
	                 String to,
	                 LocalDate startDate,
	                 LocalDate stopDate,
	                 LocalTime startTime,
	                 LocalTime stopTime ) {
		this.carrier = carrier;
		this.from = from;
		this.to = to;
		this.startDate = startDate;
		this.stopDate = stopDate;
		this.startTime = startTime;
		this.stopTime = stopTime;
		logger.info( this.toString() );
	}

	public String getCarrier() {
		return carrier;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getStopDate() {
		return stopDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getStopTime() {
		return stopTime;
	}

	@Override
	public String toString() {
		return "Segment{" +
				"carrier='" + carrier + '\'' +
				", from='" + from + '\'' +
				", to='" + to + '\'' +
				", startDate=" + startDate +
				", stopDate=" + stopDate +
				", startTime=" + startTime +
				", stopTime=" + stopTime +
				'}';
	}

	private final String carrier;
	private final String from;
	private final String to;
	private final LocalDate startDate;
	private final LocalDate stopDate;
	private final LocalTime startTime;
	private final LocalTime stopTime;
	private static final Logger logger = LogManager.getLogger( Segment.class );
}
