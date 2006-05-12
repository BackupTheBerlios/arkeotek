/**
 * Created on 16 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import ontologyEditor.ImagesManager;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public abstract class AbstractNavigationPanel extends JPanel
{
	/**
	 * Update display on components impacted by navigation
	 * 
	 * @param element the element to set
	 */
	public abstract void reflectNavigation(LinkableElement element);
	
	/**
	 * Clears all the tables and textfields.
	 */
	public abstract void refresh();

	/**
	 * Delete key delete the current element
	 * 
	 * @param evt
	 * @throws Exception 
	 */
	protected abstract void doNavigationKeyPressed(KeyEvent evt) throws Exception;

	/**
	 * Change the state of the panel from pure navigation to edition
	 * 
	 * @param state true if the panel is in edition state
	 */
	public abstract void changeState(boolean state);

	/**
	 * Reload the values contained in the panel. 
	 */
	public abstract void reload();

	/**
	 * Reflects the element removal action. 
	 * @param element The <code>LinkableElement</code> to be removed. 
	 */
	public abstract void elementRemoved(LinkableElement element);
	
	/**
	 * Refreshes the graphical view.  
	 * @param element The <code>LinkableElement</code> to be removed. 
	 */
	public abstract void refreshNavigation(LinkableElement element);
	
	/**
	 * Reflects the relation modification action. 
	 * @param source The source <code>LinkableElement</code> affected by this modification. 
	 * @param target The target <code>LinkableElement</code> affected by this modification. 
	 */
	public abstract void relationChanged(LinkableElement source, LinkableElement target);

	private class TableComponentCellRenderer extends JLabel implements TableCellRenderer
	{
		// This method is called each time a cell in a column
		// using this renderer needs to be rendered.
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex)
		{
			// 'value' is value contained in the cell located at
			// (rowIndex, vColIndex)
			
			setBackground(isSelected ? Color.blue : Color.white);

			// Configure the component with the specified value
			setText(((LinkableElement) value).getName());
			setIcon(ImagesManager.getInstance().getDefaultIcon((LinkableElement)value));

			// Set tool tip if desired
			setToolTipText(((LinkableElement) value).getName());

			// Since the renderer is a component, return itself
			return this;
		}

		// The following methods override the defaults for performance reasons
		public void validate()
		{
		}

		public void revalidate()
		{
		}

		protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
		{
		}

		public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue)
		{
		}
	}

}
