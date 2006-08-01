/**
 * Created on 21 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.transfers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.panels.corpus.CorpusNavigationPanel;
import ontologyEditor.gui.tableModel.IndexingConceptTableModel;
import ontologyEditor.gui.tableModel.HighEditorTableModel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ConceptDropTransferHandler extends TransferHandler
{
//	 The data type exported.
	String mimeTypeConcept = DataFlavor.javaJVMLocalObjectMimeType
	+ ";class=arkeotek.ontology.Concept";
	String mimeTypeLinkableElement = DataFlavor.javaJVMLocalObjectMimeType
	+ ";class=arkeotek.ontology.LinkableElement";
	
	DataFlavor exportedConcept;
	DataFlavor exportedLinkableElement;
	
	/**
	* 
	*/
	public ConceptDropTransferHandler()
	{
		// Try to create a DataFlavor for Concept and Lemma.
		try
		{
			this.exportedConcept = new DataFlavor(this.mimeTypeConcept);
			this.exportedLinkableElement = new DataFlavor(this.mimeTypeLinkableElement);
		}
		catch (ClassNotFoundException e)
		{
		    e.printStackTrace();
		}
	}

	/**
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	public boolean importData(JComponent c, Transferable t)
    {
        try
        {
			JTable target = (JTable) c;
            if (t != null)
			{
            	LinkableElement element = (LinkableElement) t.getTransferData(this.exportedLinkableElement);
				ArrayList<LinkableElement> relations = ApplicationManager.ontology.get(Relation.KEY);
				ArrayList<LinkableElement> conceptToConcept=new ArrayList<LinkableElement>();
				
				if (relations.size() != 0)
				{
					// si la cible c'est les conceptIndexant 
					if (target.getModel() instanceof IndexingConceptTableModel)
					{
						//-------------------------->
						for (LinkableElement rel:relations)
						{
							if (((Relation)rel).getType()==Relation.RELATION_CONCEPT_DOCUMENT)
							{
								conceptToConcept.add(rel);
							}
						}
						//<-------------------------
						Object[] rels=conceptToConcept.toArray();
						if (rels.length!=0)
						{
							Relation relation = (Relation)JOptionPane.showInputDialog(DisplayManager.mainFrame, 
									"Veuillez entrer le nom de la relation:", "Création d'un lien", JOptionPane.INFORMATION_MESSAGE, null,
									rels, rels[0]);
							if (relation !=null)
							{
								//on regarde dans quel panel s'est fait le drag and drop
								int panel=DisplayManager.mainFrame.BOTTOM_PANEL;
								if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getY()==target.getParent().getParent().getParent().getParent().getParent().getY())
								{
									panel=DisplayManager.mainFrame.TOP_PANEL;
								}
								// on recupere le document source
								LinkableElement doc=((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).getDoc();
								// on créer une nouvelle relation
								ApplicationManager.ontology.addRelation(doc,element,relation);
								ApplicationManager.ontology.addRelation(element,doc,relation);
								// on met a jour les tables correspondantes
								((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableConceptIndexant(doc);
								((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableConceptPotentiel(doc);
								if (DisplayManager.mainFrame.getEditionPanel().getEditionButton().isSelected())
								{
									if ((DisplayManager.mainFrame.getEditionPanel().getCourant().equals(doc)))
									{
										DisplayManager.mainFrame.getEditionPanel().remplirTableHautParent(doc);
									}
									else if((DisplayManager.mainFrame.getEditionPanel().getCourant().equals(element)))
									{
										DisplayManager.mainFrame.getEditionPanel().remplirTableBasParent(element);
									}
								}
							}
						}
						else
						{
							JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Aucune relation possible entre ces deux types d'objet");
						}
					}
					// si on glisse un concept dans le panneau haut d'edition
					else if (target.getModel() instanceof HighEditorTableModel)
					{
						//-------------------------->
						LinkableElement le=DisplayManager.mainFrame.getEditionPanel().getCourant();
						if (le instanceof Concept)
						{
							for (LinkableElement rel:relations)
							{
								if (((Relation)rel).getType()==Relation.RELATION_CONCEPT_CONCEPT)
								{
									conceptToConcept.add(rel);
								}
							}
						}
						else if (le instanceof Lemma)
						{
							for (LinkableElement rel:relations)
							{
								if (((Relation)rel).getType()==Relation.RELATION_TERME_CONCEPT)
								{
									conceptToConcept.add(rel);
								}
							}
						}
						else if (le instanceof DocumentPart)
						{
							for (LinkableElement rel:relations)
							{
								if (((Relation)rel).getType()==Relation.RELATION_CONCEPT_DOCUMENT)
								{
									conceptToConcept.add(rel);
								}
							}
						}
						//<-------------------------
						Object[] rels=conceptToConcept.toArray();
						if (rels.length!=0)
						{
							Relation relation = (Relation)JOptionPane.showInputDialog(DisplayManager.mainFrame, 
									"Veuillez entrer le nom de la relation:", "Création d'un lien", JOptionPane.INFORMATION_MESSAGE, null,
									rels, rels[0]);
							if (relation !=null)
							{
								// on créer une nouvelle relation
								ApplicationManager.ontology.addRelation(le,element,relation);
								ApplicationManager.ontology.addRelation(element,le,relation);
								// on met a jour l'interface
								DisplayManager.mainFrame.getEditionPanel().remplirTableHautParent(le);
								DisplayManager.mainFrame.mAJ(le);
								DisplayManager.mainFrame.mAJ(element);
								//DisplayManager.mainFrame.refresh();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Aucune relation possible entre ces deux types d'objet");
						}
					}
				}
				return true;
			}
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

	protected Transferable createTransferable(JComponent c)
    {
		JTable source = (JTable) c;
		LinkableElement element = (LinkableElement)source.getValueAt(source.getSelectedRow(), 1);
		if (element instanceof Concept)

			return new TransferableConcept(element);
		else if (element instanceof Lemma)
			return new TransferableLemma(element);
		return null;
    }
	
	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
	 */
	public boolean canImport(JComponent c, DataFlavor[] flavors)
    {
		for (int i = 0; i < flavors.length; i++) 
		{ 
			if (this.exportedConcept.equals(flavors[i])) 
				return true; 
		} 
		return false;  
    }
	
	/**
	 * @see javax.swing.TransferHandler#getVisualRepresentation(java.awt.datatransfer.Transferable)
	 */
	public Icon getVisualRepresentation(Transferable t)
    {
		return super.getVisualRepresentation(t);
    }

	/**
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	public int getSourceActions(JComponent c)
	{
		return NONE;
	}
}