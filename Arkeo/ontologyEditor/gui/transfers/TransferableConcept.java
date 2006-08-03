/**
 * Created on 21 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.transfers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import arkeotek.ontology.LinkableElement;

/**
 * Julien Sanmartin
 * Classe metier pour le transfert d'un concept
 *
 */
public class TransferableConcept implements Transferable
{
	/**
	 * Definition of the transportable elements of type <code>Concept</code>. 
	 */
	public static DataFlavor conceptFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
            + "; class=arkeotek.ontology.Concept", "Concept");
	
	/**
	 * Dataflavor supported by this transferable object
	 */
	public static DataFlavor[] flavor = {conceptFlavor};	

	
	private LinkableElement element;
	
	/**
	 * @param element
	 */
	public TransferableConcept(LinkableElement element)
	{
		this.element = element;
	}
	
	/**
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors()
	{
		return flavor;
	}

	/**
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor dataflavor)
	{
		for (int i = 0; i < flavor.length; i++)
		{
			if (dataflavor == flavor[i])
				return true;
		}

		return false;
	}

	/**
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor dataflavor) throws UnsupportedFlavorException, IOException
	{
		return this.element;
	}
}
