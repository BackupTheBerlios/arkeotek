/**
 * Created on 4 mai 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public interface IService
{
	/**
	 * Save the object in parameter into the persistent layer
	 * @param object object to save
	 * @throws Exception 
	 */
	public void save(LinkableElement object) throws Exception;
	
	/**
	 * Save objects in parameter into the persistent layer
	 * @param list list of objects to save
	 * @throws Exception 
	 */
	public void save(List<LinkableElement> list) throws Exception;
	
	/**
	 * Delete the object in parameter into the persistent layer
	 * @param object
	 * @throws Exception 
	 */
	public void delete(LinkableElement object) throws Exception;
	
	/**
	 * Delete objects in parameter into the persistent layer
	 * @param list list of objects to delete
	 * @throws Exception 
	 */
	public void delete(ArrayList<LinkableElement> list) throws Exception;
	
	/**
	 * @return A <code>HashMap</code> with <code>IIndexable</code> category keys as keys and the <code>IIndexables</code> thenmselves as elements. 
	 * @throws Exception 
	 */
	public HashMap<Integer, ArrayList<LinkableElement>> retrieveOntology() throws Exception;

	/**
	 * @return The owner ontology to whom this <code>Service</code> is dedicated.
	 */
	public Ontology getOwner();
	
	/**
	 * @param owner The <code>Ontology</code> to be set as owner of this <code>IService</code>. 
	 */
	public void setOwner(Ontology owner);
}
