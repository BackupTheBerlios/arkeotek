/**
 * Created on 6 juin 2005
 * 
 * Arkeotek Project
 */
package ontologyEditor.gui.dialogs;

import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JProgressBar;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class FenetreBarreProgression extends JDialog
{

	Thread thread;
	
	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public FenetreBarreProgression(Frame owner, String title, boolean modal) throws HeadlessException
	{
		super(owner, title, modal);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setIndeterminate(true);
		this.add(progressBar);
		this.pack();
		this.setLocation((owner.getLocation().x) + (owner.getSize().width/2) - (this.getSize().width/2),(owner.getLocation().y) + (owner.getSize().height/2) - (this.getSize().height/2));
	}
	
	public void launch()
	{
		this.thread = new Thread()
		{public void run()
		{
			FenetreBarreProgression.this.setVisible(true);
		}
		}
		;
	}
	
	public void stop()
	{
		this.thread.interrupt();
	}

}
