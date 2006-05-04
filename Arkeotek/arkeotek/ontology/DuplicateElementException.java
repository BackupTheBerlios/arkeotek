/**
 * Created on 19 août 2005
 * 
 * Arkeotek Project
 */
package arkeotek.ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class DuplicateElementException extends Exception
{

	/**
	 * 
	 */
	public DuplicateElementException()
	{
		super();
	}

	/**
	 * @param message
	 */
	public DuplicateElementException(String message)
	{
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DuplicateElementException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DuplicateElementException(Throwable cause)
	{
		super(cause);
	}
}
