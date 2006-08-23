package arkeotek.io.exporter.OWL;

/**
 * <p>Title: TERMINAE</p>
 * <p>Description: Outil d'aide � la construction d'ontologies~</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: LIPN</p>
 * @author Sylvie Szulman
 * @version 6.0
 */
import java.util.*;
import java.io.*;

public class KBOwl extends KB implements OwlConstants {
	public Hashtable<String, Vector<OwlProperty>> lesObjetsOwlProperty; // ajout
																		// pour
																		// g�rer
																		// les
																		// propri�t�s
																		// pour
																		// owl

	public Hashtable<String, Vector<OwlPropertyValeur>> lesObjetsOwlPropertyValeur; // ajout
																					// pour
																					// g�r�er
																					// les
																					// propri�t�s
																					// individuelles

	public Hashtable<String, Vector<OwlDataProperty>> lesObjetsDataProperty; // ens
																				// des
																				// propri�t�s
																				// donn�es
																				// Xml

	public Hashtable<String, OwlClass> lesObjetsOwlClasses; // ens des classe
															// Owl

	public static Vector<String> sauvMereFille = new Vector<String>(); // sert
																		// pour
																		// d�finir
																		// les
																		// relations
																		// pere-fils
																		// entre
																		// classes

	public KBOwl(String n) {
		super(n, VarGlobales.resTerm.getString("Ontologie"));
		lesObjetsOwlProperty = new Hashtable<String, Vector<OwlProperty>>();
		lesObjetsDataProperty = new Hashtable<String, Vector<OwlDataProperty>>();
		lesObjetsOwlClasses = new Hashtable<String, OwlClass>();
		lesObjetsOwlPropertyValeur = new Hashtable<String, Vector<OwlPropertyValeur>>();
	}

	public void initKBOwl() {
		// cr�ation de la classe Thing
		OwlClass thing = new OwlClass(TopConcept, new TreeSet<OwlClass>(),
				null, null, null, null, new Vector<OwlClass>(),
				" la racine de l'ontologie ", new Vector<String>());
		// cr�ation de la classe Classe
		TreeSet<OwlClass> ensMeres = new TreeSet<OwlClass>();
		ensMeres.add(thing);
		OwlClass classe = new OwlClass(ConceptAnonyme, ensMeres, null, null,
				null, null, new Vector<OwlClass>(), " regroupe les classes",
				new Vector<String>());
		// cr�ation de la classe DataType
		OwlClass datatype = new OwlClass(DataType, ensMeres, null, null, null,
				null, new Vector<OwlClass>(),
				" regroupe les types de donn�es d�finies dans XML sch�ma",
				new Vector<String>());
		// cr�ation de la classe EnsProprietes
		OwlClass ensProprietes = new OwlClass(EnsProprietes, ensMeres, null,
				null, null, null, new Vector<OwlClass>(),
				" regroupe les propri�t�s d�finies sans domain et range",
				new Vector<String>());
		// cr�ation du concept de regroupement des compl�mentaires
		OwlClass complementaireConc = new OwlClass(ComplementaireConc,
				ensMeres, null, null, null, null, new Vector<OwlClass>(),
				" regroupe les concepts d�finis par un complementOf",
				new Vector<String>());
		// cr�ation du concept de regroupment des unionof
		OwlClass unionOfConc = new OwlClass(UnionOfConc, ensMeres, null, null,
				null, null, new Vector<OwlClass>(),
				" regroupe les concepts d�finis par un unionOf",
				new Vector<String>());
		// cr�ation du concept de regroupment des intersectionof
		// OwlClass intersectionOfConc = new
		// OwlClass(IntersectionOfConc,ensMeres,null,null,null,null,new
		// Vector()," regroupe les concepts d�finis par un intersectionOf",new
		// Vector());
		
		lesObjetsOwlClasses.put(TopConcept, thing);
		lesObjetsOwlClasses.put(ConceptAnonyme, classe);
		lesObjetsOwlClasses.put(DataType, datatype);
		lesObjetsOwlClasses.put(EnsProprietes, ensProprietes);
		lesObjetsOwlClasses.put(ComplementaireConc, complementaireConc);
		lesObjetsOwlClasses.put(UnionOfConc, unionOfConc);
		// suppression du concept intersectionOf
		// lesObjetsOwlClasses.put(IntersectionOfConc,intersectionOfConc);
		//cr�ation d'un propri�t� proprieteOntologie_nomOnto rattach�e � ensPropri�t�
		// � valeur dans String 
		OwlProperty uneProp = new OwlProperty(nomPropOntoExt,EnsProprietes,"String");
		this.addObjetOwlProperty(uneProp);
	}

