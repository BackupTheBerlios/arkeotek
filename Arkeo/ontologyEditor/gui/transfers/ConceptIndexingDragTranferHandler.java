/**
 * Created on 7 juil. 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.transfers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import arkeotek.ontology.Concept;
import arkeotek.ontology.LinkableElement;

/**
 * Julien Sanmartin
 * Classe appelé lors du tranfert d'un objet de type Concept
 */

public class ConceptIndexingDragTranferHandler extends TransferHandler
{
//	 The data type exported.
    String mimeTypeConcept = DataFlavor.javaJVMLocalObjectMimeType
            + ";class=arkeotek.ontology.Concept";
	
	DataFlavor currentFlavor;
	DataFlavor exportedConcept;

	/**
	 * 
	 */
	public ConceptIndexingDragTranferHandler()
	{
//		 Try to create a DataFlavor for Concept and Lemma.
        try
        {
			this.exportedConcept = new DataFlavor(this.mimeTypeConcept);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
	}

	/**
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
	 */
	public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
	{
		return false;
	}

	/**
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	protected Transferable createTransferable(JComponent c)
	{
		JTable source = (JTable) c;
		LinkableElement element = (LinkableElement)source.getValueAt(source.getSelectedRow(), 1);
		if (element instanceof Concept)
		{
			this.currentFlavor = this.exportedConcept;
			return new TransferableConcept(element);
		}

		return null;
	}

	/**
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	public int getSourceActions(JComponent c)
	{
		return MOVE;
	}

	/**
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	public boolean importData(JComponent comp, Transferable t)
	{
		return false;
	}
	
	
	

}
