/**
 * Created on 26 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.panels.edition;

import javax.swing.Icon;
import javax.swing.JTextField;

import ontologyEditor.ImagesManager;
import arkeotek.ontology.LinkableElement;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class LinkableElementTextField extends JTextField
{
	private LinkableElement element;
	private Icon icon;

	/**
	 * @param element
	 */
	public LinkableElementTextField(LinkableElement element)
	{
		super();
		this.element = element;
		this.icon = ImagesManager.getInstance().getDefaultIcon(element);
	}

	/**
	 * @return Returns the element.
	 */
	public LinkableElement getElement()
	{
		return this.element;
	}

	/**
	 * @param element The element to set.
	 */
	public void setElement(LinkableElement element)
	{
		this.element = element;
		if (element != null)
			this.setText(element.getName());
		else
			this.setText("");
		this.icon = ImagesManager.getInstance().getDefaultIcon(element);
	}

	/**
	 * @return Returns the icon.
	 */
	public Icon getIcon()
	{
		return this.icon;
	}
	
	
	
}
