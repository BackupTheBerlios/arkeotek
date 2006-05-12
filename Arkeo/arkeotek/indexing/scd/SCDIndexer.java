/**
 * Created on 27 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.indexing.scd;

import java.util.ArrayList;

import arkeotek.indexing.Indexer;
import arkeotek.indexing.Rule;
import arkeotek.io.importer.syntex.OldImporter;
import arkeotek.io.importer.termonto.OldImporterT;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class SCDIndexer extends Indexer
{
	/**
	 * Constant used for base directory containing rules files. 
	 */
	public static String RULES_DIR = "SCD";
		
	/**
	 * @param target The <code>LinkableElement</code> targeted by the indexation. 
	 */
	public SCDIndexer(Ontology target)
	{
		this.target = target;
		this.owner = target;
		this.rules = new ArrayList<Rule>();
		this.rules.add(new SCDRule(this));
	}
	
	/**
	 * @param owner The <code>Ontology</code> that possesses <code>target</code>. 
	 * @param target The <code>LinkableElement</code> targeted by the indexation. 
	 */
	public SCDIndexer(Ontology owner, LinkableElement target)
	{
		this.target = target;
		this.owner = owner;
		this.rules = new ArrayList<Rule>();
		this.rules.add(new SCDRule(this));
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Ontology onto = new Ontology("Pipontologie");
	
		OldImporterT termonto = new OldImporterT(onto, "/home/altrazar/Travail/Termonto/");
		termonto.performImport();

		SCDIndexer indexer = new SCDIndexer(onto);
		indexer.index();
	}
}