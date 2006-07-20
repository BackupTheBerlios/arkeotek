/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import java.awt.event.MouseEvent;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.popup.PopupTableLemme;
import ontologyEditor.gui.tables.LemmaTableModel;
import ontologyEditor.gui.tables.LinkableElementTable;
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
		if (ApplicationManager.ontology!=null)
		{
			if (e.getButton()==MouseEvent.BUTTON1)
			{
				LinkableElement lemme=((Lemma)((LinkableElementTable)e.getSource()).getValueAt(((LinkableElementTable)e.getSource()).getSelectedRow(),0));
				((LinguisticNavigationPanel) this.navigationPanel).remplirTableLemmeParent(lemme);
				((LinguisticNavigationPanel) this.navigationPanel).remplirTableLemmeLier(lemme);
				((LinguisticNavigationPanel) this.navigationPanel).remplirTableConcept(lemme);
				((LinguisticNavigationPanel) this.navigationPanel).remplirTableDocumentParent(lemme);
				((LinguisticNavigationPanel) this.navigationPanel).getPrecedent().add(lemme);
				DisplayManager.getInstance().reflectNavigation(lemme);
			}
			else if (e.getButton()==MouseEvent.BUTTON3)
			{
				PopupTableLemme popup=new PopupTableLemme(this.getTable());
				e.consume();
				// afficher le menu contextuel
				popup.show(this, e.getX(), e.getY());
			}
		}
		
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
