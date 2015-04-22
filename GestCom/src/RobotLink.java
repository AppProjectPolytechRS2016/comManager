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
		// TODO Auto-generated constructor stub
	}
	
	public void printIp()
	{
		this.myComManager.getMyWindow().writeListeRobot(sIpClient);
	}
	
	public void checkOld()
	{
		int index;
		index = this.myComManager.findRobot(sIpClient);
		if(index == -1)
		{
			this.myComManager.addRobot(this);
		}
		else
		{
			this.myComManager.getArRobotLink().remove(index);
			this.myComManager.getMyWindow().removeListeRobot(index);
			this.myComManager.addRobot(this);
		}
	}
}
