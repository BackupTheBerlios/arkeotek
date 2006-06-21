package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.FenetreAjouterConceptFils;
import ontologyEditor.gui.dialogs.FenetreRenommerConcept;
import ontologyEditor.gui.panels.EditionPanel;

import arkeotek.ontology.LinkableElement;

import com.sun.corba.se.spi.orbutil.fsm.Action;

public class PopupArbreConcept extends JPopupMenu implements ActionListener{

	private JMenuItem ajouter;
	private JMenuItem supprimer;
	private JMenuItem renommer;
	private LinkableElement concept;
	
	public PopupArbreConcept(LinkableElement concept) {
		this.concept=concept;
		
		this.renommer = new JMenuItem ("Renommer le concept") ;
		this.add (this.renommer) ;
		this.renommer.addActionListener (this) ;               
		
		this.supprimer = new JMenuItem ("Supprimer le concept") ;
		this.add (this.supprimer) ;
		this.supprimer.addActionListener (this) ;  
		
		this.ajouter = new JMenuItem ("Ajouter un concept fils") ;
		this.add (this.ajouter) ;
		this.ajouter.addActionListener (this) ;  
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// si on renomme ouverte d'une fenetre
		if (source == this.renommer)
		{
			FenetreRenommerConcept fsc=new FenetreRenommerConcept(this.concept);
			fsc.show();
		}
		//sinon on supprime un concept 
		else if (source == this.supprimer)
		{
			Object[] options = {"Oui", "Non"};
			int choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Vous êtes sur le point de supprimer définitivement le concept. Désirez-vous continuer?", "Avertissement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			if (choice == 0)
			{
				try {
					ApplicationManager.ontology.unlinkElement(concept);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				DisplayManager.mainFrame.refresh();
			}
		}
		// si on ajoute un concept
		else if (source== this.ajouter)
		{
			FenetreAjouterConceptFils fsc=new FenetreAjouterConceptFils(this.concept);
			fsc.show();
		}
	}

}
