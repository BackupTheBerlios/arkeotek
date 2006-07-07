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
import ontologyEditor.gui.tables.SecondEditorPaneTM;
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
public class LemmaDropTransferHandler extends TransferHandler
{
//	 The data type exported.
	String mimeTypeLemma = DataFlavor.javaJVMLocalObjectMimeType
	+ ";class=arkeotek.ontology.Lemma";
	String mimeTypeLinkableElement = DataFlavor.javaJVMLocalObjectMimeType
	+ ";class=arkeotek.ontology.LinkableElement";
	
	DataFlavor exportedLemma;
	DataFlavor exportedLinkableElement;
	
	/**
	* 
	*/
	public LemmaDropTransferHandler()
	{
		// Try to create a DataFlavor for Concept and Lemma.
		try
		{
			this.exportedLemma = new DataFlavor(this.mimeTypeLemma);
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
				if (relations.size() != 0)
				{
					ArrayList<LinkableElement> relations_terme=new ArrayList<LinkableElement>();
			
					if (DisplayManager.mainFrame.getEditionPanel().getCourant() instanceof Concept)
					{
						//-------------------------->
						for (LinkableElement rel:relations)
						{
							if (((Relation)rel).getType()==Relation.RELATION_TERME_CONCEPT)
							{
								relations_terme.add(rel);
							}
						}
						//<-------------------------
					}
					else if(DisplayManager.mainFrame.getEditionPanel().getCourant() instanceof Lemma)
					{
						//-------------------------->
						for (LinkableElement rel:relations)
						{
							if (((Relation)rel).getType()==Relation.RELATION_TERME_TERME)
							{
								relations_terme.add(rel);
							}
						}
						//<-------------------------
					}
					else if(DisplayManager.mainFrame.getEditionPanel().getCourant() instanceof DocumentPart)
					{
						//-------------------------->
						for (LinkableElement rel:relations)
						{
							if (((Relation)rel).getType()==Relation.RELATION_TERME_DOCUMENT)
							{
								relations_terme.add(rel);
							}
						}
						//<-------------------------
					}
					
					if (relations_terme.size()!=0)
					{
					
						Relation relation = (Relation)JOptionPane.showInputDialog(DisplayManager.mainFrame, 
								"Veuillez entrer le nom de la relation:", "Cr\u00e9ation d'un lien", JOptionPane.INFORMATION_MESSAGE, null,
								relations_terme.toArray(), relations_terme.toArray()[0]);
						if (relation != null)
						{
							
							if (target.getModel() instanceof SecondEditorPaneTM)
							{
								Boolean trouver=false;
								for(int i=0;i<target.getRowCount();i++)
								{
									LinkableElement courant=(LinkableElement)target.getValueAt(i,1);
									if (courant.equals(element))
									{
										trouver=true;
										break;
									}
								}
								if (!trouver)
								{
									ApplicationManager.ontology.addRelation(DisplayManager.mainFrame.getEditionPanel().getCourant(),element,relation);
									ApplicationManager.ontology.addRelation(element,DisplayManager.mainFrame.getEditionPanel().getCourant()/*,element*/,relation);
									DisplayManager.mainFrame.getEditionPanel().remplirTableBasParent(DisplayManager.mainFrame.getEditionPanel().getCourant());
									DisplayManager.mainFrame.getEditionPanel().remplirTableHautParent(DisplayManager.mainFrame.getEditionPanel().getCourant());
									//on regarde dans quel panel s'est fait le drag and drop
									DisplayManager.mainFrame.mAJ(DisplayManager.mainFrame.getEditionPanel().getCourant());
									DisplayManager.mainFrame.mAJ(element);
								}
								
								
							}
						}
					}
					else
					{
						JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Aucune relation possible entre ces deux types d'objet");
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
			if (this.exportedLemma.equals(flavors[i])) 
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
