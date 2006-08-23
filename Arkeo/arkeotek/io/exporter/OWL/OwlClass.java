package arkeotek.io.exporter.OWL;

import java.util.*;

/**
 * <p>Titre : Terminae</p>
 * <p>Description : Plateforme d'aide à la création d'ontologies à partir de textes</p>
 * <p>Copyright : Copyright (c) 2004</p>
 * <p>Société : LIPN</p>
 * @author Szulman
 * @version 8.0
 * définition d'une classe représentant une classe en OWL
 */

public class OwlClass implements Comparable{
  private String nom; //nom de la classe;
  private TreeSet<OwlClass> ensMeres ; //ens des classes parents - définit par subClassOf

  // propriétés des classes OWL
  private OwlClass complementOf=null; //la classe complémentaire
  private Vector<OwlClass> disjointWith=null;   //la classe disjointe
  private OwlClass equivalentOf=null; //une classe équivalente
  private OwlClass[] intersectionOf=null; // classe décrite par l'intersection de 2 classes
  private Vector<OwlClass> ensOwlClassUnionOf = null;  //classe décrite par la réunion de classes
  private String comment=""; //le commentaire associé
  //ens des individus d'une classe
  private Vector<String> ensInd = null;
  //pour les classes enumérées
  private boolean benum = false; //distingue les classes enumérées si enum alors enum =true


  public OwlClass() {
    this.ensMeres = new TreeSet<OwlClass>();
    this.ensOwlClassUnionOf = new Vector<OwlClass>();
    this.ensInd = new Vector<String>();

		//pas d'insertion de l'objet sans nom
  }
  public OwlClass(String nom) {
    this.nom = nom;
   this.ensMeres = new TreeSet<OwlClass>();
   this.ensOwlClassUnionOf = new Vector<OwlClass>();
	 RSOwl.kbOwlEnCours.addObjetOwlClass(this);
   //pas d'insertion de l'objet sans nom
   this.ensInd = new Vector<String>();
  }
  public OwlClass(String nom, TreeSet<OwlClass> ensMeres, OwlClass complementOf,
                  Vector<OwlClass> disjointWith,
                  OwlClass equivalentOf,
                  OwlClass[] intersectionOf,
                  Vector<OwlClass> ensOwlClassUnionOf,
                  String comment, Vector<String> ensInd){
    if(nom.startsWith("#")) nom = nom.substring(1);
    this.nom = nom;
    this.ensMeres = new TreeSet<OwlClass> (ensMeres);
    if (complementOf != null) this.complementOf = new OwlClass(complementOf);
    this.disjointWith = disjointWith;
    this.equivalentOf = equivalentOf;
    this.intersectionOf = intersectionOf;
    this.ensOwlClassUnionOf = new Vector<OwlClass>(ensOwlClassUnionOf);
    this.comment = comment;
		RSOwl.kbOwlEnCours.addObjetOwlClass(this);
    this.ensInd = new Vector<String> (ensInd);
  }
  public OwlClass(OwlClass uneCl){
    this(uneCl.getNom(),uneCl.getEnsMeres(),uneCl.getComplementOf(),
         uneCl.getDisjointWith(),uneCl.getEquivalentOf(),uneCl.getIntersectionOf(),
         uneCl.getEnsOwlClassUnionOf(),uneCl.getComment(),uneCl.ensInd);
  }
  //services
  public String getNom(){
    return this.nom;
  }
  public void setNom(String nom){
    if(nom.startsWith("#")) nom = nom.substring(1);
    this.nom = nom;
//    System.err.println("trace modifie nom "+nom);
		//insertion lorsqu'on a le nom de la classe
		RSOwl.kbOwlEnCours.addObjetOwlClass(this);
  }
  public TreeSet<OwlClass> getEnsMeres(){
    return this.ensMeres;
  }
  public void setEnsMeres(TreeSet<OwlClass> ensM){
    this.ensMeres = new TreeSet<OwlClass>(ensM);
  }
  public OwlClass getComplementOf(){
    return this.complementOf;
  }
  public void setComplementOf(OwlClass ensCl){
    this.complementOf = new OwlClass(ensCl);
  }
//  public void addUnComplementOf(OwlClass uneCl){
//    this.complementOf.add(uneCl);
//  }

