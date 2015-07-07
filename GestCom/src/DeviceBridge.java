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
	 * @author Jerome
	 * @param myComManager
	 * @param myExecServ
	 */
	public DeviceBridge(ComManager myComManager, ExecutorService myExecServ) 
	{
		this.myComManager = myComManager;
		this.myExecServ = myExecServ;
		
		try 
		{
			sockServDevice = new ServerSocket (6020);  //Definition of where listening
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}  
		
	}

	public void run(){
		//System.out.println("Device Bridge online!");
		this.myComManager.writeConsoleLog("Device Bridge online!");
		bRunDeviceBridge = true;
		try
		{
			sockServDevice.setSoTimeout(0); //Infinite waiting on accept()
			
			while (bRunDeviceBridge)
			{
				//System.out.println("Waiting for device ...");
				this.myComManager.writeConsoleLog("Waiting for device ...");
				Socket sockcli = sockServDevice.accept();  //Waiting for Device connection
				//System.out.println("Device connection ok");
				this.myComManager.writeConsoleLog("Device connection ok");
				myExecServ.execute(new DeviceLink(myExecServ, sockcli, myComManager));//Execute run() method of the DeviceLink Object
			}
			sockServDevice.close();
		}
		catch (IOException ex) 
		{ 
			try 
			{
				sockServDevice.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
