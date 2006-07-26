package ontologyEditor.gui.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import ontologyEditor.ApplicationManager;

public class LemmaTableRenderer extends DefaultTableCellRenderer  {
	
	
	public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {
	    
	    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    if (ApplicationManager.ontology.getLemmeValider().contains(value))
	    {
    		c.setForeground(Color.MAGENTA);
	    }
	    else
	    {
	    	c.setForeground(Color.BLACK);
	    }
	    return c;
	  } 

}
