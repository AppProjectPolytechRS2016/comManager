import javax.swing.JFrame;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener, ListSelectionListener{

	//Declaration des attributs
	JButton b1 = new JButton("Clic");
	JButton bSortie = new JButton("Sortir");
	JLabel l1 = new JLabel("Bonjour");
	JList liste1 = new JList(new String[]
			{"Rouge", "Vert", "Bleu", "Jaune"});
	JRadioButton radioButton1 = new JRadioButton("Fonction 1",true);
	JRadioButton radioButton2 = new JRadioButton("Fonction 2",false);
	JPanel panRadio = new JPanel();
	JPanel panBouton = new JPanel();
	JPanel panLabel = new JPanel();
	JPanel panListe = new JPanel();
	ButtonGroup group = new ButtonGroup();
	JDialog dialogue = new JDialog();
	
	JTextArea textArea = new JTextArea(5, 20);
	JScrollPane scrollPane = new JScrollPane(textArea);
	JPanel panText= new JPanel();
	
	private int cpt = 0;
	private Color couleur = new Color(255, 0, 140);
	
	public Window(){
		super("Hello world");
		//Personnalisation de la fenetre
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(couleur);
		this.textArea.setEditable(false);
	}
	
	public void add(){
		//Ajout des �lements
		this.addBouton();
		//this.addLabel();
		this.addListe();
		this.addRadioBouton();
		this.addBoutonSortie();
		this.addTextConsole();
		this.setVisible(true);
		pack();
	}
	public void dialog(){
		int reponse = JOptionPane.showConfirmDialog(null, "Voulez vous quitter ce programme ?", 
				"Sortir", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
		if(reponse == JOptionPane.YES_OPTION){
			System.exit(0);  //Fin de l'aaplication
		}
	}
	public void addBouton (){
		//ajout du bouton au panel
		this.panBouton.add(b1);
		
		this.getContentPane().add(panBouton,BorderLayout.NORTH);
		b1.addActionListener(this);
		
		//Comportement au lancement de l'appli
		if(radioButton1.isSelected()){
			b1.setEnabled(true);
		}
		else{
			b1.setEnabled(false);
		}
	}
	public void addLabel(){
		this.panLabel.add(l1);
		this.getContentPane().add(panLabel,BorderLayout.SOUTH);
	}
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
	}
	public void addRadioBouton(){
		//definition de radioButton1
		radioButton1.setActionCommand("Prog1");
		radioButton1.setSelected(true);
		radioButton1.addActionListener(this);
		
		//definition de radioButton2
		radioButton2.setActionCommand("Prog2");
		radioButton2.setSelected(true);
		radioButton2.addActionListener(this);
		
		//Ajout au panel
		panRadio.add(radioButton1,BorderLayout.WEST);
		panRadio.add(radioButton2,BorderLayout.CENTER);
		this.getContentPane().add(panRadio,BorderLayout.WEST);
	    group.add(radioButton1);
	    group.add(radioButton2);
		
	}
	
	public void addBoutonSortie(){
		this.panBouton.add(bSortie);
		this.bSortie.addActionListener(this);
	}
	public void addTextConsole(){
		this.panText.add(scrollPane);
		this.getContentPane().add(panText,BorderLayout.WEST);
	}
	
	public void writeTextC(String Text)
	{
		this.textArea.append(Text+'\n');
		pack();
	}
	
	public static void main(String[] args) {
		
		//Creation de la fenetre
		Window fenetre = new Window();
		fenetre.add();
	}

	public void actionPerformed(ActionEvent arg0) {
			Object source = arg0.getSource();
			
			//Selection en fonction de l'emetteur de l'evenement
	        if (source == radioButton1) {
	            b1.setEnabled(true);
	            liste1.setEnabled(false);
	        } 
	        else if (source == radioButton2) {
	            liste1.setEnabled(true);
	            b1.setEnabled(false);
	        }
	        else if(source == b1){
	        	cpt ++;
				l1.setText("Vous avez cliqu� "+cpt+" fois");
	        }
	        else if(source == this.bSortie){
	        	this.dialog();
	        }
		}
	public void valueChanged(ListSelectionEvent arg0) {
		if(arg0.getValueIsAdjusting()==false){
			l1.setText((String)liste1.getSelectedValue());
		}
	}
}

