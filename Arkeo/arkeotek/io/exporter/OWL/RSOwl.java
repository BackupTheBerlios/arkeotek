package arkeotek.io.exporter.OWL;



//pour le fichier XML
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import javax.swing.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;



/**
* <p>
* Title: TERMINAE
* </p>
* création d'une classe RS d'entrée pour les fichiers Owl
* <p>
* Description: Outil d'aide à la construction d'ontologies~
* </p>
* <p>
* Copyright: Copyright (c) 2002
* </p>
* <p>
* Company: LIPN
* </p>
* 
* @author Sylvie Szulman
* @version 6.0
*/

public class RSOwl implements OwlConstants {
	private static Logger traceur = Logger.getLogger("owl.RSOwl");

	public RSOwl() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public FenRS fenRSOwl;

	public static KBOwl kbOwlEnCours;

	// public MyContentHandler unDefHand; //le receptuer des événements Sax
	// public ErreurHandler unDefErrorHand; //le recepteur d'erreurs

	// public Namespace espNomXml =
	// Namespace.getNamespace("xml","http://www.w3.org/1999");
	/** *******modif dec 2002*********** */
	public static Vector sauvPereFils = new Vector(); // sert pour définir les

	// relations pere-fils
	// entre classes

	public static Vector sauvProp = new Vector(); // sert pour définir les

	// sous propriétés

	public java.util.List listeEspNoms;

	public static TreeSet<String> ecritOwl = new TreeSet<String>();

	public static Namespace espaceNomDefaut = null; // pour repérer l'espace de

	// nom par défaut

	private static Vector<String> sauvPropMereFille = new Vector<String>();
	//sert à n'écrire qu'une seul fois ID lors de l'écriture des noms de propriétés en oWL
 public static Vector<String> sauvLesNomsRoles = new Vector<String>();
	// // provisoire
 /*
	 * 0 pas desauvegarde des occurrences, 1 sauvegarde textuelle, 2 sauvegarde URL
	 */
 public static final int pasSauvOccu = 0;
 public static final int sauvOccuText = 1;
 public static final int sauvOccuUrl = 2;

	/** *************************************************************************** */
	public RSOwl(FenRS fen) {
		fenRSOwl = fen;
	}

	/**
	 * Lecture d'un fichier avec SAX
	 */
	/*
	 * public void analyseFichXMLParSax(File f){ SAXParserFactory fabriqueSAX=
	 * SAXParserFactory.newInstance(); XMLReader xmlReader=null; try{ //création
	 * du parser SAX SAXParser saxParser = fabriqueSAX.newSAXParser();
	 * //transformation de saxParser en un objet XmlReader xmlReader =
	 * saxParser.getXMLReader(); unDefErrorHand = new ErreurHandler(); unDefHand =
	 * new MyContentHandler(this,unDefErrorHand);
	 * xmlReader.setContentHandler(unDefHand);
	 * xmlReader.setErrorHandler(unDefErrorHand);
	 * xmlReader.parse(f.getParent()+"/"+f.getName()); } catch(SAXException e) {
	 * System.err.println("erreur sax "+e+" "+unDefHand.getNumElement()); }
	 * catch(IOException e){ System.err.println("erreur E/S "+e); }
	 * catch(ParserConfigurationException e){ System.err.println("erreur config
	 * "+e); } }
	 */
	/**
	 * lecture d'un fichier XML (rdfs) pour créer une ontologie avec JDOM
	 */
	public void lecFichierOWL() {

		try {
			ExtensionFileFilter filter = new ExtensionFileFilter();
			filter.addExtension("xml");
			filter.addExtension("rdfs");
			filter.addExtension("owl");

			String dir = VarGlobales.RepertoireOntologies;

			//
			JFileChooser fj = new JFileChooser(new File(dir));
			fj.setFileFilter(filter);
			fj.addChoosableFileFilter(filter);
			int resul = fj.showOpenDialog(null);
			if (resul == JFileChooser.APPROVE_OPTION) {
				File file = fj.getSelectedFile();
				boolean correct = lecFichierOWL(file);
				// this.analyseFichXMLParSax(file);
				// visualisation des objets Owl créés
				if (correct) {
					RSOwl.kbOwlEnCours.dumpOwl();
					kbOwlEnCours.creatOntoOwl();
				}
			} // fin if

		} // fin try

		catch (java.lang.Exception e) {
			JOptionPane.showMessageDialog(null, "lectFich " + e);
			Outils.handleException(this, e);
		}

		return;
	}

