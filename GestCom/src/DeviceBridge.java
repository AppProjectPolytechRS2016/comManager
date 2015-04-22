import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class DeviceBridge implements Runnable{
	
	private ComManager myComManager;
	private ExecutorService myExecServ;
	private ServerSocket sockServDevice;
	private boolean bRunDeviceBridge;
	
	/**Constructor of DeviceBridge's object
	 * 
	 * @param myComManager
	 * @param myExecServ
	 */
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
		//System.out.println("Device Bridge online!");
		this.myComManager.writeConsoleLog("Device Bridge online!");
		bRunDeviceBridge = true;
		try
		{
			sockServDevice.setSoTimeout(0); //Attente infini sur accept()
			
			while (bRunDeviceBridge)
			{
				//System.out.println("Waiting for device ...");
				this.myComManager.writeConsoleLog("Waiting for device ...");
				Socket sockcli = sockServDevice.accept();  //Attente de la connexion d'un client
				//System.out.println("Device connection ok");
				this.myComManager.writeConsoleLog("Device connection ok");
				myExecServ.execute(new DeviceLink(myExecServ, sockcli, myComManager));//On execute la methode run() de l'objet de communication
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
