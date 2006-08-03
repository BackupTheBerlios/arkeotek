package ontologyEditor.gui.model.treeModel;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/*
 * Julien Sanmartin
 * Classe représenttant le model de l'arbre des concepts
 */

public class ConceptualTreeModel extends AbstractTreeModel {

	// retourne la racine de l'arbre
	private DefaultMutableTreeNode racine;
	
	public ConceptualTreeModel() {
		super();
		//this.racine = racine;
		// TODO Auto-generated constructor stub
	}

	public Object getRoot() {
		// TODO Auto-generated method stub
		return racine;
	}

	public Object getChild(Object arg0, int arg1) {
		// TODO Auto-generated method stub
		
		return((DefaultMutableTreeNode)arg0).getChildAt(arg1);
	}

	public int getChildCount(Object arg0) {
		// TODO Auto-generated method stub
		return((DefaultMutableTreeNode)arg0).getChildCount();
		
		//return 0;
	}

	public boolean isLeaf(Object arg0) {
		// TODO Auto-generated method stub
		return((DefaultMutableTreeNode)arg0).isLeaf();
	}

	public void valueForPathChanged(TreePath arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public int getIndexOfChild(Object arg0, Object arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub

	}

	public void removeTreeModelListener(TreeModelListener arg0) {
		// TODO Auto-generated method stub

	}

	public DefaultMutableTreeNode getRacine() {
		return racine;
	}

	public void setRacine(DefaultMutableTreeNode racine) {
		this.racine = racine;
	}
	
	// fonction qui construit l'arbre des concepts 
	public void remplirArbreConcept() {
		this.setRacine(new DefaultMutableTreeNode(ApplicationManager.ontology.toString()));
		// concepts= liste de tout les concepts
		ArrayList<LinkableElement> concepts = ApplicationManager.ontology.get(Concept.KEY);
		//ConceptualTM model=((ConceptualTM)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel());
		DefaultMutableTreeNode racine=this.getRacine();
		for (int i=0;i<concepts.size();i++)
		{
			// creation du neoud courant
			DefaultMutableTreeNode courant=new DefaultMutableTreeNode(concepts.get(i));
			creerSousNoeud(courant,concepts.get(i));
			if (ApplicationManager.ontology.getParentsOf(concepts.get(i),Concept.KEY).size()==0)
			{
				racine.add(courant);
			}
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel() instanceof ConceptualTreeModel)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().setModel(this);
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree()!=null)
		{	
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().getModel() instanceof ConceptualTreeModel)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().setModel(this);
		}
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).updateUI();
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).updateUI();
	}
	
	
	private void creerSousNoeud(DefaultMutableTreeNode courant, LinkableElement element) {
		
		ArrayList<LinkableElement> tmp=new ArrayList<LinkableElement>();
		if (element instanceof Concept)
		{
			tmp = ((Concept) element).getLinks(Concept.KEY, new Relation(Relation.DEFAULT_CONCEPTS_RELATION)); 
			
		}
		else if (element instanceof DocumentPart)
		{
			tmp = ((DocumentPart) element).getLinks(DocumentPart.KEY, new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION));
		}
		for(int j=0;j<tmp.size();j++)
		{
			DefaultMutableTreeNode fils;
			fils=new DefaultMutableTreeNode(tmp.get(j));
			creerSousNoeud(fils,tmp.get(j));
			courant.add(fils);
		}
	}

	public void supprimerNoeud(DefaultMutableTreeNode courant) {
		courant.removeFromParent();
		
	}

}
