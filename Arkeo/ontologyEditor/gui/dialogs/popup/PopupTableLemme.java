package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.ApplicationManager.Request;
import ontologyEditor.gui.panels.CorpusPanel;
import arkeotek.ontology.LinkableElement;

public class PopupTableLemme extends JPopupMenu implements ActionListener{

	private JMenuItem fusion;
	//private ArrayList<LinkableElement> lemmes;
	
	public PopupTableLemme(/*ArrayList<LinkableElement> lemmes*/) {
		//this.lemmes=lemmes;
		
		this.fusion = new JMenuItem ("Fusionner les lemmes") ;
		this.add (this.fusion) ;
		this.fusion.addActionListener (this) ;                 
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// fusion des lemmes
		if (source == this.fusion)
		{
			ApplicationManager.getApplicationManager().manageRequest(Request.LEMMAS_FUSION);
		}
	}
	

}
