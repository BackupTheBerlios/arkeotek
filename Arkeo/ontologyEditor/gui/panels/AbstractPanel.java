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
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.gui.tables.LemmaTableModel;
import ontologyEditor.gui.tables.LinkableElementTable;
import ontologyEditor.gui.transfers.LinkableElementDragTransferHandler;
import ontologyEditor.gui.treeviews.AbstractTM;
import ontologyEditor.gui.treeviews.AbstractTreeModel;
import ontologyEditor.gui.treeviews.ConceptualTM;
import ontologyEditor.gui.treeviews.CorpusTM;
import ontologyEditor.gui.treeviews.LinkableElementTree;
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
	protected LinkableElementTable table;
    // End of variables declaration
	
	/**
	 * @param treeModel 
	 * @param navigationPanel 
	 */
	public AbstractPanel(AbstractTM treeModel,AbstractNavigationPanel navigationPanel)
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

	private void initComponents(AbstractTM treeModel,AbstractNavigationPanel np) {
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
			if (treeModel instanceof CorpusTM)
			{
				((CorpusTM)treeModel).setRacine(new DefaultMutableTreeNode("Corpus"));
				((CorpusTM)treeModel).remplirArbreDocumentSeq();
			}
			else if (treeModel instanceof ConceptualTM)
			{
				((ConceptualTM)treeModel).setRacine(new DefaultMutableTreeNode(ApplicationManager.ontology.getName()));
				((ConceptualTM)treeModel).remplirArbreConcept();
			}
		}
		else
		{
			if (treeModel instanceof CorpusTM)
			{
				((CorpusTM)treeModel).setRacine(new DefaultMutableTreeNode("Corpus"));
			}
			else if (treeModel instanceof ConceptualTM)
			{
				((ConceptualTM)treeModel).setRacine(new DefaultMutableTreeNode("Ontologie"));
			}
		}
		
		this.tree = new JTree(treeModel);
		
		if (this.tree.getModel() instanceof ConceptualTM)
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
				if (AbstractPanel.this.tree.getModel() instanceof ConceptualTM)
				{
					if (DisplayManager.editionState)
					{
						TransferHandler handler = AbstractPanel.this.tree.getTransferHandler();
						handler.exportAsDrag(AbstractPanel.this.tree, e, TransferHandler.COPY);
				
					}
				}
			}
		});
		
		/*if (this.tree.getModel() instanceof ConceptualTM)
		{
			this.tree.setDropTarget(new DropTarget(this.tree, new DropTargetAdapter()
			{
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
							AbstractPanel.this.tree.getTransferHandler().importData(AbstractPanel.this.tree, transferable);
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
		}*/
		this.tree.setCellRenderer(new TreeCellRenderer());
		 
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
		this.table=new LinkableElementTable();
		//this.setTable(this.table);
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
		
		this.rendererTableLemme(this.table);
		
		this.add(this.jSplitPane1, "0, 0, 0, 0");
		
	}

	abstract public void refresh();
	
	public abstract void reflectNavigation(LinkableElement element);
	
	public void changeState(boolean state)
	{
		//this.tree.setDragEnabled(state);
		//this.navigationPanel.changeState(state);
	}
	
	public void elementAdded(LinkableElement element)
	{
		((AbstractTreeModel) this.tree.getModel()).fireTreeNodesAdded(element);
	}
	
	public abstract void elementRemoved(LinkableElement element, int index);
	
	public void reloadTrees()
	{
		//ArrayList<TreePath> paths = this.tree.getExpandedPaths();
		//((AbstractTreeModel) this.tree.getModel()).reloadTree();
		//this.tree.setExpandedPaths(paths);
	}
	
	public void reloadPanel()
	{
		this.navigationPanel.reload();
	}
		
	public void relationChanged(LinkableElement source, LinkableElement target)
	{
		this.navigationPanel.relationChanged(source, target);
	}
		
	protected abstract void performMouseClicked(MouseEvent e);
	
	public int getChildIndexInTree(LinkableElement element)
	{
		return ((AbstractTreeModel)this.tree.getModel()).getIndexOfChild(((AbstractTreeModel)this.tree.getModel()).getParentNode(element), element);
		//return 0;
	}
	
	public void refreshNavigation(LinkableElement element)
	{
		this.navigationPanel.refreshNavigation(element);
	}
	
	public TreePath[] getSelectedValues()
	{
		return this.tree.getSelectionPaths();
		//return null;
	}
	
	private class TreeCellRenderer extends DefaultTreeCellRenderer
    {
        /**
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
         *      java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
      /*  public Component getTreeCellRendererComponent(JTree jTree, Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean focus)
        {
           Component c = null;

            c = super.getTreeCellRendererComponent(jTree, value, sel, expanded,
                    leaf, row, focus);
			if (!(value instanceof String))
			{
	            try {
					this.setIcon(ImagesManager.getInstance().getDefaultIcon(
					       (LinkableElement)value));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (value instanceof DocumentPart)
				{
					String text = this.getText() + " (";
					if (((LinkableElement)value).getLinks(Concept.KEY) != null)
					{
						for (HashMap<LinkableElement, Link> links : ((LinkableElement)value).getLinks(Concept.KEY).values())
						{
							for (LinkableElement element : links.keySet())
							{
								text += element + ", ";
							}
						}
						text = text.substring(0, text.length() - 2);
						if (text.length() > ((LinkableElement)value).toString().length())
							text += ")";
						this.setText(text);
					}
				}
				if ((value instanceof Lemma || value instanceof DocumentPart) && ((LinkableElement)value).getState() == LinkableElement.DEFAULT)
				{
					Font f = this.getFont();
	                Font f2 = f.deriveFont(Font.BOLD);
	                this.setFont(f2);
					this.setForeground(Color.GREEN.darker().darker());
				}
				else if (((IIndexable)value).isNew())
				{
	                Font f = this.getFont();
	                Font f2 = f.deriveFont(Font.BOLD);
	                this.setFont(f2);
					this.setForeground(Color.RED);
	            }
				else
					this.setForeground(Color.BLACK);
			}
//			we change the font if the node is selected
            if (sel)
            {
                Font f = jTree.getFont();
                Font f2 = f.deriveFont(Font.ITALIC);
                this.setFont(f2);
            }
            else
            {
                Font f = jTree.getFont();
                Font f2 = f.deriveFont(Font.PLAIN);
                this.setFont(f2);
            }
            return c;
        }*/
    }

	public LinkableElementTable getTable() {
		return table;
	}

	public void setTable(LinkableElementTable table) {
		this.table = table;
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(LinkableElementTree tree) {
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
