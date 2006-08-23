package arkeotek.io.exporter.OWL;


/**
 *  classe contenant les variables globales du logiciel
 * @author Sylvie Szulman
 *   @version 4.0
 *    @date novembre 2000
 */
import java.util.*;
import java.io.*;

import javax.swing.*;

import java.text.*; // pour DateFormat

// pour le fichier XML
import org.jdom.*;
import org.jdom.output.*;
import org.jdom.input.*;
import java.util.regex.*;

public class VarGlobales {

	// variables nécessaires pour l'internationalisation et les chaînes de
	// caractères
	public static ResourceBundle res = ResourceBundle
			.getBundle("fenetre.ResFenetre"); // le fichier de ressource du
												// paquet fenetre

	public static ResourceBundle resTerm = ResourceBundle
			.getBundle("terminae.ResTerminae"); // le fichier de ressource du
												// paquet terminae

	public static ResourceBundle resRechMotifs = ResourceBundle
			.getBundle("rechMotifs.ResRechMotifs"); // le fichier de ressource
													// du paquet RechMotifs

	public static ResourceBundle resLinguae = ResourceBundle
			.getBundle("linguae.ResLinguae"); // le fichier de ressource du
												// paquet Linguae
/*
 * le fichier de ressource de Yatea
 */
	public static ResourceBundle resLexter = ResourceBundle.getBundle("lexter.ResLexter");
	public static ResourceBundle resYatea = ResourceBundle.getBundle("yatea.ResYatea");

	public static ResourceBundle resUtilss = ResourceBundle.getBundle("utilss.ResUtilss");

	// public static ResourceBundle resMFD =
	// ResourceBundle.getBundle("MFD.ResMFD");
	public static String Langage = resTerm.getString("fran_ais"); // pour
																	// modifier
																	// la langue

	// répertoires créés automatiquement
	public static String RepertoireTerminae = null; // répertoire d'accueil du
													// logiciel

	public static String RepertoireSysteme = null; // doit contenir les données
													// systeme
    
	public static String RepertoireFichiersLexter = null; // doit contenir le
															// répertoire
															// d'acces au
															// fichier Lexter
    public static String RepertoireYatea = null; // contient les fichiers
	                                                      // contenant le résultat de l'extraction
	                                                      // des termes par l'outil de sophie et thierry
	public static String RepertoireFichesTerminologique = null; // doit contenir
																// le répertoire
																// d'acces aux
																// fiches

	public static String RepertoireFichesModelisation = null; // doit contenir
																// le répertoire
																// d'acces aux
																// fiches

	public static String RepertoireCorpus = null; // doit contenir le
													// répertoire d'acces au
													// corpus etiquette

	public static String RepertoireCandidatsTermes = null; // doit contenir le
															// répertoire
															// d'acces au corpus
															// séquencé

	public static String RepertoireOntologies = null; // doit contenir le
														// répertoire d'acces
														// aux ontologies
	// public static String RepertoireReseauxConceptuels = null;

	public static String RepertoireLinguae = null;

	public static String RepertoireAide = resTerm.getString("aide"); // répertoire
																		// dans
																		// lequel
																		// se
																		// trouve
																		// les
																		// aides

	public static String RepertoireAppli = null; // répertoire de
													// l'application en cours

	public static String nomAppliEnCours = null; // nom de l'application en
													// cours

//	public static String RepertoireMFD = null;
	

	// la terminologie portera le même nom
	public static int numReseauConceptuelAppliEnCours = 0; // contient le
															// numéro du réseau
															// conceptuel lié à
															// la terminologie
															// de l'application

	// nom de fichiers contenant des variables globales du système
	public static String nomFichierTermeFiche = "tableTermeFiches.objja"; // contient
																			// une
																			// table
																			// terme-fichier
																			// fiche
																			// terminologique

	public static String nomFichierTermeFicheXml = "tableTermeFiches.xml"; // contient
																			// une
																			// table
																			// terme-fichier
																			// fiche
																			// terminologique
																			// en
																			// Xml

	public static String nomFichierListeConceptsTerm = "listeConcepts.objja"; // contient
																				// une
																				// table
																				// avec
																				// les
																				// concepts
																				// terminologiques

	public static String nomFichierListeConceptsTermXml = "listeConcepts.xml"; // contient
																				// une
																				// table
																				// avec
																				// les
																				// concepts
																				// terminologiques
																				// en
																				// Xml

	public static String nomFichierModelisationFiche = "tableModelisationFiches.objja"; // contient
																						// une
																						// table
																						// avec
																						// les
																						// fiches
																						// de
																						// modélisation
  /*
   *  nom du fichier contenant la dernière table des ct en cours de validation
   *  par Lexter
   */
	public static String nomFichierTableValidationCT = null; 
	/*
	 *  nom du fichier contenant la dernière table des ct en cours de validation
	 *  par Syntex
	 */

	public static String nomFichierTableValidationSyntex = null; 
	/*
	 *  nom du fichier contenant la dernière table des ct en cours de validation
	 *  par Yatea
	 */
	public static String nomFichierTableValidationYatea = null;
	
	public static String nomFichierEnsCTValidéOccu = null; // nom du fichier
															// contenant la
															// table des CT
															// validés et de
															// leurs occurrences
															// à partir des
															// fichiers Lexter

	public static String nomFichierEnsCTValidéOccuSyntex = null; // nom du
																	// fichier
																	// contenant
																	// la table
																	// des CT
																	// validés
																	// et de
																	// leurs
																	// occurrences
																	// à partir
																	// des
																	// fichiers
																	// Syntex

	public static String nomFichierRelationsLexSynt = "ensRelationlexSynt.objja"; // nom
																					// du
																					// fichier
																					// contenant
																					// les
																					// relations
																					// lexicoSynt

	public static String nomFichierRelationsLexSyntXml = "ensRelationlexSynt.xml"; // nom
																					// du
																					// fichier
																					// contenant
																					// les
																					// relations
																					// lexicoSynt
																					// en
																					// xml

	public static String nomFichierRelationsSemantiquesXml = "ensRelationsSem.xml"; // nom
																					// du
																					// fichier
																					// contenant
																					// l'ensemble
																					// des
																					// relations
																					// sémantiques

	public static String nomFichierObjetEnCoursValidation = "ensObjetsEnCoursValid.txt"; // nom
																							// du
																							// fichier
																							// contenant
																							// l'ensemble
																							// des
																							// objets
																							// en
																							// cours
																							// de
																							// validation

	public static String nomFichierTermesAjoutes = "termesAjoutes.txt";

	public static boolean modifEnsRelationsLexSynt = false; // pour savoir s'il
															// faut sauvegarder
															// l'ensemble des
															// relations LexSynt

	public static boolean modifEnsRelationsSem = false; // pour savoir s'il faut
														// sauvegarder
														// l'ensemble des
														// relations semantiques

	public static String Corpus = null;

	public static String CorpusCordialise = null; // le corpus étiquetté par
													// Cordial

	public static String CorpusTreeTagger = null; // le corpus étiquetté par
													// TreeTagger

	public static String langueCorpus = null;

	public static String CorpusSequence = null; // le corpus séquencé par Lexter
												// htl.seq

	public static String CorpusSequenceSyntex = null; // le corpus séquencé
														// par Syntex syntex-seq

	public static String CorpusSynoTerm = null; // le corpus donné par Synoterm
	public static String CorpusYatea = null; //le corpus donné par Yatea

	public static Table tableSeq = null; // table obtenue à partir du fichier
											// corpus séquencé par Lexter

	public static Table tableSeqSyntex = null; // table obtenue à partir du
												// fichier corpus séquencé par
												// syntex

	public static TreeMap<String, ElementCTOccu> ensCTOccu = new TreeMap<String, ElementCTOccu>(); // ensemble
																									// des
																									// CT
																									// validés
																									// à
																									// partir
																									// de
																									// Lexter
																									// et
																									// de
																									// leurs
																									// occurrences
																									// dans
																									// le
																									// texte
/*
 * ensemble des CT validés à partir de Syntex et de leurs occurrences dans le texte 
 */
	public static TreeMap<Integer, ElemCTOccuNum> ensCTOccuSyntex = new TreeMap<Integer, ElemCTOccuNum>(); 
//	table contenant un ID, le terme candidat correspondant
	public static TreeMap<String,TermCandidate> ensCTYatea = new TreeMap<String,TermCandidate>(); 
	
//table contenant un lemme, les ID correspondants
	public static TreeMap<String,ArrayList<String>> ensLemmaIDsYatea = new TreeMap<String,ArrayList<String>>();
	public static TreeSet<RelationLexSynt> ensRelationsLexSynt = new TreeSet<RelationLexSynt>(); // ensemble
																									// des
																									// relations
																									// lexico-synt

	public static TreeSet<RelationSem> ensRelationsSem = new TreeSet<RelationSem>(); // ensemble
																						// des
																						// relations
																						// sémantiques
																						// (prédéfinies)

