/**
 * Created on 19 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import ontologyEditor.ActionManager;
import ontologyEditor.Constants;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class MenuBar extends JMenuBar
{
    /**
     * Creates and initializes a <code>MenuBar</code> customized for the ontologyEditor. 
     */
    public MenuBar()
    {
        super();
		// "File" menu creation. 
		JMenu mFile = new JMenu("Fichier");

		JMenuItem miNew = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_ONTOLOGY));
		miNew.setText("Nouvelle ontologie... ");
        mFile.add(miNew);

		mFile.addSeparator();

		JMenuItem miOpen = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_OPEN));
		miOpen.setText("Ouvrir");
        mFile.add(miOpen);
		
		JMenuItem miTermontoOpen = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_OPEN_FROM_TERMONTO));
		miTermontoOpen.setText("Importer une ontologie Termonto");
        mFile.add(miTermontoOpen);

        JMenuItem miSave = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_SAVE));
		miSave.setText("Enregistrer");
        mFile.add(miSave);

		mFile.addSeparator();
		
        JMenuItem miQuit = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_QUIT));
		miQuit.setText("Quitter");
        mFile.add(miQuit);

        this.add(mFile);

		// "Edit" menu creation. 
		JMenu mEdit = new JMenu("Edition");

        JMenuItem miNewConcept = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_CONCEPT));
		miNewConcept.setText("Nouveau concept... ");
        mEdit.add(miNewConcept);

        JMenuItem miNewLemma = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_LEMMA));
		miNewLemma.setText("Nouveau lemme... ");
        mEdit.add(miNewLemma);
		
		JMenuItem miNewRelation = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_NEW_RELATION));
		miNewRelation.setText("Nouvelle relation... ");
        mEdit.add(miNewRelation);

		mEdit.addSeparator();
		
        JMenuItem miBreed = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_BREED));
		miBreed.setText("Enrichir... ");
        mEdit.add(miBreed);
		
		mEdit.addSeparator();
		
		JMenuItem miFusion = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_LEMMAS_FUSION));
		miFusion.setText("Fusionner les lemmes");
        mEdit.add(miFusion);
		
		JMenuItem miIndexation = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_GENERIC_INDEXATION));
		miIndexation.setText("Indexer le document (g\u00e9n\u00e9rique)");
        mEdit.add(miIndexation);

        this.add(mEdit);

		// "View" menu creation
		JMenu mView = new JMenu("Affichage");
		
		// "Top pannel" sub menu
		JMenu miTopPannel = new JMenu();
		miTopPannel.setText("Panneau sup\u00e9rieur");
		
		JMenuItem miTopChangeToConcepts = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_TOP_PANNEL_CONCEPTS));
		miTopChangeToConcepts.setText("Vue par concepts");
		miTopPannel.add(miTopChangeToConcepts);

		JMenuItem miTopChangeToLemmas = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_TOP_PANNEL_LEMMAS));
		miTopChangeToLemmas.setText("Vue par termes");
		miTopPannel.add(miTopChangeToLemmas);

		JMenuItem miTopChangeToDocuments = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_TOP_PANNEL_DOCUMENTS));
		miTopChangeToDocuments.setText("Vue par documents");
		miTopPannel.add(miTopChangeToDocuments);
		
		mView.add(miTopPannel);

		// "Bottom pannel" sub menu
		JMenu miBottomPannel = new JMenu();
		miBottomPannel.setText("Panneau inf\u00e9rieur");
		
		JMenuItem miBottomChangeToConcepts = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_CONCEPTS));
		miBottomChangeToConcepts.setText("Vue par concepts");
		miBottomPannel.add(miBottomChangeToConcepts);

		JMenuItem miBottomChangeToLemmas = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_LEMMAS));
		miBottomChangeToLemmas.setText("Vue par termes");
		miBottomPannel.add(miBottomChangeToLemmas);

		JMenuItem miBottomChangeToDocuments = new JMenuItem(ActionManager.getActionManager().getAction(Constants.ACTION_CHANGE_BOTTOM_PANNEL_DOCUMENTS));
		miBottomChangeToDocuments.setText("Vue par documents");
		miBottomPannel.add(miBottomChangeToDocuments);
		
		mView.add(miBottomPannel);
		
		this.add(mView);
		
		// "Search" menu pour la recherche. 
		JMenu mSearch = new JMenu("Recherche");

		JMenuItem miSearchLemma = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_SEARCH_LEMMA));
		miSearchLemma.setText("Recherche de lemme");
        mSearch.add(miSearchLemma);

        this.add(mSearch);

		// "Help" menu creation. 
        JMenu mHelp = new JMenu("?");

        JMenuItem miAPropos = new JMenuItem(ActionManager.getActionManager().getAction(
                Constants.ACTION_APROPOS));
		miAPropos.setText("A propos");
        mHelp.add(miAPropos);
        
        this.add(mHelp);
    }

}
