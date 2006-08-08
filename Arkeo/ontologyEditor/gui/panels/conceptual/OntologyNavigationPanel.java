/**
 * Created on 16 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels.conceptual;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.model.tableModel.ChildConceptTableModel;
import ontologyEditor.gui.model.tableModel.DefinedConceptTableModel;
import ontologyEditor.gui.model.tableModel.EditorTableModel;
import ontologyEditor.gui.model.tableModel.LemmasToConceptTableModel;
import ontologyEditor.gui.panels.AbstractNavigationPanel;
import ontologyEditor.gui.panels.linguistic.LinguisticNavigationPanel;
import ontologyEditor.gui.transfers.LinkableElementDragTransferHandler;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * 
 * @author Sanmartin
 * 
 * classe relative au panneau de navigation de l'ontologie. Associé à un concept
 */
public class OntologyNavigationPanel extends AbstractNavigationPanel
{
	private JTable conceptFilsTable;

	private JLabel labelPere;
	
	private JLabel conceptPere;

	private JTable lemmeAssocieTable;
	
	private JTable conceptDefiniTable;

	private LinkableElement currentElement;
	
	private JButton suivantButton;
	
	private JButton precedentButton;
	
	private ArrayList<LinkableElement> suivant;
	
	private ArrayList<LinkableElement> precedent;
	
	/**
	 * 
	 */
	public OntologyNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 10;
		double size[][] = { { border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.FILL, border }, // Columns
				{ border,20,border, TableLayout.FILL, border,20,border } }; // Rows
		this.setLayout(new TableLayout(size));
		
		this.suivant=new ArrayList<LinkableElement>();
		this.precedent=new ArrayList<LinkableElement>();
		
		// affichage du pere
		this.labelPere=new JLabel(ApplicationManager.getApplicationManager().getTraduction("conceptfather"));
		this.conceptPere=new JLabel();
		conceptPere.setFont(new Font("Serif", Font.PLAIN, 16));
		
