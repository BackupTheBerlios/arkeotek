/*
 * Created on 4 mai 2005
 *
 * Arkeotek Project
 */
package arkeotek.ontology;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.crypto.NoSuchPaddingException;

import ontologyEditor.Constants;
import arkeotek.io.EncryptedProperties;
import arkeotek.io.IService;
import arkeotek.io.importer.syntex.Importer;
import arkeotek.tools.TreeTagger;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class Ontology extends LinkableElement {
	
	/**
	 * Variable statically initialized by calling the LinkableElement.obtainCategoryKey() method. 
	 * This variable is used to identify instances of this class with a simple <code>int</code>.  
	 */
	public static final int KEY;
	
	private static final Relation NON_SENSE = new Relation("");
	
	private HashMap<Integer, ArrayList<LinkableElement>> elements = new HashMap<Integer, ArrayList<LinkableElement>>(5);
	
	private IService dataAccessor;
	
	private URL path;
    
	
	static {
		KEY = obtainCategoryKey();
	}
	
	/**
	 * Create a new <code>Ontology</code> with the specified name and from properties file located at url
	 * @param name
	 * @param url
	 */
    public Ontology(String name, URL url) {
		super(0, name, LinkableElement.DEFAULT);
		
		try {
			if (url == null)
			{
				File file = new File(Constants.DEFAULT_ONTOLOGIES_PATH);
				this.path = file.toURL();
			}
			else
				this.path = url;
			TreeTagger.init(this.path);
			this.importProperties();

			this.elements = this.dataAccessor.retrieveOntology();

			for (int ctg : this.elements.keySet())
				Collections.sort(this.elements.get(ctg));
			
		} catch (NullPointerException e) {
			// We simply ignore errors occuring while retrieving storage properties. 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e)
		{
			// 
		} catch (InstantiationException e)
		{
			// 
		} catch (IllegalAccessException e)
		{
			// 
		} catch (ClassNotFoundException e)
		{
			// 
		} catch (IOException e)
		{
			// 
		} catch (Exception e)
		{
			// 
		}
    }
	
	 /**
     * Creates a new <code>Ontology</code> with the specified name. 
     * @param name The name of the new Ontology. 
	 * @throws Exception 
     */
	public Ontology(String name) throws Exception
	{
		this(name, null);		
	}
    
    private void importProperties() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		EncryptedProperties properties = new EncryptedProperties();
		URL[] urls = {this.path};
		URLClassLoader classLoader = new URLClassLoader(urls);
		InputStream propertiesStream = classLoader.getResourceAsStream(this.toPropertiesName());
		properties.load(propertiesStream);

		// Retrieving the IService to use. 
		String value = properties.getProperty(EncryptedProperties.STORAGE_SERVICE);
		this.dataAccessor = (IService) Class.forName(value).newInstance();
		this.dataAccessor.setOwner(this);
	}

	
	/**
	 * @param categoryKey
	 * @return The <code>ArrayList</code> of <code>LinkableElements</code> owned by this <code>Ontology</code>. 
	 */
	public ArrayList<LinkableElement> get(int categoryKey) {
		if (this.elements.get(categoryKey) == null)
			this.elements.put(categoryKey, new ArrayList<LinkableElement>());
		return this.elements.get(categoryKey);
	}
	
	/**
	 * @return Returns the dataAccessor.
	 */
	public IService getDataAccessor()
	{
		return this.dataAccessor;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Generates a <code>LinkedList</code> with dirty <code>IIndexable</code> as values. If no dirty <code>IIndexable</code> is found in the ontology, then a <code>null</code> value is returned.
	 * @return A <code>LinkedList</code> of <code>IIndexable</code>. 
	 */
	public Object isDirty()
	{
		LinkedList<LinkableElement> dirtyElements = new LinkedList<LinkableElement>();
		
		for (int ctg : this.elements.keySet())
			for (LinkableElement elem : this.elements.get(ctg))
				if (elem.isDirty() != null)
					dirtyElements.add((LinkableElement) elem.isDirty());
		// For optimization reasons, we use a unique "for"
/*		for (int i = 0; i < Math.max(Math.max(Math.max(this.relations.size(), this.lemmas.size()), this.concepts.size()), this.documents.size()); i++) {
			if (i < this.lemmas.size()) {
				LinkableElement toSave = (LinkableElement) this.lemmas.get(i).isDirty();
				if (toSave != null)
					dirtyElements.add(toSave);
			}
			
			if (i < this.concepts.size()) {
				LinkableElement toSave = (LinkableElement) this.concepts.get(i).isDirty();
				if (toSave != null)
					 dirtyElements.add(toSave);
			}
			
			if (i < this.documents.size()) {
				LinkableElement toSave = (LinkableElement) this.documents.get(i).isDirty();
				if (toSave != null)
					 dirtyElements.add(toSave);
			}

			if (i < this.relations.size()) {
				LinkableElement toSave = (LinkableElement) this.relations.get(i).isDirty();
				if (toSave != null)
					 dirtyElements.add(toSave);
			}
		}
*/		
		return dirtyElements.isEmpty() ? null : dirtyElements;
	}

	/**
	 * @return The .properties file name associated to this ontology. 
	 */
	public String toPropertiesName()
	{
		return this.name + ".properties";
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#lookup(arkeotek.ontology.LinkableElement)
	 */
	public ResultSet lookup(LinkableElement target)
	{
		ArrayList<LinkableElement> list = this.elements.get(target.getCategoryKey());
		ResultSet matching_lemmas = new ResultSet();
		
		for (LinkableElement elem : list) {
			int relevance = elem.evaluate(target);
			if (relevance > LinkableElement.DISTINCT)
				matching_lemmas.add(new Result(relevance, elem));
		}
		
		ResultSet matching_concepts = new ResultSet();
		for (int i = 0; i < matching_lemmas.size(); i++) {
			LinkableElement matching_elem = matching_lemmas.get(i).getValue();
			for (HashMap<LinkableElement, Link> links : matching_elem.getLinks(Concept.KEY).values()) {
				for (LinkableElement concept : links.keySet())
					matching_concepts.add(concept.lookup(target));
			}
		}
		
		ResultSet matching_documents = new ResultSet();
		for (int i = 0; i < matching_concepts.size(); i++) {
			for (HashMap<LinkableElement, Link> docs : matching_concepts.get(i).getRelations(DocumentPart.KEY).values()) {
				for (LinkableElement doc : docs.keySet())
					matching_documents.add(new Result(matching_concepts.get(i).getRelevance(), doc));
			}
		}
		
		return matching_documents;
	}

	/**
	 * Breed the current ontology with texts parsed by Syntex
	 * @param dir_path The path to the directory where Syntex files are located. 
	 * @throws Exception 
	 *
	 */
	public void breed(String dir_path) throws Exception
	{
		Importer syntex = new Importer(this, dir_path);
		syntex.performImport();
	}
	
	/**
	 * Retrieves the parents of the specified category for the specified <code>LinkableElement</code>
	 * @param element The <code>LinkableElement</code> whose parents are to be found. 
	 * @param parent_category The category key indicating the wished type for the parents.  
	 * @return Parent <code>ArrayList</Code> of Object[relation, concepts] of the <code>LinkableElement</code>
	 */
	public ArrayList<Object[]> getParentsOf(LinkableElement element, int parent_category)
	{
		ArrayList<LinkableElement> elementsToCheck = new ArrayList<LinkableElement>(0);
		ArrayList<LinkableElement> originalElements = new ArrayList<LinkableElement>(0);
		ArrayList<Object[]> parentsToArray = new ArrayList<Object[]>();
		
		originalElements = this.elements.get(parent_category);
		
		if (parent_category == DocumentPart.KEY)
		{
			if (element instanceof DocumentPart)
			{	
				ArrayList<Relation> relations = new ArrayList<Relation>(element.getLinks(element.getCategoryKey()).keySet());
				Collections.sort(relations);
				for (Relation relation : relations)
				{	
					if (!relation.equals(new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION)))
					{
						originalElements = element.getLinks(DocumentPart.KEY, relation);
						for (LinkableElement document : originalElements)
						{
							Object[] elem = {relation, document};
							parentsToArray.add(elem);
						}
					}
				}
				return parentsToArray;
			}
		}
		
		elementsToCheck = (ArrayList<LinkableElement>) originalElements.clone();
		
		HashMap<LinkableElement, ArrayList<Relation>> parents = new HashMap<LinkableElement, ArrayList<Relation>>();
		for (LinkableElement toCheck : originalElements)
		{
			isChild(toCheck, element, elementsToCheck, parents);
		}
		for (LinkableElement toCheck : parents.keySet())
		{
			for (Relation relation : parents.get(toCheck))
			{
				Object[] elem = {relation, toCheck};
				parentsToArray.add(elem);
			}
		}
		return parentsToArray;
	}
	
	/**
	 * Retrieves the lemmas parent of the specified <code>LinkableElement</code>
	 * @param element The <code>LinkableElement</code> whose parents are to be found. 
	 * @return Parent <code>ArrayList</Code> of Object[relation, lemmas] of the <code>LinkableElement</code>
	 */
	public ArrayList<Object[]> getLemmasParents(LinkableElement element)
	{
		ArrayList<LinkableElement> lemmasToCheck = (ArrayList<LinkableElement>) this.elements.get(Lemma.KEY).clone();
		HashMap<LinkableElement, ArrayList<Relation>> parents = new HashMap<LinkableElement, ArrayList<Relation>>();
		ArrayList<Object[]> parentsToArray = new ArrayList<Object[]>();
		
		for (LinkableElement lemma : this.elements.get(Lemma.KEY))
		{
			isChild(lemma, element, lemmasToCheck, parents);
		}
		for (LinkableElement lemma : parents.keySet())
		{
			for (Relation relation : parents.get(lemma))
			{
				Object[] elem = {relation, lemma};
				parentsToArray.add(elem);
			}
		}
		return parentsToArray;
	}
		
	
	private void isChild(LinkableElement parent, LinkableElement child, ArrayList<LinkableElement> elementsToCheck, HashMap<LinkableElement, ArrayList<Relation>> parents)
	{
		if (elementsToCheck.contains(parent))
		{
			elementsToCheck.remove(parent);
			ArrayList<Relation> relation_links = parent.getLinks(child);
			if (relation_links.size() > 0)
			{
				parents.put(parent, new ArrayList<Relation>());
				for (Relation key : relation_links)
				{
					parents.get(parent).add(key);
				}			
				if (parent.getLinks(child.getCategoryKey()) != null)
				{
					Set<Relation> keys = parent.getLinks(child.getCategoryKey()).keySet();
					for (Relation key : keys)
					{
						for (Object element : parent.getLinks(child.getCategoryKey(), key))
							isChild((LinkableElement)element, child, elementsToCheck, parents);
					}
				}
			}
		}
	}
	
	/**
	 * Return all elements that reference <code>element</code>
	 * @param element the element referenced
	 * @return an ArrayList of LinkableElement
	 */
	public ArrayList<Object[]> getElementsThatReference(LinkableElement element)
	{
		HashMap<LinkableElement, ArrayList<Relation>> parents = new HashMap<LinkableElement, ArrayList<Relation>>();
		ArrayList<Object[]> parentsToArray = new ArrayList<Object[]>();
		
		ArrayList<LinkableElement> conceptsToCheck = (ArrayList<LinkableElement>) this.elements.get(Concept.KEY).clone();	
		for (LinkableElement elemRef : this.elements.get(Concept.KEY))
		{
			isChild(elemRef, element, conceptsToCheck, parents);
		}
		for (LinkableElement concept : parents.keySet())
		{
			for (Relation relation : parents.get(concept))
			{
				Object[] elem = {concept, relation};
				parentsToArray.add(elem);
			}
		}
		
		parents = new HashMap<LinkableElement, ArrayList<Relation>>();
		ArrayList<LinkableElement> lemmasToCheck = (ArrayList<LinkableElement>) this.elements.get(Lemma.KEY).clone();
		for (LinkableElement elemRef : this.elements.get(Lemma.KEY))
		{
			isChild(elemRef, element, lemmasToCheck, parents);
		}
		for (LinkableElement lemma : parents.keySet())
		{
			for (Relation relation : parents.get(lemma))
			{
				Object[] elem = {lemma, relation};
				parentsToArray.add(elem);
			}
		}
		
		parents = new HashMap<LinkableElement, ArrayList<Relation>>();
		ArrayList<LinkableElement> documentsToCheck = (ArrayList<LinkableElement>) this.elements.get(DocumentPart.KEY).clone();
		for (LinkableElement elemRef : this.elements.get(DocumentPart.KEY))
		{
			isChild(elemRef, element, documentsToCheck, parents);
		}
		for (LinkableElement document : parents.keySet())
		{
			for (Relation relation : parents.get(document))
			{
				Object[] elem = {document, relation};
				parentsToArray.add(elem);
			}
		}
		
		return parentsToArray;
	}
	
	/**
	 * This method determine if  the <code>element</code> has parent in its category
	 * @param element the element checked
	 * @return true if <code>element</code> has no parent in its category
	 */
	public boolean isRoot(LinkableElement element)
	{
		ArrayList<LinkableElement> elementsToCheck = this.get(element.getCategoryKey());	
		for (LinkableElement elemRef : elementsToCheck)
		{
			if (elemRef.getLinks(element).size() != 0)
				return false;
		}
		return true;
	}
	
	/**
	 * @see arkeotek.ontology.IIndexable#getCategoryKey()
	 */
	public int getCategoryKey()
	{
		return KEY;
	}
	
	/**
	 * Save elements of the ontology into the persistent layer
	 * @throws Exception 
	 */
	public void save() throws Exception
	{
		List<LinkableElement> elements_list = (List<LinkableElement>) this.isDirty();
		if (elements_list != null)
		{
			this.getDataAccessor().save(elements_list);
			this.setSaved();
		}
	}
	
	/**
	 * Delete <code>elementsToDelete</code> into the persistent layer
	 * @param elementsToDelete elements to save
	 * @throws Exception 
	 */
	public void delete(ArrayList<LinkableElement> elementsToDelete) throws Exception
	{
		this.getDataAccessor().delete(elementsToDelete);
	}

	/**
	 * Remove all existing links between the <code>LinkableElement</code> in parameter and
	 * each other <code>LinkableElement</code>
	 * @param element
	 * @throws Exception 
	 */
	public void unlinkElement(LinkableElement element) throws Exception
	{
		for (Object[] relation : this.getElementsThatReference(element))
		{
			((LinkableElement)relation[0]).unlink((Relation)relation[1], element);
		}
		this.unlink(element);
	}
	
	/**
	 * Sets the <code>dirty</code> state to false for each elements of the ontology.
	 * This method has to be called after the saving of the ontology. 
	 */
	public void setSaved()
	{
		for (int ctg : this.elements.keySet())
			for (LinkableElement elem : this.elements.get(ctg))
				elem.setSaved();
	}
	
	/**
	 * Add the relation <code>relation</code> between elements <code>source</code> and <code>target</code>
	 * @param source element source of the relation
	 * @param target element target of the relation
	 * @param relation the relation
	 * @throws Exception 
	 */
	public void addRelation(LinkableElement source, LinkableElement target, Relation relation) throws Exception
	{
		source.link(relation, target);
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#getLinks()
	 */
	public HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>> getLinks()
	{
		HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>> map = new HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>>(3);
		
		map.put(DocumentPart.KEY, new HashMap<Relation, HashMap<LinkableElement, Link>>());
		map.put(Concept.KEY, new HashMap<Relation, HashMap<LinkableElement, Link>>());
		map.put(Lemma.KEY, new HashMap<Relation, HashMap<LinkableElement, Link>>());
		map.put(Relation.KEY, new HashMap<Relation, HashMap<LinkableElement, Link>>());

		for (Integer category : this.elements.keySet()) {
			map.put(category, this.getLinks(category));
		}
			
		return map;
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#getLinks(java.lang.Integer)
	 */
	public HashMap<Relation, HashMap<LinkableElement, Link>> getLinks(Integer category_key)
	{
		HashMap<Relation, HashMap<LinkableElement, Link>> map = new HashMap<Relation, HashMap<LinkableElement, Link>>(3);
		
		if (this.elements.get(category_key) == null)
			this.elements.put(category_key, new ArrayList<LinkableElement>());
		
		for (LinkableElement elem : this.elements.get(category_key)) {
			for (HashMap<Relation, HashMap<LinkableElement, Link>> links : elem.getLinks().values()) {
				for (Relation rel : links.keySet()) {
					if (map.get(rel) == null)
						map.put(rel, new HashMap<LinkableElement, Link>(2));
					for (LinkableElement target : links.get(rel).keySet()) {
						map.get(rel).put(target, links.get(rel).get(target));
					}
				}
			}
		}

		return map;
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#link(arkeotek.ontology.Relation, arkeotek.ontology.LinkableElement, int, int)
	 * @return <code>null</code> as the links with the <code>Ontology</code> have no sense. 
	 * @throws DuplicateElementException 
	 */
	public Link link(Relation relation, LinkableElement target, int rel_weighting, int rel_state) throws DuplicateElementException
	{
//		try {
			int position = Collections.binarySearch(this.get(target.getCategoryKey()), target);
			if (position < 0)
				this.get(target.getCategoryKey()).add(-position - 1, target);
//		} catch (Exception e) {
			else throw new DuplicateElementException();
//		}
		
		return null;
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#link(arkeotek.ontology.Relation, arkeotek.ontology.LinkableElement)
	 * @return <code>null</code> as the links with the <code>Ontology</code> have no sense. 
	 * @throws DuplicateElementException 
	 */
	public Link link(Relation relation, LinkableElement target) throws DuplicateElementException
	{
		return this.link(relation, target, Relation.DEFAULT_WEIGHTING, Relation.DEFAULT_STATE);
	}
	
	/**
	 * @param target
	 * @return <code>null</code> as the links with the <code>Ontology</code> have no sense. 
	 * @throws DuplicateElementException 
	 */
	public Link link(LinkableElement target) throws DuplicateElementException {
		return this.link(NON_SENSE, target);
	}

	/**
	 * @return Returns the path.
	 */
	public URL getPath()
	{
		return this.path;
	}

	/**
	 * @throws Exception 
	 * @see arkeotek.ontology.LinkableElement#unlink(arkeotek.ontology.Link)
	 */
	public void unlink(Link link) throws Exception
	{
		this.elements.get(link.getTarget().getCategoryKey()).remove(link.getTarget());
		if (link.getTarget().getId() != LinkableElement.NEW_ELEMENT_ID)
			this.dataAccessor.delete(link.getTarget());
	}

	/**
	 * @throws Exception 
	 * @see arkeotek.ontology.LinkableElement#unlink(arkeotek.ontology.Relation, arkeotek.ontology.LinkableElement)
	 */
	public void unlink(Relation relation, LinkableElement target) throws Exception
	{
		this.unlink(new Link(target, relation));
	}
	
	/**
	 * @param target The <code>LinkableElement</code> to remove from the <code>Ontology</code>.
	 * @throws Exception 
	 */
	public void unlink(LinkableElement target) throws Exception {
		this.unlink(NON_SENSE, target);
	}

	
	
}