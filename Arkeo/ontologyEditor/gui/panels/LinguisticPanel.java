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
import ontologyEditor.gui.tables.LemmaTableModel;
import ontologyEditor.gui.tables.LinkableElementTable;
import ontologyEditor.gui.treeviews.CorpusTM;
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
		super(new LemmaTableModel(), new LinguisticNavigationPanel());
    }
    
	protected void performMouseClicked(MouseEvent e) {
			LinkableElement lemme=((Lemma)((LinkableElementTable)e.getSource()).getValueAt(((LinkableElementTable)e.getSource()).getSelectedRow(),0));
			((LinguisticNavigationPanel) this.navigationPanel).remplirTableLemmeParent(lemme);
			((LinguisticNavigationPanel) this.navigationPanel).remplirTableLemmeLier(lemme);
			((LinguisticNavigationPanel) this.navigationPanel).remplirTableConcept(lemme);
			((LinguisticNavigationPanel) this.navigationPanel).remplirTableDocumentParent(lemme);
			DisplayManager.getInstance().reflectNavigation(lemme);
	}
	
	public void elementRemoved(LinkableElement element, int index)
	{
		//if (element instanceof Lemma)
			//((LinguisticTreeModel) this.tree.getModel()).fireTreeNodesRemoved(element, index);
	}

	@Override
	public void reflectNavigation(LinkableElement element)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		((LemmaTableModel)this.getTable().getModel()).remplirTableLemme();
		if (ApplicationManager.ontology!=null)
			((LinguisticNavigationPanel)this.getNavigationPanel()).refresh();
	}
}
