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
import ontologyEditor.gui.dialogs.FindLemmaDialog;
import ontologyEditor.gui.dialogs.OntologyWizard;
import ontologyEditor.gui.dialogs.ProgressBarDialog;
import ontologyEditor.gui.dialogs.RelationsEditorDialog;
import ontologyEditor.gui.filechoosers.OntologyFileChooser;
import ontologyEditor.gui.filechoosers.SyntexFileChooser;
import ontologyEditor.gui.filechoosers.TermontoFileChooser;
import ontologyEditor.gui.panels.LinguisticNavigationPanel;
import ontologyEditor.gui.panels.LinguisticPanel;
import sun.security.jca.GetInstance;
import arkeotek.indexing.generic.GenericIndexer;
import arkeotek.indexing.scd.SCDIndexer;
import arkeotek.io.importer.AbstractParser;
import arkeotek.io.importer.termonto.Importer;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.DuplicateElementException;
import arkeotek.ontology.IIndexable;
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
					OntologyWizard wizard = new OntologyWizard();
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
				if (ontology != null) {
					Object[] options = {"Oui", "Non"};
					choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Cette op\u00e9ration peut s'av\u00e9rer longue et le système peut être surcharg\u00e9 pendant toute cette p\u00e9riode. \nEtes vous sûr de vouloir continuer ?", "Avertissement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
					if (choice == JOptionPane.OK_OPTION) {
						SyntexFileChooser syntexFileChooser = new SyntexFileChooser();
						syntexFileChooser.showOpenDialog(DisplayManager.mainFrame);
		
						if (syntexFileChooser.getSelectedFile() != null && !syntexFileChooser.getSelectedFile().getPath().equals(SyntexFileChooser.LAST_USED_DIRECTORY)) {
							Properties prop = new Properties();
							InputStream inStream = ApplicationManager.ontology.getClass().getClassLoader().getResourceAsStream(Constants.DEFAULT_RESOURCES_PATH + "global.properties");
							OutputStream outStream = new FileOutputStream(Constants.DEFAULT_RESOURCES_PATH + "global.properties");
							try {
								prop.load(inStream);
								prop.setProperty(SyntexFileChooser.LAST_USED_DIRECTORY_KEY, syntexFileChooser.getSelectedFile().getPath());
								prop.store(outStream, "");
							} catch (IOException e) {
								// Nothing to do. 
							} finally {
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
						choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "L'ontologie courante n'a pas \u00e9t\u00e9 enregistr\u00e9e, voulez-vous le faire maintenant ?", "Modifications non enregistr\u00e9es", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
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
									ApplicationManager.ontology = new Ontology((String)((Object[]) this.getArgument())[0]);
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
				if (ontology != null) {
					if (ontology.isDirty() != null) {
						Object[] options = {"Oui", "Non", "Annuler"};
						choice = JOptionPane.showOptionDialog(DisplayManager.mainFrame, "L'ontologie courante n'a pas \u00e9t\u00e9 enregistr\u00e9e, voulez-vous le faire maintenant ?", "Modifications non enregistr\u00e9es", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
						if (choice == JOptionPane.OK_OPTION)
							ontology.save();
					}
				} else {
					this.manageRequest(Request.CREATE_NEW_ONTOLOGY);
				}
				if (choice != JOptionPane.CANCEL_OPTION) {
					TermontoFileChooser fileChooser = new TermontoFileChooser();
					fileChooser.showOpenDialog(DisplayManager.mainFrame);
	
					if (fileChooser.getSelectedFile() != null) {
						ProgressBarDialog progressBarDialog = new ProgressBarDialog(DisplayManager.mainFrame, "Chargement", true);
							
						Object[] parameters = {AbstractParser.addSlash(fileChooser.getSelectedFile().getPath()), progressBarDialog};
						GenericRunnable termontoRunnable = new GenericRunnable(parameters)
						{
							public void run()
							{
								try {
//									OldImporterT termonto = new OldImporterT(ontology, ((String)((Object[])this.getArgument())[0]));
									Importer termonto = new Importer(ontology, ((String)((Object[])this.getArgument())[0]));
									termonto.performImport();
									((ProgressBarDialog)((Object[])this.getArgument())[1]).dispose();
								} catch (DuplicateElementException e) {
									Object[] options = {"OK"};
									JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Fichiers Termonto erron\u00e9s : \u00e9l\u00e9ments dupliqu\u00e9s. ", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
								} catch (IOException e) {
									Object[] options = {"OK"};
									JOptionPane.showOptionDialog(DisplayManager.mainFrame, "Une erreur d'entr\u00e9es/sorties est survenue lors de la r\u00e9cup\u00e9ration des donn\u00e9es Termonto", "Action impossible", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
								} catch (Exception e)
								{
									e.printStackTrace();
								}
								((ProgressBarDialog)((Object[])this.getArgument())[1]).dispose();
							}
						};
						termontoRunnable.start();
						progressBarDialog.setVisible(true);
						
						ProgressBarDialog progressBarRefreshDialog = new ProgressBarDialog(DisplayManager.mainFrame, "Chargement", true);
						
						GenericRunnable refreshRunnable = new GenericRunnable(progressBarRefreshDialog)
						{
							public void run()
							{
								DisplayManager.getInstance().resetDisplay();
								((ProgressBarDialog)(this.getArgument())).dispose();
							}
						};
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
				ArrayList<LinkableElement> lemmas = DisplayManager.getInstance().getSelectedElements(Lemma.KEY);
				Object[] possibilities = lemmas.toArray();
				Lemma mainLemma = (Lemma)JOptionPane.showInputDialog(
				                    DisplayManager.mainFrame,
				                    "Choisissez le lemme \u00e0 conserver",
				                    "Fusion de lemmes",
				                    JOptionPane.PLAIN_MESSAGE,
				                    null,
				                    possibilities,
				                    lemmas.get(0));

				if (mainLemma != null) 
				{
					for (LinkableElement lemma : lemmas)
					{
						if (lemma != mainLemma)
						{
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
							int[] indexes = DisplayManager.mainFrame.getChildIndexesInTrees(lemma);
							ApplicationManager.ontology.unlink(lemma);
							DisplayManager.getInstance().removeElement(lemma, indexes);
						}
					}
					DisplayManager.getInstance().reloadGUI();
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
					// We check if the LinguisticPanel is Active
					if(DisplayManager.mainFrame.isActive(Lemma.KEY))
					{
						FindLemmaDialog lemmaSearchDialog = new FindLemmaDialog();
						lemmaSearchDialog.setVisible(true);
						String lcn = lemmaSearchDialog.getContainName();
						if ( lcn != null)
						{
							// TODO appeler la fonction de recherche qui doit se situer
							// dans le SearchAction  ...
							System.out.println("huhu : " + lcn);
							ArrayList<LinkableElement> all_lemmas = ApplicationManager.ontology.get(Lemma.KEY);
							ArrayList<LinkableElement> matching_lemmas = new ArrayList<LinkableElement>();
							for(int i=0;i<all_lemmas.size();i++)
							{
								if(((Lemma) all_lemmas.get(i)).getName().contains(lcn))
								{
									matching_lemmas.add(all_lemmas.get(i));
								}
							}
							
							// affichage pour verif
							for(int i=0;i<matching_lemmas.size();i++)
							{
								System.out.println(matching_lemmas.get(i));
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
				JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Veuillez red�marrer l'application afin que les nouveaux param�tres soient pris en compte.");
				break;
			case EDITION_RELATIONS :
				if(ApplicationManager.ontology!=null)
				{
					new RelationsEditorDialog();
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
     * Chargement de la langue courante sauvegard�e
     * dans un fichier de propri�t� de l'application
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
			 // probl�me de lecture du fichier
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
	 * Renvoie la chaine de caract�re associ�e � la cl� s dans le fichier de configuration
	 * @param s cl� dont on veut r�cup�rer la valeur
	 * @return valeur associ�e � la cl� dans le fichier de configuration
	 */
	public String getConfigProperty(String s)
	{
		if ( config == null )
		{
			return "" ;
		}
		String retour = config.getProperty(s);
		// si la cl� n'existe pas on renvoie une chaine nulle
		if ( retour == null )
		{
			return "";
		}
		// renvoie la valeur r�cup�r�e
		return retour;
	}
	
	/**
	 * Renvoie la chaine de caract�re associ�e � la cl� s dans le fichier de langue
	 * @param s cl� dont on veut r�cup�rer la traduction
	 * @return valeur associ�e � la cl� dans le fichier de langue
	 */
	public String getTraduction(String s)
	{
		if ( currentLanguage == null )
		{
			return ApplicationManager.ERRORSTRING;
		}
		String retour = currentLanguage.getProperty(s);
		// si la cl� n'existe pas on renvoie une chaine nulle
		if ( retour == null )
		{
			return ApplicationManager.ERRORSTRING;
		}
		// renvoie la valeur r�cup�r�e
		return retour;
	}
	
	/**
	 * Renvoie la liste des langues disponibles r�cup�r�e
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
