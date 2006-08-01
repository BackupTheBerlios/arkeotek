/**
 * Created on 16 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.dialogs.DocumentPartToLemme;
import ontologyEditor.gui.tables.ConceptLemmeTM;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.LemmaParentTM;
import ontologyEditor.gui.tables.LinesTableModel;
import ontologyEditor.gui.tables.LinkableElementTableModel;
import ontologyEditor.gui.tables.LinkableLemmeTM;
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
	
	private JButton voirDoc;
	
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
		
		String[] titreLier={"relation","terme"};
		LinkableLemmeTM tableLLModel = new LinkableLemmeTM();
		tableLLModel.setColumnNames(titreLier);
		this.linkedLemmasTable = new JTable(tableLLModel);
		this.linkedLemmasTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.linkedLemmasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		JScrollPane linkedLemmasScrollPane = new JScrollPane();
		linkedLemmasScrollPane.setViewportView(this.linkedLemmasTable);
		linkedLemmasScrollPane.setBorder(BorderFactory.createTitledBorder("Lemmes liÈs"));

		String[] titreParent={"relation","terme"};
		LemmaParentTM tableParentModel = new LemmaParentTM();
		tableParentModel.setColumnNames(titreParent);
		this.lemmasParentsTable = new JTable(tableParentModel);
		this.lemmasParentsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.lemmasParentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane lemmasParentsScrollPane = new JScrollPane();
		lemmasParentsScrollPane.setViewportView(this.lemmasParentsTable);
		lemmasParentsScrollPane.setBorder(BorderFactory.createTitledBorder("Lemmes parents liÈs"));
		
		ConceptLemmeTM tableConceptModel = new ConceptLemmeTM();
		tableConceptModel.setColumnNames(titreParent);
		this.concept = new JTable(tableConceptModel);
		this.concept.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.concept.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane conceptScrollPane = new JScrollPane();
		conceptScrollPane.setViewportView(this.concept);
		conceptScrollPane.setBorder(BorderFactory.createTitledBorder("Concepts liÈs"));
		
		String[] titre={"Relation","Identifiant","AperÁu"};
		LinesTableModel tableModel = new LinesTableModel();
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
		documentScrollPane.setBorder(BorderFactory.createTitledBorder("Documents liÈs"));
		
		this.validationButton = new JButton("Valider");
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
		
		this.precedentButton = new JButton("Retour");
		this.precedentButton.setIcon(new ImageIcon(Constants.DEFAULT_ICONS_PATH+"previous.gif"));
		/*if (precedent.size()!=0)
		{
			precedentButton.setToolTipText(precedent.get(precedent.size()-1).toString());
		}
		else
		{
			precedentButton.setToolTipText("Aucun prÈcÈdent");
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
		
		this.suivantButton = new JButton("Suivant");
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
		
		
		
		this.voirDoc = new JButton("Documents associÈs");
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
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation"));
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
		this.voirDoc.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				DocumentPartToLemme fen = new DocumentPartToLemme(DisplayManager.mainFrame,elem);
				fen.setVisible(true);
			}
		});
		((EditorTableModel) (this.linkedLemmasTable.getModel())).setElement(element);
		((EditorTableModel) (this.lemmasParentsTable.getModel())).setElement(element);
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+element));
		this.validationButton.setText((element.getState() == LinkableElement.VALIDATED)?"Invalider":"Valider");
	}

	/**
	 * Update display on components impacted by navigation
	 * 
	 * @param element the element to set
	 */
	public void reflectNavigation(LinkableElement element)
	{
//		empty corpse
	}

	/**
	 * Change the state of the panel from pure navigation to edition
	 * 
	 * @param state true if the panel is in edition state
	 */
	public void changeEditionState(boolean state)
	{
		//this.appearancesTable.setDragEnabled(state);
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((ConceptLemmeTM) this.concept.getModel()).setDonnees(null);
		((LinkableLemmeTM) (this.linkedLemmasTable.getModel())).setDonnees(null);
		((LemmaParentTM) (this.lemmasParentsTable.getModel())).setDonnees(null);
		((LinesTableModel) (this.document.getModel())).setDonnees(null);
		this.validationButton.setText("Valider");
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation du lemme : "));
		this.updateUI();
	}
	
	public void elementRemoved(LinkableElement element)
	{
		/*if (((LinesTableModel) this.appearancesTable.getModel()).getElement() == element) 
		{
			((EditorTableModel) this.appearancesTable.getModel()).setElement(null);
			((EditorTableModel) (this.linkedLemmasTable.getModel())).setElement(null);
			((EditorTableModel) (this.lemmasParentsTable.getModel())).setElement(null);
			this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation"));
		}
		this.validationButton.setText("Valider");*/
	}
	
	/** 
     * Delete key delete the current element
	 * @param evt 
	 * @throws Exception 
     **/
    protected void doNavigationKeyPressed(KeyEvent evt) throws Exception {
		switch(evt.getKeyCode()){
			case KeyEvent.VK_DELETE : 
				if(((JTable)evt.getSource()).getRowCount() > 0)
					((EditorTableModel) ((JTable)evt.getSource()).getModel()).removeElement(((JTable)evt.getSource()).getSelectedRow());
				evt.consume();
				break;
			default : break;
        }
    }

	@Override
	public void changeState(boolean state)
	{
		// TODO S'assurer que le changement d'√©tat n'est pas utilis√© dans cette vue. 
	}
	
	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#relationChanged(arkeotek.ontology.LinkableElement, arkeotek.ontology.LinkableElement)
	 */
	@Override
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		/*if (((LinesTableModel)this.appearancesTable.getModel()).getElement() == source
			|| ((LinesTableModel)this.appearancesTable.getModel()).getElement() == target)
		{
			((LinesTableModel)this.appearancesTable.getModel()).fireTableStructureChanged();
			((LinkableElementTableModel) (this.linkedLemmasTable.getModel())).fireTableStructureChanged();
			((EditorTableModel) (this.lemmasParentsTable.getModel())).fireTableStructureChanged();
		}*/
		
	}

	public void refreshNavigation(LinkableElement element)
	{
		/*if (((LinesTableModel) this.appearancesTable.getModel()).getElement() == element) this.refresh();
		else 
		{
			((LinesTableModel) this.appearancesTable.getModel()).fireTableStructureChanged();
			((LinkableElementTableModel) (this.linkedLemmasTable.getModel())).fireTableStructureChanged();
			((EditorTableModel) (this.lemmasParentsTable.getModel())).fireTableStructureChanged();
		}*/
	}
	
	public void reload()
	{
		//((LinesTableModel) this.appearancesTable.getModel()).fireTableStructureChanged();
		((LinkableElementTableModel) (this.linkedLemmasTable.getModel())).fireTableStructureChanged();
		((EditorTableModel) (this.lemmasParentsTable.getModel())).fireTableStructureChanged();
	}
	
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

