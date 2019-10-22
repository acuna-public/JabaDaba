  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 24.03.2018
  */
  
  import org.jsoup.Jsoup;
  import org.jsoup.nodes.Document;
  import org.jsoup.nodes.Element;
  import org.jsoup.parser.Parser;
  
  public class Objects {
    
    public static boolean toBool (String str) {
      return str.equals ("true");
    }
    
    public static boolean equals (Object o1, Object o2) {
      return o1 == null ? o2 == null : o1.equals (o2);
    }
    
    public static int hashCode (Object o) {
      return o != null ? o.hashCode () : 0;
    }
    
    public static String getClassName (Object object) {
      return object.getClass ().getSimpleName ();
    }
    
    public static int materialsNum (int a, int h, Integer[]... walls) {
      
      int output = 0;
      
      for (Integer[] size : walls)
        output += (((1 / (double) a) * size[0]) * ((1 / (double) h) * size[1]));
      
      return output;
      
    }
    
    public static int cbmNum (int a, int b, int h) {
      return cbmNum (a, b, h, 1);
    }
    
    public static int cbmNum (int a, int b, int h, int quality) {
      return cbmNum (a, b, h, quality, 3);
    }
    
    public static int cbmNum (int a, int b, int h, int quality, int power) {
      
      int output = (a * b * h);
      double output2 = ((1 / ((double) output / Math.pow (1000, power))));
      
      if (quality > 0) output2 = (quality / output2);
      
      return (int) Math.ceil (output2);
      
    }
    
  }