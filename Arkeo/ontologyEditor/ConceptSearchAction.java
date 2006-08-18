package ontologyEditor;

import javax.swing.KeyStroke;

import ontologyEditor.ApplicationManager;
import ontologyEditor.actions.AbstractAction;

public class ConceptSearchAction extends AbstractAction {

	/**
	 * Default label associated to this action
	 */
	public static final String DEFAULT_LABEL = "Rechercher un concept";
	/**
	 * Default icon associated to this action
	 */
	public static final String DEFAULT_ICON = "loupe.gif";

	/**
	 * @throws Exception
	 */
	public ConceptSearchAction() throws Exception
	{
		this(DEFAULT_LABEL, DEFAULT_ICON, ApplicationManager.Request.CONCEPT_SEARCH);
	}

	/**
	 * @param label The name of this action.
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
    public ConceptSearchAction(String label, String icon, ApplicationManager.Request request) throws Exception
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
	public ConceptSearchAction(String label, String icon, ApplicationManager.Request request, KeyStroke keystroke) throws Exception                                
	{
		super(label, icon, request, keystroke);
	}

}
