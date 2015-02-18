import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Toutes les taches utilisent l'interface Runnable

public class ComManager implements Runnable
{
	private ExecutorService es;            //Definition du groupe de threads
	private ServerSocket sockserv = null;  //Socket du serveur pour attendre les requ�tes en provenance des clients
	private ChaineServCli chaine = null;   //Objet de communication entre le client et le serveur
	


	//Variable Serveur
	private boolean bRun = true;  
	private InetAddress LocaleAdresse ;  //Pour recuperer l'adresse Ip locale
	private ArrayList<RobotLink> arRobotLink = new ArrayList<RobotLink>(); //ArrayList contenant les clients du serveur
	private ArrayList<DeviceLink> arDeviceLink = new ArrayList<DeviceLink>(); //ArrayList contenant les clients du serveur
	
	public ComManager(ExecutorService es)
	{
		this.es = es;

		try
		{
			sockserv = new ServerSocket (6020);  //Definition du port d'ecoute du serveur
			
			LocaleAdresse = InetAddress.getLocalHost();  //Recuperation de l'adresse Ip
            System.out.println("L'adresse locale est : "+LocaleAdresse );
		}
		catch (IOException ex) { }
	}
	
	/** Methode permettant de transmettre au destinataire un message provenant d'un client */
	public void transmissionMessage(char idRecepteur, String sMessage){
		int iBcl;
		
		for(iBcl = 0; iBcl < this.arClientServeur.size(); iBcl++ ) //On parcours le tableau de client pour trouver le destinataire
		{
			System.out.println("Ce que contient l'id : "+this.arClientServeur.get(iBcl).getIdClientServeur());
			System.out.println("l'id a trouve : "+idRecepteur);
			System.out.println(idRecepteur == this.arClientServeur.get(iBcl).getIdClientServeur());
			if(this.arClientServeur.get(iBcl).getIdClientServeur() == idRecepteur) //Si le recepteur est trouve
			{
				
				this.arClientServeur.get(iBcl).envoieMessageClient(sMessage);
			}
			else //Sinon
			{
				System.out.println("Transmission impossible");
			}
		}
	}

	public void run()
	{
		int nb_clients = 0;
		System.out.println("Serveur demarre!");

		try
		{
			sockserv.setSoTimeout(0); //Attente infini sur accept()
			while (bRun)
			{
				Socket sockcli = sockserv.accept();  //Attente de la connexion d'un client
				if(this.arClientServeur.contains(sockcli))  //Si le client est deja referenc� dans le tableau
				{
					System.out.println("Re-Bonjour");
				}
				else //Sinon
				{
					nb_clients++;
					System.out.println("Connexion �tablie : "+nb_clients+"�me client");
					chaine = new ChaineServCli(es, sockcli, this);  //Creation de l'objet de communication avec le client
					this.arClientServeur.add(chaine);  //On ajoute le client dans la table
					System.out.println(this.arClientServeur.size());
					es.execute(chaine); //On execute la methode run() de l'objet de communication
					
				}
			}
			sockserv.close();
		}
		catch (IOException ex) { 
			try 
			{
				sockserv.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<RobotLink> getArRobotLink() {
		return arRobotLink;
	}

	public ArrayList<DeviceLink> getArDeviceLink() {
		return arDeviceLink;
	}

	public static void main (String args[])
	{
		ExecutorService es = Executors.newFixedThreadPool(13); //Permet l'execution de 10 thread 
		ComManager comManager = new ComManager(es); //Instancie le serveur
		es.execute(comManager); //Execute la methode run() du serveur
	}
}
