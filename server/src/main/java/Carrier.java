/**
 * @author krzysztof
 */
public class Carrier
{
	private String name;
	private String address;
	private int port;

	public Carrier( String name, String address, int port ) {
		this.name = name;
		this.address = address;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "Carrier{" +
		       "name='" + name + '\'' +
		       ", address='" + address + '\'' +
		       ", serverPort=" + port +
		       '}';
	}
}
