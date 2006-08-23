package arkeotek.io.exporter.OWL;

import java.util.*;

public class OwlClassEnum extends OwlClass {

  public OwlClassEnum() {
    super();
  }
 public OwlClassEnum(String nom){
   super(nom);
   //il faut mettre la nouvelle classe dans l'ensemble des classes
   //suppression de la classe créée éventuellemnt en OwlClass
   RSOwl.kbOwlEnCours.removeObjetOwlClass(this);
   RSOwl.kbOwlEnCours.addObjetOwlClass(this);
 }
 public OwlClassEnum(String nom, TreeSet<OwlClass> ensMeres, OwlClass complementOf,
                  Vector<OwlClass> disjointWith,
                  OwlClass equivalentOf,
                  OwlClass[] intersectionOf,
                  Vector<OwlClass> ensOwlClassUnionOf,
                  String comment, Vector<String> ensInd){
   super(nom,ensMeres, complementOf, disjointWith, equivalentOf, intersectionOf, ensOwlClassUnionOf,comment, ensInd);
   //il faut mettre la nouvelle classe dans l'ensemble des classes
   //suppression de la classe créée éventuellemnt en OwlClass
   RSOwl.kbOwlEnCours.removeObjetOwlClass(this);
   RSOwl.kbOwlEnCours.addObjetOwlClass(this);
 }
  public OwlClassEnum(OwlClassEnum uneCl){
    super(uneCl);
    //il faut mettre la nouvelle classe dans l'ensemble des classes
   //suppression de la classe créée éventuellemnt en OwlClass
   RSOwl.kbOwlEnCours.removeObjetOwlClass(this);
   RSOwl.kbOwlEnCours.addObjetOwlClass(this);
  }
  public OwlClassEnum(OwlClass uneCl){
   super(uneCl);
   //il faut mettre la nouvelle classe dans l'ensemble des classes
   //suppression de la classe créée éventuellemnt en OwlClass
   RSOwl.kbOwlEnCours.removeObjetOwlClass(this);
   RSOwl.kbOwlEnCours.addObjetOwlClass(this);
  }
}
