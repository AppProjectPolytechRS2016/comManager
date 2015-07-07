import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class NetworkFlow
{
	/**Method to write a message over the network
	 * @author Jerome
	 * @param out
	 * @param s
	 * @throws IOException
	 */
	public static void writeMessageNet(DataOutputStream out, String s) throws IOException
	{
		s.concat("\r\n");
		byte message[] = s.getBytes();
		out.write(message);
	}
	
	/**Method to read a message from the network
	 * @author Jerome
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws EOFException
	 */
	public static String readMessageNet(BufferedReader in) throws IOException,EOFException
	{
		return in.readLine();
	}
}
