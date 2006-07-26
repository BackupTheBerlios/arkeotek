/**
 * Created on 4 mai 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.db;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import ontologyEditor.ApplicationManager;

import arkeotek.io.IService;
import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Ontology;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class Service implements IService
{
	private Ontology owner;

	/**
	 * Just to allow Class.forName(String className).newInstance().
	 * Use Service(Ontology) to really use this class. 
	 */
	public Service() {
		// Just to allow Class.forName(String className).newInstance(). 
		// Use Service(Ontology) to really use this class. 
	}
	
	/**
	 * @param owner The owner ontology to whom this <code>Service</code> is dedicated.
	 */
	public Service(Ontology owner)
	{
		this.owner = owner;
	}

	/**
	 * @return The owner ontology to whom this <code>Service</code> is dedicated.
	 */
	public Ontology getOwner()
	{
		return this.owner;
	}

	/**
	 * @param owner The <code>Ontology</code> to be set as owner of this <code>IService</code>.
	 */
	public void setOwner(Ontology owner)
	{
		if (this.owner == null) this.owner = owner;
	}

	/**
	 * Saves the IIndexable element specified in <code>object</code> from the database.
	 * @param object The <code>LinkableElement</code> to be saved. 
	 * @throws Exception 
	 * @see arkeotek.io.IService#save(arkeotek.ontology.LinkableElement)
	 */
	// jamais utilisé
	/*
	public void save(LinkableElement object) throws Exception
	{
		System.out.println("deja sauvé");
		Transaction transaction;
		transaction = new Transaction(this, !Transaction.AUTOCOMMIT);
		DTO dto = new DTO(transaction, object);

		if (object instanceof Concept)
		{
			if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
			{
				// creation in database
				IOPerformer.createConcept(dto);
			}
			else
			{
				// update in database
				IOPerformer.updateConcept(dto);
			}
		}
		else if (object instanceof Lemma)
		{
			if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
			{
				// creation in database
				IOPerformer.createTerm(dto);
			}
			else
			{
				// update in database
				IOPerformer.updateTerm(dto);
			}
		}
		else if (object instanceof DocumentPart)
		{
			if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
			{
				// creation in database
				IOPerformer.createDocElem(dto);
			}
			else
			{
				// update in database
				IOPerformer.updateDocElem(dto);
			}
		}
		else if (object instanceof Relation)
		{
				if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
				{
					// creation in database
					IOPerformer.createRelation(dto);
				}
				else
				{
					// update in database
					IOPerformer.updateRelation(dto);
				}
		}
			// links with ontology
			saveLinks(dto);
			transaction.commit();
			object.setSaved();
	}
	*/
	
	private void saveLinks(DTO dto) throws SQLException
	{
		if (dto.getElement() instanceof Concept)
		{
			IOPerformer.updateConceptLinks(dto,this.owner.isEnBase());
		}
		else if (dto.getElement() instanceof Lemma)
		{
			IOPerformer.updateTermLinks(dto,this.owner.isEnBase());
		}
		else if (dto.getElement() instanceof DocumentPart)
		{
			IOPerformer.updateDocElemLinks(dto,this.owner.isEnBase());
		}
	}

	
	/**
	 * Deletes the IIndexable element specified in <code>object</code> from the database.
	 * @throws SQLException 
	 * 
	 * @see arkeotek.io.IService#delete(arkeotek.ontology.LinkableElement)
	 */
	public void delete(LinkableElement object) throws SQLException
	{
			Transaction transaction = new Transaction(this, !Transaction.AUTOCOMMIT);
			DTO dto = new DTO(transaction, object);

			if (object instanceof Concept)
			{
				IOPerformer.deleteConcept(dto);
			}
			else if (object instanceof Lemma)
			{
				IOPerformer.deleteTerm(dto);
			}
			else if (object instanceof DocumentPart)
			{
				IOPerformer.deleteDocElem(dto);
			}
			else if (object instanceof Relation)
			{
				IOPerformer.deleteRelation(dto);
			}
			transaction.commit();
			// We don't need to delete references in others tables because we
			// use
			// option ON_DELETE_CASCADE in the database
	}

	/**
	 * Saves the <code>IIndexable</code> elements specified in <code>list</code> from the database.
	 * @param list A <code>List</code> of <code>LinkableElement</code> to be saved. 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @see arkeotek.io.IService#save(List)
	 */
	public void save(List<LinkableElement> list) throws SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, IOException
	{
		Transaction transaction = new Transaction(this, !Transaction.AUTOCOMMIT);
		DTO dto = null;
		
			
			for (LinkableElement object : list)
			{
				System.out.println(object.getName());
				dto = new DTO(transaction, object);
				if (object instanceof Concept)
				{
					if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
					{
						// creation in database
						IOPerformer.createConcept(dto);
					}
					else
					{
						// update in database
						IOPerformer.updateConcept(dto);
					}
				}
				else if (object instanceof Lemma)
				{
					if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
					{
						// creation in database
						IOPerformer.createTerm(dto);
					}
					else
					{
						// update in database
						IOPerformer.updateTerm(dto);
					}
				}
				else if (object instanceof DocumentPart)
				{
					if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
					{
						// creation in database
						IOPerformer.createDocElem(dto);
					}
					else
					{
						// update in database
						IOPerformer.updateDocElem(dto);
					}
				}
				else if (object instanceof Relation)
				{
					if (object.getId() == LinkableElement.NEW_ELEMENT_ID)
					{
						// creation in database
						IOPerformer.createRelation(dto);
					}
					else
					{
						// Suppression des relations inutilisées dans l'application
						if( ((Relation) object ).getType()==Relation.RELATION_INUTILE)
						{
							delete(object);
							ApplicationManager.ontology.get(Relation.KEY).remove(object);
						}
						else
						{
							// update in database
							IOPerformer.updateRelation(dto);
						}
					}
				}
			}
			
			for (LinkableElement object : list)
			{
				// on crée une dto pour l'objet courant (transaction + objet)
				dto = new DTO(transaction, object);
				// links with ontology
				saveLinks(dto);
				// on met la variable dirty à true
				object.setSaved();
			}
			// on commite
			transaction.commit();
			
			// passage de la variable enBase a true pour éviter les sauvegardes lourdes
			this.owner.setEnBase(true);
	}

	/**
	 * Deletes the LinkableElement elements specified in <code>list</code> from the database.
	 * @throws SQLException 
	 * 
	 * @see arkeotek.io.IService#delete(java.util.ArrayList)
	 */
	public void delete(ArrayList<LinkableElement> list) throws SQLException
	{
		for (LinkableElement object : list)
			delete(object);
	}

	/**
	 * Retrieves the ontology from database
	 * @throws Exception 
	 *
	 * @see arkeotek.io.IService#retrieveOntology()
	 */
	public HashMap<Integer, ArrayList<LinkableElement>> retrieveOntology() throws Exception
	{
		
		Transaction transaction = new Transaction(this, !Transaction.AUTOCOMMIT);
		HashMap<Integer, ArrayList<LinkableElement>> ontology = null;
		try
		{
			// retrieves concepts
			HashMap<Integer, LinkableElement> concepts = IOPerformer.retrieveConcepts(transaction);
			// retrieves terms
			HashMap<Integer, LinkableElement> terms = IOPerformer.retrieveTerms(transaction);
			// retrieves documentElements
			HashMap<Integer, LinkableElement> docElems = IOPerformer.retrieveDocElems(transaction);
			// retrieves relations
			HashMap<Integer, LinkableElement> relations = IOPerformer.retrieveRelations(transaction);
			// links that will be found
			HashMap<Integer, LinkableElement> links = new HashMap<Integer, LinkableElement>();
			
			// retrieves links between concepts
			ArrayList<ArrayList<Object>> concept_conceptRelation = IOPerformer.retrieveLinks(transaction, "Concept", "Concept");
			if (concept_conceptRelation.size()!=0)
			{
				this.owner.setEnBase(true);
			}
			for (ArrayList<Object> array : concept_conceptRelation)
			{
				Relation rel = (Relation) relations.get(array.get(2));
				Link lnk = new Link(concepts.get(array.get(1)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk.getTarget().getCategoryKey(), lnk);
				concepts.get(array.get(0)).link(rel, concepts.get(array.get(1)), (Integer) array.get(3), (Integer) array.get(4));
			}

			// retrieves links between terms and concepts
			ArrayList<ArrayList<Object>> term_conceptRelation = IOPerformer.retrieveLinks(transaction, "Term", "Concept");
			for (ArrayList<Object> array : term_conceptRelation)
			{
				Relation rel = (Relation) relations.get(array.get(2));
				Link lnk = new Link(concepts.get(array.get(1)), rel, (Integer) array.get(3), (Integer) array.get(4));
				Link lnk2 = new Link(terms.get(array.get(0)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk.getTarget().getCategoryKey(), lnk);
				links.put(lnk2.getTarget().getCategoryKey(), lnk2);
				concepts.get(array.get(1)).link(rel, terms.get(array.get(0)), (Integer) array.get(3), (Integer) array.get(4));
				terms.get(array.get(0)).link(rel, concepts.get(array.get(1)), (Integer) array.get(3), (Integer) array.get(4));
			}

			// retrieves links between terms and documents
			ArrayList<ArrayList<Object>> term_documentRelation = IOPerformer.retrieveLinks(transaction, "Term", "DocumentElement");
			for (ArrayList<Object> array : term_documentRelation)
			{
				Relation rel = (Relation) relations.get(array.get(2));
				Link lnk = new Link(docElems.get(array.get(1)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk.getTarget().getCategoryKey(), lnk);
				terms.get(array.get(0)).link(rel, docElems.get(array.get(1)), (Integer) array.get(3), (Integer) array.get(4));
				Link lnk2 = new Link(terms.get(array.get(0)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk2.getTarget().getCategoryKey(), lnk2);
				docElems.get(array.get(1)).link(rel, terms.get(array.get(0)), (Integer) array.get(3), (Integer) array.get(4));
			}

			// retrieves links between terms
			ArrayList<ArrayList<Object>> term_termRelation = IOPerformer.retrieveLinks(transaction, "Term", "Term");
			for (ArrayList<Object> array : term_termRelation)
			{
				Relation rel = (Relation) relations.get(array.get(2));
				Link lnk = new Link(terms.get(array.get(1)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk.getTarget().getCategoryKey(), lnk);
				terms.get(array.get(0)).link(rel, terms.get(array.get(1)), (Integer) array.get(3), (Integer) array.get(4));
			}

			// retrieves links between concepts and documentElements
			ArrayList<ArrayList<Object>> concept_docElemRelation = IOPerformer.retrieveLinks(transaction, "Concept", "DocumentElement");
			for (ArrayList<Object> array : concept_docElemRelation)
			{
				Relation rel = (Relation) relations.get(array.get(2));
				Link lnk = new Link(docElems.get(array.get(1)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk.getTarget().getCategoryKey(), lnk);
				concepts.get(array.get(0)).link(rel, docElems.get(array.get(1)), (Integer) array.get(3), (Integer) array.get(4));
				Link lnk2 = new Link(concepts.get(array.get(0)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk2.getTarget().getCategoryKey(), lnk2);
				docElems.get(array.get(1)).link(rel, concepts.get(array.get(0)), (Integer) array.get(3), (Integer) array.get(4));
			}

			// retrieves links between documentElements and documentElements
			ArrayList<ArrayList<Object>> docElem_docElemRelation = IOPerformer.retrieveLinks(transaction, "DocumentElement", "DocumentElement");
			for (ArrayList<Object> array : docElem_docElemRelation)
			{
				Relation rel = (Relation) relations.get(array.get(2));
				Link lnk = new Link(docElems.get(array.get(1)), rel, (Integer) array.get(3), (Integer) array.get(4));
				links.put(lnk.getTarget().getCategoryKey(), lnk);
				docElems.get(array.get(0)).link(rel, docElems.get(array.get(1)), (Integer) array.get(3), (Integer) array.get(4));
			}

			
			// builds the ontology
			ontology = new HashMap<Integer, ArrayList<LinkableElement>>();
			ontology.put(Concept.KEY, new ArrayList<LinkableElement>(concepts.values()));
			ontology.put(Lemma.KEY, new ArrayList<LinkableElement>(terms.values()));
			ontology.put(DocumentPart.KEY, new ArrayList<LinkableElement>(docElems.values()));
			ontology.put(Relation.KEY, new ArrayList<LinkableElement>(relations.values()));
			
		} catch (SQLException e)
		{
			transaction.rollback();
			throw e;
		}
		return ontology;
	}
}
