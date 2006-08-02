/**
 * Created on 23 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.filechoosers;

import javax.swing.JFileChooser;

import ontologyEditor.ApplicationManager;
import ontologyEditor.Constants;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class OntologyFileChooser extends JFileChooser
{

	/**
	 * 
	 */
	public OntologyFileChooser()
	{
		super(Constants.DEFAULT_ONTOLOGIES_PATH);
		this.setDialogTitle(ApplicationManager.getApplicationManager().getTraduction("openingontology"));
		this.setFileFilter(new OntologyFileFilter());
		this.setMultiSelectionEnabled(false);
	}
	
	/**
	 * @return The name of the ontology implied by the .properties file selected. 
	 */
	public String extractOntologyName() {
		String file_name = super.getSelectedFile().getName();
		return file_name.substring(0, file_name.lastIndexOf(".properties"));
	}
}
