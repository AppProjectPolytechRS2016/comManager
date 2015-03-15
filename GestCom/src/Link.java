import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Link implements Runnable
{
	protected ExecutorService es; //Definition du groupe de taches */
	protected Socket socklink = null; //Definition socket pour communiquer avec le client
	
	protected DataInputStream in;
	protected DataOutputStream out;
	
	protected ComManager myComManager;
	
	protected int idClientServeur;
	protected String sName; 
	protected boolean bRunLink;
	
	public Link (ExecutorService es, Socket sockcli, ComManager myComManager)
	{
		this.es = es;
		this.socklink = sockcli;
		this.myComManager = myComManager;
		
		try 
		{
			this.in = new DataInputStream(this.socklink.getInputStream()); //Definition des canaux de communications
			this.out = new DataOutputStream(this.socklink.getOutputStream());
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**Methode traitant les message arrivant vers le serveur et discriminant les type de message */
	public void traitementReception(String sMessage)
	{
		System.out.println("Message recu par le serveur : "+sMessage);
	}
	
	/**Methode Permettant l'envoie de message au client */
	public void envoieMessageClient(String sMessage)
	{
		try 
		{
			NetworkFlow.ecritureMessage(out, sMessage);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Methode renvoyant l'Id du client*/
	public int getIdClientServeur()
	{
		return idClientServeur;
	}

	public void run()
	{
		bRunLink = true;
		String sChaine;
		
		while(bRunLink)
		{
			try 
			{
				sChaine = NetworkFlow.lectureMessage(in); //Lecture des messages venant du client
				traitementReception(sChaine);
			} 
			catch(EOFException a){
				bRunLink = false;
				try {
					this.socklink.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			
			Thread.yield();
		}
	}
}
