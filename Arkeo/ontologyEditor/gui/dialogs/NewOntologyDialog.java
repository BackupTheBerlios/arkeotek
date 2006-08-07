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
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import arkeotek.io.EncryptedProperties;
import arkeotek.io.db.Service;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 *
 */
public class NewOntologyDialog extends JDialog  implements ActionListener, PropertyChangeListener
{
	private JFrame frame;
	   
	private JLabel lbl_ontology_name;
	private JLabel lbl_storage_type;
	private JLabel lbl_storage_repository;
	private JLabel lbl_user_login;
	private JLabel lbl_user_password;
	private JLabel lbl_user_confirm;
	
	private JTextField txt_ontology_name;
	private JTextField txt_storage_repository;
	private JComboBox cmb_storage_type;
	private JTextField txt_user_login;
	private JPasswordField txt_user_password;
	private JPasswordField txt_user_confirm;
   
	private static final String VALIDATE_CAPTION = ApplicationManager.getApplicationManager().getTraduction("validate");
	private static final String CANCEL_CAPTION = ApplicationManager.getApplicationManager().getTraduction("cancel");

	private Object value;

	private boolean valid = false;
	
	private JButton validate_button = new JButton(VALIDATE_CAPTION);
	private JButton cancel_button = new JButton(CANCEL_CAPTION);

