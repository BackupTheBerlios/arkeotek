/**
 * Created on 16 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels.corpus;

import info.clearthought.layout.TableLayout;

import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.model.tableModel.ImagesToDocumentPartTableModel;
import ontologyEditor.gui.model.tableModel.IndexingConceptTableModel;
import ontologyEditor.gui.model.tableModel.LemmasToDocumentPartTableModel;
import ontologyEditor.gui.model.tableModel.PotentialConceptTableModel;
import ontologyEditor.gui.panels.AbstractNavigationPanel;
import ontologyEditor.gui.panels.linguistic.LinguisticNavigationPanel;
import ontologyEditor.gui.transfers.ConceptDropTransferHandler;
import ontologyEditor.gui.transfers.ConceptIndexingDragTranferHandler;
import ontologyEditor.gui.transfers.TransferableConcept;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * Julien sanmartin
 * Classe permettant d'afficher le panneau de navigation d'un document 
 */
public class CorpusNavigationPanel extends AbstractNavigationPanel
{
	private JTextArea txtArea_docText;
	private JTextArea txtArea_docComm;
	private JTable imagesTable;
	private JTable conceptsIndexingTable;
	private JTable potentialConceptsTable;
	private JTable termeAssocie;
	private JButton validationButton;
	// element courant selectionner
	private LinkableElement doc;

