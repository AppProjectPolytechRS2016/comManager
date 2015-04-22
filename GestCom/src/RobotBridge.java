import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class RobotBridge implements Runnable {
	
	private ComManager myComManager;
	private ExecutorService myExecServ;
	private ServerSocket sockServRobot;
	private boolean bRunRobotBridge;

	/**Constructor of Link's object
	 * 
	 * @param myComManager
	 * @param myExecServ
	 */
	public RobotBridge(ComManager myComManager, ExecutorService myExecServ) {
		this.myComManager = myComManager;
		this.myExecServ = myExecServ;
		
		try {
			sockServRobot = new ServerSocket (6030);  //Definition du port d'ecoute du serveur
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	public void run(){
		//System.out.println("Robot Bridge online!");
		this.myComManager.writeConsoleLog("Robot Bridge online!");
		bRunRobotBridge = true;
		try
		{
			sockServRobot.setSoTimeout(0); //Attente infini sur accept()
			
			while (bRunRobotBridge)
			{
				//System.out.println("Waiting for robot ...");
				this.myComManager.writeConsoleLog("Waiting for robot ...");
				Socket sockcli = sockServRobot.accept();  //Attente de la connexion d'un client
				//System.out.println("Robot connection ok");
				this.myComManager.writeConsoleLog("Robot connection ok");
				myExecServ.execute(new RobotLink(myExecServ, sockcli, myComManager));//On execute la methode run() de l'objet de communication
			}
			sockServRobot.close();
		}
		catch (IOException ex) { 
			try 
			{
				sockServRobot.close();
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
