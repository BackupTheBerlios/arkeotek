/**
 * Created on 3 juin 2005
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
public class LemmaSearchAction extends AbstractAction
{
	/**
	 * Default label associated to this action
	 */
	public static final String DEFAULT_LABEL = "Rechercher un lemme";
	/**
	 * Default icon associated to this action
	 */
	public static final String DEFAULT_ICON = "new.gif";

	/**
	 * @throws Exception
	 */
	public LemmaSearchAction() throws Exception
	{
		this(DEFAULT_LABEL, DEFAULT_ICON, ApplicationManager.Request.LEMMA_SEARCH);
	}

	/**
	 * @param label The name of this action.
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
    public LemmaSearchAction(String label, String icon, ApplicationManager.Request request) throws Exception
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
	public LemmaSearchAction(String label, String icon, ApplicationManager.Request request, KeyStroke keystroke) throws Exception                                
	{
		super(label, icon, request, keystroke);
	}
}