	/**
	 * 
	 */
	public CorpusNavigationPanel()
	{
		super();
		// Create a TableLayout for the panel
		double border = 7;
		double sizeNavPanel[][] = { 
				{ border, 400,border, TableLayout.FILL,border, TableLayout.FILL, border/*, TableLayout.FILL, TableLayout.PREFERRED, border */}, // Columns
				{ border, TableLayout.FILL, border, TableLayout.FILL,border, TableLayout.FILL,border,TableLayout.FILL, border } // Rows 
			};
		this.setLayout(new TableLayout(sizeNavPanel));
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("detail")));

		this.txtArea_docText = new JTextArea("");
		this.txtArea_docText.setLineWrap(true);
		this.txtArea_docText.setEditable(false);
		JScrollPane areaSP = new JScrollPane(this.txtArea_docText);
		areaSP.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("seq")));
		this.add(areaSP, "1, 1, 1, 1");
		
		this.txtArea_docComm = new JTextArea("");
		this.txtArea_docComm.setLineWrap(true);
		this.txtArea_docComm.setEditable(false);
		JScrollPane areaC = new JScrollPane(this.txtArea_docComm);
		areaC.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("com")));
		this.add(areaC, "1, 3, 1, 1");
		
		String[] Titre={ApplicationManager.getApplicationManager().getTraduction("relation"),ApplicationManager.getApplicationManager().getTraduction("element")};
		IndexingConceptTableModel tableCIModel = new IndexingConceptTableModel();
		tableCIModel.setColumnNames(Titre);
		this.conceptsIndexingTable = new JTable(tableCIModel);
		this.conceptsIndexingTable.setTransferHandler(new ConceptDropTransferHandler());
		this.conceptsIndexingTable.setDragEnabled(true);
		this.conceptsIndexingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.conceptsIndexingTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		TableColumn column1 = conceptsIndexingTable.getColumnModel().getColumn(0);
		column1.setPreferredWidth(65);
		TableColumn column2 = conceptsIndexingTable.getColumnModel().getColumn(1);
		column2.setPreferredWidth(150);
		this.conceptsIndexingTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				//LinkableElement selected = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 1));
				//reflectNavigation(selected);
			}
		});
		this.conceptsIndexingTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				TransferHandler handler = CorpusNavigationPanel.this.conceptsIndexingTable.getTransferHandler();
				handler.exportAsDrag(CorpusNavigationPanel.this.conceptsIndexingTable, e, TransferHandler.COPY);
			}
		});
		JScrollPane jsp = new JScrollPane(this.conceptsIndexingTable);
		jsp.setDropTarget(new DropTarget(jsp, new DropTargetAdapter()
				{

			/**
			 * @see java.awt.dnd.DropTargetAdapter#dragEnter(java.awt.dnd.DropTargetDragEvent)
			 */
			@Override
			public void dragEnter(DropTargetDragEvent dtde)
			{
				Transferable transferable = dtde.getTransferable();
				if (transferable.isDataFlavorSupported(TransferableConcept.conceptFlavor))
					super.dragEnter(dtde);
				else
					dtde.rejectDrag();
			}

			public void drop(DropTargetDropEvent dtde)
			{
				try {
					
		            Transferable transferable = dtde.getTransferable();
		            if (transferable.isDataFlavorSupported(TransferableConcept.conceptFlavor))
		            {
						CorpusNavigationPanel.this.conceptsIndexingTable.getTransferHandler().importData(CorpusNavigationPanel.this.conceptsIndexingTable, transferable);
		            }
		            dtde.rejectDrop();
		            dtde.dropComplete(false);
		        }       
		        catch (Exception e) {   
		            dtde.rejectDrop();
		            dtde.dropComplete(false);
		        } 
		    }
			
		}));
		jsp.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("conceptsindexing")));
		this.add(jsp, "3, 1, 1, 3");
		
		
		// table des lemmes associé
		String[] TitreL={ApplicationManager.getApplicationManager().getTraduction("element")};
		LemmasToDocumentPartTableModel tableLemmeModel = new LemmasToDocumentPartTableModel();
		tableLemmeModel.setColumnNames(TitreL);
		this.termeAssocie = new JTable(tableLemmeModel);
		this.termeAssocie.setTransferHandler(new ConceptDropTransferHandler());
		this.termeAssocie.setDragEnabled(true);
		this.termeAssocie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.termeAssocie.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		JScrollPane jsp2 = new JScrollPane(this.termeAssocie);
		jsp2.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("termsrelateddocument")));
		this.add(jsp2, "5, 1, 1, 5");
		this.termeAssocie.addMouseListener(new MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 0));
				//termeAssocie.setToolTipText(currentElement.getLinks(element).get(0).getName()+" "+currentElement.getName());
				termeAssocie.setToolTipText(element.getLinks(doc).get(0).toString());
				if (e.getClickCount() >= 2)
				{
					int panel=-1;
					if ((DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getNavigationPanel() instanceof LinguisticNavigationPanel) && (e.getComponent().getParent().getParent().getParent().getParent().getParent().getY()==1))
					{
						panel=DisplayManager.mainFrame.BOTTOM_PANEL;
					}
					else if ((DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getNavigationPanel() instanceof LinguisticNavigationPanel) && (e.getComponent().getParent().getParent().getParent().getParent().getParent().getY()!=1))
					{
						panel=DisplayManager.mainFrame.TOP_PANEL;
					}
					if (panel!=-1)
					{
						// selection de la ligne correpondant au lemme selectionné
						for (int i=0;i<DisplayManager.mainFrame.getPanel(panel).getTable().getRowCount();i++)
						{
							if (DisplayManager.mainFrame.getPanel(panel).getTable().getValueAt(i,0).toString().equals(element.toString()))
							{
								DisplayManager.mainFrame.getPanel(panel).getTable().setRowSelectionInterval(i,i);
								
							}
						}
						// remplisssage de navigation panel
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableLemmeParent(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableLemmeLier(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableConcept(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).remplirTableDocumentParent(element);
						((LinguisticNavigationPanel) DisplayManager.mainFrame.getPanel(panel).getNavigationPanel()).getPrecedent().add(element);
					}
				}
				//DisplayManager.getInstance().reflectNavigation(element);
			}
		});
		
		
		
		
		PotentialConceptTableModel tableCPModel = new PotentialConceptTableModel();
		tableCPModel.setColumnNames(Titre);
		this.potentialConceptsTable = new JTable(tableCPModel);
		this.potentialConceptsTable.setTransferHandler(new ConceptIndexingDragTranferHandler());
		this.potentialConceptsTable.setDragEnabled(true);
		this.potentialConceptsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.potentialConceptsTable.setDefaultRenderer(String.class, new DefaultTableCellRenderer());
		TableColumn column13 = potentialConceptsTable.getColumnModel().getColumn(0);
		column13.setPreferredWidth(65);
		TableColumn column23 = potentialConceptsTable.getColumnModel().getColumn(1);
		column23.setPreferredWidth(150);
		this.potentialConceptsTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				String value=(String)((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 0);
				LinkableElement selected; 
				for (int i=0;i<ApplicationManager.ontology.get(Concept.KEY).size();i++)
				{
					if (ApplicationManager.ontology.get(Concept.KEY).get(i).toString().equals(value))
					{
						selected=ApplicationManager.ontology.get(Concept.KEY).get(i);
						reflectNavigation(selected);
					}
				}
			}
		});
		this.potentialConceptsTable.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{	
				TransferHandler handler = CorpusNavigationPanel.this.potentialConceptsTable.getTransferHandler();
				handler.exportAsDrag(CorpusNavigationPanel.this.potentialConceptsTable, e, TransferHandler.MOVE);
			}
		});
		JScrollPane jspPotential = new JScrollPane(this.potentialConceptsTable);
		jspPotential.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("potentialconcepts")));
		this.add(jspPotential, "3, 5, 1, 7");
		
		this.validationButton = new JButton(ApplicationManager.getApplicationManager().getTraduction("validate"));
		this.validationButton.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (doc!=null)
				{
					
					if (doc.getState() == LinkableElement.VALIDATED)
					{
						ApplicationManager.ontology.getDocValider().remove(doc);
					}
					else
					{
						ApplicationManager.ontology.getDocValider().add(doc);
						//DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().setValueAt(DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getDefaultRenderer(LinkableElement.class).getTableCellRendererComponent(DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable(),currentElement,true,true,DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getSelectedRow(),DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getSelectedColumn()),DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTable().getSelectedRow(),0);
					}
					CorpusNavigationPanel.this.validationButton.setText((doc.getState() == LinkableElement.VALIDATED)?"Valider":"Invalider");
					doc.setState((doc.getState() == LinkableElement.VALIDATED)?LinkableElement.DEFAULT:LinkableElement.VALIDATED);
					if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree()!=null)
						DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.BOTTOM_PANEL).getTree().updateUI();
					if (DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree()!=null)
						DisplayManager.mainFrame.getPanel(DisplayManager.mainFrame.TOP_PANEL).getTree().updateUI();
				
				}
			}
		});
		this.add(this.validationButton, "5, 7, 1, 1");
		
		String[] TitreI={ApplicationManager.getApplicationManager().getTraduction("image")};
		ImagesToDocumentPartTableModel tableIModel = new ImagesToDocumentPartTableModel();
		tableIModel.setColumnNames(TitreI);
		this.imagesTable = new JTable(tableIModel);
		JScrollPane jspI = new JScrollPane(this.imagesTable);
		jspI.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("imageToDoc")));
		this.add(jspI, "1, 5, 1, 7");
		this.imagesTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				LinkableElement element = ((LinkableElement) ((JTable) e.getSource()).getModel().getValueAt(((JTable) e.getSource()).getSelectedRow(), 0));
				imagesTable.setToolTipText(element.getName());
			}
		});
		
		// mise en place des renderer
		this.rendererTableConcept(this.conceptsIndexingTable);
		this.rendererTableConcept(this.potentialConceptsTable);
		this.rendererTableLemme(this.termeAssocie);

	}

	/**
	 * Clears all the tables and textfields.
	 */
	public void refresh()
	{
		txtArea_docText.setText(null);
		((IndexingConceptTableModel)conceptsIndexingTable.getModel()).setDonnees(null);
		((PotentialConceptTableModel)potentialConceptsTable.getModel()).setDonnees(null);
		((LemmasToDocumentPartTableModel)termeAssocie.getModel()).setDonnees(null);
		this.updateUI();
	}
		
	// focntion permettant de remplir la table des concept indexant par raport a un document
	public void remplirTableConceptIndexant(LinkableElement doc) {
		// TODO Auto-generated method stub
		this.doc=doc;
		((IndexingConceptTableModel)conceptsIndexingTable.getModel()).setDonnees(null);
		this.validationButton.setText((doc.getState() == LinkableElement.VALIDATED)?ApplicationManager.getApplicationManager().getTraduction("unvalidate"):ApplicationManager.getApplicationManager().getTraduction("validate"));
		this.setBorder(BorderFactory.createTitledBorder(ApplicationManager.getApplicationManager().getTraduction("corpusnavpanel")+" "+doc.getName()));
		this.txtArea_docText.setText(((DocumentPart)doc).getValue());
		if (((DocumentPart)doc).getCommentaire()!=null)
			this.txtArea_docComm.setText(((DocumentPart)doc).getCommentaire().getValue());
		this.conceptsIndexingTable.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		ArrayList<Integer> links_categories = new ArrayList<Integer>(1);
		links_categories.add(Concept.KEY);
		for (Integer category : links_categories) {
			if (doc.getLinks(category.intValue()) != null) {
				Set<Relation> keys = doc.getLinks(category.intValue()).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement temp : doc.getLinks(category.intValue(), key))
					{
						Object[] triple = {key, temp};
						elements.add(triple);
					}
				}
			}
		}
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			((IndexingConceptTableModel)conceptsIndexingTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((IndexingConceptTableModel)conceptsIndexingTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
			
	}
	
//	 focntion permettant de remplir la table des images indexant par raport a un document
	public void remplirTableimages(LinkableElement doc) {
		// TODO Auto-generated method stub
		((ImagesToDocumentPartTableModel)imagesTable.getModel()).setDonnees(null);
		
		this.imagesTable.removeAll();
		
		if (((DocumentPart)doc).getImages().size()!=0)
		{
			Object [][] donnees=new Object[((DocumentPart)doc).getImages().size()][1];
			for (int i=0;i<((DocumentPart)doc).getImages().size();i++)
			{
				donnees[i][0]=(((DocumentPart)doc).getImages().get(i));
				//donnees[i][1]=(((DocumentPart)doc).getImages().get(i));
			}
			((ImagesToDocumentPartTableModel)imagesTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][1];
			((ImagesToDocumentPartTableModel)imagesTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
			
	}

//	 focntion permettant de remplir la table des concept potentiels par raport a un document
	public void remplirTableConceptPotentiel(LinkableElement doc) {
		// TODO Auto-generated method stub
		this.potentialConceptsTable.removeAll();
		((PotentialConceptTableModel)potentialConceptsTable.getModel()).setDonnees(null);
		//on selectionne les lemme associé au corpus courant : doc
		ArrayList<LinkableElement> lemmes = new ArrayList<LinkableElement>();
		ArrayList<Integer> links_categories_lemme = new ArrayList<Integer>(1);
		links_categories_lemme.add(Lemma.KEY);
		for (Integer category : links_categories_lemme) {
			if (doc.getLinks(category.intValue()) != null) {
				Set<Relation> keys = doc.getLinks(category.intValue()).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement temp : doc.getLinks(category.intValue(), key))
					{
						lemmes.add(temp);
						
					}
				}
			}
		}
		
		
		// on cherche les concept indexant
		ArrayList<Concept> conceptIndexant = new ArrayList<Concept>();
		ArrayList<Integer> links_categoriesCI = new ArrayList<Integer>(1);
		links_categoriesCI.add(Concept.KEY);
		for (Integer category : links_categoriesCI) {
			if (doc.getLinks(category.intValue()) != null) {
				Set<Relation> keys = doc.getLinks(category.intValue()).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement temp : doc.getLinks(category.intValue(), key))
					{
						conceptIndexant.add((Concept)temp);
					}
				}
			}
		}
		
		
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		ArrayList<Integer> links_categories = new ArrayList<Integer>(1);
		ArrayList<Concept> last=new ArrayList<Concept>();
		// on regarde les fils du document courant 
		HashMap<Relation,HashMap<LinkableElement,Link>> docs=doc.getLinks(DocumentPart.KEY);
		ArrayList<LinkableElement> docFils=new ArrayList<LinkableElement>();
		for (Relation rel:docs.keySet())
		{
			if (rel.toString().equals("englobe"))
			{
				HashMap<LinkableElement,Link>hm=docs.get(rel);
				for (LinkableElement le:hm.keySet())
				{
					docFils.add(le);
				}
			}
		}
		
		// pour chaque fils on cherche les concept indexant
		for (LinkableElement le:docFils)
		{
			HashMap<Relation,HashMap<LinkableElement,Link>> concep=le.getLinks(Concept.KEY);
			for (Relation rel:concep.keySet())
			{
				HashMap<LinkableElement,Link>hm=concep.get(rel);
				if (!hm.isEmpty())
				{
					for (LinkableElement con:hm.keySet())
					{
						Object[] triple = {rel, con};
						Boolean deja=false;
						for (int i=0;i<elements.size();i++)
						{
							// eviter les doublons
							if (elements.get(i)[1].equals(triple[1]))
							{
								deja=true;
								break;
							}
							if (!deja)
							{
								for (int j=0;j<conceptIndexant.size();j++)
								{
									if (elements.get(i)[1].equals(conceptIndexant.get(j).toString()))
									{
										deja=true;
										break;
									}
								}
							}
						}
						if (!deja)
						{
							elements.add(triple);
						}
					}
				}
			}
		}	
		
		// on compare les concepts lié au lemme - concept indexant = concept potentiel
		for (int i=0;i<lemmes.size();i++)
		{
			links_categories.add(Concept.KEY);
			for (Integer category : links_categories) {
				if (lemmes.get(i).getLinks(category.intValue()) != null) {
					Set<Relation> keys = lemmes.get(i).getLinks(category.intValue()).keySet();
					for (Relation key : keys)
					{
						for (LinkableElement temp : lemmes.get(i).getLinks(category.intValue(), key))
						{
							boolean trouver=false;	
							// recherche de doublons
							for (int j=0;j<last.size();j++)
							{
								if (last.get(j).equals(temp))
								{
									trouver=true;
									break;
								}
							}
							// recherche de concept indexant
							for (int j=0;j<conceptIndexant.size();j++)
							{
								if (conceptIndexant.get(j).equals(temp))
								{	
									trouver=true;
									break;
								}
							}
							// si on ne le trouve pas on le rajoute
							if (!trouver)
							{
								boolean present=false;
								for (int n=0;n<elements.size();n++)
								{
									if (elements.get(n)[1].toString().equals(temp.toString()))
									{
										present=true;
										break;
									}
								}
								if (!present)
								{
									last.add((Concept)temp);
									Object[] triple = {"annote", temp};
									elements.add(triple);
								}
							}
						}
					}
				}
			}
		}
		for (int i=0;i<conceptIndexant.size();i++)
		{
			for (int j=0;j<elements.size();j++)
			{
				if (conceptIndexant.get(i).equals(elements.get(j)[1]))
				{
					elements.remove(j);
					break;
				}
			}
		}
		
			
					
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][2];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[0]);
				donnees[i][1]=(elements.get(i)[1]);
			}
			((PotentialConceptTableModel)potentialConceptsTable.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][2];
			((PotentialConceptTableModel)potentialConceptsTable.getModel()).setDonnees(donnees);
		}
		this.updateUI();
	}

