package ontologyEditor.gui.model.treeModel;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

abstract public class AbstractTreeModel implements TreeModel {

	public AbstractTreeModel() {
		super();
	}

	abstract public Object getRoot();
	
	abstract public Object getChild(Object arg0, int arg1);

	abstract public int getChildCount(Object arg0);

	abstract public boolean isLeaf(Object arg0);

	abstract public void valueForPathChanged(TreePath arg0, Object arg1);

	abstract public int getIndexOfChild(Object arg0, Object arg1);

	abstract public void addTreeModelListener(TreeModelListener arg0);

	abstract public void removeTreeModelListener(TreeModelListener arg0);

}
