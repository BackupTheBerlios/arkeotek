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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import ontologyEditor.DisplayManager;

/**
 * @author Sanmartin Julien
 * @author Nouhen Hubert
 *
 */
public class SearchLemmaDialog extends JDialog  implements ActionListener, PropertyChangeListener
{
	private JFrame frame;
	   
	private JLabel lbl_contain;
	private JTextField txt_contain;
   
	private static final String SEARCH_CAPTION = "Rechercher";
	private static final String CANCEL_CAPTION = "Annuler";

	private Object value;

	private boolean valid = false;
	
	private JButton search_button = new JButton(SEARCH_CAPTION);
	private JButton cancel_button = new JButton(CANCEL_CAPTION);


	public SearchLemmaDialog()
	{
		this(DisplayManager.mainFrame);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private SearchLemmaDialog(JFrame frame)
	{
        super(frame, "Recherche d'un Lemme", true);
        this.frame = frame;
        
		double border = 10;
		double separator = 5 + 2 * border;
		double line = 30;
		double lbl_width = 240;
		double txt_width = 240;
		double size[][] = { 
				{ border, lbl_width, border, txt_width, border }, // Columns
				{ border, line, separator, line, border, line }  // Rows
			};
		this.setLayout(new TableLayout(size));
		//this.setLocationRelativeTo(null);
		
        // Create the components. 
		this.lbl_contain = new JLabel("Le lemme contient la chaine : ");
        this.add(this.lbl_contain, "1, 1, 1, 1");
		
		this.txt_contain = new JTextField("");
		this.add(this.txt_contain, "3, 1, 3, 1");
		

		// Validation Button Action
		this.search_button.addMouseListener(new MouseAdapter()
		{
			/**
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent e)
			{
				SearchLemmaDialog.this.validateInput();
			}
		});
		this.add(this.search_button, "3, 3, 3, 3");
		
		
		// Annulation Button Action
		this.cancel_button.addMouseListener(new MouseAdapter()
				{
					/**
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					@Override
					public void mouseClicked(MouseEvent e)
					{
						SearchLemmaDialog.this.cancelInput();
					}
				});
		this.add(this.cancel_button, "1, 3, 1, 3");
		
		this.setResizable(false);
		this.pack();
		this.setLocation((frame.getLocation().x) + (frame.getSize().width/2) - (this.getSize().width/2),(frame.getLocation().y) + (frame.getSize().height/2) - (this.getSize().height/2));
	}

	/**
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 	*/
	public void actionPerformed(ActionEvent e) {
		this.value = SEARCH_CAPTION;
    }

    /** 
     * This method reacts to state changes in the option pane. 
     * @param e The event to treat. 
     */
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (isVisible() && (JOptionPane.VALUE_PROPERTY.equals(prop) || JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
            Object temp_value = this.value;

            if (temp_value == JOptionPane.UNINITIALIZED_VALUE) {
                // Ignore reset
                return;
            }

            // Reset the JOptionPane's value.
            // If you don't do this, then if the user
            // presses the same button next time, no
            // property change event will be fired.
            this.value = JOptionPane.UNINITIALIZED_VALUE;

            if (SEARCH_CAPTION.equals(temp_value))
            {
				String err_msg = "";
				if (this.txt_contain.getText().equals("")) err_msg += "Le nom de l'ontologie ne doit pas \u00eatre vide. \n";
				if (!err_msg.equals("")) err_msg += "Des erreurs de saisies ont \u00e9t\u00e9 rep\u00e9r\u00e9es : \n";
				else
				{
					//this.setVisible(false);
					this.dispose();
				}
            }
            else
            {
				//this.setVisible(false);
            	this.dispose();
            }
        }
    }

	private void validateInput()
	{
		String err_msg = "";
		if (this.txt_contain.getText().equals(""))
		{
			err_msg += "\t- Nom de l'ontologie non sp\u00e9cifi\u00e9. \n";
			this.dispose();
		}
		else
		{
			this.valid = true;
			this.dispose();
		}
	}

	private void cancelInput()
	{
		this.dispose();
	}
   
	/**
	 * @return Whether the input is valid or not. 
	 */
	public boolean validInput()
	{
		return this.valid;
	}
	
	/**
	 * @return The typed name of the new <code>Ontology</code> if the input has been validated. 
	 */
	public String getContainName() {
		return this.validInput() ? this.txt_contain.getText() : null;
	}
}