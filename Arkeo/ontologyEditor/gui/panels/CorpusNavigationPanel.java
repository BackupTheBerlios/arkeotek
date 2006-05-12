/**
 * Created on 16 mai 2005
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.LinkableElementTableModel;
import ontologyEditor.gui.tables.PotentialConceptsTableModel;
import ontologyEditor.gui.transfers.ConceptDropTransferHandler;
import ontologyEditor.gui.transfers.ConceptIndexingDragTranferHandler;
import ontologyEditor.gui.transfers.TransferableConcept;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class CorpusNavigationPanel extends AbstractNavigationPanel
{
	private JTextArea txtArea_docText;
	private JTable conceptsIndexingTable;
	private JTable potentialConceptsTable;
	private SonDetailPanel pnl_SonDetail;
	
	private JButton validationButton;

	/**
	 * 
	 */
	public CorpusNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 7;
		double sizeNavPanel[][] = { 
				{ border, TableLayout.FILL, border, TableLayout.FILL, TableLayout.PREFERRED, border }, // Columns
				{ border, TableLayout.FILL, border, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, 20, border } // Rows 
			};
		this.setLayout(new TableLayout(sizeNavPanel));
		this.setBorder(BorderFactory.createTitledBorder("D\u00e9tail du document "));

		this.txtArea_docText = new JTextArea("");
		this.txtArea_docText.setLineWrap(true);
		this.txtArea_docText.setEditable(false);
		JScrollPane areaSP = new JScrollPane(this.txtArea_docText);
		areaSP.setBorder(BorderFactory.createTitledBorder("Aper\u00e7u"));
		this.add(areaSP, "1, 1, 4, 1");
		
		this.conceptsIndexingTable = new JTable(new LinkableElementTableModel(null, Concept.KEY));
		this.conceptsIndexingTable.setTransferHandler(new ConceptDropTransferHandler());
		this.conceptsIndexingTable.setDragEnabled(true);
		this.conceptsIndexingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.conceptsIndexingTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.conceptsIndexingTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.conceptsIndexingTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				LinkableElement selected = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1));
				reflectNavigation(selected);
			}
		});
		this.conceptsIndexingTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				TransferHandler handler = CorpusNavigationPanel.this.conceptsIndexingTable.getTransferHandler();
				handler.exportAsDrag(CorpusNavigationPanel.this.conceptsIndexingTable, e, TransferHandler.COPY);
			}
		});
		JScrollPane jsp = new JScrollPane(this.conceptsIndexingTable);
		jsp.setDropTarget(new DropTarget(jsp, new DropTargetAdapter()
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
						CorpusNavigationPanel.this.conceptsIndexingTable.getTransferHandler().importData(CorpusNavigationPanel.this.conceptsIndexingTable, transferable);
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
		jsp.setBorder(BorderFactory.createTitledBorder("Concepts indexant"));
		this.add(jsp, "1, 3, 1, 4");
		
		this.potentialConceptsTable = new JTable(new PotentialConceptsTableModel(null, Concept.KEY));
		this.potentialConceptsTable.setTransferHandler(new ConceptIndexingDragTranferHandler());
		this.potentialConceptsTable.setDragEnabled(true);
		this.potentialConceptsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.potentialConceptsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.potentialConceptsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.potentialConceptsTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				LinkableElement selected = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 2));
				reflectNavigation(selected);
			}
		});
		this.potentialConceptsTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				TransferHandler handler = CorpusNavigationPanel.this.potentialConceptsTable.getTransferHandler();
				handler.exportAsDrag(CorpusNavigationPanel.this.potentialConceptsTable, e, TransferHandler.MOVE);
			}
		});
		JScrollPane jspPotential = new JScrollPane(this.potentialConceptsTable);
		jspPotential.setBorder(BorderFactory.createTitledBorder("Concepts potentiels"));
		this.add(jspPotential, "1, 5, 1, 7");
		
		this.pnl_SonDetail = new SonDetailPanel();
		this.add(this.pnl_SonDetail, "3, 3, 4, 6");
		
		this.validationButton = new JButton("Valider");
		this.validationButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinkableElementTableModel)CorpusNavigationPanel.this.conceptsIndexingTable.getModel()).getElement();
				if (element != null)
				{
					CorpusNavigationPanel.this.validationButton.setText((element.getState() == LinkableElement.VALIDATED)?"Valider":"Invalider");
					element.setState((element.getState() == LinkableElement.VALIDATED)?LinkableElement.DEFAULT:LinkableElement.VALIDATED);
					DisplayManager.getInstance().reloadTrees();
				}
			}
		});
		this.add(this.validationButton, "4, 7, 4, 7");
	}

	/**
	 * Updates navigation
	 * 
	 * @param element the element to set on the first navigation table
	 */
	public void fillTable(DocumentPart element)
	{
		this.txtArea_docText.setText(element.getValue());
		((LinkableElementTableModel) (this.conceptsIndexingTable.getModel())).setElement(element);
		((PotentialConceptsTableModel) (this.potentialConceptsTable.getModel())).setElement(element);
		this.validationButton.setText((element.getState() == LinkableElement.VALIDATED)?"Invalider":"Valider");
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+element.getName()));
		this.pnl_SonDetail.refresh();
	}

	/**
	 * Update display on components impacted by navigation
	 * 
	 * @param element the element to set
	 */
	public void reflectNavigation(LinkableElement element)
	{
		this.validationButton.setText((element.getState() == LinkableElement.VALIDATED)?"Invalider":"Valider");
		this.pnl_SonDetail.setElement(element);
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((EditorTableModel) this.conceptsIndexingTable.getModel()).setElement(null);
		((EditorTableModel) this.potentialConceptsTable.getModel()).setElement(null);
		this.pnl_SonDetail.refresh();
	}
		
	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#elementRemoved(arkeotek.ontology.LinkableElement)
	 */
	public void elementRemoved(LinkableElement element)
	{
		if (((LinkableElementTableModel) this.conceptsIndexingTable.getModel()).getElement() == element) ((PotentialConceptsTableModel) this.conceptsIndexingTable.getModel()).setElement(null);
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
				{
					((EditorTableModel) ((JTable)evt.getSource()).getModel()).removeElement(((JTable)evt.getSource()).getSelectedRow());
				}
				evt.consume();
				break;
			default : break;
        }
    }

	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#changeState(boolean)
	 */
	@Override
	public void changeState(boolean state)
	{
		// Nothing special to do for this sub-class of AbstractNavigationPanel.  
	}
	
	private class SonDetailPanel extends JPanel {
		private JTextArea txt_text;
		private JTable tbl_concepts;
		private JTable tbl_lemmas;
		private LinkableElement element;
		
		/**
		 * Creates a new <code>SonDetailPanel</code>. 
		 */
		public SonDetailPanel() {
			super();
			// Create a TableLayout for the panel
			double border = 10;
			double sizeNavPanel[][] = { 
					{ border, TableLayout.FILL, border, TableLayout.FILL, border }, // Columns
					{ border, TableLayout.FILL, border, TableLayout.FILL, TableLayout.FILL, TableLayout.FILL, border } // Rows 
				};
			this.setLayout(new TableLayout(sizeNavPanel));
			this.setBorder(BorderFactory.createTitledBorder("D\u00e9tail de l'\u00e9l\u00e9ment"));

			this.txt_text = new JTextArea("");
			this.txt_text.setEditable(false);
			this.txt_text.setOpaque(false);
			this.txt_text.setLineWrap(true);
			JScrollPane jsp0 = new JScrollPane(this.txt_text);
			jsp0.setBorder(BorderFactory.createTitledBorder("Aper\u00e7u"));
			this.add(jsp0, "1, 1, 3, 1");
			
			this.tbl_concepts = new JTable(new LinkableElementTableModel(null, Concept.KEY));
			this.tbl_concepts.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
			this.tbl_concepts.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
			JScrollPane jsp1 = new JScrollPane(this.tbl_concepts);
			jsp1.setBorder(BorderFactory.createTitledBorder("Concepts"));
			this.add(jsp1, "1, 3, 1, 5");

			this.tbl_lemmas = new JTable(new LinkableElementTableModel(null, Lemma.KEY));
			this.tbl_lemmas.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
			this.tbl_lemmas.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
			JScrollPane jsp2 = new JScrollPane(this.tbl_lemmas);
			jsp2.setBorder(BorderFactory.createTitledBorder("Termes"));
			this.add(jsp2, "3, 3, 3, 5");
		}

		/**
		 * Clears all the tables and textfields.
		 */
		public void refresh()
		{
			this.txt_text.setText("");
			((EditorTableModel) this.tbl_concepts.getModel()).setElement(null);
			((LinkableElementTableModel) this.tbl_concepts.getModel()).fireTableStructureChanged();
			((EditorTableModel) this.tbl_lemmas.getModel()).setElement(null);
			((LinkableElementTableModel) this.tbl_lemmas.getModel()).fireTableStructureChanged();
			this.validate();
		}

		/**
		 * @return The current <code>LinkableElement</code> on wich is focused this panel.
		 */
		public LinkableElement getElement()
		{
			return this.element;
		}

		/**
		 * @param element The <code>LinkableElement</code> to focus this panel on. 
		 */
		public void setElement(LinkableElement element)
		{
			this.refresh();
			this.element = element;
			((LinkableElementTableModel) this.tbl_concepts.getModel()).setElement(element);
			((LinkableElementTableModel) this.tbl_lemmas.getModel()).setElement(element);
			((AbstractTableModel) this.tbl_concepts.getModel()).fireTableStructureChanged();
			((AbstractTableModel) this.tbl_lemmas.getModel()).fireTableStructureChanged();
			if (element instanceof DocumentPart)
				this.txt_text.setText(((DocumentPart) element).getValue());
			else 
				this.txt_text.setText(element.toString());
		}
		
		public void refreshNavigation(LinkableElement element)
		{
			this.reload();
		}
		
		public void reload()
		{
			this.txt_text.setText("");
			((LinkableElementTableModel) this.tbl_concepts.getModel()).fireTableStructureChanged();
			((LinkableElementTableModel) this.tbl_lemmas.getModel()).fireTableStructureChanged();
			this.validate();
		}
	}

	@Override
	public void refreshNavigation(LinkableElement element)
	{
		((EditorTableModel) this.conceptsIndexingTable.getModel()).fireTableStructureChanged();
		((EditorTableModel) this.potentialConceptsTable.getModel()).fireTableStructureChanged();
		this.validationButton.setText("Valider");
		this.pnl_SonDetail.refreshNavigation(element);
	}

	@Override
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		// Nothing special to do for this sub-class of AbstractNavigationPanel.  
	}

	@Override
	public void reload()
	{
		((EditorTableModel) this.conceptsIndexingTable.getModel()).fireTableStructureChanged();
		((EditorTableModel) this.potentialConceptsTable.getModel()).fireTableStructureChanged();
		this.pnl_SonDetail.reload();
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
}