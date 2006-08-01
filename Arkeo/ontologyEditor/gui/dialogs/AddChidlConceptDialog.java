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

/**
 * @author sanmartin
 * fenetre relative à l'ajout d'un concept fils au concept courant
 */
public class AddChidlConceptDialog extends JDialog implements ActionListener {

	private JFrame frame;
	   
	private JLabel lbl_contain;
	private JTextField txt_contain;
   
	private static final String SEARCH_CAPTION = "Ajouter";
	private static final String CANCEL_CAPTION = "Annuler";

	// concept pere
	private LinkableElement conceptSource;

	private JButton search_button = new JButton(SEARCH_CAPTION);
	private JButton cancel_button = new JButton(CANCEL_CAPTION);
	
	private int panel;


	public AddChidlConceptDialog(LinkableElement concept, int panel)
	{
		this(DisplayManager.mainFrame,concept,panel);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private AddChidlConceptDialog(JFrame frame,LinkableElement concept,int panel)
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
				// quand on clique sur Ajouter
				AddChidlConceptDialog.this.validateInput();
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
				AddChidlConceptDialog.this.cancelInput();
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
			//création d'une nouvelle relation
			try {
				// ajoute le fils dans la liste des concept
				ApplicationManager.ontology.get(Concept.KEY).add(fils);
				// création d'une nouvelle relation DEFAULT_CONCEPTS_RELATION entre le pere et le fils
				ArrayList<LinkableElement> relations = ApplicationManager.ontology.get(Relation.KEY);
				Relation rel=new Relation(Relation.DEFAULT_CONCEPTS_RELATION);
				// on recherche l'identifiant de cette relation
				for (int i=0;i<relations.size();i++)
				{
					if (rel.getName().equals(((Relation)relations.get(i)).getName()))
					{
						rel.setId(((Relation)relations.get(i)).getId());
					}
				}
				// on ajoute la relation
				ApplicationManager.ontology.addRelation(conceptSource,fils,rel);
				// on ajoute le concept à l'arbre
				DefaultMutableTreeNode courant=(DefaultMutableTreeNode)DisplayManager.mainFrame.getPanel(panel).getTree().getLastSelectedPathComponent();
				courant.add(new DefaultMutableTreeNode(fils));
				// MAJ de l'interface
				DisplayManager.mainFrame.getPanel(panel).getTree().expandPath(DisplayManager.mainFrame.getPanel(panel).getTree().getSelectionPath());
				DisplayManager.mainFrame.getPanel(panel).getTree().updateUI();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
