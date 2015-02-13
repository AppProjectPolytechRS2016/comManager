import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class Link implements Runnable
{
	protected ExecutorService es; //Definition du groupe de taches */
	protected Socket sockcli = null; //Definition socket pour communiquer avec le client
	
	protected DataInputStream in;
	protected DataOutputStream out;
	
	protected ComManager myComManager;
	
	protected char idClientServeur;
	
	public Link (ExecutorService es, Socket sockcli, ComManager myComManager)
	{
		this.es = es;
		this.sockcli = sockcli;
		this.myComManager = myComManager;
		
		try 
		{
			this.in = new DataInputStream(this.sockcli.getInputStream()); //Definition des canaux de communications
			this.out = new DataOutputStream(this.sockcli.getOutputStream());
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
		switch(sMessage.charAt(0))
		{
		case '0': //Definition d'id
			this.idClientServeur = sMessage.charAt(1);
			System.out.println("L'id du client est : "+idClientServeur);
			break;
		case '1': //Message a transmettre
			System.out.println("Message a transmettre");
			System.out.println("L'id cible est : "+sMessage.charAt(1));
			System.out.println("Le message est : "+sMessage.substring(2));
			this.myComManager.transmissionMessage(sMessage.charAt(1), sMessage.substring(2)); //Appel de la methode transmission du serveur
			break;
		default:
			break;
		}
	}
	
	/**Methode Permettant l'envoie de message au client */
	public void envoieMessageClient(String sMessage)
	{
		try 
		{
			Flux.ecritureMessage(out, sMessage);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** Methode renvoyant l'Id du client*/
	public char getIdClientServeur()
	{
		return idClientServeur;
	}

	public void run()
	{
		boolean bBoucle = true;
		String sChaine;
		//int iLongueur;

		while(bBoucle)
		{
			try 
			{
				sChaine = Flux.lectureMessage(in); //Lecture des messages venant du client
				traitementReception(sChaine);
				/*iLongueur = sChaine.length();
				System.out.println(sChaine.charAt(iLongueur-1));
				if(sChaine.charAt(iLongueur-1) == '1'){
					bBoucle = false;
					System.out.println("Fin de transmission");
				}*/
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
