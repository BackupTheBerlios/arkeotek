/**
 * Created on 16 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels.linguistic;

import info.clearthought.layout.TableLayout;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.model.tableModel.DocumentPartsToLemmaTableModel;
import ontologyEditor.gui.model.tableModel.LemmaParentTableModel;
import ontologyEditor.gui.model.tableModel.LemmasToConceptTableModel;
import ontologyEditor.gui.model.tableModel.LemmasToDocumentPartTableModel;
import ontologyEditor.gui.panels.AbstractNavigationPanel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class LinguisticNavigationPanel extends AbstractNavigationPanel
{
	//private JTable appearancesTable;
	
	//private JButton voirDoc;
	
	private JTable linkedLemmasTable;
	
	private JTable lemmasParentsTable;
	
	private JTable concept;
	
	private JTable document;
	
	private JButton validationButton;
	
	private JButton suivantButton;
	
	private JButton precedentButton;
	
	private LinkableElement currentElement;
	
	private ArrayList<LinkableElement> suivant;
	
	private ArrayList<LinkableElement> precedent;
	
	/**
	 * 
	 */
	public LinguisticNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 10;
		double sizeNavPanel[][] = { { border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.FILL, border }, // Columns
				{ border, TableLayout.FILL, border, TableLayout.FILL,border, 20, border } }; // Rows
		this.setLayout(new TableLayout(sizeNavPanel));

		this.suivant=new ArrayList<LinkableElement>();
		this.precedent=new ArrayList<LinkableElement>();
		
		String[] titreLier={ApplicationManager.getApplicationManager().getTraduction("relation"),ApplicationManager.getApplicationManager().getTraduction("element")};
		LemmasToDocumentPartTableModel tableLLModel = new LemmasToDocumentPartTableModel();
		tableLLModel.setColumnNames(titreLier);
		this.linkedLemmasTable = new JTable(tableLLModel);
		//this.linkedLemmasTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.linkedLemmasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		JScrollPane linkedLemmasScrollPane = new JScrollPane();
		linkedLemmasScrollPane.setViewportView(this.linkedLemmasTable);
		linkedLemmasScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("dependentlemmas")));

		String[] titreParent={ApplicationManager.getApplicationManager().getTraduction("relation"),ApplicationManager.getApplicationManager().getTraduction("element")};
		LemmaParentTableModel tableParentModel = new LemmaParentTableModel();
		tableParentModel.setColumnNames(titreParent);
		this.lemmasParentsTable = new JTable(tableParentModel);
		//this.lemmasParentsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.lemmasParentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane lemmasParentsScrollPane = new JScrollPane();
		lemmasParentsScrollPane.setViewportView(this.lemmasParentsTable);
		lemmasParentsScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("dependentparentslemmas")));
		
		LemmasToConceptTableModel tableConceptModel = new LemmasToConceptTableModel();
		tableConceptModel.setColumnNames(titreParent);
		this.concept = new JTable(tableConceptModel);
		//this.concept.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.concept.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane conceptScrollPane = new JScrollPane();
		conceptScrollPane.setViewportView(this.concept);
		conceptScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("dependentconcepts")));
		
		String[] titre={ApplicationManager.getApplicationManager().getTraduction("relation"),ApplicationManager.getApplicationManager().getTraduction("id"),ApplicationManager.getApplicationManager().getTraduction("seq")};
		DocumentPartsToLemmaTableModel tableModel = new DocumentPartsToLemmaTableModel();
		tableModel.setColumnNames(titre);
		this.document=new JTable();
		this.document.setModel(tableModel);
		TableColumn columndoc1 = document.getColumnModel().getColumn(0);
		columndoc1.setPreferredWidth(70);
		TableColumn columndoc2 = document.getColumnModel().getColumn(1);
		columndoc2.setPreferredWidth(300);
		TableColumn columndoc3 = document.getColumnModel().getColumn(2);
		columndoc3.setPreferredWidth(500);
		
		TableColumn columnlem1 = lemmasParentsTable.getColumnModel().getColumn(0);
		columnlem1.setPreferredWidth(80);
		TableColumn columnlem2 = lemmasParentsTable.getColumnModel().getColumn(1);
		columnlem2.setPreferredWidth(150);
		
		TableColumn columnlin1 = linkedLemmasTable.getColumnModel().getColumn(0);
		columnlin1.setPreferredWidth(80);
		TableColumn columnlin2 = linkedLemmasTable.getColumnModel().getColumn(1);
		columnlin2.setPreferredWidth(150);
		
		TableColumn columncon1 = concept.getColumnModel().getColumn(0);
		columncon1.setPreferredWidth(80);
		TableColumn columncon2 = concept.getColumnModel().getColumn(1);
		columncon2.setPreferredWidth(150);

		
		JScrollPane documentScrollPane = new JScrollPane();
		documentScrollPane.setViewportView(this.document);
		documentScrollPane.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("dependentdocuments")));
		
		this.validationButton = new JButton(ApplicationManager.getApplicationManager().getTraduction("validate"));
		this.validationButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (currentElement!=null)
				{
					
					if (currentElement.getState() == LinkableElement.VALIDATED)
					{
						ApplicationManager.ontology.getLemmeValider().remove(currentElement);
					}
					else
					{
						ApplicationManager.ontology.getLemmeValider().add(currentElement);
						//DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().setValueAt(DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getDefaultRenderer(LinkableElement.class).getTableCellRendererComponent(DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable(),currentElement,true,true,DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getSelectedRow(),DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getSelectedColumn()),DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getSelectedRow(),0);
					}
					LinguisticNavigationPanel.this.validationButton.setText((currentElement.getState() == LinkableElement.VALIDATED)?"Valider":"Invalider");
					currentElement.setState((currentElement.getState() == LinkableElement.VALIDATED)?LinkableElement.DEFAULT:LinkableElement.VALIDATED);
					if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable()!=null)
						DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().updateUI();
					if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTable()!=null)
						DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTable().updateUI();
				
				}
			}
		});
		
		this.precedentButton = new JButton(ApplicationManager.getApplicationManager().getTraduction("retour"));
		this.precedentButton.setIcon(new ImageIcon(Constants.DEFAULT_ICONS_PATH+"previous.gif"));
		/*if (precedent.size()!=0)
		{
			precedentButton.setToolTipText(precedent.get(precedent.size()-1).toString());
		}
		else
		{
			precedentButton.setToolTipText("Aucun précédent");
		}*/
		
		this.precedentButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				suivant.add(0,currentElement);
				precedent.remove(currentElement);
				// remplisssage de navigation panel
				if (precedent.size()!=0)
				{
					remplirTableLemmeParent(precedent.get(precedent.size()-1));
					remplirTableLemmeLier(precedent.get(precedent.size()-1));
					remplirTableConcept(precedent.get(precedent.size()-1));
					remplirTableDocumentParent(precedent.get(precedent.size()-1));
				}
			}
		});
		
		this.suivantButton = new JButton(ApplicationManager.getApplicationManager().getTraduction("suivant"));
		this.suivantButton.setIcon(new ImageIcon(Constants.DEFAULT_ICONS_PATH+"next.gif"));
		this.suivantButton.setHorizontalTextPosition(SwingConstants.LEFT);
		
		/*if (suivant.size()!=0)
		{
			suivantButton.setToolTipText(suivant.get(0).toString());
		}
		else
		{
			suivantButton.setToolTipText("Aucun suivant");
		}*/
		this.suivantButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				precedent.add(currentElement);
				suivant.remove(currentElement);
				// remplisssage de navigation panel
				if (suivant.size()!=0)
				{
					remplirTableLemmeParent(suivant.get(0));
					remplirTableLemmeLier(suivant.get(0));
					remplirTableConcept(suivant.get(0));
					remplirTableDocumentParent(suivant.get(0));
				}
			}
		});
		
		
		
		//this.voirDoc = new JButton("Documents associés");
		/*if (ApplicationManager.ontology!=null)
		{
			final LinkableElement elem=;
			this.voirDoc.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e)
				{
					DocumentPartToLemme fen = new DocumentPartToLemme(DisplayManager.mainFrame,elem);
					System.out.println("LEMME : "+elem);
					fen.setVisible(true);
				}
			});
		}*/
		
		// mise en place des renderer
		this.rendererTableConcept(this.concept);
		this.rendererTableLemme(this.lemmasParentsTable);
		this.rendererTableLemme(this.linkedLemmasTable);
		this.rendererTableDocument(this.document);
		
		// mise en place des composant dasn le panel
		this.add(linkedLemmasScrollPane, "1, 1, 1, 1");
		this.add(lemmasParentsScrollPane, "3, 1, 1, 1");
		this.add(conceptScrollPane, "5, 1, 1, 1");
		this.add(documentScrollPane, "1, 3, 5, 3");
		this.add(this.validationButton, "3, 5, 1, 1");
		this.add(this.suivantButton, "5, 5, 1, 1");
		this.add(this.precedentButton, "1, 5, 1, 1");
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("navigationpanel")));
}

	/**
	 * Updates navigation. 
	 * 
	 * @param element the element to set on the first navigation table
	 */
	public void fillTable(LinkableElement element)
	{
		//((EditorTableModel) (this.appearancesTable.getModel())).setElement(element);
		final LinkableElement elem=element;
		/*this.voirDoc.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				DocumentPartToLemme fen = new DocumentPartToLemme(DisplayManager.mainFrame,elem);
				fen.setVisible(true);
			}
		});*/
		//((EditorTableModel) (this.linkedLemmasTable.getModel())).setElement(element);
		//((EditorTableModel) (this.lemmasParentsTable.getModel())).setElement(element);
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("navigationpanel")+" "+element));
		this.validationButton.setText((element.getState() == LinkableElement.VALIDATED)?ApplicationManager.getApplicationManager().getTraduction("unvalidate"):ApplicationManager.getApplicationManager().getTraduction("validate"));
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((LemmasToConceptTableModel) this.concept.getModel()).setDonnees(null);
		((LemmasToDocumentPartTableModel) (this.linkedLemmasTable.getModel())).setDonnees(null);
		((LemmaParentTableModel) (this.lemmasParentsTable.getModel())).setDonnees(null);
		((DocumentPartsToLemmaTableModel) (this.document.getModel())).setDonnees(null);
		this.validationButton.setText(ApplicationManager.getApplicationManager().getTraduction("validate"));
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("lemmanavpanel")+" : "));
		this.updateUI();
	}
	

	// rempli la table des lemmes parent du lemme courant
	public void remplirTableLemmeParent(LinkableElement lemme)
	{
		this.currentElement=lemme;
		this.validationButton.setText((lemme.getState() == LinkableElement.VALIDATED)?ApplicationManager.getApplicationManager().getTraduction("unvalidate"):ApplicationManager.getApplicationManager().getTraduction("validate"));
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("lemmanavpanel")+" : "+lemme.getName()));
		lemmasParentsTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		elements=ApplicationManager.ontology.getLemmasParents(lemme);
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			((LemmaParentTableModel)lemmasParentsTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((LemmaParentTableModel)lemmasParentsTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
	}
	
	// rempli la table des lemmes lié du lemme courant
	public void remplirTableLemmeLier(LinkableElement lemme)
	{
		linkedLemmasTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		ArrayList<Integer> links_categories = new ArrayList<Integer>(1);
		links_categories.add(Lemma.KEY);
		for (Integer category : links_categories) {
			if (lemme.getLinks(category.intValue()) != null) {
				Set<Relation> keys = lemme.getLinks(category.intValue()).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement temp : lemme.getLinks(category.intValue(), key))
					{
						Object[] triple = {key, temp};
						elements.add(triple);
					}
				}
			}
		}
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			((LemmasToDocumentPartTableModel)linkedLemmasTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((LemmasToDocumentPartTableModel)linkedLemmasTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
		final LinkableElement elem=lemme;
	}

	public void remplirTableConcept(LinkableElement lemme) {
		// TODO Auto-generated method stub
		concept.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();

		
		HashMap<Relation,HashMap<LinkableElement,Link>> conc=lemme.getLinks(Concept.KEY);
		for (Relation rel:conc.keySet())
		{
			HashMap<LinkableElement,Link>hm=conc.get(rel);
			if (!hm.isEmpty())
			{
				for (LinkableElement con:hm.keySet())
				{
					Object[] triple = {rel, con};
					elements.add(triple);
				}
			}
		}
		
		
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			((LemmasToConceptTableModel)concept.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((LemmasToConceptTableModel)concept.getModel()).setDonnees(donnees);
		}
		this.updateUI();
	}
	
	private ArrayList<Object[]> getElementsFromLinkableElement(LinkableElement element)
	{
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element != null)
		{
			Set<Relation> keys = null;
			if (element.getLinks(DocumentPart.KEY) != null)
			{
				keys = element.getLinks(DocumentPart.KEY).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement elem : element.getLinks(DocumentPart.KEY, key))
					{
						Object[] line = {key, ((DocumentPart)elem).getName(), ((DocumentPart) elem).getValue()};
						elements.add(line);
					}
				}
			}
		}
		return elements;
	}
	
	public void remplirTableDocumentParent(LinkableElement lemme)
	{
		document.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		elements=getElementsFromLinkableElement(lemme);
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][3];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
				donnees[i][2]=(elements.get(i)[2]);
			}
			((DocumentPartsToLemmaTableModel)document.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][3];
			((DocumentPartsToLemmaTableModel)document.getModel()).setDonnees(donnees);
		}
		this.document.updateUI();
	}

	public LinkableElement getCurrentElement() {
		return currentElement;
	}

	public void setCurrentElement(LinkableElement currentElement) {
		this.currentElement = currentElement;
	}
	
	private void rendererTableConcept(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_CONCEPT_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(1).setCellRenderer(custom);
	}
	
	private void rendererTableLemme(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_LEMMA_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(1).setCellRenderer(custom);
	}
	
	private void rendererTableDocument(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_DOCUMENTPART_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(1).setCellRenderer(custom);
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

	@Override
	public void reflectNavigation(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doNavigationKeyPressed(KeyEvent evt) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeState(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementRemoved(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshNavigation(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void relationChanged(LinkableElement source, LinkableElement target) {
		// TODO Auto-generated method stub
		
	}
}
