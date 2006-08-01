/**
 * Created on 23 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class RelationsEditorTableModel extends EditorTableModel
{
	protected ArrayList<Object> relations = null;
	protected String titre = null;
	protected int typeRelation;
	
	/**
	 * Initialize attributes ..
	 * list of the kind of relations ..
	 * Column name ..
	 */
	public RelationsEditorTableModel(int typeRelation)
	{
		super(new Lemma("JeNeSertARien"));
		this.titre = ""; // ApplicationManager.getApplicationManager().getTraduction("relations");
		this.typeRelation=typeRelation;
		
		ArrayList<LinkableElement> aLR = null;
		
		try
		{
			aLR = ApplicationManager.ontology.get(Relation.KEY);
		}
		catch(NullPointerException exception)
		{
			JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Veuillez ouvrir une ontologie","Pas d'ontologie chargée",JOptionPane.INFORMATION_MESSAGE);
		}
		this.relations = new ArrayList<Object>();
		for(int i=0;i<aLR.size();i++)
		{
			if(((Relation)aLR.get(i)).getType()==typeRelation) this.relations.add(aLR.get(i));
		}
	}
	
	/*
	 * Méthodes à implémenter
	 * **********************
	 * 
	 */
	
	// Retourne le nombre de colonnes
	public int getColumnCount()
	{
		if (this.getElement() != null)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	// Retourne le nombre de lignes
	public int getRowCount()
	{
		if (this.getElement() != null)
			return relations.size();
		return 0;
	}
	
	// Retourne le nom de la colonne
	public String getColumnName(int column)
	{
		if(column==0)
		{
			return(this.titre);
		}
		else
		{
			return("");
		}
	}
	
	// Retourne le type d'objet de la colonne passée en paramètre
	public Class getColumnClass(int columnIndex)
	{
		return Relation.class;
	}

	// Retourne si les cellules sont éditables
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}

	// Retourne l'objet se trouvant à la ligne rowIndex et colonne columnIndex
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return this.relations.get(rowIndex);
	}

	// Comportement lors d'une modification de l'utilisateur
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		((Relation) this.relations.get(rowIndex)).setName(aValue.toString());
	}

	// Suppression des lignes désignées par un ensemble d'index
	public void delRows(int[] sr)
	{
		boolean trouve = false;
		for(int i=sr.length-1; i>=0;i--)
		{
			// Avant de supprimer les relations il faut s'assurer que celle-ci ne sont pas utilisées !
			
			int rowIndex = sr[i];
			
			// On ne supprime pas de suite la Relation
			// Car on devra aussi la supprimer de la base de donnée
			//ApplicationManager.ontology.get(Relation.KEY).remove(this.relations.get(rowIndex));
			HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>> hmap = ApplicationManager.ontology.getLinks();
			Collection coll = hmap.values();
			Iterator it = coll.iterator();
			
			while( it.hasNext() )
			{
				Set lesCles = ((HashMap<Integer, HashMap<Relation, HashMap<LinkableElement, Link>>>) it.next()).keySet() ;
				Iterator it2 = lesCles.iterator() ;
				
				if(lesCles.contains(this.relations.get(i)))
				{
					trouve = true;
					JOptionPane.showMessageDialog(DisplayManager.mainFrame,"message","title",JOptionPane.WARNING_MESSAGE);
					break;
				}
			}

			if(!trouve)
			{
				// on change l'id de la Relation
				( (Relation) this.relations.get(rowIndex)).setToDel();
				this.relations.remove(rowIndex);
			}
		}
	}

	public void addRow(String nom)
	{
		try
		{
			boolean res = false;
			for(int i=0;i<relations.size();i++)
			{
				if(nom.equals(((Relation) relations.get(i)).getName())) res=true;
			}
			if(!res)
			{
				Relation rel = new Relation(nom,typeRelation);
				ApplicationManager.ontology.get(Relation.KEY).add(rel);
				relations.add(rel);
				ApplicationManager.ontology.setDirty();
				
				System.out.println("id :" + rel.getId());
				System.out.println("nm :" + rel.getName());
				System.out.println("st :" + rel.getState());
				System.out.println("ty :" + rel.getType());
				
			}
			else
			{
				JOptionPane.showMessageDialog(DisplayManager.mainFrame,"Nom deja utilise");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void removeRelation(int lineSelected) throws Exception
	{
		int [] tab = {lineSelected};
		delRows(tab);
	}

	public void removeElement(int lineSelected) throws Exception
	{
		int [] tab = {lineSelected};
		delRows(tab);
	}
	
	
	
	/**
	 * 
	 * Fin des méthodes à implémenter
	 * 
	 */
	
	/**
	 * 
	 * Controleur
	 * 
	 */
	
	public void renameRelation()
	{
		
	}
	
	public void supprimerRelation()
	{
		
	}
	
}