	public static Table tableObjetEnCoursValid = null; // table qui contient
														// l'ensemble des objets
														// en cours de
														// validation

	// Type de l'objet -identifiant de l'objet

	public static SynoTerm instSynoTerm = null; // pointe sur une instance de
												// synoterm initialisée dans la
												// méthode synoterm() de
												// FenPrinc
	/*
	 * pointe sur une instance de Yatea initialisée dans la méthode 
	 */
	//public  static Yatea instYatea = null;

	public static String FileSeparateur = null;

	public static Icon valider = new ImageIcon(FenPrinc.class
			.getResource("/image/ok.gif"));

	public static FenPrinc instanceFenPrinc = null;

	public static String commande = null;

	public static File fileTerminae = null;

	public static Vector<String> ensRubriquesLexicales = new Vector<String>(); // gestion
																				// des
																				// rubriques
																				// lexicales

	// initialisation des variables globales au chargement à partir du fichier
	// de config si il existe

	// variables globales urilisées par les fiches terminologiques
	// à un terme peut correspondre plusieurs concepts terminologiques
	// à un concept terminologiques peut correspondre plusieurs termes
	public static TreeSet<ConceptTerminologique> ensConceptsTerminologiques = new TreeSet<ConceptTerminologique>(); // contient
																													// une
																													// liste
																													// non
																													// structurée
																													// des
																													// concepts
																													// céées
																													// ordonnée

	private static boolean lexterSynt = false; // si vrai on travaille à partir
												// de fichiers lexter si faux à
												// partir de fichiers Syntex

	public static String auteur = null;

	public static String etiqueteur = null;
	public static String OutilExtracteurCT = null;
	
	

	/**
	 * @return
	 */
	public static boolean creatApplication() {
		nomAppliEnCours = (String) JOptionPane.showInputDialog(null, resTerm
				.getString("Nom_de_votre"), resTerm.getString("Entr_e"),
				JOptionPane.INFORMATION_MESSAGE, null, null, "application1");
		if (nomAppliEnCours == null) {
			return false;
		}

		if (nomAppliEnCours.length() == 0) {
			nomAppliEnCours = "application1";
		}
		// création d'un répertoire de nom rep
		// dans le répertoire où est installé le logiciel
		// modification dec 2002
		// try {
		if (RepertoireAppli != null) {
			int rep = JOptionPane.showConfirmDialog(null, VarGlobales.resTerm
					.getString("int_rep_appli")
					+ " " + RepertoireAppli);
			if (rep == JOptionPane.CANCEL_OPTION) {
				return false;
			}
			if (rep == JOptionPane.YES_OPTION) {
				// création du répertoire de l'appli
				JFileChooser fj = new JFileChooser(RepertoireAppli);
				fj.setDialogTitle(resTerm.getString("Indiquer_"));
				fj.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fj.showDialog(null, "OK");
				if (fj.getSelectedFile() == null) {
					return false;
				}
			} else {
				JFileChooser fj = new JFileChooser(RepertoireTerminae);
				fj.setDialogTitle(resTerm.getString("Indiquer_"));
				fj.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fj.showDialog(null, "OK");
				if (fj.getSelectedFile() == null) {
					return false;
				}
				RepertoireAppli = fj.getSelectedFile().getPath();
			}
		} else {
			JFileChooser fj = new JFileChooser(RepertoireTerminae);
			fj.setDialogTitle(resTerm.getString("Indiquer_"));
			fj.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fj.showDialog(null, "OK");
			if (fj.getSelectedFile() == null) {
				return false;
			}
			RepertoireAppli = fj.getSelectedFile().getPath();
		}
		File fileAppli = new File(RepertoireAppli, nomAppliEnCours);
		if (fileAppli.exists()) {
			int rep = JOptionPane.showConfirmDialog(null, resTerm
					.getString("Il_existe_d_j_un_r"));
			if (rep == JOptionPane.NO_OPTION) {
				return false;
			} else {
				rep = JOptionPane
						.showConfirmDialog(null, VarGlobales.res
								.getString("Voulez-vous_conserver"),
								JOptionPane.MESSAGE_PROPERTY,
								JOptionPane.YES_NO_OPTION);
				if (rep == JOptionPane.NO_OPTION) {
					// création du répertoire
					fileAppli.mkdir();
				}
			}
		} else {
			fileAppli.mkdir();
		}

		File mkSysteme = new File(fileAppli, "systeme");
		mkSysteme.mkdir(); // répertoire système qui ocntien les fichiers
							// système de l'appli

		File mkCorpus = new File(fileAppli, "corpus");
		mkCorpus.mkdir(); // répertoire qui contient les corpus

		File mkFichiersLexter = new File(fileAppli, "fichiersLexter");
		mkFichiersLexter.mkdir(); // répertoire qui contient les fichiers
									// Lexter
		File mkFichiersLipnExtrac = new File(fileAppli, "Yatea");
		mkFichiersLipnExtrac.mkdir(); // répertoire qui contient les fichiers
									// de l'extracteur LIPN

		File mkLinguae = new File(fileAppli, "linguae");
		mkLinguae.mkdir(); // répertoire qui contient les motifs et les
							// résultats de motifs

		File mkRepCandidatsTermes = new File(fileAppli, "repCandidatsTermes");
		mkRepCandidatsTermes.mkdir(); // répertoire qui contient les listes de
										// candidats termes en cours de
										// validation

		File mkFichesTerminologique = new File(fileAppli,
				"fichesTerminologique");
		mkFichesTerminologique.mkdir(); // répertoire qui contient les fiches
										// terminologiques

		File mkFichesModelisation = new File(fileAppli, "fichesModelisation");
		mkFichesModelisation.mkdir(); // répertoire qui contient les fiches de
										// modélisation

		// File mkReseauxConceptuels =new File(fileAppli,"reseauxConceptuels" );
		// mkReseauxConceptuels.mkdir();//répertoire qui contient les réseaux
		// conceptuel

		File mkOntologies = new File(fileAppli, "ontologies");
		mkOntologies.mkdir();

		// File mkMFD = new File(fileAppli,"MFD");
		// mkMFD.mkdir();
		// création d'une table des objets en cours de validation
		creatTableObjetEnCoursValidation();
		// réinitialisation des différentes varaibles globales
		VarGlobales.initVarGlobalesAppli();
		VarGlobales.creationFichierConfigGlobales(VarGlobales.fileTerminae);
		return true;
	}

	public static void definitionRepertoires(String nomAppli) {
		RepertoireSysteme = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "systeme"; // doit contenir les données
												// systeme
		RepertoireFichiersLexter = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "fichiersLexter"; // doit contenir le
														// répertoire d'acces au
														// fichier Lexter
		RepertoireYatea = RepertoireAppli + FileSeparateur + nomAppli
		+ FileSeparateur + "Yatea"; // doit contenir le
												// répertoire d'acces au
												// fichier Yatea
		
		RepertoireFichesTerminologique = RepertoireAppli + FileSeparateur
				+ nomAppli + FileSeparateur + "fichesTerminologique"; // doit
																		// contenir
																		// le
																		// répertoire
																		// d'acces
																		// aux
																		// fiches
		RepertoireFichesModelisation = RepertoireAppli + FileSeparateur
				+ nomAppli + FileSeparateur + "fichesModelisation"; // doit
																	// contenir
																	// le
																	// répertoire
																	// d'acces
																	// aux
																	// fiches
		RepertoireCorpus = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "corpus"; // doit contenir le répertoire
												// d'acces au corpus etiquette
		RepertoireCandidatsTermes = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "repCandidatsTermes"; // doit contenir le
															// répertoire
															// d'acces au corpus
															// séquencé
		RepertoireOntologies = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "ontologies"; // doit contenir le
													// répertoire d'acces aux
													// ontologies
		// RepertoireReseauxConceptuels =
		// RepertoireAppli+FileSeparateur+nomAppli+FileSeparateur+"reseauxConceptuels";
		RepertoireLinguae = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "linguae"; // le répertoire terminae
/*		RepertoireMFD = RepertoireAppli + FileSeparateur + nomAppli
				+ FileSeparateur + "MFD";*/

	}

