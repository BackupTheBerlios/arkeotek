/**
 * Created on 30 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import ontologyEditor.gui.panels.AbstractPanel;
import ontologyEditor.gui.panels.CorpusPanel;
import ontologyEditor.gui.panels.EditionPanel;
import ontologyEditor.gui.panels.LinguisticPanel;
import ontologyEditor.gui.panels.OntologyPanel;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class MainFrame extends JFrame
{
    // Variables declaration - do not modify
    private MenuBar menuBar;
    private AbstractPanel topPanel;
    private AbstractPanel bottomPanel;
    private EditionPanel editionPanel;
	private JPanel loadingPanel;
    private JSplitPane jSplitPane1;
	private JSplitPane jSplitPane2;
    // End of variables declaration

	/**
	 * Constant used to indicate the top pannel of the <code>MainFrame</code>. 
	 */
	public static final int TOP_PANEL = 0;
	/**
	 * Constant used to indicate the bottom pannel of the <code>MainFrame</code>. 
	 */
	public static final int BOTTOM_PANEL = 1;
	
	/** 
	 * Creates new form MainFrame 
	 */
    public MainFrame() {
		Rectangle screenSize = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getMaximumWindowBounds();
		this.setSize(screenSize.width , screenSize.height);
		this.setMaximizedBounds(new Rectangle(screenSize.width, screenSize.height));
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		initComponents();
		this.editionPanel.setMinimumSize(new Dimension(screenSize.width /6, screenSize.height));
		this.setVisible(true);
    }
    
    /** 
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
		this.loadingPanel = new JPanel();
		this.loadingPanel.add(new JLabel("chargement..."));
		
		this.menuBar = new MenuBar();
		this.jSplitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		
		this.topPanel = new OntologyPanel();
		this.bottomPanel = new LinguisticPanel();
		
		this.jSplitPane2.setTopComponent(this.topPanel);
		this.jSplitPane2.setBottomComponent(this.bottomPanel);
		this.jSplitPane2.setOneTouchExpandable(true);
		this.jSplitPane2.setResizeWeight(0.50);
		
		this.editionPanel = new EditionPanel();
		
		this.jSplitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.jSplitPane2, this.editionPanel);
		this.jSplitPane1.setOneTouchExpandable(true);
		this.jSplitPane1.setResizeWeight(0.9);
        getContentPane().add(this.jSplitPane1, java.awt.BorderLayout.CENTER);
        setJMenuBar(this.menuBar);
        
    }
	
	/**
	 * Refreshes the display of the <code>MainFrame</code> and its <code>Components</code>. 
	 */
	public void refresh() {
		this.jSplitPane1.setLeftComponent(this.loadingPanel);
		this.topPanel.refresh();
		this.bottomPanel.refresh();
		this.editionPanel.refresh();
		this.jSplitPane1.setLeftComponent(this.jSplitPane2);
	}
	
	/**
	 * Reflects the user's actions on the elements to the window. 
	 * @param element The <code>LinkableElement</code> generating a graphical change of the application. 
	 */
	public void reflectNavigation(LinkableElement element)
	{
		this.topPanel.reflectNavigation(element);
		this.bottomPanel.reflectNavigation(element);
		this.editionPanel.repercutNavigation(element);
	}
    
	/**
	 * Sets the state of the window to the "Edition" or "Navigation" mode.  
	 * @param state <code>true</code> indicates the "Edition" mode, 
	 * <code>false</code> the "Navigation" mode. 
	 */
	public void changeState(boolean state)
	{
		this.topPanel.changeState(state);
		this.bottomPanel.changeState(state);
		this.editionPanel.changeState(state);
	}
		
	/**
	 * Notify all panels that <code>element</code> has been removed
	 * @param element the element removed
	 * @param indexes indexes of element in trees
	 */
	public void elementRemoved(LinkableElement element, int[] indexes)
	{
		if (element instanceof Concept)
			this.topPanel.elementRemoved(element, indexes[0]);
		this.editionPanel.ontologyElementRemoved(element);
		if (element instanceof Lemma)
			this.bottomPanel.elementRemoved(element, indexes[1]);
	}
	
	/**
	 * Reload trees of panels
	 */
	public void reloadTrees()
	{
		this.topPanel.reloadTrees();
		this.bottomPanel.reloadTrees();
	}
		
	/**
	 * @param panel
	 * @param category_key
	 */
	public void changeView(int panel, int category_key) {
		 switch (panel) {
		 case TOP_PANEL : 
			 if (category_key == Concept.KEY) {
				 if (!(this.topPanel instanceof OntologyPanel))
					 this.topPanel = new OntologyPanel();
			 } else if (category_key == Lemma.KEY) {
				 if (!(this.topPanel instanceof LinguisticPanel))
					 this.topPanel = new LinguisticPanel();
			 } else if (category_key == DocumentPart.KEY) {
				 if (!(this.topPanel instanceof CorpusPanel))
					 this.topPanel = new CorpusPanel();
			 }
			 ((JSplitPane) this.jSplitPane1.getLeftComponent()).setTopComponent(this.topPanel);
			 break;
		 case BOTTOM_PANEL : 
			 if (category_key == Concept.KEY) {
				 if (!(this.bottomPanel instanceof OntologyPanel))
					 this.bottomPanel = new OntologyPanel();
			 } else if (category_key == Lemma.KEY) {
				 if (!(this.bottomPanel instanceof LinguisticPanel))
					 this.bottomPanel = new LinguisticPanel();
			 } else if (category_key == DocumentPart.KEY) {
				 if (!(this.bottomPanel instanceof CorpusPanel))
					 this.bottomPanel = new CorpusPanel();
			 }
			 ((JSplitPane) this.jSplitPane1.getLeftComponent()).setBottomComponent(this.bottomPanel);
			 break;
		 }
	}
	
	/**
	 * isAtBottomPanel(Concept.KEY ou Lemma.KEY ou DocumentPart.KEY)
	 * @ Hubert Nouhen
	 */
	
	public boolean isAtBottomPanel(int key)
	{
		boolean res=false;
				
		if(key == Lemma.KEY)
		{
			if (this.bottomPanel instanceof LinguisticPanel) res=true;
		}
		
		if(key == Concept.KEY)
		{
			if (this.bottomPanel instanceof OntologyPanel) res=true;
		}
		
		if(key == DocumentPart.KEY)
		{
			if (this.bottomPanel instanceof CorpusPanel) res=true;
		}
		
		return res;
	}

	/**
	 * isAtTopPanel(Concept.KEY ou Lemma.KEY ou DocumentPart.KEY)
	 * @ Hubert Nouhen
	 */
	public boolean isAtTopPanel(int key)
	{
		boolean res=false;
				
		if(key == Lemma.KEY)
		{
			if (this.topPanel instanceof LinguisticPanel) res=true;
		}
		
		if(key == Concept.KEY)
		{
			if (this.topPanel instanceof OntologyPanel) res=true;
		}
		
		if(key == DocumentPart.KEY)
		{
			if (this.topPanel instanceof CorpusPanel) res=true;
		}
		
		return res;
	}
	
	/**
	 * isActive(Concept.KEY ou Lemma.KEY ou DocumentPart.KEY)
	 * @ Hubert Nouhen
	 */
	public boolean isActive(int key)
	{
		return(isAtBottomPanel(key)||isAtTopPanel(key));
	}
	
	/**
	 * Return indexes of <code>element</code> in trees
	 * @param element
	 * @return indexes of element
	 */
	public int[] getChildIndexesInTrees(LinkableElement element)
	{
		int[] indices = new int[2];
		indices[0] = this.topPanel.getChildIndexInTree(element);
		indices[1] = this.bottomPanel.getChildIndexInTree(element);
		return indices;
	}

	/**
	 * @param i index of panel<br>value can be topPanel or bottomPanel
	 * @return Returns the panel.
	 */
	public AbstractPanel getPanel(int i)
	{
		if (i == TOP_PANEL)
			return this.topPanel;
		return this.bottomPanel;
	}

	/**
	 * @return Returns the editionPanel.
	 */
	public EditionPanel getEditionPanel()
	{
		return this.editionPanel;
	}
	
	
}