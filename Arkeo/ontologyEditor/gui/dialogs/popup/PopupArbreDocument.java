package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.text.Position;
import javax.swing.text.Position.Bias;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.panels.corpus.CorpusNavigationPanel;
import ontologyEditor.gui.panels.corpus.CorpusPanel;
import ontologyEditor.gui.treeviews.CorpusTM;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author sanmartin
 * affiche le popup relatif à l'arbre des documents
 */

public class PopupArbreDocument extends JPopupMenu implements ActionListener{

	private JMenuItem id;
	private JMenuItem seq;
	private JMenuItem indexer;
	private LinkableElement document;
	
	public PopupArbreDocument(LinkableElement document) {
		this.document=document;
		
		this.id = new JMenuItem ("Vue Identifiant") ;
		this.add (this.id) ;
		this.id.addActionListener (this) ;               
		
		this.seq = new JMenuItem ("Vue Séquence") ;
		this.add (this.seq) ;
		this.seq.addActionListener (this) ; 
		
		this.indexer = new JMenuItem ("Indexer le document") ;
		this.add (this.indexer) ;
		this.indexer.addActionListener (this) ;
		 
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		//on regarde dans quel panel 
		
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL) instanceof CorpusPanel)
		{
			// vue par id
			if (source == this.id)
			{
				/*
				JTree tree=DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree();
				//System.out.println(tree.getSelectionPath());
				TreePath path=tree.getSelectionPath();
				ArrayList documents=ApplicationManager.ontology.get(DocumentPart.KEY);
				ArrayList<LinkableElement> doc=new ArrayList<LinkableElement>();
				for (int j=0;j<path.getPathCount();j++)
				{
					//System.out.println("path : "+path.getPathComponent(j));
					for (int i=0;i<documents.size();i++)
					{
						if(documents.get(i).toString().equals(path.getPathComponent(j).toString()))
						{
							doc.add((LinkableElement)documents.get(i));
							//System.out.println(documents.get(i));
							break;
						}
					}
				}
				String[] chemin=new String[doc.size()+1];
				chemin[0]="Corpus";
				int l=1;
				for (LinkableElement le:doc)
				{
					chemin[l]=le.getName();
					l++;
				}
				
				*/
				
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL)).setVue(1);
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).refresh();
				/*
				for 
				tree.setSelectionPath(new TreePath(chemin));
				System.out.println("Nouveau path : "+tree.getSelectionPath());
				tree.expandPath(tree.getSelectionPath());*/
				/*if (doc!=null)
				{
					//tree.get
					tree.setSelectionPath(tree.getNextMatch(doc.getName(),0,Position.Bias.Forward));
					tree.expandPath(tree.getNextMatch(doc.getName(),0,Position.Bias.Forward));
					System.out.println(tree.getSelectionPath());
					tree.updateUI();
				}*/
				
				
				
				
			}
			//vue par seq 
			else if (source == this.seq)
			{
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL)).setVue(0);
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).refresh();
			}
			// indexation du document
			else if (source == this.indexer)
			{
				DefaultMutableTreeNode noeud=(DefaultMutableTreeNode)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().getLastSelectedPathComponent();
				indexer(noeud);
				((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getNavigationPanel()).remplirTableConceptIndexant(this.document);
				((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getNavigationPanel()).remplirTableConceptPotentiel(this.document);
			}
		}
		
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL) instanceof CorpusPanel)
		{
			// vue par id
			if (source == this.id)
			{
				//((CorpusTM)((AbstractPanel)DisplayManager.mainFrame.getPanel(panel)).getTree().getModel()).remplirArbreDocumentId();
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL)).setVue(1);
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).refresh();
			}
			//vue par seq 
			else if (source == this.seq)
			{
				((CorpusPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL)).setVue(0);
				//((CorpusTM)((AbstractPanel)DisplayManager.mainFrame.getPanel(panel)).getTree().getModel()).remplirArbreDocumentSeq();
				DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).refresh();
			}
			//indexation du document
			else if (source == this.indexer)
			{
				DefaultMutableTreeNode noeud=(DefaultMutableTreeNode)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().getLastSelectedPathComponent();
				indexer(noeud);
				((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getNavigationPanel()).remplirTableConceptIndexant(this.document);
				((CorpusNavigationPanel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getNavigationPanel()).remplirTableConceptPotentiel(this.document);
			}
		}
		
	}
	
	public void indexer(DefaultMutableTreeNode noeud)
	{
		ArrayList<TreeNode> noeudFils=new ArrayList<TreeNode>();
		// selection des noeud fils du doc courant
		for (int i=0;i<noeud.getChildCount();i++)
		{
			noeudFils.add(noeud.getChildAt(i));
		}
		
		// selection des document associé au noeud
		ArrayList<LinkableElement> documentPart=ApplicationManager.ontology.get(DocumentPart.KEY);
		ArrayList<LinkableElement> documentFils=new ArrayList<LinkableElement>();
		for (LinkableElement doc:documentPart)
		{
			for (int j=0;j<noeudFils.size();j++)
			{
				if (doc.toString().equals(noeudFils.get(j).toString()))
				{
					documentFils.add(doc);
				}
			}
		}
		
		// selection de tous les concept associé a chaque doc
		ArrayList<ArrayList<LinkableElement>> conceptToDoc=new ArrayList<ArrayList<LinkableElement>>();
		for (LinkableElement doc:documentFils)
		{
			ArrayList<LinkableElement> c=new ArrayList<LinkableElement>();
			ArrayList<Object[]> getr=ApplicationManager.ontology.getElementsThatReference(doc);
			for (int p=0;p<getr.size();p++)
			{
				for (Object o:getr.get(p))
				{
					if (o instanceof Concept)
					{
						c.add((LinkableElement)o);
					}
				}
			}
			conceptToDoc.add(c);
			
		}
		
		// selection des concepts communs à tout les documents
		if (conceptToDoc.size()!=0)
		{
			ArrayList<LinkableElement> concepts=new ArrayList<LinkableElement>();
			if (conceptToDoc.size()>1)
			{
				ArrayList<LinkableElement> premier=conceptToDoc.get(0);
				for (LinkableElement le:premier)
				{
					int n=1;
					for (int i=1;i<conceptToDoc.size();i++)
					{
						for (LinkableElement l:conceptToDoc.get(i))
						{
							if (le.toString().equals(l.toString()))
							{
								n++;
							}
						}
					}
					if (n==conceptToDoc.size())
					{
						concepts.add(le);
					}
				}
			}
			else if (conceptToDoc.size()==1)
			{
				concepts=conceptToDoc.get(0);
			}
			
			// on ajoute une relation entre le document et le concept
			if (concepts.size()!=0)
			{
				ArrayList<LinkableElement> relations=ApplicationManager.ontology.get(Relation.KEY);
				Relation relation=new Relation("");
				for (LinkableElement rel:relations)
				{
					if (((Relation)rel).getType()==Relation.RELATION_CONCEPT_DOCUMENT)
					{
						relation=(Relation)rel;
					}
				}
				for (LinkableElement le:concepts)
				{
					try {
						ApplicationManager.ontology.addRelation(this.document,le,relation);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						ApplicationManager.ontology.addRelation(le,this.document,relation);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			JOptionPane.showMessageDialog(DisplayManager.mainFrame,concepts.size()+" concept(s) trouvé(s) pour le document "+this.document.getName());
			
		}
	}

}
