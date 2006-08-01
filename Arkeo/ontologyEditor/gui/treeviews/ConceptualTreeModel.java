/**
 * Created on 30 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.treeviews;

import java.util.ArrayList;
import java.util.Set;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ConceptualTreeModel extends AbstractTreeModel
{
	private LinkableElement lastElement;
	
	private Integer treeSize;

	/**
	 * 
	 */
	public ConceptualTreeModel()
	{
		super(Concept.KEY);
		this.treeSize=0;
	}

	public ArrayList<LinkableElement> getElementsFromIndexable(LinkableElement parent)
	{
		ArrayList<LinkableElement> elems = new ArrayList<LinkableElement>();
		Set<Relation> keys = null;
		if (parent instanceof Concept)
		{
			//with the following lines, we can display documents in ontology tree
			/*if (((Concept) parent).getRelations(DocumentPart.KEY) != null)
			{
				keys = ((Concept) parent).getRelations(DocumentPart.KEY).keySet();
				for (Relation key : keys)
					elems.addAll(((Concept) parent).getLinkedElements(DocumentPart.KEY, key));
			}*/
			if (((Concept) parent).getLinks(Concept.KEY) != null)
			{
				ArrayList<LinkableElement> tmp = ((Concept) parent).getLinks(Concept.KEY, new Relation(Relation.DEFAULT_CONCEPTS_RELATION)); 
				if (tmp.size() != 0)
					elems.addAll(tmp);
			}
			if (((Concept) parent).getLinks(Lemma.KEY) != null)
			{
				keys = ((Concept) parent).getLinks(Lemma.KEY).keySet();
				/*for (Relation key : keys)
					elems.addAll(((Concept) parent).getLinks(Lemma.KEY, key));*/
			}
		}
		else if (parent instanceof Ontology)
		{
			for (LinkableElement concept : ((Ontology)parent).get(Concept.KEY))
			{
				if (((Ontology)parent).getParentsOf(concept, Concept.KEY).size() == 0)
					elems.add(concept);
			}
		}
		treeSize=elems.size();
		return elems;		
	}

	
	/**
	 * @param element the child element
	 * @return the path to the root
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getPathToRoot(arkeotek.ontology.LinkableElement)
	 */
	@Override
	public LinkableElement[] getPathToRoot(LinkableElement element) 
	{
		LinkableElement[] path = null;
		if (element != null)
		{
			path = new LinkableElement[2];
			path[0] = ApplicationManager.ontology;
			path[1] = element;
		}
		return path;
    }

	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getParentNode(java.lang.Object)
	 */
	@Override
	public Object getParentNode(Object element)
	{
		if (element != null)
			return this.getRoot();
		return null;
	}

	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getChildCount(java.lang.Object)
	 */
	@Override
	public int getChildCount(Object parent)
	{
		if (((LinkableElement) parent).getCategoryKey() == this.getModelCategory() || parent instanceof Ontology)
			//return treeSize;
			return getElementsFromIndexable((LinkableElement) parent).size();
       return 0;
	}

	/**
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child)
	{
		ArrayList elems = null;
        if (parent instanceof LinkableElement)
			elems = getElementsFromIndexable((LinkableElement) parent);
		return elems.indexOf(child);
	}

	public Integer getTreeSize() {
		return treeSize;
	}

	public void setTreeSize(Integer treeSize) {
		this.treeSize = treeSize;
	}
	
	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#reloadTree()
	 */
	/*public void reloadTree()
	{
		this.fireTreeStructureChanged();
	}*/
	
	
}