	public String getTopConcept() {
		return TopConcept;
	}

	public String getConceptEnsProprietes() {
		return EnsProprietes;
	}

	public boolean addObjetOwlPropertyValeur(OwlPropertyValeur p) {
		Vector<OwlPropertyValeur> v = new Vector<OwlPropertyValeur>();
		if (lesObjetsOwlPropertyValeur.containsKey(p.getNom())) {
			v = (Vector<OwlPropertyValeur>) lesObjetsOwlPropertyValeur.get(p
					.getNom());
			if (v.contains(p)) {
				return false;
			}
		}
		v.addElement(p);

		lesObjetsOwlPropertyValeur.put(p.getNom(), v);
		return true;
	}

	public boolean addObjetDataProperty(OwlDataProperty p) {
		// il peut y avoir plusieurs propri�t�s de m�me nom � valeur
		Vector<OwlDataProperty> v = new Vector<OwlDataProperty>();
		if (lesObjetsDataProperty.containsKey(p.getNom())) {
			v = (Vector<OwlDataProperty>) lesObjetsDataProperty.get(p.getNom());
			if (v.contains(p)) {
				return false;
			}
		}
		v.addElement(p);
		lesObjetsDataProperty.put(p.getNom(), v);
		return true;
	}

	public boolean removeObjectDataProperty(OwlDataProperty p) {
		Vector<OwlDataProperty> v = (Vector<OwlDataProperty>) lesObjetsDataProperty
				.get(p.getNom());
		if (v.remove(p)) {
			lesObjetsDataProperty.put(p.getNom(), v);
			return true;
		}
		return false;
	}

	public boolean addObjetOwlProperty(OwlProperty p) {
		// il peut y avoir plusieurs propri�t�s de m�me nom
		Vector<OwlProperty> v = new Vector<OwlProperty>();
		if (lesObjetsOwlProperty.containsKey(p.getNom())) {
			v = (Vector<OwlProperty>) lesObjetsOwlProperty.get(p.getNom());
			if (v.contains(p)) {
				return false;
			}
		}
		v.addElement(p);
		lesObjetsOwlProperty.put(p.getNom(), v);
		return true;
	}

	public boolean removeObjetOwlProperty(OwlProperty p) {
		Vector<OwlProperty> v = (Vector<OwlProperty>) lesObjetsOwlProperty
				.get(p.getNom());
		if (v.remove(p)) {
			lesObjetsOwlProperty.put(p.getNom(), v);
			return true;
		}
		return false;
	}

	public boolean addObjetOwlClass(OwlClass uneCl) {
		if (this.lesObjetsOwlClasses.contains(uneCl)) {
			// test si le couple nom, classe existe
			if (this.lesObjetsOwlClasses.containsKey(uneCl.getNom())) {
				return false;
			} else {
				this.lesObjetsOwlClasses.remove(uneCl);
			}
		}

		// System.err.println("trace addObb "+uneCl.getNom());
		this.lesObjetsOwlClasses.put(uneCl.getNom(), uneCl);
		return true;
	}

	public boolean removeObjetOwlClass(OwlClass uneCl) {
		if (!this.lesObjetsOwlClasses.contains(uneCl)) {
			return false;
		}
		this.lesObjetsOwlClasses.remove(uneCl);
		return true;
	}

