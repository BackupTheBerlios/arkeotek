/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import java.awt.event.MouseEvent;

import javax.swing.JTree;

import ontologyEditor.DisplayManager;
import ontologyEditor.gui.treeviews.ConceptualTreeModel;
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
		super(new ConceptualTreeModel(), new OntologyNavigationPanel());
    }
    
	protected void performMouseClicked(MouseEvent e) {
		((OntologyNavigationPanel) this.navigationPanel).rollFirstPanel((LinkableElement)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
		DisplayManager.getInstance().reflectNavigation((LinkableElement)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
	}
	
	public void elementRemoved(LinkableElement element, int index)
	{
		if (element instanceof Concept)
			((ConceptualTreeModel) this.tree.getModel()).fireTreeNodesRemoved(element, index);
	}

	@Override
	public void reflectNavigation(LinkableElement element)
	{		
		this.navigationPanel.reflectNavigation(element);
	}
	
	
}
