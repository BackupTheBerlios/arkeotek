/**
 * Created on 20 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.actions;

import javax.swing.KeyStroke;

import ontologyEditor.ApplicationManager;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class OpenAction extends AbstractAction
{
	/**
	 * Default label associated to this action
	 */
	public static final String DEFAULT_LABEL = "Ouvre une ontologie existante";
	/**
	 * Default icon associated to this action
	 */
	public static final String DEFAULT_ICON = "open.gif";
	
    /**
     * @throws Exception
     */
    public OpenAction() throws Exception
    {
		this(DEFAULT_LABEL, DEFAULT_ICON, ApplicationManager.Request.OPEN_ONTOLOGY);
    }
	
	/**
	 * @param label The name of this action.
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
    public OpenAction(String label, String icon, ApplicationManager.Request request) throws Exception
    {
		super(label, icon, request);
    }

	/**
	 * @param label The name of this action. 
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @param keystroke The keystroke invoking this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
	public OpenAction(String label, String icon, ApplicationManager.Request request, KeyStroke keystroke) throws Exception                                
	{
		super(label, icon, request, keystroke);
	}
}
