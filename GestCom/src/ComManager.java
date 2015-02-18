import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Toutes les taches utilisent l'interface Runnable

public class ComManager
{
	private ExecutorService es;            //Definition du groupe de threads

	//ComManager attribute
	private InetAddress LocaleAdresse ;  //Pour recuperer l'adresse Ip locale
	private RobotBridge myRobotBridge;
	private DeviceBridge myDeviceBridge;
	private ArrayList<RobotLink> arRobotLink = new ArrayList<RobotLink>(); //ArrayList contenant les clients du serveur
	private ArrayList<DeviceLink> arDeviceLink = new ArrayList<DeviceLink>(); //ArrayList contenant les clients du serveur
	
	public ComManager(ExecutorService es)
	{
		this.es = es;

		try
		{
			LocaleAdresse = InetAddress.getLocalHost();  //Recuperation de l'adresse Ip
            System.out.println("L'adresse locale est : "+LocaleAdresse );
		}
		catch (IOException ex) {}
		
		this.myDeviceBridge = new DeviceBridge(this, es);
		this.myRobotBridge = new RobotBridge(this, es);
		es.execute(myDeviceBridge);
		es.execute(myRobotBridge);
	}
	
	/** Methode permettant de transmettre au destinataire un message provenant d'un client */
	public void transmissionMessage(char idRecepteur, String sMessage){
		//TODO
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
		ExecutorService es = Executors.newFixedThreadPool(13); //Permet l'execution de 10 thread 
		ComManager comManager = new ComManager(es); //ComManager's instantiation
	}
}