//	 focntion permettant de remplir la table des lemme lié par raport a un document
	public void remplirTableLemmeLier(LinkableElement doc) {
		// TODO Auto-generated method stub
		this.termeAssocie.removeAll();
		ArrayList<Object[]> elements = new ArrayList<Object[]>();
		ArrayList<Integer> links_categories = new ArrayList<Integer>(1);
		links_categories.add(Lemma.KEY);
		for (Integer category : links_categories) {
			if (doc.getLinks(category.intValue()) != null) {
				Set<Relation> keys = doc.getLinks(category.intValue()).keySet();
				for (Relation key : keys)
				{
					for (LinkableElement temp : doc.getLinks(category.intValue(), key))
					{
						Object[] triple = {key, temp};
						elements.add(triple);
					}
				}
			}
		}
		if (elements.size()!=0)
		{
			Object [][] donnees=new Object[elements.size()][1];
			for (int i=0;i<elements.size();i++)
			{
				donnees[i][0]=(elements.get(i)[1]);
			}
			((LemmasToDocumentPartTableModel)termeAssocie.getModel()).setDonnees(donnees);
		}
		else
		{
			Object [][] donnees=new Object[0][1];
			((LemmasToDocumentPartTableModel)termeAssocie.getModel()).setDonnees(donnees);
		}
		this.updateUI();
	}

	public LinkableElement getDoc() {
		return doc;
	}

	public void setDoc(LinkableElement doc) {
		this.doc = doc;
	}
	
	// affichage des icone correspondant au concept
	private void rendererTableConcept(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_CONCEPT_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(1).setCellRenderer(custom);
	}
	
	// affichage des icone correspondant au lemme
	private void rendererTableLemme(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		//custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_LEMMA_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
	}

	@Override
	public void reflectNavigation(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doNavigationKeyPressed(KeyEvent evt) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeState(boolean state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementRemoved(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshNavigation(LinkableElement element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void relationChanged(LinkableElement source, LinkableElement target) {
		// TODO Auto-generated method stub
		
	}
	
}