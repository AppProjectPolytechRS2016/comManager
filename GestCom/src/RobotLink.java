import java.net.Socket;
import java.util.concurrent.ExecutorService;


public class RobotLink extends Link{

	public RobotLink(ExecutorService es, Socket sockcli, ComManager myComManager) {
		super(es, sockcli, myComManager);
		// TODO Auto-generated constructor stub
	}
	
	/**Methode traitant les message arrivant vers le serveur et discriminant les type de message */
	public void traitementReception(String sMessage)
	{
		System.out.println("Message recu par le serveur : "+sMessage);
	}
}
