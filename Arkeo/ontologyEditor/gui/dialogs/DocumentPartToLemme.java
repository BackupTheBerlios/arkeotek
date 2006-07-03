/**
 * 
 */
package ontologyEditor.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import ontologyEditor.gui.tables.LinesTableModel;
import arkeotek.ontology.LinkableElement;

/**
 * @author sanmartin
 *
 */
public class DocumentPartToLemme extends JDialog {

	private JFrame frame;
	   
	private JLabel titre;
	
	private JTable appearancesTable;
   
	private static final String FERMER_CAPTION = "Fermer";
	
	private JButton close_button = new JButton(FERMER_CAPTION);
	
	private LinkableElement lemme;


	public DocumentPartToLemme(JFrame frame,LinkableElement lemme){
		super(frame,"Documents associés au terme "+lemme.getName(), true);
		this.lemme=lemme;
        this.frame = frame;
		double border = 10;
		double separator = 5 + 2 * border;
		double line = 30;
		double lbl_width = 140;
		double size[][] = { 
				{ border, TableLayout.FILL,TableLayout.FILL,TableLayout.FILL, border }, // Columns
				{ border, line,line,line,line,line,line,line, border, line , line , line, border}  // Rows
			};
		this.setLayout(new TableLayout(size));
		this.setLocationRelativeTo(null);
		
		String[] titre={"Relation","Identifiant","Aperçu"};
		LinesTableModel tableModel = new LinesTableModel();
		tableModel.setColumnNames(titre);
		this.appearancesTable=new JTable();
		this.appearancesTable.setModel(tableModel);
		//remplirTableLemmeParent(lemme);
		TableColumn column1 = appearancesTable.getColumnModel().getColumn(0);
		column1.setPreferredWidth(50);
		TableColumn column2 = appearancesTable.getColumnModel().getColumn(1);
		column2.setPreferredWidth(250);
		TableColumn column3 = appearancesTable.getColumnModel().getColumn(2);
		column3.setPreferredWidth(550);
		this.appearancesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane appearancesScrollPane = new JScrollPane();
		appearancesScrollPane.setViewportView(this.appearancesTable);
		appearancesScrollPane.setBorder(BorderFactory.createTitledBorder("Documents recensés"));
		
		// Create the components. 
		
		this.close_button.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				dispose();
			}
		});
		this.add(this.close_button, "2, 10, 1, 1");
		this.add(appearancesScrollPane, "1, 1, 3, 7");
		this.setResizable(true);
		this.setLocation(140,240);
		//this.setLocationRelativeTo(this.getParent());
		this.setPreferredSize(new Dimension(1000,350));
		this.pack();
	}
}
