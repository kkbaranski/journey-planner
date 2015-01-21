import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author krzysztof
 */
public class Config
{
	private static final Properties prop;

	static {
		prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream( "config.properties" );
			prop.load( input );
		} catch( IOException e ) {
			e.printStackTrace();
		} finally {
			if( input != null ) {
				try {
					input.close();
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}

	public static int getInt( String property ) {
		return Integer.parseInt( prop.getProperty( property ) );
	}

	public static String getString( String property ) {
		return prop.getProperty( property );
	}

	public static int serverPort() {return getInt( "server_port" );}

	public static String serverAddress() {
		return getString( "server_address" );
	}
}
