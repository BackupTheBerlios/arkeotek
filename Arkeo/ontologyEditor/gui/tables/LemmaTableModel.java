package ontologyEditor.gui.tables;

import java.util.ArrayList;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.MainFrame;
import ontologyEditor.gui.panels.LinguisticPanel;
import ontologyEditor.gui.treeviews.ConceptualTM;
import arkeotek.ontology.Concept;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

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
