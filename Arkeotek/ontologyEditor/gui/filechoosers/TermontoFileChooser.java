/**
 * Created on 2 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.filechoosers;

import javax.swing.JFileChooser;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class TermontoFileChooser extends JFileChooser
{
	/**
	 * 
	 */
	public TermontoFileChooser()
	{
		super();
		this.setDialogTitle("Import des sorties Termonto au format csv");
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.setMultiSelectionEnabled(false);
	}
}
