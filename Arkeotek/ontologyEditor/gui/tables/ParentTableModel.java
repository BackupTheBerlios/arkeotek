/**
 * Created on 25 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ParentTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relation",
            "Element"};
	
	/**
	 * @param element
	 */
	public ParentTableModel(LinkableElement element)
	{
		super(element);
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount()
	{
		if (this.getElement() != null)
			return ApplicationManager.ontology.getParentsOf(this.getElement(), Concept.KEY).size();
		return 0;
	}

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return ApplicationManager.ontology.getParentsOf(this.getElement(), Concept.KEY).get(rowIndex)[columnIndex];
	}
	
	/**
	 * @throws Exception 
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeRelation(int)
	 */
	@Override
	public void removeRelation(int lineSelected) throws Exception
	{
		((LinkableElement)this.getValueAt(lineSelected, 1)).unlink((Relation)this.getValueAt(lineSelected, 0), this.getElement());
		this.getElement().unlink((Relation)this.getValueAt(lineSelected, 0), ((LinkableElement)this.getValueAt(lineSelected, 1)));
		this.fireTableStructureChanged();
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
			return this.columnNames.length;
		return 0;
	}
}
