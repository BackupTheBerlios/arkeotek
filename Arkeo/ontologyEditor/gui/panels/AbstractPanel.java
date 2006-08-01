/**
 * Created on 9 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import info.clearthought.layout.TableLayout;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.model.tableModel.LemmaTableModel;
import ontologyEditor.gui.model.treeModel.AbstractTreeModel;
import ontologyEditor.gui.model.treeModel.ConceptualTreeModel;
import ontologyEditor.gui.model.treeModel.CorpusTreeModel;
import ontologyEditor.gui.renderer.tableRenderer.LemmaTableRenderer;
import ontologyEditor.gui.renderer.treeRenderer.DocumentPartTreeRenderer;
import ontologyEditor.gui.transfers.LinkableElementDragTransferHandler;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public abstract class AbstractPanel extends JPanel
{
    // Variables declaration - do not modify
    protected javax.swing.JPanel treeViewPanel;
	protected AbstractNavigationPanel navigationPanel;
	protected javax.swing.JSplitPane jSplitPane1;
	protected JTree tree;
	protected JTable table;
    // End of variables declaration
	
	/**
	 * @param treeModel 
	 * @param navigationPanel 
	 */
	public AbstractPanel(AbstractTreeModel treeModel,AbstractNavigationPanel navigationPanel)
	{
		super();
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setSize(screenSize.width , screenSize.height);
		initComponents(treeModel,navigationPanel);
		this.treeViewPanel.setMinimumSize(new Dimension(screenSize.width /8, 0));
		this.navigationPanel.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
	}
	
	public AbstractPanel(AbstractTableModel tableModel, AbstractNavigationPanel navigationPanel)
	{
		super();
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setSize(screenSize.width , screenSize.height);
		initComponentsTable(navigationPanel);
		this.treeViewPanel.setMinimumSize(new Dimension(screenSize.width /8, 0));
		this.navigationPanel.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
	}

	private void initComponents(AbstractTreeModel treeModel,AbstractNavigationPanel np) {
//		 Create a TableLayout for the panel
		double size[][] = { 
				{TableLayout.FILL}, // Columns
				{TableLayout.FILL} 
			}; // Rows
		this.setLayout(new TableLayout(size));

	
		this.jSplitPane1 = new JSplitPane();
		this.treeViewPanel = new javax.swing.JPanel();
		JScrollPane jScrollPane1 = new JScrollPane();
		
		if (ApplicationManager.ontology!=null)
		{
			if (treeModel instanceof CorpusTreeModel)
			{
				((CorpusTreeModel)treeModel).setRacine(new DefaultMutableTreeNode("Corpus"));
				((CorpusTreeModel)treeModel).remplirArbreDocumentSeq();
			}
			else if (treeModel instanceof ConceptualTreeModel)
			{
				((ConceptualTreeModel)treeModel).setRacine(new DefaultMutableTreeNode(ApplicationManager.ontology.getName()));
				((ConceptualTreeModel)treeModel).remplirArbreConcept();
			}
		}
		else
		{
			if (treeModel instanceof CorpusTreeModel)
			{
				((CorpusTreeModel)treeModel).setRacine(new DefaultMutableTreeNode("Corpus"));
			}
			else if (treeModel instanceof ConceptualTreeModel)
			{
				((ConceptualTreeModel)treeModel).setRacine(new DefaultMutableTreeNode("Ontologie"));
			}
		}
		
		this.tree = new JTree(treeModel);
		
		if (this.tree.getModel() instanceof ConceptualTreeModel)
		{
			this.tree.setTransferHandler(new LinkableElementDragTransferHandler());
		}
		this.tree.setDragEnabled(false);
		
		this.tree.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				AbstractPanel.this.performMouseClicked(e);
			}						
		});
		
		this.tree.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if (AbstractPanel.this.tree.getModel() instanceof ConceptualTreeModel)
				{
					if (DisplayManager.editionState)
					{
						TransferHandler handler = AbstractPanel.this.tree.getTransferHandler();
						handler.exportAsDrag(AbstractPanel.this.tree, e, TransferHandler.COPY);
				
					}
				}
			}
		});
		if (this.tree.getModel() instanceof CorpusTreeModel)
			this.tree.setCellRenderer(new DocumentPartTreeRenderer());
		 
		jScrollPane1.setViewportView(this.tree);		
	
		this.treeViewPanel.setLayout(new BoxLayout(this.treeViewPanel, BoxLayout.X_AXIS));
		this.treeViewPanel.add(jScrollPane1);
		
		this.navigationPanel = np;
		
		this.jSplitPane1.setLeftComponent(this.treeViewPanel);
		this.jSplitPane1.setRightComponent(this.navigationPanel);
		this.jSplitPane1.setOneTouchExpandable(true);
		
		this.add(this.jSplitPane1, "0, 0, 0, 0");
	}
	
	private void initComponentsTable(AbstractNavigationPanel np) {
//		 Create a TableLayout for the panel
		double size[][] = { 
				{TableLayout.FILL}, // Columns
				{TableLayout.FILL} 
			}; // Rows
		this.setLayout(new TableLayout(size));

		this.jSplitPane1 = new JSplitPane();
		this.treeViewPanel = new javax.swing.JPanel();
		String[] titre={"Termes"};
		LemmaTableModel tableModel = new LemmaTableModel();
		tableModel.setColumnNames(titre);
		this.table=new JTable();
		this.table.setDefaultRenderer(Object.class,new LemmaTableRenderer());
		this.setTable(this.table);
		this.table.setModel(tableModel);
		this.table.setTransferHandler(new LinkableElementDragTransferHandler());
		this.table.setDragEnabled(false);
		this.table.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
						AbstractPanel.this.performMouseClicked(e);
				}						
			});
		this.table.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if (DisplayManager.editionState)
				{
					TransferHandler handler = AbstractPanel.this.table.getTransferHandler();
					handler.exportAsDrag(AbstractPanel.this.table, e, TransferHandler.COPY);
				}
				
			}
		});
		JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(this.table);
		jScrollPane1.setBorder(BorderFactory.createTitledBorder("Termes du corpus"));
		this.treeViewPanel.setLayout(new BoxLayout(this.treeViewPanel, BoxLayout.X_AXIS));
		this.treeViewPanel.add(jScrollPane1);
		this.navigationPanel = np;
		
		this.jSplitPane1.setLeftComponent(this.treeViewPanel);
		this.jSplitPane1.setRightComponent(this.navigationPanel);
		this.jSplitPane1.setOneTouchExpandable(true);
		
		//this.rendererTableLemme(this.table);
		
		this.add(this.jSplitPane1, "0, 0, 0, 0");
		
	}

	abstract public void refresh();
	
	public abstract void reflectNavigation(LinkableElement element);
	
	public void changeState(boolean state)
	{
		
	}
	
	
	public abstract void elementRemoved(LinkableElement element, int index);
		
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		this.navigationPanel.relationChanged(source, target);
	}
		
	protected abstract void performMouseClicked(MouseEvent e);
	
	public void refreshNavigation(LinkableElement element)
	{
		this.navigationPanel.refreshNavigation(element);
	}
	
	public TreePath[] getSelectedValues()
	{
		return this.tree.getSelectionPaths();
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}

	public AbstractNavigationPanel getNavigationPanel() {
		return navigationPanel;
	}

	public void setNavigationPanel(AbstractNavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}
	
	private void rendererTableLemme(JTable table) {     
		DefaultTableCellRenderer custom = new DefaultTableCellRenderer();
		custom.setHorizontalAlignment(JLabel.CENTER);
		try {
			//custom.setIcon(ImagesManager.getInstance().getIcon(Constants.DEFAULT_LEMMA_ICON));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.getColumnModel().getColumn(0).setCellRenderer(custom);
	}
}
