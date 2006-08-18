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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.panels.conceptual.OntologyNavigationPanel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author sanmartin
 * fenetre relative à l'ajout d'un concept fils au concept courant
 */
public class SearchConceptDialog extends JDialog implements ActionListener {

	private JFrame frame;
	   
	private JLabel lbl_contain;
	private JTextField txt_contain;
   
	private static final String SEARCH_CAPTION = "Rechercher"/*ApplicationManager.getApplicationManager().getTraduction("add")*/;
	private static final String CANCEL_CAPTION = ApplicationManager.getApplicationManager().getTraduction("cancel");

	// concept pere
	private LinkableElement conceptSource;

	private JButton search_button = new JButton(SEARCH_CAPTION);
	private JButton cancel_button = new JButton(CANCEL_CAPTION);
	
	private int panel;


	public SearchConceptDialog(int panel)
	{
		this(DisplayManager.mainFrame,panel);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private SearchConceptDialog(JFrame frame,int panel)
	{
        super(frame, ApplicationManager.getApplicationManager().getTraduction("addachildtoconcept"), true);
        this.panel=panel;
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
		this.lbl_contain = new JLabel(ApplicationManager.getApplicationManager().getTraduction("nameoftheconcept"));
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
				SearchConceptDialog.this.validateInput();
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
				SearchConceptDialog.this.cancelInput();
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
			err_msg += ApplicationManager.getApplicationManager().getTraduction("nameoftheconceptnotspecified");
			this.dispose();
		}
		else
		{
			ArrayList<LinkableElement> concepts=ApplicationManager.ontology.get(Concept.KEY);
			ArrayList<LinkableElement> equivalents=new ArrayList<LinkableElement>();
			for (LinkableElement con:concepts)
			{
				if (con.toString().contains(this.txt_contain.getText()))
				{
					equivalents.add(con);
				}
			}
			if (equivalents.size()!=0)
			{
				// on tranforme l'arrayList en Object []
				Object[] conc=equivalents.toArray();
				LinkableElement concept = (LinkableElement)JOptionPane.showInputDialog(DisplayManager.mainFrame, 
						/*ApplicationManager.getApplicationManager().getTraduction("enternamerelation")*/"concept" + " : ", /*ApplicationManager.getApplicationManager().getTraduction("creationlink")*/"recherche concept", JOptionPane.INFORMATION_MESSAGE,null,
						conc, conc[0]);
				if (concept !=null)
				{
					((OntologyNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirLabelPere(concept);
					((OntologyNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableFils(concept);
					((OntologyNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableLemme(concept);
					((OntologyNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableDefini(concept);
					((OntologyNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).getPrecedent().add(concept);
					DisplayManager.getInstance().reflectNavigation(concept);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Aucun concept approchant "+this.txt_contain.getText()+" n'a été trouvé");
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
