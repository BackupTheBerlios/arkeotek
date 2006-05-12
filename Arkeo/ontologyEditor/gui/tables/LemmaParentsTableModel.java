/**
 * Created on 22 août 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LemmaParentsTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relations",
    "Elements"};
	
	/**
	 * @param element
	 */
	public LemmaParentsTableModel(LinkableElement element)
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
	 * @param element the "root" element
	 * @return An <code>ArrayList</code> of <code>Object</code>[relation, element] linked with element passed in parameter
	 */
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement element)
	{
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element != null)
		{	
			elements.addAll(ApplicationManager.ontology.getLemmasParents(this.getElement()));
		}
		return elements;
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

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return getElementsFromLinkableElement(this.getElement()).get(rowIndex)[columnIndex];
	}

	/**
	 * Empty method
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeRelation(int)
	 */
	@Override
	public void removeRelation(int lineSelected)
	{
		//Nothing to do in this method

	}

	/**
	 * Empty method
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeElement(int)
	 */
	@Override
	public void removeElement(int lineSelected)
	{
		//Nothing to do in this method

	}
}
