package ontologyEditor.gui.dialogs.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPopupMenu;

import com.sun.corba.se.spi.orbutil.fsm.Action;

public class PopupArbreConcept extends JPopupMenu implements ActionListener{

	//private JMenuItem renommer ;
	//private JMenuItem supprimer ;
	//private JMenuItem propriete	;
	
	public PopupArbreConcept() {
		/*this.nomScenario=nom;
		this.renommer = new JMenuItem (Application.getApplication().getTraduction("Renommer_Scenario")) ;
		this.add (this.renommer) ;

		this.renommer.addActionListener (this) ;               
		this.supprimer = new JMenuItem (Application.getApplication().getTraduction("Supprimer_Scenario")) ;
		this.add (this.supprimer) ;
		this.supprimer.addActionListener (this) ;  
		
		this.addSeparator();
		
		this.propriete = new JMenuItem (Application.getApplication().getTraduction("Propriete_Scenario")) ;
		this.add (this.propriete) ;
		this.propriete.addActionListener (this) ;  
*/	}

	public PopupArbreConcept(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	/*	Object source = e.getSource() ;
		// si on renomme ouverte d'une fenetre
		if (source == this.renommer)
		{
			
			FenetreRenommerScenario fsc=new FenetreRenommerScenario(Application.getApplication().getFenetrePrincipale(),this.nomScenario);
			fsc.show();
		}
		//sinon on supprime le scnario du processus 
		else if (source == this.supprimer)
		{
			// on retire le modele du vector DiagrammeModele
			Vector modele=Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe().getModelesDiagrammes();
			for (int i=1;i<modele.size();i++)
			{
				if (((GraphModelView)modele.elementAt(i)).getNomDiagModel().equals(this.nomScenario))
				{
					modele.removeElementAt(i);
				}
			}
			// on retire le scenario de l'arbre du referentiel 
			Referentiel ref=Application.getApplication().getReferentiel();
			for(int i=0;i<ref.getNoeudScenarios().getChildCount();i++)
			{
				ElementReferentiel courant=((ElementReferentiel)ref.getNoeudScenarios().getChildAt(i));
				if (courant.getNomElement().equals(this.nomScenario))
				{
					courant.removeFromParent();
					ref.majObserveurs(Referentiel.CHANGED);
				}
			}
			Application.getApplication().getProjet().setModified(true);
		}
		else if (source== this.propriete)
		{
			for (int i=0;i<Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe().getModelesDiagrammes().size();i++)
			{
				GraphModelView courant=(GraphModelView)Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe().getModelesDiagrammes().elementAt(i);
				if (courant.getNomDiagModel().equals(this.nomScenario))
				{
					FenetreProprieteScenario fsc=new FenetreProprieteScenario(Application.getApplication().getFenetrePrincipale(),courant);
					fsc.show();
				}				
			}
			Application.getApplication().getProjet().setModified(true);
			
		}
	*/}

}
