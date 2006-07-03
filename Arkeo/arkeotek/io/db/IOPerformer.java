/**
 * Created on 10 mai 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import arkeotek.ontology.Concept;
import arkeotek.ontology.DocumentPart;
import arkeotek.ontology.Lemma;
import arkeotek.ontology.Link;
import arkeotek.ontology.LinkableElement;
import arkeotek.ontology.Relation;

/**
 * @author Bernadou Pierre
 * @author Czerny Jean
 */
public class IOPerformer
{
	/** ************************************************************************************ */
	/* Concept */
	/** ************************************************************************************ */

	/**
	 * Deletes the concept specified in <code>dto</code> from the database. 
	 * @param dto The <code>Concept</code> to delete
	 * @throws SQLException 
	 */
	public static void deleteConcept(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		req.append("delete from T_Concept where id = ? ");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setInt(1, ((LinkableElement) dto.getElement()).getId());
			ps.executeUpdate();
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}
	}

	/**
	 * Creates the concept specified in <code>dto</code> from the database
	 * @param dto The <code>Concept</code> to create
	 * @throws SQLException 
	 */
	public static void createConcept(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		// Quotes allow MySQL to use auto increment
		req.append("insert into T_Concept (id, name, state) values (NULL, ?, ?) ");

		try
		{
			
			ps = dto.getTransaction().getConnexion().prepareStatement(req.toString());
			
			ps.setString(1, ((LinkableElement) dto.getElement()).getName());
			ps.setInt(2, ((LinkableElement) dto.getElement()).getState());
			//System.out.println(ps);
			ps.executeUpdate();
			if (ps instanceof com.mysql.jdbc.PreparedStatement)
				((LinkableElement) dto.getElement()).setId((int)((com.mysql.jdbc.PreparedStatement)ps).getLastInsertID());
			
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}

		if (((LinkableElement) dto.getElement()).getId() == -1)
		{
			req = new StringBuffer();
			ps = null;
			ResultSet rs = null;
			req.append("select id from T_Concept ");
			req.append("where name = ? ");

			try
			{
				ps = dto.getTransaction().getConnexion().prepareStatement(
						req.toString());
				ps.setString(1, ((LinkableElement) dto.getElement()).getName());
				rs = ps.executeQuery();
	
				if (rs.last())
				{
					((LinkableElement) dto.getElement()).setId(rs.getInt("id"));
				}
			} catch (SQLException e)
			{
				throw e;
			} finally
			{
				dto.getTransaction().clean(rs, ps);
			}
		}

	}

	/**
	 * Updates the concept specified in <code>dto</code> from the database
	 * @param dto The <code>Concept</code> to update
	 * @throws SQLException 
	 */
	public static void updateConcept(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		req.append("update T_Concept set name = ?, state = ? where id=?");

		try
		{

			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setString(1, ((LinkableElement) dto.getElement()).getName());
			ps.setInt(2, ((LinkableElement) dto.getElement()).getState());
			ps.setInt(3, ((LinkableElement) dto.getElement()).getId());
			ps.executeUpdate();

		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}

	}

	/**
	 * Deletes the concept specified in <code>dto</code> from the database. 
	 * @param dto The <code>Relation</code> to delete
	 * @throws SQLException 
	 */
	public static void deleteRelation(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		
		req.append("delete FROM T_Relation where id=?");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(req.toString());
			System.out.println(req.toString());
			ps.setInt(1, ((LinkableElement) dto.getElement()).getId());
			ps.executeUpdate();
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}
	}

	/**
	 * Creates the concept specified in <code>dto</code> from the database
	 * @param dto The <code>Concept</code> to create
	 * @throws SQLException 
	 */
	public static void createRelation(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		// Quotes allow MySQL to use auto increment
		req.append("insert into T_Relation (id, name, state, type) values (NULL, ?, ?, ?) ");
		try
		{

			ps = dto.getTransaction().getConnexion().prepareStatement(req.toString());
			ps.setString(1, ((LinkableElement) dto.getElement()).getName());
			ps.setInt(2, ((LinkableElement) dto.getElement()).getState());
			ps.setInt(3, ((Relation) dto.getElement()).getType());
			//System.out.println(ps);
			ps.executeUpdate();
			if (ps instanceof com.mysql.jdbc.PreparedStatement)
				((LinkableElement) dto.getElement()).setId((int)((com.mysql.jdbc.PreparedStatement)ps).getLastInsertID());

		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}

		if (((LinkableElement) dto.getElement()).getId() == -1)
		{
			req = new StringBuffer();
			ps = null;
			ResultSet rs = null;
			req.append("select id from T_Relation ");
			req.append("where name = ? ");
	
			try
			{
				ps = dto.getTransaction().getConnexion().prepareStatement(
						req.toString());
				ps.setString(1, ((LinkableElement) dto.getElement()).getName());
				rs = ps.executeQuery();
	
				if (rs.last())
				{
					((LinkableElement) dto.getElement()).setId(rs.getInt("id"));
				}
			} catch (SQLException e)
			{
				throw e;
			} finally
			{
				dto.getTransaction().clean(rs, ps);
			}
		}

	}

	/**
	 * Updates the concept specified in <code>dto</code> from the database
	 * @param dto The <code>Concept</code> to update
	 * @throws SQLException 
	 */
	public static void updateRelation(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		req.append("update T_Relation set name = ?, state = ?, type = ? where id=?");

		try
		{

			ps = dto.getTransaction().getConnexion().prepareStatement(req.toString());
			ps.setString(1, ((LinkableElement) dto.getElement()).getName());
			ps.setInt(2, ((LinkableElement) dto.getElement()).getState());
			ps.setInt(3, ((Relation) dto.getElement()).getType());
			ps.setInt(4, ((LinkableElement) dto.getElement()).getId());
			ps.executeUpdate();

		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}

	}

	/**
	 * Finds a concept in the database whose Id is specified in <code>dto</code>. <code>dto</code> is then filled with the concept's values.  
	 * @param dto The <code>Concept</code>'s values matching the initial Id. 
	 * @throws SQLException 
	 */
	public static void findConceptByIdent(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		req.append("select id, name from T_Concept ");
		req.append("where id = ? ");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setInt(1, ((LinkableElement) dto.getElement()).getId());
			rs = ps.executeQuery();

			if (rs.next())
			{
				((LinkableElement) dto.getElement()).setName(rs
						.getString("name"));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(rs, ps);
		}
	}

	/**
	 * Updates the links of the concept specified in <code>dto</code> from database. 
	 * @param dto The <code>Concept</code> whose links are to be updated. 
	 * @throws SQLException 
	 */
	public static void updateConceptLinks(DTO dto,boolean enBase) throws SQLException
	{
		Set<Integer> keys = ((LinkableElement) dto.getElement()).getLinks().keySet();		
		for (Integer key : keys)
		{
			if (key.equals(Concept.KEY))
			{
				if (!enBase)
				{
					// nouvelle ontologie, il n'y a rien en base
					updateLinksFromSource(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Concept2Concept");
				}
				else
				{
					// la base est remplie
					updateLinksFromSourceNext(
							dto.getTransaction(), 
							(LinkableElement) dto.getElement(), 
							((LinkableElement) dto.getElement()).getLinks(key), 
							new StringBuffer("select * from L_Concept2Concept where idSource = " + ((LinkableElement) dto.getElement()).getId()));
							
				}
			}
			else if (key.equals(Lemma.KEY))
			{
				if (!enBase)
				{
					updateLinksFromTarget(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Term2Concept");
				}
				else
				{
					updateLinksFromTargetNext(
					dto.getTransaction(), 
					(LinkableElement) dto.getElement(), 
					((LinkableElement) dto.getElement()).getLinks(key), 
					new StringBuffer("select * from L_Term2Concept where idTarget = " + ((LinkableElement) dto.getElement()).getId()));
				}
			}
			else if (key.equals(DocumentPart.KEY))
			{
				if (!enBase){
					
					updateLinksFromSource(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Concept2DocumentElement");
				}
				else
				{
				updateLinksFromSourceNext(
						dto.getTransaction(),
						(LinkableElement) dto.getElement(), 
						((LinkableElement) dto.getElement()).getLinks(key), 
						new StringBuffer("select * from L_Concept2DocumentElement where idSource = " + ((LinkableElement) dto.getElement()).getId()));
				}
			}
		}
	}

	
	/**
	 * Retrieves all the concepts from base and stores them in a HashMap. 
	 * @param transaction The database transaction to use for this update. 
	 * @return a HashMap of <code>Concepts</code> as <code>LinkableElements</code>, mapped by their Ids. 
	 * @throws SQLException 
	 */
	public static HashMap<Integer, LinkableElement> retrieveConcepts(
			Transaction transaction) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Integer, LinkableElement> concepts = new HashMap<Integer, LinkableElement>();
		
		req.append("select * from T_Concept");

		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				concepts.put(rs.getInt("id"), new Concept(rs.getInt("id"), rs.getString("name"), rs.getInt("state")));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return concepts;
	}

	/** ************************************************************************************ */
	/* DocumentElement */
	/** ************************************************************************************ */

	/**
	 * Deletes the document part specified in <code>dto</code> from the database. 
	 * @param dto The <code>DocumentPart</code> to delete
	 * @throws SQLException 
	 */
	public static void deleteDocElem(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		req.append("delete from T_DocumentElement where id=? ");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setInt(1, ((LinkableElement) dto.getElement()).getId());
			ps.executeUpdate();
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}
	}

	/**
	 * Creates the document part specified in <code>dto</code> from the database
	 * @param dto The <code>DocumentPart</code> to create
	 * @throws SQLException 
	 */
	public static void createDocElem(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		req.append("insert into T_DocumentElement (id, value, text, state) values (NULL, ?, ?, ?) ");
		try
		{
			//System.out.println(((LinkableElement) dto.getElement()).getId());
			ps = dto.getTransaction().getConnexion().prepareStatement(req.toString());
			ps.setString(1, ((DocumentPart) dto.getElement()).getName());
			ps.setString(2, ((DocumentPart) dto.getElement()).getValue());
			ps.setInt(3, ((LinkableElement) dto.getElement()).getState());
			//System.out.println(ps);
			ps.executeUpdate();
			if (ps instanceof com.mysql.jdbc.PreparedStatement)
				((LinkableElement) dto.getElement()).setId((int)((com.mysql.jdbc.PreparedStatement)ps).getLastInsertID());
			System.out.println(((LinkableElement) dto.getElement()).getId());
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}

		if (((LinkableElement) dto.getElement()).getId() == -1)
		{
			req = new StringBuffer();
			ps = null;
			ResultSet rs = null;
			req.append("select id from T_DocumentElement ");
			req.append("where value = ? ");
	
			try
			{
				ps = dto.getTransaction().getConnexion().prepareStatement(
						req.toString());
				ps.setString(1, ((LinkableElement) dto.getElement()).getName());
				rs = ps.executeQuery();
	
				if (rs.last())
					((LinkableElement) dto.getElement()).setId(rs.getInt("id"));
			} catch (SQLException e)
			{
				throw e;
			} finally
			{
				dto.getTransaction().clean(rs, ps);
			}
		}

	}

	/**
	 * Updates the document part specified in <code>dto</code> from the database
	 * @param dto The <code>DocumentPart</code> to update
	 * @throws SQLException 
	 */
	public static void updateDocElem(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		req.append("update T_DocumentElement set value = ?, text = ?, state = ? where id=?");

		try
		{

			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setString(1, ((DocumentPart) dto.getElement()).getName());
			ps.setString(2, ((DocumentPart) dto.getElement()).getValue());
			ps.setInt(3, ((LinkableElement) dto.getElement()).getState());
			ps.setInt(4, ((DocumentPart) dto.getElement()).getId());
			ps.executeUpdate();

		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}
	}

	/**
	 * Finds a document part in the database whose Id is specified in <code>dto</code>. <code>dto</code> is then filled with the document part's values.  
	 * @param dto The <code>DocumentPart</code>'s values matching the initial Id. 
	 * @throws SQLException 
	 */
	public static void findDocElemByIdent(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		req.append("select id, value, text from T_DocumentElement ");
		req.append("where id = ? ");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setInt(1, ((DocumentPart) dto.getElement()).getId());
			rs = ps.executeQuery();

			if (rs.next())
			{
				((DocumentPart) dto.getElement()).setName(rs
						.getString("value"));
				((DocumentPart) dto.getElement()).setValue(rs
						.getString("text"));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(rs, ps);
		}
	}

	/**
	 * Updates the links of the document part specified in <code>dto</code> from database. 
	 * @param dto The <code>DocumentPart</code> whose links are to be updated. 
	 * @throws SQLException 
	 */
	public static void updateDocElemLinks(DTO dto,boolean enBase) throws SQLException
	{
		Set<Integer> keys = ((LinkableElement) dto.getElement()).getLinks()
				.keySet();
		for (Integer key : keys)
		{
			if (key.equals(Concept.KEY))
			{

				
				if(!enBase)
				{
					updateLinksFromTarget(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Concept2DocumentElement");
				}
				else
				{
					updateLinksFromTargetNext(
							dto.getTransaction(),
							((LinkableElement) dto.getElement()), 
							((LinkableElement) dto.getElement()).getLinks(key),
							new StringBuffer("select * from L_Concept2DocumentElement where idTarget = " + ((LinkableElement) dto.getElement()).getId()));
				}
			

			}
			else if (key.equals(Lemma.KEY))
			{
				if(!enBase)
				{
					updateLinksFromTarget(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Term2DocumentElement");
				}
				else
				{
					updateLinksFromTargetNext(
							dto.getTransaction(), 
							((LinkableElement) dto.getElement()), 
							((LinkableElement) dto.getElement()).getLinks(key), 
							new StringBuffer("select * from L_Term2DocumentElement where idTarget = " + ((LinkableElement) dto.getElement()).getId()));
				}
			}
			else if (key.equals(DocumentPart.KEY))
			{
				if(!enBase)
				{
					updateLinksFromSource(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_DocumentElement2DocumentElement");
				}
				else
				{
					updateLinksFromSourceNext(
							dto.getTransaction(),
							(LinkableElement) dto.getElement(), 
							((LinkableElement) dto.getElement()).getLinks(key), 
							new StringBuffer("select * from L_DocumentElement2DocumentElement where idSource = " + ((LinkableElement) dto.getElement()).getId()));
				}
			}
		}
	}
	
	/**
	 * Retrieves all the document parts from base and stores them in a HashMap. 
	 * @param transaction The database transaction to use for this action. 
	 * @return a HashMap of <code>DocumentParts</code> as <code>LinkableElements</code>, mapped by their Ids. 
	 * @throws SQLException 
	 */
	public static HashMap<Integer, LinkableElement> retrieveDocElems(
			Transaction transaction) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Integer, LinkableElement> docElems = new HashMap<Integer, LinkableElement>();
		
		req.append("select * from T_DocumentElement");

		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				docElems.put(rs.getInt("id"), new DocumentPart(rs.getInt("id"), rs.getString("value"), rs.getInt("state"), rs.getString("text")));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return docElems;
	}

	/** ***************************************************************************************** */
	/* Term */
	/** ***************************************************************************************** */

	/**
	 * Deletes the term specified in <code>dto</code> from the database. 
	 * @param dto The <code>Lemma</code> to delete
	 * @throws SQLException 
	 */
	public static void deleteTerm(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		req.append("delete from T_Term where id=? ");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setInt(1, ((LinkableElement) dto.getElement()).getId());
			ps.executeUpdate();
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}
	}

	/**
	 * Creates the term specified in <code>dto</code> from the database
	 * @param dto The <code>Lemma</code> to create
	 * @throws SQLException 
	 */
	public static void createTerm(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		// Les quotes permettent a MySQL d'utiliser l'auto-increment
		req.append("insert into T_Term (id, value, state) values (NULL, ?, ?) ");
		try
		{

			ps = dto.getTransaction().getConnexion().prepareStatement(req.toString());
			ps.setString(1, ((LinkableElement) dto.getElement()).getName());
			ps.setInt(2, ((LinkableElement) dto.getElement()).getState());
			//System.out.println(ps);
			ps.executeUpdate();
			if (ps instanceof com.mysql.jdbc.PreparedStatement)
				((LinkableElement) dto.getElement()).setId((int)((com.mysql.jdbc.PreparedStatement)ps).getLastInsertID());

		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}

		if (((LinkableElement) dto.getElement()).getId() == -1)
		{
			req = new StringBuffer();
			ps = null;
			ResultSet rs = null;
			req.append("select id from T_Term ");
			req.append("where value = ? ");
	
			try
			{
				ps = dto.getTransaction().getConnexion().prepareStatement(
						req.toString());
				ps.setString(1, ((LinkableElement) dto.getElement()).getName());
				rs = ps.executeQuery();
	
				if (rs.last())
				{
					((LinkableElement) dto.getElement()).setId(rs.getInt("id"));
				}
			} catch (SQLException e)
			{
				throw e;
			} finally
			{
				dto.getTransaction().clean(rs, ps);
			}
		}
	}

	/**
	 * Updates the term specified in <code>dto</code> from the database
	 * @param dto The <code>Lemma</code> to update
	 * @throws SQLException 
	 */
	public static void updateTerm(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;

		req.append("update T_Term set value = ?, state = ? where id=?");

		try
		{

			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setString(1, ((LinkableElement) dto.getElement()).getName());
			ps.setInt(2, ((LinkableElement) dto.getElement()).getState());
			ps.setInt(3, ((LinkableElement) dto.getElement()).getId());
			
			ps.executeUpdate();

		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(null, ps);
		}
	}

	/**
	 * Finds a term in the database whose Id is specified in <code>dto</code>. <code>dto</code> is then filled with the term's values.  
	 * @param dto The <code>Lemma</code>'s values matching the initial Id. 
	 * @throws SQLException 
	 */
	public static void findTermByIdent(DTO dto) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;

		req.append("select id, value from T_Term ");
		req.append("where id = ? ");

		try
		{
			ps = dto.getTransaction().getConnexion().prepareStatement(
					req.toString());
			ps.setInt(1, ((LinkableElement) dto.getElement()).getId());
			rs = ps.executeQuery();

			if (rs.next())
			{
				((LinkableElement) dto.getElement()).setName(rs
						.getString("value"));
				((LinkableElement) dto.getElement()).setState(rs
						.getInt("state"));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			dto.getTransaction().clean(rs, ps);
		}
	}

	
	
	/**
	 * @param dto
	 * @throws SQLException 
	 */
	public static void updateTermLinks(DTO dto,boolean enBase) throws SQLException
	{
		Set<Integer> keys = ((LinkableElement) dto.getElement()).getLinks().keySet();
		for (Integer key : keys)
		{
			if (key.equals(Concept.KEY))
			{
				if(!enBase)
				{
					updateLinksFromSource(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Term2Concept");	
				}
				else
				{
					updateLinksFromSourceNext(
							dto.getTransaction(), 
							(LinkableElement) dto.getElement(), 
							((LinkableElement) dto.getElement()).getLinks(key), 
							new StringBuffer("select * from L_Term2Concept where idSource = " + ((LinkableElement) dto.getElement()).getId()));
				}
			}
			else if (key.equals(Lemma.KEY))
			{
				if(!enBase)
				{
					updateLinksFromSource(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Term2Term");
				}
				else
				{
					updateLinksFromSourceNext(
							dto.getTransaction(), 
							(LinkableElement) dto.getElement(), 
							((LinkableElement) dto.getElement()).getLinks(key), 
							new StringBuffer("select * from L_Term2Term where idSource = " + ((LinkableElement) dto.getElement()).getId()));
				}
				

			}
			else if (key.equals(DocumentPart.KEY))
			{
				if(!enBase)
				{
					updateLinksFromSource(dto.getTransaction(),(LinkableElement) dto.getElement(),((LinkableElement) dto.getElement()).getLinks(key),"L_Term2DocumentElement");
				}
				else
				{
					updateLinksFromSourceNext(
						dto.getTransaction(), 
						(LinkableElement) dto.getElement(), 
						((LinkableElement) dto.getElement()).getLinks(key), 
						new StringBuffer("select * from L_Term2DocumentElement where idSource = " + ((LinkableElement) dto.getElement()).getId()));
				}
			}
		}
	}

	/**
	 * Retrieves all the terms from base and stores them in a HashMap. 
	 * @param transaction The database transaction to use for this update. 
	 * @return a HashMap of <code>Lemmas</code> as <code>LinkableElements</code>, mapped by their Ids. 
	 * @throws SQLException 
	 */
	public static HashMap<Integer, LinkableElement> retrieveTerms(
			Transaction transaction) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Integer, LinkableElement> terms = new HashMap<Integer, LinkableElement>();
		
		req.append("select * from T_Term");

		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				terms.put(rs.getInt("id"), new Lemma(rs.getInt("id"), rs.getString("value"), rs.getInt("state")));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return terms;
	}
	
	/**
	 * Retrieves all the terms from base and stores them in a HashMap. 
	 * @param transaction The database transaction to use for this update.
	 * @return a HashMap of <code>Lemmas</code> as <code>LinkableElements</code>, mapped by their Ids.
	 * @throws SQLException
	 */
	public static HashMap<Integer, LinkableElement> retrieveRelations(Transaction transaction) throws SQLException
	{
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		HashMap<Integer, LinkableElement> relations = new HashMap<Integer, LinkableElement>();
		
		req.append("select * from T_Relation");

		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
			
			while (rs.next())
			{
				relations.put(rs.getInt("id"), new Relation(rs.getInt("id"), rs.getString("name"), rs.getInt("state"), rs.getInt("type")));
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}

	/** ************************************************************************************ */
	/*
	 * L_Concept_DocElem L_Concept_Concept L_Term_Concept L_Term_Term
	 * L_Term_DocElem L_DocElem_DocElem
	 */
	/** ************************************************************************************ */

	/**
	 * Updates links targeting the element <code>idTarget</code> in the specified <code>relations</code>.  
	 * @param transaction The database transaction to use. 
	 * @param target The element targeted by the relations. 
	 * @param links 
	 * @param req The SQL request to execute. 
	 * @throws SQLException 
	 */
	private static void updateLinksFromTarget(Transaction transaction,LinkableElement target, HashMap<Relation, HashMap<LinkableElement, Link>> links,String table) throws SQLException
	{
		Statement etatSimple = null;
		try
		{
			Set<Relation> relations = links.keySet();
			for (Relation relation : relations)
			{
				Set<LinkableElement> sources = links.get(relation).keySet();
				for (LinkableElement source : sources)
				{
					String ordreSQL = "INSERT INTO " + table + " VALUES (" + source.getId() + "," + target.getId() + "," + relation.getId() + "," + links.get(relation).get(source).getState() + "," + links.get(relation).get(source).getWeighting() + ")";
					System.out.println(ordreSQL);
					etatSimple = transaction.getConnexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					etatSimple.executeUpdate(ordreSQL);
				}
			}
		}
		catch (SQLException e)
		{
			System.out.println("huhu");
			System.out.println(e.getErrorCode());
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			System.out.println(e.getSQLState());
			System.out.println(e.getLocalizedMessage());
			/*if(e.getErrorCode() == 1)
			{
					System.out.println("Bordel à cul !!!!!!");
			}*/
			//throw e;
		}
		//finally
		//{
			//transaction.clean(null, etatSimple);
		//}
	}

	/**
	 * Updates links targeted by the element <code>idSource</code> in the specified <code>relations</code>.  
	 * @param transaction The database transaction to use. 
	 * @param source The source element of the relations. 
	 * @param links The kind of relations concerned by this update.  
	 * @param req The SQL request to execute. 
	 * @throws SQLException 
	 */
	private static void updateLinksFromSource(Transaction transaction,LinkableElement source, HashMap<Relation, HashMap<LinkableElement, Link>> links,String table) throws SQLException
	{
		Statement etatSimple = null;
		try
		{
			Set<Relation> relations = links.keySet();
			for (Relation relation : relations)
			{
				Set<LinkableElement> sources = links.get(relation).keySet();
				for (LinkableElement target : sources)
				{
					String ordreSQL = "INSERT INTO " + table + " VALUES (" + source.getId() + "," + target.getId() + "," + relation.getId() + "," + links.get(relation).get(target).getState() + "," + links.get(relation).get(target).getWeighting() + ")";
					System.out.println(ordreSQL);
					etatSimple = transaction.getConnexion().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
					etatSimple.executeUpdate(ordreSQL);
				}
			}
		} catch (SQLException e)
		{
			System.out.println("huhu");
			System.out.println(e.getErrorCode());
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			System.out.println(e.getSQLState());
			System.out.println(e.getLocalizedMessage());
			// throw e;
		}
		//finally
		//{
			//transaction.clean(null, etatSimple);
		//}
	}
	/**
	  * Updates links targeting the element <code>idTarget</code> in the specified <code>relations</code>.  
	  * @param transaction The database transaction to use. 
	  * @param target The element targeted by the relations. 
	  * @param links 
	  * @param req The SQL request to execute. 
	  * @throws SQLException 
	  */
	 private static void updateLinksFromTargetNext(Transaction transaction,
	   LinkableElement target, HashMap<Relation, HashMap<LinkableElement, Link>> links,
	   StringBuffer req) throws SQLException
	 {
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	 
	  try
	  {
	   ps = transaction.getConnexion().prepareStatement(req.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	   rs = ps.executeQuery();
	   
	   while (rs.first())
	    rs.deleteRow();
	   
	   Set<Relation> relations = links.keySet();
	   for (Relation relation : relations)
	   {
	    Set<LinkableElement> sources = links.get(relation).keySet();
	    for (LinkableElement source : sources)
	    {
	     rs.moveToInsertRow();
	     System.out.println("source"+source.getId());
	     System.out.println("target"+target.getId());
	     rs.updateInt("idSource", source.getId());
	     rs.updateInt("idTarget", target.getId());
	     rs.updateInt("idRelation", relation.getId());
	     rs.updateInt("state", links.get(relation).get(source).getState());
	     rs.updateInt("weight", links.get(relation).get(source).getWeighting());
	     rs.insertRow();
	    }
	   }
	  } catch (SQLException e)
	  {
	   throw e;
	  } finally
	  {
	   transaction.clean(rs, ps);
	  }
	 }
	 
	 /**
	  * Updates links targeted by the element <code>idSource</code> in the specified <code>relations</code>.  
	  * @param transaction The database transaction to use. 
	  * @param source The source element of the relations. 
	  * @param links The kind of relations concerned by this update.  
	  * @param req The SQL request to execute. 
	  * @throws SQLException 
	  */
	 private static void updateLinksFromSourceNext(Transaction transaction,
	   LinkableElement source, HashMap<Relation, HashMap<LinkableElement, Link>> links,
	   StringBuffer req) throws SQLException
	 {
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	 
	  try
	  {
	   ps = transaction.getConnexion().prepareStatement(req.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	   rs = ps.executeQuery();
	   while (rs.first())
	    rs.deleteRow();
	 
	   Set<Relation> relations = links.keySet();
	   for (Relation relation : relations)
	   {
	    Set<LinkableElement> sources = links.get(relation).keySet();
	    for (LinkableElement target : sources)
	    {
	     rs.moveToInsertRow();
	     System.out.println("idSource "+ source.getId());
	     System.out.println("idSource "+ target.getId());
	     System.out.println("idSource "+ relation.getId());
	     rs.updateInt("idSource", source.getId());
	     rs.updateInt("idTarget", target.getId());
	     rs.updateInt("idRelation", relation.getId());
	     rs.updateInt("state", links.get(relation).get(target).getState());
	     rs.updateInt("weight", links.get(relation).get(target).getWeighting());
	     rs.insertRow();
	    }
	   }
	  } catch (SQLException e)
	  {
	   throw e;
	  } finally
	  {
	   transaction.clean(rs, null);
	  }
	 }
	/**
	 * Retrieves all the relations between concepts from base and stores them in a HashMap. 
	 * @param transaction The transaction to use for this retrieval. 
	 * @return The list of elements under the form idSource-idTarget-relation. 
	 * @throws SQLException 
	 */
/*	public static ArrayList<ArrayList<Object>> retrieveConceptConceptRelations(Transaction transaction) throws SQLException
	{
		System.out.println("retrieveConceptConceptRelations");
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_Concept2Concept");

		System.out.println(req);
		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(4);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getInt("idRelation"));
				relation.add(rs.getInt("state"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
*/	
	/**
	 * Retrieves all the relations between terms and concepts from base and stores them in a HashMap. 
	 * @param transaction The transaction to use for this retrieval. 
	 * @return The list of elements under the form idSource-idTarget-relation. 
	 * @throws SQLException 
	 */
/*	public static ArrayList<ArrayList<Object>> retrieveTermConceptRelations(Transaction transaction) throws SQLException
	{
		System.out.println("retrieveTermConceptRelations");
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_Term2Concept");

		System.out.println(req);
		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(4);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getInt("idRelation"));
				relation.add(rs.getInt("state"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
*/	
	/**
	 * Retrieves all the relations between concepts and document parts from base and stores them in a HashMap. 
	 * @param transaction The transaction to use for this retrieval. 
	 * @return The list of elements under the form idSource-idTarget-relation. 
	 * @throws SQLException 
	 */
/*	public static ArrayList<ArrayList<Object>> retrieveConceptDocElemRelations(Transaction transaction) throws SQLException
	{
		System.out.println("retrieveConceptDocElemRelations");
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_Concept2DocumentElement");

		System.out.println(req);
		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(4);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getInt("idRelation"));
				relation.add(rs.getInt("state"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
*/	
	/**
	 * Retrieves all the relations between terms and document parts from base and stores them in a HashMap. 
	 * @param transaction The transaction to use for this retrieval. 
	 * @return The list of elements under the form idSource-idTarget-relation. 
	 * @throws SQLException 
	 */
/*	public static ArrayList<ArrayList<Object>> retrieveTermDocElemRelations(Transaction transaction) throws SQLException
	{
		System.out.println("retrieveTermDocElemRelations");
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_Term2DocumentElement");

		System.out.println(req);
		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(4);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getString("idRelation"));
				relation.add(rs.getInt("state"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
*/
	/**
	 * Retrieves all the relations between terms from base and stores them in a HashMap. 
	 * @param transaction The transaction to use for this retrieval. 
	 * @return The list of elements under the form idSource-idTarget-relation. 
	 * @throws SQLException 
	 */
/*	public static ArrayList<ArrayList<Object>> retrieveTermTermRelations(Transaction transaction) throws SQLException
	{
		System.out.println("retrieveTermDocElemRelations");
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_Term2Term");

		System.out.println(req);
		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(4);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getInt("idRelation"));
				relation.add(rs.getInt("state"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
*/
	/**
	 * Retrieves all the relations between terms and document parts from base and stores them in a HashMap. 
	 * @param transaction The transaction to use for this retrieval. 
	 * @return The list of elements under the form idSource-idTarget-relation. 
	 * @throws SQLException 
	 */
/*	public static ArrayList<ArrayList<Object>> retrieveDocElemDocElemRelations(Transaction transaction) throws SQLException
	{
		System.out.println("retrieveDocElemDocElemRelations");
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_DocumentElement2DocumentElement");

		System.out.println(req);
		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(4);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getInt("idRelation"));
				relation.add(rs.getInt("state"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
*/	
	/**
	 * TODO Delete. 
	 * @param transaction 
	 * @param source_type_table 
	 * @param target_type_table 
	 * @return Un truc
	 * @throws SQLException 
	 */
	public static ArrayList<ArrayList<Object>> retrieveLinks(Transaction transaction, String source_type_table, String target_type_table) throws SQLException {
		StringBuffer req = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<ArrayList<Object>> relations = new ArrayList<ArrayList<Object>>();
		
		req.append("select * from L_" + source_type_table + "2" + target_type_table);

		try
		{
			ps = transaction.getConnexion().prepareStatement(req.toString());
			rs = ps.executeQuery();
				
			ArrayList<Object> relation = null;
			while (rs.next())
			{
				relation = new ArrayList<Object>(5);
				relation.add(rs.getInt("idSource"));
				relation.add(rs.getInt("idTarget"));
				relation.add(rs.getInt("idRelation"));
				relation.add(rs.getInt("state"));
				relation.add(rs.getInt("weight"));
				relations.add(relation);
			}
		} catch (SQLException e)
		{
			throw e;
		} finally
		{
			transaction.clean(null, ps);
		}
		return relations;
	}
}
