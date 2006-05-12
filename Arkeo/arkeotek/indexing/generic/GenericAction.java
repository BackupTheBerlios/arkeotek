/**
 * Created on 11 août 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import arkeotek.indexing.Action;
import arkeotek.indexing.Indexer;
import arkeotek.indexing.Rule;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class GenericAction extends Action
{

	/**
	 * 
	 */
	public GenericAction()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 */
	public GenericAction(Rule owner)
	{
		super(owner);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws Exception 
	 * @see arkeotek.indexing.Action#index(arkeotek.ontology.LinkableElement)
	 */
	@Override
	public void index(LinkableElement target) throws Exception
	{
		HashMap<LinkableElement, ArrayList<LinkableElement>> concepts = new HashMap<LinkableElement, ArrayList<LinkableElement>>();
		int sons_number = 0;
		
		// Retrieving linked elements of type DocumentPart. 
		for (LinkableElement elem : target.getLinks(DocumentPart.KEY, new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION))) {
			sons_number++;
			// Retrieving their concepts. 
			for (Relation son_rel : elem.getLinks(Concept.KEY).keySet()) {
				for (LinkableElement sub_concept : elem.getLinks(Concept.KEY).get(son_rel).keySet()) {
					if (concepts.get(sub_concept) == null)
						concepts.put(sub_concept, new ArrayList<LinkableElement>());
					concepts.get(sub_concept).add(elem);
				}
			}
		}
		
		// Retrieving the Relation.DEFAULT_INDEXING_RELATION from the owner if exists. 
		int position = Collections.binarySearch(this.owner.getOwner().getOwner().get(Relation.KEY), new Relation(Indexer.DEFAULT_INDEXING_RELATION));
		Relation temp_rel;
		// If not, we create it
		if (position < 0) {
			temp_rel = new Relation(Indexer.DEFAULT_INDEXING_RELATION);
			this.owner.getOwner().getOwner().link(temp_rel);
		}
		// If so, we retrieve it
		else temp_rel = (Relation) this.owner.getOwner().getOwner().get(Relation.KEY).get(position);

		// Indexing concepts appearing in all of the sub-paragraphs. 
		for (LinkableElement concept : concepts.keySet()) {
			if (concepts.get(concept).size() == sons_number)
				target.link(temp_rel, concept);
		}
	}
}
