/**
 * Created on 19 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import ontologyEditor.actions.AProposAction;
import ontologyEditor.actions.BottomPanelChangeAction;
import ontologyEditor.actions.BreedAction;
import ontologyEditor.actions.ChangeLanguageAction;
import ontologyEditor.actions.GenericIndexationAction;
import ontologyEditor.actions.ImportTermontoAction;
import ontologyEditor.actions.LemmaSearchAction;
import ontologyEditor.actions.LemmasFusionAction;
import ontologyEditor.actions.NewConceptAction;
import ontologyEditor.actions.NewLemmaAction;
import ontologyEditor.actions.NewOntologyAction;
import ontologyEditor.actions.NewRelationAction;
import ontologyEditor.actions.OpenAction;
import ontologyEditor.actions.QuitAction;
import ontologyEditor.actions.RelationsEditionAction;
import ontologyEditor.actions.SCDIndexationAction;
import ontologyEditor.actions.SaveAction;
import ontologyEditor.actions.TopPanelChangeAction;
import ontologyEditor.gui.MainFrame;
import ontologyEditor.gui.MenuBar;
import ontologyEditor.gui.dialogs.AboutDialog;
import ontologyEditor.gui.dialogs.SearchLemmaDialog;
import ontologyEditor.gui.dialogs.NewOntologyDialog;
import ontologyEditor.gui.dialogs.ProgressBarDialog;
import ontologyEditor.gui.dialogs.RelationEditionDialog;
import ontologyEditor.gui.filechoosers.OntologyFileChooser;
import ontologyEditor.gui.filechoosers.SyntexFileChooser;
import ontologyEditor.gui.filechoosers.TermontoFileChooser;
import ontologyEditor.gui.model.tableModel.LemmaTableModel;
import ontologyEditor.gui.panels.linguistic.LinguisticPanel;
import arkeotek.indexing.generic.GenericIndexer;
import arkeotek.indexing.scd.SCDIndexer;
import arkeotek.io.importer.AbstractParser;
import arkeotek.io.importer.termonto.Importer;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.DuplicateElementException;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 * 
 */

/*
 *  classe main de l'application
 *  elle gére les requetes de l'utilisateur en pointe vers l'action souhaité
 *  met en place l'interface
 */
public class ApplicationManager
{
	
    private static ApplicationManager applicationManager = null;
	
	/**
	 * Current ontology
	 */
	public static Ontology ontology;
	

    /**
     * Requests wich may be resolved by ApplicationManager
     */
    public enum Request {
        /**
         * Launchs application
         */
        RUN_APPLICATION,
        /**
         * Changes the content of the top panel
         */
        CHANGE_TOP_CONTENT_CONCEPTS,
        /**
         * Changes the content of the top panel
         */
        CHANGE_TOP_CONTENT_LEMMAS,
        /**
         * Changes the content of the top panel
         */
        CHANGE_TOP_CONTENT_DOCUMENTS,
        /**
         * Changes the content of the bottom panel
         */
        CHANGE_BOTTOM_CONTENT_CONCEPTS,
        /**
         * Changes the content of the bottom panel
         */
        CHANGE_BOTTOM_CONTENT_LEMMAS,
        /**
         * Changes the content of the bottom panel
         */
        CHANGE_BOTTOM_CONTENT_DOCUMENTS,
        /**
         * Opens an ontology
         */
        OPEN_ONTOLOGY,
		/**
         * Opens an ontology created in Termonto
         */
        OPEN_ONTOLOGY_FROM_TERMONTO,
        /**
         * Saves an ontology
         */
        SAVE_ONTOLOGY,
        /**
         * Creates a new concept
         */
        CREATE_NEW_CONCEPT,
		/**
         * Creates a new relation
         */
        CREATE_NEW_RELATION,
        /**
         * Creates a new ontology
         */
        CREATE_NEW_ONTOLOGY,
        /**
         * Creates a new lemma
         */
        CREATE_NEW_LEMMA,
        /**
         * Quits application
         */
        QUIT_APPLICATION,
        /**
         * Breeds an ontology with Syntex documents
         */
        BREED_ONTOLOGY,
        /**
         * Shows "About" section
         */
        SHOW_ABOUT,
        /**
         * Merge selectionned lemmas
         */
        LEMMAS_FUSION, 
        /**
         * Performs the SCD indexing action
         */
        SCD_INDEXATION,
        /**
         * Performs the generic indexing action
         */
        GENERIC_INDEXATION,
        /**
         * Performs Lemma Search
         */
        LEMMA_SEARCH,
        /**
         * Performs Showing About Dialog
         */
        SHOW_APROPOS,
        /**
         * Performs Changing Language of the application
         */
        CHANGE_LANGUAGE,
        /**
         * Performs Edition Relations
         */
        EDITION_RELATIONS
    }
    