  public Vector<OwlClass> getDisjointWith(){
    return this.disjointWith;
  }
  public void addDisjointWith(OwlClass uneCl){
    if(this.disjointWith == null)
      this.disjointWith = new Vector<OwlClass>();
//    System.err.println("trace unecl "+uneCl.getNom());
    this.disjointWith.add(new OwlClass(uneCl));
  }

  public OwlClass getEquivalentOf(){
    return this.equivalentOf;
  }
  public void setEquivalentOf(OwlClass uneCl){
    this.equivalentOf = new OwlClass(uneCl);
  }
  public OwlClass[] getIntersectionOf(){
    return this.intersectionOf;
  }
  public void setIntersectionOf(OwlClass[] tabOwlClass){
    this.intersectionOf = tabOwlClass;
  }
  //initialisation du tableau pour l'intersection
  public void setIntersectionOf(){
    this.intersectionOf = new OwlClass[2];
  }
  public void addIntersectionOf(OwlClass uneCl){
    if(this.intersectionOf[0] == null)
    {
      //cas de la première classe
      this.intersectionOf[0] = uneCl;
    }
    else
    {
      this.intersectionOf[1] = uneCl;
    }
  }
  public Vector<OwlClass> getEnsOwlClassUnionOf(){
   return this.ensOwlClassUnionOf;
  }
  public void setEnsOwlClassUnionOf(Vector<OwlClass> vClass){
    this.ensOwlClassUnionOf = new Vector<OwlClass>(vClass);
  }
  public String getComment(){
    return comment;
  }
  public void setComment(String com){
    if(com !=null) this.comment = com;
    else
      this.comment = "";
  }
  public void addComment(String com){
    this.comment+="\n"+com;
  }
  //pour ajouter des meres
  public void addUneMere(OwlClass uneMere){
    this.ensMeres.add(new OwlClass(uneMere));
  }
  //pour les individus
  public Vector getEnsInd(){
    return this.ensInd;
  }
  public void setEnsInd(Vector<String> ens){
    this.ensInd = new Vector<String>(ens);
  }
  /* ajout d'un individu*/
  public void addUnInd(String nom){
    this.ensInd.addElement(nom);
  }
  public boolean equals(Object obj){
    if(obj == null) return false;
    if(!(obj instanceof OwlClass)) return false;
    return this.nom.equals(((OwlClass)obj).getNom());
  }
  public String toString(){
    //à compléter si nécessaire
    String meres="";
    if(this.ensMeres.isEmpty()) meres=meres+"pas d'ancetre";
    else
    {
    for(Iterator i= this.ensMeres.iterator(); i.hasNext();)
    {
      meres = meres + ((OwlClass)i.next()).getNom()+" ";
    }
    }
    //intersectionOf
    String inter="";
    if(this.intersectionOf!=null){
      inter = inter + "intersection de "+((OwlClass)this.intersectionOf[0]).getNom()+" "+((OwlClass)this.intersectionOf[1]).getNom();
    }
    String disjoint="";
    if(this.disjointWith !=null ){

      for(int i=0; i< this.disjointWith.size();i++)
      {
        disjoint = disjoint+" disjoint avec " + ((OwlClass)this.disjointWith.elementAt(i)).getNom()+" ";
      }
    }
    String complementDe ="";
    if(this.complementOf!=null){
     complementDe =complementDe+" complément de "+((OwlClass)this.complementOf).getNom()+" ";
    }
    String lesInds="";
    if(!(this.ensInd.isEmpty())){
      for(int i= 0; i< this.ensInd.size(); i++){
        lesInds = lesInds + this.ensInd.elementAt(i);
      }
    }
    return "\n owlClass nom:"+nom+" meres "+meres+"\n "+inter+" "+disjoint+
        " "+complementDe+" \n instances "+lesInds+"\n***********";
  }
  public int compareTo(Object obj)
     {
        if(obj==null) return 1;
        return ((OwlClass)obj).getNom().compareTo(this.nom);
      }
  public void setEnum(boolean b){
    this.benum =b;
  }
  public boolean getBenum(){
    return this.benum;

  }
}
