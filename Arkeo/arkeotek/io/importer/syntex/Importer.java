/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer.syntex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import arkeotek.io.importer.engine.DocumentsParser;
import arkeotek.io.importer.engine.DocumentsToLemmasParser;
import arkeotek.io.importer.engine.TermsParser;
import arkeotek.io.importer.engine.TermsToTermsParser;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.DuplicateElementException;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * This class defines the standard methods for importing files such as Termonto and Syntex exports. 
 * @author Bernadou Pierre 
 * @author Czerny Jean
 */
public class Importer extends arkeotek.io.importer.AbstractImporter
{
	private DocumentsParser documents_parser = new DocumentsParser(this.owner, this.file, "syntex_seq.txt");
	private TermsParser terms_parser = new TermsParser(this.owner, this.file, "syntex_liste.txt");
	private DocumentsToLemmasParser docs2lemmas_parser = new DocumentsToLemmasParser(this.owner, this.file, "syntex_occ.txt");
	private TermsToTermsParser terms2terms_parser = new TermsToTermsParser(this.owner, this.file, "syntex_dep.txt");
	
	/**
	 * @param owner The ontology for wich this parser will work.  
	 * @param file_path The system path to the files to parse. 
	 * @throws IOException 
	 */
	public Importer(Ontology owner, String file_path) throws IOException {
		super(owner, file_path);
		this.parsers.add(this.documents_parser);
		this.parsers.add(this.terms_parser);
		this.parsers.add(this.docs2lemmas_parser);		
		this.parsers.add(this.terms2terms_parser);
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
		
		if (i == this.parsers.indexOf(this.documents_parser)) {
			if (this.results.get(i).get(this.current_result[0]) == null)
			{
				DocumentPart element = new DocumentPart((String) this.current_result[0], (String) this.current_result[1]);
				this.results.get(i).put(this.current_result[0], element);
				this.owner.link(element);
			} else {
				((DocumentPart) this.results.get(i).get(this.current_result[0])).setValue(((DocumentPart) this.results.get(i).get(this.current_result[0])).getValue().concat(" " + (String) this.current_result[1]));
				if ((((DocumentPart) this.results.get(i).get(this.current_result[0])).getValue().charAt(0)) == '.')
					((DocumentPart) this.results.get(i).get(this.current_result[0])).setValue(((DocumentPart) this.results.get(i).get(this.current_result[0])).getValue().substring(1));
			}
			
		} else if (i == this.parsers.indexOf(this.terms_parser)) {
			int pos = Collections.binarySearch(this.owner.get(Lemma.KEY), new Lemma((String) this.current_result[1]));
			LinkableElement temp_lemma;
			if (pos > 0) {
				temp_lemma = this.owner.get(Lemma.KEY).get(pos);
				temp_lemma.setState(LinkableElement.DEFAULT);
			} else {
				temp_lemma = new Lemma((String) this.current_result[1]);
				this.owner.link(temp_lemma);
			}
			this.results.get(i).put(this.current_result[0], temp_lemma);
			
		} else if (i == this.parsers.indexOf(this.docs2lemmas_parser)) {
			DocumentPart temp_doc = (DocumentPart) this.results.get(this.parsers.indexOf(this.documents_parser)).get(this.current_result[0]);
			Lemma temp_lemma = (Lemma) this.results.get(this.parsers.indexOf(this.terms_parser)).get(this.current_result[1]);
			//On verifie que le lemme existe bien
			if (temp_lemma!=null)
			{
				Relation temp_rel;
				int position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION));
				if (position < 0) {
					temp_rel = new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION);
					this.owner.link(temp_rel);
				} else {
					temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
				}

