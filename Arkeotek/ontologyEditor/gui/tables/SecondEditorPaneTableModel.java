/**
 * Created on 20 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class SecondEditorPaneTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relation",
    "Element"};

	/**
	* @param element
	*/
	public SecondEditorPaneTableModel(LinkableElement element)
	{
	super(element);
	}
	
	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getRowCount()
	 */
	@Override
	public int getRowCount()
	{
		if (this.getElement() != null)
			return getElementsFromLinkableElement(this.getElement()).size();
		return 0;
	}

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount()
	{
		if (this.getElement() != null)
			return this.columnNames.length;
		return 0;
	}


	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return this.getElementsFromLinkableElement(this.getElement()).get(rowIndex)[columnIndex];
	}

	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement element)
	{
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element.getLinks(Lemma.KEY) != null)
		{
			Set<Relation> keys = element.getLinks(Lemma.KEY).keySet();
			if (keys != null)
			{
				Object[] link = null;
				for (Relation key : keys)
				{
					ArrayList<LinkableElement> tmp = element.getLinks(Lemma.KEY, key);
					for (LinkableElement linkedElem : tmp)
					{
						link = new Object[2];
						link[0] = key;
						link[1] = linkedElem;
						elements.add(link);
					}
				}
			}
		}
		if (!(element instanceof DocumentPart))
		{
			if (element.getLinks(DocumentPart.KEY) != null)
			{
				Set<Relation> keys = element.getLinks(DocumentPart.KEY).keySet();
				if (keys != null)
				{
					Object[] link = null;
					for (Relation key : keys)
					{
						ArrayList<LinkableElement> tmp = element.getLinks(DocumentPart.KEY, key);
						for (LinkableElement linkedElem : tmp)
						{
							link = new Object[2];
							link[0] = key;
							link[1] = linkedElem;
							elements.add(link);
						}
					}
				}
			}
		}
		if (element instanceof Lemma)
			elements.addAll(ApplicationManager.ontology.getLemmasParents(this.getElement()));
		return elements;
	}

	/**
	 * @throws Exception 
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeRelation(int)
	 */
	@Override
	public void removeRelation(int lineSelected) throws Exception
	{
		//we must not remove a relation between documentParts and lemmas
		if (!(this.getElement() instanceof DocumentPart))
		{
			LinkableElement element = (LinkableElement)this.getValueAt(lineSelected, 1);
			element.unlink((Relation)this.getValueAt(lineSelected, 0), this.getElement());
			this.getElement().unlink((Relation)this.getValueAt(lineSelected, 0), element);
			DisplayManager.getInstance().removeRelation(this.getElement(), element);
			DisplayManager.getInstance().relationChanged(element, this.getElement());
			this.fireTableRowsDeleted(lineSelected, lineSelected);
		}
	}

	/**
	 * Empty method, not used in this table model
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeElement(int)
	 */
	@Override
	public void removeElement(int lineSelected)
	{
		//empty corpse

	}

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column)
	{
		return this.columnNames[column];
	}

}
