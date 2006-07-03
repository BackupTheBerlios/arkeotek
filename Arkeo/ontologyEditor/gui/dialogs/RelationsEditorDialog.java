/**
 * Created on 6 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.tables.RelationsEditorTableModel;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 *
 */
public class RelationsEditorDialog extends JDialog  implements ActionListener
{
	private JFrame frame;
	
	private JButton okButton = new JButton();
	private JButton cancelButton = new JButton();
	private JTextField textField = new JTextField();
	private JButton addButton = new JButton();
	private JButton delButton = new JButton();
	
	private RelationsEditorTableModel model = new RelationsEditorTableModel();
	private JTable liste = new JTable(model);
	
	public RelationsEditorDialog()
	{
		this(DisplayManager.mainFrame);
	}

	private RelationsEditorDialog(JFrame frame)
	{
        super(frame, ApplicationManager.getApplicationManager().getTraduction("relationseditor"), true);
        this.frame = frame;
        
        
        
        /*
         * Window Disposition
         */
		double espacement = 20;
		double longSpace = 90;
		double buttonHeight = 25;
		double buttonWidth = 105;
		double size[][] = {  
							{ espacement, TableLayout.FILL, buttonWidth, longSpace, espacement, buttonWidth, espacement }, // Columns
							{ espacement, buttonHeight, buttonHeight, buttonHeight, TableLayout.FILL, espacement, buttonHeight, espacement }  // Rows
						  };
		this.setLayout(new TableLayout(size));
		/*
		 * 
		 */

		
		JScrollPane borderedPane = new JScrollPane();
		borderedPane.setBorder(BorderFactory.createTitledBorder("Relations"));
		
		this.liste = new JTable(model);
		borderedPane.setViewportView(liste);
		//this.liste.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//panel.add(this.liste);
        this.add(borderedPane, "1, 1, 3, 4");
		
		okButton.setText(ApplicationManager.getApplicationManager().getTraduction("okbutton"));
        this.add(this.okButton, "5, 6, 5, 6");
		this.okButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.validateInput();
			}
		});

		cancelButton.setText(ApplicationManager.getApplicationManager().getTraduction("cancelbutton"));
		this.add(this.cancelButton, "1, 6, 2, 6");
		this.cancelButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.cancelInput();
			}
		});

		textField.setText("");
		this.add(textField,"5, 1, 5, 1");
		
		addButton.setText(ApplicationManager.getApplicationManager().getTraduction("addbutton"));
		this.add(this.addButton, "5, 2, 5, 2");
		this.addButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.addInput();
			}
		});
		
		delButton.setText(ApplicationManager.getApplicationManager().getTraduction("delbutton"));
		this.add(this.delButton, "5, 3, 5, 3");
		this.delButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.delInput();
			}
		});
		
		this.setSize(450,350);
		this.setLocation((frame.getLocation().x) + (frame.getSize().width/2) - (this.getSize().width/2),(frame.getLocation().y) + (frame.getSize().height/2) - (this.getSize().height/2));
		this.setVisible(true);
	}

	/**
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 	*/
	public void actionPerformed(ActionEvent e)
	{

    }

    private void validateInput()
    {
    	validInput();
    	dispose();
    }

    private void cancelInput()
    {
    	this.dispose();
    }
    
    private void delInput()
    {
    	int [] sr = liste.getSelectedRows();
    	if(sr.length>0)
    	{
    		model.delRows(sr);
    	}
    	liste.changeSelection(0,0,false,false);
    	liste.updateUI();
	}

    private void addInput()
    {
		String nom = textField.getText();
    	if(!nom.equals(""))	model.addRow(nom);
    	textField.setText("");
    	liste.updateUI();
	}
    
    private class StorageCell
    {
    	private String name = "";
    	private String driver = "";
   
		/**
		* @param name The displayable name of this cell. 
		* @param driver The Java class name of the <code>IService</code> to use for this kind of storage.
		*/
		public StorageCell(String name, String driver)
		{
			this.name = name;
			this.driver = driver;
		}
	   
	   /**
	    * @return The Java class name of the <code>IService</code> to use for this kind of storage. 
	    */
	   public String getDriver() {
		   return this.driver;
	   }
	   
	   /**
	    * @return The displayable name of this cell. 
	    */
	   public String toString() {
		   return this.name;
	   }
   }

	/**
	 * @return Whether the input is valid or not. 
	 */
	public boolean validInput()
	{
		return true;
	}
}