    // Language
    private static Vector availableLanguages = null ;
	private static Properties config = null ;
	private static Properties currentLanguage = null;
	private static String currentLang = null;
	public static final String CHEMIN = "./ontologyEditor/resources/i18n/";
	public static final String ERRORSTRING = "#ERROR";
	public static final String EXTENSION = "lng";
	public static final String LANGFILE = "lang.cfg";
	

	
	private ApplicationManager()
    {
		applicationManager=this;
        try
        {
            // Initialisation of actions
			ActionManager am = ActionManager.getActionManager();
            am.registerAction(Constants.ACTION_NEW_ONTOLOGY, new NewOntologyAction());
            am.registerAction(Constants.ACTION_SAVE, new SaveAction());
            am.registerAction(Constants.ACTION_OPEN, new OpenAction());
			am.registerAction(Constants.ACTION_OPEN_FROM_TERMONTO, new ImportTermontoAction());
            am.registerAction(Constants.ACTION_QUIT, new QuitAction());

            am.registerAction(Constants.ACTION_NEW_CONCEPT, new NewConceptAction());
			am.registerAction(Constants.ACTION_NEW_RELATION, new NewRelationAction());
            am.registerAction(Constants.ACTION_NEW_LEMMA, new NewLemmaAction());
            am.registerAction(Constants.ACTION_BREED, new BreedAction());
			am.registerAction(Constants.ACTION_LEMMAS_FUSION, new LemmasFusionAction());
			am.registerAction(Constants.ACTION_SCD_INDEXATION, new SCDIndexationAction());
			am.registerAction(Constants.ACTION_GENERIC_INDEXATION, new GenericIndexationAction());
			am.registerAction(Constants.ACTION_EDITION_RELATIONS, new RelationsEditionAction());

			am.registerAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_CONCEPTS, new BottomPanelChangeAction(Concept.KEY));
            am.registerAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_LEMMAS, new BottomPanelChangeAction(Lemma.KEY));
            am.registerAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_DOCUMENTS, new BottomPanelChangeAction(DocumentPart.KEY));
            am.registerAction(Constants.ACTION_CHANGE_TOP_PANNEL_CONCEPTS, new TopPanelChangeAction(Concept.KEY));
            am.registerAction(Constants.ACTION_CHANGE_TOP_PANNEL_LEMMAS, new TopPanelChangeAction(Lemma.KEY));
            am.registerAction(Constants.ACTION_CHANGE_TOP_PANNEL_DOCUMENTS, new TopPanelChangeAction(DocumentPart.KEY));
            
            am.registerAction(Constants.ACTION_SEARCH_LEMMA, new LemmaSearchAction());
			am.registerAction(Constants.ACTION_APROPOS, new AProposAction());
			am.registerAction(Constants.ACTION_CHANGE_LANGUAGE, new ChangeLanguageAction());

			
			// Retrieview of ontology (we must create the ontology before the mainframe)
			// ApplicationManager.ontology = new Ontology("Pipontologie");
			
			// Creation of main frame
            DisplayManager.mainFrame = new MainFrame();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
			System.exit(0);
        }
    }
    
	/**
	 * @return Returns the applicationManager.
	 */
	public static ApplicationManager getApplicationManager()
	{
		if (ApplicationManager.applicationManager == null)
        {
            ApplicationManager.applicationManager = new ApplicationManager();
        }
        return (ApplicationManager.applicationManager);
	}
	
	/**
	 * Realises the adapted treatment for the specified <code>Request</code>. 
	 * @param request The request to treat. 
	 */
	public void manageRequest(Request request)
    {
        try
        {
			int choice = 0;
		switch (request)
			{
			case CREATE_NEW_ONTOLOGY : 
				if (ontology != null) {
					if (ontology.isDirty() != null) {
						Object[] options = {"Oui", "Non", "Annuler"};
						choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "L'ontologie courante n'a pas \u00e9t\u00e9 enregistr\u00e9e, voulez-vous le faire maintenant ?", "Avertissement", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
						if (choice == JOptionPane.OK_OPTION)
							ontology.save();
					}
					
				}
				if (choice != JOptionPane.CANCEL_OPTION) {
					NewOntologyDialog wizard = new NewOntologyDialog();
					wizard.setVisible(true);
					if (wizard.validInput()){
						ontology = null;
						ProgressBarDialog progressBarDialog = new ProgressBarDialog(DisplayManager.mainFrame, "chargement", true);
						Object[] parameters = {wizard.getOntologyname(), progressBarDialog};
						GenericRunnable ontoRunnable = new GenericRunnable(parameters)
						{
							public void run()
							{
								try {
									ApplicationManager.ontology = new Ontology((String)((Object[]) this.getArgument())[0]);
									((ProgressBarDialog)((Object[])this.getArgument())[1]).dispose();
								} catch (DuplicateElementException e) {
									Object[] options = {"OK"};
									JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Fichiers Termonto erron\u00e9s : \u00e9l\u00e9ments dupliqu\u00e9s. ", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						ontoRunnable.start();
						progressBarDialog.setVisible(true);
						
						DisplayManager.getInstance().resetDisplay();
						
					}
				}
				break;
				
			case CREATE_NEW_CONCEPT : 
				String concept_name;
				if (ontology != null) 
				{
					concept_name = JOptionPane.showInputDialog(DisplayManager.mainFrame, "Veuillez entrer le nom du nouveau concept:", "Cr\u00e9ation d'un concept", JOptionPane.INFORMATION_MESSAGE);
					if (concept_name != null)
					{
						Concept element = new Concept(concept_name);
						ontology.link(element);
						DisplayManager.getInstance().addElement(element);
					}
				}
				break;
			case CREATE_NEW_RELATION : 
				String relation_name;
				if (ontology != null) 
				{
					relation_name = JOptionPane.showInputDialog(DisplayManager.mainFrame, "Veuillez entrer le nom de la nouvelle relation:", "Cr\u00e9ation d'une relation", JOptionPane.INFORMATION_MESSAGE);
					if (relation_name != null)
					{
						Relation element = new Relation(relation_name);
						ontology.link(element);
					}
				}
				break;
			case CREATE_NEW_LEMMA : 
				String lemma_name;
				if (ontology != null) 
				{
					lemma_name = JOptionPane.showInputDialog(DisplayManager.mainFrame, "Veuillez entrer le nom du nouveau lemme:", "Cr\u00e9ation d'un lemme", JOptionPane.INFORMATION_MESSAGE);
					if (lemma_name != null)
					{
						Lemma element = new Lemma(lemma_name);
						ontology.link(element);
						DisplayManager.getInstance().addElement(element);
					}
				}
				break;
			case SAVE_ONTOLOGY : 
				if (ontology != null)
					ontology.save();
				break;
				
			case BREED_ONTOLOGY :
				/* On vérifie qu'une ontologie est chargée dans l'application */
				if (ontology != null) {
					/* On averti l'utilisateur que l'opération d'enrichissement est longue et qu'elle surcharge le systeme
					   On lui demande si il est bien sûr de vouloir continuer le traitement */
					Object[] options = {"Oui", "Non"};
					choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Cette op\u00e9ration peut s'av\u00e9rer longue et le systÃ¨me peut Ãªtre surcharg\u00e9 pendant toute cette p\u00e9riode. \nEtes vous sÃ»r de vouloir continuer ?", "Avertissement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					/* On procède à l'enrichissement si la réponse de l'utilisateur est positive */					
					if (choice == JOptionPane.OK_OPTION)
					{
						/* On affiche une boite dialogue d'ouverture de dossier pour permettre à l'utilisateur
						   de choisir le repertoire oùse trouvent les fichiers syntex parser */
						SyntexFileChooser syntexFileChooser = new SyntexFileChooser();
						syntexFileChooser.showOpenDialog(DisplayManager.mainFrame);
						
						/* On verifie le chemin donné par l'utilisateur (apparement le dernier rep utilisé ne peut etre réutilisé) */
						if (syntexFileChooser.getSelectedFile() != null && !syntexFileChooser.getSelectedFile().getPath().equals(SyntexFileChooser.LAST_USED_DIRECTORY))
						{
							Properties prop = new Properties();
							InputStream inStream = ApplicationManager.ontology.getClass().getClassLoader().getResourceAsStream(Constants.DEFAULT_RESOURCES_PATH + "global.properties");
							OutputStream outStream = new FileOutputStream(Constants.DEFAULT_RESOURCES_PATH + "global.properties");
							try
							{
								prop.load(inStream);
								prop.setProperty(SyntexFileChooser.LAST_USED_DIRECTORY_KEY, syntexFileChooser.getSelectedFile().getPath());
								prop.store(outStream, "");
							}
							catch (IOException e)
							{
								// Nothing to do. 
							}
							finally
							{
								prop.setProperty(SyntexFileChooser.LAST_USED_DIRECTORY_KEY, syntexFileChooser.getSelectedFile().getPath());
							}
						}
						ontology.breed(AbstractParser.addSlash(syntexFileChooser.getSelectedFile().getPath()));
						
						DisplayManager.getInstance().resetDisplay();
					}
				}
				break;
				
			case OPEN_ONTOLOGY :
				if (ontology != null) {
					if (ontology.isDirty() != null) {
						Object[] options = {"Oui", "Non", "Annuler"};
						choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "L'ontologie courante n'a pas été enregistrée, voulez-vous le faire maintenant ?", "Modifications non enregistr\u00e9es", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
						if (choice == JOptionPane.OK_OPTION)
							ontology.save();
					}
					
				}
				if (choice != JOptionPane.CANCEL_OPTION) {
					OntologyFileChooser ontologyFileChooser = new OntologyFileChooser();
					ontologyFileChooser.showOpenDialog(DisplayManager.mainFrame);

					if (ontologyFileChooser.getSelectedFile() != null) {
					
						ProgressBarDialog progressBarDialog = new ProgressBarDialog(DisplayManager.mainFrame, "Chargement", true);
						
						
						ontology = null;
						Object[] parameters = {ontologyFileChooser.extractOntologyName(), progressBarDialog};
						GenericRunnable ontoRunnable = new GenericRunnable(parameters)
						{
							public void run()
							{
								try {
									// crétaion de l'ontology
									ApplicationManager.ontology = new Ontology((String)((Object[]) this.getArgument())[0]);
									// parcours tous les objets de l'ontology et verifie s'ils sont deja sauvé ou pas
									ApplicationManager.ontology.setSaved();
									((ProgressBarDialog)((Object[])this.getArgument())[1]).dispose();
								} catch (DuplicateElementException e) {
									Object[] options = {"OK"};
									JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Fichiers Termonto erron\u00e9s : \u00e9l\u00e9ments dupliqu\u00e9s. ", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						ontoRunnable.start();
						progressBarDialog.setVisible(true);
						
						DisplayManager.getInstance().resetDisplay();
					}
				}
				break;
			case OPEN_ONTOLOGY_FROM_TERMONTO : 
				/* On s'assure qu'une ontologie est chargée dans l'application */
				if (ontology != null)
				{
					/* Si l'ontologie chargée n'est pas sauvegardé (modifications réscentes) alors on propose à l'utilisateur de sauvegarder ! */
					if (ontology.isDirty() != null)
					{
						/* Boite de dialogue proposant la sauvegarde de l'ontologie */
						Object[] options = {"Oui", "Non", "Annuler"};
						choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "L'ontologie courante n'a pas \u00e9t\u00e9 enregistr\u00e9e, voulez-vous le faire maintenant ?", "Modifications non enregistr\u00e9es", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
						if (choice == JOptionPane.OK_OPTION) ontology.save();
					}
				}
				else
				{
					/* Si aucune ontologie n'est ouverte alors on propose d'en creer une */
					this.manageRequest(Request.CREATE_NEW_ONTOLOGY);
				}
				
				/* On poursuit le traitement si l'utilisateur n' a pas annulé l'importation par le biais de la boite de dialogue */
				if (choice != JOptionPane.CANCEL_OPTION)
				{
					/* TermontoFileChooser est une boite de dialogue de type File Chooser paramétrée pour l'importation termonto
					 * On appel la méthode showOpenDialog avec la frame principale en paramètre */
					TermontoFileChooser fileChooser = new TermontoFileChooser();
					fileChooser.showOpenDialog(DisplayManager.mainFrame);

					if (fileChooser.getSelectedFile() != null)
					{
						/* ProgressBarDialog est une boite de dialogue avec une barre d'avancement dedans
						 * elle permet par l'intermédiaire de la méthode launch d'afficher la boite tout en s'executant en parallèle de l'application
						 */
						ProgressBarDialog progressBarDialog = new ProgressBarDialog(DisplayManager.mainFrame, "Chargement", true);
						
						
						/* GenericRunnable est une inner classe de ApplicationManager qui permet de procéder à l'execution d'un traitement
						 * que l'on définit à l'instanciation de l'objet
						 * Il y a possibilité de passer un objet en paramètre (structure quelquonque pour le traitement) 
						 */
						Object[] parameters = {AbstractParser.addSlash(fileChooser.getSelectedFile().getPath()), progressBarDialog};
						GenericRunnable termontoRunnable = new GenericRunnable(parameters)
						{
							public void run()
							{
								try
								{
									/* Importer est la classe qui s'occupe de l'importation
									 * Paramètres : ontologie
									 */
									Importer termonto = new Importer(ontology, ((String)((Object[])this.getArgument())[0]));
									
									/* Lancement du traitement des données */
									termonto.performImport();
									
									/* Une fois le traitement effectué, on ferme la barre d'avancement */
									((ProgressBarDialog)((Object[])this.getArgument())[1]).dispose();
								}
								catch (DuplicateElementException e)
								{
									Object[] options = {"OK"};
									JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Fichiers Termonto erron\u00e9s : \u00e9l\u00e9ments dupliqu\u00e9s. ", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
								}
								catch (IOException e) {
									Object[] options = {"OK"};
									JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Une erreur d'entr\u00e9es/sorties est survenue lors de la r\u00e9cup\u00e9ration des donn\u00e9es Termonto", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
								
								/* fermeture de nouveau surement au cas ou */
								((ProgressBarDialog)((Object[])this.getArgument())[1]).dispose();
							}
						};
						
						/* On lance donc le threads instancié plus haut */
						termontoRunnable.start();
						progressBarDialog.setVisible(true);
						
						ProgressBarDialog progressBarRefreshDialog = new ProgressBarDialog(DisplayManager.mainFrame, "Rafraichissement", true);
						
						/* Thread pour le rafraichissement de l'image */
						GenericRunnable refreshRunnable = new GenericRunnable(progressBarRefreshDialog)
						{
							public void run()
							{
								DisplayManager.getInstance().resetDisplay();
								((ProgressBarDialog)(this.getArgument())).dispose();
							}
						};
						
						/* lancement du thread et affichage de la barre d 'avancement */
						refreshRunnable.start();
						progressBarRefreshDialog.setVisible(true);
					}
				}
				break;
			case CHANGE_TOP_CONTENT_CONCEPTS : 
				DisplayManager.mainFrame.changeView(MainFrame.TOP_PANEL, Concept.KEY);
				break;
			case CHANGE_TOP_CONTENT_LEMMAS : 
				DisplayManager.mainFrame.changeView(MainFrame.TOP_PANEL, Lemma.KEY);
				break;
			case CHANGE_TOP_CONTENT_DOCUMENTS : 
				DisplayManager.mainFrame.changeView(MainFrame.TOP_PANEL, DocumentPart.KEY);
				break;
			case CHANGE_BOTTOM_CONTENT_CONCEPTS : 
				DisplayManager.mainFrame.changeView(MainFrame.BOTTOM_PANEL, Concept.KEY);
				break;
			case CHANGE_BOTTOM_CONTENT_LEMMAS : 
				DisplayManager.mainFrame.changeView(MainFrame.BOTTOM_PANEL, Lemma.KEY);
				break;
			case CHANGE_BOTTOM_CONTENT_DOCUMENTS : 
				DisplayManager.mainFrame.changeView(MainFrame.BOTTOM_PANEL, DocumentPart.KEY);
				break;
			case LEMMAS_FUSION :
				// selectionne tous les termes selectionnés dans la table des termes
				ArrayList<LinkableElement> lemmas = DisplayManager.getInstance().getSelectedElementsTable(Lemma.KEY);;
				Object[] possibilities = lemmas.toArray();
				// selectionne un terme parmi tous les terme sélectionné
				Lemma mainLemma = (Lemma)JOptionPane.showInputDialog(
				                    DisplayManager.mainFrame,
				                    "Choisissez le lemme à conserver",
				                    "Fusion de lemmes",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    lemmas.get(0));
				// si le terme est different de null
				if (mainLemma != null) 
				{
					// pour chacun des lemmes selectionné
					for (LinkableElement lemma : lemmas)
					{
						// si le lemme courant est != du terme selectionné
						if (lemma != mainLemma)
						{
							// on supprime l'element courant et tous ces liens
							HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>> relations  = lemma.getLinks();
							for (Integer key : relations.keySet())
							{
								HashMap<Relation, HashMap<LinkableElement, Link>> links = lemma.getLinks(key);
								if (mainLemma.getLinks().containsKey(key))
								{
									for (Relation relation : links.keySet())
									{
										if (mainLemma.getLinks(key).containsKey(relation))
										{
											for (LinkableElement element : lemma.getLinks(key, relation))
											{
												if (!mainLemma.getLinks(key).get(relation).containsKey(element))
													mainLemma.link(relation, element);
											}
										}
										else
										{
											for (LinkableElement element : lemma.getLinks(key, relation))
											{
												mainLemma.link(relation, element);
											}
										}	
									}
								}
								else
								{
									mainLemma.getLinks().put(key, new HashMap<Relation, HashMap<LinkableElement, Link>>());
									for (Relation relation : links.keySet())
									{
										for (LinkableElement element : lemma.getLinks(key, relation))
										{
											mainLemma.link(relation, element);
										}	
									}
								}
							}
							for (Object[] reference : ontology.getElementsThatReference(lemma))
							{
								((LinkableElement)reference[0]).unlink(((Relation)reference[1]), lemma);
								((LinkableElement)reference[0]).link(((Relation)reference[1]), mainLemma);
							}
							
							ApplicationManager.ontology.unlink(lemma);
							// mise a jour de la table des lemmes
							if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTable()!=null)
								DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTable().updateUI();
							if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable()!=null)
								DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().updateUI();
							
							
						}
					}
				}
				break;
			case SCD_INDEXATION :
				new SCDIndexer(ontology, DisplayManager.getInstance().getSelectedElements(DocumentPart.KEY).get(0)).index();
				break;
			case GENERIC_INDEXATION :
				new GenericIndexer(ontology, DisplayManager.getInstance().getSelectedElements(DocumentPart.KEY).get(0)).index();
				break;
				
			case LEMMA_SEARCH :
				if (ontology != null)
				{
					// Si la vue lemme est active
					if(DisplayManager.mainFrame.isActive(Lemma.KEY))
					{
						// ouverture de la boite de dialogue correspondante
						SearchLemmaDialog lemmaSearchDialog = new SearchLemmaDialog();
						lemmaSearchDialog.setVisible(true);
						String lcn = lemmaSearchDialog.getContainName();
						// si la chaine n'est pas null
						if ( lcn != null)
						{
							// all_lemma contient tous les lemme de l'ontology
							ArrayList<LinkableElement> all_lemmas = ApplicationManager.ontology.get(Lemma.KEY);
							// matching lemma contiendra tous les lemmes correpondant à la recherche
							ArrayList<LinkableElement> matching_lemmas = new ArrayList<LinkableElement>();
							// des qu'un lemme de la premiere liste ressemble à la recherche on le rajoute à la seconde liste
							for(int i=0;i<all_lemmas.size();i++)
							{
								if(((Lemma) all_lemmas.get(i)).getName().contains(lcn))
								{
									matching_lemmas.add(all_lemmas.get(i));
								}
							}
							
							// on rempli la table des lemmes dans la vue lemme on rempli le model de la table avec search_lemma
							// si le bas est une vue lemme
							if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL) instanceof LinguisticPanel)
							{
								LinguisticPanel panel=(LinguisticPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL);
								panel.getTable().removeAll();
								Object[][] donnees=new Object[matching_lemmas.size()][1];
		
								if (matching_lemmas.size()!=0)
								{
									for (int i=0;i<matching_lemmas.size();i++)
									{
										donnees[i][0]=(matching_lemmas.get(i));
									}
									((LemmaTableModel)panel.getTable().getModel()).setDonnees(donnees);
									panel.getTable().updateUI();
								}
								else
								{
									JOptionPane.showMessageDialog(DisplayManager.mainFrame, "Il n'y aucun terme qui correspond à "+lcn);
								}
							}
							// si le haut est une vue lemme
							if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL) instanceof LinguisticPanel)
							{
								LinguisticPanel panel=(LinguisticPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL);
								panel.getTable().removeAll();
								Object[][] donnees=new Object[matching_lemmas.size()][1];
		
								if (matching_lemmas.size()!=0)
								{
									for (int i=0;i<matching_lemmas.size();i++)
									{
										donnees[i][0]=(matching_lemmas.get(i));
									}
									((LemmaTableModel)panel.getTable().getModel()).setDonnees(donnees);
									panel.getTable().updateUI();
								}
								else
								{
									((LemmaTableModel)panel.getTable().getModel()).setDonnees(donnees);
									panel.getTable().updateUI();
								}
							}
						}
					}
					else
					{
						//if not active we display a message...
						JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Veuillez afficher le panneau des termes.");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Veuillez charger une ontologie, avant d'effectuer une recherche.");
				}
				break;
			
			case SHOW_APROPOS : // Instanciation de la boite de dialog "a propos" de l'application
					/*AboutDialog aboutDialog = */new AboutDialog();break;
				
			case CHANGE_LANGUAGE : 
				Enumeration<AbstractButton> enumer = MenuBar.GROUPLANG.getElements();
				while(enumer.hasMoreElements())
				{
					JRadioButtonMenuItem jrbmi = (JRadioButtonMenuItem)enumer.nextElement();
					if(jrbmi.isSelected()) setCurrentLang(jrbmi.getName());
				}
				JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Veuillez redémarrer l'application afin que les nouveaux paramètres soient pris en compte.");
				break;
			case EDITION_RELATIONS :
				if(ApplicationManager.ontology!=null)
				{
					new RelationEditionDialog();
				}
				break;
			case QUIT_APPLICATION : System.exit(0);
			case RUN_APPLICATION : 
				// No special action is to be done during launching. 
			}
        }
		catch (DuplicateElementException e) {
			Object[] options = {"OK"};
			JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Vous ne pouvez ajouter un \u00e9l\u00e9ment existant.", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		}
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
        
    /*********************************************************************************************************************************
     *  PARTIE MULTILANGUE
     *  
     * 	Julien Sanmartin
     * 
     * 	Hubert Nouhen
     * 
     * 	Le 6/06/2006
     * 
     */
	
	
	public static void loadConfig()
	{
		config = new Properties();
		try
		{
			// Reading File "lang.cfg"
			config.load(new FileInputStream ("./ontologyEditor/resources/i18n/" + LANGFILE));
		}
		catch (FileNotFoundException e)
		{			
			createLanguageFile("french");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void createLanguageFile(String s)
	{
		// Creation of the "lang.cfg" config file with the curent language
		config.clear();
		config.put("currentLanguage", s);
		sauvegarderConfig();
	}
	
	public static void sauvegarderConfig()
	{
		try
		{
			config.save(new FileOutputStream((CHEMIN + LANGFILE)), "Arkeotek Language Configuration");
		}
		catch (FileNotFoundException e)
		{
			// file writing matter
			e.printStackTrace();
		}
	}
    
    /**
     * Chargement de la langue courante sauvegardée
     * dans un fichier de propriété de l'application
     */
    public static void loadCurrentLanguage()
	{
    	currentLanguage = new Properties() ;
		try
		{
    		currentLanguage.load(new FileInputStream (CHEMIN + config.getProperty("currentLanguage") + "." + EXTENSION));
   		 	currentLang = config.getProperty("currentLanguage");
		}
    	catch (FileNotFoundException e)
		{
    		// file does not exist, we load first lang file 
			// change la langue et appelle directement charger
			if (availableLanguages.size() != 0)
			{
				ApplicationManager.getApplicationManager().setCurrentLanguage(CHEMIN + availableLanguages.elementAt(0));
			}
			else
			{
				// aucune langue disponible
				e.printStackTrace();
			}
		 }
		 catch (IOException e)
		 {
			 // problème de lecture du fichier
			 e.printStackTrace();
		 }
	}
    
    /**
	 * Retrieve all languages of the application
	 * There is 1 property file by language
	 * in the language directory
	 */
	public static void retrieveAvailableLanguages()
	{
		availableLanguages = new Vector(); // vector initialisation
		File file = new File(CHEMIN);			// file language directory
		File[] fliste = file.listFiles();		// listing of all files in the directory
		for (int i = 0 ; i < fliste.length ; i++ )
		{
			if (fliste[i].isFile())
			{
				// si le fichier courant correspond bien un fichier.lng, c'est un fichier de langue
			 	 if ( fliste[i].getName().substring(fliste[i].getName().length() - 1 - EXTENSION.length()).equals(".".concat(EXTENSION)))
			  	 {
			  		// on remplit notre liste de fichiers de langue
					availableLanguages.addElement(fliste[i].getName().substring( 0 , fliste[i].getName().length()-1- EXTENSION.length()));
			  	 }
			 }
		}
	}
	
	/**
	 * Renvoie la chaine de caractère associée à la clé s dans le fichier de configuration
	 * @param s clé dont on veut récupérer la valeur
	 * @return valeur associée à la clé dans le fichier de configuration
	 */
	public String getConfigProperty(String s)
	{
		if ( config == null )
		{
			return "" ;
		}
		String retour = config.getProperty(s);
		// si la clé n'existe pas on renvoie une chaine nulle
		if ( retour == null )
		{
			return "";
		}
		// renvoie la valeur récupérée
		return retour;
	}
	
	/**
	 * Renvoie la chaine de caractère associée à la clé s dans le fichier de langue
	 * @param s clé dont on veut récupérer la traduction
	 * @return valeur associée à la clé dans le fichier de langue
	 */
	public String getTraduction(String s)
	{
		if ( currentLanguage == null )
		{
			return ApplicationManager.ERRORSTRING;
		}
		String retour = currentLanguage.getProperty(s);
		// si la clé n'existe pas on renvoie une chaine nulle
		if ( retour == null )
		{
			return ApplicationManager.ERRORSTRING;
		}
		// renvoie la valeur récupérée
		return retour;
	}
	
	/**
	 * Renvoie la liste des langues disponibles récupérée
	 */
	public Vector getLangues()
	{
		return availableLanguages;
	}
	
	/**
	 * update current language used in the application
	 * write a new "lang.cfg" in the language directory
	 * charge le fichier de langues
	 * @param s
	 */
	public void setCurrentLanguage(String s)
	{
		// check existence of s
		if(availableLanguages.contains((Object) s))
		{
			createLanguageFile(s);
			loadCurrentLanguage();
		}
	}
	
	public Vector getAvailableLanguages() {
		return availableLanguages;
	}

	public Properties getCurrentLanguage() {
		return currentLanguage;
	}

	public static String getCurrentLang() {
		return currentLang;
	}

	public static void setCurrentLang(String currentLang) {
		ApplicationManager.currentLang = currentLang;
		createLanguageFile(getCurrentLang());
		loadConfig();
		loadCurrentLanguage();
	}
	
	/**
	 * 
	 * Fin Partie Multilanguage
	 * 
	 * @author nouhen
	 ****************************************************************************************************************************************
	 */

	
	private class GenericRunnable extends Thread
	{
		private Object argument;
		
		/**
		 * @param argument
		 */
		public GenericRunnable(Object argument)
		{
			this.argument = argument;
		}
		
		/**
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			//this method has to be implemented during the creation of the class
		}
		
		/**
		 * @return the argument
		 */
		public Object getArgument()
		{
			return this.argument;
		}
	}
	
	
	/**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ApplicationManager.retrieveAvailableLanguages();
                ApplicationManager.loadConfig();
                ApplicationManager.loadCurrentLanguage();
                ApplicationManager.getApplicationManager();
           }
        });
    }
}
