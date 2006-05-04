/*
 * Created on 4 mai 2005
 *
 * Arkeotek Project
 */
package arkeotek.ontology;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import arkeotek.tools.TreeTagger;


/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class DocumentPart extends LinkableElement {

	/**
	 * Variable statically initialized by calling the LinkableElement.obtainCategoryKey() method. 
	 * This variable is used to identify instances of this class with a simple <code>int</code>.  
	 */
	public static final int KEY;
	private String value = "";
	
	static {
		KEY = obtainCategoryKey();
	}

    /**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(int, String, int)
     * @param id the id used to find this <code>LinkableElement</code> in the corpus. 
     * @param name the name of the <code>DocumentPart</code> in the corpus. 
     * @param state he state of the <code>DocumentPart</code> in the corpus.
     * @param value The text of this <code>DocumentPart</code>. 
     */
    public DocumentPart(int id, String name, int state, String value) {
        super(id, name, state);
		this.value = value;
    }

	/**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(String)
     * @param name the name of the <code>DocumentPart</code> in the corpus. 
	 * @param value the value of the <code>DocumentPart</code>
     */
    public DocumentPart(String name, String value) {
        super(name);
		this.value = value;
    }
	/**
     * @see arkeotek.ontology.LinkableElement#LinkableElement(String)
     * @param name the name of the <code>DocumentPart</code> in the corpus. 
     */
    public DocumentPart(String name) {
        super(name);
    }

    /**
	 * Return the value
	 * @return Returns the textual value of this <code>DocumentPart</code>.
	 */
	public String getValue()
	{
		return this.value;
	}

	/**
	 * @param value The textual value to set for this <code>DocumentPart</code>.
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	/**
     * @see arkeotek.ontology.LinkableElement#getCategoryKey()
     */
    public int getCategoryKey() {
        return KEY;
    }

	/**
	 * @see arkeotek.ontology.LinkableElement#lookup(arkeotek.ontology.LinkableElement)
	 */
	@Override
	public ResultSet lookup(LinkableElement target)
	{
		ResultSet results = new ResultSet();
		int relevance = 0;
		try {
			HashMap<String, ArrayList<String>> document = TreeTagger.tag(this.getValue());
			
			for (String doc_key : document.keySet()) {
				for (String lemma_key : target.getName().split(" ")) {
					if (document.get(doc_key).get(TreeTagger.LEMMA).equals(lemma_key))
						relevance++;
				}
			}
			results.add(new Result(relevance, this));
		} catch (IOException e) {
			System.err.println("An error occured while tagging with TreeTagger");
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * @see arkeotek.ontology.LinkableElement#toString()
	 */
	public String toString()
	{
		return this.getValue();
	}
}
