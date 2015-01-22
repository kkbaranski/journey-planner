import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * @author krzysztof
 */
public class TimetableAccessor
{
	public boolean openDatabase() {
		logger.info( "Opening database..." );
		try {
			Class.forName( "org.sqlite.JDBC" );
			connection = DriverManager.getConnection( "jdbc:sqlite:database/" + databaseName );
		} catch( Exception e ) {
			logger.error( e.getClass().getName() + ": " + e.getMessage() );
			return true;
		}
		logger.info( "Opened database successfully" );
		return false;
	}

	public boolean closeDatabase() {
		logger.info( "Closing database..." );
		try {
			if( connection != null && !connection.isClosed() ) connection.close();
		} catch( SQLException e ) {
			logger.error( "Closing database failed" );
			return true;
		}
		logger.info( "Closed database successfully" );
		return false;
	}

	public Segment getCourse( Query query ) {
		String from = query.getFrom();
		String to = query.getTo();
		LocalDate date = query.getDate();
		LocalTime time = query.getTime();

		String statement = "select start_time, stop_time " +
		                   "from timetable " +
		                   "where from_city = '" + from + "' " +
		                   "and to_city = '" + to + "' " +
		                   "and start_time >= '" + time + "' " +
		                   "order by stop_time " +
		                   "limit 1 ";

		logger.info( "STATEMENT: " + statement );

		Statement st = null;
		ResultSet rs = null;

		try {
			st = connection.createStatement();
			rs = st.executeQuery( statement );

			if( rs.next() ) {
				String startTimeString = rs.getString( "start_time" );
				String stopTimeString = rs.getString( "stop_time" );
				logger.info( "start_time = " + startTimeString );
				logger.info( "stop_time = " + stopTimeString );

				LocalTime startTime;
				LocalTime stopTime;
				try {
					startTime = LocalTime.parse( startTimeString, timeFormatter );
					stopTime = LocalTime.parse( stopTimeString, timeFormatter );
				} catch( DateTimeParseException e ) {
					logger.warn( e.getMessage(), e );
					return null;
				}

				return new Segment.Builder( "" ).setFrom( from )
				                                .setTo( to )
				                                .setStartDate( date )
				                                .setStopDate( date )
				                                .setStartTime( startTime )
				                                .setStopTime( stopTime )
				                                .build();
			} else {
				logger.info( "No result" );
			}
		} catch( SQLException e ) {
			logger.error( "Query problem" );

		} finally {
			try {
				if( rs != null ) rs.close();
				if( st != null ) st.close();

			} catch( SQLException ex ) {
				logger.warn( ex.getMessage(), ex );
			}
		}
		return null;
	}


	public static void setDatabaseName( String databaseName ) {
		TimetableAccessor.databaseName = databaseName;
	}

	private Connection connection = null;
	private static String databaseName = "";
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern( "HH:mm" );
	private static final Logger logger = LogManager.getLogger( TimetableAccessor.class );
}
