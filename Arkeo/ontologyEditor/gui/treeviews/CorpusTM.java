package ontologyEditor.gui.treeviews;

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

public class CorpusTM extends AbstractTM {
	private DefaultMutableTreeNode racine;
	
	public CorpusTM() {
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
	
	public void remplirArbreDocumentSeq() {
		// concepts= liste de tout les concepts
		this.setRacine(new DefaultMutableTreeNode("Corpus"));
		ArrayList<LinkableElement> documents = ApplicationManager.ontology.get(DocumentPart.KEY);
		for (int i=0;i<documents.size();i++)
		{
			// creation du neoud courant
			DefaultMutableTreeNode courant=new DefaultMutableTreeNode(documents.get(i));
			creerSousNoeudSeq(courant,documents.get(i));
			if (ApplicationManager.ontology.getParentsOf(documents.get(i),DocumentPart.KEY).size()==0)
			{
				racine.add(courant);
			}
		}
		//System.out.println(DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel());
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel() instanceof CorpusTM)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().setModel(this);
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().getModel() instanceof CorpusTM)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().setModel(this);
		}
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).updateUI();
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).updateUI();
	}
	
	private void creerSousNoeudSeq(DefaultMutableTreeNode courant, LinkableElement element) {
		
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
			creerSousNoeudSeq(fils,tmp.get(j));
			courant.add(fils);
		}
	}
	
	public void remplirArbreDocumentId() {
		// concepts= liste de tout les concepts
		this.setRacine(new DefaultMutableTreeNode("Corpus"));
		ArrayList<LinkableElement> documents = ApplicationManager.ontology.get(DocumentPart.KEY);
		for (int i=0;i<documents.size();i++)
		{
			// creation du neoud courant
			DefaultMutableTreeNode courant=new DefaultMutableTreeNode(documents.get(i).getName());
			creerSousNoeudId(courant,documents.get(i));
			if (ApplicationManager.ontology.getParentsOf(documents.get(i),DocumentPart.KEY).size()==0)
			{
				racine.add(courant);
			}
		}
		//System.out.println(DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel());
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getModel() instanceof CorpusTM)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().setModel(this);
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree()!=null)
		{
			if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().getModel() instanceof CorpusTM)
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().setModel(this);
		}
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).updateUI();
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).updateUI();
	}
	
	private void creerSousNoeudId(DefaultMutableTreeNode courant, LinkableElement element) {
		
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
			fils=new DefaultMutableTreeNode(tmp.get(j).getName());
			creerSousNoeudId(fils,tmp.get(j));
			courant.add(fils);
		}
	}

}
