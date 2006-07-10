/**
 * Created on 25 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.filechoosers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import javax.swing.JFileChooser;

import ontologyEditor.Constants;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class SyntexFileChooser extends JFileChooser
{
	/**
	 * The property key for last directory where Syntex files were found. 
	 */
	public static String LAST_USED_DIRECTORY_KEY = "syntex.directory";
	/**
	 * The path of this directory, whether it has been read from bundle or not. 
	 * If nothing has been found in the ontologyEditor.resources.global.properties, 
	 * the default value is the user home directory. 
	 */
	public static final String LAST_USED_DIRECTORY;
	
	static
	{
		String value = "";
		Properties prop = new Properties();
		File file = new File(Constants.DEFAULT_RESOURCES_PATH );
		try 
		{
			URL[] urls={file.toURL()};
			URLClassLoader classLoader = new URLClassLoader(urls);
		
			InputStream propertiesStream = classLoader.getResourceAsStream("global.properties");
			
			try
			{
				prop.load(propertiesStream);
				value = prop.getProperty(LAST_USED_DIRECTORY_KEY);
			}
			catch (IOException e)
			{
				value = System.getProperty("user.home");
			}
		}
		catch (MalformedURLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LAST_USED_DIRECTORY = value;
	}

	/**
	 * 
	 */
	public SyntexFileChooser()
	{
		super(LAST_USED_DIRECTORY);
		this.setDialogTitle("Import des sorties Syntex");
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		this.setMultiSelectionEnabled(false);
	}
}
