package ontologyEditor.gui.model.treeModel;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/*
 *  Julien Snamartin
 *  Classe Abstraite implémentant TreeModel
 */

abstract public class AbstractTreeModel implements TreeModel {

	public AbstractTreeModel() {
		super();
	}
	
	// retourne la racine de l'arbre
	abstract public Object getRoot();
	
	//Retourne le fils de rang arg1 du nœud arg0.
	abstract public Object getChild(Object arg0, int arg1);

	//Retourne le nombre de fils du nœud arg0.
	abstract public int getChildCount(Object arg0);

	//Retourne true si arg0 est une feuille.
	abstract public boolean isLeaf(Object arg0);

	//  Messaged when the user has altered the value for the item identified by arg0 to arg1.
	abstract public void valueForPathChanged(TreePath arg0, Object arg1);

	// Retourne le rang du nœud  arg1 de père arg0.
	abstract public int getIndexOfChild(Object arg0, Object arg1);

	// Adds a listener for the TreeModelEvent  posted after the tree changes.
	abstract public void addTreeModelListener(TreeModelListener arg0);

	// Removes a listener previously added with addTreeModelListener.
	abstract public void removeTreeModelListener(TreeModelListener arg0);

}
