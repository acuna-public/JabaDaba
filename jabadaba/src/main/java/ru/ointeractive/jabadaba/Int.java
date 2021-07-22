  package ru.ointeractive.jabadaba;
  /*
   Created by Acuna on 17.07.2017
  */
  
  import org.json.JSONArray;
  import org.json.JSONObject;
  
  import java.io.File;
  import java.io.IOException;
  import java.io.InputStream;
  import java.math.BigDecimal;
  import java.math.RoundingMode;
  import java.text.DecimalFormat;
  import java.util.List;
  import java.util.Map;
  import java.util.Set;
  
  public class Int {
		
	  private static final int MAX_DECIMAL = 3;
	  public static String mPrefix = "";
	  private static String mFormat = mPrefix + "#,###";
	
	  public static int size (CharSequence str) {
      return str.length ();
    }
    
    public static int size (Object[] items) {
      return items.length;
    }
    
    public static int size (int[] items) {
      return items.length;
    }
    
    public static int size (long[] items) {
      return items.length;
    }
    
    public static int size (float[] items) {
      return items.length;
    }
    
    public static int size (byte[] items) {
      return items.length;
    }
    
    public static int size (List<?> items) {
      return items.size ();
    }
    
    public static int size (Map<?, ?> items) {
      return items.size ();
    }
    
    public static int size (Set<?> items) {
      return items.size ();
    }
    
    public static long size (File file) {
      return file.length ();
    }
    
    public static int size (InputStream stream) throws IOException {
      return stream.available ();
    }
    
    public static int size (JSONArray items) {
      return items.length ();
    }
    
    public static int size (JSONObject items) {
      return items.length ();
    }
    
    public static int size (org.jsonp.JSONArray items) {
      return items.length ();
    }
    
    public static int size (org.jsonp.JSONObject items) {
      return items.length ();
    }
    
    public static int valueOf (Object str) {
      
      try {
        
        if (str != null) {
          
          String str2 = Strings.trimStart ("0", str.toString ().trim ());
          return Integer.parseInt (str2);
          
        } else return 0;
        
      } catch (NumberFormatException e) {
        return 0;
      }
      
    }
    
    public static int prop (int from, int to) {
      return prop (from, to, 100);
    }
    
    public static int prop (int from, int to, int divider) {
      
      int result = 0;
      if (to > 0) result = ((from * divider) / to);
      
      return result;
      
    }
    
    public static long prop (long from, long to) {
      return prop (from, to, (long) 100);
    }
    
    public static long prop (long from, long to, long divider) {
      
      long result = 0;
      if (to > 0) result = ((from * divider) / to);
      
      return result;
      
    }
    
    public static float prop (float from, float to) {
      return prop (from, to, (float) 100);
    }
    
    public static float prop (float from, float to, float divider) {
      
      float result = 0;
      if (to > 0) result = ((from * divider) / to);
      
      return result;
      
    }
    
    public static double prop (double from, double to) {
      return prop (from, to, 100);
    }
    
    public static double prop (double from, double to, double divider) {
      
      double result = 0;
      if (to > 0) result = ((from * divider) / to);
      
      return result;
      
    }
    
    public static int ceil (int number) { // В большую
      return (int) Math.ceil (number);
    }
    
    public static int floor (int number) {
      return (int) Math.floor (number);
    }
    
    public static long ceil (long number) {
      return (long) Math.ceil (number);
    }
    
    public static long floor (long number) {
      return (long) Math.floor (number);
    }
    
    public static int ceil (float number) {
      return (int) Math.ceil (number);
    }
    
    public static int floor (float number) {
      return (int) Math.floor (number);
    }
    
    public static int ceil (double number) {
      return (int) Math.ceil (number);
    }
    
    public static int floor (double number) {
      return (int) Math.floor (number);
    }
    
    public static String numberFormat (String number, int num) {
      return numberFormat (number, num, ",");
    }
    
    public static String numberFormat (double number, int num) {
      return numberFormat (number, num, ",");
    }
    
    public static double toDouble (String value) {
      return Double.valueOf (value.replace (",", "."));
    }
    
    public static String numberFormat (String number, int num, String sep1) {
      return numberFormat (toDouble (number), num, sep1);
    }
    
    public static String numberFormat (double number, int num, String sep1) {
      
      DecimalFormat df = new DecimalFormat ("#" + sep1 + Strings.repeat ("#", num));
      return df.format (number);
      
    }
    
    public static double round (String value, int places) {
      return round (toDouble (value), places);
    }
    
    public static double round (double value, int places) {
      
      BigDecimal bd = new BigDecimal (value);
      bd = bd.setScale (places, RoundingMode.HALF_UP);
      
      return bd.doubleValue ();
      
    }
    
    public static int correct (String num, String result) {
      return correct (num, Integer.valueOf (result));
    }
    
    public static int correct (String num, int result) {
      return correct (Integer.valueOf (num), result);
    }
    
    public static int correct (int num, int result) {
      return correct (num, result, 0);
    }
    
    public static int correct (int num, int result, int expect) {
      
      if (num <= expect) num = result;
      return num;
      
    }
    
    public static boolean isNumeric (Object str) {
      return (str instanceof Integer);
    }
    
    public static boolean isNumeric (String str) {
      
      try {
        
        Integer.parseInt (str);
        return true;
        
      } catch (NumberFormatException e) {
        return false;
      }
      
    }
    
    public static boolean isLong (Object str) {
      return (str instanceof Long);
    }
    
    public static boolean isLong (String str) {
      
      try {
        
        Long.parseLong (str);
        return true;
        
      } catch (NumberFormatException e) {
        return false;
      }
      
    }
    
    public static boolean isNumeric (double d) {
      return (Math.abs (Math.floor (d) - d) < 1E-5);
    }
    
    public static int rand (int min, int max) {
      return (int) rand (min, max, false);
    }
    
    public static long rand (long min, long max, boolean isLong) {
      
      max -= min;
      return (long) (Math.random () * ++max) + min;
      
    }
    
    public static String doRand (int num) {
      return doRand (num, 1);
    }
    
    public static String doRand (int num, int type) {
      
      StringBuilder salt = new StringBuilder ();
      
      if (type == 1 || type == 2 || type == 3 || type == 4)
        salt.append (Strings.SUMB_DIGITS); // 1
      if (type == 2 || type == 3 || type == 4)
        salt.append (Strings.SUMB_LETTERS_LOW); // 2
      if (type == 3 || type == 4)
        salt.append (Strings.SUMB_LETTERS_UP); // 3
      if (type == 4)
        salt.append (Strings.SUMB_SPECIAL); // 4
      
      if (type == 5) salt.append (Strings.SUMB_LETTERS_LOW); // 5
      if (type == 6) salt.append (Strings.SUMB_LETTERS_UP); // 6
      
      StringBuilder rand = new StringBuilder ();
      int len = (Int.size (salt) - 1);
      
      String[] salt2 = Arrays.strSplit (salt.toString ());
      
      for (int i = 0; i < num; ++i)
        rand.append (salt2[rand (0, len)]);
      
      return rand.toString ();
      
    }
    
    public static String toIntString (String part) {
      return part.replaceAll ("[,\\s+]", "");
    }
    
    public static int toInt (String part) {
      
      try {
        return Integer.parseInt (part);
      } catch (NumberFormatException e) {
        return 0;
      }
      
    }
    
    public static long toLong (String part) {
      
      try {
        return Long.parseLong (part);
      } catch (NumberFormatException e) {
        return 0;
      }
      
    }
	  
		public static int compare (int x, int y) {
			return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
		
		public static int compare (long x, long y) {
			return (x < y) ? -1 : ((x == y) ? 0 : 1);
		}
		
		public static int compareUnsigned (int x, int y) {
			return compare (x + Integer.MIN_VALUE, y + Integer.MIN_VALUE);
		}
		
    public static String addZero (String number) {
      return addZero (valueOf (number));
    }
    
    public static String addZero (int number) {
      return addZero (number, 1);
    }
    
    public static String addZero (String number, int num) {
      return addZero (valueOf (number), num);
    }
    
    public static String addZero (String number, int num, String prefix) {
      return addZero (valueOf (number), num, prefix);
    }
    
    public static String addZero (int number, int num) {
      return addZero (number, num, "0");
    }
    
    public static String addZero (int number, int num, String prefix) { // Добавляет ведущий нуль, если число number меньше, чем его num-значный эквивалент. Короче, нуль ведущий добавляет.
      
      String output = String.valueOf (number);
      if (size (output) <= num) output = prefix + output;
      
      return output;
      
    }
    
    public static int getRangeNum (int day, int total, int rangeNum) {
      
      int i;
      double range = Math.ceil ((double) total / rangeNum); // 6
      
      for (i = 1; i < rangeNum; ++i) {
        
        if (day < range * i)
          return (i - 1);
        
      }
      
      return i;
      
    }
		
	  /**
	   * It will return suitable pattern for format decimal
	   * For example: 10.2 -> return 0 | 10.23 -> return 00, | 10.235 -> return 000
	   */
	  public static StringBuilder getDecimalPattern (String str) {
		  
		  int decimalCount = size (str) - str.indexOf (",") - 1;
		  
		  StringBuilder pattern = new StringBuilder ();
		  
		  for (int i = 0; i < decimalCount && i < MAX_DECIMAL; i++)
			  pattern.append ("0");
		  
		  return pattern;
		  
	  }
		
	  public static String format (int value) {
	  	return format (String.valueOf (value));
	  }
	  
	  public static String format (String str) {
		  
		  String string;
		  
		  if (str.contains (".") || str.contains (","))
			  string = formatDecimal (str);
		  else
			  string = formatInteger (str);
		  
		  return string;
		  
	  }
		
	  private static String formatInteger (String str) {
		  
		  BigDecimal parsed = new BigDecimal (str);
		  DecimalFormat formatter = new DecimalFormat (mFormat);
		  
		  return formatter.format (parsed);
		  
	  }
		
	  private static String formatDecimal (String str) {
		  
		  if (str.equals (".")) return mPrefix + ".";
		  
		  BigDecimal parsed = new BigDecimal (prepare (str));
		  // example pattern # ###.00
		  
		  DecimalFormat formatter = new DecimalFormat (mFormat + "." + getDecimalPattern (str));
		  formatter.setRoundingMode (RoundingMode.DOWN);
		  
		  return formatter.format (parsed);
		  
	  }
		
	  public static String prepare (String str) {
		  return str.replace (",", ".");
	  }
		
  }