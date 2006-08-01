/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels.conceptual;

import java.awt.event.MouseEvent;

import javax.swing.JTree;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.popup.PopupConceptTree;
import ontologyEditor.gui.model.treeModel.ConceptualTreeModel;
import ontologyEditor.gui.panels.AbstractPanel;
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
		super(new ConceptualTreeModel(),new OntologyNavigationPanel());
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
						//Bouton GAUCHE enfoncé
						if ( e.getButton() == MouseEvent.BUTTON1) {
							((OntologyNavigationPanel) this.navigationPanel).remplirLabelPere(concept);
							((OntologyNavigationPanel) this.navigationPanel).remplirTableFils(concept);
							((OntologyNavigationPanel) this.navigationPanel).remplirTableLemme(concept);
							((OntologyNavigationPanel) this.navigationPanel).remplirTableDefini(concept);
							DisplayManager.getInstance().reflectNavigation(concept);
						} 
						//Bouton DROIT enfoncé
						else if( e.getButton() == MouseEvent.BUTTON3) 
						{
							int panel=DisplayManager.mainFrame.BOTTOM_PANEL;
							if (e.getComponent().getParent().getParent().getParent().getParent().getParent().getY()==1)
							{
								panel=DisplayManager.mainFrame.TOP_PANEL;
							}
							PopupConceptTree popup=new PopupConceptTree(concept,panel);
							
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
	
	public void refresh() {
		
		this.getTree().removeAll();
		((ConceptualTreeModel)this.getTree().getModel()).remplirArbreConcept();
		this.getTree().updateUI();
		if (ApplicationManager.ontology!=null)
			((OntologyNavigationPanel)this.getNavigationPanel()).refresh();
		this.updateUI();
	}

	@Override
	public void reflectNavigation(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementRemoved(LinkableElement element, int index) {
		// TODO Auto-generated method stub
		
	}
	
	
}
