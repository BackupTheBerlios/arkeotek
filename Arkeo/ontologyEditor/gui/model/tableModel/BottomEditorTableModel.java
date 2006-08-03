package ontologyEditor.gui.model.tableModel;

import javax.swing.table.AbstractTableModel;

/*
 *  Julien Snamartin
 *  Classe représentant le model pour la table BottomEditor
 */

public class BottomEditorTableModel extends AbstractTableModel {

	// nom des colonnes
	private String[] columnNames;
	// donnees contenu dans la table
	private Object[][] donnees;
	
	public BottomEditorTableModel() {
		super();
		//super(element);
		// TODO Auto-generated constructor stub
	}
	
	public BottomEditorTableModel(Object [][] donnees,String[] columnNames) {
		super();
		this.columnNames=columnNames;
		this.donnees=donnees;
		// TODO Auto-generated constructor stub
	}

	// indique le nombre de lignes dans la table
	public int getRowCount() {
		// TODO Auto-generated method stub
		if (donnees!=null)
		{
			return  donnees.length;
		}
		else
		{
			return 0;
		}
	}

	// retourne le nombre de colonne dans la table
	public int getColumnCount() {
		// TODO Auto-generated method stub
		if (columnNames!=null)
		{
			return  columnNames.length;
		}
		else
		{
			return 0;
		}
	}

	// retourne la valeur a la position arg0, arg1 de donnees
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return donnees[arg0][arg1];
	}

	// retourne le nom de la colonne situé à la position c
	public String getColumnName(int c){
		   return this.columnNames[c];
	}

	//setter
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	// getter
	public Object[][] getDonnees() {
		return donnees;
	}

	// setter
	public void setDonnees(Object[][] donnees) {
		this.donnees = donnees;
	}

}
