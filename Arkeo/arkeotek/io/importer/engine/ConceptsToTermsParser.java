/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer.engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import arkeotek.io.importer.AbstractParser;
import arkeotek.ontology.Ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ConceptsToTermsParser extends AbstractParser
{
	private static final ArrayList<String> retainedColumnHeaders;
	/**
	 * Concepts' column name in file
	 */
	public static final String COL_CONCEPT = "concept";
	/**
	 * Lemmas indexes' colum name in file
	 */
	public static final String COL_NUM_TERM = "num_terme";
	
	static{
		retainedColumnHeaders = new ArrayList<String>(2);
		retainedColumnHeaders.add(COL_CONCEPT);
		retainedColumnHeaders.add(COL_NUM_TERM);
	}
	
	/**
	 * @param owner The ontology for which this parser will work.
	 * @param dir_path The system path to the csv file to parse.
	 * @param file_name The name of the file to parse. 
	 * @throws IOException
	 */
	public ConceptsToTermsParser(Ontology owner, String dir_path, String file_name) throws IOException
	{
		super(owner, dir_path + file_name, retainedColumnHeaders);
	}
	
	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file The <code>File</code> object representing the csv file to parse.
	 * @throws IOException
	 */
	public ConceptsToTermsParser(Ontology owner, File file) throws IOException
	{
		super(owner, file, retainedColumnHeaders);
	}

	/**
	 * @see arkeotek.io.importer.AbstractParser#treat(java.lang.String)
	 */
	public Object treat(String value)
	{
//		 Nothing to do in this subclass of AbstractImporter2.
		return value;
	}

	/**
	 * @see arkeotek.io.importer.AbstractParser#treat(java.lang.String[])
	 */
	public Object[] treat(String[] line)
	{
		Object[] elementRepresentation = new Object[2];
		elementRepresentation[0] = line[retainedColumnHeaders.indexOf(COL_CONCEPT)];
		elementRepresentation[1] = line[retainedColumnHeaders.indexOf(COL_NUM_TERM)];
		return elementRepresentation;
	}

}
