/**
 * Created on 6 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class RelationsEditorDialog extends JDialog  implements ItemListener
{
	private JFrame frame;
	private JScrollPane borderedPane;
	
	private JComboBox typeComboBox = new JComboBox();
	private JButton okButton = new JButton();
	//private JButton cancelButton = new JButton();
	private JTextField textField = new JTextField();
	private JButton addButton = new JButton();
	private JButton delButton = new JButton();
	
	private RelationsEditorTableModel  models[] = new RelationsEditorTableModel[5];
	private JTable [] listes = new JTable[5];
	
	private int currentList = 0;
	
	public RelationsEditorDialog()
	{
		this(DisplayManager.mainFrame);
	}

	private RelationsEditorDialog(JFrame frame)
	{
        super(frame, ApplicationManager.getApplicationManager().getTraduction("relationseditor"), true);
        this.frame = frame;
        
        for(int i=0;i<models.length;i++)
        {
        	models[i] = new RelationsEditorTableModel(i);
        	listes[i] = new JTable(models[i]);
        }
        
        
        /*
         * Window Disposition
         */
		double espacement = 20;
		double longSpace = 90;
		double buttonHeight = 25;
		double buttonWidth = 105;
		double size[][] = {  
							{ espacement, TableLayout.FILL, buttonWidth, longSpace, espacement, buttonWidth, espacement }, // Columns
							{ espacement, buttonHeight, buttonHeight, buttonHeight, buttonHeight, buttonHeight, buttonHeight, buttonHeight, TableLayout.FILL, espacement, buttonHeight, espacement }  // Rows
						  };
		this.setLayout(new TableLayout(size));
		/*
		 * 
		 */

		
		borderedPane = new JScrollPane();
		borderedPane.setBorder(BorderFactory.createTitledBorder("Relations"));
		borderedPane.setViewportView(listes[currentList]);
		this.add(borderedPane, "1, 3, 3, 8");
		
        typeComboBox.addItem("Concept - Concept");
        typeComboBox.addItem("Terme - Concept");
        typeComboBox.addItem("Terme - Document");
        typeComboBox.addItem("Terme - Terme");
        typeComboBox.addItem("Concept - Document");
        typeComboBox.addItemListener(this);
        this.add(typeComboBox,"1, 1, 2, 1");
        
		okButton.setText(ApplicationManager.getApplicationManager().getTraduction("okbutton"));
        this.add(this.okButton, "5, 10, 5, 10");
		this.okButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.validateInput();
			}
		});

/*		
 		cancelButton.setText(ApplicationManager.getApplicationManager().getTraduction("cancelbutton"));
		this.add(this.cancelButton, "1, 10, 2, 10");
		this.cancelButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.cancelInput();
			}
		});
*/
		textField.setText("");
		this.add(textField,"5, 4, 5, 4");
		
		addButton.setText(ApplicationManager.getApplicationManager().getTraduction("addbutton"));
		this.add(this.addButton, "5, 5, 5, 5");
		this.addButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				RelationsEditorDialog.this.addInput();
			}
		});
		
		delButton.setText(ApplicationManager.getApplicationManager().getTraduction("delbutton"));
		this.add(this.delButton, "5, 7, 5, 7");
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
	
	public void itemStateChanged(ItemEvent evt)  
    {
		changerTable(typeComboBox.getSelectedIndex());
    }
	
	public void changerTable(int viewNum)
	{
		this.currentList=viewNum;
		borderedPane.setViewportView(listes[viewNum]);
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
    	/*
    	 * Lors de la suppression le cas suivant devra etre traité !
    	 * 
    	 * Si la relation est utilisée alors dire à l'utilisateur
    	 * qu'il ne peut le faire car il ne doit pas utiliser la relation.
    	 */
    	
    	int [] sr = listes[currentList].getSelectedRows();
    	if(sr.length>0)
    	{
    		models[currentList].delRows(sr);
    	}
    	listes[currentList].changeSelection(0,0,false,false);
    	listes[currentList].updateUI();
	}

    private void addInput()
    {
		String nom = textField.getText();
    	if(!nom.equals(""))	models[currentList].addRow(nom);
    	textField.setText("");
    	listes[currentList].updateUI();
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