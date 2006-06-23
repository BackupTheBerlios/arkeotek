package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.ApplicationManager.Request;
import ontologyEditor.gui.panels.CorpusPanel;
import arkeotek.ontology.LinkableElement;

public class PopupTableLemme extends JPopupMenu implements ActionListener{

	private JMenuItem fusion;
	private JMenuItem recherche;
	private JMenuItem ajouter;
	private JMenuItem supprimer;
	private JTable table;
	
	public PopupTableLemme(JTable table) {
		this.table=table;
		if (table.getSelectedRowCount()>1)
		{
			this.fusion = new JMenuItem ("Fusionner les lemmes") ;
			this.add (this.fusion) ;
			this.fusion.addActionListener (this) ;
		}
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
		else if (source == this.recherche)
		{
			ApplicationManager.getApplicationManager().manageRequest(Request.LEMMA_SEARCH);
		}
		else if (source == this.ajouter)
		{
			ApplicationManager.getApplicationManager().manageRequest(Request.CREATE_NEW_LEMMA);
		}
		else if (source == this.supprimer)
		{
			Object[] options = {"Oui", "Non"};
			int choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Vous �tes sur le point de supprimer d�finitivement le concept. D�sirez-vous continuer?", "Avertissement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
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
