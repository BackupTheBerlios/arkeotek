/**
 * Created on 25 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class SonTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relation",
            "Element"};
	
	/**
	 * @param element
	 */
	public SonTableModel(LinkableElement element)
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
	 * Return all elements linked with <code>LinkableElement</code> in parameter
	 * @param parent the "root" element
	 * @return an <code>ArrayList</code> of Object[relation, element] linked with element passed in parameter
	 */
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement parent)
	{
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (parent != null)
		{
			if (parent instanceof Ontology)
			{
				for (LinkableElement concept : ((Ontology)parent).get(Concept.KEY))
				{
					Object[] couple = {"", concept};
					elements.add(couple);
				}
				
			}
			else
			{
				Set<Relation> keys = null;
				if (parent.getLinks(Lemma.KEY) != null)
				{
					keys = parent.getLinks(Lemma.KEY).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement elem : parent.getLinks(Lemma.KEY, key))
						{
							Object[] couple = {key, elem};
							elements.add(couple);
						}
					}
				}
				if (parent.getLinks(DocumentPart.KEY) != null)
				{
					keys = parent.getLinks(DocumentPart.KEY).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement elem : parent.getLinks(DocumentPart.KEY, key))
						{
							Object[] couple = {key, elem};
							elements.add(couple);
						}
					}
				}
				if (parent.getLinks(Concept.KEY) != null)
				{
					keys = parent.getLinks(Concept.KEY).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement elem : parent.getLinks(Concept.KEY, key))
						{
							Object[] couple = {key, elem};
							elements.add(couple);
						}
					}
				}	
			}
		}
		return elements;
	}

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return getElementsFromLinkableElement(this.getElement()).get(rowIndex)[columnIndex];
	}

	
	/**
	 * @throws Exception 
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeRelation(int)
	 */
	@Override
	public void removeRelation(int lineSelected) throws Exception
	{
		this.getElement().unlink((Relation)this.getValueAt(lineSelected, 0), (LinkableElement)this.getValueAt(lineSelected, 1));
		((LinkableElement)this.getValueAt(lineSelected, 1)).unlink((Relation)this.getValueAt(lineSelected, 0), this.getElement());
		this.fireTableRowsDeleted(lineSelected, lineSelected);
	}

	/**
	 * @throws Exception 
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeElement(int)
	 */
	@Override
	public void removeElement(int lineSelected) throws Exception
	{
		LinkableElement element = (LinkableElement) this.getValueAt(lineSelected, 1);
		for (Object[] relation : ApplicationManager.ontology.getElementsThatReference(element))
		{
			((LinkableElement)relation[0]).unlink((Relation)relation[1], element);
			element.unlink((Relation)relation[1], ((LinkableElement)relation[0]));
		}
		int[] indexes = DisplayManager.mainFrame.getChildIndexesInTrees(element);
		ApplicationManager.ontology.unlink(element);
		DisplayManager.getInstance().removeElement(element, indexes);
	}

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column)
	{
		return this.columnNames[column];
	}
	
	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount()
	{
		if (this.getElement() != null)
			return 2;
		return 0;
	}
}
