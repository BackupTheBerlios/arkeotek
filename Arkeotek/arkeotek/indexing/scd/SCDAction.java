/**
 * Created on 28 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing.scd;

import java.util.ArrayList;
import java.util.HashMap;

import arkeotek.indexing.Action;
import arkeotek.indexing.Rule;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class SCDAction extends Action
{

	/**
	 * Exists only to allow usage of Class.forName(String).newInstance().
	 */
	public SCDAction()
	{
		// Exists only to allow usage of Class.forName(String).newInstance().
	}

	/**
	 * @param owner
	 */
	public SCDAction(Rule owner)
	{
		super(owner);
	}

	/**
	 * @throws Exception 
	 * @see arkeotek.indexing.Action#index(LinkableElement target)
	 */
	@Override
	public void index(LinkableElement target) throws Exception
	{
		ArrayList<HashMap<LinkableElement, Link>> linked_documents = new ArrayList<HashMap<LinkableElement, Link>>(target.getLinks(DocumentPart.KEY).values());
		ArrayList<LinkableElement> parent_documents = new ArrayList<LinkableElement>();
		HashMap<Relation, HashMap<LinkableElement, Link>> linked_concepts = target.getLinks(Concept.KEY);
		ArrayList<Relation> relations = new ArrayList<Relation>(target.getLinks(Concept.KEY).keySet()); 
		
		for (HashMap<LinkableElement, Link> list : linked_documents) {
			for (LinkableElement document : list.keySet()) {
				if (document.toString().contains("ARGUMENT")) {
					for (LinkableElement parent : parent_documents) {
						for (Relation relation : relations) {
							for (LinkableElement elem : linked_concepts.get(relation).keySet())
								parent.link(relation, elem);
						}
					}
				}
			}
		}
	}
}
