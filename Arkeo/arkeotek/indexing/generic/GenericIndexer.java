/**
 * Created on 27 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing.generic;

import java.util.ArrayList;

import arkeotek.indexing.Indexer;
import arkeotek.indexing.Rule;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class GenericIndexer extends Indexer
{
	/**
	 * Constant used for base directory containing rules files. 
	 */
	public static String RULES_DIR = "Generic";
	
	/**
	 * @param target The <code>LinkableElement</code> targeted by the indexation. 
	 */
	public GenericIndexer(Ontology target)
	{
		this.target = target;
		this.owner = target;
		this.rules = new ArrayList<Rule>();
		this.rules.add(new GenericRule(this));
	}

	/**
	 * @param owner The <code>Ontology</code> that possesses <code>target</code>. 
	 * @param target The <code>LinkableElement</code> targeted by the indexation. 
	 */
	public GenericIndexer(Ontology owner, LinkableElement target)
	{
		this.target = target;
		this.owner = owner;
		this.rules = new ArrayList<Rule>();
		this.rules.add(new GenericRule(this));
	}

	/**
	 * @throws Exception 
	 * @see arkeotek.indexing.Indexer#index()
	 */
	public void index() throws Exception
	{
		if (this.target.getCategoryKey() != Ontology.KEY)
			super.index();
		else {
			ArrayList<LinkableElement> documents = this.owner.get(DocumentPart.KEY);
			for (LinkableElement document : documents)
			{
				if (!(this.owner.getParentsOf(document, DocumentPart.KEY).isEmpty())) {
					this.target = document;
					super.index();
					this.target = this.owner;
				}
			}
		}
	}	
}