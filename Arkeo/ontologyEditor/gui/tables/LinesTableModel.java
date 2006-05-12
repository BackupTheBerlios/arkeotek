/**
 * Created on 31 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LinesTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relation",
            "Document",
            "Contenu"};
	
	/**
	 * @param element
	 */
	public LinesTableModel(LinkableElement element)
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
	 * @return an <code>ArrayList</code> of Object[relation, element, element value, isIgnored] linked with element passed in parameter
	 */
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement element)
	{
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element != null)
		{
			Set<Relation> keys = null;
			if (element.getLinks(DocumentPart.KEY) != null)
			{
				keys = element.getLinks(DocumentPart.KEY).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement elem : element.getLinks(DocumentPart.KEY, key))
					{
						Object[] line = {key, elem, ((DocumentPart) elem).getValue()};
						elements.add(line);
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

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		if (this.getColumnClass(columnIndex) == Boolean.class)
			return true;
		return super.isCellEditable(rowIndex, columnIndex);
	}
	
	
}