	// public Propri�t� creatPropri�t�(String nomC,String nomR,String nomCV) {
	// System.err.println("creatPropri�t� "+nomC);
	// Concept dom = this.qc(nomC);
	// if(dom == null)
	// {
	// JOptionPane.showMessageDialog(null,"domaine "+nomC+" n'existe pas");
	// return null;
	// }
	// Concept val = this.qc(nomCV);
	// if(val == null)
	// {
	// JOptionPane.showMessageDialog(null,"valeur "+nomCV+" n'existe pas");
	// return null;
	// }
	// Propri�t� p= new Propri�t�(nomR,dom,val,RSOwl.kbEnCours);
	// System.err.println("trace "+p);
	// if(this.addObjetPropri�t�(p))
	// {
	// return p;
	// }
	// return null;
	// }
	// public Propri�t�Valeur creatPropri�t�Valeur(String nomC,String
	// nomR,String nomCV) {
	// System.err.println("creatPropri�t� "+nomC);
	// Concept dom = this.qc(nomC);
	// if(dom == null)
	// {
	// JOptionPane.showMessageDialog(null,"domaine "+nomC+" n'existe pas");
	// return null;
	// }
	// ConceptInd val = this.qcInd(nomCV);
	// if(val == null)
	// {
	// JOptionPane.showMessageDialog(null,"valeur "+nomCV+" n'existe pas");
	// return null;
	// }
	// Propri�t�Valeur p= new Propri�t�Valeur(nomR,dom,val,RSOwl.kbEnCours);
	// System.err.println("trace "+p);
	// if(this.addObjetPropri�t�Valeur(p))
	// {
	// return p;
	// }
	// return null;
	// }
	public OwlProperty qPOwl(String nom, String nomDom, String nomRange) {
		Object obj = this.lesObjetsOwlProperty.get(nom);
		if (obj == null) {
			return null;
		}
		Vector v = (Vector) obj;
		if (v.size() == 1) {
			return (OwlProperty) v.elementAt(0);
		} else {

			for (int i = 0; i < v.size(); i++) {
				OwlProperty uneOwlP = (OwlProperty) v.elementAt(i);
				if (uneOwlP.getNom().equals(nom)
						&& uneOwlP.getDomaine().equals(nomDom)
						&& uneOwlP.getRange().equals(nomRange)) {
					return uneOwlP;
				}
			}
		}
		return null;
	}

	public OwlProperty qPOwl(String nom) {
		Object obj = this.lesObjetsOwlProperty.get(nom);
		if (obj == null) {
			return null;
		}
		Vector v = (Vector) obj;
		if (v.size() == 1) {
			return (OwlProperty) v.elementAt(0);
		} else {
			// System.err.println("trace erreur pb recherche de la sous
			// propri�t� "+nom+" plusieurs propri�t�s de m�me nom ");
			// for(int i= 0 ; i< v.size(); i++){
			// System.err.println("trace propri�t� "+v.elementAt(i));
			// }
			// on prend celle qui n'est pas anonyme
			int i = 0;
			while (i < v.size()) {
				if (((OwlProperty) v.elementAt(i)).getDomaine() != null
						&& !(((OwlProperty) v.elementAt(i)).getDomaine()
								.startsWith("anonyme"))) {
					return (OwlProperty) v.elementAt(i);
				}
				i++;
			}
		}
		return null;
	}

	public Vector<OwlPropertyValeur> qPVOwl(String nom) {
		return (Vector<OwlPropertyValeur>) this.lesObjetsOwlPropertyValeur
				.get(nom);
	}

	/*
	 * renvoie la classe owl de nom nom *
	 */
	public OwlClass qCOwl(String nom) {
		return (OwlClass) this.lesObjetsOwlClasses.get(nom);
	}

