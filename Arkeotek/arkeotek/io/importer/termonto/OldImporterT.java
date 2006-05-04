/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer.termonto;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Collections;
import java.util.HashMap;

import arkeotek.io.importer.engine.ConceptsParser;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DuplicateElementException;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * This class defines the standard methods for importing files such as Termonto and Syntex exports. 
 * @author Bernadou Pierre 
 * @author Czerny Jean
 */
public class OldImporterT extends arkeotek.io.importer.AbstractImporter
{
	private ConceptsParser concepts_parser = new ConceptsParser(this.owner, this.file, "CONCEPT.txt");
	
	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file_path The system path to the files to parse. 
	 * @throws IOException 
	 */
	public OldImporterT(Ontology owner, String file_path) throws IOException {
		super(owner, file_path);
		this.parsers.add(this.concepts_parser);
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
						this.innerTransitionTreatment(i, j);
						this.previous_result = this.current_result;
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
	@Override
	protected void transitionTreatment(int i)
	{
		// Nothing to do in this method for the Termonto Importer. 
	}

	@Override
	protected void treatResults(int i)
	{
		// Nothing to do in this method for the Termonto Importer. 
	}

	@Override
	protected void innerTreatment(int i, int j) throws Exception
	{
		if (this.current_result == null) return;
		
		if (i == this.parsers.indexOf(this.concepts_parser)) {
			Concept temp_concept;
			Concept temp_concept_cible;

			if (this.results.get(i).get(this.current_result[1]) == null)
			{
				temp_concept = new Concept((String) this.current_result[1]);
				this.owner.link(temp_concept);
				this.results.get(i).put(this.current_result[1], temp_concept);
			}
			else
				temp_concept = (Concept) this.results.get(i).get(this.current_result[1]);

			if (this.current_result[0] == null && this.current_result[2] == null)
				return;
			
			else if (this.current_result[2] == null && this.current_result[0] != null) {
				this.current_result[2] = new String((String) this.current_result[0]);
				this.current_result[0] = Relation.DEFAULT_CONCEPTS_RELATION;
			}
				
			else if (this.current_result[2].equals("C") || this.current_result[2].equals("R") || this.current_result[2].equals("CD")) {
				this.current_result[2] = new String((String) this.current_result[0]);
				this.current_result[0] = Relation.DEFAULT_CONCEPTS_RELATION;
			}
			
			// We check wether the read relation exists in the Ontology or not. 
			int position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation((String)this.current_result[0]));
			Relation temp_rel;
			// If not, we create it
			if (position < 0) {
				temp_rel = new Relation((String)this.current_result[0]);
				this.owner.link(temp_rel);
			}
			// If so, we retrieve it
			else temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);

			// If the second Concept does not exist yet as an Object, we create it. 
			if (this.results.get(i).get(this.current_result[2]) == null)
			{
				temp_concept_cible = new Concept((String)this.current_result[2]);
				this.owner.link(temp_concept_cible);
				this.results.get(i).put(this.current_result[2], temp_concept_cible);
				temp_concept_cible.link(temp_rel, temp_concept);
			} // Else we link the two Concepts.
			else 
			{
				temp_concept_cible = (Concept) this.results.get(i).get(this.current_result[2]);
				temp_concept_cible.link(temp_rel, temp_concept);
			}
		}
	}

	@Override
	protected void innerTransitionTreatment(int i, int j)
	{
		// Nothing to do in this method for the Termonto Importer. 
	}
	
	/**
	 * @throws Exception 
	 * @see arkeotek.io.importer.AbstractImporter#postTreat()
	 */
	@Override
	protected void postTreat() throws Exception
	{
		// Nothing to do. 
	}

	/**
	 * Retrieves the usual <code>Relations</code> encountered in Termonto's exports. 
	 * If these <code>Relations</code> do not exist yet in the <code>Ontology</code>, it creates them. 
	 * @throws DuplicateElementException 
	 * @see arkeotek.io.importer.AbstractImporter#preTreat()
	 */
	@Override
	protected void preTreat() throws DuplicateElementException
	{
		// Nothing to do. 
	}
}