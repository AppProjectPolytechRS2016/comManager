import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class RobotBridge implements Runnable {
	
	private ComManager myComManager;
	private ExecutorService myExecServ;
	private RobotLink newRobotLink;
	private ServerSocket sockServRobot;
	private boolean bRunRobotBridge;

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
		int nb_clients = 0;
		System.out.println("Robot Bridge online!");
		bRunRobotBridge = true;
		try
		{
			sockServRobot.setSoTimeout(0); //Attente infini sur accept()
			
			while (bRunRobotBridge)
			{
				System.out.println("Waiting for robot ...");
				Socket sockcli = sockServRobot.accept();  //Attente de la connexion d'un client
				nb_clients++;
				System.out.println("Robot connection ok");
				newRobotLink = new RobotLink(myExecServ, sockcli, myComManager);  //Creation de l'objet de communication avec le client
				this.myComManager.addRobot(newRobotLink);   						//add Robotlink to myComManager's list
				this.newRobotLink.idClientServeur = nb_clients;
				myExecServ.execute(newRobotLink);          //On execute la methode run() de l'objet de communication
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
