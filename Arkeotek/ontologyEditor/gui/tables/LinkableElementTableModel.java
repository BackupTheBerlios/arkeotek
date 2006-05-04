/**
 * Created on 25 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LinkableElementTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relations",
            "Elements"};
	private ArrayList<Integer> links_categories;
	
	/**
	 * @param element
	 * @param links_category 
	 */
	public LinkableElementTableModel(LinkableElement element, int links_category)
	{
		super(element);
		this.links_categories = new ArrayList<Integer>(1);
		this.links_categories.add(links_category);
	}

	/**
	 * @param element
	 * @param links_categories 
	 */
	public LinkableElementTableModel(LinkableElement element, ArrayList<Integer> links_categories)
	{
		super(element);
		this.links_categories = links_categories;
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
	 * Returns all elements linked with <code>LinkableElement</code> in parameter
	 * @param parent The "root" element
	 * @return An <code>ArrayList</code> of <code>Object</code>[relation, element] linked with element passed in parameter
	 */
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement parent)
	{
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (parent != null)
		{
			LinkableElement elem = this.getElement();
			for (Integer category : this.links_categories) {
				if (elem.getLinks(category.intValue()) != null) {
					Set<Relation> keys = parent.getLinks(category.intValue()).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement temp : parent.getLinks(category.intValue(), key))
						{
							Object[] triple = {key, temp, this};
							elements.add(triple);
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
		ApplicationManager.ontology.unlink(element);
		this.fireTableStructureChanged();
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
			return this.columnNames.length;
		return 0;
	}
}
