package arkeotek.io.exporter.OWL;

import java.io.File;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import ontologyEditor.ApplicationManager;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import arkeotek.io.IService;
import arkeotek.io.db.Service;
import arkeotek.io.db.Transaction;
import arkeotek.io.exporter.AbstractExporter;
import arkeotek.ontology.Concept;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;


public class Exporter extends AbstractExporter
{

//	protected OWLOntology ontology;
//	protected OWLClass clazz;
	
	public Exporter()
	{
		super(ApplicationManager.ontology,"");
	}
	
	public Exporter(Ontology owner,String file_path)
	{
		super(owner, file_path);
	}

	public void performExport() throws Exception
	{
		super.performExport();
		
		//ontology.
	}
	
	public void ecrFichierOWL()
	{
		File file = null;
		JFileChooser fj = new JFileChooser();
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("owl");
		fj.setFileFilter(filter);

		boolean bool = false;
		do
		{
			fj.showSaveDialog(null);
			file = fj.getSelectedFile();
			if (file == null)
			{
				return;
			}
			String nomFile = file.getName();
			if (!(nomFile.endsWith(".owl")))
			{
				nomFile = new String(nomFile + ".owl");
				file = new File(file.getParent(), nomFile);
			}
			if (file.exists())
			{
				int rep = JOptionPane.showConfirmDialog(null,VarGlobales.resTerm.getString("Ce_fichier_existe_d_j"));
				bool = rep == JOptionPane.YES_OPTION;
			}
			else
			{
				bool = true;
			}
		} while(!bool);
		
		

		FileWriter writer;
		try
		{
			writer = new FileWriter(file);
			System.out.println(writer.getEncoding());
			
			org.jdom.Element kbOwl = new Element("RDF", OwlConstants.espNomRdf);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomOwl);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomRdfs);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomXMLSchema);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomArkeotek);

			
			// Création d'un Document XML de JDOM qui contiendra l'élément racine nommé "RDF"
			Document myDocument = new Document(kbOwl);
			
			// On vérifie qu'une ontologie est chargée
			if(ApplicationManager.ontology!=null)
			{
				// On construit l'élément "Ontology" que l'on rajoute dans la racine du fichier XML (élément RDF)
				Element baliseOntologie = new Element("Ontology", OwlConstants.espNomOwl);
				baliseOntologie.setAttribute("about", ApplicationManager.ontology.getName(), OwlConstants.espNomRdf);
				kbOwl.addContent(baliseOntologie);
			
				// Les concepts de l'ontologie
				// On doit parcourir la base de données
				// Pour chaque concept il faut donner son concept pere si celui ci en a un !
				// Tous les concept qui n'ont pas de pere seront traités comme orphelins .. en fait on fait rien de spécial pour l'instant ..
				
				Transaction trans = new Transaction(ApplicationManager.ontology.getDataAccessor());
				Statement stat = trans.getConnexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				
				String requete = "SELECT id, name FROM t_concept";
				ResultSet rs = stat.executeQuery(requete);
				
				Statement stat2 = trans.getConnexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
				String requete2 = "SELECT idSource, idTarget FROM l_concept2concept";
				ResultSet rs2 = stat2.executeQuery(requete2);
				// Pour chaque concept on regarde si il est sous concept d'un autre concept...
				while(rs.next())
				{
					Integer id = rs.getInt("id");
					String name = rs.getString("name");
					
					Element baliseConcept = new Element(OwlConstants.classe, OwlConstants.espNomOwl);
					baliseConcept.setAttribute("ID", name, OwlConstants.espNomRdf);
					
					baliseOntologie.addContent(baliseConcept);
					
					Integer idPere = null;
					boolean conceptPereTrouve = false;
					rs2.beforeFirst(); // On se replace au début du curseur pour reparcourir celui-ci
					while(rs2.next() && !conceptPereTrouve)
					{
						Integer idSource = rs2.getInt("idSource");
						Integer idTarget = rs2.getInt("idTarget");
												
						if(id == idTarget)
						{
							conceptPereTrouve = true; // pour sortir de la boucle
							idPere = idSource;		  // pour savoir l'id du pere
						}
					}

					// On test si un pere 
					if(conceptPereTrouve && idPere!=null)
					{
						// On cherche le nom du pere avec une requete (statement) sans oublier de fermer le curseur
						Statement stat3 = trans.getConnexion().createStatement();
						String requete3 = "SELECT name FROM t_concept WHERE id = " + idPere.toString();
						ResultSet rs3 = stat3.executeQuery(requete3);
						rs3.first();
						String nomDuPere = rs3.getString("name");
						rs3.close();
						
						// On rajoute, à l'interieur de la balise du concept, une balise qui indique que ce concept est fils du concept "nomDuPere".. 
						Element baliseSubClass = new Element(OwlConstants.subClassOf, OwlConstants.espNomRdfs);
						baliseSubClass.setAttribute("resource", "#" + nomDuPere, OwlConstants.espNomRdf);
						baliseConcept.addContent(baliseSubClass);
						
						//Element baliseConceptPere = new Element(OwlConstants.classe, OwlConstants.espNomOwl);
						//baliseConceptPere.setAttribute("resource", "#" + nomDuPere, OwlConstants.espNomRdf);
						
						//baliseSubClass.addContent(baliseConceptPere);
					}
				}

				// On peut fermer rs et rs2
				rs.close();
				rs2.close();
				
				// On ferme la connexion et on commit la transaction !
				trans.getConnexion().close();
				trans.commit();
			}

			// On écrit notre document XML dans le fichier
			XMLOutputter outputter = new XMLOutputter();
			outputter.output(myDocument, writer);
			
			// On ferme l'accès au fichier !
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
			
			
			// ******************************************* FIN *********************************************************
			/*
			KB kbAecrit;
			if (RSOwl.kbOwlEnCours == null)
			{
				kbAecrit = RS.kbEnCours;
			}
			else
			{
				kbAecrit = RSOwl.kbOwlEnCours;
			}
			*/

			//création de la balise Ontology

			
			
			// Traitement des orphelins ?
			// Serais-ce les concepts qui ne sont pas rattachés à TOP
			// Existent ils dans Arkeotek
			/*Vector<Concept> lesOrphelins = kbAecrit.lesSansPere();

			Concept cgen;

			String[] tabConcept = new String[lesOrphelins.size()];
			
			for (int i = 0; i < lesOrphelins.size(); i++)
			{
				tabConcept[i] = ((ConceptAbstrait) lesOrphelins.elementAt(i)).getNom();
			}
			
			if (lesOrphelins.size() != 1)
			{
				JOptionPane.showMessageDialog(null, VarGlobales.resTerm.getString("avant_de_sauvegarder"));
				String top = (String) JOptionPane.showInputDialog(null,VarGlobales.resTerm.getString("cliquer_sur_le_top_de"),VarGlobales.resTerm.getString("information"),JOptionPane.INFORMATION_MESSAGE, null, tabConcept,tabConcept[0]);
				cgen = RS.kbEnCours.qc(top);
				// on force les autres concepts à avoir pour pere top
				for (int i = 0; i < lesOrphelins.size(); i++)
				{
					ConceptAbstrait cgenOrph = (ConceptAbstrait) lesOrphelins.elementAt(i);
					if (!cgenOrph.equals(cgen))
					{
						cgenOrph.initPere(cgen);
					}
				}
			}
			else
			{
				// on ecrit tous les concepts
				cgen = (Concept) lesOrphelins.elementAt(0);
				boolean sauvTerm = true;
				int sauvOccu = 0;
				String adrUrl = null;
				
				int rep = JOptionPane.showConfirmDialog(null,VarGlobales.res.getString("aspect_term"));
				if(rep == JOptionPane.OK_OPTION)
				{
					rep = JOptionPane.showConfirmDialog(null,VarGlobales.res.getString("sauv_occu"));
					if(rep == JOptionPane.OK_OPTION)
					{
						//sauvegarde sous forme textuelle ou URL
						String[] selValue = new String[2];
						selValue[0]="text";
						selValue[1] = "url";
						String repTextUrl = (String)JOptionPane.showInputDialog(null,VarGlobales.res.getObject("sauv_occu_text_url"),
								"Question", JOptionPane.INFORMATION_MESSAGE, null,
								selValue, selValue[0]);
						if(repTextUrl.equals("text") )
						{
							sauvOccu = RSOwl.sauvOccuText;
						}
						else
						{
							sauvOccu = RSOwl.sauvOccuUrl;
							adrUrl = JOptionPane.showInputDialog(null,VarGlobales.res.getString("adr_url"));
						}
						//+ sauvegarde terminologique
						cgen.ecritConceptOwl(kbOwl,sauvTerm,sauvOccu,adrUrl);
					}
					else
					{	sauvOccu = RSOwl.pasSauvOccu;
						cgen.ecritConceptOwl(kbOwl,sauvTerm,sauvOccu, adrUrl);
					}
				}
				else
				{
					sauvTerm = false;
					// pas de sauvegarde terminologique
					cgen.ecritConceptOwl(kbOwl,sauvTerm,sauvOccu, adrUrl);
				}
				
				ecritOwl.add(cgen.getNom());
				
				if (cgen.isChildren())
				{
					ecrFilsOwl(cgen, kbOwl,sauvTerm, sauvOccu, adrUrl);
				}
				
				XMLOutputter outputter = new XMLOutputter("  ", true,"ISO-8859-1");

				outputter.output(myDocument, writer);
				writer.close();
				JOptionPane.showMessageDialog(this.fenRSOwl,VarGlobales.resTerm.getString("Fichier_cr_"),VarGlobales.resTerm.getString("Information"),JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch (Exception e)
		{
			//Outils.handleException(this, e);
			System.err.println(e);
		}
		finally
		{
			writer = null;
			file = null;
		};
	}
	
*/	
	
	/**
	 * Méthode ecrFilsOwl
	 * @param cgen on va dire le concept courant
	 * @param kbOwl Element dans 
	 * @param sauvTerminologique
	 * @param sauvOccu
	 * @param adrUrl
	 */
	
