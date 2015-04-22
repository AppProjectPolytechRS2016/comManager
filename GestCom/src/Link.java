import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Link implements Runnable, DecoSource
{
	protected ExecutorService es; //Definition du groupe de taches
	protected Socket socklink = null; //Definition socket pour communiquer avec le client
	
	//protected DataInputStream in;
	protected DataOutputStream out;
	
	protected BufferedReader  inBuffer;
	
	protected ComManager myComManager;
	
	protected int idClientServeur;
	protected String sIpClient;
	protected String sName; 
	protected boolean bRunLink;
	
	/**Constructor of Link's object
	 * 
	 * @param es
	 * @param sockcli
	 * @param myComManager
	 */
	public Link (ExecutorService es, Socket sockcli, ComManager myComManager)
	{
		this.es = es;
		this.socklink = sockcli;
		this.myComManager = myComManager;
		
		try 
		{
			//this.in = new DataInputStream(this.socklink.getInputStream()); //Definition des canaux de communications
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
	
	/**Method to deal with JSON frame
	 * 
	 *@param
	 */
	public void proccesingOnReception(String sMessage)
	{
		if(sMessage != null)
		{
			//System.out.println("Message recu par le serveur : "+sMessage);
			this.myComManager.writeConsoleLog("Message recu par le serveur : "+sMessage);
			Object obj = JSONValue.parse(sMessage);
			JSONObject objJson = (JSONObject) obj;
			
			System.out.println(objJson.get("MsgType"));
			if(objJson.get("MsgType").equals("Ident"))
			{
				System.out.println("Ident trame");
				prcossessingIdent(objJson);
			}
			else if(objJson.get("MsgType").equals("Logout"))
			{
				System.out.println("Logout trame");
				prcossessingLogout();
			}
			else if(objJson.get("MsgType").equals("UpdateList"))
			{
				if(this.getClass() == DeviceLink.class)
				{
					System.out.println("Update list trame");
					sendInformation();
				}
				else
				{
					System.out.println("Wrong class for update list");
				}
			}
			else{
				System.out.println("Other trame");
				this.myComManager.transmissionMessage(objJson, this);
			}
		}
	}
	
	/**Process on Identification frame
	 * 
	 * @param objJson
	 */
	public void prcossessingIdent(JSONObject objJson){
		this.sIpClient = (String) objJson.get("From");
		checkOld();
		this.myComManager.writeConsoleLog("Ip client = " + sIpClient);
		printIp();
		sendInformation();
	}
	
	/**Insert IP in the GUI
	 * 
	 */
	public void printIp(){
	}
	
	/**Process on Logout frame
	 * 
	 */
	public void prcossessingLogout(){
		this.bRunLink = false;
		notifyObserver();	
	}
	
	/**Check if a previous version of the object is present in ComManager table
	 * 
	 */
	public void checkOld(){
		
	}
	
	/**Send data to the client
	 * 
	 */
	public void sendInformation(){
		
	}
	
	/**Method sending a message to the client
	 * 
	 *@param 
	 */
	public void sendMessageToClient(String sMessage)
	{
		try 
		{
			this.myComManager.writeConsoleLog("Message envoye : " + sMessage);
			NetworkFlow.writeMessageNet(out, sMessage);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		bRunLink = true;
		String sChaine;
		
		while(bRunLink)
		{
			try 
			{
				sChaine = NetworkFlow.readMessageNet(this.inBuffer); //Lecture des messages venant du client
				proccesingOnReception(sChaine);
				Thread.yield();
			} 
			catch(SocketException e2)
			{
				notifyObserver();
				try 
				{
					this.socklink.close();
				} 
				catch (IOException e1) 
				{
					//e1.printStackTrace();
					this.myComManager.writeConsoleLog("Problem : "+e1.toString());
					//System.out.println("Problem : "+e1.toString());
				}
				this.myComManager.writeConsoleLog("Problem : "+e2.toString());
				//System.out.println("Problem : "+e2.toString());
				bRunLink = false;
			}
			catch(IOException e) 
			{
				notifyObserver();
				// TODO Auto-generated catch block
				try 
				{
					this.socklink.close();
				} 
				catch (IOException e1) 
				{
					this.myComManager.writeConsoleLog("Problem : "+e1.toString());
					//System.out.println("Problem : "+e1.toString());
				}
				//System.out.println("Problem : "+e.toString());
				this.myComManager.writeConsoleLog("Problem : "+e.toString());
				bRunLink = false;
			}
		}
	}

	@Override
	public void notifyObserver() {
		this.myComManager.logoutPerformed(this);
	}
}
