/**
 * Created on 19 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor;

/**
 * This interface defines several constants used by the ontologyEditor, e.g. action tags, default paths, etc. 
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public interface Constants
{
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>QuitAction</code> action. 
	 */
	public static final String ACTION_QUIT = "quit";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>BottomPanelChangeAction</code> action. 
	 */
	public static final String ACTION_CHANGE_BOTTOM_PANNEL_CONCEPTS = "change_bottom_pannel_concepts";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>BottomPanelChangeAction</code> action. 
	 */
	public static final String ACTION_CHANGE_BOTTOM_PANNEL_LEMMAS = "change_bottom_pannel_lemmas";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>BottomPanelChangeAction</code> action. 
	 */
	public static final String ACTION_CHANGE_BOTTOM_PANNEL_DOCUMENTS = "change_bottom_pannel_documents";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>TopPanelChangeAction</code> action. 
	 */
	public static final String ACTION_CHANGE_TOP_PANNEL_CONCEPTS = "change_top_pannel_concepts";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>TopPanelChangeAction</code> action. 
	 */
	public static final String ACTION_CHANGE_TOP_PANNEL_LEMMAS = "change_top_pannel_lemmas";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>TopPanelChangeAction</code> action. 
	 */
	public static final String ACTION_CHANGE_TOP_PANNEL_DOCUMENTS = "change_top_pannel_documents";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>SaveAction</code> action. 
	 */
	public static final String ACTION_SAVE = "save";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>OpenAction</code> action. 
	 */
	public static final String ACTION_OPEN = "open";

	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>OpenFromTermontoAction</code> action. 
	 */
	public static final String ACTION_OPEN_FROM_TERMONTO = "about";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>OpenAction</code> action. 
	 */
	public static final String ACTION_NEW_ONTOLOGY = "new_ontology";

	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>NewConceptAction</code> action. 
	 */
	public static final String ACTION_NEW_CONCEPT = "new_concept";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>NewRelationAction</code> action. 
	 */
	public static final String ACTION_NEW_RELATION = "new_relation";

	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>NewLemmaAction</code> action. 
	 */
	public static final String ACTION_NEW_LEMMA = "new_lemma";

	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>BreedAction</code> action. 
	 */
	public static final String ACTION_BREED = "breed";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>LemmasFusionAction</code> action. 
	 */
	public static final String ACTION_LEMMAS_FUSION = "Fusionner les lemmes";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>GenericIndexationAction</code> action. 
	 */
	public static final String ACTION_GENERIC_INDEXATION = "Indexer le document (g\u00e9n\u00e9rique)";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>SCDIndexationAction</code> action. 
	 */
	public static final String ACTION_SCD_INDEXATION = "Indexer le document (SCD)";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>LemmaSearchAction</code> action. 
	 */
	public static final String ACTION_SEARCH_LEMMA = "Rechercher un lemme";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>AProposAction</code> action. 
	 */
	public static final String ACTION_APROPOS = "A propos";
	
	/**
	 * Changement de la langue de l'application
	 */
	public static final String ACTION_CHANGE_LANGUAGE = "Changer la langue de l'application";
	
	/**
	 * Edition of the relations
	 */
	public static final String ACTION_EDITION_RELATIONS = "Ouvrir l'editeur de relations";
	
	/**
	 * Edition of the relations
	 */
	public static final String ACTION_TYPE_RELATIONS = "Changer le type d'une ou plusieurs relations";
	
	/**
	 * Default path where resources are located. 
	 */
	public static final String DEFAULT_RESOURCES_PATH = "ontologyEditor/resources/";
	
	/**
	 * Default path where icons are located. 
	 * This constant is used by the <code>ImageManager</code>.  
	 */
	public static final String DEFAULT_ICONS_PATH = DEFAULT_RESOURCES_PATH + "icons/";
	
	/**
	 * Default path where ontology's properties are located. 
	 * This constant is used by the <code>ApplicationManager</code>.  
	 */
	public static final String DEFAULT_ONTOLOGIES_PATH = "datas/ontologies/";
	
	/**
	 * Default icon for concepts
	 */
	public static final String DEFAULT_CONCEPT_ICON = "concept.gif";
	
	/**
	 * Default icon for lemmas
	 */
	public static final String DEFAULT_LEMMA_ICON = "lemma.gif";
	
	/**
	 * Default icon for documentParts
	 */
	public static final String DEFAULT_DOCUMENTPART_ICON = "document.gif";
	
	/**
	 * Default icon for relations
	 */
	public static final String DEFAULT_RELATION_ICON = "about.gif";
	
	/**
	 * Default icon
	 */
	public static final String DEFAULT_ICON = "about.gif";
	
	/**
	 * Flag used in the actions map of <code>ActionManager</code> for the <code>SearchConceptAction</code> action. 
	 */
	public static final String ACTION_SEARCH_CONCEPT = "Rechercher un concept";
}