	/**
	 * lecture à partir d'un fichier Owl ou rdfs
	 */
	public boolean lecFichierOWL(File f) {

		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(f);

			Element elemRoot = doc.getRootElement();
			String strRdf = elemRoot.getName();
			if (!(strRdf.equals("RDF"))) {
				throw new JDOMException(VarGlobales.res.getString("bal_RDF"));
			}
			// recherche des espaces noms
			this.rechercheEspNoms(elemRoot);
			Element elemOnto = elemRoot.getChild("Ontology", espNomOwl);
			if (elemOnto == null) {
				// cas de l'espace par défaut
				elemOnto = elemRoot.getChild("Ontology");
				if (elemOnto == null) {
					throw new JDOMException(VarGlobales.res
							.getString("bal_Onto"));
				}
			}
			// création d'une ontologie
			this.creatOntology(f.getName());
			// création des classes
			this.creatLesClasses(elemRoot);
			// création des concepts individuels
			this.creatIndividus(elemRoot);
			// création des propriétés
			// creation desobjectProperty
			// création des propriétés = rôles - owl:ObjectProperty
			this
					.creatLesObjectProperties(elemRoot,
							OwlConstants.objectProperty);
			this.creatLesObjectProperties(elemRoot,
					OwlConstants.transitiveProperty);
			this.creatLesObjectProperties(elemRoot,
					OwlConstants.symetricProperty);
			this.creatLesObjectProperties(elemRoot,
					OwlConstants.functionalProperty);
			// création des datatypeProperty
			this.creatLesDataTypeProperties(elemRoot);
			System.out.println(VarGlobales.resTerm.getString("succes"));

		} catch (JDOMException e) {
			Outils.handleException(this, e);
			JOptionPane.showMessageDialog(null, VarGlobales.res
					.getString("format_incorrect"));
			return false;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
			Outils.handleException(this, e);
			return false;
		}
		return true;
	}

	public void ecrFichierOWL(String nomKB) {
		String dir = VarGlobales.RepertoireOntologies;
		File file;

		JFileChooser fj = new JFileChooser(dir);
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("owl");
		fj.setFileFilter(filter);

		boolean bool = false;
		do {
			int result = fj.showSaveDialog(null);
			file = fj.getSelectedFile();
			if (file == null) {
				return;
			}
			String nomFile = file.getName();
			if (!(nomFile.endsWith(".owl"))) {
				nomFile = new String(nomFile + ".owl");
				file = new File(file.getParent(), nomFile);
			}
			if (file.exists()) {
				int rep = JOptionPane.showConfirmDialog(null,
						VarGlobales.resTerm.getString("Ce_fichier_existe_d_j"));
				bool = rep == JOptionPane.YES_OPTION;
			} else {
				bool = true;
			}
		} while (!bool);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			// ecriture de l'entete
			// entete du fichier
			//
			/*
			 * <rdf:RDF xmlns:dc="http://purl.org/dc/elements/1.1/"
			 * xmlns:owl="http://www.w3.org/2002/07/owl#"
			 * xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
			 * xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
			 * xmlns:xsd="http://www.w3.org/2000/10/XMLSchema#">
			 */
			org.jdom.Element kbOwl = new Element("RDF", OwlConstants.espNomRdf);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomOwl);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomRdfs);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomXMLSchema);
			kbOwl.addNamespaceDeclaration(OwlConstants.espNomTerminae);

			Document myDocument = new Document(kbOwl);
			KB kbAecrit;
			if (RSOwl.kbOwlEnCours == null) {
				kbAecrit = RS.kbEnCours;
			} else {
				kbAecrit = RSOwl.kbOwlEnCours;
			}
			// création de la balise Ontology
			org.jdom.Element balOnto = new Element("Ontology",
					OwlConstants.espNomOwl);
			balOnto.setAttribute("about", "",OwlConstants.espNomRdf);
			kbOwl.addContent(balOnto);
			Vector<Concept> lesOrphelins = kbAecrit.lesSansPere();

			Concept cgen;

			String[] tabConcept = new String[lesOrphelins.size()];
			for (int i = 0; i < lesOrphelins.size(); i++) {
				tabConcept[i] = ((ConceptAbstrait) lesOrphelins.elementAt(i))
						.getNom();
			}
			if (lesOrphelins.size() != 1) {
				JOptionPane.showMessageDialog(null, VarGlobales.resTerm
						.getString("avant_de_sauvegarder"));
				String top = (String) JOptionPane.showInputDialog(null,
						VarGlobales.resTerm.getString("cliquer_sur_le_top_de"),
						VarGlobales.resTerm.getString("information"),
						JOptionPane.INFORMATION_MESSAGE, null, tabConcept,
						tabConcept[0]);
				cgen = RS.kbEnCours.qc(top);
				// on force les autres concepts à avoir pour pere top
				for (int i = 0; i < lesOrphelins.size(); i++) {
					ConceptAbstrait cgenOrph = (ConceptAbstrait) lesOrphelins
							.elementAt(i);
					if (!cgenOrph.equals(cgen)) {
						cgenOrph.initPere(cgen);
					}
				}
			} else {

				// on ecrit tous les concepts
				cgen = (Concept) lesOrphelins.elementAt(0);
				boolean sauvTerm = true;
				int sauvOccu = 0;
				String adrUrl = null;
				
				int rep = JOptionPane.showConfirmDialog(null,VarGlobales.res.getString("aspect_term"));
				if(rep == JOptionPane.OK_OPTION){
					
					rep = JOptionPane.showConfirmDialog(null,VarGlobales.res.getString("sauv_occu"));
					if(rep == JOptionPane.OK_OPTION){
						//sauvegarde sous forme textuelle ou URL
						String[] selValue = new String[2];
						selValue[0]="text";
						selValue[1] = "url";
						String repTextUrl = (String)JOptionPane.showInputDialog(null,VarGlobales.res.getObject("sauv_occu_text_url"),
								"Question", JOptionPane.INFORMATION_MESSAGE, null,
								selValue, selValue[0]);
						if(repTextUrl.equals("text") ){
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
				if (cgen.isChildren()) {
					ecrFilsOwl(cgen, kbOwl,sauvTerm, sauvOccu, adrUrl);
				}

				XMLOutputter outputter = new XMLOutputter("  ", true,
						"ISO-8859-1");

				outputter.output(myDocument, writer);
				writer.close();
				JOptionPane.showMessageDialog(this.fenRSOwl,
						VarGlobales.resTerm.getString("Fichier_cr_"),
						VarGlobales.resTerm.getString("Information"),
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			Outils.handleException(this, e);
			System.err.println(e);
		} finally {
			writer = null;
			file = null;
		}
		;

	}

	public void ecrFilsOwl(Concept cgen, org.jdom.Element kbOwl, boolean sauvTerminologique,
			int sauvOccu, String adrUrl) {
		if (!cgen.isChildren()) {
			return;
		}
		TreeSet<ConceptAbstrait> sesFils = cgen.getSubsume();

		for (Iterator<ConceptAbstrait> i = sesFils.iterator(); i.hasNext();) {
			ConceptAbstrait cAbs = (ConceptAbstrait) i.next();
			// System.err.println("trace cAbs nom "+cAbs.getNom()+" classe "+cAbs.getClass().getName());
			ConceptAbstrait cSauv = RS.kbEnCours.qcAbs(cAbs.getNom());
			// System.err.println("trace "+cSauv.getClass().getName());
			// modification
			// recherche du type par le nom
			// car pb en cas de modif concept en concept enuméré
			// le type concept enuméré n'est pas gardé
			if (cSauv instanceof ConceptEnumere) {
				// ConceptEnuméré cgenF=(ConceptEnuméré)cSauv;
				if (ecritOwl.add(cSauv.getNom())) {
					// System.err.println("trace RS ecrisFilsXml conceptenum
					// "+cgenF.getNom());
					cSauv.ecritConceptOwl(kbOwl,sauvTerminologique, sauvOccu, adrUrl);
				}
			} else {
				if (cSauv instanceof Concept) {

					if (ecritOwl.add(cSauv.getNom())) {
						cSauv.ecritConceptOwl(kbOwl, sauvTerminologique, sauvOccu, adrUrl);
					}
				} else {
					ConceptInd cgenF = (ConceptInd) cAbs;
					if (ecritOwl.add(cgenF.getNom())) {
						cgenF.ecritConceptOwl(kbOwl, sauvTerminologique, sauvOccu, adrUrl);
					}
				}
			}
		}
		for (Iterator<ConceptAbstrait> i = sesFils.iterator(); i.hasNext();) {
			ConceptAbstrait cAbs = (ConceptAbstrait) i.next();
			if (cAbs instanceof ConceptEnumere) {
				ConceptEnumere cgenF = (ConceptEnumere) cAbs;
				ecrFilsOwl(cgenF, kbOwl, sauvTerminologique, sauvOccu, adrUrl);
			} else {
				if (cAbs instanceof Concept) {
					Concept cgenF = (Concept) cAbs;
					ecrFilsOwl(cgenF, kbOwl, sauvTerminologique, sauvOccu, adrUrl);
				}
			}
		}
		return;
	}

	/**
	 * on peut créer une classe anonyme sous DataType (
	 */

	public void creatConceptInd(Element elemOnto) throws Exception {
		// création des individuels - owl:Thing
		java.util.List listInd = elemOnto.getChildren(kbOwlEnCours
				.getTopConcept(), espNomOwl);

		for (Iterator i = listInd.iterator(); i.hasNext();) {
			Element elem = (Element) i.next();
			// nom = valeur de rdf:about
			String nomCInd = this.getAttributValeurRdf(elem);
			if (nomCInd == null) {
				throw new Exception(VarGlobales.resTerm
						.getString("Erreur_identifiant1"));
			}
			// classe
			Element elemType = elem.getChild("type", espNomRdf);
			String nomPere = kbOwlEnCours.getTopConcept();
			if (elemType == null) {
				// classe non définie
				throw new Exception(VarGlobales.resTerm
						.getString("Erreur_identifiant2"));
			} else {
				nomPere = this.getAttributValeurRdf(elemType);
			}
			kbOwlEnCours.creatConceptInd(nomCInd, nomPere,
					VarGlobales.resUtilss.getString("En_cours"));
		} // fin for
		// recherche des individus non définis par owl:Thing caractérisés par
		// un attribut rdf:ID
		listInd = elemOnto.getContent();
		java.util.List listElemInd = elemOnto.getChildren();
		// recherche des individus sans espace de noms
		for (Iterator i = listElemInd.iterator(); i.hasNext();) {
			Element elem = (Element) i.next();
			Attribute att = elem.getAttribute("ID", espNomRdf);
			if (att != null && elem.getNamespacePrefix().equals("")) {
				String nomCInd = att.getValue();
				String nomPere = elem.getName();
				kbOwlEnCours.creatConceptInd(nomCInd, nomPere,
						VarGlobales.resUtilss.getString("En_cours"));
			}
		}

	}

	public void creatLesObjectProperties(Element elemOnto, String baliseProp)
			throws Exception {

		java.util.List listProp = elemOnto.getChildren(baliseProp,
				espNomOwl);

		for (Iterator i = listProp.iterator(); i.hasNext();) {
			Element elemProp = (Element) i.next();
			OwlProperty uneOwlProp = this.creatUnePropriété(elemProp);
		//	 System.err.println("trace RSOWL 415 creat property "+uneOwlProp.getNom());
			// transitiveProperty
			if (baliseProp.equals(OwlConstants.transitiveProperty)) {
				uneOwlProp.setTransitive(true);
			}
			// FunctionalProperty

			if (baliseProp.equals(OwlConstants.functionalProperty)) {
				String nomFonc = this.getAttributValeurRdf(elemProp);
				uneOwlProp.setFonctionDe(nomFonc);
			}
			// SymmetricProperty
			if (baliseProp.equals(OwlConstants.symetricProperty)) {
				uneOwlProp.setSymetrique(true);
			}

		}
		// traitement des relations entre propriétés non traitées
		for (int i = 0; i < sauvPropMereFille.size(); i++) {
			String tabFilsPere = (String) sauvPropMereFille.elementAt(i);
			StringTokenizer st = new StringTokenizer(tabFilsPere, "\t");
			String lien = st.nextToken();
			if (lien.equals("mere_prop")) {
				String fille = st.nextToken();
				// System.err.println("trace fille "+fille);
				String nomDomFille = st.nextToken();
				String nomValFille = st.nextToken();
				String mere = st.nextToken();
				// System.err.println("trace mere "+mere);
				// String nomDomMere = st.nextToken();
				// String nomValMere = st.nextToken();
				OwlProperty uneOwlFille = kbOwlEnCours.qPOwl(fille,
						nomDomFille, nomValFille);
				OwlProperty uneOwlMere = kbOwlEnCours.qPOwl(mere);
				if (uneOwlFille == null) {
					System.err.println("erreur dans le fichier sous propriété "
							+ fille + " " + nomDomFille + " " + nomValFille
							+ " n'existe pas");
				} else {

					if (uneOwlMere == null) {
						// il faut sauvegarder le lien
						//test si la propriété mere est dans une ontologie référee
						//test si la mere est une propriété définie dans une ontologie externe
						Namespace nm = this.qEspNoms(mere);
						//si oui créer cette propriété
						if(nm != null){
							//this.creatUnePropriété()
						}
						else
						{
						System.err
								.println("erreur dans le fichier sous propriété "
										+ mere + " n'existe pas");
						}
						
					} else {
						uneOwlFille.setSuperProp(uneOwlMere);
					}
				}
			} else {
				if (lien.equals("inverseOf")) {
					String p1 = st.nextToken();
					String p2 = st.nextToken();
					// System.err.println("lien trace inverseOf p1 "+p1+" p2
					// "+p2);
					OwlProperty unOwlP1 = kbOwlEnCours.qPOwl(p1);
					OwlProperty unOwlP2 = kbOwlEnCours.qPOwl(p2);
					if (unOwlP1 != null) {
						unOwlP1.setInverseOf(p2);
					}
					if (unOwlP2 != null) {
						unOwlP2.setInverseOf(p1);
					}
				}
			}
		}

	} // fin

	// à traiter

	// InverseFunctional
	private OwlProperty creatUnePropriété(Element elemProp) throws Exception {

		String nom = this.getAttributValeurRdf(elemProp);
		// System.err.println("creatUnePropriété nom prop "+nom);
		// lecture domain
		Element domain = elemProp.getChild("domain", espNomRdfs);
		String valDomaine;
		if (domain == null) {
			valDomaine = kbOwlEnCours.getConceptEnsProprietes();
		} else {
			valDomaine = this.getAttributValeurRdf(domain);
			if (valDomaine == null) {
				// recherche d'une class owl incluse
				Element nomCl = domain
						.getChild("Class", OwlConstants.espNomOwl);
				if (nomCl != null) {
					valDomaine = this.getAttributValeurRdf(nomCl);
					if (valDomaine == null) {
						// le domaine est défini par une classe complexe
						// System.err.println("trace valDomaine null nom prop
						// "+nom);
						OwlClass uneCl = this.traitClassesComplexes(nomCl);
						valDomaine = uneCl.getNom();
					}
				} else {
					System.err
							.println("erreur cas à traiter pas de domaine pour la propriété "
									+ nom);
				}

			}
		}
		// System.err.println("trace nomR "+nomR+" domaine "+valDomaine);

		// lecture range
		Element range = elemProp.getChild("range", espNomRdfs);
		String valRange = EnsProprietes;
		if (range == null) {
			// JOptionPane.showMessageDialog(null,VarGlobales.resTerm.getString("Erreur_identifiant3")+
			// "par défaut mis à Thing");

		} else {
			valRange = this.getAttributValeurRdf(range);
			if (valRange == null) {
				// recherche d'une class owl incluse
				Element nomCl = range.getChild("Class", OwlConstants.espNomOwl);
				if (nomCl != null) {
					valRange = this.getAttributValeurRdf(nomCl);
				} else {
					System.err
							.println("erreur cas à traiter pas de range pour la propriété "
									+ nom);
				}
			}
		}
		// lors de la création d'une propriété, elle est mise dans l'ensemble
		// des propriéts de la kb en cours
		OwlProperty uneOwlProp = new OwlProperty(nom, valDomaine, valRange);
		// les autres balises associée

		// inverseOf
		Element inverseOf = elemProp.getChild(OwlConstants.inverseOf,
				OwlConstants.espNomOwl);
		if (inverseOf != null) {
			String nomInverse = this.getAttributValeurRdf(inverseOf);
			if (nomInverse == null) {
				// le nom est défini dans une balise Object property
				Element uneObjProp = inverseOf.getChild(
						OwlConstants.objectProperty, espNomOwl);
				if (uneObjProp == null) {
					Element elemFunctionnalObject = inverseOf.getChild(
							OwlConstants.functionalProperty, espNomOwl);
					if (elemFunctionnalObject != null) {
						nomInverse = this
								.getAttributValeurRdf(elemFunctionnalObject);
					} else {
						Element elemTransitiveObject = inverseOf.getChild(
								OwlConstants.transitiveProperty, espNomOwl);
						if (elemTransitiveObject != null) {
							nomInverse = this
									.getAttributValeurRdf(elemTransitiveObject);
						} else {
							Element elemInverseFunctionnal = inverseOf
									.getChild(
											OwlConstants.inverseFunctionalProperty,
											espNomOwl);
							if (elemInverseFunctionnal != null) {
								nomInverse = this
										.getAttributValeurRdf(elemInverseFunctionnal);
							} else {
								Outils.afficheHierOwl(inverseOf);
								Outils.afficheLesFilsOwl(inverseOf);
								throw new Exception(
										"erreur cas à traiter pas de nom de l'inverse pour la propriété "
												+ nom);
							}
						}
					}
				} else {
					nomInverse = this.getAttributValeurRdf(uneObjProp);
				}
			}
			uneOwlProp.setInverseOf(nomInverse);
			OwlProperty unePropInv = kbOwlEnCours.qPOwl(nomInverse);
			if (unePropInv != null) {
				unePropInv.setInverseOf(nom);
			} else {
				// on sauve
				String tabPropFilsPere = new String("inverseOf\t"); //
				tabPropFilsPere += nom + "\t";
				tabPropFilsPere += nomInverse;
				// System.err.println("trace sauvMereFille nomProp
				// "+nomPropMere+ " nom fille "+nom+" valDom "+valDomaine +"
				// valrange "+valRange);
				sauvPropMereFille.add(tabPropFilsPere);

			}
		}

		// annotationProperty
		// ontologyProperty
		// EquivalentProperties
		// subPropertyOf
		/*
		 * il peut y avoir plusieurs propriétés méres
		 */
		java.util.List listSousProp = elemProp.getChildren(
				OwlConstants.subPropertyOf, espNomRdfs);
		for (Iterator j = listSousProp.iterator(); j.hasNext();) {

			Element subPropertyOf = (Element) j.next();
			if (subPropertyOf != null) {
				String nomPropMere = this.getAttributValeurRdf(subPropertyOf);
				if (nomPropMere == null) {
					// cas de la propriété donnée par ObjectPrperty
					// cas d'une imbrication
					java.util.List listEnf = subPropertyOf.getChildren();
					if (listEnf.isEmpty()) {
						System.err
								.println("erreur format fichier classe sans nom "
										+ uneOwlProp.getNom());
						throw new Exception(
								"erreur format fichier classe sans nom "
										+ uneOwlProp.getNom());
					}
					for (Iterator i = listEnf.iterator(); i.hasNext();) {
						Element unElem = (Element) i.next();
						String nomBal = unElem.getName();

						// System.err.println("trace RSOWL 622 nomBal "+nomBal);
						if (nomBal.equals(OwlConstants.objectProperty)) {
							Element unElemInclus = subPropertyOf.getChild(
									OwlConstants.objectProperty, espNomOwl);
							nomPropMere = this
									.getAttributValeurRdf(unElemInclus);
							// test si la propriété mere existe sinon création
							// de la propriété
							OwlProperty uneProp = kbOwlEnCours
									.qPOwl(nomPropMere);
							if (uneProp == null) {
								this.creatUnePropriété(unElemInclus);
							}
						} else {
							Element elemTransitiveObject = subPropertyOf
									.getChild(OwlConstants.transitiveProperty,
											espNomOwl);
							if (elemTransitiveObject != null) {
								nomPropMere = this
										.getAttributValeurRdf(elemTransitiveObject);
								// test si la mere existe sinon, il faut créer
								// la propriété
								OwlProperty uneProp = kbOwlEnCours
										.qPOwl(nomPropMere);
								if (uneProp == null) {
									this
											.creatUnePropriété(elemTransitiveObject);
								}

							} else {
								System.err
										.println("trace cas à traiter apres OnProperty");
								throw new Exception(
										"erreur cas non traité nom sous propriété non trouvée");
							}
						}
					}

				}
				// System.err.println("trace nom sous prop "+nomPropMere);
				OwlProperty uneOwlP = kbOwlEnCours.qPOwl(nomPropMere);
				if (uneOwlP != null) {
					uneOwlProp.setSuperProp(uneOwlP);
				} else {
					// la propriété mère n'existe pas encore
					String tabPropFilsPere = new String("mere_prop\t"); // relation
					// pere
					// -fils
					// -pere
					tabPropFilsPere += nom + "\t" + valDomaine + "\t"
							+ valRange + "\t";
					tabPropFilsPere += nomPropMere;
					// System.err.println("trace sauvMereFille nomProp
					// "+nomPropMere+ " nom fille "+nom+" valDom "+valDomaine +"
					// valrange "+valRange);
					sauvPropMereFille.add(tabPropFilsPere);

				}
			} // fin for sur j
		}

		return uneOwlProp;
	}

	/**
	 * les propriétés valeurs type dans un langage de programmation
	 */
	public void creatLesDataTypeProperties(Element elemOnto) throws Exception {
		java.util.List listProp = elemOnto.getChildren("DatatypeProperty",
				espNomOwl);
		for (Iterator i = listProp.iterator(); i.hasNext();) {
			Element elemProp = (Element) i.next();
			this.creatUnePropriétéData(elemProp);
		}
	} // fin

	public OwlDataProperty creatUnePropriétéData(Element elemProp) {
		try {
			String nom = this.getAttributValeurRdf(elemProp);
			// System.err.println("trace datatype "+nom);
			// lecture domain
			Element domain = this.getBaliseOwlouRdf(elemProp, "domain");
			String valDomaine;
			if (domain == null) {
				// cas samePropertyAs !!!!
				Element samePropertyElem = elemProp.getChild("samePropertyAs",
						espNomOwl);
				if (samePropertyElem != null) {
					String nomSyn = this.getAttributValeurRdf(samePropertyElem);
					// recherche de la propriété de même nom
					OwlDataProperty uneDataP = kbOwlEnCours
							.getDataProperty(nomSyn);
					uneDataP.addSyn(nomSyn);
					return null;
				}
				valDomaine = kbOwlEnCours.getTopConcept();
			} else {
				valDomaine = this.getAttributValeurRdf(domain);
			}
			// System.err.println("trace nom "+nom+" domaine "+valDomaine);

			// lecture range
			Element range = this.getBaliseOwlouRdf(elemProp, "range");
			String valRange = "";
			if (range == null) {
				valRange = this.getAttributValeurRdf(elemProp);
				if (valRange == null) {
					throw new Exception(
							"erreur  range à null dans le datatype " + nom);
				}
				// on met range à Thing

			} else {
				valRange = this.getAttributValeurRdf(range);
			}
			this.creatClassXMLSchema(valRange);
			// lors de la création d'une propriété, elle est mise dans
			// l'ensemble des propriéts de la kb en cours
			OwlDataProperty uneOwlData = new OwlDataProperty(nom, valDomaine,
					valRange);
			return uneOwlData;
		} catch (Exception e) {
			System.err.println("erreur " + e);
		}
		return null;
	}

	public void rechercheEspNoms(org.jdom.Element elemRoot) {
		listeEspNoms = elemRoot.getAdditionalNamespaces();
		Namespace espVide = null;
		for (Iterator i = listeEspNoms.iterator(); i.hasNext();) {
			Namespace unEsp = (Namespace) i.next();
			if (unEsp.getPrefix().trim().equals("")) {
				espVide = unEsp;
				// System.err.println("trace esp vide " + unEsp.getURI());
			}
		}
		// recherche de la valeur de espVide s'il existe
		if (espVide != null) {
			for (Iterator i = listeEspNoms.iterator(); i.hasNext();) {
				Namespace unEsp = (Namespace) i.next();
				// System.err.println("trace " + unEsp.getURI() + " prefix " +
				// unEsp.getPrefix() + " " + espVide.getURI());
				// recherche de l'espace de nom par défaut
				if (unEsp.getURI().trim().equals(espVide.getURI().trim())) {
					espaceNomDefaut = unEsp;
				}
			}

		}
		if (espaceNomDefaut == null) {
			System.err.println("pas d'espace de noms par défaut- classe RSOWL");
		}
	}
	/**
	 * vérifie que l'espace de nom est une ontologie externe et
	 * donc que l'élément est définie ailleurs
	 * @param elem: élément 
	 * @return
	 */
	public Namespace qEspNoms(String elem) {
		//recherche de l'espace noms de elem
		String prefix = elem.substring(0,elem.indexOf("_"));
		Iterator<Namespace> i= this.listeEspNoms.iterator();
		while(i.hasNext()){
			Namespace nm = i.next();

			String uri = nm.getURI();
			//recherche du nom de l'onto
			int ind =uri.lastIndexOf("/");
		//	System.err.println("elem "+elem+" uri "+uri+" ind "+ind+" "+uri.length());
			if(ind !=-1 && uri.length()!= 0 && ind < uri.length()-1){
				String nomOnto = uri.substring(ind+1,uri.length()-1);
				if(nomOnto.equals(prefix)) return nm;
			}
			
		}
		
		return null;
	}

	// ///////////////////////////////////////////////////////////////////
	/**
	 * ***** les méthodes utilisées pour construire l'onto OWL
	 */
	private void creatOntology(String nomOnto) {
		// il faudrait tester about ou comment ou title pour trouver un nom
		// d'onto
		// pour ne pas avoir a taper le nom
		// String nomOnto = "test";
		// nomOnto=JOptionPane.showInputDialog(VarGlobales.res.getString("Nom_onto"));
		// if(nomOnto == null || nomOnto.trim().length()==0) return;
		if (!fenRSOwl.creatKBOwl(nomOnto)) {
			// création impossible, la base existe déjà
			JOptionPane.showMessageDialog(null, VarGlobales.res
					.getString("Nom_d_ontologie_ou_de"));
			return;
		}

	}

	/***************************************************************************
	 * traitements des classes ********
	 */
	public void creatLesClasses(Element elemRoot) {
		try {
			java.util.List listeClasses = this.getLesBalisesOwlouRdfs(
					elemRoot, "Class");

			for (Iterator i = listeClasses.iterator(); i.hasNext();) {
				Element uneClasse = (Element) i.next();
				String nomCl = this.getAttributValeurRdf(uneClasse);

				if (nomCl == null) {
					// cas de l'unionOf
					// System.err.println("trace creatLesClasses
					// "+uneClasse.getName());
					OwlClass owlCl = this.traitUnionOf(uneClasse);
					continue;
				}
				OwlClass owlCl = this.creatClassOwl(nomCl);
				if (owlCl == null) {
					throw new Exception("pb dans la création de la classe "
							+ nomCl);
				}
				// traitement du commentaire
				Element elemCommentaire = uneClasse.getChild("comment",
						espNomOwl);
				if (elemCommentaire == null) {
					elemCommentaire = uneClasse.getChild("comment", espNomRdfs);
				}
				if (elemCommentaire != null) {
					// il y a un commentaire
					if (owlCl.getComment() == null) {
						owlCl.setComment(elemCommentaire.getTextTrim());
					} else {
						owlCl.addComment(elemCommentaire.getTextTrim());
					}
				}

				// traitement des mères
				this.traitSubClassOf(uneClasse, owlCl);
				// traitement des complementOf
				this.traitComplementOf(uneClasse, owlCl);
				// traitement des unionOf
				this.traitUnionOf(uneClasse, owlCl);
				// traitement des intersectionOf
				this.traitIntersectionOf(uneClasse, owlCl);
				// traitement des disjointOf
				this.traitDisjointWith(uneClasse, owlCl);
				// traitement du oneOf
				this.traitOneOf(uneClasse, owlCl);
				// traitement de l'équivalence de classe
				this.traitEquivalentClass(uneClasse, owlCl);
			} // fin for i
		} catch (Exception e) {
			System.err.println("erreur " + e);
			// e.printStackTrace();
		}

	}

	// tratement des oneOf
	/**
	 * @param uneClasse:
	 *            la définition en XML de la classe qui peut contenir des OneOf
	 *            représentation par une classe enumérée
	 * @param owlCl:
	 *            une instance de OwlClass correspondante à la def en XML
	 * @throws Exception
	 */
	private void traitOneOf(Element uneClasse, OwlClass owlCl) throws Exception {
		Element unOneOf = this.getBaliseOwlouRdf(uneClasse, "oneOf");
		if (unOneOf == null) {
			return;
		}
		String nomCl = this.getAttributValeurRdf(uneClasse);
		// création de la classe
		OwlClass uneCl = this.creatClassOwl(nomCl);
		uneCl.setEnum(true);
		// on prend tous les enfants - ce sont des individus
		java.util.List listeInd = unOneOf.getChildren();
		for (Iterator i = listeInd.iterator(); i.hasNext();) {
			Element unInd = (Element) i.next();
			String nomInd = this.getAttributValeurRdf(unInd);
			// ajout de l'individu
			uneCl.addUnInd(nomInd);
		}
	}

	private OwlClass traitOneOf(Element uneClasse) throws Exception {
		Element unOneOf = this.getBaliseOwlouRdf(uneClasse, "oneOf");
		if (unOneOf == null) {
			return null;
		}
		String nomCl = this.getAttributValeurRdf(uneClasse);
		// création de la classe
		OwlClass uneCl = this.creatClassOwl(nomCl);
		uneCl.setEnum(true);
		// on prend tous les enfants - ce sont des individus
		java.util.List listeInd = unOneOf.getChildren();
		for (Iterator i = listeInd.iterator(); i.hasNext();) {
			Element unInd = (Element) i.next();
			String nomInd = this.getAttributValeurRdf(unInd);
			// ajout de l'individu
			uneCl.addUnInd(nomInd);
		}
		return uneCl;
	}

	// tratement des complementOf
	/**
	 * @param uneClasse:
	 *            la définition en XML de la classe qui peut contenir des
	 *            complementOf
	 * @param owlCl:
	 *            une instance de OwlClass correspondante à la def en XML
	 * @throws Exception
	 */
	private void traitComplementOf(Element uneClasse, OwlClass owlCl)
			throws Exception {

		java.util.List lesNegs = this.getLesBalisesOwlouRdfs(uneClasse,
				"complementOf");
		if (lesNegs == null || lesNegs.isEmpty()) {
			return;
		}

		for (Iterator j = lesNegs.iterator(); j.hasNext();) {
			Element elemNegDe = (Element) j.next();
			String nomNegDe = this.getAttributValeurRdf(elemNegDe);
			// System.err.println("traitement d'une classe définie par
			// complementOf "+nomNegDe);
			if (nomNegDe != null && nomNegDe.trim().length() != 0) {
				// création du complémentaire et rattachement
				OwlClass uneClNeg = this.creatClassOwl(nomNegDe);
				uneClNeg.addUneMere((OwlClass) kbOwlEnCours.lesObjetsOwlClasses
						.get(ComplementaireConc));
				owlCl.setComplementOf(uneClNeg);
			} else {
				// balise complementOf suivi de class
				Element elem = this.getBaliseOwlouRdfs(elemNegDe, "Class");
				if (elem != null) {
					nomNegDe = this.getAttributValeurRdf(elem);
					if (nomNegDe != null) {
						OwlClass uneClNeg = this.creatClassOwl(nomNegDe);
						uneClNeg
								.addUneMere((OwlClass) kbOwlEnCours.lesObjetsOwlClasses
										.get(ComplementaireConc));
						owlCl.setComplementOf(uneClNeg);
					}
				} else {
					// balise complementOf suivi de restriction création d'une
					// propriété
					// cas d'une sous classe définie par une propriété
					// creation d'une classe anonyme
					java.util.List lesRestrictions = this
							.getLesBalisesOwlouRdfs(elemNegDe, "Restriction");

					for (Iterator k = lesRestrictions.iterator(); k.hasNext();) {
						Element elemRestr = (Element) k.next();
						OwlClass uneClNeg = this.traitRestriction(elemRestr);
						uneClNeg
								.addUneMere((OwlClass) kbOwlEnCours.lesObjetsOwlClasses
										.get(ComplementaireConc));
						owlCl.setComplementOf(uneClNeg);
					} // fin for k
				} // fin else
			} // fin else
		} // fin for j
	}

	// traitement des meres de la classe owlCl
	private void traitSubClassOf(Element uneClasse, OwlClass owlCl)
			throws Exception {
		// traitement des mères
		java.util.List lesMeres = this.getLesBalisesOwlouRdfs(uneClasse,
				"subClassOf");

		for (Iterator j = lesMeres.iterator(); j.hasNext();) {

			Element elemSubClass = (Element) j.next();
			String nomMere = this.getAttributValeurRdf(elemSubClass);
			// System.err.println("trace nomMere "+nomMere);
			if (nomMere != null && nomMere.trim().length() != 0) {
				// création de la mère et rttachement
				OwlClass uneClMere = this.creatClassOwl(nomMere);
				owlCl.addUneMere(uneClMere);
			} else {
				// balise subClassOf suivi de class
				Element elem = this.getBaliseOwlouRdfs(elemSubClass, "Class");
				if (elem != null) {
					nomMere = this.getAttributValeurRdf(elem);
					if (nomMere == null) {
						// cas d'une imbrication
						java.util.List listEnf = elem.getChildren();
						if (listEnf.isEmpty()) {
							System.err
									.println("erreur format fichier classe sans nom "
											+ owlCl.getNom());
							throw new Exception(
									"erreur format fichier classe sans nom "
											+ owlCl.getNom());
						}
						for (Iterator i = listEnf.iterator(); i.hasNext();) {
							Element unElem = (Element) i.next();
							String nomBal = unElem.getName();
							if (nomBal.equals(OwlConstants.intersectionOf)) {
								OwlClass uneClInclus = this
										.traitIntersectionOf(unElem);
								nomMere = uneClInclus.getNom();
							}
							if (nomBal.equals(OwlConstants.unionOf)) {
								OwlClass uneClInclus = this
										.traitUnionOf(unElem);
								nomMere = uneClInclus.getNom();
							}
							if (nomBal.equals(OwlConstants.complementOf)) {
								OwlClass uneClInclus = this
										.traitComplementOf(unElem);
								nomMere = uneClInclus.getNom();
							}

						}

					}
					OwlClass uneClMere = this.creatClassOwl(nomMere);
					owlCl.addUneMere(uneClMere);
				}

				// balise subClassOf suivi de restriction création d'une
				// propriété
				// cas d'une sous classe définie par une propriété
				// creation d'une classe anonyme
				java.util.List lesRestrictions = this.getLesBalisesOwlouRdfs(
						elemSubClass, "Restriction");

				for (Iterator k = lesRestrictions.iterator(); k.hasNext();) {
					Element elemRestr = (Element) k.next();
					OwlClass uneClMere = this.traitRestriction(elemRestr);
					owlCl.addUneMere(uneClMere);
				} // fin for k

			} // fin else
		} // fin for j

	} // fin

	private OwlClass creatClassOwl(String nom) {
		// System.out.println("trace creatClassOwl nom "+nom);

		// cas la classe existe déjà
		if (nom.startsWith("#")) {
			nom = nom.substring(1);
		}
		OwlClass uneCl = RSOwl.kbOwlEnCours.qCOwl(nom);
		if (uneCl != null) {
			return uneCl;
		}
		OwlClass uneClInter = new OwlClass();
		// on prend l'attribut s'il existe
		// on suppose rdf:ID ou rdf:about il y a une différence entre les 2
		uneClInter.setNom(nom);
		// kbEnCours.addObjetOwlClass(uneClInter); faait dansle constructeur
		return uneClInter;
	}

	/**
	 * création d'une classe complémentaire de une autre
	 */
	private OwlClass creatClassNegDe(OwlClass uneCl) throws Exception {
		if (uneCl == null) {
			throw new Exception(
					"erreur création d'un complementaire d'une classe inexistante");
		}
		String nomCl = uneCl.getNom();
		String nomNeg = "complementaireDe" + Outils.transNomPremLetMaj(nomCl);
		OwlClass uneClNeg = RSOwl.kbOwlEnCours.getEnsOwlClass(nomNeg);
		if (uneClNeg != null) {
			return uneClNeg;
		}
		uneClNeg = new OwlClass(nomNeg);
		uneClNeg.addUneMere((OwlClass) kbOwlEnCours.lesObjetsOwlClasses
				.get(ComplementaireConc));
		uneClNeg.setComplementOf(uneCl);
		uneCl.setComplementOf(uneClNeg);
		return uneClNeg;
	}

	/**
	 * pour rechercher une balise indifferrement dans l'espace owl ou l'espace
	 * rdf
	 */
	private Element getBaliseOwlouRdf(Element elem, String balise) {
		Element val = elem.getChild(balise, espNomOwl);
		if (val != null) {
			return val;
		}
		val = elem.getChild(balise, espNomRdf);
		return val;
	}

	/**
	 * pour rechercher une balise indifferrement dans l'espace owl ou l'espace
	 * rdfs
	 */
	private Element getBaliseOwlouRdfs(Element elem, String balise) {
		Element val = elem.getChild(balise, espNomOwl);
		if (val != null) {
			return val;
		}
		val = elem.getChild(balise, espNomRdfs);
		if (val == null && espaceNomDefaut != null) {
			val = elem.getChild(balise);
		}
		return val;
	}

	/**
	 * pour rechercher les balises indifferrement dans l'espace owl ou l'espace
	 * rdfs
	 */
	private java.util.List getLesBalisesOwlouRdfs(Element elem,
			String balise) {
		java.util.List ensElem = elem.getChildren(balise, espNomOwl);
		if (!(ensElem.isEmpty())) {
			return ensElem;
		}
		ensElem = elem.getChildren(balise, espNomRdfs);
		if (ensElem.isEmpty() && espaceNomDefaut != null) {
			// il y a un espace par défaut
			ensElem = elem.getChildren(balise);
		}
		return ensElem;
	}

	// recherche de la balise unionOf dans owl ou rdf ou rdfs
	private Element getBalise(Element elemClass, String uneBal) {
		Element elem = elemClass.getChild(uneBal, espNomOwl);
		if (elem == null) {
			elem = elemClass.getChild(uneBal, espNomRdf);
			if (elem == null) {
				elem = elemClass.getChild(uneBal, espNomRdfs);
				if (elem == null && espaceNomDefaut != null) {
					// il y a un espace par défaut
					elem = elemClass.getChild(uneBal);
				}
			}
		}
		return elem;
	}

	// valeur de la valise rdf:ID ou rdf:about ou rdf:resource
	private String getAttributValeurRdf(Element elem) {
		String nom = elem.getAttributeValue(OwlConstants.ID, espNomRdf);
		String nomOnto = "";
		if (nom == null) {
			nom = elem.getAttributeValue(OwlConstants.about, espNomRdf);
		}
		if (nom == null) {
			nom = elem.getAttributeValue(OwlConstants.resource, espNomRdf);
		}
		if (nom != null && nom.startsWith("#")) {
			nom = nom.substring(1);
		}
		if (nom != null && nom.startsWith("http")) {
			int ind = nom.indexOf("#");
			// System.err.println("trace getAttributValeurRdf nom "+nom);
			if (ind != -1) {
				String nomAux = nom;
				nom = nom.substring(ind + 1);
				// recherche de l'ontologie de base
				// utiliser dans CLO
				int indNomOnto = nomAux.indexOf("ontologies");
				if (indNomOnto != -1) {
					int indSep = nomAux.lastIndexOf("/");
					if (indSep != -1) {
						nomOnto = nomAux.substring(indSep + 1, ind);
						// System.err.println("trace "+nomOnto);
					}
				}
			}
		}
		if (nomOnto.equals("") || nom == null) {
			return nom;
		} else {
			return nomOnto + "_" + nom;
		}
	}

	/** ******************************************************************* */
	private OwlClass creatClassAnonym(String nameProperty) {
		String nomCl = "anonyme" + Outils.transNomPremLetMaj(nameProperty);
		String com = "Création d'une classe définie par la propriété "
				+ nameProperty;
		TreeSet<OwlClass> pere = new TreeSet<OwlClass>();
		pere.add(RSOwl.kbOwlEnCours.qCOwl(ConceptAnonyme));
		OwlClass uneClAnonym = new OwlClass(nomCl, pere, null, null, null,
				null, new Vector<OwlClass>(), com, new Vector<String>());
		// RSOwl.kbEnCours.addObjetOwlClass (uneClAnonym);
		return uneClAnonym;
	}

	/**
	 * création d'une classe anonyme définie par une restriction de propriété
	 * renvoie le nom de cette classe ou null balise Restriction - onProperty -
	 * allValueFrom - someValueFrom
	 */

	private void creatClassXMLSchema(String nomClasse) {
		TreeSet<OwlClass> pere = new TreeSet<OwlClass>();
		pere.add(RSOwl.kbOwlEnCours.qCOwl(DataType));
		String com = "Création d'une classe définie définie dans XML schéma ";
		new OwlClass(nomClasse, pere, null, null, null, null,
				new Vector<OwlClass>(), com, new Vector<String>());
		// RSOwl.kbEnCours.addObjetOwlClass(uneClAnonym);
	}

	// private OwlPropertyValeur creatProprieteOwlValeur(String nom){
	// OwlPropertyValeur uneOwlProp = new OwlPropertyValeur(nom);
	// return uneOwlProp;
	// }

	/***************************************************************************
	 * traitement de la balise Restriction renvoie la classe décrite dans la
	 * restriction definie par la propriété
	 */
	public OwlClass traitRestriction(Element elemRestr) throws Exception {
		// cas d'une sous classe définie par une propriété
		// recherche de la balise onProperty
		Element elemOnProperty = elemRestr.getChild("onProperty", espNomOwl);
		if (elemOnProperty == null) {
			System.err
					.println("trace cas à traiter une balise restriction suivie de onProperty");
			return null;
		}
		// nom de la propriété
		String nomProp = this.getAttributValeurRdf(elemOnProperty);
		if (nomProp == null) {
			// cas de l'insertion d'une définition par ObjectProperty
			Element elemOnPropertyObject = elemOnProperty.getChild(
					OwlConstants.objectProperty, espNomOwl);
			if (elemOnPropertyObject != null) {
				nomProp = this.getAttributValeurRdf(elemOnPropertyObject);
			}
			if (nomProp == null) {
				// cas de functionnalProperty
				Element elemFunctionnalObject = elemOnProperty.getChild(
						OwlConstants.functionalProperty, espNomOwl);
				if (elemFunctionnalObject != null) {
					nomProp = this.getAttributValeurRdf(elemFunctionnalObject);
				} else {
					Element elemTransitiveObject = elemOnProperty.getChild(
							OwlConstants.transitiveProperty, espNomOwl);
					if (elemTransitiveObject != null) {
						nomProp = this
								.getAttributValeurRdf(elemTransitiveObject);
					} else {
						Element elemDataTypeProperty = elemOnProperty.getChild(
								OwlConstants.dataProperty, espNomOwl);
						if (elemDataTypeProperty != null) {
							nomProp = this
									.getAttributValeurRdf(elemDataTypeProperty);
						} else {
							System.err
									.println("trace cas à traiter apres OnProperty");
						}
					}
				}
			}
		}

		OwlProperty uneOwlProp = new OwlProperty(nomProp);
		// creation d'une classe anonyme

		OwlClass domProp = this.creatClassAnonym(nomProp);

		uneOwlProp.setDomaine(domProp.getNom());
		// System.err.println("trace traitRestriction "+nomProp+ "nom domProp
		// "+domProp.getNom());
		// traitement de allValueFrom

		Element elemAllValueFrom = elemRestr.getChild(
				OwlConstants.allValuesFrom, espNomOwl);
		List uneListe = elemRestr.getChildren();
		// FenPrinc.traceur.info("taille "+uneListe.size());
		// for(int i= 0; i<uneListe.size();i++){
		// FenPrinc.traceur.info("elemRestr "+uneListe.get(i));
		// }

		if (elemAllValueFrom != null) {
			this.traitQualifOnProperty(elemAllValueFrom, uneOwlProp);
			// ajout de la cardinalité 0, 100
			uneOwlProp.setCardMin("0");
			uneOwlProp.setCardMax("100");
			// tenir compte du nom du range et modifier le nom de la classe
			// anonyme
			String nomDom = domProp.getNom();
			domProp.setNom(nomDom + "#"
					+ Outils.transNomPremLetMaj(uneOwlProp.getRange()));
			OwlClass clAno = this.creatClassAnonym(nomDom + "#"
					+ Outils.transNomPremLetMaj(uneOwlProp.getRange()));

			// on ajoute la valeur du role
			uneOwlProp.setDomaine(nomDom + "#"
					+ Outils.transNomPremLetMaj(uneOwlProp.getRange()));
		}
		// traitement de someValueFrom
		Element elemSomeValueFrom = elemRestr.getChild(
				OwlConstants.someValuesFrom, espNomOwl);
		if (elemSomeValueFrom != null) {
			this.traitQualifOnProperty(elemSomeValueFrom, uneOwlProp);
			// ajout de la cardinalité 0, 100
			uneOwlProp.setCardMin("1");
			uneOwlProp.setCardMax("100");
			// tenir compte du nom du range et modifier le nom de la classe
			// anonyme
			String nomDom = domProp.getNom();
			domProp.setNom(nomDom + "#"
					+ Outils.transNomPremLetMaj(uneOwlProp.getRange()));
			OwlClass clAno = this.creatClassAnonym(nomDom + "#"
					+ Outils.transNomPremLetMaj(uneOwlProp.getRange()));
			// on ajoute la valeur du role
			uneOwlProp.setDomaine(nomDom + "#"
					+ Outils.transNomPremLetMaj(uneOwlProp.getRange()));
		}
		// traitement de hasValue
		Element elemHasValue = elemRestr.getChild("hasValue", espNomOwl);
		if (elemHasValue != null) {
			// création d'une propriété valeur
			// correspond à un rôle individuel
			RSOwl.kbOwlEnCours.removeObjetOwlProperty(uneOwlProp);
			OwlPropertyValeur unePropVal = new OwlPropertyValeur(nomProp);
			String resource = this.getAttributValeurRdf(elemHasValue);
			unePropVal.setRange(resource);
		}
		// traitement de la cardinalité
		// cas present dans shuttle_crew
		// la cardinalité définie comme attribut de l balise restriction
		String card = elemRestr.getAttributeValue("cardinality", espNomOwl);
		if (card != null) {
			uneOwlProp.setCardMax(card);
			uneOwlProp.setCardMin(card);
		}
		// cas conforme au guide

		Element elemCard = elemRestr.getChild("cardinality", espNomOwl);
		if (elemCard != null) {
			card = elemCard.getTextTrim();
			uneOwlProp.setCardMax(card);
			uneOwlProp.setCardMin(card);
		}
		Element elemCardMin = elemRestr.getChild("minCardinality", espNomOwl);
		if (elemCardMin != null) {
			card = elemCardMin.getTextTrim();
			uneOwlProp.setCardMin(card);
		}
		Element elemCardMax = elemRestr.getChild("maxCardinality", espNomOwl);
		if (elemCardMax != null) {
			card = elemCardMax.getTextTrim();
			uneOwlProp.setCardMax(card);
		}
		return domProp;
	}

	/***************************************************************************
	 * traitement commun à allValueFrom et someValueFrom
	 */
	private void traitQualifOnProperty(Element elemQual, OwlProperty uneOwlProp)
			throws Exception {
		// FenPrinc.traceur.info("traitQualifproperty"+elemQual.getChildren()+"
		// OwlProperty "+uneOwlProp);
		// Outils.afficheHierOwl(elemQual);
		// Outils.afficheLesFilsOwl(elemQual);
		// System.err.println("trace traitQualifOnProperty elemntQual
		// "+elemQual);
		String resource = this.getAttributValeurRdf(elemQual);
		if (resource == null) {
			// l'attribut resource n'existe pas, on regarde si il y a une balise
			// Class qui définit la resource cas dolce
			Element elemClass = elemQual.getChild(OwlConstants.classe, espNomOwl);

			if (elemClass != null) {
				resource = this.getAttributValeurRdf(elemClass);
				if (resource == null) {
					// cas des classes complexes
					// System.err.println("trace traitement de classes complexes
					// dans la restriction d'une propriété");
					OwlClass uneCl = this.traitClassesComplexes(elemClass);
					if (uneCl == null) {
						// System.err.println(
						// "erreur cas à traiter traitQualifOnProperty " +
						// elemClass);
						Outils.afficheHierOwl(elemQual);
						Outils.afficheLesFilsOwl(elemClass);
						throw new Exception(
								"erreur cas à traiter traitQualifOnProperty "
										+ elemClass);
					}
					resource = uneCl.getNom();
					uneOwlProp.setRange(resource);
				}
			} else {
				// contient une balise différente
				// cas de la restriction
				Element elemRestriction = elemQual.getChild("Restriction",
						espNomOwl);
				if (elemRestriction == null) {
					throw new Exception(
							"erreur cas à traiter traitQualifOnProperty  balise "
									+ elemQual.getChildren());
				} else {
					// cas de la balise Restriction qui suit allValuesFrom
					OwlClass uneCl = this.traitRestriction(elemRestriction);
					if (uneCl != null) {
						resource = uneCl.getNom();
						uneOwlProp.setRange(resource);
					} else {
						throw new Exception(
								"erreur cas à traiter traitQualifOnProperty pb apres restriction");
					}
				}

			}
		}
		// System.err.println("trace resource "+resource);
		if (resource.startsWith("http://www.w3.org/2001/XMLSchema#")
				|| resource.startsWith("&xsd;")) {
			// cas http;...
			int ind = resource.indexOf(OwlConstants.separateurNom);
			if (ind == -1) {
				ind = resource.indexOf(";");
			}
			// cas donnée XML schéma
			String nomResource = "";
			if (ind != -1) {
				nomResource = resource.substring(ind + 1);
			}
			// cas d'une resource de base XML schema
			// création de la classe
			if (RSOwl.kbOwlEnCours.qc(nomResource) == null) {
				// création de la classe
				this.creatClassXMLSchema(nomResource);
			}
			uneOwlProp.setRange(nomResource);

		} else {
			// cas d'une resource qui est une classe owl
			uneOwlProp.setRange(resource);
		}
	}

	/**
	 * cas des classes complexes
	 */
	public OwlClass traitClassesComplexes(Element elemClass) {
		// Outils.afficheHierOwl(elemClass);
		// pour arrêter la récursion
		if (elemClass == null) {
			return null;
		}
		// FenPrinc.traceur.info("traitClassesComplexes "+elemClass.getName());
		// cas d'une classe définie par son complement
		Element elemComplementOf = elemClass
				.getChild("complementOf", espNomOwl);
		if (elemComplementOf != null) {
			try {
				OwlClass uneClComplementOf = this
						.traitComplementOf(elemComplementOf);
				return uneClComplementOf;
			} catch (Exception e) {
				System.err.println("erreur dans un complementOf " + e);
				return null;
			}
		}
		// cas d'une classe union de plusieurs classes
		Element elemUnionOf = this.getBalise(elemClass, OwlConstants.unionOf);
		if (elemUnionOf != null) {
			// traitement d'une classe unionOf de classes
			try {
				OwlClass uneClUnionOf = this.traitUnionOf(elemUnionOf);
				return uneClUnionOf;
			} catch (Exception e) {

				System.err.println("erreur dans un unionof " + e);
				Outils.afficheHierOwl(elemUnionOf);
				e.printStackTrace();
				return null;
			}
		}
		Element elemIntersectionOf = elemClass.getChild("intersectionOf",
				espNomOwl);
		if (elemIntersectionOf != null) {
			// traitement d'une classe intersectionOf de classes
			try {
				OwlClass uneClIntersectionOf = this
						.traitIntersectionOf(elemIntersectionOf);
				return uneClIntersectionOf;
			} catch (Exception e) {
				System.err.println("erreur dans un intersectionOf " + e);
				// e.printStackTrace();
				return null;
			}
		}
		// cas disjoint with
		Element elemDisjointWith = elemClass
				.getChild("disjointWith", espNomOwl);
		if (elemDisjointWith != null) {
			try {
				OwlClass uneClDisjointWith = this
						.traitDisjointWith(elemDisjointWith);
				return uneClDisjointWith;
			} catch (Exception e) {
				System.err.println("erreur dans un disjointWith " + e);
				// e.printStackTrace();
				return null;
			}
		}
		// cas du oneof
		Element elemOneOf = elemClass.getChild("oneOf", espNomOwl);
		if (elemOneOf != null) {
			try {
				OwlClass uneClOneOf = this.traitOneOf(elemOneOf);
				return uneClOneOf;
			} catch (Exception e) {
				System.err.println("erreur dans un oneOf " + e);
				// e.printStackTrace();
				return null;
			}
		}
		java.util.List<Element> lesFils = elemClass.getChildren();
		for (Iterator<Element> k = lesFils.iterator(); k.hasNext();) {
			System.err.println("trace erreur - cas à traiter " + k.next());
		}

		return null;
	}

	/*
	 * cette méthode sert lorsqu'on définit une classe dans une propriété
	 * elemClass: la definition en XML
	 */
	public OwlClass traitComplementOf(Element elemClass) throws Exception {
		// System.err.println("traitement d'une classe définie par complementOf
		// dans une propriété "+elemClass.getText());
		// recherche de la classe complementaire
		// cas de la classe imbriquée
		Element elemFille = elemClass.getChild("Class", espNomOwl);
		String nomClasseNeg = null;
		if (elemFille != null) {
			nomClasseNeg = this.getAttributValeurRdf(elemFille);
		} else {
			// recherche du nom de la classe complémentaire
			nomClasseNeg = this.getAttributValeurRdf(elemClass);
		}
		OwlClass uneClElem = this.testDansTraitClassesComplexes(nomClasseNeg,
				elemFille);
		nomClasseNeg = uneClElem.getNom();
		// recherche de la classe si elle existe
		OwlClass uneCl = RSOwl.kbOwlEnCours.getEnsOwlClass(nomClasseNeg);
		if (uneCl == null) {
			// création d'une classe complémentaire
			uneCl = new OwlClass(nomClasseNeg);
		}
		OwlClass uneClNeg = this.creatClassNegDe(uneCl);

		return uneClNeg;
	}

	public OwlClass traitUnionOf(Element elemClass) throws Exception {
		// System.err.println("traitement d'une classe définie par unionOf
		// "+elemClass.getName()+" parent "+elemClass.getParent().getName());
		// création d'une classe qui subsume les 2 classes

		java.util.List lesCl = elemClass.getChildren("Class", espNomOwl);
		// création de la classe union
		OwlClass uneCl = new OwlClass();
		String nomUnion = OwlConstants.unionOf;
		Vector<OwlClass> vCl = new Vector<OwlClass>();
		for (Iterator i = lesCl.iterator(); i.hasNext();) {
			Element uneElem = (Element) i.next();
			// création de la classe correspondante à uneElem
			String nomCl = this.getAttributValeurRdf(uneElem);
			OwlClass uneClElem = this.testDansTraitClassesComplexes(nomCl,
					uneElem);
			nomCl = uneClElem.getNom();
			nomUnion = nomUnion + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(nomCl);
			vCl.addElement(uneClElem);
		}// fin for

		uneCl.setNom(nomUnion);
		uneCl.addUneMere((OwlClass) kbOwlEnCours.lesObjetsOwlClasses
				.get(UnionOfConc));
		for (int i = 0; i < vCl.size(); i++) {
			((OwlClass) vCl.elementAt(i)).addUneMere(uneCl);
		}

		return uneCl;
	}

	/**
	 * uneCl est définie comme l'union de classes
	 */
	public void traitUnionOf(Element elemClass, OwlClass uneCl)
			throws Exception {
		// System.err.println("traitement d'une classe définie par unionOf
		// "+elemClass.getText());
		// création d'une classe qui subsume les 2 classes
		Element elemUnion = this.getBalise(elemClass, "unionOf");
		if (elemUnion == null) {
			return;
		}
		java.util.List lesCl = elemUnion.getChildren(OwlConstants.classe, espNomOwl);
		// création de la classe union
		String nomUnion = OwlConstants.unionOf;
		Vector<OwlClass> vCl = new Vector<OwlClass>();
		for (Iterator i = lesCl.iterator(); i.hasNext();) {

			Element uneElem = (Element) i.next();
			// création de la classe correspondante à uneElem
			String nomCl = this.getAttributValeurRdf(uneElem);
			OwlClass uneClElem = this.testDansTraitClassesComplexes(nomCl,
					uneElem);
			nomCl = uneClElem.getNom();
			vCl.addElement(uneClElem);
			nomUnion = nomUnion + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(nomCl);
		}
		// balise unionOf suivi de restriction création d'une propriété
		// cas d'une sous classe définie par une propriété
		// creation d'une classe anonyme
		java.util.List lesRestrictions = this.getLesBalisesOwlouRdfs(elemUnion,
				"Restriction");

		for (Iterator k = lesRestrictions.iterator(); k.hasNext();) {
			Element elemRestr = (Element) k.next();
			OwlClass uneClUnion = this.traitRestriction(elemRestr);
			uneClUnion.addUneMere((OwlClass) kbOwlEnCours.lesObjetsOwlClasses
					.get(UnionOfConc));
			vCl.addElement(uneClUnion);
			nomUnion = nomUnion + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(uneClUnion.getNom());
		} // fin for k

		// System.err.println("trace "+nomUnion);
		uneCl.addComment("\n= " + nomUnion);
		for (int i = 0; i < vCl.size(); i++) {
			((OwlClass) vCl.elementAt(i)).addUneMere(uneCl);
		}

	}

	public OwlClass traitIntersectionOf(Element elemClass) throws Exception {
		// System.err.println("traitement d'une classe définie par
		// intersectionOf "+elemClass.getText());
		// création d'une classe qui est subsumée par les classes
		java.util.List lesCl = elemClass.getChildren("Class",espNomOwl);
		traceur.info("test intersection 1 elemnt");
		// création de la classe intersection
		OwlClass uneCl = new OwlClass();
		String nomIntersection = OwlConstants.intersectionOf;
		Vector<OwlClass> vCl = new Vector<OwlClass>();
		for (Iterator i = lesCl.iterator(); i.hasNext();) {
			Element uneElem = (Element) i.next();
			// création de la classe correspondante à uneElem
			String nomCl = this.getAttributValeurRdf(uneElem);
			traceur.info(nomCl);
			OwlClass uneClElem = this.testDansTraitClassesComplexes(nomCl,
					uneElem);
			nomCl = uneClElem.getNom();
			traceur.info(nomCl);
			vCl.addElement(uneClElem);
			nomIntersection = nomIntersection + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(nomCl);
			traceur.info("nomIntersection " + nomIntersection);
		}
		// balise intersectionOf suivi de restriction création d'une propriété
		// cas d'une sous classe définie par une propriété
		// creation d'une classe anonyme
		java.util.List lesRestrictions = this.getLesBalisesOwlouRdfs(
				elemClass, "Restriction");

		for (Iterator k = lesRestrictions.iterator(); k.hasNext();) {
			Element elemRestr = (Element) k.next();
			OwlClass uneClInter = this.traitRestriction(elemRestr);
			// uneClInter.addUneMere((OwlClass)kbOwlEnCours.lesObjetsOwlClasses.get(IntersectionOfConc));
			vCl.addElement(uneClInter);
			nomIntersection = nomIntersection + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(uneClInter.getNom());
			traceur.info("restriction " + nomIntersection);
		} // fin for k
		uneCl.setNom(nomIntersection);
		// uneCl.addUneMere((OwlClass)kbOwlEnCours.lesObjetsOwlClasses.get(IntersectionOfConc));
		for (int i = 0; i < vCl.size(); i++) {
			uneCl.addUneMere(((OwlClass) vCl.elementAt(i)));
		}

		return uneCl;
	}

	/**
	 * uneCl est définie comme l'intersection de classes
	 */
	public void traitIntersectionOf(Element elemClass, OwlClass uneCl)
			throws Exception {
		// System.err.println("traitement d'une classe définie par
		// intersectionof "+elemClass.getText());
		// création d'une classe qui subsume les 2 classes
		Element elemIntersection = this.getBalise(elemClass, "intersectionOf");
		if (elemIntersection == null) {
			return;
		}

		java.util.List lesCl = elemIntersection.getChildren("Class",
				espNomOwl);
		// création de la classe intersection
		String nomIntersection = "intersectionOf";
		Vector<OwlClass> vCl = new Vector<OwlClass>();
		for (Iterator i = lesCl.iterator(); i.hasNext();) {
			Element uneElem = (Element) i.next();
			// création de la classe correspondante à uneElem
			String nomCl = this.getAttributValeurRdf(uneElem);
			OwlClass uneClElem = this.testDansTraitClassesComplexes(nomCl,
					uneElem);
			nomCl = uneClElem.getNom();
			vCl.addElement(uneClElem);
			nomIntersection = nomIntersection + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(nomCl);
		}
		// balise intersectionOf suivi de restriction création d'une propriété
		// cas d'une sous classe définie par une propriété
		// creation d'une classe anonyme
		java.util.List lesRestrictions = this.getLesBalisesOwlouRdfs(
				elemIntersection, "Restriction");

		for (Iterator k = lesRestrictions.iterator(); k.hasNext();) {
			Element elemRestr = (Element) k.next();
			OwlClass uneClInter = this.traitRestriction(elemRestr);
			// uneClInter.addUneMere((OwlClass)kbOwlEnCours.lesObjetsOwlClasses.get(IntersectionOfConc));
			vCl.addElement(uneClInter);
			nomIntersection = nomIntersection + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(uneClInter.getNom());
			traceur.info("restriction " + nomIntersection);
		} // fin for k

		// System.err.println("trace "+nomUnion);
		uneCl.addComment("\n= " + nomIntersection);
		for (int i = 0; i < vCl.size(); i++) {
			uneCl.addUneMere(((OwlClass) vCl.elementAt(i)));
		}

	}

	/**
	 * uneCl est définie comme disjointe de la classe définie dans elemClass
	 */
	public void traitDisjointWith(Element elemClass, OwlClass uneCl)
			throws Exception {
		// System.err.println("traitement d'une classe définie par disjoint
		// "+elemClass.getText());
		// création d'une classe complementaire de uneCl
		// exemple vegetable disjoint de sweetFruit
		// création d'un complementaire de sweetfruit
		// et vegetable fils de ce complémentaire
		java.util.List listDisjoint = this.getLesBalisesOwlouRdfs(elemClass,
				"disjointWith");
		if (listDisjoint == null || listDisjoint.isEmpty()) {
			return;
		}
		// création du complémentaire de uneCl
		String nomCl = uneCl.getNom();
		OwlClass uneClNeg = this.creatClassNegDe(uneCl);

		// création de la classe disjoint
		String nomDisjoint = "disjointWith";
		Vector<OwlClass> vCl = new Vector<OwlClass>();
		for (Iterator i = listDisjoint.iterator(); i.hasNext();) {
			Element elemDisjoint = (Element) i.next();
			String nomClDisjoint = this.getAttributValeurRdf(elemDisjoint);
			OwlClass uneClElem = this.testDansTraitClassesComplexes(
					nomClDisjoint, elemDisjoint);
			nomClDisjoint = uneClElem.getNom();

			vCl.addElement(uneClElem);
			nomDisjoint = nomDisjoint + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(nomClDisjoint);
		}
		// balise disjointDe suivi de restriction création d'une propriété
		// cas d'une sous classe définie par une propriété
		// creation d'une classe anonyme
		java.util.List lesRestrictions = this.getLesBalisesOwlouRdfs(elemClass,
				"Restriction");

		for (Iterator k = lesRestrictions.iterator(); k.hasNext();) {
			Element elemRestr = (Element) k.next();
			OwlClass uneClInter = this.traitRestriction(elemRestr);
			vCl.addElement(uneClInter);
			nomDisjoint = nomDisjoint + OwlConstants.separateurNom
					+ Outils.transNomPremLetMaj(uneClInter.getNom());
		} // fin for k

		// System.err.println("trace "+nomUnion);
		uneCl.addComment("= " + nomDisjoint);
		for (int i = 0; i < vCl.size(); i++) {
			((OwlClass) vCl.elementAt(i)).addUneMere(uneClNeg);
		}

	}

	public OwlClass traitDisjointWith(Element elemClass) throws Exception {
		// System.err.println("traitement d'une classe définie par disjoint dans
		// unpropriété");
		System.out
				.println("cas à traiter - disjointWith défini dans une propriété");
		return new OwlClass();
	}

	/***************************************************************************
	 * recherche d'une balise classe à la suite d'une balise unionOf,
	 * intersectionOf, disjointWith, complementOf
	 **************************************************************************/
	private Element rechercheClassDansBaliseComplexe(Element elem) {
		Element elemCl = elem.getChild("Class", espNomOwl);
		if (elemCl == null) {
			elemCl = elem.getChild("Class", espNomRdfs);
			if (elemCl == null) {
				elemCl = elem.getChild("Class", espNomRdf);
			}
		}
		return elemCl;
	}

	public void creatIndividus(Element elemRoot) {
		// à traiter
		// un individu est défini soit par aucun élément
		// <WineGrape rdf:ID="CabernetSauvignonGrape>
		// ou par rdf:type
		/*
		 * <owl:Thing rdf:ID="CentralCoastRegion" /> ou <owl:Thing
		 * rdf:about="#CentralCoastRegion" > <rdf:type rdf:resource="#Region"/>
		 * </owl:Thing>
		 */
		java.util.List ensListInd = this.getLesBalisesOwlouRdfs(elemRoot,
				"Thing");
		for (Iterator i = ensListInd.iterator(); i.hasNext();) {
			Element unInd = (Element) i.next();
			String nomInd = this.getAttributValeurRdf(unInd);
			// recherche de la classe utilisation de rdf:type
			Element classeInd = unInd.getChild("type", OwlConstants.espNomRdf);
			if(classeInd != null){
			String nomCl = this.getAttributValeurRdf(classeInd);
			// System.err.println("trace nomCl "+nomCl);
			// recherche de la classe owl correspondante
			OwlClass uneCl = RSOwl.kbOwlEnCours.qCOwl(nomCl);
			// ajout de l'individu
			if (uneCl == null) {
				JOptionPane.showMessageDialog(null,
						"Pb dans le fichier owl, la classe " + nomCl
								+ " n'est pas définie");
			} else {
				uneCl.addUnInd(nomInd);
			}
			}

		}
		// cas 2 sans aucun élément
		ensListInd = elemRoot.getChildren();

		for (Iterator i = ensListInd.iterator(); i.hasNext();) {
			Element unElement = (Element) i.next();

			if (espaceNomDefaut == null
					&& unElement.getNamespace().getPrefix().equals("")) {
				// on suppose que c'est un individu - pas de namespace
				String nomCl = unElement.getName(); // ??? a tester
				// recherche de la classe utilisation de rdf:type
				String nomInd = this.getAttributValeurRdf(unElement);
				// recheche de la classe owl correspondante
				OwlClass uneCl = RSOwl.kbOwlEnCours.qCOwl(nomCl);
				// ajout de l'individu
				if (uneCl == null) {
					JOptionPane.showMessageDialog(null,
							"Pb dans le fichier owl, la classe " + nomCl
									+ " n'est pas définie");
				} else {
					uneCl.addUnInd(nomInd);
				}
			}
		}
	}

	/**
	 * ens de tests utilisés dans le traitement de balises imbriquées
	 */
	private OwlClass testDansTraitClassesComplexes(String nomCl, Element uneElem)
			throws Exception {
		OwlClass uneClElem = null;
		if (nomCl == null) {
			// System.err.println(" trace nomcl null ");
			Element elem = this.rechercheClassDansBaliseComplexe(uneElem);
			if (elem == null) {
				// System.err.println(" trace elem null");
				// cas de balises imbriquées d'union et d'intersections et autre
				uneClElem = this.traitClassesComplexes(uneElem);
				if (uneClElem == null) {
					Outils.afficheHierOwl(uneElem);
					throw new Exception(
							"trace cas à traiter RSOwl - trait complexes  "
									+ uneElem);
				} else {
					nomCl = uneClElem.getNom();
				}

				// System.err.println("trace cas à traiter RSOwl - unionOf 1
				// para ");
			}

			else {
				nomCl = this.getAttributValeurRdf(elem);
				uneClElem = this.creatClassOwl(nomCl);
			}
		} else {
			uneClElem = this.creatClassOwl(nomCl);
		}
		return uneClElem;

	}

	public OwlClass traitEquivalentClass(Element elemClass, OwlClass uneOwlCl)
			throws Exception {
		Element elemEquivalentClass = this.getBalise(elemClass,
				OwlConstants.equivalentClass);
		if (elemEquivalentClass == null) {
			return null;
		}

		// Outils.afficheHierOwl(elemEquivalentClass);
		// Outils.afficheLesFilsOwl(elemEquivalentClass);
		Element elemFille = elemEquivalentClass.getChild("Class", espNomOwl);
		traceur.info(elemFille.getName());
		// elemFille balise class
		// on cherche son nom
		String nomClasseEquiv = null;
		if (elemFille != null) {
			nomClasseEquiv = this.getAttributValeurRdf(elemFille);
			if (nomClasseEquiv == null) {
				OwlClass uneCl = this.traitClassesComplexes(elemFille);
				uneCl.setEquivalentOf(uneOwlCl);
				uneOwlCl.setEquivalentOf(uneCl);
				if (uneCl == null) {
					throw new Exception(
							"pb dans la recherche du nom d'une classe équivalente");
				}
				return uneCl;
			}
		} else {
			throw new Exception(
					"pb dans la recherche du nom d'une classe équivalente - cas pas de balise imbriquée class");
		}
		// recherche de la classe si elle existe
		OwlClass uneCl = RSOwl.kbOwlEnCours.getEnsOwlClass(nomClasseEquiv);
		if (uneCl == null) {
			// création d'une classe equivalente
			uneCl = new OwlClass(nomClasseEquiv);
			uneCl.setEquivalentOf(uneOwlCl);
			uneOwlCl.setEquivalentOf(uneCl);
		}

		return uneCl;
		/*
		 * System.err.println("trait equivalentClass");
		 * Outils.afficheHierOwl(elemEquivalentClass);
		 * Outils.afficheLesFilsOwl(elemEquivalentClass);
		 */

	}

	public static void initVarGlobales() {
		sauvPereFils = new Vector(); // sert pour définir les relations
		// pere-fils entre classes
		sauvProp = new Vector(); // sert pour définir les sous propriétés
		ecritOwl = new TreeSet<String>();
		espaceNomDefaut = null; // pour repérer l'espace de nom par défaut
		sauvPropMereFille = new Vector<String>();
		RSOwl.sauvLesNomsRoles = new Vector<String>();
		kbOwlEnCours = null;
	}

	private void jbInit() throws Exception {
	}

} // fin classe
