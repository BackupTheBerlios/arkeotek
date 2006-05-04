/**
 * Created on 9 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels;

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;
import ontologyEditor.gui.transfers.LinkableElementDragTransferHandler;
import ontologyEditor.gui.treeviews.AbstractTreeModel;
import ontologyEditor.gui.treeviews.LinkableElementTree;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.IIndexable;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
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
	protected LinkableElementTree tree;
    // End of variables declaration
	
	/**
	 * @param treeModel 
	 * @param navigationPanel 
	 */
	public AbstractPanel(AbstractTreeModel treeModel, AbstractNavigationPanel navigationPanel)
	{
		super();
		Rectangle screenSize = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setSize(screenSize.width , screenSize.height);
		initComponents(treeModel, navigationPanel);
		this.treeViewPanel.setMinimumSize(new Dimension(screenSize.width /8, 0));
		this.navigationPanel.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
	}

	private void initComponents(AbstractTreeModel tm, AbstractNavigationPanel np) {
//		 Create a TableLayout for the panel
		double size[][] = { 
				{TableLayout.FILL}, // Columns
				{TableLayout.FILL} 
			}; // Rows
		this.setLayout(new TableLayout(size));

	
		this.jSplitPane1 = new JSplitPane();
		this.treeViewPanel = new javax.swing.JPanel();
		JScrollPane jScrollPane1 = new JScrollPane();
	
		AbstractTreeModel treeModel = tm;
		//this.tree = new LinkableElementTree(null);
		this.tree = new LinkableElementTree(treeModel);
		this.tree.setTransferHandler(new LinkableElementDragTransferHandler());
		this.tree.setDragEnabled(DisplayManager.editionState);
		this.tree.addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					if (((JTree)e.getSource()).getSelectionPath() != null && ((JTree)e.getSource()).getSelectionPath().getLastPathComponent() instanceof LinkableElement)
					{
						AbstractPanel.this.performMouseClicked(e);
					}
				}						
			});
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

	public void refresh() {
		ArrayList<TreePath> paths = this.tree.getExpandedPaths();
		((AbstractTreeModel) this.tree.getModel()).fireTreeStructureChanged();
		this.tree.setExpandedPaths(paths);
		this.navigationPanel.refresh();
	}
	
	public abstract void reflectNavigation(LinkableElement element);
	
	public void changeState(boolean state)
	{
		this.tree.setDragEnabled(state);
		this.navigationPanel.changeState(state);
	}
	
	public void elementAdded(LinkableElement element)
	{
		((AbstractTreeModel) this.tree.getModel()).fireTreeNodesAdded(element);
	}
	
	public abstract void elementRemoved(LinkableElement element, int index);
	
	public void reloadTrees()
	{
		ArrayList<TreePath> paths = this.tree.getExpandedPaths();
		((AbstractTreeModel) this.tree.getModel()).reloadTree();
		this.tree.setExpandedPaths(paths);
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
	}
	
	public void refreshNavigation(LinkableElement element)
	{
		this.navigationPanel.refreshNavigation(element);
	}
	
	public TreePath[] getSelectedValues()
	{
		return this.tree.getSelectionPaths();
	}
	
	private class TreeCellRenderer extends DefaultTreeCellRenderer
    {
        /**
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
         *      java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        public Component getTreeCellRendererComponent(JTree jTree, Object value,
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
        }
    }
}
