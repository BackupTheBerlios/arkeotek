/**
 * Created on 19 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor;

import java.util.HashMap;

import javax.swing.AbstractAction;


/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ActionManager
{
    private static ActionManager actionManager = null;

    private HashMap<String, AbstractAction> actionsMap = new HashMap<String, AbstractAction>();

	 /**
     * Constructor of ActionManager. It is private to allow the implementation of the 
     * singleton pattern
     */
    private ActionManager()
    {
		super();
    }

    /**
	 * @return Returns the actionManager.
	 */
	public static ActionManager getActionManager()
	{
		if (ActionManager.actionManager == null)
        {
            ActionManager.actionManager = new ActionManager();
        }
        return (ActionManager.actionManager);
	}
	
    /**
     * Give the action associated to the <code>key</code>
     * 
     * @param key
     *            key which identifies the action to get
     * @return action corresponding to the <code>key</code>
     */
    public AbstractAction getAction(String key)
    {
        return this.actionsMap.get(key);
    }
	
	/**
     * Register the <code>action</code> with the <code>key</code>
     * 
     * @param key
     *            key which identifie the action to register
     * @param action
     *            action to register
     */
    public void registerAction(String key, AbstractAction action)
    {       
            if (getAction(key) == null)	
				this.actionsMap.put(key, action);
    }	
}
