/**
 * Created on 30 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.treeviews;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class LinguisticTreeModel extends AbstractTreeModel
{
	private HashMap<String, Integer[]> letters_index;
	
	/**
	 * 
	 */
	public LinguisticTreeModel()
	{
		super(Lemma.KEY);
		this.letters_index = new HashMap<String, Integer[]>(27);
	}

	/**
	 * Return the child at rank <code>index</code> from the
	 * <code>parent</code>
	 * 
	 * @return The child
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index)
	{
		//System.out.println("lemmes! "+ApplicationManager.ontology.get(Lemma.KEY));
		Object elem = null;
		if (parent instanceof String) {
			if (this.letters_index.get(parent) == null)
				this.getSortedElements((String) parent);
			//System.out.println("getChild "+index+" "+parent+" "+this.letters_index.get(parent)[0]+" "+this.letters_index.get(parent)[1]);
			if (!parent.equals("..."))
				return this.getRoot().get(this.getModelCategory()).get(index + this.letters_index.get(parent)[0].intValue());
			//System.out.println("parent is "+parent);
			return this.getRoot().get(this.getModelCategory()).get(index);
		}
		else if (((LinkableElement) parent).getCategoryKey() == this.getModelCategory())
			elem = getElementsFromIndexable((LinkableElement) parent).get(index);
		else if (parent instanceof Ontology) {
			if (index <= 25 )
			{
				elem = String.valueOf((char)('A' + index));
			}
			else return "..."; 
		}
		return elem;
	}
	
	/**
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child)
	{
		ArrayList elems = new ArrayList();
		if (parent instanceof String)
		{
			if (this.letters_index.get(parent) == null)
				this.getSortedElements((String) parent);
			elems = this.getRoot().get(((LinkableElement)child).getCategoryKey());
			if (elems.indexOf(child) == -1)
				return this.indexElementRemoved;

			return elems.indexOf(child) - this.letters_index.get(parent)[0];
		}
		else if (parent instanceof Ontology)
		{
			if (((String)child).equals("..."))
				return 26;
			return ((String)child).charAt(0) - 65;
		}
        else if (parent instanceof LinkableElement)
			elems = getElementsFromIndexable((LinkableElement) parent);
        int i = 0;
        for (Object object : elems)
        {
            if (object == child)
                return i;
            i++;
        }
        return -1;
	}

	
	/**
	 * Check if the <code>node</code> is a leaf
	 * @return true if <code>node</code> is a leaf
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node)
	{
		if (!(node instanceof String) && (((LinkableElement) node).getCategoryKey() != this.getModelCategory()) && !(node instanceof Ontology))
			return true;
		return false;
	}


	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object parent)
	{
		if (parent instanceof String) {
			if (this.letters_index.get(parent) == null)
				this.getSortedElements((String) parent);
			return this.letters_index.get(parent)[1] - this.letters_index.get(parent)[0];
		}
		else if (((LinkableElement) parent).getCategoryKey() == this.getModelCategory())
			return getElementsFromIndexable((LinkableElement) parent).size();
		else if (parent instanceof Ontology)
		{
			return 27;
		}
		else
			return 0;
	}

	protected ArrayList<LinkableElement> getElementsFromIndexable(LinkableElement parent)
	{
		ArrayList<LinkableElement> elems = new ArrayList<LinkableElement>();
		Set<Relation> keys = null;
		if (parent.getLinks(this.getModelCategory()) != null)
		{
			keys = parent.getLinks(this.getModelCategory()).keySet();
			for (Relation key : keys)
				elems.addAll(parent.getLinks(this.getModelCategory(), key));
		}
		return elems;
	}

	protected void getSortedElements(String parent)
	{
		int begin_range, end_range;
		ArrayList<LinkableElement> temp_elems = this.getRoot().get(this.getModelCategory());
		
		if (temp_elems != null)
		{
			if (Character.toLowerCase(parent.charAt(0)) >= 'a' && Character.toLowerCase(parent.charAt(0)) <= 'z') {
				begin_range = Collections.binarySearch(temp_elems, new Lemma(String.valueOf(parent.charAt(0))));
				end_range = Collections.binarySearch(temp_elems, new Lemma(Character.toString((char) (Character.toLowerCase(parent.charAt(0)) + 1))));
				
				if (begin_range < 0) begin_range = -begin_range - 1;
				if (end_range < 0) end_range = -end_range - 1;
			} else {
				begin_range = 0;
				end_range = Collections.binarySearch(temp_elems, new Lemma("Termes associés au corpus"));
				
				if (end_range < 0) end_range = -end_range - 1;
			}
			this.letters_index.put(parent, new Integer[] {begin_range, end_range});
		}
	}

	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#fireTreeStructureChanged()
	 */
	@Override
	public void fireTreeStructureChanged()
	{
		this.letters_index = new HashMap<String, Integer[]>();
		super.fireTreeStructureChanged();
	}

	/**
	 * Notifies the listeners that a tree node was removed. 
	 * @param element the <code>LinkableElement</code> removed
	 * @param index index in tree of element removed
	 */
	public void fireTreeNodesRemoved(LinkableElement element, int index)
    {
	   	this.indexElementRemoved = index;
		Object[] listeners = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;

		if (element instanceof Lemma)
        {
			for (int i = listeners.length-2; i>=0; i-=2) {
	            if (listeners[i]==TreeModelListener.class) {
	                // Lazily create the event:
	                if (treeModelEvent == null)
					{
						Object[] path = new Object[2];
						int[] childIndices = {this.indexElementRemoved};
						if ((element.getName().toUpperCase().charAt(0)) > 64 && (element.getName().toUpperCase().charAt(0)) < 91)
						{
							path[1] = (String)this.getChild(ApplicationManager.ontology, (element.getName().toUpperCase().charAt(0)) - 65);
						}
						else
						{
							path[1] = this.getChild(ApplicationManager.ontology, 26);
						}
							
						path[0] = this.getRoot();
						Object[] removedChildren = {element};
	                    treeModelEvent = new TreeModelEvent(this, path, childIndices, removedChildren);
						this.updateIndexes((String)this.getParentNode(element), -1);
					}

					((TreeModelListener)listeners[i+1]).treeNodesRemoved(treeModelEvent);
	            }
			}
        }
    }
	
	/**
	 * Notifies the listeners that a tree node was added. 
	 * @param element the <code>LinkableElement</code> added
	 */
	public void fireTreeNodesAdded(LinkableElement element)
    {
		Object[] listeners = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;

		if (element instanceof Lemma)
        {
			for (int i = listeners.length-2; i>=0; i-=2) {
	            if (listeners[i]==TreeModelListener.class) {
	                // Lazily create the event:
	                if (treeModelEvent == null)
					{
						Object[] path = new Object[2];
						int[] childIndices = new int[1];
						if ((element.getName().toUpperCase().charAt(0)) > 64 && (element.getName().toUpperCase().charAt(0)) < 91)
						{
							path[1] = (String)this.getChild(ApplicationManager.ontology, (element.getName().toUpperCase().charAt(0)) - 65);
							childIndices[0] = this.getIndexOfChild(String.valueOf(element.getName().toUpperCase().charAt(0)), element);
						}
						else
						{
							path[1] = this.getChild(ApplicationManager.ontology, 26);
							childIndices[0] = this.getIndexOfChild("...", element);
						}
						path[0] = this.getRoot();
						Object[] addedChildren = {element};
	                    treeModelEvent = new TreeModelEvent(this, path, childIndices, addedChildren);
						this.updateIndexes((String)this.getParentNode(element), 1);
					}
					((TreeModelListener)listeners[i+1]).treeNodesInserted(treeModelEvent);
	            }
			}
        }
    }
	
	/**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
	 * @param element 
	 *  
     * @see EventListenerList
     */
    public void fireStringNodesChanged(LinkableElement element) {
        // Guaranteed to return a non-null array
        Object[] listeners = this.listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
				{
					Object[] path = {this.getRoot()};
					int[] childIndice = {this.getIndexOfChild(this.getRoot(), this.getParentNode(element))};
					Object[] elementChanged = {this.getParentNode(element)};
					e = new TreeModelEvent(this.getRoot(), path, 
                                           childIndice, elementChanged);
				}
				((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
            }          
        }
    }
	
	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getPathToRoot(LinkableElement)
	 */
	@Override
	public Object[] getPathToRoot(LinkableElement element) 
	{
		Object[] path = null;
		if (element != null)
		{
			path = new Object[3];
			path[0] = ApplicationManager.ontology;
			path[2] = element;
			if (element.getName().length() > 0 && (element.getName().toUpperCase().charAt(0)) > 64 && (element.getName().toUpperCase().charAt(0)) < 91)
			{
				path[1] = (String)this.getChild(ApplicationManager.ontology, (element.getName().toUpperCase().charAt(0)) - 65);
			}
			else
				path[1] = this.getChild(ApplicationManager.ontology, 26);
		}

		return path;
    }
	
	/**
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#getParentNode(java.lang.Object)
	 */
	public Object getParentNode(Object element)
	{
		if (element instanceof String)
			return this.getRoot();
		else if (element instanceof Lemma && !(element instanceof Ontology))
		{
			if (((LinkableElement)element).getName().toUpperCase().charAt(0) > 64 && ((LinkableElement)element).getName().toUpperCase().charAt(0) < 91)
				return (String.valueOf(((LinkableElement)element).getName().toUpperCase().charAt(0)));
			return "...";
		}
		return null;
	}
	
	
	private void updateIndexes(String elem, int increment)
	{
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(this.letters_index.keySet());
		Collections.sort(keys);
		//System.out.println(keys);
		for (int elemIndex = keys.indexOf(elem); elemIndex < keys.size(); elemIndex++)
		{
			//System.out.println("indice before "+elemIndex+" "+this.letters_index.get(keys.get(elemIndex))[0]+" "+this.letters_index.get(keys.get(elemIndex))[1]);
			this.letters_index.get(keys.get(elemIndex))[0]+=increment;
			if (increment < 0 || elemIndex != keys.indexOf(elem))
				this.letters_index.get(keys.get(elemIndex))[1]+=increment;
			//System.out.println("indice after "+elemIndex+" "+this.letters_index.get(keys.get(elemIndex))[0]+" "+this.letters_index.get(keys.get(elemIndex))[1]);
		}
		this.letters_index.get(keys.get(keys.indexOf(elem)))[0]-=increment;
		//System.out.println("lemmes! "+ApplicationManager.ontology.get(Lemma.KEY));
	}
	
}
