package ontologyEditor.gui.model.tableModel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/*
 *  Julien Snamartin
 *  Classe représentant le model pour la table LemmaTableModel
 */

public class LemmaTableModel extends AbstractTableModel {

	
	private String[] columnNames;
	private Object[][] donnees;
	
	public LemmaTableModel() {
		super();
		//super(element);
		// TODO Auto-generated constructor stub
	}
	
	public LemmaTableModel(Object [][] donnees,String[] columnNames) {
		super();
		this.columnNames=columnNames;
		this.donnees=donnees;
		// TODO Auto-generated constructor stub
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
	
	public void remplirTableLemme()
	{
		ArrayList<LinkableElement> listLemme=ApplicationManager.ontology.get(Lemma.KEY);
		
		Object [][] donnees=new Object[listLemme.size()][1];
		for (int i=0;i<listLemme.size();i++)
		{
//			modif aldo 21/07/06
	        if (listLemme.get(i).getState()==LinkableElement.VALIDATED)
	        {
	        	ApplicationManager.ontology.getLemmeValider().add(listLemme.get(i));
	        }
			donnees[i][0]=listLemme.get(i);
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTable()!=null)
		{
			((LemmaTableModel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTable().getModel()).setDonnees(donnees);
		}
		if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable()!=null)
		{
				((LemmaTableModel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getModel()).setDonnees(donnees);
		}
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).updateUI();
		DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).updateUI();
		//((LemmaTableModel)DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getModel()).setDonnees(donnees);
	}
}
