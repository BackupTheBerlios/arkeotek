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
public class ProgressBarDialog extends JDialog
{

	Thread thread;
	
	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @throws HeadlessException
	 */
	public ProgressBarDialog(Frame owner, String title, boolean modal) throws HeadlessException
	{
		super(owner, title, modal);
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setString("");
		progressBar.setIndeterminate(true);
		this.add(progressBar);
		this.pack();
	}
	
	public void launch()
	{
		this.thread = new Thread()
		{public void run()
		{
			ProgressBarDialog.this.setVisible(true);
		}
		}
		;
	}
	
	public void stop()
	{
		this.thread.interrupt();
	}

}