				temp_lemma.link(temp_rel, temp_doc);
				temp_doc.link(temp_rel, temp_lemma);
			}
			
		} else if (i == this.parsers.indexOf(this.terms2terms_parser)) {
			// We check wether the read relation exists in the Ontology or not. 
			int position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation((String) this.current_result[2]));
			Relation temp_rel;
			// If not, we create it
			if (position < 0) {
				temp_rel = new Relation((String) this.current_result[2]);
				this.owner.link(temp_rel);
			}
			// If so, we retrieve it
			else temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);

			((LinkableElement) this.results.get(this.parsers.indexOf(this.terms_parser)).get(this.current_result[0])).link(temp_rel, (Lemma) this.results.get(this.parsers.indexOf(this.terms_parser)).get(this.current_result[1]));
			
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
		HashMap<Object, Object> text = this.results.get(this.parsers.indexOf(this.documents_parser));
		HashMap<String, LinkableElement> props = new HashMap<String, LinkableElement>();
		HashMap<String, ArrayList<LinkableElement>> copy_props = new HashMap<String, ArrayList<LinkableElement>>();
		for (Object element : this.results.get(this.parsers.indexOf(this.documents_parser)).values()) {
			String[] particles;
			int position;
			Relation temp_rel;
			boolean isPROP = false;
			
			
			particles = this.stripName(((DocumentPart) element).getName());
			String parent_key = "";
			for (int i=0; i < particles.length - 2; i++)
				parent_key += particles[i] + "-";
			if (particles.length >= 3)
				parent_key = parent_key.substring(0, parent_key.length() - 1);
			if (text.get(parent_key) == null) {
				if (text.get(parent_key + "-" + particles[particles.length - 2]) != null)
					parent_key = parent_key + "-" +  particles[particles.length - 2];
			}
			if (text.get(parent_key) != null) {
				// Ascending link between paragraphs
				position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION));			
				if (position < 0) {
					position = -position - 1;
					this.owner.get(Relation.KEY).add(position, new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION));
				}
				temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
				((DocumentPart) text.get(parent_key)).link(temp_rel, (DocumentPart) element);
	
				// Down going link between paragraphs
				position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(particles[particles.length - 2]));
				if (position < 0) {
					position = -position - 1;
					this.owner.get(Relation.KEY).add(position, new Relation(particles[particles.length - 2]));
				}
				temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
				((DocumentPart) element).link(temp_rel, (DocumentPart) text.get(parent_key));
			}

			// Preparing tables for later merge. 
			if (isProposition(((DocumentPart) element).getName())) {
				// Looking whether current document is the original "PROPOSITION"
				if (!((DocumentPart) element).getName().endsWith("ANTECEDENT"))
					isPROP = true;
				if (isPROP)
					props.put(((DocumentPart) element).getValue(), (LinkableElement) element);
				else {
					if (copy_props.get(((DocumentPart) element).getValue()) == null)
						copy_props.put(((DocumentPart) element).getValue(), new ArrayList<LinkableElement>());
					copy_props.get(((DocumentPart) element).getValue()).add((LinkableElement) element);
				}
			}
		}
		
		// Merging duplicated "PROPOSITIONS"
		for (LinkableElement element : props.values()) {
			ArrayList<LinkableElement> copies;

			if (copy_props.get(((DocumentPart) element).getValue()) != null)
					copies = copy_props.get(((DocumentPart) element).getValue());
			else copies = copy_props.get(((DocumentPart) element).getValue().substring(((DocumentPart) element).getValue().indexOf('.') + 1));
			if (copies != null) {
				for (LinkableElement copy : copies) {
					for (int ctg : copy.getLinks().keySet()) {
						for (Relation rel : copy.getLinks(ctg).keySet()) {
							for (LinkableElement link : copy.getLinks(ctg, rel)) {
								element.link(rel, link);
								for (Relation link_rel : link.getLinks(copy)) {
									link.link(link_rel, element);
									link.unlink(link_rel, copy);
								}
							}
						}
					}
					this.owner.unlink(copy);
				}
			}
		}		
	}
	
	/**
	 * Retrieves the usual <code>Relations</code> encountered in Syntex's exports. 
	 * If these <code>Relations</code> do not exist yet in the <code>Ontology</code>, it creates them. 
	 * @throws DuplicateElementException 
	 * @see arkeotek.io.importer.AbstractImporter#preTreat()
	 */
	@Override
	protected void preTreat() throws DuplicateElementException
	{
		int position;
		Relation temp_rel;
		
		// We check wether the Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION exists in the Ontology or not. 
		position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION));
		// If not, we create it
		if (position < 0)
			temp_rel = new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION);
		// If so, we retrieve it
		else temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
		
		this.owner.link(temp_rel);

		// We check wether the Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION exists in the Ontology or not. 
		position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION));
		// If not, we create it
		if (position < 0)
			temp_rel = new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION);
		// If so, we retrieve it
		else temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);

		this.owner.link(temp_rel);
	}

	private String[] stripName(String name) {
		return name.split("-");
	}
	
	private static boolean isProposition(String text) {
		return text.endsWith("-PROPOSITION") || text.endsWith("-ANTECEDENT");
	}
}
