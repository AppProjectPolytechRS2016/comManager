import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;

// Toutes les taches utilisent l'interface Runnable

public class ComManager implements DecoObserver
{
	//ComManager attribute
	private RobotBridge myRobotBridge;
	private DeviceBridge myDeviceBridge;
	private ArrayList<RobotLink> arRobotLink = new ArrayList<RobotLink>(); //ArrayList contenant les clients du serveur
	private ArrayList<DeviceLink> arDeviceLink = new ArrayList<DeviceLink>(); //ArrayList contenant les clients du serveur
	private ExecutorService es;            //Definition du groupe de threads
	private Window myWindow;
	private String myIp;
	
	
	public ComManager(ExecutorService es)
	{
		this.es = es;
		try
		{
            this.myIp = InetAddress.getLocalHost().getHostAddress();
            //System.out.println("ComManager address is : " + this.myIp);
		}
		catch (IOException ex) {}
		this.myWindow = new Window(this);
		this.myWindow.add();		
	}
	
	public void startComManager ()
	{
		this.myDeviceBridge = new DeviceBridge(this, es);
		this.myRobotBridge = new RobotBridge(this, es);
		es.execute(myDeviceBridge);
		es.execute(myRobotBridge);
	}
	
	/** Methode permettant de transmettre au destinataire un message provenant d'un client */
	public void transmissionMessage(JSONObject objJson, Object obj) {
		int index; 
		
		if(obj.getClass() == DeviceLink.class) //if message is for a Robot
		{
			index = findRobot((String)objJson.get("To"));
			if(index != -1)
			{
				this.arRobotLink.get(index).envoieMessageClient(objJson.toJSONString());
			}
			else
			{
				this.writeConsoleLog("Error while routing message !!!");
			}
		}
		else if (obj.getClass() ==  RobotLink.class)//if message is for a Device
		{
			index = findDevice((String)objJson.get("To"));
			if(index != -1)
			{
				this.arDeviceLink.get(index).envoieMessageClient(objJson.toJSONString());
			}
			else
			{
				this.writeConsoleLog("Error while routing message !!!");
			}
		}
		else
		{
			System.out.println("Error while routing message !!!");
		}	
	}
	
	/**Find a robot by is IP*/
	public int findRobot(String sIp)
	{
		int index = -1;
		
		for(int iBcl = 0; iBcl < this.arRobotLink.size(); iBcl++)
		{
			if(this.arRobotLink.get(iBcl).sIpClient.equalsIgnoreCase(sIp))
			{
				index = iBcl;
				break;
			}
		}
		return index;
	}
	
	/**Find a device by is IP*/
	public int findDevice(String sIp)
	{
		int index = -1;
		
		for(int iBcl = 0; iBcl < this.arDeviceLink.size(); iBcl++)
		{
			if(this.arDeviceLink.get(iBcl).sIpClient.equals(sIp))
			{
				index = iBcl;
				break;
			}
		}
		return index;
	}
	
	public void addDevice (DeviceLink newDevice){
		this.arDeviceLink.add(newDevice);
	}

	public void addRobot (RobotLink newRobot){
		this.arRobotLink.add(newRobot);
	}
	
	public ArrayList<RobotLink> getArRobotLink() {
		return arRobotLink;
	}

	public ArrayList<DeviceLink> getArDeviceLink() {
		return arDeviceLink;
	}
	
	public void writeConsoleLog(String message){
		this.myWindow.writeTextC(message);
	}
	
	public String getMyIp() {
		return myIp;
	}

	@Override
	public void logoutPerformed(Object obj) 
	{
		if(obj.getClass() == DeviceLink.class)
		{
			//System.out.println("Deconnexion Device !!!");
			this.writeConsoleLog("Deconnexion Device !!!");
			this.arDeviceLink.remove(obj);
		}
		else if(obj.getClass() == RobotLink.class)
		{
			//System.out.println("Deconnexion Robot !!!");
			this.writeConsoleLog("Deconnexion Robot !!!");
			this.arRobotLink.remove(obj);
		}
		else
		{
			//System.out.println("Problem de type logout !!!");
			this.writeConsoleLog("Problem de type logout !!!");
		}
	}
	
	public static void main (String args[])
	{
		ExecutorService es = Executors.newFixedThreadPool(13); //Allow 10 connections (devices and robots mingled)  
		new ComManager(es); //ComManager's instantiation
	}
}