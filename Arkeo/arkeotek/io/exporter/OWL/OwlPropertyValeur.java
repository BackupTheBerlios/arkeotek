package arkeotek.io.exporter.OWL;


public class OwlPropertyValeur extends OwlPropertyAbs {

  private String range; //le range  correspondant
//  private RoleInd roleInd ; //le rôle individuel correspondant

  public OwlPropertyValeur(String nom) {
    super(nom);
  }

  public OwlPropertyValeur(String nom, String range) {
    super(nom);
    this.range = range;
  }
  public OwlPropertyValeur(String nom, String dom,String range) {
    super(nom,dom);
    this.range = range;
  }
  public String getRange(){
    return this.range;
  }
  public void setRange(String range){
    this.range = range;
  }
}
