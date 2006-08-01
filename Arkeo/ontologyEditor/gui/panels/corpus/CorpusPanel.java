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
import ontologyEditor.gui.panels.AbstractPanel;
import ontologyEditor.gui.treeviews.CorpusTreeModel;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class CorpusPanel extends AbstractPanel {
    /** Creates new form MainFrame */
    
	private int vue;
	
	public CorpusPanel() {
		super(new CorpusTreeModel(),new CorpusNavigationPanel());
		this.vue=0;
    }
    
	protected void performMouseClicked(MouseEvent e) {
		if (ApplicationManager.ontology!=null)
		{
			if (((JTree)e.getSource()).getLastSelectedPathComponent()!=null)
			{
				String nomDoc=((JTree)e.getSource()).getLastSelectedPathComponent().toString();
				DocumentPart document;
				if (vue==1)
				{
					for (int i=0;i<ApplicationManager.ontology.get(DocumentPart.KEY).size();i++)
					{
						if (ApplicationManager.ontology.get(DocumentPart.KEY).get(i).getName().toString().equals(nomDoc))
						{
							document=(DocumentPart)ApplicationManager.ontology.get(DocumentPart.KEY).get(i);
							this.getTree().setToolTipText(document.toString());
							if (e.getButton()==MouseEvent.BUTTON1)
							{
								((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptIndexant(document);
								((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptPotentiel(document);
								((CorpusNavigationPanel) this.navigationPanel).remplirTableLemmeLier(document);
								((CorpusNavigationPanel) this.navigationPanel).remplirTableimages(document);
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
							break;
						}
					}
				}
				else
				{
					for (int i=0;i<ApplicationManager.ontology.get(DocumentPart.KEY).size();i++)
					{
						if (ApplicationManager.ontology.get(DocumentPart.KEY).get(i).toString().equals(nomDoc))
						{
							document=(DocumentPart)ApplicationManager.ontology.get(DocumentPart.KEY).get(i);
							this.getTree().setToolTipText(document.getName());
							if (e.getButton()==MouseEvent.BUTTON1)
							{
								((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptIndexant(document);
								((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptPotentiel(document);
								((CorpusNavigationPanel) this.navigationPanel).remplirTableLemmeLier(document);
								((CorpusNavigationPanel) this.navigationPanel).remplirTableimages(document);
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
							break;
						}
					}
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
		
		if (vue==0)
		{
			((CorpusTreeModel)this.getTree().getModel()).setRacine(new DefaultMutableTreeNode(null));
			this.getTree().removeAll();
			((CorpusTreeModel)this.getTree().getModel()).remplirArbreDocumentSeq();
			this.getTree().updateUI();
		}
		else
		{
			this.getTree().removeAll();
			((CorpusTreeModel)this.getTree().getModel()).setRacine(new DefaultMutableTreeNode(null));
			((CorpusTreeModel)this.getTree().getModel()).remplirArbreDocumentId();
			this.getTree().updateUI();
		}
		if (ApplicationManager.ontology!=null)
			((CorpusNavigationPanel)this.getNavigationPanel()).refresh();
	}

	public int getVue() {
		return vue;
	}

	public void setVue(int vue) {
		this.vue = vue;
	}
	
}
