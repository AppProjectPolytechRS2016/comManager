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
	 * 
	 * @param es
	 * @param sockcli
	 * @param myComManager
	 */
	public DeviceLink(ExecutorService es, Socket sockcli,
			ComManager myComManager) {
		super(es, sockcli, myComManager);
		// TODO Auto-generated constructor stub
	}
	
	public void printIp()
	{
		this.myComManager.getMyWindow().writeListeDevice(sIpClient);
	}
	
	public void checkOld()
	{
		int index;
		index = this.myComManager.findDevice(sIpClient);
		if(index == -1)
		{
			this.myComManager.addDevice(this);
		}
		else
		{
			this.myComManager.getArDeviceLink().remove(index);
			this.myComManager.getMyWindow().removeListeDevice(index);
			this.myComManager.addDevice(this);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sendInformation(){
		JSONObject myjson = new JSONObject();
		JSONArray RobotList = new JSONArray();
		String fromIp;
		ArrayList<RobotLink> arRobotList = this.myComManager.getArRobotLink();
		if(arRobotList.size() != 0)
		{
			for(int iBcl = 0; iBcl < arRobotList.size(); iBcl++)
			{
				RobotList.add(arRobotList.get(iBcl).sIpClient);
			}
			myjson.put("RobotList", RobotList);
		}
		else{
			myjson.put("RobotList", "null");
		}

		try {
			fromIp = InetAddress.getLocalHost().getHostAddress();
			myjson.put("From",fromIp);
			myjson.put("To", this.sIpClient);
			myjson.put("MsgType", "Order");
			myjson.put("OrderName","UpdateList"); 
			this.sendMessageToClient(myjson.toJSONString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}