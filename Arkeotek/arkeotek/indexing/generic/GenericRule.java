/**
 * Created on 11 août 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing.generic;

import java.lang.reflect.Constructor;

import arkeotek.indexing.Indexer;
import arkeotek.indexing.Rule;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class GenericRule extends Rule
{
	/**
	 * The name of the file describing the rules. 
	 */
	public static final String PROPERTIES_FILE = "scd.rule";

	/**
	 * Exists only to allow usage of Class.forName(String).newInstance().
	 */
	public GenericRule()
	{
		super();
	}

	/**
	 * @param owner The <code>Indexer</code> using this <code>Rule</code>.
	 */
	public GenericRule(Indexer owner)
	{
		this.owner = owner;
		this.category = DocumentPart.class;
		this.depth = 1;
		this.action = new GenericAction(this);
		this.propagate = true;
	}

	/**
	 * @see arkeotek.indexing.Rule#isCheckedBy(arkeotek.ontology.LinkableElement)
	 */
	@Override
	public boolean isCheckedBy(LinkableElement target)
	{
		if (target.getClass().equals(this.category))
				return true;
		return false;
	}

	/**
	 * Performs an action on specified <code>LinkableElement</code> 
	 * <b>if</b> the call of the method isCheckedBy() returns <code>true</code>.
	 * @param target
	 * @throws Exception 
	 */
	public void index(LinkableElement target) throws Exception {
		if (this.depth < 0)
			return;
		
		if (this.isCheckedBy(target)) {
			this.action.index(target);
			if (!this.propagate)
				return;
		}
		
		try {
			Constructor<LinkableElement> c = this.category.getConstructor(String.class);
			LinkableElement d = c.newInstance("TEMP");
			int ck = d.getCategoryKey();
			
			for (LinkableElement elem : target.getLinks(ck, new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION)))
				this.index(elem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see arkeotek.indexing.Rule#getFileName()
	 */
	@Override
	public String getFileName()
	{
		return PROPERTIES_FILE;
	}
}