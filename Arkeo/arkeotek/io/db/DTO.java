/**
 * Created on 4 mai 2005
 * 
 * Project Arkeotek
 * Authors: Bernadou Pierre - Czerny Jean
 */
package arkeotek.io.db;

import java.io.Serializable;

import arkeotek.ontology.IIndexable;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class DTO implements Serializable
{
	
	private Transaction transaction;
	private IIndexable element;
	
	/**
	 * @param transaction
	 * @param element
	 */
	public DTO(Transaction transaction, IIndexable element)
	{
		super();
		// TODO Auto-generated constructor stub
		this.transaction = transaction;
		this.element = element;
	}
	
	/**
	 * @return Returns the element.
	 */
	public IIndexable getElement()
	{
		return this.element;
	}
	/**
	 * @param element The element to set.
	 */
	public void setElement(IIndexable element)
	{
		this.element = element;
	}
	/**
	 * @return Returns the transaction.
	 */
	public Transaction getTransaction()
	{
		return this.transaction;
	}
	/**
	 * @param transaction The transaction to set.
	 */
	public void setTransaction(Transaction transaction)
	{
		this.transaction = transaction;
	}	
}
