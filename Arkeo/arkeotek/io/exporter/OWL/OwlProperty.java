package arkeotek.io.exporter.OWL;



public class OwlProperty extends OwlPropertyAbs {
  private String range;
  protected String cardMin = "0";
  protected String cardMax = "100";  //valeur par défaut à revoir???
	private String inverseOf;
	private boolean symetrique = false;//par deafaut
	private String fonctionDe = null;
	private OwlProperty superProp; //la propriété mère
	private OwlProperty subsume; //la propriété subsumée
  private boolean transitive=false;

  public OwlProperty(String nom, String domaine, String range) {
    super(nom,domaine);
    this.range = range;
		RSOwl.kbOwlEnCours.addObjetOwlProperty (this);
  }
  public OwlProperty(String nom) {
    super(nom );
  RSOwl.kbOwlEnCours.addObjetOwlProperty (this);
  }
  public String getRange(){
    return range;
  }
  public void setRange(String range){
    this.range = range;
  }
  public String getCardMin(){
    return this.cardMin;
  }
  public void setCardMin(String card){
    this.cardMin = card;
  }
  public String getCardMax(){
    return this.cardMax;
  }
  public void setCardMax(String card){
    this.cardMax = card;
  }
	public String getInverseOf(){
		return inverseOf;
	}
	public void setInverseOf(String nomInverse){
		this.inverseOf = nomInverse;
	}
	public boolean getSymetrique(){
		return this.symetrique;
	}
	public void setSymetrique(boolean sym){
		this.symetrique = sym;
	}
	public String getFonctionDe(){
		return this.fonctionDe;
	}
	public void setFonctionDe(String nomFonction){
		this.fonctionDe = nomFonction;
	}
  public boolean isFonctionnelle(){
    return this.fonctionDe !=null;
  }
	public OwlProperty getSuperProp(){
		return this.superProp;
	}
	public void setSuperProp(OwlProperty uneProp){
		this.superProp = uneProp;
		uneProp.subsume = this;
	}
	public OwlProperty getSubsume(){
		return this.subsume;
	}
	public void setSubsume(OwlProperty uneProp){
		this.subsume = uneProp;
		uneProp.superProp = this;
	}
	public boolean getTransitive(){
		return transitive;
	}
	public void setTransitive(boolean val){
		this.transitive = val;
	}
  public boolean equals(Object obj){
   if(obj == null) return false;
   if(!(obj instanceof OwlProperty))
     return false;
   return super.equals(obj)&& range.equals(((OwlProperty)obj).range);
  }
  public String toString(){
    return super.toString()+" range "+range+" card min "+cardMin+"card Max "+cardMax;
  }
}