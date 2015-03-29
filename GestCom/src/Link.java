import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Link implements Runnable, DecoSource
{
	protected ExecutorService es; //Definition du groupe de taches */
	protected Socket socklink = null; //Definition socket pour communiquer avec le client
	
	protected DataInputStream in;
	protected DataOutputStream out;
	
	protected BufferedReader  inBuffer;
	protected BufferedReader outBuffer;
	
	protected ComManager myComManager;
	
	protected int idClientServeur;
	protected String sIpClient;
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
			this.inBuffer = new BufferedReader(new InputStreamReader(
								this.socklink.getInputStream()));
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
		if(sMessage != null)
		{
			System.out.println("Message recu par le serveur : "+sMessage);
			Object obj = JSONValue.parse(sMessage);
			JSONObject objJson = (JSONObject) obj;
			
			
			if(objJson.get("MsgType").equals("Ident")){
				System.out.println("ident trame");
				traitementIdent(objJson);
			}
			else if(objJson.get("MsgType").equals("Logout")){
				System.out.println("logout trame");
				traitementLogout(objJson);
			}
			else{
				System.out.println("other trame");
				this.myComManager.transmissionMessage(objJson, this);
			}
		}
	}
	
	public void traitementIdent(JSONObject objJson){
		this.sIpClient = (String) objJson.get("From");
		System.out.println("Ip client = ");
		System.out.println(sIpClient);
	}
	
	public void traitementLogout(JSONObject objJson){
		
	}
	
	/**Methode Permettant l'envoie de message au client */
	public void envoieMessageClient(String sMessage)
	{
		try 
		{
			NetworkFlow.writeMessage(out, sMessage);
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
				sChaine = NetworkFlow.readMessage(this.inBuffer); //Lecture des messages venant du client		
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

	@Override
	public void addObserver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyObserver() {
		this.myComManager.logoutPerformed();
	}
}
