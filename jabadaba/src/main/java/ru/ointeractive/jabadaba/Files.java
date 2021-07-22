  package ru.ointeractive.jabadaba;
  /*
   Created by Acuna on 17.07.2017
  */
  
  import java.io.BufferedWriter;
  import java.io.File;
  import java.io.FileOutputStream;
  import java.io.FileWriter;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.OutputStream;
  import java.io.OutputStreamWriter;
  import java.io.PrintWriter;
  import java.text.DecimalFormat;
  import java.text.NumberFormat;
  import java.util.ArrayDeque;
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Locale;
  import java.util.Map;
  
  import ru.ointeractive.jabadaba.exceptions.OutOfMemoryException;
  
  public class Files {
    
    public static final int BUFFER_SIZE = 4096;
    public static final byte[] BUFFER = new byte[BUFFER_SIZE];
    public static final String DS = File.separator;
    
    public static String getExtension (String file) {
      return getExtension (file, false);
    }
    
    public static String getExtension (File file) {
      return getExtension (file, false);
    }
    
    public static String getExtension (File file, boolean dot) {
      return getExtension (file.getAbsolutePath (), dot);
    }
    
    public static String getExtension (String file, boolean dot) {
      
      file = getName (file, true);
      
      int i = file.lastIndexOf ('.');
      if (!dot) ++i;
      
      return file.substring (i);
      
    }
    
    public static String getName (File file) {
      return getName (file, false);
    }
    
    public static String getName (File file, boolean ext) {
      return getName (file.toString (), ext);
    }
    
    public static String getName (String file) {
      return getName (file, false);
    }
    
    public static String getName (String str, boolean ext) {
      
      int pos = str.lastIndexOf (File.separator);
      str = str.substring ((pos + 1));
      
      if (!ext) {
        
        pos = str.lastIndexOf (".");
        if (pos > 0) str = str.substring (0, pos);
        
      }
      
      return str;
      
    }
    
    public static String read (String file) throws IOException, OutOfMemoryException {
      return read (file, Strings.DEF_CHARSET);
    }
    
    public static String read (File file) throws IOException, OutOfMemoryException {
      return read (file, Strings.DEF_CHARSET);
    }
    
    public static String read (String fileName, String charset) throws IOException, OutOfMemoryException {
      return read (new File (fileName), charset);
    }
    
    public static String read (File file, String charset) throws IOException, OutOfMemoryException {
      return read (file, charset, BUFFER_SIZE);
    }
    
    public static String read (File file, String charset, int buffer) throws IOException, OutOfMemoryException {
      return read (Streams.toInputStream (file), charset, buffer);
    }
    
    public static String read (InputStream stream) throws IOException, OutOfMemoryException {
      return read (stream, Strings.DEF_CHARSET);
    }
    
    public static String read (InputStream stream, String charset) throws IOException, OutOfMemoryException {
      return read (stream, charset, BUFFER_SIZE);
    }
    
    public static String read (InputStream stream, String charset, int buffer) throws IOException, OutOfMemoryException {
      return Strings.toStringBuilder (stream, charset, buffer).toString ();
    }
    
    public static List<String> read (String fileName, List<String> output) throws IOException {
      return read (new File (fileName), output);
    }
    
    public static List<String> read (File file, List<String> output) throws IOException {
      return read (Streams.toInputStream (file), output);
    }
    
    public static List<String> read (InputStream stream, List<String> output) throws IOException {
      return Arrays.toStringList (stream, output);
    }
    
    public static void write (Object object, String fileName) throws IOException {
      write (object, new File (fileName));
    }
    
    public static void write (List<?> array, String fileName) throws IOException {
      write (array, new File (fileName));
    }
    
    public static void write (Object text, File file) throws IOException {
      write (text, file, false);
    }
    
    public static void write (Object content, String fileName, boolean append) throws IOException {
      write (content, new File (fileName), append);
    }
    
    public static void write (Object content, File file, boolean append) throws IOException {
      write (content, file, append, Strings.DEF_CHARSET);
    }
    
    public static void write (Object text, File file, boolean append, String charset) throws IOException {
      
      if (makeDir (getPath (file)) > 0) {
        
        BufferedWriter bw;
        
        if (append) {
          
          bw = new BufferedWriter (new FileWriter (file, true));
          PrintWriter out = new PrintWriter (bw);
          
          out.println (text);
          
          out.flush ();
          out.close ();
          
        } else {
          
          FileOutputStream fos = new FileOutputStream (file);
          bw = new BufferedWriter (new OutputStreamWriter (fos));
          
          bw.write (String.valueOf (text));
          
        }
        
        bw.close ();
        
      }
      
    }
    
    public static void write (List<?> items, String fileName, boolean append) throws IOException {
      write (items, new File (fileName), append, Strings.DEF_CHARSET);
    }
    
    public static void write (List<?> items, File file) throws IOException {
      write (items, file, false);
    }
    
    public static void write (List<?> items, File file, boolean append) throws IOException {
      write (items, file, append, Strings.DEF_CHARSET);
    }
    
    public static void write (List<?> items, File file, boolean append, String charset) throws IOException {
      
      if (makeDir (getPath (file)) > 0 && Int.size (items) > 0) {
        
        if (append) {
          
          FileWriter fw = new FileWriter (file, true);
          BufferedWriter bw = new BufferedWriter (fw);
          PrintWriter out = new PrintWriter (bw);
          
          for (Object item : items)
            out.println (item);
          
          //out.flush ();
          //out.close ();
          
          bw.close ();
          
        } else {
          
          FileOutputStream fos = new FileOutputStream (file);
          BufferedWriter bw = new BufferedWriter (new OutputStreamWriter (fos));
          
          for (Object item : items) {
            
            bw.write (item.toString ());
            bw.newLine ();
            
          }
          
          bw.flush ();
          bw.close ();
          
        }
        
      }
      
    }
    
    /*public static void write (String content, String fileName, boolean append, boolean check) throws IOException {
      
      List<?> items = new ArrayList<> ();
      
      try {
        if (check) items = read (fileName, new ArrayList<?> ());
      } catch (IOException e) {
        // empty
      }
      
      if (!check || !Arrays.contains (content, items)) {
        
        items.add (content);
        write (items, fileName, append);
        
      }
      
    }*/
    
    public static int makeDir (String dirName) throws IOException {
      return makeDir (new File (Strings.trimEnd (File.separator, dirName)));
    }
    
    public static int makeDir (File file) throws IOException {
      
      int result;
      
      if (!file.isDirectory ()) {
        if (file.mkdirs ()) result = 1;
        else result = 0;
      } else result = 2;
      
      return result;
      
    }
    
    public static boolean delete (String fileName) throws IOException {
      return delete (new String[] { fileName });
    }
    
    public static boolean delete (String[] fileName) throws IOException {
      return delete (new File (Arrays.implode ("/", fileName)));
    }
    
    public static boolean delete (File path) throws IOException {
      
      if (path.isDirectory ()) {
        
        File[] files = path.listFiles ();
        
        if (files != null)
        for (File file : files) {
          
          if (file.isDirectory ())
            delete (file);
          else
            file.delete ();
          
        }
        
        /*String fileName = path.toString () + "_" + Int.doRand (3, 2);
        File to = new File (fileName);
        
        path.renameTo (to);
        
        path = to;*/
        
        return path.delete ();
        
      } else if (exists (path))
        return path.delete ();
      else
        return false;
      
    }
    
    public static boolean exists (File file) {
      return file.exists ();
    }
    
    public static String getPath (File path) {
      return getPath (path, -1);
    }
    
    public static String getPath (File path, int level) {
      return getPath (path.getAbsolutePath (), level);
    }
    
    public static String getPath (String str) {
      
      List<String> parts = Arrays.explode (DS, str);
      
      StringBuilder output = new StringBuilder ();
      
      if (Int.size (parts) > 1) {
        
        for (int i = 0; i < Int.size (parts) - 1; ++i) {
          
          if (i > 0) output.append (DS);
          output.append (parts.get (i));
          
        }
        
      } else output.append (str);
      
      return output.toString ();
      
    }
    
    public static String getPath (String path, int level) {
      
      String proto = "", sep = "://";
      List<String> parts = Arrays.explode (sep, path);
      
      if (Int.size (parts) > 1) {
        
        proto = parts.get (0) + sep;
        path = parts.get (1);
        
      } else path = parts.get (0);
      
      parts = Arrays.explode (File.separator, path);
      
      if (Int.size (parts) > 1)
        path = Arrays.implode (File.separator, parts, 0, level);
      
      return proto + path;
      
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public static List<File> list (String sourceFile) {
      return list (new File (sourceFile));
    }
    
    public static List<File> list (File sourceFile) {
      return list (sourceFile, true);
    }
    
    public static List<File> list (String sourceFile, boolean recurse) {
      return list (new File (sourceFile), recurse, false);
    }
    
    public static List<File> list (File sourceFile, boolean recurse) {
      return list (sourceFile, recurse, false);
    }
    
    public static List<File> list (File sourceFile, boolean recurse, boolean folders) {
      return list (sourceFile, recurse, folders, new ArrayList<File> ());
    }
    
    public static ArrayDeque<File> list (File dir, ArrayDeque<File> stack) {
      
      File[] files = dir.listFiles ();
      
      if (files != null)
        for (File file : files)
          stack.push (file);
      
      return stack;
      
    }
    
    public static List<File> list (File dir, boolean recurse, boolean folders, List<File> result) {
      
      int level = 0;
      ArrayDeque<File> stack = new ArrayDeque<> ();
      
      stack.push (dir);
      
      while (!stack.isEmpty ()) {
        ++level;
        
        dir = stack.pop ();
        
        if (dir.isDirectory ()) {
          
          if (folders && level > 1) result.add (dir);
          if (recurse || level == 1) list (dir, stack);
          
        } else result.add (dir);
        
      }
      
      return result;
      
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public static List<File> copy (String sourceFile, String destFile) throws IOException {
      return copy (new File (sourceFile), destFile);
    }
    
    public static List<File> copy (File sourceFile, String destFile) throws IOException {
      return copy (sourceFile, destFile, sourceFile.getAbsolutePath (), new ArrayList<File> ());
    }
    
    public static List<File> copy (File sourceFile, String destFile, String root, List<File> result) throws IOException {
      
      if (sourceFile.isDirectory ()) {
        
        File[] files = sourceFile.listFiles ();
        
        if (files != null)
        for (File file : files) {
          
          if (file.isDirectory ()) copy (file, destFile, root, result);
          else result.add (copy (file, destFile, root));
          
        }
        
      } else result.add (copy (sourceFile, destFile, root));
      
      return result;
      
    }
    
    private static File copy (File sourceFile, String destFile, String root) throws IOException {
      return copy (sourceFile, (!destFile.equals ("") ? new File (destFile + File.separator + Strings.trimStart (root, sourceFile)) : sourceFile));
    }
    
    public static File copy (String sourceFile, File destFile) throws IOException {
      return copy (new File (sourceFile), destFile);
    }
    
    public static File copy (File sourceFile, File destFile) throws IOException {
      return copy (Streams.toInputStream (sourceFile), destFile);
    }
    
    public static File copy (InputStream in, File destFile) throws IOException {
      
      File parent = destFile.getParentFile ();
      
      if (makeDir (parent) != 0) {
        
        OutputStream out = Streams.toOutputStream (destFile);
        
        Streams.copy (in, out);
        
        in.close ();
        out.close ();
        
        return destFile;
        
      } else throw new IOException ("Unable to create folder " + parent);
      
    }
    
    public static void copy (File sourceFile, OutputStream outputStream) throws IOException {
      Streams.copy (Streams.toInputStream (sourceFile), outputStream);
    }
    
    public static String mksize (float bytes) {
      return mksize ((long) bytes);
    }
    
    public static String mksize (String bytes) {
      return mksize (Long.parseLong (bytes));
    }
    
    public static String mksize (long bytes) {
      return mksize (bytes, "");
    }
    
    public static String mksize (File file) throws IOException {
      return mksize (file, "");
    }
    
    public static String mksize (File file, String value) throws IOException {
      return mksize (Int.size (file), value);
    }
    
    public static String mksize (long bytes, String value) {
      return mksize (bytes, value, " ");
    }
    
    public static String mksize (long bytes, String value, String sep) {
      return mksize (bytes, value, sep, true);
    }
    
    public static String mksize (long bytes, String value, String sep, boolean si) {
      return mksize (bytes, value, sep, si, 3);
    }
    
    public static String mksize (long bytes, String value, String sep, boolean si, int num) {
      return mksize (bytes, value, sep, si, num, Locale.getDefault ());
    }
    
    public static String mksize (long bytes, String value, String sep, boolean si, int num, Locale locale) {
      return mksize (bytes, value, sep, si, num, locale, new String[] { "b", "kb", "Mb", "Gb", "Tb" });
    }
    
    public static String mksize (long bytes, String value, String sep, boolean si, int num, Locale locale, String[] lang) {
      
      String output = "";
      int unit = (si ? 1024 : 1000);
      
      if (!value.equals ("")) {
        
        for (int i = 0; i < Int.size (lang); ++i)
        if (value.equals (lang[i]) || value.equals ("b")) {
          
          if (i > 0) {
            
            double pow = Math.pow (unit, i);
            output = String.valueOf (Int.numberFormat (bytes / pow, num));
            
          } else output = String.valueOf (bytes);
          
        }
        
      } else {
        
        for (int i = 0; i < Int.size (lang); ++i) {
          
          double pow = Math.pow (unit, i);
          
          if (bytes >= pow) {
            
            DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance (locale);
            format.applyPattern ((i < 3) ? "#0" : "#0.00");
            
            output = format.format (bytes / pow) + sep + lang[i];
            
          }
          
        }
        
        if (output.equals ("")) output = "0 " + lang[0];
        
      }
      
      return output;
      
    }
    
    public static Map<String, String> mimeTypes () {
      
      Map<String, String> types = new HashMap<> ();
      
      types.put ("xls", "application/vnd.ms-excel");
      types.put ("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      types.put ("xml", "text/xml");
      types.put ("ods", "application/vnd.oasis.opendocument.spreadsheet");
      types.put ("csv", "text/plain");
      types.put ("tmpl", "text/plain");
      types.put ("pdf", "application/pdf");
      types.put ("php", "application/x-httpd-php");
      types.put ("jpg", "image/jpeg");
      types.put ("png", "image/png");
      types.put ("gif", "image/gif");
      types.put ("bmp", "image/bmp");
      types.put ("txt", "text/plain");
      types.put ("doc", "application/msword");
      types.put ("js", "text/js");
      types.put ("swf", "application/x-shockwave-flash");
      types.put ("apk", "application/vnd.android.package-archive");
      types.put ("jar", "application/java-archive");
      types.put ("mp3", "audio/mpeg");
      types.put ("zip", "application/zip");
      types.put ("rar", "application/rar");
      types.put ("tar", "application/tar");
      types.put ("arj", "application/arj");
      types.put ("cab", "application/cab");
      types.put ("html", "text/html");
      types.put ("htm", "text/html");
      types.put ("default", "application/octet-stream");
      
      return types;
      
    }
    
    public static String getMimeType (String file) {
      
      Map<String, String> types = mimeTypes ();
      
      String type = types.get (getExtension (file));
      if (type == null) type = types.get ("default");
      
      return type;
      
    }
    
    public static void writeLog (Exception e, String file) {
      writeLog (e, file, "");
    }
    
    public static void writeLog (Throwable e, String file) {
      writeLog (e, file, "");
    }
	
	  public static void writeLog (Throwable e, String file, Object appName) {
		  writeLog (new Exception (e), file, appName);
	  }
	
	  public static String logText (String mess) {
      return mess + ":\n\n" + Arrays.implode (Thread.currentThread ().getStackTrace ()) + "\n\n";
    }
    
    private static String logText (Exception e, Object appName) {
      
      return e.toString () +
             (!appName.equals ("") ? " [" + appName + "]" : "") +
             (e.getCause () != null ? ":\n\n" +
             Arrays.implode (e.getCause ().getStackTrace ()) : "") + "\n\n";
      
    }
    
    public static void writeLog (Exception e, String file, Object appName) {
      writeLog (logText (e, appName), file);
    }
    
    public static void writeLog (String mess, String file) {
      
      try {
        write (Locales.date (6, false) + ": " + mess, file, true);
      } catch (IOException e) {
        // empty
      }
      
    }
    
    public static boolean isSymlink (String file) {
      return isSymlink (new File (file));
    }
    
    public static boolean isSymlink (File file) {
      
      try {
        return !(file.getCanonicalFile ().equals (file.getAbsoluteFile ()));
      } catch (IOException e) {
        return false;
      }
      
    }
    
    public static String prepPath (File file) {
      return prepPath (file.getAbsolutePath ());
    }
    
    public static String prepPath (String path) {
      
      path = Strings.addStart (File.separator, path);
      path = Strings.trimEnd (File.separator, path);
      
      return path;
      
    }
    
    public interface CountListener {
      
      void onProgress (long length);
      void onFinish (long length);
      
    }
    
    public static long getFolderSize (String dir) throws IOException {
      return getFolderSize (new File (dir));
    }
    
    public static long getFolderSize (File dir) throws IOException {
      return getFolderSize (dir, 1);
    }
    
    public static long getFolderSize (File dir, long blockSize) throws IOException {
      return getFolderSize (dir, blockSize, null);
    }
    
    public static long getFolderSize (File dir, long blockSize, CountListener listener) throws IOException {
      return getFolderSize (dir, blockSize, listener, 0);
    }
    
    private static long getFolderSize (File dir, long blockSize, CountListener listener, long size) throws IOException {
     
    	if (dir.isDirectory ()) {
    		
		    File[] files = dir.listFiles ();
		    
		    if (files != null)
			    for (File file : files) {
			    	
				    if (file.isDirectory ())
					    size = getFolderSize (file, blockSize, listener, size);
				    else
					    size += getFileSize (file, blockSize, listener);
				    
			    }
	      
	    } else size += getFileSize (dir, blockSize, listener);
      
      return size;
      
    }
    
    public static long getFileSize (File file, long blockSize, CountListener listener) throws IOException {
      
      long size = (Int.size (file) / (blockSize > 1 ? (blockSize + 1) : 1)) * blockSize;
      if (listener != null) listener.onProgress (size);
      
      return size;
      
    }
    
    public static List<File> ls (String folder) throws Console.ConsoleException {
      return ls (folder, Console.su);
    }
    
    public static List<File> ls (String folder, String shell) throws Console.ConsoleException {
      return ls (new File (folder), shell);
    }
    
    public static List<File> ls (File folder) throws Console.ConsoleException {
      return ls (folder, Console.su);
    }
    
    public static List<File> ls (final File folder, String shell) throws Console.ConsoleException {
      
      final List<File> output = new ArrayList<> ();
      final List<String> errors = new ArrayList<> ();
      
      Console exec = new Console (new Console.Listener () {
        
        @Override
        public void onExecute (String line, int i) {}
        
        @Override
        public void onSuccess (String line, int i) {
          output.add (new File (folder, line));
        }
        
        @Override
        public void onError (String line, int i) {
          errors.add (line);
        }
        
      });
      
      exec.shell (shell);
      exec.query ("ls " + folder);
      
      if (Int.size (errors) > 0)
        throw new Console.ConsoleException (errors);
      
      return output;
      
    }
    
    public static void debug (String str) {
      
      try {
        write (str, "/storage/sdcard/log.txt", true);
      } catch (IOException e) {
        // empty
      }
      
    }
    
    public static String[] images = new String[] { "jpg", "jpeg", "png", "gif", "webm" };
    
    public static boolean isImageByExt (String file) {
      return Arrays.contains (getExtension (file), images);
    }
    
    public static File toFile (InputStream stream, File file) throws IOException {
      
      byte[] buffer = new byte[stream.available ()];
      stream.read (buffer);
      
      OutputStream out = Streams.toOutputStream (file);
      
      out.write (buffer);
      
      return file;
    
    }
    
  }