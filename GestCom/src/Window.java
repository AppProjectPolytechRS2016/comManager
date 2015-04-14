import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener
{

	//Declaration des attributs
	
	JButton bSortie = new JButton("Sortir");
	JLabel l1 = new JLabel("Bonjour");
	JRadioButton radioButton1 = new JRadioButton("Fonction 1",true);
	JRadioButton radioButton2 = new JRadioButton("Fonction 2",false);
	ButtonGroup group = new ButtonGroup();
	JDialog dialogue = new JDialog();
	private ComManager myComManager;
	
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
	private DefaultListModel jListModelDevice = new DefaultListModel();
	private JList jListDevice = new JList(jListModelDevice);
	
	//Pour Champ Device
	private JLabel jLabelRobot = new JLabel("Liste des Robots :");
	private DefaultListModel jListModelRobot = new DefaultListModel();
	private JList jListRobot = new JList(jListModelRobot);
	
	private Color couleur = new Color(255, 0, 140);
	
	public Window(ComManager comManager)
	{
		super("Gestionnaire de Communications");
		//Personnalisation de la fenetre
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(couleur);
				
		this.myComManager = comManager;
	}
	
	public void add()
	{
		//Ajout des �lements
		this.addPanel();
		this.addIPInfo();
		this.addBouton();
		this.addListeDevice();
		this.addListeRobot();
		this.addTextConsole();
		this.setVisible(true);
		//pack();
	}
	
	public void addPanel()
	{
		this.getContentPane().add(this.panTop,BorderLayout.NORTH);
		this.getContentPane().add(this.panBottom,BorderLayout.SOUTH);
		this.getContentPane().add(this.panLeft,BorderLayout.WEST);
		this.getContentPane().add(this.panCenter,BorderLayout.CENTER);
		this.getContentPane().add(this.panRight,BorderLayout.EAST);
	}
	
	public void dialog()
	{
		int reponse = JOptionPane.showConfirmDialog(null, "Voulez vous quitter ce programme ?", 
				"Sortir", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
		if(reponse == JOptionPane.YES_OPTION){
			System.exit(0);  //Fin de l'aaplication
		}
	}
	
	public void addIPInfo ()
	{
		this.panTop.add(jLabelIp);
		this.jTextIp.append(this.myComManager.getMyIp());
		this.jTextIp.setEditable(false);
		this.panTop.add(jTextIp);
		
	}
	
	public void addBouton ()
	{
		this.panCenter.add(jButtonStart);
		jButtonStart.addActionListener(this);
	}
	
	public void addTextConsole()
	{
		this.jTextAreaConsole.setEditable(false);
		this.jTextAreaConsole.setRows(5);
		this.panBottom.setLayout(new BoxLayout(panBottom, BoxLayout.PAGE_AXIS));
		this.panBottom.add(this.jLabelConsole);
		this.panBottom.add(this.jScrollPaneConsole);
	}
	
	
	public void addListeDevice(){
		this.panLeft.setLayout(new BoxLayout(panLeft, BoxLayout.PAGE_AXIS));
		this.panLeft.add(jLabelDevice);
		this.panLeft.add(jListDevice);
	}
	
	public void addListeRobot(){
		this.panRight.setLayout(new BoxLayout(panRight, BoxLayout.PAGE_AXIS));
		this.panRight.add(jLabelRobot);
		this.panRight.add(jListRobot);
	}
	
	public void writeTextC(String Text)
	{
		this.jTextAreaConsole.append(Text+'\n');
	}
	
	public void writeListeDevice(String name)
	{
		this.jListModelDevice.addElement(name);
	}

	public void writeListeRobot(String name)
	{
		this.jListModelRobot.addElement(name);
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

