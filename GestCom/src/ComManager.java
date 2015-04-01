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
	
	public ComManager(ExecutorService es)
	{
		this.es = es;

		try
		{
			//LocaleAdresse = InetAddress.getLocalHost();  //Recuperation de l'adresse Ip
            System.out.println("ComManager address is : "+InetAddress.getLocalHost().getHostAddress() );
		}
		catch (IOException ex) {}
		
		this.myDeviceBridge = new DeviceBridge(this, es);
		this.myRobotBridge = new RobotBridge(this, es);
		es.execute(myDeviceBridge);
		es.execute(myRobotBridge);
	}
	
	/** Methode permettant de transmettre au destinataire un message provenant d'un client */
	public void transmissionMessage(JSONObject objJson, Object obj) {
		int index; 
		
		if(obj.getClass() == DeviceLink.class){
			index = findRobot((String)objJson.get("To"));
			this.arRobotLink.get(index).envoieMessageClient(objJson.toJSONString());
		}
		else if (obj.getClass() ==  RobotLink.class){
			index = findDevice((String)objJson.get("To"));
			this.arDeviceLink.get(index).envoieMessageClient(objJson.toJSONString());
		}
		else{
			System.out.println("Error while routing message !!!");
		}	
	}
	public int findRobot(String sIp){
		int index = 0;
		
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
	public int findDevice(String sIp){
		int index = 0;
		
		for(int iBcl = 0; iBcl < this.arDeviceLink.size(); iBcl++)
		{
			System.out.println(this.arDeviceLink.get(iBcl).sIpClient);
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

	public static void main (String args[])
	{
		ExecutorService es = Executors.newFixedThreadPool(13); //Allow 10 connections (devices and robots mingled)  
		ComManager comManager = new ComManager(es); //ComManager's instantiation
	}

	@Override
	public void logoutPerformed(Object obj) {
		if(obj.getClass() == DeviceLink.class)
		{
			
		}
		
	}
}
