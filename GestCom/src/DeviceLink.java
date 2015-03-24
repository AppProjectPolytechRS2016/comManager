import java.net.Socket;
import java.util.concurrent.ExecutorService;




public class DeviceLink extends Link {

	public DeviceLink(ExecutorService es, Socket sockcli,
			ComManager myComManager) {
		super(es, sockcli, myComManager);
		// TODO Auto-generated constructor stub
	}
}
