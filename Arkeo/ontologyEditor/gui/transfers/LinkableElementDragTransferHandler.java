/**
 * Created on 23 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.transfers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.panels.OntologyNavigationPanel;
import ontologyEditor.gui.treeviews.LinkableElementTree;
import arkeotek.ontology.Concept;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LinkableElementDragTransferHandler extends TransferHandler
{
//	 The data type exported.
    String mimeTypeConcept = DataFlavor.javaJVMLocalObjectMimeType
            + ";class=arkeotek.ontology.Concept";
	String mimeTypeLemma = DataFlavor.javaJVMLocalObjectMimeType
    + ";class=arkeotek.ontology.Lemma";
	String mimeTypeLinkableElement = DataFlavor.javaJVMLocalObjectMimeType
    + ";class=arkeotek.ontology.LinkableElement";

	DataFlavor currentFlavor;
	DataFlavor exportedConcept;
	DataFlavor exportedLemma;
	
	/**
	 * 
	 */
	public LinkableElementDragTransferHandler()
    {
        // Try to create a DataFlavor for Concept and Lemma.
        try
        {
			this.exportedConcept = new DataFlavor(this.mimeTypeConcept);
			this.exportedLemma = new DataFlavor(this.mimeTypeLemma);
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
        return false;
    }

	protected Transferable createTransferable(JComponent c)
    {
		LinkableElement element = null;
		if (c instanceof JTable)
		{
			JTable source = (JTable) c;
			element = (LinkableElement)source.getValueAt(source.getSelectedRow(), 0);
		}
		else
		{
			JTree source = (JTree) c;
			if (ApplicationManager.ontology!=null)
			{
				String nomConcept=source.getLastSelectedPathComponent().toString();
				for (int i=0;i<ApplicationManager.ontology.get(Concept.KEY).size();i++)
				{
					if (ApplicationManager.ontology.get(Concept.KEY).get(i).toString().equals(nomConcept))
					{
						element=(LinkableElement)ApplicationManager.ontology.get(Concept.KEY).get(i);
					}
				}
			}
		}
		
		if (element instanceof Concept)
		{
			this.currentFlavor = this.exportedConcept;
			return new TransferableConcept(element);
		}
		else if (element instanceof Lemma)
		{
			this.currentFlavor = this.exportedLemma;
			return new TransferableLemma(element);
		}

		return null;
    }
	
	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
	 */
	public boolean canImport(JComponent c, DataFlavor[] flavors)
    {
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
		return COPY;
	}
	
}
