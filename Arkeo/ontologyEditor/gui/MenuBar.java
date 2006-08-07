/**
 * Created on 19 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import ontologyEditor.ActionManager;
import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class MenuBar extends JMenuBar
{
	public static ButtonGroup GROUPLANG = null;
    /**
     * Creates and initializes a <code>MenuBar</code> customized for the ontologyEditor. 
     * @throws InterruptedException 
     */
    public MenuBar()
    {
        super();
        
        // "File" menu creation. 
		JMenu mFile = new JMenu(ApplicationManager.getApplicationManager().getTraduction("file"));

		JMenuItem miNew = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_ONTOLOGY));
		miNew.setText(ApplicationManager.getApplicationManager().getTraduction("newontology"));
        mFile.add(miNew);

		mFile.addSeparator();

		JMenuItem miOpen = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_OPEN));
		miOpen.setText(ApplicationManager.getApplicationManager().getTraduction("openontology"));
        mFile.add(miOpen);
		
		JMenuItem miTermontoOpen = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_OPEN_FROM_TERMONTO));
		miTermontoOpen.setText(ApplicationManager.getApplicationManager().getTraduction("importtermontoontology"));
        mFile.add(miTermontoOpen);

        JMenuItem miSave = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_SAVE));
		miSave.setText(ApplicationManager.getApplicationManager().getTraduction("saveontology"));
        mFile.add(miSave);

		mFile.addSeparator();
		
        JMenuItem miQuit = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_QUIT));
		miQuit.setText(ApplicationManager.getApplicationManager().getTraduction("quit"));
        mFile.add(miQuit);

        this.add(mFile);

		// "Edit" menu creation. 
		JMenu mEdit = new JMenu(ApplicationManager.getApplicationManager().getTraduction("edition"));

        JMenuItem miNewConcept = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_CONCEPT));
		miNewConcept.setText(ApplicationManager.getApplicationManager().getTraduction("newconcept"));
        mEdit.add(miNewConcept);

        JMenuItem miNewLemma = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_LEMMA));
		miNewLemma.setText(ApplicationManager.getApplicationManager().getTraduction("newterm"));
        mEdit.add(miNewLemma);
		
		JMenuItem miNewRelation = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_RELATION));
		miNewRelation.setText(ApplicationManager.getApplicationManager().getTraduction("newrelation"));
        mEdit.add(miNewRelation);

		mEdit.addSeparator();
		
        JMenuItem miBreed = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_BREED));
		miBreed.setText(ApplicationManager.getApplicationManager().getTraduction("breed"));
        mEdit.add(miBreed);
		
		mEdit.addSeparator();
		
		JMenuItem miFusion = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_LEMMAS_FUSION));
		miFusion.setText(ApplicationManager.getApplicationManager().getTraduction("mergeterms"));
        mEdit.add(miFusion);
		
		JMenuItem miIndexation = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_GENERIC_INDEXATION));
		miIndexation.setText(ApplicationManager.getApplicationManager().getTraduction("indexdocumentgeneric"));
        mEdit.add(miIndexation);
        
		JMenuItem miEditionRelations = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_EDITION_RELATIONS));
		miEditionRelations.setText(ApplicationManager.getApplicationManager().getTraduction("editionrelations"));
        mEdit.add(miEditionRelations);

        this.add(mEdit);

		// "View" menu creation
		JMenu mView = new JMenu(ApplicationManager.getApplicationManager().getTraduction("display"));
		
		// "Top pannel" sub menu
		JMenu miTopPannel = new JMenu();
		miTopPannel.setText(ApplicationManager.getApplicationManager().getTraduction("toppanel"));
		
		JMenuItem miTopChangeToConcepts = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_TOP_PANNEL_CONCEPTS));
		miTopChangeToConcepts.setText(ApplicationManager.getApplicationManager().getTraduction("conceptview"));
		miTopPannel.add(miTopChangeToConcepts);

		JMenuItem miTopChangeToLemmas = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_TOP_PANNEL_LEMMAS));
		miTopChangeToLemmas.setText(ApplicationManager.getApplicationManager().getTraduction("termview"));
		miTopPannel.add(miTopChangeToLemmas);

		JMenuItem miTopChangeToDocuments = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_TOP_PANNEL_DOCUMENTS));
		miTopChangeToDocuments.setText(ApplicationManager.getApplicationManager().getTraduction("documentview"));
		miTopPannel.add(miTopChangeToDocuments);
		
		mView.add(miTopPannel);

		// "Bottom pannel" sub menu
		JMenu miBottomPannel = new JMenu();
		miBottomPannel.setText(ApplicationManager.getApplicationManager().getTraduction("bottompanel"));
		
		JMenuItem miBottomChangeToConcepts = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_CONCEPTS));
		miBottomChangeToConcepts.setText(ApplicationManager.getApplicationManager().getTraduction("conceptview"));
		miBottomPannel.add(miBottomChangeToConcepts);

		JMenuItem miBottomChangeToLemmas = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_LEMMAS));
		miBottomChangeToLemmas.setText(ApplicationManager.getApplicationManager().getTraduction("termview"));
		miBottomPannel.add(miBottomChangeToLemmas);

		JMenuItem miBottomChangeToDocuments = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_DOCUMENTS));
		miBottomChangeToDocuments.setText(ApplicationManager.getApplicationManager().getTraduction("documentview"));
		miBottomPannel.add(miBottomChangeToDocuments);
		
		mView.add(miBottomPannel);
		
		mView.addSeparator();
		
		// "Language" sub menu
		JMenu miLanguage = new JMenu();
		miLanguage.setText(ApplicationManager.getApplicationManager().getTraduction("language"));
		
		GROUPLANG = new ButtonGroup();
		
		ApplicationManager app = ApplicationManager.getApplicationManager();
	
		for(int i=0;i<app.getAvailableLanguages().size();i++)
		{
			JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_LANGUAGE));
			String lang = (String) app.getAvailableLanguages().get(i);
			rbMenuItem.setText(app.getTraduction(lang)); rbMenuItem.setName(lang);
			rbMenuItem.setSelected(ApplicationManager.getCurrentLang().equals(lang));
			GROUPLANG.add(rbMenuItem);
			miLanguage.add(rbMenuItem);
		}
		
		mView.add(miLanguage);
		
		
		this.add(mView);
		
		// "Search" menu pour la recherche. 
		JMenu mSearch = new JMenu(ApplicationManager.getApplicationManager().getTraduction("search"));

		JMenuItem miSearchLemma = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_SEARCH_LEMMA));
		miSearchLemma.setText(ApplicationManager.getApplicationManager().getTraduction("findterm"));
        mSearch.add(miSearchLemma);

        this.add(mSearch);

		// "Help" menu creation. 
        JMenu mHelp = new JMenu("?");

        JMenuItem miAPropos = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_APROPOS));
		miAPropos.setText(ApplicationManager.getApplicationManager().getTraduction("aboutarkeotek"));
        mHelp.add(miAPropos);
        
        this.add(mHelp);
    }

}
