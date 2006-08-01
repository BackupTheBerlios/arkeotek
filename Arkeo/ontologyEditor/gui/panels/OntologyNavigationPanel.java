/**
 * Created on 16 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.tables.ConceptDefiniTM;
import ontologyEditor.gui.tables.ConceptFilsTM;
import ontologyEditor.gui.tables.ConceptLemmeTM;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.LinkableElementTable;
import ontologyEditor.gui.transfers.LinkableElementDragTransferHandler;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class OntologyNavigationPanel extends AbstractNavigationPanel
{
	private JTable conceptFilsTable;

	private JLabel labelPere;
	
	private JLabel conceptPere;

	private JTable lemmeAssocieTable;
	
	private JTable conceptDefiniTable;

	private LinkableElement currentElement;
	
	/**
	 * 
	 */
	public OntologyNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 10;
		double size[][] = { { border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.FILL, border }, // Columns
				{ border,20,border, TableLayout.FILL, border } }; // Rows
		this.setLayout(new TableLayout(size));
		
		this.labelPere=new JLabel("Concept Père : ");
		this.conceptPere=new JLabel();
		conceptPere.setFont(new Font("Serif", Font.PLAIN, 16));
		String[] titreF={"Fils"};
		ConceptFilsTM tableCFModel = new ConceptFilsTM();
		tableCFModel.setColumnNames(titreF);
		this.conceptFilsTable = new JTable(null,titreF);
		this.conceptFilsTable.setModel(tableCFModel);
		this.conceptFilsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.conceptFilsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.conceptFilsTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.conceptFilsTable.setDragEnabled(false);
		this.conceptFilsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.conceptFilsTable.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				/*if (e.getClickCount() >= 2)
				{
					OntologyNavigationPanel.this.rollSecondPanel(((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1)));
					showParents(((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1)));
				}*/
				//DisplayManager.getInstance().reflectNavigation(((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1)));
			}
		});		
		this.conceptFilsTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if (DisplayManager.editionState)
				{
					TransferHandler handler = OntologyNavigationPanel.this.conceptFilsTable.getTransferHandler();
					handler.exportAsDrag(OntologyNavigationPanel.this.conceptFilsTable, e, TransferHandler.COPY);
				}
			}
		});
		
		String[] titreL={"Termes Associés"};
		ConceptLemmeTM tableCLModel = new ConceptLemmeTM();
		tableCLModel.setColumnNames(titreL);
		this.lemmeAssocieTable = new JTable(tableCLModel);
		this.lemmeAssocieTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.lemmeAssocieTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.lemmeAssocieTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.lemmeAssocieTable.setDragEnabled(DisplayManager.editionState);
		this.lemmeAssocieTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
				//DisplayManager.getInstance().reflectNavigation(element);
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
		
		String[] titreD={"Relation","Concepts Reliés"};
		ConceptDefiniTM tableCDModel = new ConceptDefiniTM();
		tableCDModel.setColumnNames(titreD);
		this.conceptDefiniTable = new JTable(tableCDModel);
		this.conceptDefiniTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.conceptDefiniTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.conceptDefiniTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.conceptDefiniTable.setDragEnabled(DisplayManager.editionState);
		this.conceptDefiniTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.conceptDefiniTable.addMouseListener(new MouseAdapter()
				{

					public void mouseClicked(MouseEvent e)
					{
						LinkableElement element = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1));
						/*if (e.getClickCount() >= 2)
						{
							OntologyNavigationPanel.this.rollFirstPanel(((SonTableModel) OntologyNavigationPanel.this.lemmeAssocieTable.getModel()).getElement());
							OntologyNavigationPanel.this.rollSecondPanel(element);
							showParents(element);
						}*/
						//DisplayManager.getInstance().reflectNavigation(element);
					}
				});
		
		
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

		
		JScrollPane filsScrollPane = new JScrollPane();
		filsScrollPane.setViewportView(this.conceptFilsTable);
		filsScrollPane.setBorder(BorderFactory.createTitledBorder("Fils"));
		JScrollPane lemmeScrollPane = new JScrollPane();
		lemmeScrollPane.setViewportView(this.lemmeAssocieTable);
		lemmeScrollPane.setBorder(BorderFactory.createTitledBorder("Termes associés"));
		JScrollPane definiScrollPane = new JScrollPane();
		definiScrollPane.setViewportView(this.conceptDefiniTable);
		definiScrollPane.setBorder(BorderFactory.createTitledBorder("Concepts définis"));
		
		//mise en place des renderer
		this.rendererTableConcept(this.conceptDefiniTable);
		this.rendererTableLemme(this.lemmeAssocieTable);
		this.rendererTableConcept(this.conceptFilsTable);
		
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
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation"));

	}

	/**
	 * Update navigation on first navigation table (centralTable)
	 * 
	 * @param element
	 *            the element to set on the first navigation table
	 */
	public void rollFirstPanel(LinkableElement element)
	{
		//((SonTableModel) (this.centralTable.getModel())).setElement(element);
		//((SonTableModel) (this.rightTable.getModel())).setElement(null);
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+element));
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
		//((SonTableModel) (this.rightTable.getModel())).setElement(element);
		OntologyNavigationPanel.this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+element));
	}

	public void showParents(LinkableElement element)
	{
		//((ParentTableModel) this.parentsTable.getModel()).setElement(element);
	}

	/**
	 * Update display on components impacted by navigation
	 * 
	 * @param element
	 *            the element to set
	 */
	public void reflectNavigation(LinkableElement element)
	{
		//this.currentElement = element;
	}

	/**
	 * Change the state of the panel from pure navigation to edition
	 * 
	 * @param state
	 *            true if the panel is in edition state
	 */
	public void changeState(boolean state)
	{
		/*this.parentsTable.setDragEnabled(state);
		this.centralTable.setDragEnabled(state);
		this.rightTable.setDragEnabled(state);
		if (state)
		{
			this.parentsTable.addMouseMotionListener(this.parentTableListener);
			this.centralTable.addMouseMotionListener(this.centralTableListener);
			this.rightTable.addMouseMotionListener(this.rightTableListener);
		}
		else
		{
			this.parentsTable.removeMouseMotionListener(this.parentTableListener);
			this.centralTable.removeMouseMotionListener(this.centralTableListener);
			this.rightTable.removeMouseMotionListener(this.rightTableListener);
		}*/
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((ConceptFilsTM)conceptFilsTable.getModel()).setDonnees(null);
		this.conceptPere.setText(null);
		((ConceptLemmeTM)lemmeAssocieTable.getModel()).setDonnees(null);
		((ConceptDefiniTM)conceptDefiniTable.getModel()).setDonnees(null);
		this.updateUI();
	}

	/**
	 * Delete key delete the current element
	 * 
	 * @param evt
	 * @throws Exception 
	 */
	protected void doNavigationKeyPressed(KeyEvent evt) throws Exception
	{
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			if (((JTable) evt.getSource()).getRowCount() > 0)
				((EditorTableModel) ((JTable) evt.getSource()).getModel()).removeElement(((JTable) evt.getSource()).getSelectedRow());
			evt.consume();
			break;
		default:
			break;
		}
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
		/*if (((ParentTableModel) this.parentsTable.getModel()).getElement() == element) ((ParentTableModel) this.parentsTable.getModel()).setElement(null);
		else ((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.centralTable.getModel()).getElement() == element) ((SonTableModel) this.centralTable.getModel()).setElement(null);
		else ((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.rightTable.getModel()).getElement() == element) ((SonTableModel) this.rightTable.getModel()).setElement(null);
		else ((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
	*/}
	
	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#relationChanged(arkeotek.ontology.LinkableElement, arkeotek.ontology.LinkableElement)
	 */
	@Override
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		if (this.currentElement == source || this.currentElement == target)
		{
	/*		((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
			((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
			((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
		*/}		
	}

	@Override
	public void reload()
	{
	/*	((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
		((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
		((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+this.currentElement.getName()));
		*/
	}

	public void refreshNavigation(LinkableElement element)
	{
		/*if (((ParentTableModel) this.parentsTable.getModel()).getElement() == element) ((ParentTableModel) this.parentsTable.getModel()).setElement(null);
		else ((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.centralTable.getModel()).getElement() == element) ((SonTableModel) this.centralTable.getModel()).setElement(null);
		else ((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.rightTable.getModel()).getElement() == element) ((SonTableModel) this.rightTable.getModel()).setElement(null);
		else ((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
*/	}

	private class TableComponentCellRenderer extends JLabel implements TableCellRenderer
	{
		/** This method is called each time a cell in a column
		/* using this renderer needs to be rendered
		 * @param table the table on that renderer is applied
		 * @param value value contained in the cell located at (rowIndex, vColIndex)
		 * @param isSelected true if the row is selected
		 * @param hasFocus true if cell has focus
		 * @param rowIndex row index
		 * @param vColIndex column index
		 * @return the component rendered
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex)
		{
			// 'value' is value contained in the cell located at
			// (rowIndex, vColIndex)

			this.setOpaque(true);
			setBackground(isSelected ? table.getSelectionBackground() : Color.white);

			// Configure the component with the specified value
			setText(((LinkableElement) value).getName());
			setIcon(ImagesManager.getInstance().getDefaultIcon((LinkableElement)value));

			// Set tool tip if desired
			if (value instanceof DocumentPart)
				setToolTipText(((DocumentPart) value).getValue());
			else
				setToolTipText(((LinkableElement) value).getName());

			// Since the renderer is a component, return itself
			return this;
		}
	}

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

	public void remplirTableFils(LinkableElement concept) {
		// TODO Auto-generated method stub
		this.currentElement=concept;
		conceptFilsTable.removeAll();
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation : "+concept));
		//String[] titreF={"Fils"};
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
				/*if (concept.getLinks(Lemma.KEY) != null)
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
				if (concept.getLinks(DocumentPart.KEY) != null)
				{
					keys = concept.getLinks(DocumentPart.KEY).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement elem : concept.getLinks(DocumentPart.KEY, key))
						{
							Object[] couple = {key, elem};
							elements.add(couple);
						}
					}
				}*/
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
			((ConceptFilsTM)conceptFilsTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][1];
			((ConceptFilsTM)conceptFilsTable.getModel()).setDonnees(donnees);
		}
		lemmeAssocieTable.updateUI();
		conceptFilsTable.updateUI();
		conceptDefiniTable.updateUI();
		this.updateUI();
		
	}

	public void remplirTableLemme(LinkableElement concept) {
		// TODO Auto-generated method stub
		lemmeAssocieTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		//elements.addAll(ApplicationManager.ontology.getParentsOf(concept, Lemma.KEY));
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
			((ConceptLemmeTM)lemmeAssocieTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][1];
			((ConceptLemmeTM)lemmeAssocieTable.getModel()).setDonnees(donnees);
		}
		
		this.updateUI();
	}

	public void remplirTableDefini(LinkableElement concept) {
		// TODO Auto-generated method stub
		conceptDefiniTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (concept != null)
		{
			/*if (concept instanceof Ontology)
			{
				for (LinkableElement concept2 : ((Ontology)concept).get(Concept.KEY))
				{
					Object[] couple = {"", concept2};
					elements.add(couple);
				}
			}
			else
			{*/
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
			//}
		}
		
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=((LinkableElement)elements.get(i)[0]);
				donnees[i][1]=((LinkableElement)elements.get(i)[1]);
			}
			((ConceptDefiniTM)conceptDefiniTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((ConceptDefiniTM)conceptDefiniTable.getModel()).setDonnees(donnees);
		}
		
		lemmeAssocieTable.updateUI();
		conceptFilsTable.updateUI();
		conceptDefiniTable.updateUI();
		this.updateUI();
		
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
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
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
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
	}

}
