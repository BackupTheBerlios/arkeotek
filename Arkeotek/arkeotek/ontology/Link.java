/**
 * Created on 8 juil. 2005
 * 
 * Arkeotek Project
 */
package arkeotek.ontology;


/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class Link extends LinkableElement
{
	private int weighting;
	private Relation relation;
	private LinkableElement target;

	/**
	 * Variable statically initialized by calling the LinkableElement.obtainCategoryKey() method. 
	 * This variable is used to identify instances of this class with a simple <code>int</code>.  
	 */
	public static final int KEY;
	
	static {
		KEY = LinkableElement.obtainCategoryKey();
	}

	/**
	 * @param target The <code>LinkableElement</code> targeted by this <code>Link</code>. 
	 * 
	 */
	public Link(LinkableElement target)
	{
		this.target = target;
		this.relation = new Relation(Relation.DEFAULT_RELATION);
		this.state = LinkableElement.DEFAULT;
		this.weighting = 0;
	}

	/**
	 * @param target The <code>LinkableElement</code> targeted by this <code>Link</code>. 
	 * @param link_type The kind of <code>Relation</code> with target <code>LinkableElement</code>. 
	 * 
	 */
	public Link(LinkableElement target, Relation link_type)
	{
		this.target = target;
		this.relation = link_type;
		this.state = LinkableElement.DEFAULT;
		this.weighting = 0;
	}
	
	/**
	 * @param target The <code>LinkableElement</code> targeted by this <code>Link</code>. 
	 * @param link_type The kind of <code>Relation</code> with target <code>LinkableElement</code>. 
	 * @param weighting The weight of this <code>Link</code>. 
	 * @param state The state of this <code>Link</code>. 
	 */
	public Link(LinkableElement target, Relation link_type, int weighting, int state) {
		this.target = target;
		this.relation = link_type;
		this.weighting = weighting;
		this.state = state;
	}

    /**
     * @return The category key of this <code>IIndexable</code>. 
     * @see arkeotek.ontology.LinkableElement#getCategoryKey()
     */
    public int getCategoryKey() {
        return KEY;
    }

	/**
	 * Return the weighting value
	 * @return The weight of this <code>Link</code>.
	 */
	public int getWeighting()
	{
		return this.weighting;
	}

	/**
	 * Sets the weight of this <code>Link</code>.
	 * @param weighting The weight of this <code>Link</code>.
	 */
	public void setWeighting(int weighting)
	{
		this.weighting = weighting;
	}

	/**
	 * Return the relation
	 * @return The <code>Relation</code> of this <code>Link</code>.
	 */
	public Relation getRelation()
	{
		return this.relation;
	}

	/**
	 * Return the target
	 * @return The <code>LinkableElement</code> targeted by this <code>Link</code>.
	 */
	public LinkableElement getTarget()
	{
		return this.target;
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#toString()
	 */
	@Override
	public String toString()
	{
		return "[" + this.relation + " " + this.weighting + " "  + this.state + " "  + this.target + "]";
	}

}
