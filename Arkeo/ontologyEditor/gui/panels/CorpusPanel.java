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
import ontologyEditor.gui.tables.LinkableElementTable;
import ontologyEditor.gui.treeviews.CorpusTM;
import ontologyEditor.gui.treeviews.CorpusTreeModel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class CorpusPanel extends AbstractPanel {
    /** Creates new form MainFrame */
    public CorpusPanel() {
		super(new CorpusTM(),new CorpusNavigationPanel());
    }
    
	protected void performMouseClicked(MouseEvent e) {
		if (ApplicationManager.ontology!=null)
		{
			if (((JTree)e.getSource()).getLastSelectedPathComponent()!=null)
			{
				String nomDoc=((JTree)e.getSource()).getLastSelectedPathComponent().toString();
				DocumentPart document;
				for (int i=0;i<ApplicationManager.ontology.get(DocumentPart.KEY).size();i++)
				{
					if (ApplicationManager.ontology.get(DocumentPart.KEY).get(i).toString().equals(nomDoc))
					{
						document=(DocumentPart)ApplicationManager.ontology.get(DocumentPart.KEY).get(i);
						((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptIndexant(document);
						((CorpusNavigationPanel) this.navigationPanel).remplirTableConceptPotentiel(document);
						((CorpusNavigationPanel) this.navigationPanel).remplirTableLemmeLier(document);
						//((OntologyNavigationPanel) this.navigationPanel).rollFirstPanel((LinkableElement)((JTree)e.getSource()).getSelectionPath().getLastPathComponent());
						DisplayManager.getInstance().reflectNavigation(document);
						return;
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
		((CorpusTM)this.getTree().getModel()).remplirArbreDocument();
		if (ApplicationManager.ontology!=null)
			((CorpusNavigationPanel)this.getNavigationPanel()).refresh();
	}
	
}
