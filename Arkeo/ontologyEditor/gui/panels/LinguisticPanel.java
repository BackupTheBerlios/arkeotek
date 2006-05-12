/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import java.awt.event.MouseEvent;

import javax.swing.JTree;

import ontologyEditor.DisplayManager;
import ontologyEditor.gui.treeviews.LinguisticTreeModel;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LinguisticPanel extends AbstractPanel {
    
    /** Creates new form MainFrame */
    public LinguisticPanel() {
		super(new LinguisticTreeModel(), new LinguisticNavigationPanel());
    }
    
	protected void performMouseClicked(MouseEvent e) {
		if (((JTree)e.getSource()).getSelectionPath().getLastPathComponent() instanceof Lemma)
		{
			((LinguisticNavigationPanel) this.navigationPanel).fillTable(((Lemma)((JTree)e.getSource()).getSelectionPath().getLastPathComponent()));
			DisplayManager.getInstance().reflectNavigation((LinkableElement)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
		}
	}
	
	public void elementRemoved(LinkableElement element, int index)
	{
		if (element instanceof Lemma)
			((LinguisticTreeModel) this.tree.getModel()).fireTreeNodesRemoved(element, index);
	}

	@Override
	public void reflectNavigation(LinkableElement element)
	{
		// TODO Auto-generated method stub
		
	}
}
