/**
 * Created on 23 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.filechoosers;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class OntologyFileFilter extends FileFilter
{
	public static String FILTER_DESCRIPTOR = "Filtre de fichiers de propriétés d'ontologies";
	
	/**
	 * 
	 */
	public OntologyFileFilter()
	{
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f)
	{
		return f.isFile() && f.getName().endsWith(".properties");
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return FILTER_DESCRIPTOR;
	}

}
