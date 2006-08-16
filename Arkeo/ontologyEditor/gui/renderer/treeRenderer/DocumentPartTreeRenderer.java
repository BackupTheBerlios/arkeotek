package ontologyEditor.gui.renderer.treeRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.model.treeModel.CorpusTreeModel;
import ontologyEditor.gui.panels.corpus.CorpusPanel;

/*
 * Julien Sanmartin
 * Claase permmettant de parametrer l'affichage des noeuds dans l'arbre des documents
 */

public class DocumentPartTreeRenderer extends DefaultTreeCellRenderer  {
	
	//cette fonction est appelée à chaque raffraichissement de l'interface
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){ 
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        // si l'ontology et la value est différente de null
        if (value!=null && ApplicationManager.ontology!=null)
        {
        	// recherche du document dans la liste des docValider de la classe Ontology 
        	boolean trouver=false;
        	for (int i=0;i<ApplicationManager.ontology.getDocValider().size();i++)
        	{
        		if (value.toString().equals(ApplicationManager.ontology.getDocValider().get(i).toString()))
        		{
        			trouver=true;
        			break;
        		}
        	}
        	// si value est trouvé
	        if (trouver)
		    {
	        	// affichage du noeud en magenta
		    	this.setForeground(Color.MAGENTA);
		    	this.setText(((DocumentPart)((DefaultMutableTreeNode)value).getUserObject()).getName());
		    }
		    else
		    {
		    	// affichage du noeud en noir
		    	this.setForeground(Color.BLACK);
		    }
	        //tree.expandPath(tree.getSelectionPath());
		}
        if (((CorpusTreeModel)tree.getModel()).getVue()==1)
        {
        	if (((DefaultMutableTreeNode)value).getUserObject() instanceof DocumentPart)
        	{
        		DocumentPart nom = (DocumentPart)((DefaultMutableTreeNode)value).getUserObject();
        		this.setText(nom.getName());
        	}
        }
        else
        {	if (((DefaultMutableTreeNode)value).getUserObject() instanceof DocumentPart)
    		{
	        	DocumentPart nom = (DocumentPart)((DefaultMutableTreeNode)value).getUserObject();
				this.setText(nom.toString());
    		}
        }
        return this;
    }
	
	 

}
