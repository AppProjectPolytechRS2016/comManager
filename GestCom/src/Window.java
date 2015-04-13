import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener, ListSelectionListener{

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
	
	private Color couleur = new Color(255, 0, 140);
	
	public Window(ComManager comManager)
	{
		super("Hello world");
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
		//this.addListe();
		this.addTextConsole();
		this.setVisible(true);
		pack();
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
		this.panBottom.setLayout(new BoxLayout(panBottom, BoxLayout.PAGE_AXIS));
		this.panBottom.add(this.jLabelConsole);
		this.panBottom.add(this.jScrollPaneConsole);
	}
	
	/*
	public void addListe(){
		this.panListe.add(liste1);
		this.getContentPane().add(panListe,BorderLayout.EAST);
		liste1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		liste1.addListSelectionListener(this);
		
		//Comportement au lancement de l'application
		if(radioButton2.isSelected()){
			liste1.setEnabled(true);
		}
		else{
			liste1.setEnabled(false);
		}
	}*/
	
	public void writeTextC(String Text)
	{
		this.jTextAreaConsole.append(Text+'\n');
		pack();
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
	
	public void valueChanged(ListSelectionEvent arg0) {
		/*if(arg0.getValueIsAdjusting()==false){
			l1.setText((String)liste1.getSelectedValue());
		}*/
	}
}