		// table représentant les concepts fils
		String[] titreF={ApplicationManager.getApplicationManager().getTraduction("child")};
		// création du model de la table
		ChildConceptTableModel tableCFModel = new ChildConceptTableModel();
		// ajout du titre
		tableCFModel.setColumnNames(titreF);
		// création de la jtable contenant le titre
		this.conceptFilsTable = new JTable(null,titreF);
		// attachement du model a la table pour les données
		this.conceptFilsTable.setModel(tableCFModel);
		// attachement du renderer pour l'apparence
		//this.conceptFilsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		// cette table pourra utiliser le drag and drop
		this.conceptFilsTable.setTransferHandler(new LinkableElementDragTransferHandler());
		// au depard à false
		this.conceptFilsTable.setDragEnabled(false);
		// on peut selectionner qu'un concept à la fois
		this.conceptFilsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		// quand on dragge
		this.conceptFilsTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				// si le bouton "Modifier" du panneau d'edition est sélectionné
				if (DisplayManager.editionState)
				{
					// on créer l'instance de l'objet transferable
					TransferHandler handler = OntologyNavigationPanel.this.conceptFilsTable.getTransferHandler();
					// transfert par copie
					handler.exportAsDrag(OntologyNavigationPanel.this.conceptFilsTable, e, TransferHandler.COPY);
				}
			}
		});
		
		
		this.precedentButton = new JButton(ApplicationManager.getApplicationManager().getTraduction("retour"));
		this.precedentButton.setIcon(new ImageIcon(Constants.DEFAULT_ICONS_PATH+"previous.gif"));
		
		this.precedentButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				suivant.add(0,currentElement);
				precedent.remove(currentElement);
				// remplisssage de navigation panel
				if (precedent.size()!=0)
				{
					remplirLabelPere(precedent.get(precedent.size()-1));
					remplirTableDefini(precedent.get(precedent.size()-1));
					remplirTableFils(precedent.get(precedent.size()-1));
					remplirTableLemme(precedent.get(precedent.size()-1));
				}
			}
		});
		
		this.suivantButton = new JButton(ApplicationManager.getApplicationManager().getTraduction("suivant"));
		this.suivantButton.setIcon(new ImageIcon(Constants.DEFAULT_ICONS_PATH+"next.gif"));
		this.suivantButton.setHorizontalTextPosition(SwingConstants.LEFT);
		
		this.suivantButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				precedent.add(currentElement);
				suivant.remove(currentElement);
				// remplisssage de navigation panel
				if (suivant.size()!=0)
				{
					remplirLabelPere(suivant.get(0));
					remplirTableDefini(suivant.get(0));
					remplirTableFils(suivant.get(0));
					remplirTableLemme(suivant.get(0));
				}
			}
		});
		
		
		String[] titreL={ApplicationManager.getApplicationManager().getTraduction("assiociatedterm")};
		LemmasToConceptTableModel tableCLModel = new LemmasToConceptTableModel();
		tableCLModel.setColumnNames(titreL);
		this.lemmeAssocieTable = new JTable(tableCLModel);
		//this.lemmeAssocieTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.lemmeAssocieTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.lemmeAssocieTable.setDragEnabled(DisplayManager.editionState);
		this.lemmeAssocieTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// quand on clique sur un lemme et que une vue par lemme est active alors le lemme courant de la vue lemme
		// passe au lemme sur lequel on vient de' cliquer
		this.lemmeAssocieTable.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 0));
				lemmeAssocieTable.setToolTipText(currentElement.getLinks(element).get(0).getName()+" "+currentElement.getName());
				if (e.getClickCount() >= 2)
				{
					int panel=-1;
					if ((DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getNavigationPanel() instanceof LinguisticNavigationPanel) && (e.getComponent().getParent().getParent().getParent().getParent().getParent().getY()==1))
					{
						panel=DisplayManager.mainFrame.BOTTOM_PANEL;
					}
					else if ((DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getNavigationPanel() instanceof LinguisticNavigationPanel) && (e.getComponent().getParent().getParent().getParent().getParent().getParent().getY()!=1))
					{
						panel=DisplayManager.mainFrame.TOP_PANEL;
					}
					if (panel!=-1)
					{
						// selection de la ligne correpondant au lemme selectionné
						for (int i=0;i<DisplayManager.mainFrame.getPanel(panel).getTable().getRowCount();i++)
						{
							if (DisplayManager.mainFrame.getPanel(panel).getTable().getValueAt(i,0).toString().equals(element.toString()))
							{
								DisplayManager.mainFrame.getPanel(panel).getTable().setRowSelectionInterval(i,i);
								
							}
						}
						// remplisssage de navigation panel
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableLemmeParent(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableLemmeLier(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableConcept(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableDocumentParent(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).getPrecedent().add(element);
					}
				}
			}
		});
		
		this.lemmeAssocieTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if (DisplayManager.editionState)
				{
					TransferHandler handler = OntologyNavigationPanel.this.lemmeAssocieTable.getTransferHandler();
					handler.exportAsDrag(OntologyNavigationPanel.this.lemmeAssocieTable, e, TransferHandler.COPY);
				}
			}
		});
		
		String[] titreD={ApplicationManager.getApplicationManager().getTraduction("relation"),ApplicationManager.getApplicationManager().getTraduction("connectedconcepts")};
		DefinedConceptTableModel tableCDModel = new DefinedConceptTableModel();
		tableCDModel.setColumnNames(titreD);
		this.conceptDefiniTable = new JTable(tableCDModel);
		//this.conceptDefiniTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.conceptDefiniTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.conceptDefiniTable.setDragEnabled(DisplayManager.editionState);
		this.conceptDefiniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.conceptDefiniTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if (DisplayManager.editionState)
				{
					TransferHandler handler = OntologyNavigationPanel.this.conceptDefiniTable.getTransferHandler();
					handler.exportAsDrag(OntologyNavigationPanel.this.conceptDefiniTable, e, TransferHandler.COPY);
				}
			}
		});

		// on associe a chaque table un JScrollPane avec un titre
		JScrollPane filsScrollPane = new JScrollPane();
		filsScrollPane.setViewportView(this.conceptFilsTable);
		filsScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("child")));
		JScrollPane lemmeScrollPane = new JScrollPane();
		lemmeScrollPane.setViewportView(this.lemmeAssocieTable);
		lemmeScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("associatedterms")));
		JScrollPane definiScrollPane = new JScrollPane();
		definiScrollPane.setViewportView(this.conceptDefiniTable);
		definiScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("definiteconcepts")));
		
		//mise en place des renderer
		this.rendererTableConcept(this.conceptDefiniTable);
		this.rendererTableLemme(this.lemmeAssocieTable);
		this.rendererTableConcept(this.conceptFilsTable);
		
		// defini la taille des colonnes
		TableColumn columncon1 = conceptDefiniTable.getColumnModel().getColumn(0);
		columncon1.setPreferredWidth(80);
		TableColumn columncon2 = conceptDefiniTable.getColumnModel().getColumn(1);
		columncon2.setPreferredWidth(150);
		
		// mise en place des composant dasn le panel
		this.add(labelPere, "1, 1, 1, 1");
		this.add(conceptPere,"3, 1, 1, 1");
		this.add(filsScrollPane, "1, 3, 1, 1");
		this.add(lemmeScrollPane, "3, 3, 1, 1");
		this.add(definiScrollPane, "5, 3, 1, 1");
		this.add(this.suivantButton, "5, 5, 1, 1");
		this.add(this.precedentButton, "1, 5, 1, 1");
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("navigationpanel")));

	}

	/**
	 * Update navigation on first navigation table (centralTable)
	 * 
	 * @param element
	 *            the element to set on the first navigation table
	 */
	public void rollFirstPanel(LinkableElement element)
	{
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("navigationpanel")+" "+element));
		showParents(element);
	}

	/**
	 * Update navigation on second navigation table (centralTable)
	 * 
	 * @param element
	 *            the element to set on the second navigation table
	 */
	public void rollSecondPanel(LinkableElement element)
	{
		OntologyNavigationPanel.this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("navigationpanel")+" "+element));
	}

	public void showParents(LinkableElement element)
	{
		
	}

	/**
	 * Update display on components impacted by navigation
	 * 
	 * @param element
	 *            the element to set
	 */
	public void reflectNavigation(LinkableElement element)
	{

	}

	/**
	 * Change the state of the panel from pure navigation to edition
	 * 
	 * @param state
	 *            true if the panel is in edition state
	 */
	public void changeState(boolean state)
	{
		
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((ChildConceptTableModel)conceptFilsTable.getModel()).setDonnees(null);
		this.conceptPere.setText(null);
		((LemmasToConceptTableModel)lemmeAssocieTable.getModel()).setDonnees(null);
		((DefinedConceptTableModel)conceptDefiniTable.getModel()).setDonnees(null);
		this.updateUI();
	}

	/**
	 * @return Returns the curentElement.
	 */
	public LinkableElement getCurrentElement()
	{
		return this.currentElement;
	}

	public void elementRemoved(LinkableElement element)
	{
	
	}
	
	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#relationChanged(arkeotek.ontology.LinkableElement, arkeotek.ontology.LinkableElement)
	 */
	@Override
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
				
	}

	@Override
	public void reload()
	{
	
	}

	public void refreshNavigation(LinkableElement element)
	{
		
	}

	// des qu'un concept est selectionné dans l'arbre à gauche on rempli tout les champ et tables de ce panneau
	public void remplirLabelPere(LinkableElement concept) {
		// TODO Auto-generated method stub
		conceptPere.setText(null);
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		elements.addAll(ApplicationManager.ontology.getParentsOf(concept, Concept.KEY));
		if (elements.size()!=0)
		{
			conceptPere.setText(((LinkableElement)elements.get(0)[1]).getName());
		}
		this.updateUI();
	}

	// focntion permettant de remplir la table des concept fils par rapport au concept courant
	public void remplirTableFils(LinkableElement concept) {
		// TODO Auto-generated method stub
		this.currentElement=concept;
		conceptFilsTable.removeAll();
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("navigationpanel")+" "+concept));
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (concept != null)
		{
			if (concept instanceof Ontology)
			{
				for (LinkableElement concept2 : ((Ontology)concept).get(Concept.KEY))
				{
					Object[] couple = {"", concept2};
					elements.add(couple);
				}
			}
			else
			{
				Set<Relation> keys = null;
				if (concept.getLinks(Concept.KEY) != null)
				{
					keys = concept.getLinks(Concept.KEY).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement elem : concept.getLinks(Concept.KEY, key))
						{
							if (key.getName().equals("généralise"))
							{
								Object[] couple = {key, elem};
								elements.add(couple);
							}
						}
					}
				}	
			}
		}
		
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][1];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=((LinkableElement)elements.get(i)[1]);
			}
			((ChildConceptTableModel)conceptFilsTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][1];
			((ChildConceptTableModel)conceptFilsTable.getModel()).setDonnees(donnees);
		}
		lemmeAssocieTable.updateUI();
		conceptFilsTable.updateUI();
		conceptDefiniTable.updateUI();
		this.updateUI();
		
	}

	// focntion permettant de remplir la table des lemmes par rapport au concept courant
	public void remplirTableLemme(LinkableElement concept) {
		// TODO Auto-generated method stub
		lemmeAssocieTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		Set<Relation> keys = null;
		if (concept.getLinks(Lemma.KEY) != null)
		{
			keys = concept.getLinks(Lemma.KEY).keySet();
			for (Relation key : keys)
			{
				for (LinkableElement elem : concept.getLinks(Lemma.KEY, key))
				{
					Object[] couple = {key, elem};
					elements.add(couple);
				}
			}
		}
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][1];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=((LinkableElement)elements.get(i)[1]);
			}
			((LemmasToConceptTableModel)lemmeAssocieTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][1];
			((LemmasToConceptTableModel)lemmeAssocieTable.getModel()).setDonnees(donnees);
		}
		
		this.updateUI();
	}

	// fonction permettant de remplir la table des concepts définit par rapport au concept courant
	public void remplirTableDefini(LinkableElement concept) {
		// TODO Auto-generated method stub
		conceptDefiniTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (concept != null)
		{
			Set<Relation> keys = null;
			if (concept.getLinks(Concept.KEY) != null)
			{
				keys = concept.getLinks(Concept.KEY).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement elem : concept.getLinks(Concept.KEY, key))
					{
						if (!key.getName().equals("généralise"))
						{
							Object[] couple = {key, elem};
							elements.add(couple);
						}
					}
				}
			}	
			
		}
		
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=((LinkableElement)elements.get(i)[0]);
				donnees[i][1]=((LinkableElement)elements.get(i)[1]);
			}
			((DefinedConceptTableModel)conceptDefiniTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((DefinedConceptTableModel)conceptDefiniTable.getModel()).setDonnees(donnees);
		}
		
		lemmeAssocieTable.updateUI();
		conceptFilsTable.updateUI();
		conceptDefiniTable.updateUI();
		this.updateUI();
		
	}
	
	
	// permet d'afficher les images associé au concepts
	private void rendererTableConcept(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_CONCEPT_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
	}
	
	// permet d'afficher les images associé au lemme
	private void rendererTableLemme(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_LEMMA_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
	}

	@Override
	protected void doNavigationKeyPressed(KeyEvent evt) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<LinkableElement> getPrecedent() {
		return precedent;
	}

	public void setPrecedent(ArrayList<LinkableElement> precedent) {
		this.precedent = precedent;
	}

	public ArrayList<LinkableElement> getSuivant() {
		return suivant;
	}

	public void setSuivant(ArrayList<LinkableElement> suivant) {
		this.suivant = suivant;
	}

}
