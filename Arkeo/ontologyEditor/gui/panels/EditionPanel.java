/**
 * Created on 30 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.HighEditorPaneTM;
import ontologyEditor.gui.tables.SecondEditorPaneTM;
import ontologyEditor.gui.transfers.ConceptDropTransferHandler;
import ontologyEditor.gui.transfers.LemmaDropTransferHandler;
import ontologyEditor.gui.transfers.TransferableConcept;
import ontologyEditor.gui.transfers.TransferableLemma;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class EditionPanel extends JPanel
{
	private JTable parentsEditionTable;

	private LinkableElementTextField elementField;

	private JTable rightEditionTable;

	private JToggleButton editionButton;
	
	private JButton deleteButton;
	
	private JLabel labelIcon = new JLabel();
	
	private LinkableElement courant;
	
	public EditionPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 20;
		double size[][] = { { border, 5, 0.50, 10, 0.50, 5, border}, // Columns
				{ border, 16, border, 0.05, border, 0.05, border, TableLayout.FILL, border, 0.1, border, TableLayout.FILL, border } }; // Rows
		this.setLayout(new TableLayout(size));
		
		String[] titreHaut={"relation","element"};
		HighEditorPaneTM tableHautModel = new HighEditorPaneTM();
		tableHautModel.setColumnNames(titreHaut);
		this.parentsEditionTable = new JTable(tableHautModel);
		//this.parentsEditionTable.setModel(tableHautModel);
		this.parentsEditionTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.parentsEditionTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.parentsEditionTable.setTransferHandler(new ConceptDropTransferHandler());
		this.parentsEditionTable.setDragEnabled(true);
		this.parentsEditionTable.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent evt) {
	            if( !evt.isConsumed() )
					try {
						doEditionKeyPressed(evt);
					} catch (Exception e) {
						e.printStackTrace();
					}
	        }
	    });
		this.editionButton = new JToggleButton("Modifier");
		this.editionButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (EditionPanel.this.editionButton.isEnabled())
				{
					DisplayManager.getInstance().changeState(EditionPanel.this.editionButton.isSelected());
				}
			}
		});
		this.parentsEditionTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				TransferHandler handler = EditionPanel.this.getParentsEditionTable().getTransferHandler();
				handler.exportAsDrag(EditionPanel.this.getParentsEditionTable(), e, TransferHandler.COPY);
			}
		});
		this.deleteButton = new JButton("Supprimer");
		this.deleteButton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (EditionPanel.this.deleteButton.isEnabled())
				{
					Object[] options = {"Oui", "Non"};
					int choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Vous \u00eates sur le point de supprimer d\u00e9finitivement l'\u00e9l\u00e9ment. D\u00e9sirez-vous continuer?", "Avertissement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					if (choice == 0)
					{
						LinkableElement element = EditionPanel.this.elementField.getElement();
						
						//int[] indexes = DisplayManager.mainFrame.getChildIndexesInTrees(element);
						try {
							ApplicationManager.ontology.unlinkElement(element);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						DisplayManager.mainFrame.refresh();
						//DisplayManager.getInstance().removeElement(element, indexes);
					}
				}
			}
		});
		this.elementField = new LinkableElementTextField(null);
		this.labelIcon.setHorizontalAlignment(JLabel.LEFT);
		
		this.elementField.setEditable(false);
		this.rightEditionTable = new JTable();
		
		String[] titreBas={"relation","element"};
		SecondEditorPaneTM tableBasModel = new SecondEditorPaneTM();
		tableBasModel.setColumnNames(titreHaut);
		
		this.rightEditionTable.setModel(tableBasModel);
		this.rightEditionTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.rightEditionTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.rightEditionTable.setTransferHandler(new LemmaDropTransferHandler());
		this.rightEditionTable.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent evt) {
	            if( !evt.isConsumed() )
					try {
						doEditionKeyPressed(evt);
					} catch (Exception e) {
						e.printStackTrace();
					}
	        }
	    });
		JScrollPane parentsEditionScrollPane = new JScrollPane();
		parentsEditionScrollPane.setDropTarget(new DropTarget(parentsEditionScrollPane, new DropTargetAdapter()
		{
			/**
			 * @see java.awt.dnd.DropTargetAdapter#dragEnter(java.awt.dnd.DropTargetDragEvent)
			 */
			@Override
			public void dragEnter(DropTargetDragEvent dtde)
			{
				Transferable transferable = dtde.getTransferable();
				if (transferable.isDataFlavorSupported(TransferableConcept.conceptFlavor))
					super.dragEnter(dtde);
				else
					dtde.rejectDrag();
			}

			public void drop(DropTargetDropEvent dtde)
			{
				try {
		            Transferable transferable = dtde.getTransferable();
		            if (transferable.isDataFlavorSupported(TransferableConcept.conceptFlavor))
		            {
						EditionPanel.this.parentsEditionTable.getTransferHandler().importData(EditionPanel.this.parentsEditionTable, transferable);
		            }
		            dtde.rejectDrop();
		            dtde.dropComplete(false);
		        }       
		        catch (Exception e) {   
		            dtde.rejectDrop();
		            dtde.dropComplete(false);
		        } 
		    }
			
		}));
		parentsEditionScrollPane.setBackground(Color.WHITE);
		parentsEditionScrollPane.setViewportView(this.parentsEditionTable);
		JScrollPane rightEditionScrollPane = new JScrollPane();
		rightEditionScrollPane.setDropTarget(new DropTarget(rightEditionScrollPane, new DropTargetAdapter()
		{
			/**
			 * @see java.awt.dnd.DropTargetAdapter#dragEnter(java.awt.dnd.DropTargetDragEvent)
			 */
			@Override
			public void dragEnter(DropTargetDragEvent dtde)
			{
				Transferable transferable = dtde.getTransferable();
				if (transferable.isDataFlavorSupported(TransferableLemma.lemmaFlavor))
					super.dragEnter(dtde);
				else
					dtde.rejectDrag();
			}
			public void drop(DropTargetDropEvent dtde)
			{
				try {
		            Transferable transferable = dtde.getTransferable();
		            if (transferable.isDataFlavorSupported(TransferableLemma.lemmaFlavor))
		            {
						EditionPanel.this.rightEditionTable.getTransferHandler().importData(EditionPanel.this.rightEditionTable, transferable);
		            }
		            dtde.rejectDrop();
		            dtde.dropComplete(false);
		        }       
		        catch (Exception e) {   
		            dtde.rejectDrop();
		            dtde.dropComplete(false);
		        } 
		    }
			
		}));
		rightEditionScrollPane.setBackground(Color.WHITE);
		rightEditionScrollPane.setViewportView(this.rightEditionTable);
		
		this.editionButton.setEnabled(false);
		this.deleteButton.setEnabled(false);

		// mise en place des renderer
		this.rendererTableConcept(this.parentsEditionTable);
		//this.rendererTableLemme(this.rightEditionTable);
		
		// mise en place des composant dasn le panel
		TableColumn columncon1 = rightEditionTable.getColumnModel().getColumn(0);
		columncon1.setPreferredWidth(80);
		TableColumn columncon2 = rightEditionTable.getColumnModel().getColumn(1);
		columncon2.setPreferredWidth(150);
		
		TableColumn columnco1 = parentsEditionTable.getColumnModel().getColumn(0);
		columnco1.setPreferredWidth(80);
		TableColumn columnco2 = parentsEditionTable.getColumnModel().getColumn(1);
		columnco2.setPreferredWidth(150);
		
		this.add(this.labelIcon, "1, 1, 2, 1");
		this.add(this.elementField, "1, 3, 5, 3");
		this.add(this.editionButton, "2, 5, 2, 5");
		this.add(this.deleteButton, "4, 5, 4, 5");
		this.add(parentsEditionScrollPane, "1, 7, 5, 7");
		this.add(rightEditionScrollPane, "1, 11, 5, 11");
		this.setBorder(BorderFactory.createTitledBorder("Panneau d'\u00e9dition"));

	}
	
	/** 
     * Delete key delete the current relation
	 * @param evt 
	 * @throws Exception 
     **/
    private void doEditionKeyPressed(KeyEvent evt) throws Exception {
            switch(evt.getKeyCode()){
                case KeyEvent.VK_DELETE:
					if(((JTable)evt.getSource()).getRowCount() > 0)
					{
						if (((JTable)evt.getSource()).getModel() instanceof HighEditorPaneTM)
						{
							LinkableElement element = (LinkableElement)EditionPanel.this.parentsEditionTable.getValueAt(((JTable)evt.getSource()).getSelectedRow(),1);
							//int[] indexes = DisplayManager.mainFrame.getChildIndexesInTrees(element);
							try {
								ApplicationManager.ontology.unlinkElement(element);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							DisplayManager.mainFrame.getEditionPanel().remplirTableHautParent(courant);
							DisplayManager.mainFrame.mAJ(courant);
						}
						else if (((JTable)evt.getSource()).getModel() instanceof SecondEditorPaneTM)
						{
							LinkableElement element = (LinkableElement)EditionPanel.this.rightEditionTable.getValueAt(((JTable)evt.getSource()).getSelectedRow(),1);
							//int[] indexes = DisplayManager.mainFrame.getChildIndexesInTrees(element);
							try {
								ApplicationManager.ontology.unlinkElement(element);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							DisplayManager.mainFrame.getEditionPanel().remplirTableBasParent(courant);
							DisplayManager.mainFrame.mAJ(courant);
						}
						evt.consume();
					}
                    break;
                default:
                    break;
            }
    }
	
	/**
	 * Change the state of the panel from pure navigation to edition
	 * 
	 * @param state
	 *            true if the panel is in edition state
	 */
	public void changeState(boolean state)
	{
		//this.elementField.setEditable(state);
		if (this.elementField.getElement() == null)this.editionButton.setEnabled(state);
		if (this.editionButton.isSelected())this.editionButton.setSelected(state);
		if (state)
		{
			this.remplirTableHautParent(this.elementField.getElement());
			this.remplirTableBasParent(this.elementField.getElement());
		}
		else
		{
			if (rightEditionTable.getRowCount()!=0)
			{
				rightEditionTable.removeAll();
				Object [][] donnees=new Object[0][2];
				//System.out.println(parentsEditionTable.getModel().getClass());
				((SecondEditorPaneTM)rightEditionTable.getModel()).setDonnees(donnees);
			}
			if(parentsEditionTable.getRowCount()!=0)
			{
				parentsEditionTable.removeAll();
				Object [][] donnees=new Object[0][2];
				//System.out.println(parentsEditionTable.getModel().getClass());
				((HighEditorPaneTM)parentsEditionTable.getModel()).setDonnees(donnees);
			}
			this.updateUI();
			
		}
		this.parentsEditionTable.setDragEnabled(state);
		this.rightEditionTable.setDragEnabled(state);
	}
	
	public void remplirTableBasParent(LinkableElement element) {
		// TODO Auto-generated method stub
		courant=element;
		rightEditionTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element.getLinks(Lemma.KEY) != null)
		{
			Set<Relation> keys = element.getLinks(Lemma.KEY).keySet();
			if (keys != null)
			{
				Object[] link = null;
				for (Relation key : keys)
				{
					ArrayList<LinkableElement> tmp = element.getLinks(Lemma.KEY, key);
					for (LinkableElement linkedElem : tmp)
					{
						link = new Object[2];
						link[0] = key;
						link[1] = linkedElem;
						elements.add(link);
					}
				}
			}
		}
		if (!(element instanceof DocumentPart))
		{
			if (element.getLinks(DocumentPart.KEY) != null)
			{
				Set<Relation> keys = element.getLinks(DocumentPart.KEY).keySet();
				if (keys != null)
				{
					Object[] link = null;
					for (Relation key : keys)
					{
						ArrayList<LinkableElement> tmp = element.getLinks(DocumentPart.KEY, key);
						for (LinkableElement linkedElem : tmp)
						{
							link = new Object[2];
							link[0] = key;
							link[1] = linkedElem;
							elements.add(link);
						}
					}
				}
			}
		}
		if (element instanceof Lemma)
			elements.addAll(ApplicationManager.ontology.getLemmasParents(element));
		
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			//System.out.println(parentsEditionTable.getModel().getClass());
			((SecondEditorPaneTM)rightEditionTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			//System.out.println(parentsEditionTable.getModel().getClass());
			((SecondEditorPaneTM)rightEditionTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
	}

	public void remplirTableHautParent(LinkableElement element) {
		// TODO Auto-generated method stub
		parentsEditionTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		if (element.getLinks(Concept.KEY) != null)
		{
			Set<Relation> keys = element.getLinks(Concept.KEY).keySet();
			if (keys != null)
			{
				Object[] link = null;
				for (Relation key : keys)
				{
					ArrayList<LinkableElement> tmp = element.getLinks(Concept.KEY, key);
					for (LinkableElement linkedElem : tmp)
					{
						link = new Object[2];
						link[0] = key;
						link[1] = linkedElem;
						elements.add(link);
					}
				}
			}
		}
		if (element instanceof Concept)
			elements.addAll(ApplicationManager.ontology.getParentsOf(element, Concept.KEY)); 
		
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			//System.out.println(parentsEditionTable.getModel().getClass());
			((HighEditorPaneTM)parentsEditionTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			//System.out.println(parentsEditionTable.getModel().getClass());
			((HighEditorPaneTM)parentsEditionTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
		
	}

	public void repercutNavigation(LinkableElement element)
	{
		if (!this.editionButton.isSelected()) 
		{
			if (element instanceof Ontology)
				this.elementField.setElement(null);
			else 
			{
				this.editionButton.setEnabled(true);
				this.deleteButton.setEnabled(!(element instanceof DocumentPart));
				this.elementField.setElement(element);
				this.labelIcon.setIcon(this.elementField.getIcon());
			}
		}
	}
	
	public void ontologyElementRemoved(LinkableElement element)
	{
		if (this.elementField.getElement() == element)
		{
			this.editionButton.setSelected(false);
			this.editionButton.setEnabled(false);
			this.deleteButton.setEnabled(false);
			this.labelIcon.setIcon(null);
			DisplayManager.getInstance().changeState(false);
			this.elementField.setElement(null);
		}
		else
			changeState(false);
		((EditorTableModel) this.parentsEditionTable.getModel()).fireTableStructureChanged();
		((EditorTableModel) this.rightEditionTable.getModel()).fireTableStructureChanged();
	}
	
	public void elementAdded(LinkableElement element)
	{
		this.editionButton.setEnabled(true);
		this.deleteButton.setEnabled(true);
		this.editionButton.setSelected(true);
		changeState(true);
		this.elementField.setElement(element);
		this.labelIcon.setIcon(this.elementField.getIcon());
		((EditorTableModel) this.parentsEditionTable.getModel()).setElement(element);
		((EditorTableModel) this.rightEditionTable.getModel()).setElement(element);
	}
	
	public void elementRemoved(LinkableElement element)
	{
			this.refresh();
	}
	
	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		this.deleteButton.setEnabled(false);
		this.elementField.setElement(null);
		this.labelIcon.setIcon(null);
		DisplayManager.getInstance().changeState(false);
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

	public JToggleButton getEditionButton() {
		return editionButton;
	}

	public void setEditionButton(JToggleButton editionButton) {
		this.editionButton = editionButton;
	}

	public JTable getRightEditionTable() {
		return rightEditionTable;
	}

	public void setRightEditionTable(JTable rightEditionTable) {
		this.rightEditionTable = rightEditionTable;
	}

	public JTable getParentsEditionTable() {
		return parentsEditionTable;
	}

	public void setParentsEditionTable(JTable parentsEditionTable) {
		this.parentsEditionTable = parentsEditionTable;
	}

	public LinkableElement getCourant() {
		return courant;
	}

	public void setCourant(LinkableElement courant) {
		this.courant = courant;
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
		//DefaultTableCellRenderer custom2 = new DefaultTableCellRenderer();
		//custom2.setHorizontalAlignment(JLabel.CENTER);
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_LEMMA_ICON));
			//custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_DOCUMENTPART_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(1).setCellRenderer(custom);
	}

}
