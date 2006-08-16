/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer;

import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;

import arkeotek.ontology.Ontology;

/**
 * This class defines the standard methods for parsing CSV files. 
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class AbstractImporter
{
	protected Ontology owner;
	protected String file;
	protected ArrayList<AbstractParser> parsers;
	protected HashMap<Integer, HashMap<Object, Object>> results;
	protected Object[] current_result = new Object[2];
	protected Object[] previous_result = new Object[2];

	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file_path The system path to the Syntex file to parse. 
	 */
	public AbstractImporter(Ontology owner, String file_path) {
		this.owner = owner;
		this.file = file_path;
		this.parsers = new ArrayList<AbstractParser>();
	}

	/**
	 * Imports datas combining the <code>AbstractParsers</code>. 
	 * @throws Exception 
	 */
	public void performImport() throws Exception {
		this.results = new HashMap<Integer, HashMap<Object, Object>>(this.parsers.size());
		
		this.preTreat();
		if (this.parsers != null && !this.parsers.isEmpty() ) {
			for (int i = 0; i < this.parsers.size(); i++) {
				String value = "";
				String[] line = new String[this.parsers.get(i).columns];
				int token, word = 0, j = 0;
				InputStreamReader reader = new InputStreamReader(this.parsers.get(i).getStream());
				StreamTokenizer tokenizer = new StreamTokenizer(reader);
				tokenizer.resetSyntax();
				tokenizer.eolIsSignificant(true);
				tokenizer.wordChars(32, 255);
				tokenizer.whitespaceChars(9, 9);
				this.results.put(i, new HashMap<Object, Object>());
				this.transitionTreatment(i);
				// We skip the columns' headers line
				while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOL) { /* Just skipping */ }
				while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
					if (this.parsers.get(i).usedColumns.get(this.parsers.get(i).current_col++) != null && token == StreamTokenizer.TT_WORD) {
						value = tokenizer.sval;
						line[word++] = value;
						this.parsers.get(i).treat(value);
					}
						
					if (token == StreamTokenizer.TT_EOL) {
						word = 0;
						//this.innerTransitionTreatment(i, j);
						//this.previous_result = this.current_result;
						this.parsers.get(i).current_col = 0;
						this.current_result = this.parsers.get(i).treat(line);
						this.innerTransitionTreatment(i, j);
						this.previous_result = this.current_result;
						this.innerTreatment(i, j++);
					}
				}
			}
		}
		this.postTreat();
	}

	protected abstract void postTreat() throws Exception;

	protected abstract void preTreat() throws Exception;

	/**
	 * Performs the treatment for the <code>j</code>th result of the <code>i</code>th iteration's of the import method. 
	 * @param i The current iteration indicating the treatment to perform. 
	 * @param j The current importing situation in the iteration indicating the treatment to perform.
	 * @throws Exception 
	 */
	protected abstract void innerTreatment(int i, int j) throws Exception;

	/**
	 * Performs the treatment between the <code>j</code>th and the <code>(j+1)</code>th result of the <code>i</code>th iteration of the import method. 
	 * <b>Note that</b> innerTransitionTreatment(0) will surely be empty. 
	 * @param i The current iteration indicating the treatment to perform. 
	 * @param j The current importing situation in the iteration indicating the treatment to perform. 
	 * @throws Exception 
	 */
	protected abstract void innerTransitionTreatment(int i, int j) throws Exception;

	/**
	 * Performs the treatment between the <code>i</code>th and the <code>(i+1)</code>th iterations of the import method. 
	 * <b>Note that</b> transitionTreatment(0) will surely be empty. 
	 * @param i The current iteration indicating the treatment to perform. 
	 */
	protected abstract void transitionTreatment(int i);

	/**
	 * Performs the treatment for the <code>i</code>th iteration's results of the import method. 
	 * @param i The current iteration indicating the treatment to perform. 
	 */
	protected abstract void treatResults(int i);
}
