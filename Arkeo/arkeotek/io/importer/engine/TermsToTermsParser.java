/**
 * Created on 14 juin 2005
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
public class TermsToTermsParser extends AbstractParser
{
	private static final ArrayList<String> retainedColumnHeaders;
	/**
	 * Column name of first lemmas ids in the file
	 */
	public static final String COL_NUM1 = "num1";
	/**
	 * Column name of second lemmas ids in the file
	 */
	public static final String COL_NUM2 = "num2";
	/**
	 * Column name of relation name between the two lemmas
	 */
	public static final String COL_REL = "rel";
	
	static{
		retainedColumnHeaders = new ArrayList<String>(3);
		retainedColumnHeaders.add(COL_NUM1);
		retainedColumnHeaders.add(COL_NUM2);
		retainedColumnHeaders.add(COL_REL);
	}
	
	/**
	 * @param owner The ontology for which this parser will work.
	 * @param dir_path The system path to the csv file to parse.
	 * @param file_name The name of the file to parse. 
	 * @throws IOException
	 */
	public TermsToTermsParser(Ontology owner, String dir_path, String file_name) throws IOException
	{
		super(owner, dir_path + file_name, retainedColumnHeaders);
	}
	
	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file The <code>File</code> object representing the text file to parse.
	 * @throws IOException
	 */
	public TermsToTermsParser(Ontology owner, File file) throws IOException
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
		Object[] elementRepresentation = new Object[3];
		elementRepresentation[0] = line[retainedColumnHeaders.indexOf(COL_NUM1)];
		elementRepresentation[1] = line[retainedColumnHeaders.indexOf(COL_NUM2)];
		elementRepresentation[2] = line[retainedColumnHeaders.indexOf(COL_REL)];
		return elementRepresentation;
	}

}
