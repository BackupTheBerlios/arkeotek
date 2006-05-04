/**
 * Created on 30 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.treeviews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 *
 */
public class CorpusTreeModel extends AbstractTreeModel
{
	private ArrayList<Integer> documentIndexes;
	/**
	 * 
	 */
	public CorpusTreeModel()
	{
		super(DocumentPart.KEY);
		this.documentIndexes = new ArrayList<Integer>();
	}

	protected ArrayList<LinkableElement> getElementsFromIndexable(LinkableElement parent)
	{
		ArrayList<LinkableElement> elems = new ArrayList<LinkableElement>();
		ArrayList<Integer> links = new ArrayList<Integer>();
		ArrayList<LinkableElement> sons = new ArrayList<LinkableElement>();
		ArrayList<Relation> tmp;
		HashMap<Integer, ArrayList<LinkableElement>> elementsLinks = new HashMap<Integer, ArrayList<LinkableElement>>();
		
		HashMap<Relation, HashMap<LinkableElement, Link>> relations =  parent.getLinks(this.getModelCategory());
		if (relations != null && relations.containsKey(new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION)))
		{
			sons.addAll( parent.getLinks(this.getModelCategory(), new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION)));
		}
		for (LinkableElement son : sons)
		{
			tmp = son.getLinks(parent);
			for (Relation relation : tmp)
			{
				int key;
				try
				{
					key = Integer.parseInt(relation.getName());
				}
				catch (NumberFormatException ex)
				{
					key = 0;
				}
				if (elementsLinks.get(key) == null)
					elementsLinks.put(key, new ArrayList<LinkableElement>());
				elementsLinks.get(key).add(son);
			}
		}
		links = new ArrayList<Integer>(elementsLinks.keySet());
		Collections.sort(links);
		for (Integer relationKey : links)
		{
			elems.addAll(elementsLinks.get(relationKey));
		}
		return elems;
	}
	
	/**
	 * This method is used to detected roots elements
	 */
	public void initializeRootElements()
	{
		int i = 0;
		this.documentIndexes.clear();
		ArrayList<LinkableElement> documents = this.getRoot().get(DocumentPart.KEY);
		for (LinkableElement document : documents)
		{
			if (this.getRoot().getParentsOf(document, document.getCategoryKey()).size() == 0)
				this.documentIndexes.add(i++, documents.indexOf(document));
		}
	}

	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getPathToRoot(arkeotek.ontology.LinkableElement)
	 */
	@Override
	public Object[] getPathToRoot(LinkableElement element)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * In this case, we suppose that there is only one parent, it may be wrong,
	 * but it is not important in our case
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getParentNode(java.lang.Object)
	 */
	@Override
	public Object getParentNode(Object element)
	{
		if (element instanceof DocumentPart)
			return this.getRoot().getParentsOf((LinkableElement) element, ((LinkableElement) element).getCategoryKey()).get(0)[1];
		return null;
	}

	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent)
	{
		if (((LinkableElement) parent).getCategoryKey() == this.getModelCategory())
			return getElementsFromIndexable((LinkableElement) parent).size();
        else if (parent instanceof Ontology)
		{
			this.initializeRootElements();
			return this.documentIndexes.size();
		}
        else
            return 0;
	}
	
	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index)
	{
		if (((LinkableElement) parent).getCategoryKey() == this.getModelCategory())
			return getElementsFromIndexable((LinkableElement) parent).get(index);
        else if (parent instanceof Ontology)
		{
			if (this.documentIndexes.size() == 0)
				this.initializeRootElements();
			return ((Ontology)parent).get(DocumentPart.KEY).get(this.documentIndexes.get(index));
		}
        else
            return null;
	}
	
	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#reloadTree()
	 */
	public void reloadTree()
	{
		this.fireTreeStructureChanged();
	}
}

