package ontologyEditor.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ontologyEditor.DisplayManager;
import arkeotek.ontology.LinkableElement;

/**
 * @author sanmartin
 * renomme le nom d'un concept
 */
public class RenameConceptDialog extends JDialog implements ActionListener {

	private JFrame frame;
	   
	private JLabel lbl_contain;
	private JTextField txt_contain;
   
	private static final String SEARCH_CAPTION = "Renommer";
	private static final String CANCEL_CAPTION = "Annuler";

	private LinkableElement conceptSource;

	private boolean valid = false;
	
	private JButton search_button = new JButton(SEARCH_CAPTION);
	private JButton cancel_button = new JButton(CANCEL_CAPTION);


	public RenameConceptDialog(LinkableElement concept)
	{
		this(DisplayManager.mainFrame,concept);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private RenameConceptDialog(JFrame frame,LinkableElement concept)
	{
        super(frame, "Renommer le concept", true);
        this.frame = frame;
        this.conceptSource=concept;
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
		this.lbl_contain = new JLabel("Nom du fils :");
        this.add(this.lbl_contain, "1, 1, 1, 1");
		
		this.txt_contain = new JTextField(conceptSource.toString());
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
				RenameConceptDialog.this.validateInput();
			}
		});
		this.add(this.search_button, "1, 3, 1, 3");
		
		
		// Annulation Button Action
		this.cancel_button.addMouseListener(new MouseAdapter()
				{
					/**
					 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
					 */
					@Override
					public void mouseClicked(MouseEvent e)
					{
						RenameConceptDialog.this.cancelInput();
					}
				});
		this.add(this.cancel_button, "3, 3, 3, 3");
		
		this.setResizable(false);
		this.pack();
		this.setLocation((frame.getLocation().x) + (frame.getSize().width/2) - (this.getSize().width/2),(frame.getLocation().y) + (frame.getSize().height/2) - (this.getSize().height/2));
	}

	private void validateInput()
	{
		String err_msg = "";
		if (this.txt_contain.getText().equals(""))
		{
			err_msg += "\t- Nom du concept non spécifié. \n";
			this.dispose();
		}
		else
		{
			try {
				conceptSource.setName(this.txt_contain.getText());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// on met a jour l'interface
			DisplayManager.mainFrame.refresh();
			this.dispose();
		}
	}

	private void cancelInput()
	{
		this.dispose();
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
