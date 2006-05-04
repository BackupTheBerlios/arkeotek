/*
 * Created on 4 mai 2005
 *
 * Arkeotek Project
 */
package arkeotek.ontology;

import arkeotek.indexing.Indexer;


/**
 * This interface declares the usual methods used on elements with indexes. 
 * All elements of the ontology have to implement this interface. 
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public interface IIndexable extends Comparable<IIndexable> {
	
	/**
     * Performs an indexation action on an element of the ontology, i.e. links it with other elements. 
	 * @param method The indexation method under the form of an <code>Indexer</code>. 
     * @return a potential result for this indexation with its relevance. 
     */
    public Result index(Indexer method);

	/**
     * Performs an indexation action on an element of the ontology, i.e. links it with other elements. 
	 * @param method The indexation method under the form of an <code>Indexer</code>. 
	 * @param target the element asking for indexation. 
     * @return a potential result for this indexation with its relevance. 
     */
    public Result index(Indexer method, IIndexable target);
    
	/**
	 * @return A <code>null</code> value if not dirty, an <code>Object</code> in other case.
	 * The kind of <code>Object</code> returned depends of the class that implements this method.  
	 */
	public Object isDirty();

	/**
	 * @return True if this <code>IIndexable</code>'s id is equal to -1, else returns false.
	 */
	public boolean isNew();

    /**
     * @return An <code>Object</code> as a key for the category of this element. 
     * In the facts, there is to be a key for each class implementing <code>IIndexable</code>. 
     */
    public int getCategoryKey();
	
	/**
	 * Sets the <code>dirty</code> state to false. This method has to be called after the saving of this object. 
	 */
	public void setSaved();
	
	/**
	 * Return the id
	 * @return the database id of this element.
	 */
	public int getId();

	/**
	 * Return the name
	 * @return the name of this element.
	 */
	public String getName();
	
	/**
	 * Sets the id if it has the <code>IndexableElement.NEW_ELEMENT_ID</code> value. 
	 * Else nothing happens. 
	 * @param id The id to set.
	 */
	public void setId(int id);

	/**
	 * @param name The name to set.
	 */
	public void setName(String name);
}
