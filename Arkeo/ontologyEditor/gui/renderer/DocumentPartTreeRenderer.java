package ontologyEditor.gui.renderer;

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

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;

public class DocumentPartTreeRenderer extends DefaultTreeCellRenderer  {
	
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){ 
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value!=null && ApplicationManager.ontology!=null)
        {
        	boolean trouver=false;
        	for (int i=0;i<ApplicationManager.ontology.getDocValider().size();i++)
        	{
        		if (value.toString().equals(ApplicationManager.ontology.getDocValider().get(i).toString()))
        		{
        			trouver=true;
        			break;
        		}
        	}
        	
	        if (trouver)
		    {
		    	this.setForeground(Color.MAGENTA);
		    }
		    else
		    {
		    	this.setForeground(Color.BLACK);
		    }
	        //tree.expandPath(tree.getSelectionPath());
		}
        return this;
    }
	
	 

}