	// public boolean addObjetPropri�t�Valeur (Propri�t�Valeur p){
	// if(lesObjetsDataProperty.contains(p))
	// return false;
	// lesObjetsDataProperty.put(p.getNom(),p);
	// RoleInd r = new RoleInd(p);
	//
	// this.addObjetRoleInd(r);
	// if(fenKB!= null) fenKB.afficheCaracPropri�t�Valeur(p);
	// return true;
	// }
	// public Propri�t�Valeur qPVal(String nom){
	// boolean trouve = false;
	// Enumeration e= this.lesObjetsDataProperty.keys();
	// Propri�t�Valeur uneP = null;
	// while((!trouve ) &&e.hasMoreElements())
	// {
	// String str=(String)e.nextElement();
	// if(str.equals(nom))
	// {
	// trouve = true;
	// uneP =(Propri�t�Valeur) lesObjetsPropri�t�s.get(str);
	// }
	// }
	// return uneP;
	// }
	/**
	 * cr�ation d'un concept gen � partir d'une d�finition Owl lesPeres: ense de
	 * noms de concepts peres
	 */
	public Concept creatConceptGen(String nomConcept, TreeSet lesPeres,
			OwlClass unClNeg, String dimDef, String dimStruct, String dimLing,
			String commentaire, String auteur, String valide, String terme,
			Vector<String> vectSyn) {
		// test si les peres existent
		TreeSet<Concept> trConcPeres = new TreeSet<Concept>();
		// TreeSet trConcNeg = new TreeSet();
		// System.err.println("trace nomConc KBOwl "+nomConcept);

		for (Iterator i = lesPeres.iterator(); i.hasNext();) {
			// String unPere = (String)i.next(); //cas lecture par jdom
			Object obj = i.next();
			// System.err.println("trace instance de
			// "+obj.getClass().getName());
			Concept concPere = null;
			String nomPere = null;

			if (obj instanceof OwlClass) {
				OwlClass unPere = (OwlClass) obj;
				// System.err.println("trace KBOwl unPere "+unPere);
				concPere = this.qc(unPere.getNom());
				nomPere = unPere.getNom();
			} else {
				if (obj instanceof Concept) {
					concPere = (Concept) obj;
					nomPere = concPere.getNom();
				}
			}

			if (concPere == null) {
				// le pere n'existe pas
				String tabFilsPere = new String("pere\t"); // relation pere
															// -fils -pere
				tabFilsPere += nomConcept + "\t";
				tabFilsPere += nomPere;
				// System.err.println("trace sauvMereFille nomPere "+nomPere+ "
				// nom fille "+nomConcept);
				sauvMereFille.add(tabFilsPere);
			} else {
				trConcPeres.add(concPere);
			}
		}
		if (trConcPeres.isEmpty()) {
			// on le met sous topConcept
			trConcPeres.add(this.qc(TopConcept));
		}
		Concept concNeg = null;
		if (unClNeg != null) {
			String nomConcNeg = unClNeg.getNom();
			// System.out.println("KBOwl Concept complementOf "+nomConcNeg);
			concNeg = this.qc(nomConcNeg);
			if (concNeg == null) {
				// le complement n'existe pas encore
				String strComp = new String("complementaireDe\t"); // relation
																	// pere
																	// -fils
																	// -pere
				strComp += nomConcept + "\t";
				strComp += nomConcNeg;
				// System.err.println("trace sauvMereFille nomPere "+nomPere+ "
				// nom fille "+nomConcept);
				sauvMereFille.add(strComp);
			}
		}

		Concept c = new Concept(this, nomConcept, trConcPeres, concNeg, dimDef,
				dimStruct, dimLing, commentaire, auteur, valide, terme, vectSyn);
		if (c != null) {
			this.addObjetConc(c);
			return c;
		}
		return null;

	}

	/**
	 * cr�ation d'un concept gen enum � partir d'une d�finition Owl lesPeres:
	 * ense de noms de concepts peres
	 */
	public ConceptEnumere creatConceptGenEnum(String nomConcept,
			TreeSet lesPeres, OwlClass unClNeg, String dimDef,
			String dimStruct, String dimLing, String commentaire,
			String auteur, String valide, String terme, Vector<String> vectSyn,
			Vector<ConceptInd> ensInd) {
		// test si les peres existent

		TreeSet<Concept> trConcPeres = new TreeSet<Concept>();
		// TreeSet trConcNeg = new TreeSet();
		// System.err.println("trace nomConc KBOwl "+nomConcept);

		for (Iterator i = lesPeres.iterator(); i.hasNext();) {
			// String unPere = (String)i.next(); //cas lecture par jdom
			Object obj = i.next();
			// System.err.println("trace instance de
			// "+obj.getClass().getName());
			Concept concPere = null;
			String nomPere = null;

			if (obj instanceof OwlClass) {
				OwlClass unPere = (OwlClass) obj;
				// System.err.println("trace KBOwl unPere "+unPere);
				concPere = this.qc(unPere.getNom());
				nomPere = unPere.getNom();
			} else {
				if (obj instanceof Concept) {
					concPere = (Concept) obj;
					nomPere = concPere.getNom();
				}
			}

			if (concPere == null) {
				// le pere n'existe pas
				String tabFilsPere = new String("pere\t"); // relation pere
															// -fils -pere
				tabFilsPere += nomConcept + "\t";
				tabFilsPere += nomPere;
				// System.err.println("trace sauvMereFille nomPere "+nomPere+ "
				// nom fille "+nomConcept);
				sauvMereFille.add(tabFilsPere);
			} else {
				trConcPeres.add(concPere);
			}
		}
		if (trConcPeres.isEmpty()) {
			// on le met sous topConcept
			trConcPeres.add(this.qc(TopConcept));
		}
		Concept concNeg = null;
		if (unClNeg != null) {
			String nomConcNeg = unClNeg.getNom();
			// System.out.println("KBOwl Concept complementOf "+nomConcNeg);
			concNeg = this.qc(nomConcNeg);
			if (concNeg == null) {
				// le complement n'existe pas encore
				String strComp = new String("complementaireDe\t"); // relation
																	// pere
																	// -fils
																	// -pere
				strComp += nomConcept + "\t";
				strComp += nomConcNeg;
				// System.err.println("trace sauvMereFille nomPere "+nomPere+ "
				// nom fille "+nomConcept);
				sauvMereFille.add(strComp);
			}
		}

		ConceptEnumere c = new ConceptEnumere(this, nomConcept, trConcPeres,
				concNeg, dimDef, dimStruct, dimLing, commentaire, auteur,
				valide, terme, vectSyn, ensInd);
		if (c != null) {
			this.addObjetConc(c);
			return c;
		}
		return null;

	}

