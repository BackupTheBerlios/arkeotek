/**
 * Created on 26 juil. 2005
 * 
 * Arkeotek Project
 */
package arkeotek.ontology;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ResultSet
{
	protected ArrayList<Result> relevance_order;
	protected HashMap<IIndexable, Result> unicity_map;
	
	/**
	 * Defines the default size of the <code>ResultSet</code>. 
	 */
	public static final int DEFAULT_SIZE = 50;
	
	/**
	 * Creates a <code>ResultSet</code> with <code>DEFAULT_SIZE</code> capacity. 
	 * @see arkeotek.ontology.ResultSet#ResultSet(int)
	 */
	public ResultSet()
	{
		this(DEFAULT_SIZE);
	}

	/**
	 * @param capacity The initial capacity of the <code>ResultSet</code>. 
	 * 
	 */
	public ResultSet(int capacity)
	{
		this.relevance_order = new ArrayList<Result>(capacity);
		this.unicity_map = new HashMap<IIndexable, Result>(capacity);
	}

	/**
	 * Creates a copy of the specified <code>ResultSet</code>. 
	 * @param toCopy The <code>ResultSet</code> to copy. 
	 */
	public ResultSet(ResultSet toCopy) {
		this.relevance_order = new ArrayList<Result>(toCopy.relevance_order);
		this.unicity_map = new HashMap<IIndexable, Result>(toCopy.unicity_map);
	}
	
	/**
	 * @param index The position in the list.
	 * @return The <code>Result</code> at specified location. 
	 */
	public Result get(int index) {
		return this.relevance_order.get(index);
	}
	
	/**
	 * @param index The position in the list.
	 * @return The relevance of the <code>Result</code> at specified location.
	 */
	public int getRelevance(int index) {
		return this.get(index).getRelevance();
	}
	
	/**
	 * @param index The position in the list.
	 * @return The value targeted by the <code>Result</code> at specified location.
	 */
	public IIndexable getValue(int index) {
		return this.relevance_order.get(index).getValue();
	}
	
	/**
	 * @param element The element in the list.
	 * @return The relevance of the <code>Result</code> at specified location.
	 */
	public int getRelevance(IIndexable element) {
		if (this.unicity_map.get(element) != null)
			return this.unicity_map.get(element).getRelevance();
		return LinkableElement.DISTINCT;
	}
	
	/**
	 * @return The <code>Result</code> with the best <code>relevance</code> in the list. 
	 */
	public Result getBest() {
		try {
			return this.relevance_order.get(0);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * @return The <code>Result</code> with the worst <code>relevance</code> in the list. 
	 */
	public Result getWorst() {
		try {
			return this.relevance_order.get(this.relevance_order.size() - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * @param category_key The category key of elements linked with the <code>LinkableElements</code> concerned by the <code>Results</code>. 
	 * @return The <code>LinkableElements</code> of the specified category. 
	 */
	public ArrayList<LinkableElement> getRelations(int category_key) {
		ArrayList<LinkableElement> elements = new ArrayList<LinkableElement>();
		for (Result result : this.relevance_order) {
			if (result.getRelations(category_key) != null) {
				for (HashMap<LinkableElement, Link> links : result.getRelations(category_key).values())
					elements.addAll(links.keySet());
			}
		}
			
		return elements;
	}
	
	/**
	 * @return The size of the <code>ResultSet</code>. 
	 */
	public int size() {
		return this.relevance_order.size();
	}
	
	/**
	 * Places the target <code>IIndexable</code> in the linked list of <code>Results</code>, depending on its relevance.
	 * <p>
	 * <b>CAUTION : </b> if target has the same <code>value</code> than current <code>Result</code>, <code>target</code> wil not be added to the list : the <code>
	 * relevance will be raised by a factor of target.getRelevance(). 
	 * @param target The <code>Result</code> to add to the current list. 
	 */
	public void add(Result target) {
		// If a Result corresponding to the same target LinblableElement, we raise the current relevance
		if (this.unicity_map.containsKey(target.getValue()))
			target.raise(this.unicity_map.get(target.getValue()).getRelevance());
		// we add the result at the right position.
		this.unicity_map.put(target.getValue(), target);

		this.relevance_order.remove(target);

		int index = this.find(target);

		if (index == this.relevance_order.size())
			this.relevance_order.add(target);
		else this.relevance_order.add(index, target);		
	}

	/**
	 * @param target The set of <code>Results</code> to add. 
	 */
	public void add(ResultSet target) {
		for (Result result : target.relevance_order)
			this.add(result);
	}

	/**
	 * Removes the <code>Result</code> at specified <code>index</code> from the ordered list. 
	 * @param index The position in the list. 
	 */
	public void remove(int index) {
		this.unicity_map.remove(this.relevance_order.get(index).getValue());
		this.relevance_order.remove(index);
	}

	private int find(Result target) {
		for (int i = 0; i < this.relevance_order.size(); i++) {
			if (this.relevance_order.get(i).getRelevance() <= target.getRelevance())
				return i;
		}
		return this.relevance_order.size();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		String value = ""; 
		for (Result result : this.relevance_order)
			value += result;
		return value;
	}
}
