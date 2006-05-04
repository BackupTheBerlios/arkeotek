/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import arkeotek.ontology.Ontology;

/**
 * This class defines the standard methods for parsing CSV files. 
 * @author Bernadou Pierre 
 * @author Czerny Jean
 */
public abstract class AbstractParser
{
	protected Ontology owner;
	protected File file;
	protected DataInputStream stream;
	protected LinkedList<String[]> values;
	public HashMap<Integer, String> usedColumns;
	public int columns = 0;
	public int current_col = 0;
	protected int totalColumnsNumber = 0;
	
	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file_path The system path to the Syntex file to parse. 
	 * @param retainedColumnHeaders The list of columns to parse. 
	 * @throws IOException 
	 */
	public AbstractParser(Ontology owner, String file_path, ArrayList<String> retainedColumnHeaders) throws IOException{
		this.owner = owner;
		this.file = new File(file_path);
		this.usedColumns = new HashMap<Integer, String>(retainedColumnHeaders.size());
		this.analyse(retainedColumnHeaders);
		this.values = new LinkedList<String[]>();
	}

	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file the <code>File</code> object representing the Syntex file to parse. 
	 * @param retainedColumnHeaders the list of columns to parse. 
	 * @throws IOException 
	 */
	public AbstractParser(Ontology owner, File file, ArrayList<String> retainedColumnHeaders) throws IOException{
		this.owner = owner;
		this.file = file;
		this.usedColumns = new HashMap<Integer, String>(retainedColumnHeaders.size());
		this.analyse(retainedColumnHeaders);
		this.values = new LinkedList<String[]>();
	}

	/**
	 * Parses the input file and stores its values in memory. 
	 * @return A <code>HashMap</code> with <code>Integers</code> as Syntex ids and <code>LinkableElements</code> as resulting objects.   
	 * @throws IOException 
	 */
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
	
	public DataInputStream getStream() throws FileNotFoundException {
		if (this.stream == null)
			this.stream = new DataInputStream(new FileInputStream(this.file));
		return this.stream;
	}
	
	/**
	 * Completes a path if its last character is not a separator. 
	 * @param path The system path that needs to be completed with a separator at its end. 
	 * @return The modified path. 
	 */
	public static String addSlash(String path) {
		if (!path.endsWith(File.separator))
			path += File.separator;
		return path;
	}
	
	/**
	 * Performs some treatment on a <code>String</code> value extracted from a line. Note that this method does not necessarily performs a treatment : it has been defined here for algorithmic reasons. 
	 * @param value the <code>String</code> value to treat. 
	 * @return An <code>Object</code> corresponding to the read value.
	 */
	public abstract Object treat(String value);
	
	/**
	 * Performs some treatment on the relevant values of a line. Note that this method does not necessarily performs a treatment : it has been defined here for algorithmic reasons. 
	 * @param line the <code>String</code> values retained from a line. 
	 * @return A couple with an <code>Integer</code> and <code>LinkableElement</code> as values.  
	 */
	public abstract Object[] treat(String[] line);	
}
