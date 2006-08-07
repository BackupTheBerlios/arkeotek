package arkeotek.io.exporter;

import java.util.ArrayList;
import java.util.HashMap;

import org.semanticweb.owl.model.OWLOntology;

import arkeotek.io.importer.AbstractParser;
import arkeotek.ontology.Ontology;

/**
 * 
 * @author Hubert Nouhen
 * 
 * Classe Abstraite pour l'exportation
 * Abstract Class for the exportation
 *
 */

public abstract class AbstractExporter
{
	private Ontology owner;
	private ArrayList<AbstractParser> parsers;
	private String file;
	private HashMap<Integer, HashMap<Object, Object>> results;
	
	
	public AbstractExporter(Ontology owner, String file_path)
	{
		this.owner = owner;
		this.file = file_path;
		this.parsers = new ArrayList<AbstractParser>();
	}


	public void performExport() throws Exception
	{
		this.results = new HashMap<Integer, HashMap<Object, Object>>(this.parsers.size());
		
	}
}
