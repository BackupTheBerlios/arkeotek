/**
 * Created on 27 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Properties;

import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class Rule
{
	/**
	 * Base property key for rules properties. 
	 */
	public static final String RULE_KEY = "rule";
	/**
	 * Property key indicating the kind of <code>Rule</code> to be created. 
	 */
	public static final String TYPE_KEY = "type";
	/**
	 * Property key indicating the kind of <code>Action</code> to be created. 
	 */
	public static final String ACTION_KEY = "action";
	/**
	 * Property key for "category" attribute. 
	 */
	public static final String CATEGORY_KEY = "category";
	/**
	 * Property key for "depth" attribute.
	 */
	public static final String DEPTH_KEY = "depth";
	
	protected Indexer owner;
	protected int depth;
	protected Class category;
	protected Action action;
	protected boolean propagate;
	

	/**
	 * Exists only to allow usage of Class.forName(String).newInstance().
	 */
	public Rule() {
		// Exists only to allow usage of Class.forName(String).newInstance().
	}
	
	/**
	 * @param owner The <code>Indexer</code> using this <code>Rule</code>.
	 * @param depth The maximum distance with the <code>LinkableElement</code> targeted by this <code>Rule</code>
	 * @param category The category key of <code>LinkableElements</code> concerned by this <code>Rule</code>; 
	 * @param action The <code>Action</code> to perform if the method isCheckedBy returns true. 
	 * @param propagate Indicates wether the Rule.index() method has to be called for linked <code>LinkableElements</code> when this.isCheckedBy(this.getOwner().getTarget()) returns false. <p>
	 * Set this paramter to <code>true</code> for a wide evaluation, as sub-elements will always be checked by the rule.  
	 */
	public Rule(Indexer owner, int depth, Class category, Action action, boolean propagate)
	{
		this.owner = owner;
		this.depth = depth;
		this.category = category;
		this.action = action;
		this.propagate = propagate;
	}

	/**
	 * @param owner The <code>Indexer</code> using this <code>Rule</code>.
	 * @param depth The maximum distance with the <code>LinkableElement</code> targeted by this <code>Rule</code>
	 * @param category The category key of <code>LinkableElements</code> concerned by this <code>Rule</code>; 
	 * @param action The <code>Action</code> to perform if the method isCheckedBy returns true. 
	 */
	public Rule(Indexer owner, int depth, Class category, Action action)
	{
		this.owner = owner;
		this.depth = depth;
		this.category = category;
		this.action = action;
		this.propagate = true;
	}

	/**
	 * @param owner The <code>Indexer</code> using this <code>Rule</code>. 
	 * @param file_name The path to the file containing this <code>Rule</code>'s properties. 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	public Rule(Indexer owner, String file_name) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException
	{
		this.init(owner, file_name);
	}
	
	/**
	 * Initialises the <code>Rule</code>. Used after a creation with Class.forName(String).newInstance().
	 * @param indexer The <code>Indexer</code> using this <code>Rule</code>. 
	 * @param file_name The path to the file containing this <code>Rule</code>'s properties. 
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public void init(Indexer indexer, String file_name) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException {
		this.owner = indexer;
		this.importDatas(file_name);
	}

	private void importDatas(String file_name) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException {
		InputStream rules_stream = this.getClass().getClassLoader().getResourceAsStream(file_name);
		Properties prop = new Properties();
		prop.load(rules_stream);
		
		this.depth = Integer.valueOf(prop.getProperty(RULE_KEY + "." + DEPTH_KEY));
		this.category = Class.forName(prop.getProperty(RULE_KEY + "." + CATEGORY_KEY));
		this.action = this.createAction(file_name);
	}
	
	/**
	 * Performs an action on specified <code>LinkableElement</code> 
	 * <b>if</b> the call of the method isCheckedBy() returns <code>true</code>.
	 * @param target
	 * @throws Exception 
	 */
	public void index(LinkableElement target) throws Exception {
		if (this.depth < 0)
			return;
		
		if (this.isCheckedBy(target)) {
			this.action.index(target);
			if (!this.propagate)
				return;
		}
		
		try {
			Constructor<LinkableElement> c = this.category.getConstructor(String.class);
			LinkableElement d = c.newInstance("TEMP");
			int ck = d.getCategoryKey();
			
			for (HashMap<LinkableElement, Link> array : target.getLinks(ck).values()) {
				if (!this.propagate)
					this.depth--;
				for (LinkableElement elem : array.keySet()) {
					this.index(elem);
				}
				if (!this.propagate)
					this.depth++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param target The <code>LinkableElement</code> whose conformity with this rule is to be checked. 
	 * @return Wether this rule is verified by current target <code>LinkableElement</code>. 
	 */
	public abstract boolean isCheckedBy(LinkableElement target);

	/**
	 * @param file_name The system file name where the properties of a <code>Rule</code> are stored. 
	 * @return The appropriate kind of Rule
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	public Action createAction(String file_name) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		InputStream rules_stream = this.getClass().getClassLoader().getResourceAsStream(file_name);
		Properties properties = new Properties();
		properties.load(rules_stream);
		
		String value = properties.getProperty(RULE_KEY + "." + ACTION_KEY);

		return (Action) Class.forName(value).newInstance();
	}
	
	/**
	 * @return The name of the file storing this <code>Rule</code>'s properties. 
	 */
	public abstract String getFileName();

	/**
	 * @return The <code>Class</code> concerned by this <code>Rule</code>. 
	 */
	public Class getCategory()
	{
		return this.category;
	}

	/**
	 * @return The <code>Indexer</code> that possesses this <code>Rule</code>.
	 */
	public Indexer getOwner()
	{
		return this.owner;
	}
}