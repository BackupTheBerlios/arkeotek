/**
 * Created on 29 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing.scd;

import arkeotek.indexing.Indexer;
import arkeotek.indexing.Rule;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class SCDRule extends Rule
{
	/**
	 * The name of the file describing the rules. 
	 */
	public static final String PROPERTIES_FILE = "scd.rule";

	/**
	 * Exists only to allow usage of Class.forName(String).newInstance().
	 */
	public SCDRule() {
		super();
	}

	/**
	 * @param owner The <code>Indexer</code> using this <code>Rule</code>.
	 */
	public SCDRule(Indexer owner)
	{
		this.owner = owner;
		this.category = DocumentPart.class;
		this.depth = 1;
		this.action = new SCDAction(this);
	}

	/**
	 * @see arkeotek.indexing.Rule#isCheckedBy(arkeotek.ontology.LinkableElement)
	 */
	@Override
	public boolean isCheckedBy(LinkableElement target)
	{
		if (target.getClass().equals(this.category))
			if (target.toString().contains("PROPOSITION"))
				return true;
		return false;
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
