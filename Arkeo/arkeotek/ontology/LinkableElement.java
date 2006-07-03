/*
 * Created on 4 mai 2005
 *
 * Arkeotek Project
 */
package arkeotek.ontology;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import arkeotek.indexing.Indexer;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public abstract class LinkableElement implements IIndexable {
	
	private static String tabs = "";
	/**
	 * Constant used for new elements not yet in the base. 
	 */
	public static final int NEW_ELEMENT_ID = -1;
	
	/**
	 * Constant used for new elements not yet in the base. 
	 */
	public static final int TODEL_ELEMENT_ID = -2;
	
	/**
	 * Constant used to define if an element is ignored
	 */
	public static final int IGNORED = 1;
	
	/**
	 * Constant used to define if an element is validated
	 */
	public static final int VALIDATED = 2;
	
	/**
	 * Constant used to define the default state of elements
	 */
	public static final int DEFAULT = 0;
	
	/**
	 * Value returned by the evaluate(IIndexable) method. 
	 */
	public static final int SIMILAR = 3;

	/**
	 * Value returned by the evaluate(IIndexable) method. 
	 */
	public static final int CLOSE = 1;

	/**
	 * Value returned by the evaluate(IIndexable) method. 
	 */
	public static final int DISTINCT = 0;

	protected boolean dirty;
	protected int state;
	protected int id;
	protected String name;
	
	private HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>> links;
	
	/*
	 * This static variable is used in the static method obtainCategoryKey() to generate a new category identifier. 
	 */
	protected static int key_counter = 0;
	
	/**
	 * Exists only to allow usage of Class.forName(String).newInstance().
	 */
	public LinkableElement() {
		// Exists only to allow usage of Class.forName(String).newInstance().
	}
	
    /**
     * @param id The id of the element, if exists in database. 
     * @param name The name of the element. 
	 * @param state the state of the element
     */
    public LinkableElement(int id, String name, int state) {
		this.id = id;
		this.name = name;
		this.state = state;
		this.dirty = false;
		this.links = new HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>>();
	}
    
    /**
     * This constructor is to be used for only for new elements, as there are no accessors to the attributes. 
     * @param name The name of the element new <code>LinkableElement</code>. 
     */
    public LinkableElement(String name) {
		this.id = NEW_ELEMENT_ID;
		this.state = DEFAULT;
		this.name = name;
		this.dirty = true;
		this.links = new HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>>();
	}
    
	/**
	 * Add the <code>link</code> in parameter to this <code>LinkableElement</code>
	 * @param link The <code>Link</code> to add to this <code>LinkableElement</code>. 
	 * @return The created <code>Link</code>. 
	 * @throws Exception 
	 */
	public Link link(Link link) throws Exception {
		if (this.links.get(link.getTarget().getCategoryKey()) == null)
			this.links.put(link.getTarget().getCategoryKey(), new HashMap<Relation, HashMap<LinkableElement, Link>>());
		
		if (this.links.get(link.getTarget().getCategoryKey()).get(link.getRelation()) == null)
			this.links.get(link.getTarget().getCategoryKey()).put(link.getRelation(), new HashMap<LinkableElement, Link>());
		
		this.links.get(link.getTarget().getCategoryKey()).get(link.getRelation()).put(link.getTarget(), link);
		this.dirty = true;

		return link;
	}
	
	/**
	 * @param relation The <code>Relation</code> wich will link current and target <code>LinkableElements</code>. 
	 * @param target
	 * @return The created <code>Link</code>. 
	 * @throws Exception 
	 */
	public Link link(Relation relation, LinkableElement target) throws Exception {
		return this.link(new Link(target, relation));
	}
	
	/**
	 * @param relation The <code>Relation</code> wich will link current and target <code>LinkableElements</code>. 
	 * @param target
	 * @param weighting 
	 * @param relationState 
	 * @return The created <code>Link</code>. 
	 * @throws Exception 
	 */
	public Link link(Relation relation, LinkableElement target, int weighting, int relationState) throws Exception {
		return this.link(new Link(target, relation, weighting, relationState));
	}
	/**
	 * @param relation The <code>Relation</code> to break. 
	 * @param target The <code>LinkableElement</code> to bo unlinked with. 
	 * @throws Exception 
	 */
	public void unlink(Relation relation, LinkableElement target) throws Exception {
		if (this.links.get(target.getCategoryKey()) != null)
		{
			if (this.links.get(target.getCategoryKey()).get(relation) != null)
				this.links.get(target.getCategoryKey()).get(relation).remove(target);
			this.dirty = true;
		}
	}
	
	/**
	 * @param link The <code>Link</code> to break. 
	 * @throws Exception 
	 */
	public void unlink(Link link) throws Exception {
		if (this.links.get(link.getRelation()) != null)
			this.links.get(link.getRelation()).remove(link.getTarget());
		this.dirty = true;
	}
	
	/**
	 * @return All the <code>LinkableElements</code> whose link(s) with <code>this</code> have not been validated. 
	 */
	public ArrayList<LinkableElement> getNonValidatedRelations() {
		ArrayList<LinkableElement> results = new ArrayList<LinkableElement>();
		for (Integer category : this.links.keySet()) {
			for (Relation relation : this.links.get(category).keySet()) {
				for (LinkableElement element : this.links.get(category).get(relation).keySet()) {
					if (this.links.get(category).get(relation).get(element).getState() != VALIDATED)
						results.add(element);
				}
			}
		}
		return results;
	}
	
	/**
	 * Return the state
	 * @return The state of this <code>LinkableElement</code>. 
	 */
	public int getState() {
		return this.state;
	}
	
	/**
	 * @param relation The relation linking this element to <code>target</code> <code>LinkableElement</code>. 
	 * @param target The <code>LinkableElement</code> whose state of relation is requested. 
	 * @return The int value coding for the state of wished relation relation. 
	 */
	public int getState(Relation relation, LinkableElement target) {
		return this.links.get(target.getCategoryKey()).get(relation).get(target).getState();
	}

	/**
	 * @param state The state to set to this <code>LinkableElement</code>. 
	 */
	public void setState(int state) {
		if (this.state == state)
			return;
		this.dirty = true;
		this.state = state;
	}
	
	/**
	 * @param state The state to set to the relation with <code>target</code> <code>LinkableElement</code>. 
	 * @param relation The relation linked with this element and whose state is to be changed.
	 * @param target The <code>LinkableElement</code> whose relation state with current element has to be changed. 
	 */
	public void setState(int state, Relation relation, LinkableElement target) {
		this.links.get(target.getCategoryKey()).get(relation).get(target).setState(state);
		this.dirty = true;
	}

	
    /**
     * @return An <code>Object</code> as a key for the category of this element. 
     * In the facts, there is to be a key for each class extending <code>LinkableElement</code>. 
     */
    public abstract int getCategoryKey();
	
	/**
	 * This method generates a new category key wich will be used 
	 * @return a new category key. 
	 */
	protected static int obtainCategoryKey(){
		return key_counter++;
	}
	
	/**
	 * @see arkeotek.ontology.IIndexable#isDirty()
	 * @return <code>this</code> if is dirty, null in other case. 
	 */
	public Object isDirty()
	{
		return this.dirty ? this : null;
	}

	/**
	 * @see arkeotek.ontology.IIndexable#isDirty()
	 * @return True if this <code>IIndexable</code>'s id is equal to -1, else returns false.
	 */
	public boolean isNew()
	{
		return this.id == NEW_ELEMENT_ID;
	}

	
	/**
	 * @see arkeotek.ontology.IIndexable#getId()
	 */
	public int getId()
	{
		return this.id;
	}

	
	/**
	 * @see arkeotek.ontology.IIndexable#getName()
	 */
	public String getName()
	{
		return this.name;
	}

	
	/**
	 * @see arkeotek.ontology.IIndexable#setId(int)
	 */
	public void setId(int id)
	{
		if (this.id == NEW_ELEMENT_ID) {
			this.id = id;
			this.dirty = true;
		}
	}

	/**
	 * @see arkeotek.ontology.IIndexable#setName(java.lang.String)
	 */
	public void setName(String name)
	{
		if (!this.name.equals(name))
			this.dirty = true;
		this.name = name;
	}

	/**
	 * Sets the <code>dirty</code> state to false. This method has to be called after hte saving of this object.
	 * @see arkeotek.ontology.IIndexable#setSaved() 
	 */
	public void setSaved(){
		this.dirty = false;
	}
	
	/**
	 * The <code>String</code> returned is the name of this <code>LinkableElement</code>. 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.name;
	}

	/**
	 * @param category_key The key of the wished category. 
	 * @param relation The wished relation.
	 * @return The list of elements of the specified category linked to current object by the specified relation. 
	 */
	public ArrayList<LinkableElement> getLinks(Object category_key, Relation relation){
		if (this.links.get(category_key) != null && this.links.get(category_key).get(relation) != null)
			return new ArrayList<LinkableElement>(this.links.get(category_key).get(relation).keySet());
		return new ArrayList<LinkableElement>(1);
	}

	/**
	 * @param category_key the key of the wished category. 
	 * @return the list of elements of the specified category. 
	 */
	public HashMap<Relation, HashMap<LinkableElement, Link>> getLinks(Integer category_key){
		if (this.links.get(category_key) == null)
			this.links.put(category_key, new HashMap<Relation, HashMap<LinkableElement, Link>>());
		return this.links.get(category_key);
	}
		
	/**
	 * @param target The target <code>LinkableElement</code> whose links with <code>this</code> are to be scanned. 
	 * @return An <code>ArrayList</code> of <code>Relations</code> containing the relations that exist between the two elements. 
	 */
	public ArrayList<Relation> getLinks(LinkableElement target) {
		ArrayList<Relation> result = new ArrayList<Relation>();
		Relation[] array_type = new Relation[0];
		if (this.getLinks(target.getCategoryKey()) != null)
		{
			Collection<HashMap<LinkableElement, Link>> values = this.getLinks(target.getCategoryKey()).values();
			Collection<Relation> keys = this.getLinks(target.getCategoryKey()).keySet();
			
			Iterator<HashMap<LinkableElement, Link>> it = values.iterator();
			for (int i = 0; i < values.size(); i++) {
				HashMap<LinkableElement, Link> elementsLinks = it.next();
				if (elementsLinks.keySet().contains(target))
					result.add(keys.toArray(array_type)[i]);
			}
		}
		return result;
	}
	
	/**
	 * Return the relations
	 * @return the list of elements linked to the current object. 
	 */
	public HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>> getLinks(){
		return this.links;
	}
	
    /**
	 * @param method The indexation method under the form of an <code>Indexer</code>. 
     * @return A <code>Result</code> as a mark for the indexation with <code>this</code>. 
     * @see arkeotek.ontology.LinkableElement#index(Indexer, arkeotek.ontology.IIndexable)
     */
    public Result index(Indexer method) {
		return null;
    }

    /**
	 * @param method The indexation method under the form of an <code>Indexer</code>. 
     * @param target The element with whom indexation process will occur if appropriate. 
     * @return A <code>Result</code> as a mark for the indexation with <code>this</code>. 
     * @see arkeotek.ontology.LinkableElement#index(Indexer, arkeotek.ontology.IIndexable)
     */
    public Result index(Indexer method, IIndexable target) {
		return null;
    }

    /**
     * Performs a lookup action on an element of the ontology, i.e. evaluates the relevance of the target with the current object.
     * @param target the element whose relevance is to be evaluated. 
     * @return a result for this lookup, balanced with its relevance.
     */
    public ResultSet lookup(LinkableElement target) {
		ResultSet rs = new ResultSet();
		Result result = null;
		
		if (this.getCategoryKey() == target.getCategoryKey()) {
			result = new Result(this.evaluate(target), this);
			rs.add(result);
		}

		if (result != null && result.getRelevance() > 0 && this.getLinks(target.getCategoryKey()) != null) {
			for (HashMap<LinkableElement, Link> elementsLinks : this.getLinks(target.getCategoryKey()).values()) {
				for (LinkableElement link : elementsLinks.keySet()) {
					tabs += " ";
					rs.add(new Result(link.evaluate(target), target));
					tabs = tabs.substring(0, tabs.length() - 1);
				}
			}
		}

		return rs;
    }

	/**
	 * Compares the current element with <code>searchedElement</code>. 
	 * @param searchedElement The element to compare to. 
	 * @return An appreciation of the comparison between the two elements. 
	 */
	public int evaluate(IIndexable searchedElement) {
		String[] particles = this.getName().split(" ");
		String[] target_particles = searchedElement.getName().split(" ");
		int result = 0;
	
		for (String particle : particles) {
			for (String target_particle : target_particles) {
				if (particle.equals(target_particle) || target_particle.equals(particle))
					result += CLOSE;
			}
		}
		return result;
	}

	/**
	 * @param o The <code>IIndexable</code> to compare to. 
	 * @return <code>true</code> if the two <code>IIndexable</code> have the the same values. 
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(IIndexable o)
	{
		return this.getName().compareToIgnoreCase(o.toString());
	}

	/**
	 * @param o The <code>String</code> to compare to. 
	 * @return <code>true</code> if the two <code>String</code> have the the same values. 
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(String o)
	{
		return this.getName().compareToIgnoreCase(o.toString());
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof LinkableElement ? this.getName().equals(((LinkableElement) obj).getName()) : false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return this.getName().hashCode();
	}
}