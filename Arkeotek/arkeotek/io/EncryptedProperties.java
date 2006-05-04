/**
 * Created on 24 mai 2005
 * 
 * Arkeotek Project
 */
package arkeotek.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import ontologyEditor.Constants;

/**
 * @author Bernadou Pierre 
 * @author Czerny Jean
 *
 */
public class EncryptedProperties extends Properties {
    private Cipher ecipher;
	private Cipher dcipher;
	private static final String algorithm = "DES";
	private boolean initialized = false;
	private byte[] salt = {
            (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
            (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
        };
    // Iteration count
	private int iterationCount = 19;
	
	/*
	 * Constants used for following public property keys. 
	 */
	private static final String STORAGE = "storage";
	private static final String SERVICE = "service";
	private static final String LOCATION = "location";
	private static final String USER = "user";
	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";
	
	/**
	 * Key for the property specifying the kind of storage used for this ontology. 
	 */
	public static final String STORAGE_SERVICE = STORAGE + "." + SERVICE;
	/**
	 * Key for the property specifying the storage location used for this ontology. 
	 */
	public static final String STORAGE_LOCATION = STORAGE + "." + LOCATION;
	/**
	 * Key for the property specifying the user login used for this ontology. 
	 */
	public static final String STORAGE_USER_LOGIN = STORAGE + "." + USER + "." + LOGIN;
	/**
	 * Key for the property specifying the user password used for this ontology. 
	 */
	public static final String STORAGE_USER_PASSWORD = STORAGE + "." + USER + "." + PASSWORD;

	/**
	 * Creates a new encrypted properties uninitialized. 
	 * Note that as long as the setSecretKey(SecretKey key) have not been called, no encoding/decoding will be possible. 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 */
	public EncryptedProperties() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.ecipher = Cipher.getInstance(algorithm);
        this.dcipher = Cipher.getInstance(algorithm);
    }

	/**
	 * @param keyString The <code>String</code> used to generate a <code>SecretKey</code>. 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeySpecException 
	 */
	public EncryptedProperties(String keyString) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
        this.ecipher = Cipher.getInstance(algorithm);
        this.dcipher = Cipher.getInstance(algorithm);
		this.setKey(keyString);
    }
	
	/**
     * Searches for the property with the specified key in this property list and deciphers it.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns
     * <code>null</code> if the property is not found.
     *
     * @param   key   the property key.
     * @return  the value in this property list with the specified key value.
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	public String getEncryptedProperty(String key) throws IllegalBlockSizeException, BadPaddingException, IOException {
		return this.decrypt(this.getProperty(key));
	}

	/**
	 * Sets a key for encoding <b>if</b> none has been set yet. 
	 * This method does not perform any action if a secret key is already set.
	 * @param keyString The <code>String</code> used to generate a <code>SecretKey</code> to set. 
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidAlgorithmParameterException 
	 */
	public void setKey(String keyString) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        if (!this.initialized) {
			// Create the key
            KeySpec keySpec = new PBEKeySpec(keyString.toCharArray(), this.salt, this.iterationCount);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            this.ecipher = Cipher.getInstance(key.getAlgorithm());
			this.dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(this.salt, this.iterationCount);

            // Create the ciphers
			this.ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			this.dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

			this.initialized = true;
        }
	}
	
    /**
     * Encrypts a string using the secret key specified at instanciation. 
     * Note that this method will fail if no key has been set. 
     * @param str The <code>String</code> to encrypt. 
     * @return A <code>String</code> encrypted. 
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     */
    public String encrypt(String str) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// Encode the string into bytes using utf-8
		byte[] utf8 = str.getBytes("UTF8");

		// Encrypt
		byte[] enc = this.ecipher.doFinal(utf8);

		// Encode bytes to base64 to get a string
		return new sun.misc.BASE64Encoder().encode(enc);
    }

    /**
     * Decrypts a specified string using the secret key set at instanciation or with the setKey(String) method. 
     * Note that this method will fail if no key has been set. 
     * @param str The decrypted <code>String</code>. 
     * @return A decrypted <code>String</code>. 
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     */
    public String decrypt(String str) throws IllegalBlockSizeException, BadPaddingException, IOException {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        // Decrypt
        byte[] utf8 = this.dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }
	
	/**
	 * Creates an initialised .properties file for a new ontology. 
	 * @param name The <code>Ontology</code> name that will determine the .properties file name. 
	 * @param service The <code>IService</code> class name to use to interact with the corpus. 
	 * @param location The location of the corpus. 
	 * @param login The login to access the corpus. 
	 * @param password The password for the login. 
	 * @return The <code>File</code> representation of the .properties created. 
	 * @throws IOException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeySpecException 
	 */
	public static File createPropertiesFile(String name, String service, String location, String login, String password) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		File new_props = new File(Constants.DEFAULT_ONTOLOGIES_PATH + name + ".properties");
		if (!new_props.createNewFile())
			throw new IOException("Error occured while trying to create file " + Constants.DEFAULT_ONTOLOGIES_PATH + name + ".properties");
		
		FileOutputStream outStream = new FileOutputStream(new_props);
		EncryptedProperties properties = new EncryptedProperties(name);
		
		properties.setProperty(STORAGE_SERVICE, service);
		properties.setProperty(STORAGE_LOCATION, location);
		properties.setProperty(STORAGE_USER_LOGIN, login);
		properties.setProperty(STORAGE_USER_PASSWORD, properties.encrypt(password));
		
		properties.store(outStream, "");
		return new_props;
	}
}