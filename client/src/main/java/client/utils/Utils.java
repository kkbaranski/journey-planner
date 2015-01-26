package client.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author krzysztof
 */
public class Utils
{
	public static void printStartPage() {
		String startPage = "startpage.ascii";
		InputStream in = Utils.class.getClassLoader().getResourceAsStream( startPage );
		BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
		String line;
		try {
			while( ( line = br.readLine() ) != null ) {
				System.out.println( "  " + line );
			}
		} catch( IOException e ) {
			logger.error( "IOException: problem with startpage" );
		}
	}

	public static String getLineFromStdin( String prompt ) {
		System.out.print( prompt + ": " );
		BufferedReader in = new BufferedReader( new InputStreamReader( System.in ) );
		String result = null;
		try {
			result = in.readLine();
		} catch( IOException e ) {
			logger.error( "IOException: problem with read line from STDIN" );
		}
		return result;
	}

	public static void printError( String message ) {
		System.out.println( "[error] " + message );
	}

	private static final Logger logger = LogManager.getLogger( Utils.class );
}
