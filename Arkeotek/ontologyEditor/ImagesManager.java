/**
 * Created on 19 mai 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class ImagesManager
{
	private static ImagesManager imageManager = null;
    
    private String iconsPath = null;
    
    private ImagesManager()
    {
		this.iconsPath = Constants.DEFAULT_ICONS_PATH;
    }

	/**
	 * @return The instance of <code>ImagesManager</code>. 
	 * If no <code>ImagesManager</code> exist yet, the getInstance() method creates it. 
	 */
	static public ImagesManager getInstance()
    {
        if (ImagesManager.imageManager == null)
        {
            ImagesManager.imageManager = new ImagesManager();
        }
        return ImagesManager.imageManager;
    }

	
    /**
     * Sets the path to the image resources directory. 
     * @param path The path to the resources. 
     */
    public void setImagePath(String path)
    {
        if (path.endsWith("/"))
        {
            this.iconsPath = path;
        }
        else
        {
            this.iconsPath = path + "/";
        }
    }

   
    /**
     * @param resourceName The system file of the small icon wished. 
     * This method uses <code>getImageResource(String resourceName)</code>. 
     * @return The small sized <code>Icon</code> object associated to the resource file.
     */
    public Icon getSmallIcon(String resourceName)
    {
        if (this.iconsPath == null)
        {
            throw new NullPointerException();
        }
        return new ImageIcon(this.getImageResource(resourceName).getScaledInstance(16, 16, Image.SCALE_SMOOTH));
    }

    
    /**
     * @param resourceName The system file of the icon wished. 
     * This method uses <code>getImageResource(String resourceName)</code>. 
     * @return The <code>ImageIcon</code> object associated to the resource file.
     * @throws Exception
     */
    public ImageIcon getIcon(String resourceName) throws Exception
    {
        if (this.iconsPath == null)
        {
            throw new Exception();
        }
        return new ImageIcon(this.getImageResource(resourceName));
    }
	
	/**
	 * @param resourceName The system file name of the image resource wished. 
	 * Note that the path to this file is automatically added at the beginning of <code>resourceName</code>. 
	 * @return The <code>Image</code> object associated to the resource file. 
	 */
	public Image getImageResource(String resourceName)
    {
        String fileName = resourceName;
        if (fileName.startsWith("/"))
        {
            fileName = fileName.substring(1);
        }
        Image img = null;    
        InputStream image;
        try
        {
			File file = new File(this.iconsPath);
			URL[] urls = {file.toURL()};
			URLClassLoader classLoader = new URLClassLoader(urls);
			if ((image = classLoader.getResourceAsStream(fileName)) == null)
                throw new FileNotFoundException();
            img = ImageIO.read(image);
            image.close();
        }
        catch(IOException e)
        {
            img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
        }
        return img;
    }
	
	/**
	 * Return the default icon associated with each class of <code>LinkableElement</code>
	 * @param element 
	 * @return the default icon for the element
	 */
	public Icon getDefaultIcon(LinkableElement element)
	{
		try
		{
			String fileName;
			if (element instanceof Concept)
				fileName = Constants.DEFAULT_CONCEPT_ICON;
			else if (element instanceof Lemma)
				fileName = Constants.DEFAULT_LEMMA_ICON;
			else if (element instanceof DocumentPart)
				fileName = Constants.DEFAULT_DOCUMENTPART_ICON;
			else if (element instanceof Relation)
				fileName = Constants.DEFAULT_RELATION_ICON;
			else
				fileName = Constants.DEFAULT_ICON;
	        return this.getSmallIcon(fileName);
		}catch (NullPointerException e)
		{
			return this.getSmallIcon(Constants.DEFAULT_ICON);
		}
	}
}
