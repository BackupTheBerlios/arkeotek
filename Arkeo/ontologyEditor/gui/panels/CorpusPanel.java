/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import java.awt.event.MouseEvent;

import javax.swing.JTree;

import ontologyEditor.DisplayManager;
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
    public CorpusPanel() {
		super(new CorpusTreeModel(), new CorpusNavigationPanel());
    }
    
	protected void performMouseClicked(MouseEvent e) {
		if (((JTree)e.getSource()).getSelectionPath().getLastPathComponent() instanceof DocumentPart)
		{
			((CorpusNavigationPanel) this.navigationPanel).fillTable(((DocumentPart)((JTree)e.getSource()).getSelectionPath().getLastPathComponent()));
			DisplayManager.getInstance().reflectNavigation((DocumentPart)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
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
	
}
