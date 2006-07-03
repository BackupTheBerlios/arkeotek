/**
 * Created on 6 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.tables.RelationsEditorTableModel;
import ontologyEditor.gui.tables.RelationsTypeComboModel;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 *
 */
public class RelationsTypeDialog extends JDialog  implements ItemListener
{
	private JFrame frame;
	private JScrollPane borderedPaneTable;
	private JScrollPane borderedPaneCombo;
	
	private JList table = null;
	private JButton okButton = new JButton();
	
	private RelationsTypeComboModel  models[] = null;
	private JComboBox [] combos = null;
	
	private int currentCombo = 0;

	
	public RelationsTypeDialog()
	{
		this(DisplayManager.mainFrame);
	}

	private RelationsTypeDialog(JFrame frame)
	{
        super(frame, ApplicationManager.getApplicationManager().getTraduction("relationseditor"), true);
        this.frame = frame;
        
        // les relations
        Object[] relations = ApplicationManager.ontology.get(Relation.KEY).toArray();
        int nombreDeRelations = relations.length;
        
        // PARTIE TABLE
        table = new JList(relations);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // PARTIE COMBOS
        ApplicationManager.ontology.get(Relation.KEY).size();
        models = new RelationsTypeComboModel[nombreDeRelations];
        combos = new JComboBox[nombreDeRelations];
        
        for(int i=0;i<models.length;i++)
        {
        	models[i] = new RelationsTypeComboModel();
        	combos[i] = new JComboBox(models[i]);
        }
        
        
        
        /*
         * Window Disposition
         */
		double espacement = 20;
		double buttonHeight = 25;
		double buttonWidth = 105;
		double size[][] = {  
							{ espacement, TableLayout.FILL, espacement, buttonWidth, espacement }, // Columns
							{ espacement, buttonHeight, TableLayout.FILL, buttonHeight, espacement }  // Rows
						  };
		this.setLayout(new TableLayout(size));
		

		borderedPaneTable = new JScrollPane();
		borderedPaneTable.setBorder(BorderFactory.createTitledBorder("Relations"));
		borderedPaneTable.setViewportView(table);
		this.add(borderedPaneTable, "1, 1, 1, 3");
		
		
		borderedPaneCombo = new JScrollPane();
		borderedPaneCombo.setBorder(BorderFactory.createTitledBorder("Type"));
		borderedPaneCombo.setViewportView(combos[currentCombo]);
		this.add(borderedPaneCombo, "3, 1, 3, 1");
        /*com.addItem("Concept - Concept");
        typeComboBox.addItem("Terme - Concept");
        typeComboBox.addItem("Terme - Document");
        typeComboBox.addItem("Terme - Terme");
        typeComboBox.addItem("Concept - Document");
        typeComboBox.addItemListener(this);
        this.add(typeComboBox,"1, 1, 2, 1");
        */
		
		okButton.setText(ApplicationManager.getApplicationManager().getTraduction("okbutton"));
        this.add(this.okButton, "3, 3, 3, 3");
		this.okButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				validateInput();
			}
		});

		this.setSize(450,350);
		this.setLocation((frame.getLocation().x) + (frame.getSize().width/2) - (this.getSize().width/2),(frame.getLocation().y) + (frame.getSize().height/2) - (this.getSize().height/2));
		this.setVisible(true);
	}
	
	public void itemStateChanged(ItemEvent evt)  
    {
		//changerTable(typeComboBox.getSelectedIndex());
    }
	
	public void changerCombo(int viewNum)
	{
		this.currentCombo=viewNum;
		borderedPaneCombo.setViewportView(combos[currentCombo]);
	}

    private void validateInput()
    {
    	validInput();
    	dispose();
    }

	public boolean validInput()
	{
		return true;
	}
}