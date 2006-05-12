/**
 * Created on 21 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.transfers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.tables.EditorTableModel;
import arkeotek.ontology.Concept;
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
				Object[] relations = ApplicationManager.ontology.get(Relation.KEY).toArray();
				if (relations.length != 0)
				{
					Relation relation = (Relation)JOptionPane.showInputDialog(DisplayManager.mainFrame, 
							"Veuillez entrer le nom de la relation:", "Cr\u00e9ation d'un lien", JOptionPane.INFORMATION_MESSAGE, null,
							relations, relations[0]);
					if (relation != null)
					{
						if (((EditorTableModel) target.getModel()).getElement() instanceof Concept)
						{
							Object[] possibleValues = {"de l'\u00e9l\u00e9ment \u00e9dit\u00e9", "vers l'\u00e9l\u00e9ment \u00e9dit\u00e9" };
							Object selectedValue = JOptionPane.showInputDialog(null, 
							"Selectionnez le sens de la relation", "Input",
							JOptionPane.INFORMATION_MESSAGE, null,
							possibleValues, possibleValues[0]);
							((EditorTableModel) target.getModel()).addRelation(element, relation, (((String)selectedValue).equals("vers l'\u00e9l\u00e9ment \u00e9dit\u00e9")?1:0));
						}
						else
							((EditorTableModel) target.getModel()).addRelation(element, relation, 2);
					}
				}
				else
					JOptionPane.showMessageDialog(DisplayManager.mainFrame, "Aucune relation n'est cr\u00e9\u00e9e, veuillez en cr\u00e9er une avant", "Information", JOptionPane.INFORMATION_MESSAGE);
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