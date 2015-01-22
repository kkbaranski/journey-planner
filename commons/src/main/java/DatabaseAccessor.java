import java.io.IOException;

/**
 * @author krzysztof
 */
public interface DatabaseAccessor
{
	public boolean openDatabase();
	public boolean closeDatabase();
}
