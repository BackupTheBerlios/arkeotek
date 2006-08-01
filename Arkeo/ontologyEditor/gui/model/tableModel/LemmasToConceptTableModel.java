package ontologyEditor.gui.model.tableModel;

import javax.swing.table.AbstractTableModel;

public class LemmasToConceptTableModel extends AbstractTableModel {

	private Object[][] donnees;
	private String[] columnNames;
	
	public LemmasToConceptTableModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LemmasToConceptTableModel(Object[][]donnees) {
		super();
		this.donnees=donnees;
	}
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

	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return donnees[arg0][arg1];
	}

	public String getColumnName(int c){
		   return this.columnNames[c];
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public Object[][] getDonnees() {
		return donnees;
	}

	public void setDonnees(Object[][] donnees) {
		this.donnees = donnees;
	}


}