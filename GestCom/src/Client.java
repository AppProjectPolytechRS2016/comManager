import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.simple.JSONObject;

public class Client implements Runnable
{
	//Client attributes
	private int Socketport = 6030;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean bRun;
	private ExecutorService es;
	private Socket sockcli = null;
	
	public Client (ExecutorService es)
	{
		this.es = es;
	}

	public int connexion(String url)
	{
		try{
			sockcli = new Socket (url, Socketport);
		}
		catch (IOException ex){ 
			return -1; 
		}

		if(sockcli.isConnected()){
			try {
				this.in = new DataInputStream(sockcli.getInputStream());
				this.out = new DataOutputStream(sockcli.getOutputStream());
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public void traitementReception(String sMessage){
	}
	
	public void writeMessage(String sLeMessage){
		try {
			NetworkFlow.writeMessage(out, sLeMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deco(){
		try {
			System.out.println("1");
			this.sockcli.close();
			System.out.println("2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run ()
	{	
		bRun  = true;
//		while(sockcli.isConnected() && bRun)
//		{
//			try { 
//				traitementReception(Flux.lectureMessage(in)); //Envoie du message
//			}
//			catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		JSONObject json = new JSONObject();
		json.put("toto", "tata");
		this.writeMessage(json.toString());
		System.out.println("Fin de l ecoute"+json.toString());
		try {
			sockcli.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main (String args[]) throws Exception
	{
		int iTestCo;
		ExecutorService es = Executors.newFixedThreadPool(3);
		Client client = new Client(es);
		
		iTestCo = client.connexion("127.0.0.1");
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(client);
		}
	}
}
