import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DeviceLink extends Link 
{
	/**Constructor of DeviceLink's object
	 * @author Jerome
	 * @param es
	 * @param sockcli
	 * @param myComManager
	 */
	public DeviceLink(ExecutorService es, Socket sockcli,
			ComManager myComManager) {
		super(es, sockcli, myComManager);
	}
	
	/**Add the IP address to the list of connected Devices on the GUI
	 * @author Jerome
	 */
	public void printIp()
	{
		this.myComManager.getMyWindow().writeListeDevice(sIpClient);
	}
	
	/**Check if the device is already in the list
	 * @author Jerome
	 */
	public void checkOld()
	{
		int index;
		index = this.myComManager.findDevice(sIpClient);
		if(index == -1) //if the device is not in the list
		{
			this.myComManager.addDevice(this);
		}
		else
		{
			this.myComManager.getArDeviceLink().remove(index); //Remove the previous one from the ArrauyList
			this.myComManager.getMyWindow().removeListeDevice(index); //Remove the previous one from the GUI list
			this.myComManager.addDevice(this); //Add the new
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sendInformation()
	{
		JSONObject myjson = new JSONObject();
		JSONArray RobotList = new JSONArray();
		String fromIp;
		ArrayList<RobotLink> arRobotList = this.myComManager.getArRobotLink();
		if(arRobotList.size() != 0) //Get if it is not empty the list connected Robots
		{
			for(int iBcl = 0; iBcl < arRobotList.size(); iBcl++)
			{
				RobotList.add(arRobotList.get(iBcl).sIpClient);
			}
			myjson.put("RobotList", RobotList);
		}
		else
		{
			myjson.put("RobotList", "null");
		}

		try 
		{
			fromIp = InetAddress.getLocalHost().getHostAddress();
			myjson.put("From",fromIp);
			myjson.put("To", this.sIpClient);
			myjson.put("MsgType", "Order");
			myjson.put("OrderName","UpdateList"); 
			this.sendMessageToClient(myjson.toJSONString()); //Send to the device the list of connected Robots
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		}
	}
}