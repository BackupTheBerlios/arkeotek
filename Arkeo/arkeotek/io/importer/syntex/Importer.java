/**
 * Created on 1 juin 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.importer.syntex;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

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
	private HashSet<String> metTags = new HashSet<String>();
	private HashSet<String> validTags = new HashSet<String>();
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
			if (this.current_result[0] != null) 
			{
				if (this.results.get(i).get(this.current_result[0]) == null)
				{
					DocumentPart element = new DocumentPart((String) this.current_result[0], (String) this.current_result[1]);
					this.results.get(i).put(this.current_result[0], element);
					this.metTags.add((String)this.current_result[0]);
					this.owner.link(element);
				} else {
					((DocumentPart) this.results.get(i).get(this.current_result[0])).setValue(((DocumentPart) this.results.get(i).get(this.current_result[0])).getValue().concat(" " + (String) this.current_result[1]));
					if ((((DocumentPart) this.results.get(i).get(this.current_result[0])).getValue().charAt(0)) == '.')
						((DocumentPart) this.results.get(i).get(this.current_result[0])).setValue(((DocumentPart) this.results.get(i).get(this.current_result[0])).getValue().substring(1));
				}
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
				int position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION,Relation.RELATION_TERME_DOCUMENT));
				if (position < 0) 
				{
					temp_rel = new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION,Relation.RELATION_TERME_DOCUMENT);
					this.owner.link(temp_rel);
				} 
				else 
				{
					temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
				}
				if (temp_doc!=null)
				{
					temp_lemma.link(temp_rel, temp_doc);
					temp_doc.link(temp_rel, temp_lemma);
				}
			}
			
		} else if (i == this.parsers.indexOf(this.terms2terms_parser)) {
			// We check wether the read relation exists in the Ontology or not. 
			
			int position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation((String) this.current_result[2],Relation.RELATION_TERME_TERME));
			Relation temp_rel;
			// If not, we create it
			if (position < 0) {
				temp_rel = new Relation((String) this.current_result[2],Relation.RELATION_TERME_TERME);
				this.owner.link(temp_rel);
			}
			// If so, we retrieve it
			else temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);

			((LinkableElement) this.results.get(this.parsers.indexOf(this.terms_parser)).get(this.current_result[0])).link(temp_rel, (Lemma) this.results.get(this.parsers.indexOf(this.terms_parser)).get(this.current_result[1]));
			
		}
	}
	protected String getParentNode(String node)
	{
		String parentNode = node;
		int lastHyphenIndex = parentNode.lastIndexOf("-");
		if (lastHyphenIndex == -1)
			parentNode="__TOP";
		else
		{
			String NodeEnd = parentNode.substring(lastHyphenIndex+1, parentNode.length());
			//tests whether the last part of the tag is an ident of level
			if (!this.validTags.contains(NodeEnd)) 
				//	(NodeEnd.contains("0")||NodeEnd.contains("1")||NodeEnd.contains("2")||NodeEnd.contains("3")||NodeEnd.contains("4")||NodeEnd.contains("5")||NodeEnd.contains("6")||NodeEnd.contains("7")||NodeEnd.contains("8")||NodeEnd.contains("9"))
				lastHyphenIndex = parentNode.substring(0,parentNode.lastIndexOf("-")).lastIndexOf("-");
			if (lastHyphenIndex!=-1)
				parentNode = parentNode.substring(0,lastHyphenIndex);
			else
				parentNode = "__TOP";
		}
		return (parentNode);
	}

	protected void parseValidTags() throws FileNotFoundException, IOException
	{
		int token;
		InputStreamReader reader = new InputStreamReader(new FileInputStream("datas/articleSCDv3.dtd"));
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.eolIsSignificant(true);
		tokenizer.ordinaryChar(95);
		tokenizer.ordinaryChars(33, 64);
		tokenizer.wordChars(33, 64);
		tokenizer.wordChars(95,95);		
		
		while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) 
		{
//			System.out.println("Le token vaut : "+tokenizer.sval);
			if((token == StreamTokenizer.TT_WORD)&&(tokenizer.sval.equals("<!ELEMENT"))) 
					{
					token = tokenizer.nextToken();
					validTags.add(tokenizer.sval);
//					System.out.println("Le tag vaut : "+tokenizer.sval);
					}	
		}
	}
	@Override
	protected void innerTransitionTreatment(int i, int j) throws Exception
	{
//		 Added by A.R.

		Vector<String> parentNodes = new Vector<String>(1,1);
		Object [] temp_result = new Object[2];
		temp_result[0]=this.current_result[0];
		temp_result[1]=this.current_result[1];
		String currentTag = (String) this.current_result[0];
		int firstHyphen;
		if ((i == this.parsers.indexOf(this.documents_parser))&&(this.current_result[0]!=null))
		{
			//System.out.println("==================\n"+"Balise analysée : "+this.current_result[0].toString());
			
			//adds the second Node (i.e. the author, when there is one) to the validTags list
			firstHyphen=currentTag.indexOf("-");
			String secondNode = currentTag.substring(firstHyphen+1,currentTag.length());
			if ((firstHyphen!=-1)&&(secondNode.indexOf("-")!=-1))
			{
				secondNode = secondNode.substring(0, secondNode.indexOf("-"));
//				System.out.println("Taille de validTags : "+validTags.size());
				this.validTags.add(secondNode);
			}
			
			
			String parentNode = this.getParentNode(currentTag);
			//System.out.println("Le noeud père est : "+ parentNode);
			while (!this.metTags.contains(parentNode))
			{
				System.out.println("Le noeud "+parentNode+" est nouveau");
				parentNodes.add(parentNode);
				parentNode=this.getParentNode(parentNode);
			}
			for (int k=1;k<parentNodes.size()+1;k++)
			{
				//System.out.println("Le noeud "+parentNodes.get(parentNodes.size()-k)+" est inséré");
				temp_result[0] = parentNodes.get(parentNodes.size()-k);
				temp_result[1] = parentNodes.get(parentNodes.size()-k);//the associated text is arbitrarily chosen the same as the name of the node
				//duplicated from innerTreatment
				if (this.results.get(i).get(temp_result[0]) == null)
				{
					DocumentPart element = new DocumentPart((String) temp_result[0], (String) temp_result[1]);
					this.results.get(i).put(temp_result[0], element);
					this.metTags.add((String)temp_result[0]);
					this.owner.link(element);
				}
				else
				{
					((DocumentPart) this.results.get(i).get(temp_result[0])).setValue(((DocumentPart) this.results.get(i).get(temp_result[0])).getValue().concat(
							" " + (String) temp_result[1]));
					if ((((DocumentPart) this.results.get(i).get(temp_result[0])).getValue().charAt(0)) == '.')
						((DocumentPart) this.results.get(i).get(temp_result[0])).setValue(((DocumentPart) this.results.get(i).get(temp_result[0])).getValue().substring(1));
				}
				//end of duplication
			}	
			//System.out.println("==================\n");
		}
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
			String tagId;
			int position;
			Relation temp_rel;
			boolean isPROP = false;
			
			
			particles = this.stripName(((DocumentPart) element).getName());
			String parent_key = this.getParentNode(((DocumentPart) element).getName());
			/*for (int i=0; i < particles.length - 2; i++)
				parent_key += particles[i] + "-";
			if (particles.length >= 3)
				parent_key = parent_key.substring(0, parent_key.length() - 1);
			if (text.get(parent_key) == null) {
				if (text.get(parent_key + "-" + particles[particles.length - 2]) != null)
					parent_key = parent_key + "-" +  particles[particles.length - 2];
			}*/
			if (text.get(parent_key) != null) {
				// Ascending link between paragraphs
				
				position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION,Relation.RELATION_DOCUMENT_DOCUMENT));			
				if (position < 0) {
					position = -position - 1;
					this.owner.get(Relation.KEY).add(position, new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION,Relation.RELATION_DOCUMENT_DOCUMENT));
				}
				temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
				((DocumentPart) text.get(parent_key)).link(temp_rel, (DocumentPart) element);
				
				// Down going link between paragraphs
				tagId=particles[particles.length - 1];
				//tests whether the last particle is an ident of level 
				if (!(tagId.contains("0")||tagId.contains("1")||tagId.contains("2")||tagId.contains("3")||tagId.contains("4")||tagId.contains("5")||tagId.contains("6")||tagId.contains("7")||tagId.contains("8")||tagId.contains("9")))
					tagId="1"; //the last particle is a level with no ident, so the ident is arbitrarily set to "1"
				if (tagId.contains("IMAGE"))	
				{
					tagId="nouveau";
				}
				// Down going link between paragraphs
				position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(particles[particles.length - 2],Relation.RELATION_DOCUMENT_DOCUMENT));
				if (position < 0) {
					position = -position - 1;
					this.owner.get(Relation.KEY).add(position, new Relation(particles[particles.length - 2],Relation.RELATION_DOCUMENT_DOCUMENT));
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

		
		position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION,Relation.RELATION_TERME_DOCUMENT));
		// If not, we create it
		if (position < 0)
			temp_rel = new Relation(Relation.DEFAULT_LEMMA_DOCUMENTPART_RELATION,Relation.RELATION_TERME_DOCUMENT);
		// If so, we retrieve it
		else temp_rel = (Relation) this.owner.get(Relation.KEY).get(position);
		
		this.owner.link(temp_rel);

		// We check wether the Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION exists in the Ontology or not. 
		position = Collections.binarySearch(this.owner.get(Relation.KEY), new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION,Relation.RELATION_DOCUMENT_DOCUMENT));
		// If not, we create it
		if (position < 0)
			temp_rel = new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION,Relation.RELATION_DOCUMENT_DOCUMENT);
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
	
	/**
	 * Imports datas combining the <code>AbstractParsers</code>.
	 * 
	 * @throws Exception
	 */
	@Override
	public void performImport() throws Exception
	{
		this.results = new HashMap<Integer, HashMap<Object, Object>>(this.parsers.size());
		this.metTags.add("__TOP");
		this.parseValidTags();
		this.preTreat();

		if (this.parsers != null && !this.parsers.isEmpty())
		{
			for (int i = 0; i < this.parsers.size(); i++)
			{
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

				while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF)
				{
					if (this.parsers.get(i).usedColumns.get(this.parsers.get(i).current_col++) != null && token == StreamTokenizer.TT_WORD)
					{
						value = tokenizer.sval;
						line[word++] = value;
						this.parsers.get(i).treat(value);

					}

					if (token == StreamTokenizer.TT_EOL)
					{
						word = 0;
						//this.innerTransitionTreatment(i, j);
						//this.previous_result = this.current_result;
						this.parsers.get(i).current_col = 0;
						this.current_result = this.parsers.get(i).treat(line);
						this.innerTransitionTreatment(i, j);
						this.previous_result = this.current_result;
						this.innerTreatment(i, j++);
						line = new String[this.parsers.get(i).columns];
					}
				}
			}
		}

		this.postTreat();
	}
}
