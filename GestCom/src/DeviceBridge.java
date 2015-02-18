import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;


public class DeviceBridge implements Runnable{
	
	private ComManager myComManager;
	private ExecutorService myExecServ;
	private DeviceLink newDeviceLink;
	private ServerSocket sockServDevice;
	
	public DeviceBridge(ComManager myComManager, ExecutorService myExecServ) {
		this.myComManager = myComManager;
		this.myExecServ = myExecServ;
	}

	public void run(){
		this.myComManager.getArDeviceLink().add(newDeviceLink);
	}

}