//			 Configure the component with the specified value
			setText(((LinkableElement) value).getName());
			setIcon(ImagesManager.getInstance().getDefaultIcon((LinkableElement)value));

//			 Set tool tip if desired
			if (value instanceof DocumentPart)
				setToolTipText(((DocumentPart) value).getValue());
			else
				setToolTipText(((LinkableElement) value).getName());
			
			this.setOpaque(true);
			setBackground(isSelected ? table.getSelectionBackground() : Color.white);
			

			// Since the renderer is a component, return itself
			return this;
		}
	}

	
	private class CheckboxRenderer extends JCheckBox implements TableCellRenderer
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
		public Component getTableCellRendererComponent (JTable table,
                                                            Object value,
                                                            boolean isSelected,
                                                            boolean hasFocus,
                                                            int rowIndex,
                                                            int vColIndex)
        {
			setOpaque(true);
			if ((Boolean)value == true)
				setSelected(true);
			else 
				setSelected(false);
			if (table.isEditing())
			{
				setBackground (table.getSelectionBackground());
			}
			else
			{
				if (isSelected)
				{
					setBackground (table.getSelectionBackground());
				}
	            else
				{
					setBackground (Color.white);
				}
			}
			setHorizontalAlignment (SwingConstants.CENTER);
            return this;
        }
    }
	
	private class CheckBoxCellEditor extends JCheckBox implements TableCellEditor
	{   
		protected ChangeEvent changeEvent = null;
		
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	    {
			if(value instanceof Boolean )
	        {
	            this.setSelected( ((Boolean)value).booleanValue() );
// TODO				((EditorTableModel)table.getModel()).getElement().setState((((EditorTableModel)table.getModel()).getElement().getState(((Relation)((EditorTableModel)table.getModel()).getValueAt(row, 0)), ((LinkableElement)((EditorTableModel)table.getModel()).getValueAt(row, 1))) == LinkableElement.IGNORED)?LinkableElement.DEFAULT:LinkableElement.IGNORED, ((String)((EditorTableModel)table.getModel()).getValueAt(row, 0)), ((LinkableElement)((EditorTableModel)table.getModel()).getValueAt(row, 1)));
				//DisplayManager.getInstance().reloadPanels();
	        }
	        return this;
	    }
	 
	 
	    public Object getCellEditorValue()
	    {
			return new Boolean(this.isSelected());
	    }


		public boolean isCellEditable(EventObject anEvent)
		{
			return true;
		}


		public boolean shouldSelectCell(EventObject anEvent)
		{
			return true;
		}


		public boolean stopCellEditing()
		{
			fireEditingStopped();
			return true;
		}


		public void cancelCellEditing()
		{
			fireEditingCanceled();
		}
		
		/**
	     * Adds a <code>CellEditorListener</code> to the listener list.
	     * @param l  the new listener to be added
	     */
	    public void addCellEditorListener(CellEditorListener l) {
			this.listenerList.add(CellEditorListener.class, l);
	    }

	    /**
	     * Removes a <code>CellEditorListener</code> from the listener list.
	     * @param l  the listener to be removed
	     */
	    public void removeCellEditorListener(CellEditorListener l) {
			this.listenerList.remove(CellEditorListener.class, l);
	    }
		
		/**
	     * Notifies all listeners that have registered interest for
	     * notification on this event type.  The event instance 
	     * is created lazily.
	     *
	     * @see EventListenerList
	     */
	    protected void fireEditingStopped() {
			// Guaranteed to return a non-null array
			Object[] listeners = this.listenerList.getListenerList();
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length-2; i>=0; i-=2) {
			    if (listeners[i]==CellEditorListener.class) {
				// Lazily create the event:
				if (this.changeEvent == null)
				    this.changeEvent = new ChangeEvent(this);
				((CellEditorListener)listeners[i+1]).editingStopped(changeEvent);
			    }	       
			}
	    }

	    /**
	     * Notifies all listeners that have registered interest for
	     * notification on this event type.  The event instance 
	     * is created lazily.
	     *
	     * @see EventListenerList
	     */
	    protected void fireEditingCanceled() {
			// Guaranteed to return a non-null array
			Object[] listeners = this.listenerList.getListenerList();
			// Process the listeners last to first, notifying
			// those that are interested in this event
			for (int i = listeners.length-2; i>=0; i-=2) {
			    if (listeners[i]==CellEditorListener.class) {
				// Lazily create the event:
				if (this.changeEvent == null)
				    this.changeEvent = new ChangeEvent(this);
				((CellEditorListener)listeners[i+1]).editingCanceled(changeEvent);
			    }	       
			}
	    }
	}
	
	// rempli la table des lemmes parent du lemme courant
	public void remplirTableLemmeParent(LinkableElement lemme)
	{
		this.currentElement=lemme;
		this.validationButton.setText((lemme.getState() == LinkableElement.VALIDATED)?"Invalider":"Valider");
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation du lemme : "+lemme.getName()));
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
			((LemmaParentTM)lemmasParentsTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((LemmaParentTM)lemmasParentsTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
	}
	
	// rempli la table des lemmes liÈ du lemme courant
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
			((LinkableLemmeTM)linkedLemmasTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((LinkableLemmeTM)linkedLemmasTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
		final LinkableElement elem=lemme;
		this.voirDoc.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				DocumentPartToLemme fen = new DocumentPartToLemme(DisplayManager.mainFrame,elem);
				fen.setVisible(true);
			}
		});
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
			((ConceptLemmeTM)concept.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((ConceptLemmeTM)concept.getModel()).setDonnees(donnees);
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
			((LinesTableModel)document.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][3];
			((LinesTableModel)document.getModel()).setDonnees(donnees);
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
}