	public ConceptInd creatConceptInd(String nomConcept, String unPere,
			String valide) {

		// test si les peres existent
		TreeSet<Concept> trConcPeres = new TreeSet<Concept>();

		Concept conc = this.qc(unPere);
		if (conc == null) {
			// le pere n'existe pas
			String tabFilsPere = new String("pere\t"); // relation pere -fils
														// -pere
			tabFilsPere += nomConcept + "\t";
			tabFilsPere += unPere;
			// System.err.println("trace sauvXml concept ind nomPere "+unPere+ "
			// fils "+nomConcept);
			sauvMereFille.add(tabFilsPere);
		} else {
			trConcPeres.add(conc);
		}

		ConceptInd c = new ConceptInd(this, nomConcept, trConcPeres, valide);
		if (c != null) {
			this.addObjetConcInd(c);
			return c;
		}
		return null;
	}

	// public boolean addObjetPropri�t� (Propri�t� p){
	// if(lesObjetsPropri�t�s.contains(p))
	// return false;
	// lesObjetsPropri�t�s.put(p.getNom(),p);
	// Role r = new Role(p);

	// this.addObjetRole(r);
	// if(fenKB!=null) fenKB.afficheCaracPropri�t�(p);
	// return true;
	// }
	/* pour acc�der a ensowlclass */
	/**
	 * renvoie null si la classe n'existe pas
	 */
	public OwlClass getEnsOwlClass(String uneClasse) {

		return (OwlClass) lesObjetsOwlClasses.get(uneClasse);
	}

