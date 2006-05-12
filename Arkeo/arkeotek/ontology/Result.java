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
public class Result implements Comparable<Result> {
    private int relevance;
    private LinkableElement value;
    
    /**
     * @param value the target element corresponding to this <code>Result</code>. 
     */
    public Result(LinkableElement value) {
        this.relevance = 0;
        this.value = value;
    }
    
    /**
     * This constructor is not supposed to be used very often : the standard usage of the <code>Result</code> class is to use the other constructor and the alter the relevance with the appropriate methods during the processing. 
     * @param relevance the initial relevance of this <code>Result</code>. 
     * @param value the target element corresponding to this <code>Result</code>. 
     */
    public Result(int relevance, LinkableElement value) {
        this.relevance = relevance;
        this.value = value;
    }
    
    /**
     * This methods lowers the relevance of the <code>Result</code> with the default factor. 
     * @return the new relevance value of this <code>Result</code>. 
     */
    public int lower() {
        return --this.relevance;
    }
    
    /**
     * This methods lowers the relevance of the <code>Result</code> with a certain <code>factor</code>. 
     * @param factor the value to lower the relevance with. 
     * @return the new relevance value of this <code>Result</code>. 
     */
    public int lower(int factor) {
        return this.relevance - factor;
    }
    
    /**
     * This methods raises the relevance of the <code>Result</code> with the default factor. 
     * @return the new relevance value of this <code>Result</code>. 
     */
    public int raise() {
        return ++this.relevance;
    }
    
    /**
     * This methods raises the relevance of the <code>Result</code> with a certain <code>factor</code>. 
     * @param factor the value to raise the relevance with. 
     * @return the new relevance value of this <code>Result</code>. 
     */
    public int raise(int factor) {
        return this.relevance = this.relevance + factor;
    }
    
	/**
	 * @return Returns the relevance.
	 */
	public int getRelevance()
	{
		return this.relevance;
	}
	
	/**
	 * @return Returns the value.
	 */
	public LinkableElement getValue()
	{
		return this.value;
	}

	/**
	 * @param category_key The category key of elements linked with the <code>LinkableElement</code> concerned by this <code>Result</code>. 
	 * @return The <code>LinkableElements</code> of the specified category. 
	 */
	public HashMap<Relation, HashMap<LinkableElement, Link>> getRelations(int category_key) {
		return this.getValue().getLinks(category_key);
	}
	
	/**
	 * @param target The <code>Result</code> to compare with. 
	 * @return The difference between the current result's relevance and the target's one. 
	 */
	public int compareTo(Result target) {
		return this.relevance - target.getRelevance();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.getValue() + " (" + this.relevance + ")\n"; 
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Result ? this.getValue().equals(((Result) obj).getValue()) : false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.getValue().hashCode();
	}
}