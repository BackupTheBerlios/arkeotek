package arkeotek.io.exporter.OWL;


import java.util.*;
/**
 * <p>Titre : Terminae</p>
 * <p>Description : Plateforme d'aide � la cr�ation d'ontologies � partir de textes</p>
 * <p>Copyright : Copyright (c) 2003</p>
 * <p>Soci�t� : LIPN</p>
 * @author Szulman & Bi�bow
 * @version 8.0
 * d�finition d'une classe d�finissant une propri�t� OWL
 * contient les caract�ristiques communes � tous les types de propri�t�s
 */

public abstract class OwlPropertyAbs {
  protected String nom;
  protected String domaine;
	protected Vector ensSyn = new Vector(); //on met la dedans les propri�t�s synonymes


  public OwlPropertyAbs() {
  }
  public OwlPropertyAbs(String nom){
    this.nom = nom;
  }
  public OwlPropertyAbs(String nom, String domaine){
    this.nom = nom;
    this.domaine = domaine;
  }
  //services
  public String getNom(){
    return nom;
  }
  public String getDomaine(){
    return domaine;
  }
  public void setNom(String nom){
   this.nom =nom;
  }
  public void setDomaine(String domaine){
    this.domaine = domaine;
  }

  public boolean equals(Object obj){
   if(obj == null) return false;
   if(!(obj instanceof OwlPropertyAbs))
     return false;
   return nom.equals((OwlPropertyAbs)obj) && domaine.equals((OwlPropertyAbs)obj);
  }
  public String toString(){
    return "nom "+nom+" domaine "+domaine;
  }
	public void addSyn(String nomSyn){
		ensSyn.addElement(nomSyn);
	}
}