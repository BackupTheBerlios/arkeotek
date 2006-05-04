/**
 * Created on 2 juin 2005
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
public class ImportTermontoAction extends AbstractAction
{
	/**
	 * Default label associated to this action
	 */
	public static final String DEFAULT_LABEL = "Importe une ontologie cr\u00e9\u00e9e dans Termonto";
	/**
	 * Default icon associated to this action
	 */
	public static final String DEFAULT_ICON = "open.gif";
	
    /**
     * @throws Exception
     */
    public ImportTermontoAction() throws Exception
    {
		this(DEFAULT_LABEL, DEFAULT_ICON, ApplicationManager.Request.OPEN_ONTOLOGY_FROM_TERMONTO);
    }
	
	/**
	 * @param label The name of this action.
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
    public ImportTermontoAction(String label, String icon, ApplicationManager.Request request) throws Exception
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
	public ImportTermontoAction(String label, String icon, ApplicationManager.Request request, KeyStroke keystroke) throws Exception                                
	{
		super(label, icon, request, keystroke);
	}
}
