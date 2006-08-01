package ontologyEditor.gui.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;

import ontologyEditor.ApplicationManager;

public class LemmeEditionRenderer extends DefaultTableCellRenderer  {
	
	
	public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {
	    
	    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    if (value instanceof Lemma)
	    {
    		c.setForeground(new Color(0,102,0));
	    }
	    else if (value instanceof DocumentPart)
	    {
	    	c.setForeground(new Color(102,51,0));
	    }
	    else
	    {
	    	c.setForeground(Color.BLACK);
	    }
	    
	    return c;
	  } 

}
