package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.ApplicationManager.Request;
import arkeotek.ontology.LinkableElement;

/**
 * @author sanmartin
 * affiche le popup relatif à la table des lemmes
 */

public class PopupLemmaTable extends JPopupMenu implements ActionListener{

	private JMenuItem fusion;
	private JMenuItem recherche;
	private JMenuItem ajouter;
	private JMenuItem supprimer;
	private JTable table;
	
	public PopupLemmaTable(JTable table) {
		this.table=table;
		// si le nombre de lemme selectionné est superieur a 1 alors on affiche la fonction fucionner
		if (table.getSelectedRowCount()>1)
		{
			this.fusion = new JMenuItem ("Fusionner les lemmes") ;
			this.add (this.fusion) ;
			this.fusion.addActionListener (this) ;
		}
		// sinon on affiche les autres fonctions
		else
		{
			this.recherche = new JMenuItem ("Recherche un lemme du corpus") ;
			this.add (this.recherche) ;
			this.recherche.addActionListener (this) ; 
			
			this.addSeparator();
			
			this.supprimer = new JMenuItem ("Supprimer le terme") ;
			this.add (this.supprimer) ;
			this.supprimer.addActionListener (this) ;  
			
			this.ajouter = new JMenuItem ("Ajouter un terme") ;
			this.add (this.ajouter) ;
			this.ajouter.addActionListener (this) ; 
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// fusion des lemmes
		if (source == this.fusion)
		{
			ApplicationManager.getApplicationManager().manageRequest(Request.LEMMAS_FUSION);
		}
		// recherche de lemme
		else if (source == this.recherche)
		{
			ApplicationManager.getApplicationManager().manageRequest(Request.LEMMA_SEARCH);
		}
		// ajout d'un lemme
		else if (source == this.ajouter)
		{
			ApplicationManager.getApplicationManager().manageRequest(Request.CREATE_NEW_LEMMA);
		}
		// supprimer un lemme
		else if (source == this.supprimer)
		{
			Object[] options = {"Oui", "Non"};
			int choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Vous êtes sur le point de supprimer définitivement le concept. Désirez-vous continuer?", "Avertissement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			if (choice == 0)
			{
				try {
					ApplicationManager.ontology.unlinkElement((LinkableElement)table.getValueAt(table.getSelectedRow(),0));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				DisplayManager.mainFrame.refresh();
			}
		}
		
	}
	

}
