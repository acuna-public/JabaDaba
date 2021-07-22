  package ru.ointeractive.jabadaba;
  /*
    Created by Acuna on 26.04.2018
  */
  
  import org.jsonp.JSONArray;
  import org.jsonp.JSONObject;

  import java.util.Formatter;
  import java.util.logging.Logger;
  import java.util.logging.Level;
  
  public class Log {
   
  	private static int i = 0;
  	
    private static Logger logger = Logger.getLogger ("JabaDaba");
    
    private static void log (Level level, Object... msg) {
    	
    	i++;
      logger.log (level, "" + System.debug (msg));
      
    }
    
    private static void log (Level level, Object msg, Exception e) {
      logger.log (level, System.debug (msg), e);
    }
    
    private static void log (Level level, Throwable err, String msg, Object... args) {
      logger.log (level, Log.format (msg, args), err);
    }
    
    public static void d (Object... msg) {
      
      /*log (Level.ALL, "all");
      log (Level.SEVERE, "severe");
      log (Level.WARNING, "warn");
      log (Level.INFO, "info");
      log (Level.OFF, "off");
      log (Level.CONFIG, "config");
      log (Level.FINE, "fine");
      log (Level.FINER, "finer");
      log (Level.FINEST, "finest");*/
      
      i (msg);
      
    }
    
    public static void d (Object msg, Exception e) {
      i (msg, e);
    }
    
    public static void d (JSONArray msg) {
      i (msg.toString (2));
    }
    
    public static void d (JSONObject msg) {
      i (msg.toString (2));
    }
    
    public static void d (ru.ointeractive.jabadaba.json.JSONArray msg) {
      d (new JSONArray (msg.toString ()));
    }
    
    public static void d (ru.ointeractive.jabadaba.json.JSONObject msg) {
      d (new JSONObject (msg.toString ()));
    }
    
    public static void i (Object... msg) {
      log (Level.INFO, msg);
    }
    
    public static void i (Object msg, Exception e) {
      log (Level.INFO, msg, e);
    }
    
    public static void i (Throwable err, String msg, Object... args) {
      log (Level.INFO, err, msg, args);
    }
    
    public static void w (Object... msg) {
      log (Level.WARNING, msg);
    }
    
	  public static void w (StackTraceElement[] msg) {
		  log (Level.WARNING, Arrays.implode (msg));
	  }
	  
	  public static void w (Object msg, Exception e) {
      log (Level.WARNING, msg, e);
    }
    
    public static void w (Throwable err, String msg, Object... args) {
      log (Level.WARNING, err, msg, args);
    }
    
    public static void e (Object... msg) {
      log (Level.SEVERE, msg);
    }
    
    public static void e (Object msg, Exception e) {
      log (Level.SEVERE, msg, e);
    }
    
    public static void e (Throwable err, String msg, Object... args) {
      log (Level.SEVERE, err, msg, args);
    }
    
    public static void v (Object... msg) {
      log (Level.OFF, msg);
    }
    
    public static void v (Object msg, Exception e) {
      log (Level.OFF, msg, e);
    }
    
    private static class ReusableFormatter {
      
      private Formatter formatter;
      private StringBuilder builder;
      
      private ReusableFormatter () {
        
        builder = new StringBuilder ();
        formatter = new Formatter (builder);
        
      }
      
      private String format (String msg, Object... args) {
        
        formatter.format (msg, args);
        
        String s = builder.toString ();
        builder.setLength (0);
        
        return s;
        
      }
      
    }
    
    private static final ThreadLocal<ReusableFormatter> formatter = new ThreadLocal<ReusableFormatter> () {
      
      @Override
      protected ReusableFormatter initialValue () {
        return new ReusableFormatter ();
      }
      
    };
    
    private static String format (String msg, Object... args) {
      return formatter.get ().format (msg, args);
    }
    
  }