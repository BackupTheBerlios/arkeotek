/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import java.awt.event.MouseEvent;

import javax.swing.JTree;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.treeviews.ConceptualTM;
import ontologyEditor.gui.treeviews.ConceptualTreeModel;
import ontologyEditor.gui.treeviews.CorpusTM;
import arkeotek.ontology.Concept;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class OntologyPanel extends AbstractPanel {
    
    /** Creates new form MainFrame */
    public OntologyPanel() {
		super(new ConceptualTM(),new OntologyNavigationPanel());
    }
    
	protected void performMouseClicked(MouseEvent e) {
		if (ApplicationManager.ontology!=null)
		{
			if (((JTree)e.getSource()).getLastSelectedPathComponent()!=null)
			{
				String nomConcept=((JTree)e.getSource()).getLastSelectedPathComponent().toString();
				Concept concept;
				for (int i=0;i<ApplicationManager.ontology.get(Concept.KEY).size();i++)
				{
					if (ApplicationManager.ontology.get(Concept.KEY).get(i).toString().equals(nomConcept))
					{
						concept=(Concept)ApplicationManager.ontology.get(Concept.KEY).get(i);
						((OntologyNavigationPanel) this.navigationPanel).remplirLabelPere(concept);
						((OntologyNavigationPanel) this.navigationPanel).remplirTableFils(concept);
						((OntologyNavigationPanel) this.navigationPanel).remplirTableLemme(concept);
						((OntologyNavigationPanel) this.navigationPanel).remplirTableDefini(concept);
						//((OntologyNavigationPanel) this.navigationPanel).rollFirstPanel((LinkableElement)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
						DisplayManager.getInstance().reflectNavigation(concept);
						return;
					}
				}
			}
		}
	}
	
	public void elementRemoved(LinkableElement element, int index)
	{
		if (element instanceof Concept)
			((ConceptualTreeModel) this.tree.getModel()).fireTreeNodesRemoved(element, index);
	}

	public void reflectNavigation(LinkableElement element)
	{		
		this.navigationPanel.reflectNavigation(element);
	}
	
	public void refresh() {
		
		this.getTree().removeAll();
		((ConceptualTM)this.getTree().getModel()).remplirArbreConcept();
		this.getTree().updateUI();
		if (ApplicationManager.ontology!=null)
			((OntologyNavigationPanel)this.getNavigationPanel()).refresh();
		this.updateUI();
	}
	
	
}
