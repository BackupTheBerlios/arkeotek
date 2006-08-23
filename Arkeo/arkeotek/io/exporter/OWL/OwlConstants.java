package arkeotek.io.exporter.OWL;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.jdom.Document;
import org.jdom.*;


/*
   contantes pour utiliser dans Owl
 */
public interface OwlConstants
{
    public static String TopConcept = "Thing"; //topconcept
    public static String DataType = "ConceptDataType"; // la classe associée ux xml schema
    public static String ConceptAnonyme = "ConceptAnomyme"; //regroupe les concepts anonymes
    public static String EnsProprietes = "ConceptPropriété"; //ens des propriétés avec domain non défini
    public static String ComplementaireConc = "ConceptComplémentaire"; //regroupe les concepts complémentaires
    public static String UnionOfConc = "ConceptUnion"; //regroupe les concepts unionOf
    public static String unionOf = "unionOf";
    public static String sousPropDe = " = sous_propriété_de_";
    public static String anonyme = "anonyme";
    public static String disjointWith = "= disjointWith";
    public static String intersectionOf = "intersectionOf";
    public static String complementOf ="complementOf";
    public static String subClassOf = "subClassOf";
    public static String restriction = "Restriction";
    public static String onProperty = "onProperty";
    public static String resource = "resource";
    public static String allValuesFrom = "allValuesFrom";
    public static String someValuesFrom = "someValuesFrom";
    public static String classe = "Class";
    public static String label = "label";
    public static String lang = "lang";
    public static String subPropertyOf = "subPropertyOf";
    public static String objectProperty = "ObjectProperty";
    public static String domain = "domain";
    public static String range = "range";
    public static String dataProperty = "DatatypeProperty";
    public static String separateurNom = "#";
    public static String minCard = "minCardinality";
    public static String maxCard = "maxCardinality";
    public static String inverseOf = "inverseOf";
    public static String functionalProperty = "FunctionalProperty";
    public static String symetricProperty = "SymmetricProperty";
    public static String transitiveProperty = "TransitiveProperty";
//  public static String IntersectionOfConc = "ConceptIntersection"; //regroupe les concepts intersectionOf
    public static Namespace espNomOwl = Namespace.getNamespace("owl","http://www.w3.org/2002/07/owl#");
    public static Namespace espNomRdf = Namespace.getNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    public static Namespace espNomRdfs = Namespace.getNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
    public static Namespace espNomArkeotek = Namespace.getNamespace("arkeotek","http://www.irit.fr");
    public static Namespace espNomXMLSchema = Namespace.getNamespace("xsd","http://www.w3.org/2000/10/XMLSchema#");
    public static String owl = "owl";
}
