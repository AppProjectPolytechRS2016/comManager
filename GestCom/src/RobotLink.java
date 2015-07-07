import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class RobotLink extends Link{

	/**Constructor of RobotLink's object
	 * 
	 * @param es
	 * @param sockcli
	 * @param myComManager
	 */
	public RobotLink(ExecutorService es, Socket sockcli, ComManager myComManager) {
		super(es, sockcli, myComManager);
	}
	
	public void printIp()
	{
		this.myComManager.getMyWindow().writeListeRobot(sIpClient);
	}
	
	public void checkOld()
	{
		int index;
		index = this.myComManager.findRobot(sIpClient);
		if(index == -1) //if the robot is not in the list
		{
			this.myComManager.addRobot(this);
		}
		else
		{
			this.myComManager.getArRobotLink().remove(index); //Remove the previous one from the ArrauyList
			this.myComManager.getMyWindow().removeListeRobot(index);//Remove the previous one from the GUI list
			this.myComManager.addRobot(this);//Add the new
		}
	}
}
