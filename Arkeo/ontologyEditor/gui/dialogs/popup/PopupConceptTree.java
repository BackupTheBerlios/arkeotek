package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.AddChidlConceptDialog;
import ontologyEditor.gui.dialogs.RenameConceptDialog;
import arkeotek.ontology.LinkableElement;

/**
 * @author sanmartin
 * permet d'afficher le popup lié à l'arbre des concepts
 */

public class PopupConceptTree extends JPopupMenu implements ActionListener{

	private JMenuItem ajouter;
	private JMenuItem supprimer;
	private JMenuItem renommer;
	private LinkableElement concept;
	private int panel;
	
	public PopupConceptTree(LinkableElement concept, int panel) {
		this.concept=concept;
		this.panel=panel;
		this.renommer = new JMenuItem (ApplicationManager.getApplicationManager().getTraduction("renametheconcept")) ;
		this.add (this.renommer) ;
		this.renommer.addActionListener (this) ;               
		
		this.supprimer = new JMenuItem (ApplicationManager.getApplicationManager().getTraduction("removetheconcept")) ;
		this.add (this.supprimer) ;
		this.supprimer.addActionListener (this) ;  
		
		this.ajouter = new JMenuItem (ApplicationManager.getApplicationManager().getTraduction("addchildconcept")) ;
		this.add (this.ajouter) ;
		this.ajouter.addActionListener (this) ;  
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// si on renomme ouverture d'une fenetre
		if (source == this.renommer)
		{
			RenameConceptDialog fsc=new RenameConceptDialog(this.concept);
			fsc.show();
		}
		//sinon on supprime un concept 
		else if (source == this.supprimer)
		{
			Object[] options = {ApplicationManager.getApplicationManager().getTraduction("yes"), ApplicationManager.getApplicationManager().getTraduction("no")};
			int choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, ApplicationManager.getApplicationManager().getTraduction("deletingconceptconfirm"), ApplicationManager.getApplicationManager().getTraduction("warning"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			if (choice == 0)
			{
				try {
					// on supprime le concept de l'ontology
					ApplicationManager.ontology.unlinkElement(concept);
					// on supprime le noeud dans l'arbre
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
			AddChidlConceptDialog fsc=new AddChidlConceptDialog(this.concept,panel);
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
