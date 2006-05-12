/**
 * Created on 28 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing;

import arkeotek.ontology.LinkableElement;


/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class Action
{
	protected Rule owner;
	
	/**
	 * Exists only to allow usage of Class.forName(String).newInstance().
	 */
	public Action()
	{
		// Exists only to allow usage of Class.forName(String).newInstance().
	}

	/**
	 * @param owner The rule using this <code>Action</code>. 
	 */
	public Action(Rule owner) {
		this.owner = owner;
	}
	
	/**
	 * @return Returns the owner.
	 */
	public Rule getOwner()
	{
		return this.owner;
	}

	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(Rule owner)
	{
		this.owner = owner;
	}

	/**
	 * Performs a specific action, eventually based on the state of <code>owner</code>. 
	 * @param target The <code>LinkableElement</code> to be treated by this <code>Action</code>. 
	 * @throws Exception 
	 */
	public abstract void index(LinkableElement target) throws Exception;
}
