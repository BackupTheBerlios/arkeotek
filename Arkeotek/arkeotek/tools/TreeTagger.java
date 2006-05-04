/**
 * Created on 28 juil. 2005
 * 
 * Arkeotek Project
 */
package arkeotek.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public final class TreeTagger
{
	private static String path;
	private static int temp_file_counter = 0;
	private static String[] forbiddenLemmas;
	
	
	/**
	 * Path of properties files' directory.
	 */
	private static URL PROPERTIES_DIR; 
	/**
	 * Path of temp files' directory. 
	 */
	public static String TEMP_FILE_DIR;
	/**
	 * The key corresponding to TreeTagger properties. 
	 */
	public static final String TREETAGGER_KEY = "treetagger";
	/**
	 * The key precising the "path" property. 
	 */
	public static final String PATH_KEY = "path";
	/**
	 * The key precising the "forbiddenLemmas" property. 
	 */
	public static final String FORBIDDENLEMMAS_KEY = "forbiddenLemmas";
	/**
	 * Prefix for temporary files created before tagging. 
	 */
	public static final String TEMP_FILE_PREFIX = "totag";
	/**
	 * Suffix for input temporary files created before tagging. 
	 */
	public static final String TEMP_INPUT_FILE_SUFFIX = ".in";
	/**
	 * Suffix for output temporary files created before tagging. 
	 */
	public static final String TEMP_OUTPUT_FILE_SUFFIX = ".out";
	/**
	 * Constant indicating the position of the "type" value in the <code>ArrayList</code> 
	 * of results returned for each word treated by TreeTagger. 
	 */
	public static final int TYPE = 0;
	/**
	 * Constant indicating the position of the lemma form in the <code>ArrayList</code> 
	 * of results returned for each word treated by TreeTagger. 
	 */
	public static final int LEMMA = 1;
	
	/*static {
		Properties common = new Properties();
		FileInputStream file;

		try {
			file = new FileInputStream(PROPERTIES_DIR.toString() + "common.properties");
			common.load(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		path = common.getProperty(TREETAGGER_KEY + "." + PATH_KEY);
	}*/

	/**
	 * Not an instanciable class.
	 */
	private TreeTagger()
	{
		// Not an instanciable class. 
	}

	/**
	 * @param treetagger_results TreeTagger results from wich the lemma form is to be extracted. 
	 * @return The lemma form of an expression.
	 */
	public static String lemmatize(HashMap<String, ArrayList<String>> treetagger_results) {
		String lemma = "";
		
		for (ArrayList<String> caracs : treetagger_results.values()) {
			lemma += caracs.get(LEMMA) + " ";
		}
		
		return lemma.substring(0, lemma.length() - 1);
	}

	/**
	 * @param phrase The expression to lemmatize. 
	 * @return The lemma form of an expression. 
	 * @throws IOException
	 */
	public static String lemmatize(String phrase) throws IOException {
		return lemmatize(tag(phrase));
	}
	
	/**
	 * @param text The text to be tagged by TreeTagger. 
	 * @return An <code>ArrayList</code><<code>String</code>> corresponding to each word of the text.
	 * @throws IOException 
	 * @see arkeotek.tools.TreeTagger#tag(File)
	 */
	public static HashMap<String, ArrayList<String>> tag(String text) throws IOException {	
		Process treetagger = Runtime.getRuntime().exec(path);
		treetagger.getOutputStream().write(text.getBytes());
		treetagger.getOutputStream().close();
		InputStream inStream = treetagger.getInputStream();
		
		return retrieveResults(inStream);
	}

	/**
	 * @param text The <code>File</code> to be tagged by TreeTagger. 
	 * @return An <code>ArrayList</code><<code>String</code>> corresponding to each word of the text.
	 * @throws IOException 
	 */
	public static HashMap<String, ArrayList<String>> tag(File text) throws IOException {
		FileInputStream file = new FileInputStream(text);
		Process treetagger = Runtime.getRuntime().exec(path + " " + text.getPath());
		
		byte[] buffer = new byte[(int) text.length()];
		file.read(buffer);
		treetagger.getOutputStream().write(buffer);
		
		InputStream inStream = treetagger.getInputStream();
		
		return retrieveResults(inStream);
	}
	
	private static HashMap<String, ArrayList<String>> retrieveResults(InputStream stream) throws IOException
	{
		HashMap<String, ArrayList<String>> tagging = new HashMap<String, ArrayList<String>>();
		ArrayList<String> tmp_values = new ArrayList<String>(2);
		int token;

		InputStreamReader reader = new InputStreamReader(stream);
		StreamTokenizer tokenizer = new StreamTokenizer(reader);
		tokenizer.eolIsSignificant(true);
		tokenizer.ordinaryChars(33, 64);
		tokenizer.wordChars(33, 64);
		
		while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
			if (token == StreamTokenizer.TT_EOL) {
				String current_word = tmp_values.get(0);
				tmp_values.remove(0);
				tagging.put(current_word, tmp_values);
				tmp_values = new ArrayList<String>();
			} else {
				tmp_values.add(tokenizer.sval);
			}
		}
		for (int i = 0; i < forbiddenLemmas.length; i++)
		{
			tagging.remove(forbiddenLemmas[i]);
		}

		return tagging;
	}
	
	/**
	 * Initializes static variables of this class with the specified <code>URL</code>. 
	 * @param url The <code>URL</code> indicating the path to TreeTagger binary file.
	 */
	public static void init(URL url)
	{
		try {
			PROPERTIES_DIR = new URL(url.toExternalForm().substring(0, url.toString().lastIndexOf("ontologies/")));
			TEMP_FILE_DIR = PROPERTIES_DIR.toExternalForm() + "tmp/";
			Properties common = new Properties();

			try {
				URL[] urls = {PROPERTIES_DIR};
				URLClassLoader classLoader = new URLClassLoader(urls);
				InputStream propertiesStream = classLoader.getResourceAsStream("common.properties");
				common.load(propertiesStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			path = common.getProperty(TREETAGGER_KEY + "." + PATH_KEY);
			forbiddenLemmas = common.getProperty(TREETAGGER_KEY + "."+ FORBIDDENLEMMAS_KEY).split(",");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}