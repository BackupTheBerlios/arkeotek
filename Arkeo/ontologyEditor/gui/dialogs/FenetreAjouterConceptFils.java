package ontologyEditor.gui.dialogs;

import info.clearthought.layout.TableLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

public class FenetreAjouterConceptFils extends JDialog implements ActionListener {

	private JFrame frame;
	   
	private JLabel lbl_contain;
	private JTextField txt_contain;
   
	private static final String SEARCH_CAPTION = "Ajouter";
	private static final String CANCEL_CAPTION = "Annuler";

	private LinkableElement conceptSource;

	private JButton search_button = new JButton(SEARCH_CAPTION);
	private JButton cancel_button = new JButton(CANCEL_CAPTION);
	
	private int panel;


	public FenetreAjouterConceptFils(LinkableElement concept, int panel)
	{
		this(DisplayManager.mainFrame,concept,panel);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private FenetreAjouterConceptFils(JFrame frame,LinkableElement concept,int panel)
	{
        super(frame, "Ajouter un fils au concept", true);
        this.panel=panel;
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
		this.lbl_contain = new JLabel("Nom du concept :");
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
				FenetreAjouterConceptFils.this.validateInput();
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
				FenetreAjouterConceptFils.this.cancelInput();
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
			Concept fils=new Concept(this.txt_contain.getText());
			//on créer une nouvelle relation
			try {
				ApplicationManager.ontology.get(Concept.KEY).add(fils);
				ArrayList<LinkableElement> relations = ApplicationManager.ontology.get(Relation.KEY);
				Relation rel=new Relation(Relation.DEFAULT_CONCEPTS_RELATION);
				for (int i=0;i<relations.size();i++)
				{
					if (rel.getName().equals(((Relation)relations.get(i)).getName()))
					{
						rel.setId(((Relation)relations.get(i)).getId());
					}
				}
				ApplicationManager.ontology.addRelation(conceptSource,fils,rel);
				DefaultMutableTreeNode courant=(DefaultMutableTreeNode)DisplayManager.mainFrame.getPanel(panel).getTree().getLastSelectedPathComponent();
				courant.add(new DefaultMutableTreeNode(fils));
				DisplayManager.mainFrame.getPanel(panel).getTree().expandPath(DisplayManager.mainFrame.getPanel(panel).getTree().getSelectionPath());
				DisplayManager.mainFrame.getPanel(panel).getTree().updateUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// on met a jour l'interface
			//DisplayManager.mainFrame.refresh();
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
