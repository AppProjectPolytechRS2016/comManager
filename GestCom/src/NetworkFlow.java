import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

public class NetworkFlow
{
	/**Methode permettant de lire le flux reseau d'entree et de convertir ce qui a ete lue en String*/
	public static String readMessage(DataInputStream in) throws IOException,EOFException
	{
		/*int taille = in.readInt();		
		int nb = in.read(message,0,taille);*/
		//in.
		int lenght = in.available();
		byte message[]=new byte[lenght];
		in.readFully(message);
		return new String(message);
	}
	
	/**Methode permettant d'ecrire un message sur le flux reseau de sortie */
	public static void writeMessage(DataOutputStream out, String s) throws IOException
	{
		System.out.println("too");
		s.concat("\r\n");
		byte message[] = s.getBytes();
		out.write(message);
	}
	
	public static String readMessage(BufferedReader in) throws IOException,EOFException
	{
		return in.readLine();
		//return in.re
	}
}
