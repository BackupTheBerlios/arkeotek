/**
 * Created on 17 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

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
public class HightEditorPaneTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relation",
    "Element"};

	/**
	* @param element
	*/
	public HightEditorPaneTableModel(LinkableElement element)
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
		System.out.println("getValueAt");
		return this.getElementsFromLinkableElement(this.getElement()).get(rowIndex)[columnIndex];
	}
	
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement element)
	{	
		System.out.println("getElementsFromLinkableElement Higt Editor TM");
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element.getLinks(Concept.KEY) != null)
		{
			Set<Relation> keys = element.getLinks(Concept.KEY).keySet();
			if (keys != null)
			{
				Object[] link = null;
				for (Relation key : keys)
				{
					ArrayList<LinkableElement> tmp = element.getLinks(Concept.KEY, key);
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
		if (element instanceof Concept)
			elements.addAll(ApplicationManager.ontology.getParentsOf(this.getElement(), Concept.KEY));
		return elements;
	}

	/**
	 * @throws Exception 
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeRelation(int)
	 */
	@Override
	public void removeRelation(int lineSelected) throws Exception
	{
		LinkableElement element = (LinkableElement)this.getValueAt(lineSelected, 1);
		element.unlink((Relation)this.getValueAt(lineSelected, 0), this.getElement());
		this.getElement().unlink((Relation)this.getValueAt(lineSelected, 0), element);
		DisplayManager.getInstance().removeRelation(this.getElement(), element);
		DisplayManager.getInstance().relationChanged(element, this.getElement());
		this.fireTableRowsDeleted(lineSelected, lineSelected);
	}

	/**
	 * @see ontologyEditor.gui.tables.EditorTableModel#removeElement(int)
	 */
	@Override
	public void removeElement(int lineSelected)
	{
		// TODO Auto-generated method stub

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