	/**
	 * 
	 */
	public NewOntologyDialog()
	{
		this(DisplayManager.mainFrame);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private NewOntologyDialog(JFrame frame)
	{
        super(frame, ApplicationManager.getApplicationManager().getTraduction("ontologycreation"), true);
        this.frame = frame;
		double border = 10;
		double separator = 5 + 2 * border;
		double line = 30;
		double lbl_width = 140;
		double size[][] = { 
				{ border, lbl_width, border, TableLayout.FILL, border }, // Columns
				{ border, line, border, separator, line, border, line, border, separator, line, border, line, border, line, border, separator, line, border }  // Rows
			};
		this.setLayout(new TableLayout(size));
		//this.setLocationRelativeTo(null);
		
        // Create the components. 
		this.lbl_ontology_name = new JLabel(ApplicationManager.getApplicationManager().getTraduction("nameoftheontology")); 
        this.add(this.lbl_ontology_name, "1, 1, 1, 1");
		
		this.txt_ontology_name = new JTextField("");
		this.add(this.txt_ontology_name, "3, 1 ,3 , 1");
		
		this.add(new JSeparator(), "1, 3, 3, 2");
		
		this.lbl_storage_type = new JLabel("Type de stockage : ");
		this.add(this.lbl_storage_type, "1, 4, 1, 3");

		this.cmb_storage_type = new JComboBox();
		this.cmb_storage_type.addItem(new StorageCell(ApplicationManager.getApplicationManager().getTraduction("stockagetype"), ""));
		this.cmb_storage_type.addItem(new StorageCell(ApplicationManager.getApplicationManager().getTraduction("dbmysql"), Service.class.getCanonicalName()));
		this.add(this.cmb_storage_type, "3, 4, 3, 3");
		
		this.lbl_storage_repository = new JLabel(ApplicationManager.getApplicationManager().getTraduction("address"));
		this.add(this.lbl_storage_repository, "1, 6, 1, 5");
		
		this.txt_storage_repository = new JTextField("jdbc:mysql://");
		this.add(this.txt_storage_repository, "3, 6, 3, 5");
		
		this.add(new JSeparator(), "1, 8, 3, 6");
		
		this.lbl_user_login = new JLabel(ApplicationManager.getApplicationManager().getTraduction("username"));
		this.add(this.lbl_user_login, "1, 9, 1, 7");

		this.txt_user_login = new JTextField("");
		this.add(this.txt_user_login, "3, 9, 3, 7");
		
		this.lbl_user_password = new JLabel(ApplicationManager.getApplicationManager().getTraduction("pwd"));
		this.add(this.lbl_user_password, "1, 11, 1, 9");

		this.txt_user_password = new JPasswordField("");
		this.add(this.txt_user_password, "3, 11, 3, 9");
		
		this.lbl_user_confirm = new JLabel(ApplicationManager.getApplicationManager().getTraduction("retype"));
		this.add(this.lbl_user_confirm, "1, 13, 1, 11");

		this.txt_user_confirm = new JPasswordField("");
		this.add(this.txt_user_confirm, "3, 13, 3, 11");
		
		this.add(new JSeparator(), "1, 15, 3, 12");
		
		this.validate_button.addMouseListener(new MouseAdapter()
		{
			/**
			 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseClicked(MouseEvent e)
			{
				NewOntologyDialog.this.validateInput();
			}
		});
		this.add(this.validate_button, "3, 16, 3, 13");
		this.cancel_button.addMouseListener(new MouseAdapter()
				{
					/**
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					@Override
					public void mouseClicked(MouseEvent e)
					{
						NewOntologyDialog.this.cancelInput();
					}
				});
		this.add(this.cancel_button, "1, 16, 1, 13");
		
		/*
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Panneau de navigation:"));
		this.add(panel);
		panel.add(lbl_ontology_name);
		panel.add(txt_ontology_name);
		*/
		
		// Disposition de la fenetre
		this.setResizable(false);
		this.pack();
		this.setLocation((frame.getLocation().x) + (frame.getSize().width/2) - (this.getSize().width/2),(frame.getLocation().y) + (frame.getSize().height/2) - (this.getSize().height/2));
	}

/**
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 	*/
	public void actionPerformed(ActionEvent e) {
		this.value = VALIDATE_CAPTION;
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

            if (VALIDATE_CAPTION.equals(temp_value)) {
				String err_msg = "";
				if (this.txt_ontology_name.getText().equals(""))
					err_msg += ApplicationManager.getApplicationManager().getTraduction("emptyontologyname");
				if (!com.sun.org.apache.xerces.internal.util.URI.isConformantSchemeName(this.txt_storage_repository.getText()))
					err_msg += ApplicationManager.getApplicationManager().getTraduction("badaddress");
				if (!err_msg.equals(""))
					err_msg = ApplicationManager.getApplicationManager().getTraduction("inputerror");
				else {
					// TODO Coder l'enregistrement de l'ontologie.
					this.setVisible(false);
				}
            } else {
				this.setVisible(false);
            }
        }
    }

	private void validateInput()
	{
		String err_msg = "";
		if (this.txt_ontology_name.getText().equals(""))
			err_msg += ApplicationManager.getApplicationManager().getTraduction("ontologynamenotspecified");
		if (this.txt_storage_repository.getText().equals(""))
			err_msg += ApplicationManager.getApplicationManager().getTraduction("depotaddress");
		
		/*if (this.txt_storage_repository.getText().equals("")) {
			if (this.cmb_storage_type.getSelectedItem() == null)
				err_msg += "\t- Type de stockage des données non spécifié. \n";
		}*/
//		if (!com.sun.org.apache.xerces.internal.util.URI.isWellFormedAddress(this.txt_storage_repository.getText()))
//			err_msg += "\t- L'emplacement doit Ãªtre une URI (chemin d'accÃ¨s ou URL). \n";
		if (!String.valueOf(this.txt_user_confirm.getPassword()).equals(String.valueOf(this.txt_user_password.getPassword()))) {
			err_msg += ApplicationManager.getApplicationManager().getTraduction("pwdnotmatch");
			this.txt_user_password.setText("");
			this.txt_user_confirm.setText("");
		}
		if (!err_msg.equals("")) {
			err_msg = ApplicationManager.getApplicationManager().getTraduction("inputerror") + err_msg;
			Object[] options = {"Ok"};
			JOptionPane.showOptionDialog(this, err_msg, ApplicationManager.getApplicationManager().getTraduction("inputerror2"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
		}
		else {
			try {
				
				EncryptedProperties.createPropertiesFile(
						this.txt_ontology_name.getText(), 
						Service.class.getCanonicalName(), 
						this.txt_storage_repository.getText(), 
						this.txt_user_login.getText(), 
						String.valueOf(this.txt_user_password.getPassword()));
				this.valid = true;
				this.setVisible(false);	
			} catch (InvalidKeyException e) {
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("invalidkey") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("invalidalgorithm") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("nonexistentpadding") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("invalidsize") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (BadPaddingException e) {
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("invalidpadding") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (IOException e) {
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("filecreationerror") + " ./" + Constants.DEFAULT_ONTOLOGIES_PATH  + this.txt_ontology_name.getText() + ".properties", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (InvalidKeySpecException e)
			{
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("invalidspecificationkey") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e)
			{
				Object[] options = {"Ok"};
				JOptionPane.showOptionDialog(this, ApplicationManager.getApplicationManager().getTraduction("pwdencodingerror") + ApplicationManager.getApplicationManager().getTraduction("invalidalgorithm") + ". ", ApplicationManager.getApplicationManager().getTraduction("savingerror"), JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
				e.printStackTrace();
			}
		}
	}

   private void cancelInput()
   {
	   this.setVisible(false);
   }
   
   private class StorageCell {
	   private String name = "";
	   private String driver = "";
	   
	   /**
	    * @param name The displayable name of this cell. 
	    * @param driver The Java class name of the <code>IService</code> to use for this kind of storage.
	    */
	   public StorageCell(String name, String driver) {
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
		return this.valid;
	}
	
	/**
	 * @return The typed name of the new <code>Ontology</code> if the input has been validated. 
	 */
	public String getOntologyname() {
		return this.validInput() ? this.txt_ontology_name.getText() : null;
	}
}