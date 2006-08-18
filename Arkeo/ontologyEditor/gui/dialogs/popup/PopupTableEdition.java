package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.dialogs.RenameConceptDialog;
import ontologyEditor.gui.model.tableModel.IndexingConceptTableModel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author sanmartin
 * permet d'afficher le popup lié à l'arbre des concepts
 */

public class PopupTableEdition extends JPopupMenu implements ActionListener{

	private JMenuItem modifier;
	private LinkableElement elementEditable;
	private LinkableElement elementCourant;
	private LinkableElement relation;

	
	public PopupTableEdition(LinkableElement elementCourant,LinkableElement elementEditable,LinkableElement relation) {
		this.elementEditable=elementEditable;
		this.elementCourant=elementCourant;
		this.relation=relation;
		this.modifier = new JMenuItem (/*ApplicationManager.getApplicationManager().getTraduction("renametheconcept")*/"Changer de relation") ;
		this.add (this.modifier) ;
		this.modifier.addActionListener (this) ;               
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// si on renomme ouverture d'une fenetre
		if (source == this.modifier)
		{
			ArrayList<LinkableElement> relations = ApplicationManager.ontology.get(Relation.KEY);
			ArrayList<LinkableElement> rela=new ArrayList<LinkableElement>();
			
			int type=-1;
			if (elementEditable instanceof Concept)
			{
				if (elementCourant instanceof Concept)
				{
					type=Relation.RELATION_CONCEPT_CONCEPT;
				}
				else if (elementCourant instanceof Lemma)
				{
					type=Relation.RELATION_TERME_CONCEPT;
				}
				else
				{
					type=Relation.RELATION_CONCEPT_DOCUMENT;
				}
			}
			if (elementEditable instanceof Lemma)
			{
				if (elementCourant instanceof Concept)
				{
					type=Relation.RELATION_TERME_CONCEPT;
				}
				else if (elementCourant instanceof Lemma)
				{
					type=Relation.RELATION_TERME_TERME;
				}
				else
				{
					type=Relation.RELATION_TERME_DOCUMENT;
				}
			}
			if (elementEditable instanceof DocumentPart)
			{
				if (elementCourant instanceof Concept)
				{
					type=Relation.RELATION_CONCEPT_DOCUMENT;
				}
				else if (elementCourant instanceof Lemma)
				{
					type=Relation.RELATION_TERME_DOCUMENT;
				}
				else
				{
					type=Relation.RELATION_DOCUMENT_DOCUMENT;
				}
			}
			// pour chaque relation
			for (LinkableElement rel:relations)
			{
				// la relation est du type Concept à Document
				if (((Relation)rel).getType()==type)
				{
					// on ajoute la relation dans la conceptToconcept
					rela.add(rel);
				}
			}
			// on tranforme l'arrayList en Object []
			Object[] rels=rela.toArray();

			if (rels.length!=0)
			{
				// on demande à l'utilisateur de choisir parmi les relations possibles
				Relation relation = (Relation)JOptionPane.showInputDialog(DisplayManager.mainFrame, 
						/*ApplicationManager.getApplicationManager().getTraduction("enternamerelation") + " : ", ApplicationManager.getApplicationManager().getTraduction("creationlink")*/"choissisez une nouvelle relation","relation", JOptionPane.INFORMATION_MESSAGE,null,
						rels, rels[0]);
				if (relation !=null)
				{
					// on créer une nouvelle relation
					try {
						elementEditable.unlink((Relation)this.relation,this.elementCourant);
						ApplicationManager.ontology.addRelation(elementEditable,elementCourant,relation);
						DisplayManager.mainFrame.mAJ(elementEditable);
						DisplayManager.mainFrame.getEditionPanel().remplirTableBasParent(elementEditable);
						DisplayManager.mainFrame.getEditionPanel().remplirTableHautParent(elementEditable);
						DisplayManager.mainFrame.getEditionPanel().updateUI();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//ApplicationManager.ontology.addRelation(element,doc,relation);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Aucune relation possible entre ces deux types d'objet");
			}
			
		}
		
	}


}
