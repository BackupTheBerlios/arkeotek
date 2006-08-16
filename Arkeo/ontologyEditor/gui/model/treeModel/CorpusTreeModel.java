package ontologyEditor.gui.model.treeModel;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/*
 * Julien Sanmartin
 * Classe représenttant le model de l'arbre des documents
 */

public class CorpusTreeModel extends AbstractTreeModel {
	// racine de l'arbre
	private DefaultMutableTreeNode racine;
	
	private int vue=0;
	
	public CorpusTreeModel() {
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
	
	//focntion permettant de construire l'arbre des documents sous la forme de sequence
	public void remplirArbreDocumentSeq() {
		// on creer la racine
		this.setRacine(new DefaultMutableTreeNode("Corpus"));
		// on selectionne tous les documents 
		ArrayList<LinkableElement> documents = ApplicationManager.ontology.get(DocumentPart.KEY);
		// pour chaque document
		for (int i=0;i<documents.size();i++)
		{
			if (documents.get(i).getState()==LinkableElement.VALIDATED)
	        {
	        	ApplicationManager.ontology.getDocValider().add(documents.get(i));
	        }
			// creation du neoud courant
			DefaultMutableTreeNode courant=new DefaultMutableTreeNode(documents.get(i));
			// creation des noued fils par récursivité
			creerSousNoeudSeq(courant,documents.get(i));
			// si le noued courant n'a pas de pere on le rattache à la racine
			if (ApplicationManager.ontology.getParentsOf(documents.get(i),DocumentPart.KEY).size()==0)
			{
				racine.add(courant);
			}
		}
		// on rempli le model
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel() instanceof CorpusTreeModel)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().setModel(this);
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().getModel() instanceof CorpusTreeModel)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().setModel(this);
		}
		// modification dans l'arbre
		for (int i=0;i<racine.getChildCount();i++)
		{
			remodelage((DefaultMutableTreeNode)racine.getChildAt(i));
		}
		
		
		
		// MAJ de l'interface
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).updateUI();
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).updateUI();
	}
	
	private void creerSousNoeudSeq(DefaultMutableTreeNode courant, LinkableElement element) {
		
		ArrayList<LinkableElement> tmp=new ArrayList<LinkableElement>();
		// pour l'arbre des concepts mais n'est pas utilisé ici
		if (element instanceof Concept)
		{
			tmp = ((Concept) element).getLinks(Concept.KEY, new Relation(Relation.DEFAULT_CONCEPTS_RELATION)); 
			
		}
		// on selectionne tout les liens du documents courant
		else if (element instanceof DocumentPart)
		{
			tmp = ((DocumentPart) element).getLinks(DocumentPart.KEY, new Relation(Relation.DEFAULT_DOCUMENT_DOWNGOING_RELATION));
		}
		// pour chaque liens
		for(int j=0;j<tmp.size();j++)
		{
			if (!tmp.get(j).getName().contains("-COM"))
			{
				// creation du fils 
				DefaultMutableTreeNode fils;
				fils=new DefaultMutableTreeNode(tmp.get(j));
				// recursivité avec le fils
				creerSousNoeudSeq(fils,tmp.get(j));
				//on ajoute le fils au noeud courant
				courant.add(fils);
			}
			else if (tmp.get(j).getName().contains("-COM-"))
			{
				if (!((DocumentPart)element).getImages().contains((DocumentPart)tmp.get(j)))
					((DocumentPart)element).getImages().add((DocumentPart)tmp.get(j));
			}
			else if (tmp.get(j).getName().contains("-COM"))
			{
				((DocumentPart)element).setCommentaire((DocumentPart)tmp.get(j));
			}
		}
	}
	
	
	private void remodelage(DefaultMutableTreeNode noeud)
	{
		for (int i=0;i<noeud.getChildCount();i++)
		{
			if (noeud.getChildAt(i).isLeaf())
			{
				((DocumentPart)((DefaultMutableTreeNode)noeud.getChildAt(i)).getUserObject()).setCommentaire(((DocumentPart)noeud.getUserObject()).getCommentaire());
				noeud.setUserObject(((DefaultMutableTreeNode)noeud.getChildAt(i)).getUserObject());
				((DefaultMutableTreeNode)noeud.getChildAt(i)).removeFromParent();
				i=i-1;
			}
			else
			{
				remodelage((DefaultMutableTreeNode)noeud.getChildAt(i));
			}
		}
	}
	

	public int getVue() {
		// TODO Auto-generated method stub
		return this.vue;
	}
	
	public void setVue(int vue) {
		// TODO Auto-generated method stub
		this.vue=vue;
	}

}
