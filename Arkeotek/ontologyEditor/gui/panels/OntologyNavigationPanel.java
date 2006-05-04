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
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.ParentTableModel;
import ontologyEditor.gui.tables.SonTableModel;
import ontologyEditor.gui.transfers.LinkableElementDragTransferHandler;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class OntologyNavigationPanel extends AbstractNavigationPanel
{
	private JTable centralTable;

	private JTable rightTable;

	private JTable parentsTable;

	private LinkableElement currentElement;
	
	private MouseMotionListener parentTableListener = new MouseMotionAdapter()
	{
		public void mouseDragged(MouseEvent e)
		{
			TransferHandler handler = OntologyNavigationPanel.this.parentsTable.getTransferHandler();
			handler.exportAsDrag(OntologyNavigationPanel.this.parentsTable, e, TransferHandler.COPY);
		}
	};
	
	private MouseMotionListener centralTableListener = new MouseMotionAdapter()
	{
		public void mouseDragged(MouseEvent e)
		{
			TransferHandler handler = OntologyNavigationPanel.this.centralTable.getTransferHandler();
			handler.exportAsDrag(OntologyNavigationPanel.this.centralTable, e, TransferHandler.COPY);
		}
	};
	
	private MouseMotionListener rightTableListener = new MouseMotionAdapter()
	{
		public void mouseDragged(MouseEvent e)
		{
			TransferHandler handler = OntologyNavigationPanel.this.rightTable.getTransferHandler();
			handler.exportAsDrag(OntologyNavigationPanel.this.rightTable, e, TransferHandler.COPY);
		}
	};

	/**
	 * 
	 */
	public OntologyNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 10;
		double size[][] = { { border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.FILL, border }, // Columns
				{ border, TableLayout.FILL, border } }; // Rows
		this.setLayout(new TableLayout(size));

		this.parentsTable = new JTable(new ParentTableModel(null));
		this.parentsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.parentsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.parentsTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.parentsTable.setDragEnabled(DisplayManager.editionState);
		this.parentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.parentsTable.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1));
				if (e.getClickCount() >= 2)
					OntologyNavigationPanel.this.rollFirstPanel(element);
				DisplayManager.getInstance().reflectNavigation(element);
			}
		});
//		following code in comments is for removing an element by pressing suppr key
		/*this.parentsTable.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent evt)
			{
				if (!evt.isConsumed()) doNavigationKeyPressed(evt);
			}
		});*/

		this.centralTable = new JTable(new SonTableModel(null));
		this.centralTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.centralTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.centralTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.centralTable.setDragEnabled(DisplayManager.editionState);
		this.centralTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.centralTable.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() >= 2)
				{
					OntologyNavigationPanel.this.rollSecondPanel(((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1)));
					showParents(((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1)));
				}
				DisplayManager.getInstance().reflectNavigation(((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1)));
			}
		});//		the following code in comments is for removing an element by pressing suppr key
/*		this.centralTable.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent evt)
			{
				if (!evt.isConsumed()) doNavigationKeyPressed(evt);
			}
		});
*/		this.rightTable = new JTable(new SonTableModel(null));
		this.rightTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		this.rightTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.rightTable.setTransferHandler(new LinkableElementDragTransferHandler());
		this.rightTable.setDragEnabled(DisplayManager.editionState);
		this.rightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.rightTable.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1));
				if (e.getClickCount() >= 2)
				{
					OntologyNavigationPanel.this.rollFirstPanel(((SonTableModel) OntologyNavigationPanel.this.rightTable.getModel()).getElement());
					OntologyNavigationPanel.this.rollSecondPanel(element);
					showParents(element);
				}
				DisplayManager.getInstance().reflectNavigation(element);
			}
		});
//		the following code in comments is for removing an element by pressing suppr key
/*		this.rightTable.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent evt)
			{
				if (!evt.isConsumed()) doNavigationKeyPressed(evt);
			}
		});
*/		JScrollPane parentsScrollPane = new JScrollPane();
		parentsScrollPane.setViewportView(this.parentsTable);
		parentsScrollPane.setBorder(BorderFactory.createTitledBorder("Concepts parents"));
		JScrollPane centerScrollPane = new JScrollPane();
		centerScrollPane.setViewportView(this.centralTable);
		centerScrollPane.setBorder(BorderFactory.createTitledBorder("Navigation 1"));
		JScrollPane rightScrollPane = new JScrollPane();
		rightScrollPane.setViewportView(this.rightTable);
		rightScrollPane.setBorder(BorderFactory.createTitledBorder("Navigation 2"));
		this.add(parentsScrollPane, "1, 1, 1, 1");
		this.add(centerScrollPane, "3, 1, 3, 1");
		this.add(rightScrollPane, "5, 1, 5, 1");
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
		((SonTableModel) (this.centralTable.getModel())).setElement(element);
		((SonTableModel) (this.rightTable.getModel())).setElement(null);
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
		((SonTableModel) (this.rightTable.getModel())).setElement(element);
		OntologyNavigationPanel.this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+element));
	}

	public void showParents(LinkableElement element)
	{
		((ParentTableModel) this.parentsTable.getModel()).setElement(element);
	}

	/**
	 * Update display on components impacted by navigation
	 * 
	 * @param element
	 *            the element to set
	 */
	public void reflectNavigation(LinkableElement element)
	{
		this.currentElement = element;
	}

	/**
	 * Change the state of the panel from pure navigation to edition
	 * 
	 * @param state
	 *            true if the panel is in edition state
	 */
	public void changeState(boolean state)
	{
		this.parentsTable.setDragEnabled(state);
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
		}
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((ParentTableModel) this.parentsTable.getModel()).setElement(null);
		((EditorTableModel) this.centralTable.getModel()).setElement(null);
		((EditorTableModel) this.rightTable.getModel()).setElement(null);
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
		if (((ParentTableModel) this.parentsTable.getModel()).getElement() == element) ((ParentTableModel) this.parentsTable.getModel()).setElement(null);
		else ((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.centralTable.getModel()).getElement() == element) ((SonTableModel) this.centralTable.getModel()).setElement(null);
		else ((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.rightTable.getModel()).getElement() == element) ((SonTableModel) this.rightTable.getModel()).setElement(null);
		else ((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
	}
	
	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#relationChanged(arkeotek.ontology.LinkableElement, arkeotek.ontology.LinkableElement)
	 */
	@Override
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		if (this.currentElement == source || this.currentElement == target)
		{
			((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
			((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
			((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
		}		
	}

	@Override
	public void reload()
	{
		((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
		((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
		((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation: "+this.currentElement.getName()));
		
	}

	public void refreshNavigation(LinkableElement element)
	{
		if (((ParentTableModel) this.parentsTable.getModel()).getElement() == element) ((ParentTableModel) this.parentsTable.getModel()).setElement(null);
		else ((ParentTableModel) this.parentsTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.centralTable.getModel()).getElement() == element) ((SonTableModel) this.centralTable.getModel()).setElement(null);
		else ((SonTableModel) this.centralTable.getModel()).fireTableStructureChanged();
		if (((SonTableModel) this.rightTable.getModel()).getElement() == element) ((SonTableModel) this.rightTable.getModel()).setElement(null);
		else ((SonTableModel) this.rightTable.getModel()).fireTableStructureChanged();
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
