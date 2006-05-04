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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.HightEditorPaneTableModel;
import ontologyEditor.gui.tables.SecondEditorPaneTableModel;
import ontologyEditor.gui.transfers.ConceptDropTransferHandler;
import ontologyEditor.gui.transfers.LemmaDropTransferHandler;
import ontologyEditor.gui.transfers.TransferableConcept;
import ontologyEditor.gui.transfers.TransferableLemma;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;

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
	
	public EditionPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 20;
		double size[][] = { { border, 5, 0.50, 10, 0.50, 5, border}, // Columns
				{ border, 16, border, 0.05, border, 0.05, border, TableLayout.FILL, border, 0.1, border, TableLayout.FILL, border } }; // Rows
		this.setLayout(new TableLayout(size));
	
		this.parentsEditionTable = new JTable(new HightEditorPaneTableModel(null));
		this.parentsEditionTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.parentsEditionTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.parentsEditionTable.setTransferHandler(new ConceptDropTransferHandler());
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
						
						int[] indexes = DisplayManager.mainFrame.getChildIndexesInTrees(element);
						try {
							ApplicationManager.ontology.unlinkElement(element);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						DisplayManager.getInstance().removeElement(element, indexes);
					}
				}
			}
		});
		this.elementField = new LinkableElementTextField(null);
		this.labelIcon.setHorizontalAlignment(JLabel.LEFT);
		
		this.elementField.setEditable(false);
		this.rightEditionTable = new JTable();
		
		this.rightEditionTable.setModel(new SecondEditorPaneTableModel(null));
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
						((EditorTableModel) ((JTable)evt.getSource()).getModel()).removeRelation(((JTable)evt.getSource()).getSelectedRow());
                    evt.consume();
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
			((EditorTableModel) (this.parentsEditionTable.getModel())).setElement(this.elementField.getElement());
			((EditorTableModel) (this.rightEditionTable.getModel())).setElement(this.elementField.getElement());

		}
		else
		{
			((EditorTableModel) (this.parentsEditionTable.getModel())).setElement(null);
			((EditorTableModel) (this.rightEditionTable.getModel())).setElement(null);
		}
		this.parentsEditionTable.setDragEnabled(state);
		this.rightEditionTable.setDragEnabled(state);
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

}
