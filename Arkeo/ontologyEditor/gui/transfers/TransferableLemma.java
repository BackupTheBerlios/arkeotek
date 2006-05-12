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
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class TransferableLemma implements Transferable
{
	/**
	 * Definition of the transportable elements of type <code>Lemma</code>. 
	 */
	public static DataFlavor lemmaFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
            + "; class=arkeotek.ontology.Lemma", "Lemma");	
	
	/**
	 * Dataflavor supported by this transferable object
	 */
	public static DataFlavor[] flavor = {lemmaFlavor};	
	
	private LinkableElement element;
	
	/**
	 * @param element
	 */
	public TransferableLemma(LinkableElement element)
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
