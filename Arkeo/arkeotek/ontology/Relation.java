/**
 * Created on 7 juil. 2005
 * 
 * Arkeotek Project
 */
package arkeotek.ontology;



/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class Relation extends LinkableElement
{
	/**
	 * Default value for weighting attribute. 
	 */
	public static final int DEFAULT_WEIGHTING = 0;
	
	/**
	 * Default value for weighting attribute. 
	 */
	public static final int DEFAULT_STATE = 0;

	/**
	 * Variable statically initialized by calling the LinkableElement.obtainCategoryKey() method. 
	 * This variable is used to identify instances of this class with a simple <code>int</code>.  
	 */
	public static final int KEY;

	
	
	/**
	 * Default relation between documentParts and their super-paragraphs.  
	 */
	public static final String DEFAULT_DOCUMENT_DOWNGOING_RELATION = "englobe";
	
	/**
	 * Default relation between lemmas and documentParts
	 */
	public static final String DEFAULT_LEMMA_DOCUMENTPART_RELATION = "occurence";

	/**
	 * Default relation between lemmas and concepts
	 */
	public static final String DEFAULT_LEMMA_CONCEPT_RELATION = "caractérise";

	/**
	 * Default relation between two <code>LinkableElements</code>. 
	 */
	public static final String DEFAULT_RELATION = "";
	
	/**
	 * Default relation between two <code>Concepts</code>. 
	 */
	public static final String DEFAULT_CONCEPTS_RELATION = "généralise";
	
	static {
		KEY = obtainCategoryKey();
	}

    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(int, String, int)
     * @param id the id used to find this <code>LinkableElement</code> in the corpus. 
     * @param name the name of the <code>Relation</code>. 
     * @param state the state of the <code>Relation</code>. 
     */
    public Relation(int id, String name, int state) {
        super(id, name, state);
    }

    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(String)
     * @param name the name of the <code>Relation</code>. 
     */
    public Relation(String name) {
        super(name);
    }

	/**
	 * @see arkeotek.ontology.LinkableElement#getCategoryKey()
	 */
	@Override
	public int getCategoryKey()
	{
		return KEY;
	}
}