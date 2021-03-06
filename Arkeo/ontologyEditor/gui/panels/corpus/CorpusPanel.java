/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels.corpus;

import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.popup.PopupDocumentPartTree;
import ontologyEditor.gui.model.treeModel.CorpusTreeModel;
import ontologyEditor.gui.panels.AbstractPanel;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;

/**
 * Julien Sanmartin
 * Classe permattant d'afficher l'arbre des concepts
 */

public class CorpusPanel extends AbstractPanel {
    /** Creates new form MainFrame */
    
	private int vue;
	
	public CorpusPanel() {
		super(new CorpusTreeModel(),new CorpusNavigationPanel());
		this.vue=0;
    }
    
	 // des qu'on clique sur un concepts dans l'arbre
	protected void performMouseClicked(MouseEvent e) {
		if (ApplicationManager.ontology!=null)
		{
			if (((JTree)e.getSource()).getLastSelectedPathComponent()!=null)
			{
				DocumentPart document=(DocumentPart)((DefaultMutableTreeNode)((JTree)e.getSource()).getLastSelectedPathComponent()).getUserObject();//  getLastSelectedPathComponent().toString();
				this.getTree().setToolTipText(document.getName());
				if (e.getButton()==MouseEvent.BUTTON1)
				{
					((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptIndexant(document);
					((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptPotentiel(document);
					((CorpusNavigationPanel) this.navigationPanel).remplirTableLemmeLier(document);
					((CorpusNavigationPanel) this.navigationPanel).remplirTableimages(document);
					if (document.getCommentaire()!=null)
					{
						((CorpusNavigationPanel) this.navigationPanel).getTxtArea_docComm().setText(document.getCommentaire().toString());
					}
					else
						((CorpusNavigationPanel) this.navigationPanel).getTxtArea_docComm().setText("");
					
					//((OntologyNavigationPanel) this.navigationPanel).rollFirstPanel((LinkableElement)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
					DisplayManager.getInstance().reflectNavigation(document);
				}
				else if (e.getButton()==MouseEvent.BUTTON3)
				{
					PopupDocumentPartTree popup=new PopupDocumentPartTree(document);
					e.consume();
					// afficher le menu contextuel
					popup.show(this, e.getX(), e.getY());
				}
			}
		}
	}

	@Override
	public void elementRemoved(LinkableElement element, int index)
	{
		//no need to implement this method		
	}

	@Override
	public void reflectNavigation(LinkableElement element)
	{
		//empty corpse
		
	}
	
	public void refresh() {
		
//		if (vue==0)
//		{
//			((CorpusTreeModel)this.getTree().getModel()).setRacine(new DefaultMutableTreeNode(null));
//			this.getTree().removeAll();
//			((CorpusTreeModel)this.getTree().getModel()).remplirArbreDocumentSeq();
//			this.getTree().updateUI();
//		}
//		else
//		{
//			this.getTree().removeAll();
//			((CorpusTreeModel)this.getTree().getModel()).setRacine(new DefaultMutableTreeNode(null));
//			((CorpusTreeModel)this.getTree().getModel()).remplirArbreDocumentId();
//			this.getTree().updateUI();
//		}
		if (ApplicationManager.ontology!=null)
			((CorpusNavigationPanel)this.getNavigationPanel()).refresh();
	}

	public int getVue() {
		return vue;
	}

	public void setVue(int vue) {
		this.vue = vue;
		((CorpusTreeModel)this.getTree().getModel()).setVue(vue);
	}
	
}
