package ontologyEditor.gui.renderer.tableRenderer;

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

/*
 * Julien Snamartin
 * Classe permettant de parametrer l'affichage dans la table BottomEdition
 */

public class LemmeEditionRenderer extends DefaultTableCellRenderer  {
	
	// cette fonction est appelée à chaque raffraichissement de l'interface
	public Component getTableCellRendererComponent (JTable table, Object value, boolean isSelected,
	        boolean hasFocus, int row, int column) {
	    
	    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    // si value est une instance de la classe Lemma alors on change la couleur des caracteres 
	    // de la ligne coorespondante a value
	    if (value instanceof Lemma)
	    {
    		c.setForeground(new Color(0,102,0));
	    }
	    // si value est une instance de la classe DocumentPart alors on change la couleur des caracteres 
	    // de la ligne coorespondante a value
	    else if (value instanceof DocumentPart)
	    {
	    	c.setForeground(new Color(102,51,0));
	    }
	    else
	    {
	    	// sinon on met la couleur de la ligne en noir
	    	c.setForeground(Color.BLACK);
	    }
	    
	    return c;
	  } 

}
