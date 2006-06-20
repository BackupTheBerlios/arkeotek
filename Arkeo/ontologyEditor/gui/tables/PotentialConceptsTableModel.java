/**
 * Created on 25 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class PotentialConceptsTableModel extends EditorTableModel
{
	private String[] columnNames = {"Relations", "Elements", "Origine"};
	private ArrayList<Integer> links_categories;
	
	/**
	 * @param element
	 * @param links_category 
	 */
	public PotentialConceptsTableModel(LinkableElement element, int links_category)
	{
		super(element);
		this.links_categories = new ArrayList<Integer>(1);
		this.links_categories.add(links_category);
	}

	/**
	 * @param element
	 * @param links_categories 
	 */
	public PotentialConceptsTableModel(LinkableElement element, ArrayList<Integer> links_categories)
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
	 * Returns all elements linked undirectly with <code>LinkableElement</code> in 
	 * parameter, excepted links which are present directly
	 * @param parent The "root" element
	 * @return An <code>ArrayList</code> of <code>Object</code>[relation, element] linked with element passed in parameter
	 */
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement parent)
	{
		System.out.println("getElementsFromLinkableElement");
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (parent != null)
		{
			LinkableElement elem = this.getElement();

			for (Integer category : this.links_categories) {
				if (elem.getLinks(Lemma.KEY) != null) {
					for (Relation rel_key : elem.getLinks(Lemma.KEY).keySet()) {
						for (LinkableElement lemma : elem.getLinks(Lemma.KEY).get(rel_key).keySet()) {
							Set<Relation> keys = lemma.getLinks(category.intValue()).keySet();
							for (Relation key : keys)
							{
								for (LinkableElement temp : lemma.getLinks(category.intValue(), key))
								{
									if (elem.getLinks(temp).size() == 0)
									{
										Object[] triple = {key, temp, lemma};
										elements.add(triple);
									}
								}
							}
						}
					}
				}
				if (elem.getLinks(DocumentPart.KEY) != null) {
					if (elem.getLinks(DocumentPart.KEY).containsKey(new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION))) {
						for (LinkableElement doc : elem.getLinks(DocumentPart.KEY).get(new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION)).keySet()) {
							Set<Relation> keys = doc.getLinks(category.intValue()).keySet();
							for (Relation key : keys)
							{
								for (LinkableElement temp : doc.getLinks(category.intValue(), key))
								{
									if (elem.getLinks(temp).size() == 0)
									{
										Object[] triple = {key, temp, doc};
										elements.add(triple);
									}
								}
							}
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
