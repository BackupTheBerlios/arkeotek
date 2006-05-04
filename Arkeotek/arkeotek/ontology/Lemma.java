/*
 * Created on 4 mai 2005
 *
 * Arkeotek Project
 */
package arkeotek.ontology;


/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class Lemma extends LinkableElement {

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
     * @param id the id used to find this <code>LinkableElement<code> in the corpus. 
     * @param name the name of the <code>Lemma</code>. 
     * @param state the state of <code>Lemma</code>
     */
    public Lemma(int id, String name, int state) {
        super(id, name, state);
    }

    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(String)
     * @param name the name of the <code>Lemma</code>. 
     */
    public Lemma(String name) {
        super(name);
    }

    /**
     * @see arkeotek.ontology.LinkableElement#getCategoryKey()
     */
    public int getCategoryKey() {
        return KEY;
    }
}
