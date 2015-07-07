import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class RobotBridge implements Runnable 
{	
	private ComManager myComManager;
	private ExecutorService myExecServ;
	private ServerSocket sockServRobot;
	private boolean bRunRobotBridge;

	/**Constructor of Link's object
	 * @author Jerome
	 * @param myComManager
	 * @param myExecServ
	 */
	public RobotBridge(ComManager myComManager, ExecutorService myExecServ) {
		this.myComManager = myComManager;
		this.myExecServ = myExecServ;
		
		try 
		{
			sockServRobot = new ServerSocket (6030);   //Definition of where listening
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}  
		
	}
	
	public void run(){
		//System.out.println("Robot Bridge online!");
		this.myComManager.writeConsoleLog("Robot Bridge online!");
		bRunRobotBridge = true;
		try
		{
			sockServRobot.setSoTimeout(0); //Infinite waiting on accept()
			
			while (bRunRobotBridge)
			{
				//System.out.println("Waiting for robot ...");
				this.myComManager.writeConsoleLog("Waiting for robot ...");
				Socket sockcli = sockServRobot.accept();  //Waiting for Robot connection
				//System.out.println("Robot connection ok");
				this.myComManager.writeConsoleLog("Robot connection ok");
				myExecServ.execute(new RobotLink(myExecServ, sockcli, myComManager));//Execute run() method of the DeviceLink Object
			}
			sockServRobot.close();
		}
		catch (IOException ex) 
		{ 
			try 
			{
				sockServRobot.close();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