/*	public void ecrFilsOwl(Concept cgen, org.jdom.Element kbOwl, boolean sauvTerminologique,int sauvOccu, String adrUrl)
	{
		if (!cgen.isChildren())
		{
			return;
		}
		TreeSet<ConceptAbstrait> sesFils = cgen.getSubsume();

		for (Iterator<ConceptAbstrait> i = sesFils.iterator(); i.hasNext();)
		{
			ConceptAbstrait cAbs = (ConceptAbstrait) i.next();
			// System.err.println("trace cAbs nom "+cAbs.getNom()+" classe "+cAbs.getClass().getName());
			ConceptAbstrait cSauv = RS.kbEnCours.qcAbs(cAbs.getNom());
			// System.err.println("trace "+cSauv.getClass().getName());
			// modification
			// recherche du type par le nom
			// car pb en cas de modif concept en concept enuméré
			// le type concept enuméré n'est pas gardé
			if (cSauv instanceof ConceptEnumere)
			{
				// ConceptEnuméré cgenF=(ConceptEnuméré)cSauv;
				if (ecritOwl.add(cSauv.getNom()))
				{
					// System.err.println("trace RS ecrisFilsXml conceptenum
					// "+cgenF.getNom());
					cSauv.ecritConceptOwl(kbOwl,sauvTerminologique, sauvOccu, adrUrl);
				}
			}
			else
			{
				if (cSauv instanceof Concept)
				{
					if (ecritOwl.add(cSauv.getNom())) 
					{
						cSauv.ecritConceptOwl(kbOwl, sauvTerminologique, sauvOccu, adrUrl);
					}
				}
				else
				{
					ConceptInd cgenF = (ConceptInd) cAbs;
					if (ecritOwl.add(cgenF.getNom()))
					{
						cgenF.ecritConceptOwl(kbOwl, sauvTerminologique, sauvOccu, adrUrl);
					}
				}
			}
		}
		
		for (Iterator<ConceptAbstrait> i = sesFils.iterator(); i.hasNext();)
		{
			ConceptAbstrait cAbs = (ConceptAbstrait) i.next();
			if (cAbs instanceof ConceptEnumere)
			{
				ConceptEnumere cgenF = (ConceptEnumere) cAbs;
				ecrFilsOwl(cgenF, kbOwl, sauvTerminologique, sauvOccu, adrUrl);
			}
			else
			{
				if (cAbs instanceof Concept)
				{
					Concept cgenF = (Concept) cAbs;
					ecrFilsOwl(cgenF, kbOwl, sauvTerminologique, sauvOccu, adrUrl);
				}
			}
		}*/
	
}
