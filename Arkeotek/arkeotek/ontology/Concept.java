/*
 * Created on 4 mai 2005
 *
 * Arkeotek Project
 */
package arkeotek.ontology;

import java.util.HashMap;


/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class Concept extends LinkableElement {

	/**
	 * Variable statically initialized by calling the LinkableElement.obtainCategoryKey() method. 
	 * This variable is used to identify instances of this class with a simple <code>int</code>.  
	 */
	public static final int KEY;
	
	static {
		KEY = obtainCategoryKey();
	}

    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(int, String, int)
     * @param id the id used to find this <code>LinkableElement</code> in the corpus. 
     * @param name the name of the <code>Concept</code>. 
     * @param state the state of the <code>Concept</code>. 
     */
    public Concept(int id, String name, int state) {
        super(id, name, state);
    }

    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(String)
     * @param name the name of the <code>Concept</code>. 
     */
    public Concept(String name) {
        super(name);
    }

    /**
     * @see arkeotek.ontology.LinkableElement#getCategoryKey()
     */
    public int getCategoryKey() {
        return KEY;
    }

	/**
	 * @see arkeotek.ontology.LinkableElement#lookup(arkeotek.ontology.LinkableElement)
	 */
	public ResultSet lookup(LinkableElement target)
	{
		ResultSet rs = new ResultSet();
		int relevance = super.evaluate(target);
		
		for (HashMap<LinkableElement, Link> links : this.getLinks(Lemma.KEY).values()) {
			for (LinkableElement lemma : links.keySet())
				relevance += lemma.evaluate(target);
		}
		rs.add(new Result(relevance, this));
		
		return rs;
	}
}
