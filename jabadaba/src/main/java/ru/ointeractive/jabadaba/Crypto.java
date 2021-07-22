  package ru.ointeractive.jabadaba;
  /*
   Created by Acuna on 24.09.2017
  */
  
  import javax.crypto.BadPaddingException;
  import javax.crypto.Cipher;
  import javax.crypto.CipherInputStream;
  import javax.crypto.CipherOutputStream;
  import javax.crypto.IllegalBlockSizeException;
  import javax.crypto.KeyGenerator;
  import javax.crypto.NoSuchPaddingException;
  import javax.crypto.SecretKey;
  import javax.crypto.SecretKeyFactory;
  import javax.crypto.spec.IvParameterSpec;
  import javax.crypto.spec.PBEKeySpec;
  import javax.crypto.spec.PBEParameterSpec;
  import javax.crypto.spec.SecretKeySpec;
  
  import java.io.File;
  import java.io.InputStream;
  import java.io.FileOutputStream;
  import java.io.IOException;
  import java.io.OutputStream;
  import java.io.UnsupportedEncodingException;
  import java.security.InvalidAlgorithmParameterException;
  import java.security.InvalidKeyException;
  import java.security.KeyFactory;
  import java.security.MessageDigest;
  import java.security.NoSuchAlgorithmException;
  import java.security.NoSuchProviderException;
  import java.security.PrivateKey;
  import java.security.Provider;
  import java.security.PublicKey;
  import java.security.SecureRandom;
  import java.security.spec.InvalidKeySpecException;
  import java.security.spec.PKCS8EncodedKeySpec;
  import java.security.spec.X509EncodedKeySpec;
  import java.util.List;
  import java.util.Random;
  
  public class Crypto {
    
    public static String SYMMETRIC_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static String ASYMMETRIC_ALGORITHM = "RSA";
    public static String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static String SECRET_ALGORITHM = "AES";
    public static String DES_ALGORITHM = "PBEWithMD5AndDES";
    
    public static final String DIGEST_MD5 = "MD5";
    public static final String DIGEST_SHA1 = "SHA-1";
    
    public static final String[] symmetricAlgorims = {"AES-128", "AES-192", "AES-256"};
    
    public static final int AES_KEY_SIZE = 32; // 256 bits
    public static int PBE_ITERATION_COUNT = 65536;
    
    public static final String PRIVATE_FILE = "private.asc";
    public static final String PUBLIC_FILE = "public.asc";
    
    private static String prefPassword, prefKeysPath;
    private static int prefPBEKeySize = 128;
    private static Integer[] prefPBEKeySizes;
    private static File prefSrcFile, prefDestFile, prevPublicFile, prefKeyFile;
    
    public static class CryptoException extends Exception {
      
      private CryptoException (Exception e) {
        super (e);
      }
      
      @Override
      public Exception getCause () {
        return (Exception) super.getCause ();
      }
      
    }
    
    public static class EncryptException extends Exception {
      
      private EncryptException (Exception e) {
        super (e);
      }
      
      @Override
      public Exception getCause () {
        return (Exception) super.getCause ();
      }
      
    }
    
    public static class DecryptException extends Exception {
      
      private DecryptException (Exception e) {
        super (e);
      }
      
      @Override
      public Exception getCause () {
        return (Exception) super.getCause ();
      }
      
    }
    
    public void setAlgoritm (String algo) {
      
      algo = Arrays.contains (algo, symmetricAlgorims, symmetricAlgorims[0]);
      List<String> data = Arrays.explode ("-", algo);
      
      prefPBEKeySize = Integer.parseInt (data.get (1));
      
    }
    
    public void srcFile (String file) {
      srcFile (new File (file));
    }
    
    public void srcFile (File file) {
      prefSrcFile = file;
    }
    
    public void destFile (String file) {
      destFile (new File (file));
    }
    
    public void destFile (File file) {
      prefDestFile = file;
    }
    
    public void setPassword (String password) {
      prefPassword = password;
    }
    
    // Strings encryption
    
    public static Cipher getCipher (int mode, String password) throws CryptoException {
      
      try {
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance (DES_ALGORITHM);
        
        char[] secret = password.toCharArray ();
        SecretKey key = keyFactory.generateSecret (new PBEKeySpec (secret));
        
        Cipher cipher = Cipher.getInstance (DES_ALGORITHM);
        cipher.init (mode, key, new PBEParameterSpec (password.getBytes (), 20));
        
        return cipher;
        
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException | InvalidAlgorithmParameterException e) {
        throw new CryptoException (e);
      }
      
    }
    
    // AES Encryption
    
    public static byte[] generateSalt (int length) {
      
      byte[] salt = new byte[length];
      Random random = new Random ();
      random.nextBytes (salt);
      
      return salt;
      
    }
    
    public static byte[] getSalt (InputStream inFile, int length) throws IOException {
      
      byte[] salt = new byte[length];
      inFile.read (salt);
      
      return salt;
      
    }
    
    public static class SHA1PRNGProvider extends Provider {
      
      public SHA1PRNGProvider () {
        
        super ("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
        
        put ("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
        put ("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        
      }
      
    }
    
    private static Cipher getCipher (int mode, String password, byte[] salt) throws CryptoException {
      
      try {
        
        /*SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance (SYMMETRIC_ALGORITHM);
        PBEKeySpec keySpec = new PBEKeySpec (password.toCharArray (), salt, PBE_ITERATION_COUNT, prefPBEKeySize);
        
        SecretKey secretKey = secretKeyFactory.generateSecret (keySpec);
        secretKey = new SecretKeySpec (secretKey.getEncoded (), SECRET_ALGORITHM);
        
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec (salt, 100);
        
        Cipher cipher = Cipher.getInstance (CIPHER_ALGORITHM);
        cipher.init (mode, secretKey, pbeParameterSpec);*/
        
        IvParameterSpec ivSpec = new IvParameterSpec (salt);
        
        KeyGenerator kgen = KeyGenerator.getInstance (SECRET_ALGORITHM);
        
        SecureRandom sr = SecureRandom.getInstance ("SHA1PRNG", "Crypto"); // TODO
        sr.setSeed (password.getBytes ());
        
        kgen.init (prefPBEKeySize, sr);
        
        SecretKey secretKey = kgen.generateKey ();
        byte[] key = secretKey.getEncoded ();
        
        SecretKeySpec skeySpec = new SecretKeySpec (key, SECRET_ALGORITHM);
        
        Cipher cipher = Cipher.getInstance (CIPHER_ALGORITHM);
        cipher.init (mode, skeySpec, ivSpec);
        
        return cipher;
        
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
        throw new CryptoException (e);
      }
      
    }
    
    private void checkPassword () {
      
      if (prefPassword == null || Int.size (prefPassword) <= 7)
        throw new IllegalArgumentException ("Password length must be more than 7 symbols");
      
    }
    
    public static void encrypt (String inputFile, String outputFile, String password) throws EncryptException {
      encrypt (new File (inputFile), new File (outputFile), password);
    }
    
    public static void encrypt (File inputFile, File outputFile, String password) throws EncryptException {
      
      Crypto crypto = new Crypto ();
      
      crypto.srcFile (inputFile);
      crypto.destFile (outputFile);
      crypto.setPassword (password);
      
      crypto.encrypt ();
      
    }
    
    public void encrypt () throws EncryptException {
      
      if (prefDestFile == null) prefDestFile = new File (prefSrcFile.toString () + ".aes");
      
      try {
        
        InputStream inFile = Streams.toInputStream (prefSrcFile);
        OutputStream outFile = new FileOutputStream (prefDestFile);
        
        encrypt (inFile, outFile);
        
      } catch (IOException e) {
        throw new EncryptException (e);
      }
      
    }
    
    public static String encrypt (String value, String phrase) throws EncryptException {
      
      try {
        return Base64.encodeToString (Crypto.getCipher (Cipher.ENCRYPT_MODE, phrase).doFinal (Arrays.toByteArray (value)), Base64.NO_WRAP); // SUPPORT 4
      } catch (Crypto.CryptoException | IllegalBlockSizeException | IllegalArgumentException | BadPaddingException | UnsupportedEncodingException e) {
        throw new EncryptException (e);
      }
      
    }
    
    public static String decrypt (String value, String phrase) throws DecryptException {
      
      try {
        
        byte[] bytes = (value != null ? Base64.decode (value, Base64.DEFAULT) : new byte[0]);
        return new String (Crypto.getCipher (Cipher.DECRYPT_MODE, phrase).doFinal (bytes));
        
      } catch (Crypto.CryptoException | IllegalBlockSizeException | IllegalArgumentException | BadPaddingException e) {
        throw new DecryptException (e);
      }
      
    }
    
    public void encrypt (InputStream inFile, OutputStream outFile) throws EncryptException {
      
      try {
        
        checkPassword ();
        
        byte[] salt = generateSalt (16);
        outFile.write (salt);
        
        Cipher cipher = getCipher (Cipher.ENCRYPT_MODE, prefPassword, salt);
        copy (cipher, inFile, outFile);
        
      } catch (IOException | IllegalArgumentException | CryptoException e) {
        throw new EncryptException (e);
      }
      
    }
    
    public static void decrypt (String inputFile, String outputFile, String password) throws DecryptException {
      decrypt (new File (inputFile), new File (outputFile), password);
    }
    
    public static void decrypt (File inputFile, File outputFile, String password) throws DecryptException {
      
      Crypto crypto = new Crypto ();
      
      crypto.srcFile (inputFile);
      crypto.destFile (outputFile);
      crypto.setPassword (password);
      
      crypto.decrypt ();
      
    }
    
    public void decrypt () throws DecryptException {
      
      if (prefDestFile == null)
        prefDestFile = new File (prefSrcFile.getAbsolutePath (), Files.getName (prefSrcFile));
      
      try {
        
        InputStream inFile = Streams.toInputStream (prefSrcFile);
        OutputStream outFile = new FileOutputStream (prefDestFile);
        
        decrypt (inFile, outFile);
        
      } catch (IOException e) {
        throw new DecryptException (e);
      }
      
    }
    
    public void decrypt (InputStream inFile, File file) throws DecryptException {
      
      try {
        
        OutputStream stream = Streams.toOutputStream (file);
        decrypt (inFile, stream);
        
      } catch (IOException e) {
        throw new DecryptException (e);
      }
      
    }
    
    public void decrypt (InputStream inFile, OutputStream outFile) throws DecryptException {
      
      try {
        
        checkPassword ();
        
        byte[] salt = getSalt (inFile, 16);
        Cipher cipher = getCipher (Cipher.DECRYPT_MODE, prefPassword, salt);
        
        copy (cipher, inFile, outFile);
        
      } catch (IOException | IllegalArgumentException | CryptoException e) {
        throw new DecryptException (e);
      }
      
    }
    
    // RSA Encryption
    
    public void saveKey (String keysPath, String publicKeyFile) throws CryptoException {
      
      try {
        
        File outputFile = new File (keysPath, PUBLIC_FILE);
        byte[] encodedKey = new byte[(int) Int.size (new File (publicKeyFile))];
        Streams.toInputStream (publicKeyFile).read (encodedKey);
        
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec (encodedKey);
        KeyFactory kf = KeyFactory.getInstance (ASYMMETRIC_ALGORITHM);
        PublicKey pk = kf.generatePublic (publicKeySpec);
        
        Cipher pkCipher = Cipher.getInstance (ASYMMETRIC_ALGORITHM);
        pkCipher.init (Cipher.ENCRYPT_MODE, pk);
        
        OutputStream os = new CipherOutputStream (new FileOutputStream (outputFile), pkCipher);
        
        KeyGenerator kgen = KeyGenerator.getInstance (CIPHER_ALGORITHM);
        kgen.init (AES_KEY_SIZE);
        
        SecretKey key = kgen.generateKey ();
        byte[] aesKey = key.getEncoded ();
        
        os.write (aesKey);
        os.close ();
        
      } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
        throw new CryptoException (e);
      }
      
    }
    
    private static SecretKeySpec loadKey (String keysPath, File privateKeyFile) throws CryptoException {
      
      try {
        
        File inputFile = new File (keysPath, PRIVATE_FILE);
        
        byte[] encodedKey = new byte[(int) Int.size (privateKeyFile)];
        Streams.toInputStream (privateKeyFile).read (encodedKey);
        
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec (encodedKey);
        
        KeyFactory kf = KeyFactory.getInstance (ASYMMETRIC_ALGORITHM);
        PrivateKey pk = kf.generatePrivate (privateKeySpec);
        
        Cipher pkCipher = Cipher.getInstance (ASYMMETRIC_ALGORITHM);
        pkCipher.init (Cipher.DECRYPT_MODE, pk);
        
        byte[] aesKey = new byte[AES_KEY_SIZE / 8];
        CipherInputStream is = new CipherInputStream (Streams.toInputStream (inputFile), pkCipher);
        is.read (aesKey);
        
        return new SecretKeySpec (aesKey, SECRET_ALGORITHM);
        
      } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e) {
        throw new CryptoException (e);
      }
      
    }
    
    public void setKeyFile (File file) {
      prefKeyFile = file;
    }
    
    public void setKeysPath (String file) {
      prefKeysPath = file;
    }
    
    public static void encrypt (String inputFile, String outputFile, String keysPath, String privateKeyFile) throws EncryptException {
      encrypt (new File (inputFile), new File (outputFile), keysPath, new File (privateKeyFile));
    }
    
    public static void encrypt (File inputFile, File outputFile, String keysPath, File privateKeyFile) throws EncryptException {
      
      Crypto crypto = new Crypto ();
      
      crypto.srcFile (inputFile);
      crypto.destFile (outputFile);
      crypto.setKeysPath (keysPath);
      crypto.setKeyFile (privateKeyFile);
      
      crypto.RSAEncrypt ();
      
    }
    
    // TODO: Rewrite RSAEncrypt with http://www.macs.hw.ac.uk/~ml355/lore/pkencryption.htm
    
    private void RSAEncrypt () throws EncryptException {
      
      try {
        
        Cipher cipher = Cipher.getInstance (CIPHER_ALGORITHM);
        cipher.init (Cipher.ENCRYPT_MODE, loadKey (prefKeysPath, prefKeyFile));
        
        InputStream is = Streams.toInputStream (prefSrcFile);
        OutputStream os = new CipherOutputStream (new FileOutputStream (prefDestFile), cipher);
        
        Streams.copy (is, os);
        
      } catch (IOException | CryptoException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new EncryptException (e);
      }
      
    }
    
    public static void decrypt (File inputFile, File outputFile, String keysPath, File privateKeyFile) throws DecryptException {
      
      Crypto crypto = new Crypto ();
      
      crypto.srcFile (inputFile);
      crypto.destFile (outputFile);
      crypto.setKeysPath (keysPath);
      crypto.setKeyFile (privateKeyFile);
      
      crypto.RSADecrypt ();
      
    }
    
    public static void decrypt (String inputFile, String outputFile, String keysPath, String privateKeyFile) throws DecryptException {
      decrypt (new File (inputFile), new File (outputFile), keysPath, new File (privateKeyFile));
    }
    
    public void RSADecrypt () throws DecryptException {
      
      try {
        
        Cipher cipher = Cipher.getInstance (CIPHER_ALGORITHM);
        cipher.init (Cipher.DECRYPT_MODE, loadKey (prefKeysPath, prefKeyFile));
        
        InputStream inputStream = new CipherInputStream (Streams.toInputStream (prefSrcFile), cipher);
        OutputStream outputStream = new FileOutputStream (prefDestFile);
        
        Streams.copy (inputStream, outputStream);
        
        inputStream.close ();
        
        outputStream.flush ();
        
      } catch (IOException | CryptoException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
        throw new DecryptException (e);
      }
      
    }
    
    // Utils
    
    public static void copy (Cipher cipher, InputStream inFile, OutputStream outFile) throws CryptoException {
      
      try {
        
        int bytesRead;
        
        while ((bytesRead = inFile.read (Files.BUFFER)) != -1) {
          
          byte[] output = cipher.update (Files.BUFFER, 0, bytesRead);
          outFile.write (output);
          
        }
        
        byte[] output = cipher.doFinal ();
        outFile.write (output);
        
        //inFile.close ();
        
        outFile.flush ();
        //outFile.close ();
        
      } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
        throw new CryptoException (e);
      }
      
    }
    
    public static String digest (String algo, String mess) throws EncryptException {
      return digest (algo, mess, Files.BUFFER_SIZE);
    }
    
    public static String digest (String algo, String mess, int byteArraySize) throws EncryptException {
      return digest (algo, Streams.toInputStream (mess), byteArraySize);
    }
    
    public static String digest (String algo, File file) throws EncryptException {
      return digest (algo, file, Files.BUFFER_SIZE);
    }
    
    public static String digest (String algo, File file, int byteArraySize) throws EncryptException {
      
      try {
        return digest (algo, Streams.toInputStream (file), byteArraySize);
      } catch (IOException e) {
        throw new EncryptException (e);
      }
      
    }
    
    public static String digest (String algo, InputStream is) throws EncryptException {
      return digest (algo, is, Files.BUFFER_SIZE);
    }
    
    public static String digest (String algo, InputStream is, int byteArraySize) throws EncryptException {
      
      try {
        return digest (MessageDigest.getInstance (algo), is, byteArraySize);
      } catch (NoSuchAlgorithmException e) {
        throw new EncryptException (e);
      }
      
    }
    
    public static String digest (MessageDigest digest, InputStream is) throws EncryptException {
      return digest (digest, is, Files.BUFFER_SIZE);
    }
    
    public static String digest (MessageDigest md, InputStream is, int size) throws EncryptException {
      
      md.reset ();
      
      try {
        
        byte[] bytes = new byte[size];
        int numBytes;
        
        while ((numBytes = is.read (bytes)) != -1)
          md.update (bytes, 0, numBytes);
        
        return Strings.toString (md.digest ());
        
      } catch (IOException e) {
        throw new EncryptException (e);
      }
      
    }
    
  }