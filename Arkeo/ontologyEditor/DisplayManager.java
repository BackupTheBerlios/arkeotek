/**
 * Created on 29 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor;

import java.util.ArrayList;

import javax.swing.tree.TreePath;

import ontologyEditor.gui.MainFrame;
import ontologyEditor.gui.panels.CorpusPanel;
import ontologyEditor.gui.panels.LinguisticPanel;
import ontologyEditor.gui.panels.OntologyPanel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * This class is responsible of managing display
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class DisplayManager
{
	/**
	 * MainFrame of this application
	 */
	public static MainFrame mainFrame;
	
	/**
	 * True if an element is edited
	 */
	public static boolean editionState = false;
	
	private static DisplayManager displayManager;
	
	/**
     * Constructor of DisplayManager. It is private to allow the implementation of the 
     * singleton pattern
     */
    private DisplayManager()
    {
        //empty corpse
    }
    
	/**
	 * @return Returns the displayManager.
	 */
	public static DisplayManager getInstance()
	{
		if (DisplayManager.displayManager == null)
        {
            DisplayManager.displayManager = new DisplayManager();
        }
        return (DisplayManager.displayManager);
	}

	/**
	 * Add 
	 * @param element the element added
	 */
	public void addElement(LinkableElement element)
	{
		if (element instanceof Concept)
		{
//			repercussion sur OntologyPanel.tree et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).elementAdded(element);
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).elementAdded(element);
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
			}
			mainFrame.getEditionPanel().elementAdded(element);
			mainFrame.changeState(true);
		}
		else if (element instanceof Lemma)
		{
//			repercussion sur LinguisticPanel.tree et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof LinguisticPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).elementAdded(element);
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof LinguisticPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).elementAdded(element);
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
			}
			mainFrame.getEditionPanel().elementAdded(element);
			mainFrame.changeState(true);
		}	
		//we cannot add a DocumentPart, but in case of evolution, code is ready to use
		else if (element instanceof DocumentPart)
		{
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof CorpusPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
			}
			mainFrame.getEditionPanel().elementAdded(element);
			mainFrame.changeState(true);
		}
	}
	
	/**
	 * Repercut the removing of an element on the display
	 * @param element the element removed
	 * @param indexes indexes of the element in trees
	 */
	public void removeElement(LinkableElement element, int[] indexes)
	{
		if (element instanceof Concept)
		{
			//repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
			{
				//mainFrame.getPanel(MainFrame.TOP_PANEL).elementRemoved(element, indexes[0]);
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(element);
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
			{
				//mainFrame.getPanel(MainFrame.BOTTOM_PANEL).elementRemoved(element, indexes[1]);
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(element);
			}
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof CorpusPanel)
				mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(element);
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(element);
			mainFrame.getEditionPanel().elementRemoved(element);
		}
		else if (element instanceof Lemma)
		{
//			repercussion sur OntologyPanel.tree, OntologyPanel.navigation, LinguisticPanel.tree, LinguisticPanel.navigation et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof LinguisticPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).elementRemoved(element, indexes[0]);
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(element);
			}
			else if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(element);
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof LinguisticPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).elementRemoved(element, indexes[1]);
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(element);
			}
			else if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(element);
			}
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof CorpusPanel)
				mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(element);
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(element);
			mainFrame.getEditionPanel().elementRemoved(element);
		}
	}
	
	/**
	 * Repercut the addition of a new relation between two <code>LinkableElements</code> on display
	 * @param source the source of the relation
	 * @param target the target of the relation
	 */
	public void addRelation(LinkableElement source, LinkableElement target)
	{
		if (source instanceof Concept)
		{
//			repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
			}
		}
		if (source instanceof Lemma)
		{
			if (target instanceof Concept)
			{
				//repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
				if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
				{
					mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				}
				if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
				{
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
				}
			}
			else if (target instanceof Lemma)
			{
				//repercussion sur tout
				if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel || mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof LinguisticPanel)
				{
					mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				}
				if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel || mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof LinguisticPanel)
				{
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
				}
			}
		}
		if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof CorpusPanel)
		{
			mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
			mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(target);
		}
		if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
		{
			mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
			mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(target);
		}
	}
	
	/**
	 * Repercut the removing of a relation between two <code>LinkableElements</code> on the display
	 * @param source the source of relation removed
	 * @param target the target of the relation removed
	 */
	public void removeRelation(LinkableElement source, LinkableElement target)
	{
		if (source instanceof Concept)
		{
			//repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
			}
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
			{
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
			}
		}
		else if (source instanceof Lemma)
		{
			if (target instanceof Concept)
			{
//				repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
				if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
				{
					mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				}
				if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
				{
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
				}
			}
			else
			{
				//repercussion sur LinguisticPanel.tree, LinguisticPanel.navigation, OntologyPanel.navigation et EditionPanel
				if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof LinguisticPanel)
				{
					mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				}
				else if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof LinguisticPanel)
				{
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
				}
				else if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
			}
		}
		if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof CorpusPanel)
		{
			mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
			mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(target);
		}
		if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
		{
			mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
			mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(target);
		}		
	}
	
	/**
	 * Reset the display
	 */
	public void resetDisplay()
	{
		mainFrame.refresh();
		this.changeState(false);
	}
	
	/**
	 * Change the state of application (edition/navigation)
	 * @param state the state
	 */
	public void changeState(boolean state)
	{
		editionState = state;
		mainFrame.changeState(state);
	}
	
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		if (source instanceof Concept)
		{
//			repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
			if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
				mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
			if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
				mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
		}
		if (source instanceof Lemma)
		{
			if (target instanceof Concept)
			{
				//repercussion sur OntologyPanel.tree, OntologyPanel.navigation et EditionPanel
				if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel)
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel)
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
			}
			else if (target instanceof Lemma)
			{
				//repercussion sur tout
				if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof OntologyPanel || mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof LinguisticPanel)
					mainFrame.getPanel(MainFrame.TOP_PANEL).relationChanged(source, target);
				if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof OntologyPanel || mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof LinguisticPanel)
					mainFrame.getPanel(MainFrame.BOTTOM_PANEL).relationChanged(source, target);
			}
		}
		if (mainFrame.getPanel(MainFrame.TOP_PANEL) instanceof CorpusPanel)
			mainFrame.getPanel(MainFrame.TOP_PANEL).refreshNavigation(target);
		if (mainFrame.getPanel(MainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
			mainFrame.getPanel(MainFrame.BOTTOM_PANEL).refreshNavigation(target);
	}
	
	/**
	 * Reflect navigation events on display
	 * @param element the current element
	 */
	public void reflectNavigation(LinkableElement element)
	{
		mainFrame.reflectNavigation(element);
	}
	
	/**
	 * Reload all the GUI
	 */
	public void reloadGUI()
	{
		this.reloadTrees();
		this.reloadPanels();
	}
	
	/**
	 * Reload the panels
	 */
	public void reloadPanels()
	{
		mainFrame.getPanel(MainFrame.TOP_PANEL).reloadPanel();
		mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadPanel();
	}
	
	/**
	 * Reload the trees
	 */
	public void reloadTrees()
	{
		mainFrame.getPanel(MainFrame.TOP_PANEL).reloadTrees();
		mainFrame.getPanel(MainFrame.BOTTOM_PANEL).reloadTrees();
	}
	
	/**
	 * Change the name of an element (not in use actually)
	 * @param element the element renamed
	 */
	public void changeName(LinkableElement element)
	{
		int[] indexes = mainFrame.getChildIndexesInTrees(element);
		this.removeElement(element, indexes);
		this.addElement(element);
	}
	
	/**
	 * Return elements of specified type selected in trees
	 * @param categoryKey categoryKey of elements to return
	 * @return elements selected
	 */
	public ArrayList<LinkableElement> getSelectedElements(int categoryKey)
	{
		ArrayList<LinkableElement> elements = new ArrayList<LinkableElement>();
		TreePath[] topPaths = mainFrame.getPanel(MainFrame.TOP_PANEL).getSelectedValues();
		if (topPaths != null)
		{
			for (int i = 0; i < topPaths.length; i++)
			{
				if (topPaths[i].getLastPathComponent() instanceof LinkableElement)
				{
					if (((LinkableElement)topPaths[i].getLastPathComponent()).getCategoryKey() == categoryKey)
					elements.add((LinkableElement)topPaths[i].getLastPathComponent());
				}
			}
		}
		
		TreePath[] bottomPaths = mainFrame.getPanel(MainFrame.BOTTOM_PANEL).getSelectedValues();
		if (bottomPaths != null)
		{
			for (int i = 0; i < bottomPaths.length; i++)
			{
				if (bottomPaths[i].getLastPathComponent() instanceof LinkableElement)
				{
					if (((LinkableElement)bottomPaths[i].getLastPathComponent()).getCategoryKey() == categoryKey)
					elements.add((LinkableElement)bottomPaths[i].getLastPathComponent());
				}
			}
		}
		return elements;
	}
	
}
