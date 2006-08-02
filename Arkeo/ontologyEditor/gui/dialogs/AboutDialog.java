/**
 * Created on 6 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import ontologyEditor.ApplicationManager;
import ontologyEditor.DisplayManager;
import ontologyEditor.ImagesManager;

/**
 * @author Sanmartin Julien
 * @author Nouhen Hubert
 *
 */
public class AboutDialog extends JDialog
{
	public static final String DEFAULT_IMAGEICON = "lance.jpg";
	
	private JButton ok = new JButton(ApplicationManager.getApplicationManager().getTraduction("close"));

	public AboutDialog()
	{
		this(DisplayManager.mainFrame);
	}

 	/**
	 * @param frame The <code>JFrame</code> holding this <code>JPanel</code>. 
	 */
	private AboutDialog(JFrame frame)
	{
        super(frame, ApplicationManager.getApplicationManager().getTraduction("abouttitle"), true);
        BorderLayout borderLayoutPrincipal = new BorderLayout();
        this.setLayout(borderLayoutPrincipal);

		// Layout Principal : Partie de Gauche
        ImagesManager im = ImagesManager.getInstance();
        ImageIcon image = null;
        try
        {
        	image = im.getIcon(DEFAULT_IMAGEICON);
		}
        catch (Exception e1)
        {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        JLabel labelImage = new JLabel(image);
        this.add(labelImage,BorderLayout.WEST);
        
        // Layout Principal : Partie de Droite
        BorderLayout borderLayoutSecondaire = new BorderLayout();
        JPanel PanneauDroit = new JPanel();
        PanneauDroit.setLayout(borderLayoutSecondaire);
        this.add(PanneauDroit,BorderLayout.EAST);
        
        	// Layout Secondaire : Partie du Haut
        	PanneauDroit.add(new JLabel("Arkeotek v1.0"),BorderLayout.NORTH);
        	
        	// Layout Secondaire : Partie du Bas
            String titres[] = {"Nom", "Prénom", "Rôle"};
            Object donnees[][] = { {"Bernadou", "Pierre", "Analyste & Programmeur"},
            					 {"Czerny",     "Jean",   "Analyste & Programmeur"},
            					 {"Sanmartin",  "Julien", "Analyste & Programmeur"},
            					 {"Nouhen",     "Hubert", "Analyste & Programmeur"} };
            JTable auteurs = new JTable(donnees,titres);
            auteurs.setPreferredSize(new Dimension(390,350));
            PanneauDroit.add(auteurs,BorderLayout.CENTER);
 
        
		this.ok.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				AboutDialog.this.dispose();
			}
		});
		PanneauDroit.add(ok,BorderLayout.SOUTH);
		
		this.setResizable(false);
		this.pack();
		this.setSize(550,400);
		this.setLocation((frame.getLocation().x) + (frame.getSize().width/2) - (this.getSize().width/2),(frame.getLocation().y) + (frame.getSize().height/2) - (this.getSize().height/2));
		this.setVisible(true);
	}
}