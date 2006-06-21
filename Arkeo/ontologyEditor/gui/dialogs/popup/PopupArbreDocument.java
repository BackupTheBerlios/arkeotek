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
import ontologyEditor.gui.panels.AbstractPanel;
import ontologyEditor.gui.panels.CorpusNavigationPanel;
import ontologyEditor.gui.panels.CorpusPanel;
import ontologyEditor.gui.treeviews.CorpusTM;
import arkeotek.ontology.LinkableElement;

public class PopupArbreDocument extends JPopupMenu implements ActionListener{

	private JMenuItem id;
	private JMenuItem seq;
	private LinkableElement document;
	
	public PopupArbreDocument(LinkableElement document) {
		this.document=document;
		
		this.id = new JMenuItem ("Vue Identifiant") ;
		this.add (this.id) ;
		this.id.addActionListener (this) ;               
		
		this.seq = new JMenuItem ("Vue Séquence") ;
		this.add (this.seq) ;
		this.seq.addActionListener (this) ;  
		 
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//on regarde dans quel panel 
		
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL) instanceof CorpusPanel)
		{
			// vue par id
			if (source == this.id)
			{
				//((CorpusTM)((AbstractPanel)DisplayManager.mainFrame.getPanel(panel)).getTree().getModel()).remplirArbreDocumentId();
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL)).setVue(1);
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).refresh();
			}
			//vue par seq 
			else if (source == this.seq)
			{
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL)).setVue(0);
				//((CorpusTM)((AbstractPanel)DisplayManager.mainFrame.getPanel(panel)).getTree().getModel()).remplirArbreDocumentSeq();
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).refresh();
			}
		}
		
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
		{
			// vue par id
			if (source == this.id)
			{
				//((CorpusTM)((AbstractPanel)DisplayManager.mainFrame.getPanel(panel)).getTree().getModel()).remplirArbreDocumentId();
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL)).setVue(1);
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).refresh();
			}
			//vue par seq 
			else if (source == this.seq)
			{
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL)).setVue(0);
				//((CorpusTM)((AbstractPanel)DisplayManager.mainFrame.getPanel(panel)).getTree().getModel()).remplirArbreDocumentSeq();
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).refresh();
			}
		}
		
	}

}
