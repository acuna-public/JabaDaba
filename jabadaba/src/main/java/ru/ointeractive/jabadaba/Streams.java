  package ru.ointeractive.jabadaba;
  /*
   Created by Acuna on 21.07.2018
  */
  
  import java.io.BufferedInputStream;
  import java.io.BufferedOutputStream;
  import java.io.ByteArrayInputStream;
  import java.io.File;
  import java.io.FileInputStream;
  import java.io.IOException;
  import java.io.FileOutputStream;
  import java.io.InputStream;
  import java.io.OutputStream;
  import java.io.UnsupportedEncodingException;
  
  public class Streams {
    
    public static InputStream toInputStream (File file) throws IOException {
      return toInputStream (file, Files.BUFFER_SIZE);
    }
    
    public static InputStream toInputStream (File file, int bufferSize) throws IOException {
      
      FileInputStream stream = new FileInputStream (file);
      return toInputStream (stream, bufferSize);
      
    }
    
    public static InputStream toInputStream (InputStream stream) {
      return toInputStream (stream, Files.BUFFER_SIZE);
    }
    
    public static InputStream toInputStream (InputStream stream, int bufferSize) {
      return new BufferedInputStream (stream, bufferSize);
    }
    
    public static InputStream toInputStream (Object string) {
      return toInputStream (string.toString ().getBytes ());
    }
    
    public static InputStream toInputStream (Object string, String charset) throws UnsupportedEncodingException {
      return toInputStream (string.toString ().getBytes (charset));
    }
    
    public static InputStream toInputStream (byte[] bytes) {
      return new BufferedInputStream (new ByteArrayInputStream (bytes));
    }
    
    public static OutputStream toOutputStream (File file) throws IOException {
      
      OutputStream stream = new FileOutputStream (file);
      return toOutputStream (stream);
      
    }
    
    public static OutputStream toOutputStream (OutputStream stream) {
      return new BufferedOutputStream (stream);
    }
    
    public static void copy (InputStream in, File file) throws IOException {
      copy (in, toOutputStream (file));
    }
    
    public static void copy (InputStream in, OutputStream out) throws IOException {
      copy (in, out, Files.BUFFER_SIZE, true);
    }
    
    public static void copy (InputStream in, OutputStream out, boolean close) throws IOException {
      copy (in, out, Files.BUFFER_SIZE, close);
    }
    
    public static void copy (InputStream in, OutputStream out, int size) throws IOException {
      copy (in, out, size, true);
    }
    
    public static void copy (InputStream in, OutputStream out, int size, boolean close) throws IOException {
      
      byte[] buffer = new byte[size];
      
      int length;
      while ((length = in.read (buffer, 0, size)) > 0)
      out.write (buffer, 0, length);
      
      if (close) out.flush ();
      
    }
    
  }