	public void dumpOwl() {
		try {
			File f = new File("dumpOwlOnto");
			FileWriter fr = new FileWriter(f);
			BufferedWriter br = new BufferedWriter(fr);
			for (Enumeration e = this.lesObjetsOwlClasses.keys(); e
					.hasMoreElements();) {
				String nom = (String) e.nextElement();
				OwlClass uneCl = (OwlClass) this.lesObjetsOwlClasses.get(nom);
				br.write("" + uneCl);
			}
			for (Enumeration e = this.lesObjetsOwlProperty.keys(); e
					.hasMoreElements();) {
				String nom = (String) e.nextElement();
				Vector v = (Vector) this.lesObjetsOwlProperty.get(nom);
				for (int i = 0; i < v.size(); i++) {
					OwlProperty uneOwlProp = (OwlProperty) v.elementAt(i);
					br.write("propri�t� owl: \n" + uneOwlProp + "\n");
				}
			}
			for (Enumeration e = this.lesObjetsOwlPropertyValeur.keys(); e
					.hasMoreElements();) {
				String nom = (String) e.nextElement();
				Vector<OwlPropertyValeur> vOwlProp = (Vector<OwlPropertyValeur>) this.lesObjetsOwlPropertyValeur
						.get(nom);
				for (int i = 0; i < vOwlProp.size(); i++) {
					br.write("propri�t� owl valeur: \n" + vOwlProp.elementAt(i)
							+ "\n");
				}
			}
			for (Enumeration e = this.lesObjetsDataProperty.keys(); e
					.hasMoreElements();) {
				String nom = (String) e.nextElement();
				br.write("datatype owl: " + nom + "\n");
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			System.err.println("erreur dump " + e);
		}
	}

	/** ************************************************************** */
	/*
	 * cr�ation de la KB � partir des objets OWL
	 */
	/**
	 * cr�ation de l'ent�te d'une ontologie import�e de owl
	 */
	public void creatTopOntoOwl() {
		// cr�ation de top
		Concept cTop = this.creatConceptFich(this.getTopConcept(),
				DimLinguistique.NT, DimStructurelle.TDS, DimDefinitionnelle.ND,
				new Vector(), "", VarGlobales.resUtilss.getString("En_cours"));

	}

	public void creatOntoOwl() {
		this.creatTopOntoOwl();
		// cr�ation des classes
		this.creatLesClassesOwl();
		// cr�ation des propri�tes
		this.creatLesObjectProperty();
		this.creatLesDataProperty();

		this.fenKB.afficheListeTousLesRoles();

	}

	/***************************************************************************
	 * cr�ation des classes Owl par des concepts
	 */
	public void creatLesClassesOwl() {

		Iterator<OwlClass> e = this.lesObjetsOwlClasses.values().iterator();
		while (e.hasNext()) {
			Object obj = e.next();
			// System.err.println("trace classe "+obj);
			OwlClass uneCl = null;
			uneCl = (OwlClass) obj;

			// cr�ation d'un concept � partir de la classe OWl
			String dimDef = DimDefinitionnelle.ND;
			String dimStruct = DimStructurelle.TDS;
			String dimLing = DimLinguistique.NT;
			if (this.qc(uneCl.getNom()) == null) {
				if (!(uneCl.getBenum())) {
					Concept conc = this.creatConceptGen(uneCl.getNom(), uneCl
							.getEnsMeres(), uneCl.getComplementOf(), dimDef,
							dimStruct, dimLing, uneCl.getComment(),
							VarGlobales.auteur, VarGlobales.resUtilss
									.getString("En_cours"), "",
							new Vector<String>());
					// test si il y a un �quivalent
					if (uneCl.getEquivalentOf() != null) {
						conc.setEquivalent(uneCl.getEquivalentOf().getNom());
					}
					// cr�ation des individus
					Vector ensInd = uneCl.getEnsInd();
					if (!(ensInd.isEmpty())) {
						for (int i = 0; i < ensInd.size(); i++) {
							String nomInd = (String) ensInd.elementAt(i);
							this
									.creatConceptInd(nomInd, conc.getNom(),
											VarGlobales.resUtilss
													.getString("En_cours"));
						}
					}
				} else {
					// concept enum�r�
					// cr�ation des individus
					Vector ensInd = uneCl.getEnsInd();
					Vector<ConceptInd> ensConcInd = new Vector<ConceptInd>();
					if (!(ensInd.isEmpty())) {
						for (int i = 0; i < ensInd.size(); i++) {
							String nomInd = (String) ensInd.elementAt(i);
							ConceptInd ci = this.creatConceptInd(nomInd, uneCl
									.getNom(), VarGlobales.resUtilss
									.getString("En_cours"));
							ensConcInd.addElement(ci);
						}
					}

					ConceptEnumere conc = this.creatConceptGenEnum(uneCl
							.getNom(), uneCl.getEnsMeres(), uneCl
							.getComplementOf(), dimDef, dimStruct, dimLing,
							uneCl.getComment(), VarGlobales.auteur,
							VarGlobales.resUtilss.getString("En_cours"), "",
							new Vector<String>(), ensConcInd);
				}
			}
		} // fin while
		try {

			int i = 0;
			while (i < sauvMereFille.size()) {
				String tabFilsPere = (String) sauvMereFille.elementAt(i);
				StringTokenizer st = new StringTokenizer(tabFilsPere, "\t");
				String lien = st.nextToken();
				if (lien.equals("pere")) {
					String fils = st.nextToken();
					String pere = st.nextToken();
					Concept unPere = this.qc(pere);
					if (unPere == null) {
						// System.err.println("A trace i,"+i+" "+pere+" "
						// +fils);
						throw new Exception(VarGlobales.resTerm
								.getString("erreur_dans_le")
								+ pere
								+ VarGlobales.resTerm
										.getString("nest_pas_d_fini"));
					} else {
						if (this.isGenC(fils)) {
							Concept unFils = this.qc(fils);
							if (unFils == null) {
								// System.err.println("B trace i,"+i+" "+pere+"
								// " +fils);
								throw new Exception(VarGlobales.resTerm
										.getString("erreur_dans_le")
										+ fils
										+ VarGlobales.resTerm
												.getString("nest_pas_d_fini"));
							}
							// System.err.println("trace un fils
							// "+unFils.getNom()+"trace un
							// pere"+unPere.getNom());
							ConceptAbstrait cTop = RSOwl.kbOwlEnCours
									.qc(TopConcept);
							if (unFils.getLesPeres().contains(cTop)) {
								unFils.suppUnPere(cTop);
								((Concept) cTop).suppUnFils(unFils);
							}
							unFils.ajoutUnPere(unPere);
						} else {
							ConceptInd unFils = this.qcInd(fils);
							if (unFils == null) {
								// System.err.println("C trace i,"+i+"pere
								// "+pere+" fils " +fils);
								throw new Exception(VarGlobales.resTerm
										.getString("erreur_dans_le")
										+ fils
										+ VarGlobales.resTerm
												.getString("nest_pas_d_fini"));
								// temp.add(tabFilsPere);
							} else {
								// System.err.println("trace un fils
								// "+unFils.getNom()+"trace un
								// pere"+unPere.getNom());
								unFils.ajoutUnPere(unPere);
							}
						}

					}
				} else {
					if (lien.equals("restreint")) {
						String nomR = st.nextToken();
						String nomRD = st.nextToken();
						String nomRR = st.nextToken();
						String nomRestr = st.nextToken();
						String nomRestrD = st.nextToken();
						String nomRestrR = st.nextToken();
						Role r = this.qr(nomR, nomRD, nomRR);
						Role rRestr = this.qr(nomRestr, nomRestrD, nomRestrR);
						if (r == null) {
							System.err.println("erreur role r " + nomR
									+ " n'est pas retrouv�");
						} else {
							if (rRestr == null) {
								System.err.println("erreur role restr "
										+ nomRestr + " n'est pas retrouv�");
							} else {
								if (!(r.peutRestreindre(rRestr))) {
									String com = r.getComment();
									if (com == null) {
										com = "";
									}
									com = com + OwlConstants.sousPropDe
											+ nomRestr;
								} else {
									r.setRestreint(rRestr);
								}
							}
						}
					} else {
						// lien inverseof
						if (lien.equals("inverseOf")) {
							System.err.println("a traiter");
						} else {
							// lien complementaireDE
							String c1 = st.nextToken();
							String notC1 = st.nextToken();
							Concept unC = RSOwl.kbOwlEnCours.qc(c1);
							Concept unNegC = RSOwl.kbOwlEnCours.qc(notC1);
							unC.addUnNeg(unNegC);
							unNegC.addUnNeg(unC);
						}
					}
				}
				i++;
			} // fin while
		} catch (Exception ex) {
			System.err.println("erreur " + ex);
		}

	}

	public void creatLesObjectProperty() {
		// System.err.println("trace cr�ation des propri�t�s ");
		Iterator i = lesObjetsOwlProperty.values().iterator();
		while (i.hasNext()) {
			Vector v = (Vector) i.next();
			for (int j = 0; j < v.size(); j++) {
				OwlProperty uneOwlP = (OwlProperty) v.elementAt(j);
				String nomRole = uneOwlP.getNom();
				// System.err.println("trace creatLesObjectProperty "+nomRole);
				String nomDom = uneOwlP.getDomaine();

				if (nomDom == null) {
					// on met un nom par d�faut concptPropri�t�
					nomDom = OwlConstants.EnsProprietes;
				}
				String nomRange = uneOwlP.getRange();

				OwlProperty superP = uneOwlP.getSuperProp();
				Vector<Boolean> ensProp = new Vector<Boolean>();
				// dans l'ordre transitive, symmetrique, fonctionnelle
				ensProp.add(new Boolean(uneOwlP.getTransitive()));
				ensProp.add(new Boolean(uneOwlP.getSymetrique()));

				ensProp.add(new Boolean(uneOwlP.isFonctionnelle()));
				String commentaire = "";
				int cMin = Integer.parseInt(uneOwlP.getCardMin());
				int cMax = Integer.parseInt(uneOwlP.getCardMax());
				// System.err.println("trace cMin"+cMin+" cMax "+cMax);
				if (nomRange == null) {
					// j'interpr�te en supposant qu'un r�le de m�me nom
					// a �t� cr��- le r�le � cr�er doit le restreindre
					// en cardinalit�
					Role r = this.qr(nomRole, nomDom);
					if (r != null) {
						nomRange = r.getConcVal().getNom();

					} else {
						nomRange = OwlConstants.EnsProprietes; // valeur par
																// d�faut
					}
					try {
						// cr�ation d'un r�le restreint de r en cardinalit�

						this.creatRoleGenConcGen(nomRole, nomDom, nomRange, "",
								r, false, cMin, cMax, ensProp, "",
								VarGlobales.resUtilss.getString("En_cours"),
								VarGlobales.auteur, "");
					} catch (Exception e) {
						System.err.println("erreur cr�ation d'un role gen "
								+ nomRole + " � partir d'une prop " + e);
						// e.printStackTrace();
						// JOptionPane.showMessageDialog(null,"erreur cr�ation
						// d'un role gen � partir d'une prop A " );
					}
				} else {
					Role r = null;
					if (this.qr(nomRole, nomDom, nomRange) == null) {
						// cr�ation du r�le
						try {
							// System.err.println("trace prop nom role 1
							// "+nomRole+" nomDom "+nomDom+" range
							// "+nomRange+ensProp);
							r = this.creatRoleGenConcGen(nomRole, nomDom,
									nomRange, "", null, true, cMin, cMax,
									ensProp, commentaire, VarGlobales.resUtilss
											.getString("En_cours"),
									VarGlobales.auteur, "");

							// cr�ation du lien de hi�rarchie �ventuellement
							Role restr = null;

							if (superP != null) {
								// le r�le est restreint
								String nomRestr = superP.getNom();
								String nomRestrD = superP.getDomaine();
								String nomRestrR = superP.getRange();

								restr = this.qr(nomRestr, nomRestrD, nomRestrR);
								// si restr == null sauvegarder l'info dans
								// sauvMereFille
								if (restr == null) {

									String tabFilsPere = new String(
											"restreint\t"); // relation pere
															// -fils -pere
									tabFilsPere += nomRole + "\t" + nomDom
											+ "\t" + nomRange + "\t";
									tabFilsPere += nomRestr + "\t" + nomRestrD
											+ "\t" + nomRestrR;
									// System.err.println("trace sauvMereFille
									// nomPere "+nomPere+ " nom fille
									// "+nomConcept);
									sauvMereFille.add(tabFilsPere);
								} else {
									/*
									 * si les domaines sont identiques ou si les
									 * range sont identiques pas de restriction
									 * possible on garde l'info en commentaire
									 */

									if (!(r.peutRestreindre(restr))) {

										commentaire = commentaire
												+ OwlConstants.sousPropDe
												+ nomRestr;
										r.setComment(commentaire);
									} else {

										r.setRestreint(restr);
									}
								}
							}
							// traitement de l'inverse
							if (uneOwlP.getInverseOf() != null) {
								// System.err.println("trace KBOwl inverseof
								// role "+r+" inverse "+uneOwlP.getInverseOf());
								r.setInverse(uneOwlP.getInverseOf());
							}

						} catch (Exception e) {
							System.err.println("erreur cr�ation d'un role gen "
									+ nomRole + " � partir d'une prop " + e);
							// e.printStackTrace();
							// JOptionPane.showMessageDialog(null,"erreur
							// cr�ation d'un role gen � partir d'une prop A " );
						}

					}
				} // fin else
			} // for
		} // fin while
	}

	public void creatLesDataProperty() {

		Iterator i = lesObjetsDataProperty.values().iterator();
		while (i.hasNext()) {
			Vector v = (Vector) i.next();
			for (int j = 0; j < v.size(); j++) {
				OwlDataProperty uneDataP = (OwlDataProperty) v.elementAt(j);
				// System.err.println("trace prop "+uneOwlP.getNom());
				String nomRole = uneDataP.getNom();
				String nomDom = uneDataP.getDomaine();
				String nomRange = uneDataP.getDatatype();
				int cMin = 0;
				int cMax = 100;

				if (this.qr(nomRole, nomDom, nomRange) == null) {
					try {
						Vector<Boolean> ensProp = new Vector<Boolean>();
						// //pas transitive, ni sym�trique, ni fonctionnelle
						ensProp.add(new Boolean(false));
						ensProp.add(new Boolean(false));
						ensProp.add(new Boolean(false));
						Role r = this.creatRoleGenConcGen(nomRole, nomDom,
								nomRange, "", null, true, cMin, cMax, ensProp,
								"",
								VarGlobales.resUtilss.getString("En_cours"),
								VarGlobales.auteur, "");
						// System.err.println("trace cr�ation d'un r�le r
						// "+r.getNom());
					} catch (Exception e) {
						System.err.println("erreur cr�ation d'un role gen "
								+ nomRole + "  � partir d'une prop " + e);
						e.printStackTrace();
					}
				}

			}
		} // fin while
	} // fin methode

	public OwlDataProperty getDataProperty(String nom) {
		Vector v = (Vector) this.lesObjetsDataProperty.get(nom);
		if (v != null) {
			int i = 0;
			while (i < v.size()) {
				if (((OwlDataProperty) v.elementAt(i)).getNom().equals(nom)) {
					return (OwlDataProperty) v.elementAt(i);
				}
			}
		}
		return null;
	}

	public static void initVarGlobales() {
		sauvMereFille = new Vector<String>();
	}
} // fin classe
