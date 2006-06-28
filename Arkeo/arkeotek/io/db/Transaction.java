/**
 * Created on 2 mai 2005
 * 
 * Project Arkeotek
 * Authors: Bernadou Pierre - Czerny Jean
 */
package arkeotek.io.db;



import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

import ontologyEditor.gui.dialogs.OntologyWizard;

import org.omg.CORBA.SystemException;

import arkeotek.io.EncryptedProperties;
import arkeotek.io.IService;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 * Manage connexions between application and database
 */

public class Transaction
{

	/**
	 * Boolean indicating the commit behavior. 
	 */
	public final static boolean AUTOCOMMIT = true;

	private Connection connexion = null;

	/**
	 * @param caller The <code>IService</code> using this <code>Transaction</code>.
	 */
	public Transaction(IService caller)
	{
		getConnexionFactory(caller);
	}

	/**
	 * Creation of new Transaction with definition of autocommit mode
	 * @param caller The <code>IService</code> using this <code>Transaction</code>.
	 * @param autocommit mode
	 */
	public Transaction(IService caller, boolean autocommit)
	{
		getConnexionFactory(caller);
		try 
		{
			this.connexion.setAutoCommit(autocommit);
		} catch (SQLException e) 
		{
			//throw new SystemException("Pbm setaucommit");
		}
	}

	/**
	 * Allows to commit current transaction
	 */
	public void commit() 
	{
		try 
		{
			this.connexion.commit();
		} catch (SQLException e) 
		{
			//throw new SystemException("Pbm commit");
		} finally 
		{
			deconnexion();
		}
	}
	
	/**
	 * Rolls back current transaction
	 */
	public void rollback() 
	{
		try 
		{
			this.connexion.rollback();
		} catch (SQLException e) 
		{
			//throw new SystemException("Pbm rollback");
		} finally 
		{
			deconnexion();
		}
	}

	/**
	 * Create a new connexion to the database
	 * @param caller The <code>IService</code> using this <code>Transaction</code>. 
	 */
	private void getConnexionFactory(IService caller)
	{
		try {
			EncryptedProperties properties = new EncryptedProperties();
			URL[] urls = {caller.getOwner().getPath()};
			URLClassLoader classLoader = new URLClassLoader(urls);
			InputStream propertiesStream = classLoader.getResourceAsStream(caller.getOwner().toPropertiesName());
			properties.load(propertiesStream);
	
			String pilote = "com.mysql.jdbc.Driver";
			
			try {Class.forName(pilote).newInstance();}
			catch (Exception e){System.err.println("echec pilote : "+e);};
	 
			String url = properties.getProperty(EncryptedProperties.STORAGE_LOCATION);
			String user = properties.getProperty(EncryptedProperties.STORAGE_USER_LOGIN);
			properties.setKey(caller.getOwner().toString());

			String password = properties.getEncryptedProperty(EncryptedProperties.STORAGE_USER_PASSWORD);
			
			try {this.connexion = DriverManager.getConnection(url,user,password);}
			catch (SQLException e){ 
				System.err.println("echec connection à la bdd: "+e);
			}
		} catch (NoSuchAlgorithmException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BadPaddingException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeyException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Deconnect from the database and close all the objects used
	 */
	public void deconnexion() 
	{

		try {
			if (this.connexion != null)
			{
				this.connexion.close();
			}
		} catch (Exception e)
		{
			//throw new SystemException("Problï¿½me dï¿½connexion base de donnï¿½es");
		}
	}

	/**
	 * Return the connexion
	 * @return Returns the connexion
	 */
	public Connection getConnexion() 
	{
		return this.connexion;
	}

	/**
	 * Close ResultSet and Statement  
	 * @param rset ResultSet to close
	 * @param stmt Statement to close
	 * @throws SystemException exception systï¿½me
	 */
	public void clean(ResultSet rset, Statement stmt) {
		try {

			if (rset != null) 
			{
				rset.close();
			}
			if (stmt != null) 
			{
				stmt.close();
			}
		} catch (Exception e) 
		{
			//throw new SystemException("Problï¿½me fermeture");
		}
	}

	/**
	 * Finalize the current object Transaction
	 * @throws Throwable 
	 */
	public void finalize() throws Throwable 
	{
		this.deconnexion();
		super.finalize();
	}

}
