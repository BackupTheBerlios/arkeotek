package arkeotek.io.exporter.OWL;

public class OwlDataProperty extends OwlPropertyAbs{
  private String datatype;

  public OwlDataProperty(String nom, String domaine,String datatype) {
    super(nom, domaine);
    this.datatype = datatype;
		RSOwl.kbOwlEnCours.addObjetDataProperty(this);
  }
	public String getDatatype(){
		return this.datatype;
	}
  public String toString(){
		return super.toString()+ "datatype "+datatype;
	}
}