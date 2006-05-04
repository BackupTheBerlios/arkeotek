/**
 * Created on 20 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.actions;

import java.util.HashMap;

import javax.swing.KeyStroke;

import ontologyEditor.ApplicationManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class TopPanelChangeAction extends AbstractAction
{
	private static final HashMap<Integer, ApplicationManager.Request> requests = new HashMap<Integer, ApplicationManager.Request>(3);
	private static final HashMap<Integer, String> icons = new HashMap<Integer, String>(3);
	private static final HashMap<Integer, String> labels = new HashMap<Integer, String>(3);
	
	static {
		requests.put(new Integer(Concept.KEY), ApplicationManager.Request.CHANGE_TOP_CONTENT_CONCEPTS);
		requests.put(new Integer(Lemma.KEY), ApplicationManager.Request.CHANGE_TOP_CONTENT_LEMMAS);
		requests.put(new Integer(DocumentPart.KEY), ApplicationManager.Request.CHANGE_TOP_CONTENT_DOCUMENTS);

		icons.put(new Integer(Concept.KEY), "concept.gif");
		icons.put(new Integer(Lemma.KEY), "lemma.gif");
		icons.put(new Integer(DocumentPart.KEY), "document.gif");

		labels.put(new Integer(Concept.KEY), "Vue par concepts");
		labels.put(new Integer(Lemma.KEY), "Vue par termes");
		labels.put(new Integer(DocumentPart.KEY), "Vue par documents");
	}
	
    /**
     * @param categoryKey The <code>IIndexable</code> category key specifying the kind of view wished. 
     * @throws Exception
     */
    public TopPanelChangeAction(int categoryKey) throws Exception
    {
		this(labels.get(new Integer(categoryKey)), icons.get(new Integer(categoryKey)), requests.get(new Integer(categoryKey)));
    }
	
	/**
	 * @param label The name of this action.
	 * @param icon The icon to display. 
	 * @param request The request corresponding to this action. 
	 * @throws Exception The keystroke invoking this action. 
	 */
    public TopPanelChangeAction(String label, String icon, ApplicationManager.Request request) throws Exception
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
	public TopPanelChangeAction(String label, String icon, ApplicationManager.Request request, KeyStroke keystroke) throws Exception                                
	{
		super(label, icon, request, keystroke);
	}
}
