import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class DeviceBridge implements Runnable{
	
	private ComManager myComManager;
	private ExecutorService myExecServ;
	private DeviceLink newDeviceLink;
	private ServerSocket sockServDevice;
	private boolean bRunDeviceBridge;
	
	public DeviceBridge(ComManager myComManager, ExecutorService myExecServ) {
		this.myComManager = myComManager;
		this.myExecServ = myExecServ;
		
		try {
			sockServDevice = new ServerSocket (6020);  //Definition du port d'ecoute du serveur
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}

	public void run(){
		int nb_clients = 0;
		System.out.println("Device Bridge online!");
		bRunDeviceBridge = true;
		try
		{
			sockServDevice.setSoTimeout(0); //Attente infini sur accept()
			
			while (bRunDeviceBridge)
			{
				System.out.println("Waiting for device ...");
				Socket sockcli = sockServDevice.accept();  //Attente de la connexion d'un client
				nb_clients++;
				System.out.println("Device connection ok");
				newDeviceLink = new DeviceLink(myExecServ, sockcli, myComManager);  //Creation de l'objet de communication avec le client
				//this.myComManager.addDevice(newDeviceLink);    						//add DeviceLink to myComManager's list
				this.newDeviceLink.idClientServeur = nb_clients;
				myExecServ.execute(newDeviceLink);          //On execute la methode run() de l'objet de communication
			}
			sockServDevice.close();
		}
		catch (IOException ex) { 
			try 
			{
				sockServDevice.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