	// création du fichier .Terminae contient les différentes applications
	// créées
	public static void creationFichierConfigGlobales(File file) {
		// création du fichier .Terminae dans le répertoire où est situé le
		// logiciel
		try {
			FileOutputStream f = new FileOutputStream(file);
			PrintWriter p = new PrintWriter(f);
			p.println(nomAppliEnCours);

			p.println(RepertoireAppli); // sauvegarde du répertoire de l'appli
										// en cours
			p.close();
			f.close();
			// System.err.println(" trace fin ");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, resTerm
					.getString("erreur_lors_de_la_cr")
					+ e, resTerm.getString("erreur"), JOptionPane.OK_OPTION);
		}

	}

	/**
	 * pour réinitialiser les variables globales lorsqu'on créée une nouvelle
	 * appli
	 */
	public static void initVarGlobalesAppli() {

		numReseauConceptuelAppliEnCours = 0;
		CorpusCordialise = null;
		CorpusTreeTagger = null;
		CorpusSequence = null;
		CorpusSequenceSyntex = null;
		Corpus = null;
		CorpusSynoTerm = null;
		CorpusYatea =null;
		ensRubriquesLexicales = new Vector<String>();
		nomFichierTableValidationCT = null;
		nomFichierEnsCTValidéOccu = null;
		nomFichierEnsCTValidéOccuSyntex = null;
		nomFichierTableValidationSyntex = null;

		modifEnsRelationsLexSynt = false;
		modifEnsRelationsSem = false;
		lexterSynt = false; // initialisation sur Lexter

		tableSeq = null;
		tableSeqSyntex = null;
		ensCTOccu = new TreeMap<String, ElementCTOccu>();
		ensCTOccuSyntex = new TreeMap<Integer, ElemCTOccuNum>();
		ensCTYatea = new TreeMap<String,TermCandidate>();
		ensRelationsLexSynt = new TreeSet<RelationLexSynt>();
		ensRelationsSem = new TreeSet<RelationSem>();
		nomFichierTableValidationCT = null;
		GestionFiches.setTermeFiche(new Hashtable<String,String>());
		GestionFiches.ConceptFiche = new Hashtable<String,String>();
		if (tableObjetEnCoursValid != null) {
			Table.suppression(tableObjetEnCoursValid);
		}
		tableObjetEnCoursValid = null;
		FenTermLexter.initVarGlobales();
		ensConceptsTerminologiques = new TreeSet<ConceptTerminologique>();

	}

