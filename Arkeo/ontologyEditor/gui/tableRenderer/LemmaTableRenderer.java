package ontologyEditor.gui.tableRenderer;

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
import ontologyEditor.Constants;
import ontologyEditor.ImagesManager;

public class LemmaTableRenderer extends DefaultTableCellRenderer  {
	
	
	public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {
	    
	    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    if (ApplicationManager.ontology.getLemmeValider().contains(value))
	    {
	    	c.setForeground(new Color(0,102,0));
	    }
	    else
	    {
	    	c.setForeground(Color.BLACK);
	    }
	    return c;
	  } 

}
