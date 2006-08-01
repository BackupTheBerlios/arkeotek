package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.FenetreAjouterConceptFils;
import ontologyEditor.gui.dialogs.FenetreRenommerConcept;
import arkeotek.ontology.LinkableElement;

/**
 * @author sanmartin
 * permet d'afficher le popup lié à l'arbre des concepts
 */

public class PopupArbreConcept extends JPopupMenu implements ActionListener{

	private JMenuItem ajouter;
	private JMenuItem supprimer;
	private JMenuItem renommer;
	private LinkableElement concept;
	private int panel;
	
	public PopupArbreConcept(LinkableElement concept, int panel) {
		this.concept=concept;
		this.panel=panel;
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
		// si on renomme ouverture d'une fenetre
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
					DefaultMutableTreeNode courant=((DefaultMutableTreeNode)DisplayManager.mainFrame.getPanel(0).getTree().getLastSelectedPathComponent());
					courant.removeFromParent();
					DisplayManager.mainFrame.getPanel(panel).getTree().removeSelectionPath(DisplayManager.mainFrame.getPanel(panel).getTree().getSelectionPath());
					DisplayManager.mainFrame.getPanel(panel).getTree().updateUI();
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		// si on ajoute un concept
		else if (source== this.ajouter)
		{
			FenetreAjouterConceptFils fsc=new FenetreAjouterConceptFils(this.concept,panel);
			fsc.show();
		}
	}

	public int getPanel() {
		return panel;
	}

	public void setPanel(int panel) {
		this.panel = panel;
	}

}