/*	public static boolean getLexterSynt() {
		return lexterSynt;
	}

	public static void setLexterSynt(boolean b) {
		lexterSynt = b;
	}
*/
	public static void lecNomAppliRepAppli() {
		try {
			String nomFile = new String(".Terminae");
			File file = new File(RepertoireTerminae, nomFile);
			FileReader fis = new FileReader(file);

			BufferedReader in = new BufferedReader(fis);
			nomAppliEnCours = in.readLine();

			// System.err.println("trace lecNomAppliRepAppli "+nomAppliEnCours);
			// lecture du repertoire de l'appli si il existe dec 02
			RepertoireAppli = in.readLine();
			if (RepertoireAppli == null) {
				RepertoireAppli = RepertoireTerminae;
			}

			in.close();
			// test que l'appli existe bien
			File fAppli = new File(RepertoireAppli, VarGlobales.nomAppliEnCours);
			if (!fAppli.exists()) {
				// le fichier Terminae est corrompu
				JOptionPane.showMessageDialog(null, resTerm
						.getString("fichier_.Terminae")
						+ " "
						+ fAppli.getName()
						+ resTerm.getString("nexiste_pas"));
				int rep = JOptionPane.showConfirmDialog(null, resTerm
						.getString("nouvelle_appli"), "Question",
						JOptionPane.YES_NO_OPTION);
				if (rep == JOptionPane.YES_OPTION) {
					creatApplication();
				} else {
					changeAppli();
				}

			} else {
				VarGlobales.definitionRepertoires(VarGlobales.nomAppliEnCours);
			}
		} catch (IOException execpt) {
			System.err.println(resTerm.getString("execution_impossible")
					+ execpt);
		}
	}

	public static void changeAppli() {
		String path;
		// recherche des fichiers .xxx
		int rep = JOptionPane.showConfirmDialog(null, VarGlobales.resTerm
				.getString("int_rep_appli")
				+ " " + VarGlobales.RepertoireAppli);
		if (rep == JOptionPane.CANCEL_OPTION) {
			return;
		}
		if (rep == JOptionPane.NO_OPTION) {
			Vector<String> vPath = new Vector<String>();
			new FenDialogPath(null, vPath);
			if (vPath.isEmpty()) {
				return;
			}
			path = (String) vPath.elementAt(0);
			VarGlobales.RepertoireAppli = path;
			// System.err.println("trace path "+path);
		} else {
			path = VarGlobales.RepertoireAppli;
		}
		if (path == null || path.trim().length() == 0) {
			return;
		}
		File dirTerm = new File(path);
		// System.err.println("trace irTerm "+dirTerm);
		if (dirTerm == null || !(dirTerm.exists())) {
			return;
		}
		java.util.regex.Pattern patAnc = java.util.regex.Pattern
				.compile("^.*.cfg");
		java.util.regex.Pattern patNew = java.util.regex.Pattern
				.compile("^.*.xcfg");
		String[] ensFich = dirTerm.list();
		// System.err.println("trace "+ensFich.length);
		if (ensFich.length == 0) {
			JOptionPane.showMessageDialog(null, VarGlobales.res
					.getString("aucun_"));
			return;
		}
		TreeSet<String> vFich = new TreeSet<String>();
		for (int i = 0; i < ensFich.length; i++) {
			Matcher matchAnc = patAnc.matcher(ensFich[i]);
			Matcher matchNew = patNew.matcher(ensFich[i]);
			if (matchAnc.find()) {
				int indPt = ensFich[i].indexOf(".",1);
				String nomAppli = ensFich[i].substring(1,indPt);
				if (!nomAppli.equals("erminae") && !(nomAppli.endsWith("."))) {
					vFich.add(nomAppli);
				}
				if (matchNew.find()) {
					indPt = ensFich[i].indexOf(".",1);
					nomAppli = ensFich[i].substring(1, indPt);
					if (!nomAppli.equals("erminae")) {
						vFich.add(nomAppli);
					}
				}
			}
		}

		String rep1 = (String) JOptionPane.showInputDialog(null,
				VarGlobales.res.getString("Quel_est_le_nom_de_l")
						+ VarGlobales.nomAppliEnCours, VarGlobales.res
						.getString("information"),
				JOptionPane.OK_CANCEL_OPTION, null, vFich.toArray(),
				VarGlobales.nomAppliEnCours);
		if (rep1 == null || rep1.length() == 0) {
			return;
		}
		// tester si il existe un répertoire de ce nom
		File repAppli = new File(path, rep1);
		if (repAppli.exists()) {
			if (repAppli.isDirectory()) {

				VarGlobales.RepertoireAppli = dirTerm.getPath();
				String nomFile = new String(".Terminae");
				VarGlobales.nomAppliEnCours = rep1;

				File file = new File(VarGlobales.RepertoireTerminae, nomFile);
				// fermeture des fenêtres déjà ouvertes
				Vector<JFrame> vEnsFen = PanListe.getListeFen();
				if (vEnsFen.size() > 0) {
					int rep2 = JOptionPane.showConfirmDialog(null,
							VarGlobales.res.getString("Fermeture_des_fen"),
							VarGlobales.res.getString("question"),
							JOptionPane.YES_NO_OPTION);
					if (rep2 == JOptionPane.YES_OPTION) {

						for (int i = 0; i < vEnsFen.size(); i++) {
							JFrame fen = ((JFrame) vEnsFen.elementAt(i));
							if (FenPrinc.uneFenRS == fen) {
								FenPrinc.uneFenRS = null;
							}
							fen.dispose();
							if (FenPrinc.EnsFenOuv != null) {
								FenPrinc.EnsFenOuv.removeFenOuv(fen);
							}
						}

					}
				}
				// modification d'une fenêtre de dialogue avec l'ensemble des
				// variables globales
				VarGlobales.creationFichierConfigGlobales(file);

				VarGlobales.initVarGlobalesAppli();
				// changement des répertoires
				VarGlobales.definitionRepertoires(rep1);

				// il faut modifier toutes les variables globales
				// System.err.println("trace
				// "+VarGlobales.RepertoireAppli+"."+VarGlobales.nomAppliEnCours+".cfg");
				File f = new File(VarGlobales.RepertoireAppli, "."
						+ VarGlobales.nomAppliEnCours + ".cfg");
				if (!f.exists()) {
					// modification des fichiers cfg en xml , suffixe xcfg
					f = new File(VarGlobales.RepertoireAppli, "."
							+ VarGlobales.nomAppliEnCours + ".xcfg");
					if (!f.exists()) {
						JOptionPane.showMessageDialog(null, VarGlobales.res
								.getString("cette_application_n_a"));
					}
					VarGlobales.chargementConfigVarGlobalesXml(f);
				} else {
					VarGlobales.chargementConfigVarGlobales(f);
				}

			}
		} else {
			JOptionPane.showMessageDialog(null, VarGlobales.res
					.getString("cette_application_n_a"));
		}
		/**
		 * on remet la bonne valeur car elle peut être modifiée par la lecture
		 * du fichier de config qui peut être incorrecte"
		 */
		VarGlobales.RepertoireAppli = path;
	}

	public static void chargementConfigVarGlobales(File f) {
		try {

			// chargement du fichier de configuration de l'application

			FileReader fisAppli = new FileReader(f);
			BufferedReader inAppli = new BufferedReader(fisAppli);
			String line = inAppli.readLine(); // lecture du nom de l'appli
			// lecture du nombre correspondant au numéro du réseau conceptuel
			String num = inAppli.readLine();
			// System.err.println("trace "+num);
			numReseauConceptuelAppliEnCours = Integer.parseInt(num);
			line = inAppli.readLine();

			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				String mot = st.nextToken("\t");

				if (mot.equals("CorpusCordialise")) {
					CorpusCordialise = st.nextToken("\t");
				}
				if (mot.equals("CorpusTreeTagger")) {
					CorpusTreeTagger = st.nextToken("\t");
				}
				if (mot.equals("CorpusSequence")) {
					CorpusSequence = st.nextToken("\t");

				}
				if (mot.equals("CorpusSequenceSyntex")) {
					CorpusSequenceSyntex = st.nextToken("\t");
				}
				if (mot.equals("Corpus")) {
					Corpus = st.nextToken("\t");
				}
				if (mot.equals("rubrique")) {
					ensRubriquesLexicales.add(st.nextToken("\t"));
				}
				if (mot.equals("fichier validation CT")) {
					nomFichierTableValidationCT = st.nextToken("\t");
				}
				if (mot.equals("fichier validation CT syntex")) {
					nomFichierTableValidationSyntex = st.nextToken("\t");
				}
				if (mot.equals("fichier CT validés")) {
					nomFichierEnsCTValidéOccu = st.nextToken("\t");
					chargementEnsCTOccu(true);
				}
				if (mot.equals("fichier CT validés Syntex")) {
					nomFichierEnsCTValidéOccuSyntex = st.nextToken("\t");
					chargementEnsCTOccu(false);
				}

				line = inAppli.readLine();
			} // fin while
			// chargement de la table des objets en cours de validation
			creatTableObjetEnCoursValidation();
			File fileO = new File(VarGlobales.RepertoireSysteme,
					VarGlobales.nomFichierObjetEnCoursValidation);
			if (fileO.exists()) {
				// lecture de la table
				// colonnes definies
				tableObjetEnCoursValid.lecFichTable(
						VarGlobales.RepertoireSysteme,
						VarGlobales.nomFichierObjetEnCoursValidation, '\t',
						true);
			}
		} catch (IOException execpt) {
			System.err.println(resTerm.getString("execution_impossible")
					+ execpt);
		}

	}

	public static void chargementConfigVarGlobalesXml(File f) {
		boolean initLexterSynt = false; // pour savoir si lexterSynt est
										// initialisé
		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(f);
			org.jdom.Element elemRoot = doc.getRootElement();

			org.jdom.Element elemNomAppli = elemRoot.getChild("Application");
			if (elemNomAppli == null) {
				throw new JDOMException(VarGlobales.res.getString("bal_Appli"));
			}
			nomAppliEnCours = elemNomAppli.getAttributeValue("nom");
			org.jdom.Element elemAuteur = elemRoot.getChild("Auteur");
			if (elemAuteur == null) {
				auteur = "admin"; // nom par défaut
			} else {
				auteur = elemAuteur.getAttributeValue("nom");
			}
			// lecture du repertoire de l'appli si il existe dec 02
			org.jdom.Element elemResConc = elemRoot
					.getChild("ReseauConceptuel");
			if (elemResConc != null) {
				numReseauConceptuelAppliEnCours = Integer.parseInt(elemResConc
						.getAttributeValue("numero"));
			}
			org.jdom.Element elemTag = elemRoot.getChild("CorpusTagger");
			if (elemTag != null) {
				CorpusCordialise = elemTag.getTextTrim();
			}
			elemTag = elemRoot.getChild("CorpusTaggerParTreeTagger");
			if (elemTag != null) {
				CorpusTreeTagger = elemTag.getTextTrim();
				langueCorpus = elemTag.getAttributeValue("langue");
			}
			elemTag = elemRoot.getChild("CorpusSequence");
			if (elemTag != null) {
				CorpusSequence = elemTag.getTextTrim();

			}
			elemTag = elemRoot.getChild("CorpusSequenceSyntex");
			if (elemTag != null) {
				CorpusSequenceSyntex = elemTag.getTextTrim();

			}
			elemTag = elemRoot.getChild("Corpus");
			if (elemTag != null) {
				Corpus = elemTag.getTextTrim();
			}
			elemTag = elemRoot.getChild("CorpusSynoTerm");
			if (elemTag != null) {
				CorpusSynoTerm = elemTag.getTextTrim();
			}
			elemTag = elemRoot.getChild("RubriquesLexicales");
			if (elemTag != null) {
				java.util.List<Element> elemRubList = elemTag.getChildren("rubrique");
				for (Iterator<Element> i = elemRubList.iterator(); i.hasNext();) {
					ensRubriquesLexicales.add(((Element) i.next())
							.getAttributeValue("nom"));
				}
			}
			elemTag = elemRoot.getChild("fichierValidationCT");
			if (elemTag != null) {
				lexterSynt = true;
				initLexterSynt = true;
				nomFichierTableValidationCT = elemTag.getTextTrim();
				VarGlobales.OutilExtracteurCT = TerminaeConstants.lexter;
			}

			elemTag = elemRoot.getChild("fichierValidationCTSyntex");
			if (elemTag != null) {
				lexterSynt = false;
				initLexterSynt = true;
				nomFichierTableValidationSyntex = elemTag.getTextTrim();
				VarGlobales.OutilExtracteurCT = TerminaeConstants.syntex;
			}
			elemTag = elemRoot.getChild("fichierValidationCTYatea");
			if (elemTag != null) {
				lexterSynt = false;
				initLexterSynt = true;
				nomFichierTableValidationYatea = elemTag.getTextTrim();
				VarGlobales.OutilExtracteurCT = TerminaeConstants.yatea;
			}
			elemTag = elemRoot.getChild("fichierCTValides");
			if (elemTag != null) {
				lexterSynt = true;
				initLexterSynt = true;
				nomFichierEnsCTValidéOccu = elemTag.getTextTrim();
				VarGlobales.OutilExtracteurCT = TerminaeConstants.lexter;
				chargementEnsCTOccu(true);
			}
			elemTag = elemRoot.getChild("fichierCTValidesSyntex");
			if (elemTag != null) {
				lexterSynt = false;
				initLexterSynt = true;
				nomFichierEnsCTValidéOccuSyntex = elemTag.getTextTrim();
				VarGlobales.OutilExtracteurCT = TerminaeConstants.syntex;
				chargementEnsCTOccu(false);
				
			}

			elemTag = elemRoot.getChild("RepertoireAppli");
			if (elemTag != null) {
				String RepertoireAppliAux = elemTag.getTextTrim();
				if (!(RepertoireAppli.equals(RepertoireAppliAux))) {
					RepertoireAppli = (String) JOptionPane
							.showInputDialog(
									null,
									resTerm
											.getString(" répertoire de l'application ?"),
									RepertoireAppli);
				}
			}

			// chargement de la table des objets en cours de validation
			creatTableObjetEnCoursValidation();
			File fileO = new File(VarGlobales.RepertoireSysteme,
					VarGlobales.nomFichierObjetEnCoursValidation);
			if (fileO.exists()) {
				// lecture de la table
				// colonnes definies
				tableObjetEnCoursValid.lecFichTable(
						VarGlobales.RepertoireSysteme,
						VarGlobales.nomFichierObjetEnCoursValidation, '\t',
						false);
			}
			// System.err.println("trace lexterSynt "+lexterSynt);
			/*if (VarGlobales.OutilExtracteurCT == null) {
				// int rep = JOptionPane.showConfirmDialog(null,
				// VarGlobales.resTerm.getString("LexterSynt"), "Question",
				// JOptionPane.YES_NO_OPTION);
				// if (rep == JOptionPane.YES_OPTION) {
				// lexterSynt = false;
				// }
				// else
				// {
				// lexterSynt = true;
				// }
				// a revoir
				String nomExtrac[] ={"Yatea","Lexter","Syntex"};
				String nomExtracteurCT = (String)JOptionPane.showInputDialog(null,"Information",VarGlobales.res.getString("Choix_extracteur_terme"),JOptionPane.INFORMATION_MESSAGE,null,
						nomExtrac,nomExtrac[0]);
				if(nomExtracteurCT ==null || nomExtracteurCT.length()==0) return;
				VarGlobales.OutilExtracteurCT=nomExtracteurCT;
				lexterSynt = false; // par défaut
			}*/
		}

		catch (JDOMException e) {
			try {
				Class cl = Class.forName("Terminae.VarGlobales");
				Outils.handleException(cl, e);
				JOptionPane.showMessageDialog(null, e.getMessage());
			} catch (Exception ex) {
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erreur " + ex);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, e.getMessage());

		}

	}

	public static void sauvegardeConfigVarglobales() {
		try {
			// System.err.println("trace sauvegardeConfigVarglobales "
			// +RepertoireAppli);
			FileOutputStream f = new FileOutputStream(new File(RepertoireAppli,
					"." + nomAppliEnCours + ".cfg"));
			PrintWriter p = new PrintWriter(f);
			p.println(nomAppliEnCours);
			p.println(numReseauConceptuelAppliEnCours);
			if (CorpusCordialise != null) {
				p.print("CorpusCordialise" + "\t");
				p.println(CorpusCordialise);
			}
			if (CorpusTreeTagger != null) {
				p.print("CorpusTreeTagger" + "\t");
				p.println(CorpusTreeTagger);
			}
			if (CorpusSequence != null) {
				p.print("CorpusSequence" + "\t");
				// System.err.println("trace sauvegarde " + CorpusSequence);
				p.println(CorpusSequence);
			}
			if (CorpusSequenceSyntex != null) {
				p.print("CorpusSequenceSyntex" + "\t");
				p.println(CorpusSequenceSyntex);
			}

			if (Corpus != null) {
				p.print("Corpus" + "\t");
				p.println(Corpus);
			}
			// sauvegarde des rubriques lexicales
			if (!ensRubriquesLexicales.isEmpty()) {
				int nb = ensRubriquesLexicales.size();
				for (int i = 0; i < nb; i++) {
					p.print("rubrique" + "\t");
					p.println(ensRubriquesLexicales.elementAt(i));
				}
			}
			if (nomFichierTableValidationCT != null) {
				p.print("fichier validation CT" + "\t");
				p.println(nomFichierTableValidationCT);
			}
			if (nomFichierTableValidationSyntex != null) {
				p.print("fichier validation CT syntex" + "\t");
				p.println(nomFichierTableValidationSyntex);
			}
			if (nomFichierEnsCTValidéOccu != null) {
				p.print("fichier CT validés" + "\t");
				p.println(nomFichierEnsCTValidéOccu);
			}
			if (nomFichierEnsCTValidéOccuSyntex != null) {
				p.print("fichier CT validés Syntex" + "\t");
				p.println(nomFichierEnsCTValidéOccuSyntex);
			}

			p.close();
			if (modifEnsRelationsLexSynt) {
				// sauvegarde de l'ensemble des relations LexSynt
				ecrFichierEnsRelSyntXML();
			}

			if (modifEnsRelationsSem) {
				// sauvegarde de l'ensemble des relations semantiques
				ecrFichierEnsRelSemXML();
			}
			if (GestionFiches.modifFicheTerm) {
				// sauvegarde de la table terme _ fiches
				GestionFiches.sauveFicheTermeXml();
			}
			// sauvegarde de la table des objets en cours de validation
			if (tableObjetEnCoursValid != null) {
				tableObjetEnCoursValid.enregistrerTexte(
						VarGlobales.RepertoireSysteme,
						VarGlobales.nomFichierObjetEnCoursValidation, true);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, resTerm
					.getString("erreur_lors_de_la_cr")
					+ e, resTerm.getString("erreur"), JOptionPane.OK_OPTION);
		}
	}

	public static void sauvegardeConfigVarglobalesXml() {
		try {
			if (nomAppliEnCours == null) {
				nomAppliEnCours = "application1";
			}
			File file = new File(RepertoireAppli, "." + nomAppliEnCours
					+ ".xcfg");
			FileWriter writer = new FileWriter(file);
			// entete du fichier
			org.jdom.Element config = new Element("Configuration");
			Document myDocument = new Document(config);
			//
			org.jdom.Element appliEnCours = new Element("Application");
			appliEnCours.setAttribute("nom", nomAppliEnCours);
			config.addContent(appliEnCours);
			//
			if (auteur != null) {
				org.jdom.Element auteur = new Element("Auteur");
				auteur.setAttribute("nom", VarGlobales.auteur);
				config.addContent(auteur);
			}
			org.jdom.Element reseauConc = new Element("ReseauConceptuel");
			reseauConc.setAttribute("numero", new Integer(
					numReseauConceptuelAppliEnCours).toString());
			config.addContent(reseauConc);
			org.jdom.Element corpCord = null;
			if (CorpusCordialise != null) {
				corpCord = new Element("CorpusTagger");
				corpCord.addContent(CorpusCordialise);
				config.addContent(corpCord);
			}
			if (CorpusTreeTagger != null) {
				corpCord = new Element("CorpusTaggerParTreeTagger");
				corpCord.addContent(CorpusTreeTagger);
				config.addContent(corpCord);
				if (VarGlobales.langueCorpus != null) {
					corpCord.setAttribute("langue", VarGlobales.langueCorpus);
				}
			}
			if (CorpusSequence != null) {
				corpCord = new Element("CorpusSequence");
				// System.err.println("trace sauv "+CorpusSequence);
				corpCord.addContent(CorpusSequence);
				config.addContent(corpCord);
			}
			if (CorpusSequenceSyntex != null) {
				corpCord = new Element("CorpusSequenceSyntex");
				corpCord.addContent(CorpusSequenceSyntex);
				config.addContent(corpCord);
			}

			if (Corpus != null) {
				corpCord = new Element("Corpus");
				corpCord.addContent(Corpus);
				config.addContent(corpCord);
			}
			if (CorpusSynoTerm != null) {
				// sauvegarde du corpus donné par SynoTerm
				corpCord = new Element("CorpusSynoTerm");
				corpCord.addContent(CorpusSynoTerm);
				config.addContent(corpCord);
			}
			if (CorpusYatea != null) {
				// sauvegarde du corpus donné par SynoTerm
				corpCord = new Element("CorpusYatea");
				corpCord.addContent(CorpusYatea);
				config.addContent(corpCord);
			}
			// sauvegarde des rubriques lexicales
			if (!ensRubriquesLexicales.isEmpty()) {
				corpCord = new Element("RubriquesLexicales");
				int nb = ensRubriquesLexicales.size();
				for (int i = 0; i < nb; i++) {
					org.jdom.Element elem = new Element("rubrique");
					elem.setAttribute("nom", (String) ensRubriquesLexicales
							.elementAt(i));
					corpCord.addContent(elem);
				}
				config.addContent(corpCord);

			}
			if (nomFichierTableValidationCT != null) {
				corpCord = new Element("fichierValidationCT");
				corpCord.addContent(nomFichierTableValidationCT);
				config.addContent(corpCord);
			}
			if (nomFichierTableValidationSyntex != null) {
				corpCord = new Element("fichierValidationCTSyntex");
				corpCord.addContent(nomFichierTableValidationSyntex);
				config.addContent(corpCord);
			}
			if (nomFichierTableValidationYatea != null) {
				corpCord = new Element("fichierValidationCTYatea");
				corpCord.addContent(nomFichierTableValidationYatea);
				config.addContent(corpCord);
			}
			if (nomFichierEnsCTValidéOccu != null) {
				corpCord = new Element("fichierCTValides");
				corpCord.addContent(nomFichierEnsCTValidéOccu);
				config.addContent(corpCord);
			}
			if (nomFichierEnsCTValidéOccuSyntex != null) {
				corpCord = new Element("fichierCTValidesSyntex");
				corpCord.addContent(nomFichierEnsCTValidéOccuSyntex);
				config.addContent(corpCord);
			}

			if (modifEnsRelationsLexSynt) {
				// sauvegarde de l'ensemble des relations LexSynt
				ecrFichierEnsRelSyntXML();
			}

			if (modifEnsRelationsSem) {
				// sauvegarde de l'ensemble des relations semantiques
				ecrFichierEnsRelSemXML();
			}
			if (GestionFiches.modifFicheTerm) {
				// sauvegarde de la table terme _ fiches
				GestionFiches.sauveFicheTermeXml();
			}
			// sauvegarde de la table des objets en cours de validation
			if (tableObjetEnCoursValid != null) {
				// System.err.println("trace varglo sauv
				// "+tableObjetEnCoursValid.getRowCount());
				tableObjetEnCoursValid.enregistrerTexte(
						VarGlobales.RepertoireSysteme,
						VarGlobales.nomFichierObjetEnCoursValidation, true);
			}
			// sauvegarde répertoire des appli
			if (RepertoireAppli != null) {
				corpCord = new Element("RepertoireAppli");
				corpCord.addContent(RepertoireAppli);
				config.addContent(corpCord);
			}
			XMLOutputter outputter = new XMLOutputter("  ", true, "ISO-8859-1");
			outputter.output(myDocument, writer);
			writer.close();
		} catch (Exception e) {
			Outils.handleException(new VarGlobales(), e);
			System.err.println(e);
		}
	}

	/**
	 * initialisation de lexSynt
	 */
	public static void initLexSynt() {
		String rep = JOptionPane.showInputDialog(VarGlobales.res
				.getString("Chargement_partir_de"));
		if (rep == null || rep.length() == 0) {
			return;
		}
		lexterSynt = rep.equals("L");
	}

	/**
	 * chargement de la table définitive des CT validés lexSynt = true fichiers
	 * lexter lexSynt=false fichiers Syntex
	 */
	public static void chargementEnsCTOccu(boolean lexSynt) {
		 //System.err.println("trace chargementEnsCTOccu "+lexSynt);
		File file = null;
		if (lexSynt) {
			file = new File(VarGlobales.RepertoireSysteme,
					VarGlobales.nomFichierEnsCTValidéOccu);
		} else {
			if (VarGlobales.nomFichierEnsCTValidéOccuSyntex == null) {
				VarGlobales.nomFichierEnsCTValidéOccuSyntex = SyntexConstants.nomFichierValSyntDef;
				if (VarGlobales.nomFichierTableValidationSyntex != null) {
					// lecture de la table qui a été créée
					TableAffCTOccuSyntex tableValCTEnCours = new TableAffCTOccuSyntex(
							VarGlobales.nomFichierTableValidationSyntex);
					tableValCTEnCours.chargerObjet(
							VarGlobales.RepertoireCandidatsTermes,
							VarGlobales.nomFichierTableValidationSyntex);
					VarGlobales.setEnsCTOccuSyntex(tableValCTEnCours
							.creatEnsCTOccuSyntexAPartirTableValCT());
					// on crée le fichier avec la table définitive
					ElemCTOccuNum.enregistrerEnsCTOccuSyntex();
				} else {
					// création du fichier et de la table
					TreeMap<Integer, ElemCTOccuNum> tableValSynt = FenValidationSyntex
							.creatTableInitValCTSynt();
					if (tableValSynt != null && !(tableValSynt.isEmpty())) {
						VarGlobales.setEnsCTOccuSyntex(tableValSynt);
					}

					// création de la table définitive
					ElemCTOccuNum.enregistrerEnsCTOccuSyntex();
				}
			}
			// sinon le fichier existe

			 //System.err.println("trace nom fich"+VarGlobales.nomFichierEnsCTValidéOccuSyntex);
			file = new File(VarGlobales.RepertoireSysteme,
					VarGlobales.nomFichierEnsCTValidéOccuSyntex);

		}
		try {
			FileInputStream f = new FileInputStream(file);
			// System.err.println("trace chargementEnsCTOccu varglo");
			ProgressMonitorInputStream pMI = new ProgressMonitorInputStream(
					null, resTerm.getString("Chargement_de_la"), f);
			BufferedInputStream buf = new BufferedInputStream(pMI);
			ObjectInputStream s = new ObjectInputStream(buf);
			if (lexSynt) {
				VarGlobales.ensCTOccu = new TreeMap<String, ElementCTOccu>(
						((TreeMap<String, ElementCTOccu>) s.readObject()));
			} else {
				//System.err.println("trace deb");
				VarGlobales.ensCTOccuSyntex = new TreeMap<Integer, ElemCTOccuNum>(
						(TreeMap<Integer, ElemCTOccuNum>) s.readObject());
				 //System.err.println("trace"+VarGlobales.ensCTOccuSyntex.values().size());
			}
			s.close();

			// JOptionPane.showMessageDialog(null, "La table est
			// chargée","information",JOptionPane.INFORMATION_MESSAGE);
		}

		catch (IOException e) {
			JOptionPane.showMessageDialog(null, resTerm
					.getString("erreur_d_e_s_dans_la")
					+ file.getName() + " " + e);
		} catch (Exception e) {
			try {
				System.err.println("trace erreur varglobales " + e);
				Class cl = Class.forName("Terminae.VarGlobales");
				Outils.handleException(cl, e);
				return;
			} catch (Exception ex) {

			}
		}
	}
	/*
	 * lecture des termes à partir de la validation faite à partir de l'extracteur Yatea
	 */
	public static void chargementEnsCTOccuYatea() {
		//test si le fichier avec la table définitive existe 
		File file = new File(RepertoireSysteme,utilss.TerminaeConstants.nomFichierValYateaDef);
		if(!file.exists()){
			//le fichier n'a pas été créé
			JOptionPane.showMessageDialog(null,"info"," afaire",JOptionPane.INFORMATION_MESSAGE);
		}
		else
		{
			//lecture du fichier
			TableAffCTOccuYatea tableValCTEnCours = new TableAffCTOccuYatea();
			if (!(TermCandidate.chargerEnsCTYatea()))
				return;
	//			tableValCTEnCours.chargerAPartirEnsCTYatea();
				tableValCTEnCours.chargerAPartirEnsLemmaYatea();
			
		}
			
	}
	/**
	 * sauvegarde du fichier des relations lex Synt en format xml
	 */
	public static void ecrFichierEnsRelSyntXML() {

		String dir = VarGlobales.RepertoireLinguae;
		File file = new File(VarGlobales.RepertoireLinguae,
				nomFichierRelationsLexSyntXml);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			// entete du fichier

			org.jdom.Element reLexSyntXml = new Element("EnsRelLexSynt");
			Document myDocument = new Document(reLexSyntXml);
			for (Iterator i = ensRelationsLexSynt.iterator(); i.hasNext();) {
				RelationLexSynt relLexSynt = (RelationLexSynt) i.next();
				relLexSynt.ecritFichierXml(reLexSyntXml);
			}
			XMLOutputter outputter = new XMLOutputter("  ", true, "ISO-8859-1");
			outputter.output(myDocument, writer);
			writer.close();
			// JOptionPane.showMessageDialog(null, "Fichier
			// créé","Information",JOptionPane.INFORMATION_MESSAGE );

		} catch (Exception e) {
			Outils.handleException(new VarGlobales(), e);
			System.err.println(e);
		} finally {
			writer = null;
			file = null;
		}
		;
	}

	/**
	 * sauvegarde du fichier des relations Semantiques (prédéfinies) en format
	 * xml
	 */
	public static void ecrFichierEnsRelSemXML() {

		String dir = VarGlobales.RepertoireFichesModelisation;
		File file = new File(dir, VarGlobales.nomFichierRelationsSemantiquesXml);
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			// entete du fichier

			org.jdom.Element reLSemXml = new Element("EnsRelSem");
			Document myDocument = new Document(reLSemXml);
			for (Iterator i = ensRelationsSem.iterator(); i.hasNext();) {
				RelationSem relSem = (RelationSem) i.next();
				relSem.ecritFichierXml(reLSemXml);
			}
			XMLOutputter outputter = new XMLOutputter("  ", true, "ISO-8859-1");
			outputter.output(myDocument, writer);
			writer.close();
			// JOptionPane.showMessageDialog(null, "Fichier
			// créé","Information",JOptionPane.INFORMATION_MESSAGE );

		} catch (Exception e) {
			Outils.handleException(new VarGlobales(), e);
			System.err.println(e);
		} finally {
			writer = null;
			file = null;
		}
		;
	}

	/**
	 * lecture d'un fichier XML
	 */
	public static void lectFichierEnsRelSyntXML() {
		File f = new File(VarGlobales.RepertoireLinguae,
				nomFichierRelationsLexSyntXml);
		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(f);
			org.jdom.Element racine = doc.getRootElement();
			java.util.List listeElem = ((Element) racine).getContent(); // .getContent();
			// System.err.println("trace "+listeElem);
			for (int i = 0; i < listeElem.size() - 1; i += 2) {
				org.jdom.Element unElem = (Element) listeElem.get(i + 1);

				// lecture d'une relation
				RelationLexSynt.litFichierXml(unElem);
			}
		} catch (JDOMException e) {
			try {
				Class varGlobales = Class.forName("VarGlobales");
				Outils.handleException(varGlobales, e);
			} catch (Exception ex) {
				System.err.println(resTerm.getString("erreur1") + ex);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erreur " + ex);
		}
	}
	/**
	 * lecture d'un fichier XML
	 */
	public static File lectFichierYateaXML() {
		File file=null;
		JFileChooser fj = new JFileChooser(new File(VarGlobales.RepertoireYatea));
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("xml");
		fj.setFileFilter(filter);
		fj.setDialogTitle(VarGlobales.res.getString("Dfinir_le_fichier2"));
		int resul = fj.showOpenDialog(null);
		// dir=fj.getCurrentDirectory().getName();
		if (resul == JFileChooser.APPROVE_OPTION) {
			file = fj.getSelectedFile();
			// chargement de la table et des objets correspondants
			if (Table.getLesNomsTable().contains(file.getName())) {
				JOptionPane.showMessageDialog(null, VarGlobales.res
						.getString("Une_table_de_m_me_nom"));
				return null;
			}
		try {
			VarGlobales.ensCTYatea = new TreeMap<String,TermCandidate>();
			
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(file);
			org.jdom.Element racine = doc.getRootElement();
			
// <!ELEMENT  TERM_EXTRACTION_RESULTS (LIST_TERM_CANDIDATES,LIST_WORDS)>
			
			Element listeTC= racine.getChild("LIST_TERM_CANDIDATES");
			java.util.List<Element> ensTC = listeTC.getChildren("TERM_CANDIDATE");
	//		org.jdom.Element listeTc = racine.getChild("ListeTermCandidate");
			
			for(Iterator<Element> i=ensTC.iterator();i.hasNext();){
				Element unElemTc = i.next();
				TermCandidate tc = new TermCandidate();
				tc.litXml(unElemTc);
				//chargement par ID
				VarGlobales.ensCTYatea.put(tc.getID(),tc);
				//chargement par lemme
	//			System.err.println(VarGlobales.ensLemmaIDsYatea.get(tc.getLEMMA()));
				if(VarGlobales.ensLemmaIDsYatea.get(tc.getLEMMA())==null)
				{
				//	System.err.println("trace"+tc.getLEMMA());
					//c'est le premier
					ArrayList<String> col = new ArrayList<String>();
					col.add(tc.getID());
					VarGlobales.ensLemmaIDsYatea.put(tc.getLEMMA(),col);
				}
				else
				{
					//fusion des lemmes !!!
					ArrayList<String> col = VarGlobales.ensLemmaIDsYatea.get(tc.getLEMMA());
					col.add(tc.getID());
					VarGlobales.ensLemmaIDsYatea.put(tc.getLEMMA(),col);
				}
				
			}
		} catch (JDOMException e) {
			System.err.println("erreur "+e);
			try {
				Class varGlobales = Class.forName("VarGlobales");
				Outils.handleException(varGlobales, e);
			} catch (Exception ex) {
				System.err.println(ex);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erreur " + ex);
		}
		}
		return file;
	}
		
	/**
	 * lecture d'un fichier XML relation semantique
	 */
	public static void lectFichierEnsRelSemXML() {
		File f = new File(VarGlobales.RepertoireFichesModelisation,
				VarGlobales.nomFichierRelationsSemantiquesXml);
		try {
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(f);
			org.jdom.Element racine = doc.getRootElement();
			java.util.List listeElem = racine.getContent(); // anc getContent
			// System.err.println("trace "+listeElem);
			for (int i = 0; i < listeElem.size() - 1; i += 2) {
				org.jdom.Element unElem = (Element) listeElem.get(i + 1);

				// lecture d'une relation
				RelationSem.litFichierXml(unElem);
			}
		} catch (JDOMException e) {
			try {
				Class varGlobales = Class.forName("VarGlobales");
				Outils.handleException(varGlobales, e);
			} catch (Exception ex) {
				System.err.println(resTerm.getString("erreur1") + ex);
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erreur " + ex);
		}
	}

	public static void incNumReseauConceptuelAppliEncours() {
		numReseauConceptuelAppliEnCours++;
	}

	public static void configRubriqueslexicales() {

		if (ensRubriquesLexicales.isEmpty()) {
			ensRubriquesLexicales.add("langue");
			ensRubriquesLexicales.add("catégorie grammaticale");
			ensRubriquesLexicales.add("genre");
		}
		VarGlobales uneInst = new VarGlobales();
		new FenInfoListObj(resTerm.getString("Configuration_des1"),
				ensRubriquesLexicales.toArray(), uneInst);
	}

	public static void ajoutRubriqueLexicale(FenInfoListObj fenInfo) {
		String rubrique = (String) JOptionPane.showInputDialog(null, resTerm
				.getString("Donner_le_nom_d_une"), resTerm
				.getString("Ajout_d_une_rubrique"), JOptionPane.PLAIN_MESSAGE);
		if (rubrique == null || rubrique.length() == 0) {
			return;
		}
		ensRubriquesLexicales.add(rubrique);
		fenInfo.ajoutListeModel(rubrique);
	}

	public static void suppRubriqueLexicale(FenInfoListObj fenInfo,
			String rubrique) {

		if (!ensRubriquesLexicales.removeElement(rubrique)) {
			JOptionPane.showMessageDialog(null, resTerm
					.getString("erreur_suppression"));
		}
		fenInfo.suppListeModel(rubrique);
	}

	/**
	 * * sauvegarde dans l'ensemble des ct valides ou en cours de validation
	 */
	public static void setEnsCTOccuSyntex(TreeMap<Integer, ElemCTOccuNum> ens) {
		ensCTOccuSyntex = ens;
	}

	public static void setCorpusSequence(String dir, String fich) {
		File f = new File(dir, fich);
		if (f == null) {
			return;
		}

		// try
		// {
		if (f.exists()) {
			File fAnc = new File(dir, "anc" + fich);
			f.renameTo(fAnc);
		}
		CorpusSequence = f.getName();

		// }
		// catch(IOException e)
		// {
		// System.out.println(resTerm.getString("erreur_d_e_s")+e);
		// }
	}

	public static void setCorpusSequenceSyntex(String dir, String fich) {
		File f = new File(dir, fich);
		if (f == null) {
			return;
		}
		CorpusSequenceSyntex = f.getName();

	}

	public static void defCorpusSeq() {
		JFileChooser fj = new JFileChooser(RepertoireFichiersLexter);
		// System.err.println("trace defcorpu");
		fj.setDialogTitle(resTerm.getString("chargement_fichier_s"));
		int result = fj.showOpenDialog(null);
		File file = fj.getSelectedFile();
		if (file == null) {
			return;
		}
		// try
		// {
		CorpusSequence = file.getName();
		// System.err.println("trace defCorpusSeq "+CorpusSequence);
		// }
		// catch(IOException e)
		// {
		// System.out.println(resTerm.getString("erreur_d_e_s")+e);
		// }

	}

	public static void defCorpusSeqSyntex() {
		JFileChooser fj = new JFileChooser(RepertoireFichiersLexter);
		// System.err.println("trace defcorpu");
		fj.setDialogTitle(resTerm.getString("chargement_fichier_s"));
		int result = fj.showOpenDialog(null);
		File file = fj.getSelectedFile();
		if (file == null) {
			return;
		}
		CorpusSequenceSyntex = file.getName();
	}

	public static void defCorpus() {
		Corpus = JOptionPane
				.showInputDialog(resTerm.getString("Nom_du_corpus"));
	}

	public static Table getTableSeq() {
		boolean fait = false;
		int rep = JOptionPane.NO_OPTION;
		// System.err.println("trace getTableseq");
		if (tableSeq == null) {
			tableSeq = Table.initTableSeq();
			// System.err.println("trace tableseq==null");
			if (CorpusSequence == null) {
				defCorpusSeq();
				if (CorpusSequence == null) {
					return null;
				}
			}

			// System.err.println("trace "+CorpusSequence);
			fait = tableSeq.lecFichTable(VarGlobales.RepertoireFichiersLexter,
					CorpusSequence, '\t', false);
			if (!fait) {
				defCorpusSeq();
				fait = tableSeq.lecFichTable(
						VarGlobales.RepertoireFichiersLexter, CorpusSequence,
						'\t', false);
			}
			if (!fait) {
				rep = JOptionPane.showConfirmDialog(null, resTerm
						.getString("il_y_a_une_erreur"), resTerm
						.getString("question"), JOptionPane.YES_NO_OPTION);
			}
			while (!fait && rep == JOptionPane.YES_OPTION) {
				fait = tableSeq.lecFichTable(RepertoireFichiersLexter,
						CorpusSequence, '\t', true);
				if (!fait) {
					rep = JOptionPane.showConfirmDialog(null, resTerm
							.getString("il_y_a_une_erreur"), resTerm
							.getString("question"), JOptionPane.YES_NO_OPTION);
				}

			}
			if (!fait) {
				tableSeq = null;
			}
		}
		return tableSeq;
	}

	public static Table getTableSyntexSeq() {
		boolean fait = false;
		int rep = JOptionPane.NO_OPTION;
		// System.err.println("trace getTableseq");
		if (tableSeqSyntex == null) {
			tableSeqSyntex = Table.initTableSyntexSeq();
			// System.err.println("trace tableseq==null");
			if (CorpusSequenceSyntex == null) {
				defCorpusSeqSyntex();
				if (CorpusSequenceSyntex == null) {
					return null;
				}
			}
			fait = tableSeqSyntex.lecFichTable(
					VarGlobales.RepertoireFichiersLexter, CorpusSequenceSyntex,
					'\t', false);
			if (!fait) {
				defCorpusSeqSyntex();
				fait = tableSeqSyntex.lecFichTable(
						VarGlobales.RepertoireFichiersLexter,
						CorpusSequenceSyntex, '\t', false);
			}
			// System.err.println("trace "+CorpusSequenceSyntex);

			// fait=tableSeqSyntex.lecFichTable(VarGlobales.RepertoireFichiersLexter,
			// CorpusSequenceSyntex,'\t',false);

			if (!fait) {
				rep = JOptionPane.showConfirmDialog(null, resTerm
						.getString("il_y_a_une_erreur"), resTerm
						.getString("question"), JOptionPane.YES_NO_OPTION);
			}
			while (!fait && rep == JOptionPane.YES_OPTION) {
				fait = tableSeqSyntex.lecFichTable(RepertoireFichiersLexter,
						SyntexConstants.fichSyntexSeq, '\t', true);
				if (!fait) {
					rep = JOptionPane.showConfirmDialog(null, resTerm
							.getString("il_y_a_une_erreur"), resTerm
							.getString("question"), JOptionPane.YES_NO_OPTION);
				}

			}
			if (!fait) {
				tableSeqSyntex = null;
			}
		}
		return tableSeqSyntex;
	}

	public static String getDateSysteme() {
		Date dateJour = new Date();
		String jourHeure = new String(DateFormat.getDateInstance().format(
				dateJour));
		jourHeure = jourHeure.trim().replace('.', '_');
		jourHeure = jourHeure.replace(':', '_');
		return jourHeure;
	}

	// pour gérer la liste des concepts terminologiques
	public static ConceptTerminologique getConceptTerm(String ident) {

		Iterator i = ensConceptsTerminologiques.iterator();
		while (i.hasNext()) {
			ConceptTerminologique concTerm = (ConceptTerminologique) i.next();
			if (concTerm.getIdentifiant().equals(ident)) {
				return concTerm;
			}
		}
		return null;

	}

	public static void creatTableObjetEnCoursValidation() {
		Vector<String> nomCol = new Vector<String>();
		nomCol.add("Type_objet");
		nomCol.add("Identifiant_objet");
		tableObjetEnCoursValid = new Table("tableObjetsEnCoursValidation", 2,
				nomCol);
	}

	public static boolean chargeCorpusSynoTerm() {
		File f = null;
		try {
			if (CorpusSynoTerm == null) {

				ExtensionFileFilter filter = new ExtensionFileFilter();
				filter.addExtension("xml");
				JFileChooser fj = new JFileChooser(new File(
						VarGlobales.RepertoireCorpus));
				fj.setFileFilter(filter);
				fj.addChoosableFileFilter(filter);
				int resul = fj.showOpenDialog(null);
				if (resul == JFileChooser.CANCEL_OPTION) {
					return false;
				}
				if (resul == JFileChooser.APPROVE_OPTION) {
					f = fj.getSelectedFile();
					CorpusSynoTerm = f.getName();
				}
			} else {
				f = new File(RepertoireCorpus, CorpusSynoTerm);
			}
			SAXBuilder builder = new SAXBuilder(false);
			org.jdom.Document doc = builder.build(f);
			org.jdom.Element racine = doc.getRootElement();
			java.util.List listeElem = racine.getContent();
			instSynoTerm = new SynoTerm();
			// System.err.println("trace "+racine);
			instSynoTerm.litXml(racine);

		} catch (JDOMException e) {
			try {
				// Outils.handleException(this ,e);
				FenDialogText fText = new FenDialogText(null,
						VarGlobales.resTerm.getString("Erreur_dans_le"), false,
						null, 500, 600);
				fText.ajouteText(e.getMessage());

				// JOptionPane.showMessageDialog(null,"erreur dans le texte xml
				// "+e.getMessage());
				return false;
			} catch (Exception ex) {
				System.err.println("erreur classe RS " + ex);
				return false;
			}
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erreur " + ex);
			return false;
		}
		return true;
	}
	/*
	 * enregistrement texte XML Yatea
	 */
	public static void ecrFichierYateaXML() {
		File file;
		String dir = VarGlobales.RepertoireYatea;
		JFileChooser fj = new JFileChooser(dir);
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("xml");
		fj.setFileFilter(filter);
		boolean bool = false;
		do {
			int result = fj.showSaveDialog(null);
			file = fj.getSelectedFile();
			if (file == null) {
				return;
			}
			String nomFile = file.getName();
			if (!(nomFile.endsWith(".xml"))) {
				nomFile = new String(nomFile + ".xml");
				file = new File(file.getParent(), nomFile);
			}
			if (file.exists()) {
				int rep = JOptionPane.showConfirmDialog(null, VarGlobales.resTerm
						.getString("Ce_fichier_existe_d_j"));
				bool = rep == JOptionPane.YES_OPTION;
			} else {
				bool = true;
			}
		} while (!bool);
		
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			// entete du fichier

			org.jdom.Element listeTC = new Element("ListeTermCandidate");
			Document myDocument = new Document(listeTC);
			for (Iterator i = ensCTYatea.values().iterator(); i.hasNext();) {
				TermCandidate tc= (TermCandidate) i.next();
				tc.ecritXml(listeTC);
			}
			XMLOutputter outputter = new XMLOutputter("  ", true);
			outputter.output(myDocument, writer);
			writer.close();
			 JOptionPane.showMessageDialog(null, "Fichier créé","Information",JOptionPane.INFORMATION_MESSAGE );

		} catch (Exception e) {
			Outils.handleException(new VarGlobales(), e);
			System.err.println(e);
		} finally {
			writer = null;
			file = null;
		}

	} // fin
	/*
	 * enregistrement texte XML Lexter
	 */
	public static void ecrFichierLexterXML() {
		File file;
		String dir = VarGlobales.RepertoireFichiersLexter;
		JFileChooser fj = new JFileChooser(dir);
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("xml");
		fj.setFileFilter(filter);
		boolean bool = false;
		do {
			int result = fj.showSaveDialog(null);
			file = fj.getSelectedFile();
			if (file == null) {
				return;
			}
			String nomFile = file.getName();
			if (!(nomFile.endsWith(".xml"))) {
				nomFile = new String(nomFile + ".xml");
				file = new File(file.getParent(), nomFile);
			}
			if (file.exists()) {
				int rep = JOptionPane.showConfirmDialog(null, VarGlobales.resTerm
						.getString("Ce_fichier_existe_d_j"));
				bool = rep == JOptionPane.YES_OPTION;
			} else {
				bool = true;
			}
		} while (!bool);
		
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			// entete du fichier

			org.jdom.Element listeTC = new Element("ListeTermCandidate");
			Document myDocument = new Document(listeTC);
			for (Iterator i = VarGlobales.ensCTOccu.values().iterator(); i.hasNext();) {
				ElementCTOccu elem = (ElementCTOccu)i.next();
				elem.ecritXML(listeTC);
			}
			XMLOutputter outputter = new XMLOutputter("  ", true);
			outputter.output(myDocument, writer);
			writer.close();
			 JOptionPane.showMessageDialog(null, "Fichier créé","Information",JOptionPane.INFORMATION_MESSAGE );

		} catch (Exception e) {
			Outils.handleException(new VarGlobales(), e);
			System.err.println(e);
		} finally {
			writer = null;
			file = null;
		}

	} // fin
	public static void imprimerYatea() {
		File file;
		String dir = VarGlobales.RepertoireYatea;
		JFileChooser fj = new JFileChooser(dir);
		ExtensionFileFilter filter = new ExtensionFileFilter();
		filter.addExtension("xml");
		fj.setFileFilter(filter);
		boolean bool = false;
		do {
			int result = fj.showSaveDialog(null);
			file = fj.getSelectedFile();
			if (file == null) {
				return;
			}
			String nomFile = file.getName();
			if (!(nomFile.endsWith(".xml"))) {
				nomFile = new String(nomFile + ".xml");
				file = new File(file.getParent(), nomFile);
			}
			if (file.exists()) {
				int rep = JOptionPane.showConfirmDialog(null, VarGlobales.resTerm
						.getString("Ce_fichier_existe_d_j"));
				bool = rep == JOptionPane.YES_OPTION;
			} else {
				bool = true;
			}
		} while (!bool);
		
		FileWriter writer;
		try {
			writer = new FileWriter(file);
			// entete du fichier

			org.jdom.Element listeTC = new Element("ListeTermCandidate");
			Document myDocument = new Document(listeTC);
			for (Iterator i = VarGlobales.ensCTYatea.values().iterator(); i.hasNext();) {
				TermCandidate tc= (TermCandidate) i.next();
				tc.imprimerXML(listeTC);
			}
			XMLOutputter outputter = new XMLOutputter("  ", true);
			outputter.output(myDocument, writer);
			writer.close();
			 JOptionPane.showMessageDialog(null, "Fichier créé","Information",JOptionPane.INFORMATION_MESSAGE );

		} catch (Exception e) {
			Outils.handleException(new VarGlobales(), e);
			System.err.println(e);
		} finally {
			writer = null;
			file = null;
		}

	} // fin
} // fin classe
