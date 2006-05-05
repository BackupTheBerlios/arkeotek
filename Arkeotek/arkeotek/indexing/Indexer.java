/**
 * Created on 27 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import arkeotek.io.importer.AbstractParser;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;



/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
// modif commit
public abstract class Indexer
{
	/**
	 * Global variable containing the directory where rules files are located. 
	 */
	public static final String INDEXING_DIR = "./datas/indexing/";
	
	/**
	 * Default relation between two <code>LinkableElements</code> due to an indexing operation. 
	 */
	public static final String DEFAULT_INDEXING_RELATION = "indexation";
	
	protected LinkableElement target;
	protected ArrayList<Rule> rules;
	protected Ontology owner;

	/**
	 * 
	 */
	public Indexer()
	{
		super();
	}

	/**
	 * @param owner The <code>Ontology</code> that possesses <code>target</code>. 
	 * @param target The <code>LinkableElement</code> targeted by the indexation. 
	 * @param path The path to the file containing the description of this <code>Indexer</code> comportement. 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	public Indexer(Ontology owner, LinkableElement target, String path) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException
	{
		String complete_path = AbstractParser.addSlash(INDEXING_DIR) + AbstractParser.addSlash(path);
		File directory = new File(complete_path);
		String[] rules_files = directory.list(new FilenameFilter()
		{
		
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".rule");
			}
		
		});
		this.rules = new ArrayList<Rule>(rules_files.length);

		for (String file_name : rules_files)
			this.rules.add(createRule(complete_path + file_name));

		this.target = target;
		this.owner = owner;
	}
	
	/**
	 * Performs the indexing actions. 
	 * @throws Exception 
	 */
	public void index() throws Exception {
		for (Rule rule : this.rules)
			rule.index(this.target);
	}

	/**
	 * @param file_name The system file name where the properties of a <code>Rule</code> are stored.
	 * @return The <code>Rule</code> corresponding to the specified file. 
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public Rule createRule(String file_name) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException {
		Rule new_rule;
		
		InputStream rules_stream = this.getClass().getClassLoader().getResourceAsStream(file_name);
		Properties properties = new Properties();
		properties.load(rules_stream);
		
		String value = properties.getProperty(Rule.RULE_KEY + "." + Rule.TYPE_KEY);

		new_rule = (Rule) Class.forName(value).newInstance();
		new_rule.init(this, file_name);
		
		return new_rule; 
	}

	/**
	 * @return The root <code>LinkableElement</code> for wich the index() method will be called.  
	 */
	public LinkableElement getTarget() {
		return this.target;
	}
	
	/**
	 * @return Returns the owner.
	 */
	public Ontology getOwner()
	{
		return this.owner;
	}
}