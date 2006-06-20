/**
 * Created on 13 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.treeviews;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.IIndexable;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class AbstractTreeModel implements TreeModel
{

	protected EventListenerList listenerList = new EventListenerList();
	protected int indexElementRemoved;
	private int elements_key;
	
	
	/**
	 * Creates a new <code>AbstractTreeModel</code> based on ApplicationManager.ontology. 
	 * @param wished_elements_key The category key of the elements to display. 
	 */
	public AbstractTreeModel(int wished_elements_key)
	{
		this.elements_key = wished_elements_key;
	}

	/**
	 * @return Returns the root.
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Ontology getRoot()
	{
		return ApplicationManager.ontology;
	}

	/**
	 * @return The category key of the node elements of this <code>TreeModel</code>.  
	 */
	public int getModelCategory() {
		return this.elements_key;
	}
	
	/**
	 * Return the child at rank <code>index</code> from the <code>parent</code>
	 * @return The child
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object parent, int index)
	{
		Object elem = null;
		elem = getElementsFromIndexable((LinkableElement) parent).get(index);
		return elem;		
	}

	/**
	 * Return the number of children of <code>parent</code>
	 * @return children number
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public abstract int getChildCount(Object parent);

	/**
	 * Check if the <code>node</code> is a leaf
	 * @return true if <code>node</code> is a leaf
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object node)
	{
		if ((((LinkableElement) node).getCategoryKey() != this.getModelCategory()) && !(node instanceof Ontology))
			return true;
		return false;
	}

	/**
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		// We don't need to code this method. 
	}

	/**
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object parent, Object child)
	{
		ArrayList elems = new ArrayList();
        if (parent instanceof Ontology)
			elems = ((Ontology) parent).get(this.getModelCategory());
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
	 * Notifies the listeners that the tree structure changed. 
	 */
	public void fireTreeStructureChanged()
    {
		if (this.getRoot() != null)
		{
			Ontology root = this.getRoot();
			Object[] listeners = this.listenerList.getListenerList();
	        TreeModelEvent treeModelEvent = null;
	
	        for (int i = listeners.length-2; i>=0; i-=2) {
	            if (listeners[i]==TreeModelListener.class) {
	                // Lazily create the event:
	                if (treeModelEvent == null)
					{
						int childrenCount = this.getChildCount(this.getRoot());
						int[] childIndices = new int[childrenCount];
						Object[] children = new Object[childrenCount];
						for (int j = 0; j < childrenCount; j++)
						{
							childIndices[j] = j;
							children[j] = this.getChild(this.getRoot(), j);
						}
						treeModelEvent = new TreeModelEvent(this.getRoot(), new TreePath(this.getRoot()), childIndices, children);
					}
					((TreeModelListener)listeners[i+1]).treeStructureChanged(treeModelEvent);
	            }
	        }
		}
    }
	
	/**
	 * Notifies the listeners that the tree structure changed. 
	 * @param source 
	 * @param path 
	 * @param childIndices 
	 * @param children 
	 */
	protected void fireTreeStructureChanged(Object source, Object[] path, 
            int[] childIndices, 
            Object[] children) 
	{
		System.out.println("fireTreeStructureChanged");
		/*if (getChildCount(path[path.length-1]) > 0)
		{*/
			// Guaranteed to return a non-null array
			Object[] listeners = this.listenerList.getListenerList();
			TreeModelEvent e = null;
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length-2; i>=0; i-=2) 
			{
				if (listeners[i]==TreeModelListener.class) 
				{
					// Lazily create the event:
					if (e == null)
					e = new TreeModelEvent(source, path, 
					               childIndices, children);
					((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
				}          
			}
		//}
	}
	
	/**
	 * Notifies the listeners that a tree node was removed. 
	 * @param element the <code>LinkableElement</code> removed
	 * @param index the index in tree of the element
	 */
	public void fireTreeNodesRemoved(LinkableElement element, int index)
    {
        Object[] listeners = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;
		this.indexElementRemoved = index;
		
		if (this.getIndexOfChild(this.getRoot(), element) != -1)
        {
			for (int i = listeners.length-2; i>=0; i-=2) {
	            if (listeners[i]==TreeModelListener.class) {
	                // Lazily create the event:
	                if (treeModelEvent == null)
					{
						Object[] path = {this.getRoot()};
						int[] childIndices = {index};
						Object[] removedChildren = {element};
	                    treeModelEvent = new TreeModelEvent(this, path, childIndices, removedChildren);
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
		System.out.println("fireTreeStructureAdded");
        Object[] listeners = this.listenerList.getListenerList();
        TreeModelEvent treeModelEvent = null;

		if (this.getIndexOfChild(this.getRoot(), element) != -1)
        {
			for (int i = listeners.length-2; i>=0; i-=2) {
	            if (listeners[i]==TreeModelListener.class) {
	                // Lazily create the event:
	                if (treeModelEvent == null)
					{
						Object[] path = {this.getRoot()};
						int[] childIndices = {this.getChildCount(this.getRoot())-1};
						Object[] addedChildren = {element};
	                    treeModelEvent = new TreeModelEvent(this, path, childIndices, addedChildren);
					}

					((TreeModelListener)listeners[i+1]).treeNodesInserted(treeModelEvent);
	            }
			}
        }
    }
	
	/**
     * Adds the specified <code>TreeModelListener</code> to the listeners list. 
     * @param l the listener to add
     * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l)
    {
        this.listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Removes the specified <code>TreeModelListener</code> from the listeners list. 
     * @param l the listener to remove
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l)
    {
        this.listenerList.remove(TreeModelListener.class, l);

    }
	

	/**
	 * Reload the node representing the element
	 * @param element
	 */
	public void reload(LinkableElement element)
	{
		if(element != null)
			fireTreeStructureChanged(this, getPathToRoot(element), null, null);
	}
	
	/**
	 * Reloads all of the elements of the tree. 
	 * @see ontologyEditor.gui.treeviews.AbstractTreeModel#reload(LinkableElement)
	 */
	public void reloadTree()
	{
		for (LinkableElement element : ApplicationManager.ontology.get(this.getModelCategory()))
			this.reload(element);
	}
	
	/**
	 * Return the node parent of <code>element</code> passed in parameter
	 * @param element
	 * @return the parent node
	 */
	public abstract Object getParentNode(Object element);
	
	
	/**
	 * Return the path from the <code>element</code> to the root
	 * @param element the child element
	 * @return the path to the root
	 */
	public abstract Object[] getPathToRoot(LinkableElement element);
	
	protected void notifyReady() {
		// Nothing to notify by default. 
	}
	
	protected abstract ArrayList<LinkableElement> getElementsFromIndexable(LinkableElement parent);
}
