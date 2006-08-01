/**
 * Created on 23 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.model.tableModel;

import javax.swing.table.AbstractTableModel;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class EditorTableModel extends AbstractTableModel
{
	private LinkableElement element;
	

	
	/**
	 * @param element
	 */
	public EditorTableModel(LinkableElement element)
	{
		super();
		this.element = element;
	}

	/**
	 * @return Returns the element.
	 */
	public LinkableElement getElement()
	{
		return this.element;
	}

	/**
	 * @param element The element to set.
	 */
	public void setElement(LinkableElement element)
	{
		this.element = element;
		this.fireTableStructureChanged();
	}
	
	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public abstract int getRowCount();

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public abstract int getColumnCount();

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public abstract Object getValueAt(int rowIndex, int columnIndex);

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

	/**
	 * Add a relation at the current element of the table with elem
	 * @param elem the LinkableElement with whitch we have to create a relation
	 * @param relation the relation between elements
	 * @param direction the direction of relation 0 for this.element as source, 1 for this.element as target
	 * @throws Exception 
	 */
	public void addRelation(LinkableElement elem, Relation relation, int direction) throws Exception
	{
		switch (direction)
		{
			case 0: 
				ApplicationManager.ontology.addRelation(this.getElement(), elem, relation);
				this.fireTableRowsInserted(this.getRowCount(), this.getRowCount());
				DisplayManager.getInstance().addRelation(this.getElement(), elem);
			break;
			case 1: 
				ApplicationManager.ontology.addRelation(elem, this.getElement(), relation);
				this.fireTableRowsInserted(this.getRowCount(), this.getRowCount());
				DisplayManager.getInstance().addRelation(elem, this.getElement());
			break;
			case 2:
				ApplicationManager.ontology.addRelation(this.getElement(), elem, relation);
				ApplicationManager.ontology.addRelation(elem, this.getElement(), relation);
				this.fireTableRowsInserted(this.getRowCount(), this.getRowCount());
				DisplayManager.getInstance().addRelation(this.getElement(), elem);
				DisplayManager.getInstance().relationChanged(elem, this.getElement());
			default:break;
		}
	}
	
	/**
	 * Remove the relation situated at lineSelected
	 * @param lineSelected the line whitch correspond to the relation to remove
	 * @throws Exception 
	 */
	public abstract void removeRelation(int lineSelected) throws Exception;

	/**
	 * Remove the element situated at lineSelected
	 * @param lineSelected the line whitch correspond to the element to remove
	 * @throws Exception 
	 */
	public abstract void removeElement(int lineSelected) throws Exception;
	
	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return getValueAt(0, columnIndex).getClass();
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public abstract String getColumnName(int column);
	
}
