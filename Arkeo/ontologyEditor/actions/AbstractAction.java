/**
 * Created on 20 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import ontologyEditor.ApplicationManager;
import ontologyEditor.ImagesManager;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class AbstractAction extends javax.swing.AbstractAction
{
    protected ApplicationManager.Request request;

	/**
	 * @param label The name of this action.
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
	public AbstractAction(String label, String icon, ApplicationManager.Request request) throws Exception
	{
		this.putValue(Action.NAME, label);
        this.putValue(Action.SMALL_ICON, ImagesManager.getInstance().getSmallIcon(icon));
        this.putValue(Action.SHORT_DESCRIPTION, label);
        this.request = request;
	}

	/**
	 * @param label The name of this action. 
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @param keystroke The keystroke invoking this action. 
	 * @throws Exception
	 */
	public AbstractAction(String label, String icon, ApplicationManager.Request request, KeyStroke keystroke) throws Exception
	{
		this(label, icon, request);
        putValue(Action.ACCELERATOR_KEY, keystroke);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (this.isEnabled())
        {
			ApplicationManager.getApplicationManager().manageRequest(this.request);
        }
	}
}