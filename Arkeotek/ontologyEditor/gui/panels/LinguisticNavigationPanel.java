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
import java.util.EventObject;

import javax.swing.BorderFactory;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.tables.EditorTableModel;
import ontologyEditor.gui.tables.LemmaParentsTableModel;
import ontologyEditor.gui.tables.LinesTableModel;
import ontologyEditor.gui.tables.LinkableElementTableModel;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class LinguisticNavigationPanel extends AbstractNavigationPanel
{
	private JTable appearancesTable;
	
	private JTable linkedLemmasTable;
	
	private JTable lemmasParentsTable;
	
	private JButton validationButton;
	
	/**
	 * 
	 */
	public LinguisticNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 10;
		double sizeNavPanel[][] = { { border, TableLayout.FILL, border, TableLayout.FILL, border, TableLayout.PREFERRED, border }, // Columns
				{ border, TableLayout.FILL, 20, border, TableLayout.FILL, 20, border } }; // Rows
		this.setLayout(new TableLayout(sizeNavPanel));

		this.appearancesTable = new JTable(new LinesTableModel(null));
		this.appearancesTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.appearancesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane appearancesScrollPane = new JScrollPane();
		appearancesScrollPane.setViewportView(this.appearancesTable);
		appearancesScrollPane.setBorder(BorderFactory.createTitledBorder("Documents recens\u00e9s"));

		this.linkedLemmasTable = new JTable(new LinkableElementTableModel(null, Lemma.KEY));
		this.linkedLemmasTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.linkedLemmasTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane linkedLemmasScrollPane = new JScrollPane();
		linkedLemmasScrollPane.setViewportView(this.linkedLemmasTable);
		linkedLemmasScrollPane.setBorder(BorderFactory.createTitledBorder("Lemmes li\u00e9s"));

		this.lemmasParentsTable = new JTable(new LemmaParentsTableModel(null));
		this.lemmasParentsTable.setDefaultRenderer(LinkableElement.class, new TableComponentCellRenderer());
		this.lemmasParentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane lemmasParentsScrollPane = new JScrollPane();
		lemmasParentsScrollPane.setViewportView(this.lemmasParentsTable);
		lemmasParentsScrollPane.setBorder(BorderFactory.createTitledBorder("Lemmes parents li\u00e9s"));
		
		this.validationButton = new JButton("Valider");
		this.validationButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinesTableModel)LinguisticNavigationPanel.this.appearancesTable.getModel()).getElement();
				if (element != null)
				{
					LinguisticNavigationPanel.this.validationButton.setText((element.getState() == LinkableElement.VALIDATED)?"Valider":"Invalider");
					element.setState((element.getState() == LinkableElement.VALIDATED)?LinkableElement.DEFAULT:LinkableElement.VALIDATED);
					DisplayManager.getInstance().reloadTrees();
				}
			}
		});
		
		this.add(appearancesScrollPane, "1, 1, 3, 2");
		this.add(linkedLemmasScrollPane, "1, 4, 1, 5");
		this.add(lemmasParentsScrollPane, "3, 4, 3, 5");
		this.add(this.validationButton, "5, 5, 5, 5");
		this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation"));
}

	/**
	 * Updates navigation. 
	 * 
	 * @param element the element to set on the first navigation table
	 */
	public void fillTable(LinkableElement element)
	{
		((EditorTableModel) (this.appearancesTable.getModel())).setElement(element);
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
		this.appearancesTable.setDragEnabled(state);
	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		((EditorTableModel) this.appearancesTable.getModel()).setElement(null);
		((EditorTableModel) (this.linkedLemmasTable.getModel())).setElement(null);
		((EditorTableModel) (this.lemmasParentsTable.getModel())).setElement(null);
		this.validationButton.setText("Valider");
	}
	
	public void elementRemoved(LinkableElement element)
	{
		if (((LinesTableModel) this.appearancesTable.getModel()).getElement() == element) 
		{
			((EditorTableModel) this.appearancesTable.getModel()).setElement(null);
			((EditorTableModel) (this.linkedLemmasTable.getModel())).setElement(null);
			((EditorTableModel) (this.lemmasParentsTable.getModel())).setElement(null);
			this.setBorder(BorderFactory.createTitledBorder("Panneau de navigation"));
		}
		this.validationButton.setText("Valider");
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
		// TODO S'assurer que le changement d'état n'est pas utilisé dans cette vue. 
	}
	
	/**
	 * @see ontologyEditor.gui.panels.AbstractNavigationPanel#relationChanged(arkeotek.ontology.LinkableElement, arkeotek.ontology.LinkableElement)
	 */
	@Override
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		if (((LinesTableModel)this.appearancesTable.getModel()).getElement() == source
			|| ((LinesTableModel)this.appearancesTable.getModel()).getElement() == target)
		{
			((LinesTableModel)this.appearancesTable.getModel()).fireTableStructureChanged();
			((LinkableElementTableModel) (this.linkedLemmasTable.getModel())).fireTableStructureChanged();
			((EditorTableModel) (this.lemmasParentsTable.getModel())).fireTableStructureChanged();
		}
		
	}

	public void refreshNavigation(LinkableElement element)
	{
		if (((LinesTableModel) this.appearancesTable.getModel()).getElement() == element) this.refresh();
		else 
		{
			((LinesTableModel) this.appearancesTable.getModel()).fireTableStructureChanged();
			((LinkableElementTableModel) (this.linkedLemmasTable.getModel())).fireTableStructureChanged();
			((EditorTableModel) (this.lemmasParentsTable.getModel())).fireTableStructureChanged();
		}
	}
	
	public void reload()
	{
		((LinesTableModel) this.appearancesTable.getModel()).fireTableStructureChanged();
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
				DisplayManager.getInstance().reloadPanels();
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
}
