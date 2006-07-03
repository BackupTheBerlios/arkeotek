/**
 * Created on 23 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.tables;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Lemma;
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
	


	/**
	 * Initialize attributes ..
	 * list of the kind of relations ..
	 * Column name ..
	 */
	public RelationsEditorTableModel()
	{
		super(new Lemma("JeNeSertARien"));
		
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
			this.relations.add(aLR.get(i));
		}
		
		this.titre = ""; // ApplicationManager.getApplicationManager().getTraduction("relations");
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

	// Suppression des lignes désignées par un ensemble d'index
	public void delRows(int[] sr)
	{
		for(int i=sr.length-1; i>=0;i--)
		{
			int rowIndex = sr[i];
			this.relations.remove(rowIndex);
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
				relations.add(new Relation(nom));
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
	
	/*
	 * 
	 * Fin des méthodes à implémenter
	 * 
	 */
}
