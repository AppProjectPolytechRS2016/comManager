import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener
{

	//Declaration des attributs
	private ComManager myComManager;
	private Color textColor = new Color(0x86, 0x37, 0x2E);
	private Color backgroundColor = new Color(0x3D, 0x06, 0x06);
	private Color listColor = new Color(0xBA, 0x7F, 0x78);
	
	//Declaration de JPanels de structure
	private JPanel panTop = new JPanel();
	private JPanel panBottom = new JPanel();
	private JPanel panRight = new JPanel();
	private JPanel panLeft = new JPanel();
	private JPanel panCenter = new JPanel();
	
	//Pour champ IP
	private JTextArea jTextIp = new JTextArea();
	private JLabel jLabelIp = new JLabel("L'adresse IP du ComManager est :");
	
	//Pour Champ Bouton de connexion
	private JButton jButtonStart = new JButton("Start");

	//Pour Champ Console
	private JTextArea jTextAreaConsole = new JTextArea();
	private JScrollPane jScrollPaneConsole = new JScrollPane(jTextAreaConsole);
	private JLabel jLabelConsole = new JLabel("Message systeme :");
	
	//Pour Champ Device
	private JLabel jLabelDevice = new JLabel("Liste des Devices :");
	private DefaultListModel<String> jListModelDevice = new DefaultListModel<String>();
	private JList<String> jListDevice = new JList<String>(jListModelDevice);
	
	//Pour Champ Device
	private JLabel jLabelRobot = new JLabel("Liste des Robots :");
	private DefaultListModel<String> jListModelRobot = new DefaultListModel<String>();
	private JList<String> jListRobot = new JList<String>(jListModelRobot);
	
	/**Constructor of Window's object
	 * 
	 * @param comManager
	 */
	public Window(ComManager comManager)
	{
		super("APPRS2016 - Gestionnaire de Communications");
		//Personnalisation de la fenetre
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		this.myComManager = comManager;
		this.setBackground(backgroundColor);
	}
	
	/**Add components to window
	 * 
	 */
	public void add()
	{
		//Ajout des �lements
		this.addPanel();
		this.addIPInfo();
		this.addButton();
		this.addListeDevice();
		this.addListeRobot();
		this.addTextConsole();
		this.setVisible(true);
	}
	
	/**Add JPanel to JFrame and set
	 * 
	 */
	public void addPanel()
	{
		this.panBottom.setBackground(backgroundColor);
		this.panTop.setBackground(backgroundColor);
		this.panLeft.setBackground(backgroundColor);
		this.panCenter.setBackground(backgroundColor);
		this.panRight.setBackground(backgroundColor);
		this.getContentPane().add(this.panTop,BorderLayout.NORTH);
		this.getContentPane().add(this.panBottom,BorderLayout.SOUTH);
		this.getContentPane().add(this.panLeft,BorderLayout.WEST);
		this.getContentPane().add(this.panCenter,BorderLayout.CENTER);
		this.getContentPane().add(this.panRight,BorderLayout.EAST);
	}
	
	/**Add IP information to JPanel
	 * 
	 */
	public void addIPInfo ()
	{
		this.jLabelIp.setForeground(textColor);
		this.panTop.add(jLabelIp);
		this.jTextIp.append(this.myComManager.getMyIp());
		this.jTextIp.setEditable(false);
		this.jTextIp.setBackground(backgroundColor);
		this.jTextIp.setForeground(Color.white);
		this.panTop.add(jTextIp);
		
	}
	
	/**Add button to JPanel
	 * 
	 */
	public void addButton ()
	{
		this.panCenter.add(jButtonStart);
		jButtonStart.addActionListener(this);
	}
	
	/**Add elements to GUI's console
	 * 
	 */
	public void addTextConsole()
	{
		this.jLabelConsole.setForeground(textColor);
		this.jTextAreaConsole.setCaretPosition(this.jTextAreaConsole.getDocument().getLength());
		this.jTextAreaConsole.setEditable(false);
		this.jTextAreaConsole.setRows(10);
		this.jTextAreaConsole.setBackground(listColor);
		this.panBottom.setLayout(new BoxLayout(panBottom, BoxLayout.PAGE_AXIS));
		this.panBottom.add(this.jLabelConsole);
		this.panBottom.add(this.jScrollPaneConsole);
	}
	
	/**Add elements to Device's list of GUI
	 * 
	 */
	public void addListeDevice()
	{
		this.jLabelDevice.setForeground(textColor);
		this.jListDevice.setBackground(listColor);
		this.panLeft.setLayout(new BoxLayout(panLeft, BoxLayout.PAGE_AXIS));
		this.panLeft.add(jLabelDevice);
		this.panLeft.add(jListDevice);
	}
	
	/**Add elements to Robot's list of GUI
	 * 
	 */
	public void addListeRobot()
	{
		this.jListRobot.setBackground(listColor);
		this.jLabelRobot.setForeground(textColor);
		this.panRight.setLayout(new BoxLayout(panRight, BoxLayout.PAGE_AXIS));
		this.panRight.add(jLabelRobot);
		this.panRight.add(jListRobot);
	}
	
	public synchronized void writeTextC(String Text)
	{
		this.jTextAreaConsole.append(Text+"\n");
		this.jTextAreaConsole.setCaretPosition(this.jTextAreaConsole.getDocument().getLength());
	}
	
	public synchronized void writeListeDevice(String name)
	{
		this.jListModelDevice.addElement(name);
	}

	public synchronized void writeListeRobot(String name)
	{
		this.jListModelRobot.addElement(name);
	}
	
	public synchronized void removeListeDevice(int index)
	{
		this.jListModelDevice.removeElementAt(index);;
	}

	public synchronized void removeListeRobot(int index)
	{
		this.jListModelRobot.removeElementAt(index);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		
		//Selection en fonction de l'emetteur de l'evenement
		if (source == this.jButtonStart)
		{
			this.jButtonStart.setEnabled(false);
			this.myComManager.startComManager();
		}
	}
}

