import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class NetworkFlow
{
	/**Methode permettant d'ecrire un message sur le flux reseau de sortie */
	public static void writeMessageNet(DataOutputStream out, String s) throws IOException
	{
		s.concat("\r\n");
		byte message[] = s.getBytes();
		out.write(message);
	}
	
	public static String readMessageNet(BufferedReader in) throws IOException,EOFException
	{
		return in.readLine();
	}
}
