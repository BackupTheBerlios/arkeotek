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
	
	protected int type;
	
	/*
	 * Définition de tous les types de relations
	 */
	public static final int RELATION_INUTILE = -1;
	public static final int RELATION_CONCEPT_CONCEPT = 0;
	public static final int RELATION_CONCEPT_DOCUMENT = 1;
	
	public static final int RELATION_TERME_CONCEPT = 2;
	public static final int RELATION_TERME_DOCUMENT = 3;
	public static final int RELATION_TERME_TERME = 4;
	
	public static final int RELATION_DOCUMENT_DOCUMENT = 5;
	
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
    public Relation(int id, String name, int state)
    {
        super(id, name, state);
        type=RELATION_CONCEPT_CONCEPT;
    }
    
    public Relation(int id, String name, int state, int type)
    {
        super(id, name, state);
        this.type=type;
    }
    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(String)
     * @param name the name of the <code>Relation</code>. 
     */
    public Relation(String name)
    {
        super(name);
    }
    
    public Relation(String name, int type)
    {
        super(name);
        this.type=type;
        this.dirty=true;
    }
    
    public void setToDel()
    {
    	type=RELATION_INUTILE;
    	dirty=true;
    }
	/**
	 * @see arkeotek.ontology.LinkableElement#getCategoryKey()
	 */
	@Override
	public int getCategoryKey()
	{
		return KEY;
	}

	public int getType()
	{
		return type;
	}
}