/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer.engine;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;

import arkeotek.io.importer.AbstractParser;
import arkeotek.ontology.Ontology;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ConceptsParser extends AbstractParser
{

	private static final ArrayList<String> retainedColumnHeaders;
	
	/**
	 * Concepts' column name in file
	 */
	public static final String COL_CONCEPT = "concept";
	/**
	 * Source concepts' column name in file
	 */
	public static final String COL_CONCEPT_SOURCE = "concept source";
	/**
	 * Relatons' column name in file
	 */
	public static final String COL_RELATION = "relation";
	/**
	 * Target concepts' column name in file
	 */
	public static final String COL_CONCEPT_CIBLE = "concept cible";
	
	static{
		retainedColumnHeaders = new ArrayList<String>(4);
		retainedColumnHeaders.add(COL_CONCEPT);
		retainedColumnHeaders.add(COL_CONCEPT_SOURCE);
		retainedColumnHeaders.add(COL_RELATION);
		retainedColumnHeaders.add(COL_CONCEPT_CIBLE);
	}
	
	/**
	 * @param owner The ontology for which this parser will work.
	 * @param dir_path The system path to the csv file to parse.
	 * @param file_name The name of the file to parse. 
	 * @throws IOException
	 */
	public ConceptsParser(Ontology owner, String dir_path, String file_name) throws IOException
	{
		super(owner, dir_path + file_name, retainedColumnHeaders);
	}
	
	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file The <code>File</code> object representing the csv file to parse.
	 * @throws IOException
	 */
	public ConceptsParser(Ontology owner, File file) throws IOException
	{
		super(owner, file, retainedColumnHeaders);
	}

	/**
	 * Locates the positions of the columns whose headers are specified in <code>retainedColumnHeaders</code>
	 * @param retainedColumnHeaders the list of columns to parse. 
	 * @throws IOException
	 */
	private void analyse(ArrayList retainedColumnHeaders) throws IOException{
		DataInputStream temp_stream = this.getStream();
		String concat = "";
		int character = temp_stream.read();

		while (character != '\r' && character != '\n' && character != -1){
			if (character == '\t'){
				if (retainedColumnHeaders.contains(concat))
					this.usedColumns.put(this.totalColumnsNumber, concat);
				this.totalColumnsNumber++;

				concat = "";
			} else concat += (char) character;
			character = temp_stream.read();
		}
		
		if (retainedColumnHeaders.contains(concat))
			this.usedColumns.put(this.totalColumnsNumber, concat);
		this.totalColumnsNumber++;
		
		this.columns = this.usedColumns.size();
		
		this.stream.close();
		this.stream = new DataInputStream(new FileInputStream(this.file));
	}
	
	@Override
	public HashMap<Object, Object> parse() throws IOException{
		String value = "";
		String[] line = new String[this.columns];
		HashMap<Object, Object> file_result = new HashMap<Object, Object>();
		Object[] line_result = new Object[2];
	
		int token, i = 0;
		InputStreamReader reader = new InputStreamReader(this.getStream());
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.eolIsSignificant(true);
		tokenizer.ordinaryChars(33, 64);
		tokenizer.wordChars(33, 64);
		tokenizer.whitespaceChars(9, 10);
		tokenizer.whitespaceChars(13, 13);
		
		while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
			if (this.usedColumns.get(this.current_col++) != null && token == StreamTokenizer.TT_WORD) {
				value = tokenizer.sval;
				line[i++] = value;
				this.treat(value);
			}
				
			if (token == StreamTokenizer.TT_EOL) {
				this.current_col = 0;
				if (this.usedColumns.get(this.current_col++) != null) {
					value = tokenizer.sval;
					line[i++] = value;
					this.treat(value);
					this.treat(line);
					file_result.put(line_result[0], line_result[1]);
				}
			}
		}

		return file_result;
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
		Object[] elementRepresentation = new Object[4];
		elementRepresentation[0] = line[retainedColumnHeaders.indexOf(COL_CONCEPT)];
		elementRepresentation[1] = line[retainedColumnHeaders.indexOf(COL_CONCEPT_SOURCE)];
		elementRepresentation[2] = line[retainedColumnHeaders.indexOf(COL_RELATION)];
		elementRepresentation[3] = line[retainedColumnHeaders.indexOf(COL_CONCEPT_CIBLE)];
		return elementRepresentation;
	}
}
