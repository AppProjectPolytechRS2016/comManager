import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DeviceLink extends Link {

	public DeviceLink(ExecutorService es, Socket sockcli,
			ComManager myComManager) {
		super(es, sockcli, myComManager);
		// TODO Auto-generated constructor stub
	}
	
	public void sendRobotList(){
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
			this.envoieMessageClient(myjson.toJSONString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}