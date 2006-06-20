/**
 * Created on 31 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.treeviews;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LinkableElementTree extends JTree
{
	/**
	 * @param model The <code>TreeModel</code> to use for filling this subclass of <code>JTree</code>. 
	 */
	private AbstractTreeModel model;
	
	public LinkableElementTree()
	{
		super();
	}
	
	public LinkableElementTree(TreeModel model)
	{
		super(model);
		((AbstractTreeModel) model).notifyReady();
	}
	
	/**
	 * @return The <code>ArrayList</code> of <code>TreePaths</code> currently expanded.  
	 */
	public ArrayList<TreePath> getExpandedPaths()
	{
		ArrayList<TreePath> expandedPaths = new ArrayList<TreePath>();
		if (this.getModel().getRoot() != null)
		{
			TreePath rootPath = new TreePath(this.getModel().getRoot());
			Enumeration<TreePath> paths = this.getExpandedDescendants(rootPath);
			if(paths != null)
				while(paths.hasMoreElements())
					expandedPaths.add(paths.nextElement());
		}
		return expandedPaths;
	}

	/**
	 * @param expandedPaths The <code>TreePaths</code> to expand. 
	 */
	public void setExpandedPaths(ArrayList<TreePath> expandedPaths)
	{
		if(expandedPaths == null)
			return;

		for(int i = 0; i < expandedPaths.size(); i++)
			this.expandPath(expandedPaths.get(i));
	}

	public AbstractTreeModel getModel() {
		return model;
	}

	public void setModel(AbstractTreeModel model) {
		this.model = model;
	}

	/**
	 * @see javax.swing.JTree#expandPath(javax.swing.tree.TreePath)
	 */
	//@Override
	/*public void expandPath(TreePath path)
	{
		super.expandPath(path);
		((AbstractTreeModel)this.getModel()).
	}*/
	
	
	
}
