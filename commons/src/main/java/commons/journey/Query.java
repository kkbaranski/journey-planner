package commons.journey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.MissingResourceException;

/**
 * @author krzysztof
 */
public class Query
{
	public static class Builder
	{
		public Builder() {}

		public Builder( String from, String to ) {
			this.from = from;
			this.to = to;
		}

		public Builder( String from, String to, LocalDate date, LocalTime time ) {
			this.from = from;
			this.to = to;
			this.date = date;
			this.time = time;
		}

		public Query build() {
			if( from == null ) {
				logger.error( "build query error: missing field 'from'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "from" );
			}

			if( to == null ) {
				logger.error( "build query error: missing field 'to'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "to" );
			}

			if( date == null ) {
				logger.error( "build query error: missing field 'date'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "date" );
			}

			if( time == null ) {
				logger.error( "build query error: missing field 'time'" );
				throw new MissingResourceException( "build query error", this.getClass().getName(), "time" );
			}

			return new Query( from, to, date, time );
		}

		public Builder setFrom( String from ) {
			this.from = from;
			return this;
		}

		public Builder setTo( String to ) {
			this.to = to;
			return this;
		}

		public Builder setDate( LocalDate date ) {
			this.date = date;
			return this;
		}

		public Builder setDate( String date, DateTimeFormatter formatter ) {
			try {
				this.date = LocalDate.parse( date, formatter );
			} catch( DateTimeParseException e ) {
				logger.error( "Problem with parsing date: " + date );
			}
			return this;
		}

		public Builder setTime( LocalTime time ) {
			this.time = time;
			return this;
		}

		public Builder setTime( String time, DateTimeFormatter formatter ) {
			try {
				this.time = LocalTime.parse( time, formatter );
			} catch( DateTimeParseException e ) {
				logger.error( "Problem with parsing time: " + time );
			}
			return this;
		}

		public Builder setDateTime(LocalDateTime dateTime) {
			this.date = dateTime.toLocalDate();
			this.time = dateTime.toLocalTime();
			return this;
		}

		private String from = null;
		private String to = null;
		private LocalDate date = null;
		private LocalTime time = null;
		private static final Logger logger = LogManager.getLogger( Builder.class );
	}

	@Override
	public String toString() {
		return "Query{" +
		       "from='" + from + '\'' +
		       ", to='" + to + '\'' +
		       ", date=" + date +
		       ", time=" + time +
		       '}';
	}

	private Query( String from, String to, LocalDate date, LocalTime time ) {
		this.from = from;
		this.to = to;
		this.date = date;
		this.time = time;
		logger.info( this.toString() );
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getTime() {
		return time;
	}

	private final String from;
	private final String to;
	private final LocalDate date;
	private final LocalTime time;
	private static final Logger logger = LogManager.getLogger( Query.class );
}
