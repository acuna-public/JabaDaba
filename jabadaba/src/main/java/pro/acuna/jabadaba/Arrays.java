  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 17.07.2017
  */
  
  import org.json.JSONArray;
  import org.json.JSONException;
  import org.json.JSONObject;
  
  import java.io.BufferedReader;
  import java.io.ByteArrayOutputStream;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.UnsupportedEncodingException;
  import java.util.ArrayList;
  import java.util.LinkedHashMap;
  import java.util.List;
  import java.util.Map;
  
  public class Arrays {
    
    public static final String[] brArray = new String[] {"<br />", "<br>"};
    
    public static List<?> toList (String[] string) {
      return java.util.Arrays.asList (string);
    }
    
    public static List<?> explodeArrayList (String preg, String string) {
      return toList (explode (preg, string));
    }
    
    public static List<?> toList (JSONArray array) {
      
      List<Object> list = new ArrayList<> ();
      
      for (int i = 0; i < Int.size (array); i++)
        list.add (array.get (i));
      
      return list;
      
    }
    
    public static String[] pregExplode (String preg, String string) {
      return ((string == null || string.equals ("")) ? new String[0] : string.split (preg));
    }
    
    public static List<String> explode (String str, int indexStart, int indexEnd, List<String> output) {
      
      if (indexEnd < 0) indexEnd = Int.size (str);
      
      String sequence = str.substring (indexStart, indexEnd);
      output.add (sequence);
      
      return output;
      
    }
    
    public static List<String> explode (String symb, String str) {
      return explode (symb, str, new ArrayList<String> ());
    }
    
    public static List<String> explode (String symb, String str, List<String> output) {
      
      int sLength = Int.size (symb);
      int indexStart = 0;
      int index = str.indexOf (symb);
      
      output = explode (str, indexStart, index, output);
      
      while (index >= 0) {
        
        indexStart = index + sLength;
        index = str.indexOf (symb, index + 1);
        
        output = explode (str, indexStart, index, output);
        
      }
      
      return output;
      
    }
    
    public static List<CharSequence> explode (CharSequence text, int indexStart, int indexEnd, List<CharSequence> output) {
      
      if (indexEnd < 0) indexEnd = Int.size (text);
      CharSequence word = text.subSequence (indexStart, indexEnd);
      
      output.add (word);
      
      return output;
      
    }
    
    public static List<CharSequence> explode (String symb, CharSequence text) {
      
      List<CharSequence> output = new ArrayList<> ();
      String str = text.toString ();
      
      int sLength = 1;
      int indexStart = 0;
      int indexEnd = str.indexOf (symb);
      
      output = explode (text, indexStart, indexEnd, output);
      
      while (indexEnd >= 0) {
        
        indexStart = indexEnd + sLength;
        indexEnd = str.indexOf (symb, indexEnd + 1);
        
        output = explode (text, indexStart, indexEnd, output);
        
      }
      
      return output;
      
    }
    
    public static String implode (Object[] string) {
      return implode ("\n", string);
    }
    
    public static String implode (String preg, Object[] string) {
      return implode (preg, string, 0);
    }
    
    public static String implode (String preg, Object[] string, int min) {
      return implode (preg, string, min, 0);
    }
    
    public static String implode (String preg, Object[] string, int min, int max) {
      
      StringBuilder output = new StringBuilder ();
      
      int length = Int.size (string);
      
      if (min < 0) min = (length + min);
      
      if (max == 0)
        max = length;
      else if (max < 0)
        max = (length + max);
      
      if (length > 0) {
        
        for (int i = min; i < max; ++i) {
          
          if (!string[i].equals (preg)) {
            
            if (i > min) output.append (preg);
            output.append (string[i]);
            
          }
          
        }
        
      }
      
      return output.toString ();
      
    }
    
    public static String implode (List<?> string) {
      return implode ("\n", string);
    }
    
    public static String implode (String preg, List<?> string) {
      return implode (preg, string, 0);
    }
    
    public static String implode (String preg, List<?> string, int min) {
      return implode (preg, string, min, 0);
    }
    
    public static String implode (String preg, List<?> string, int min, int max) {
      
      StringBuilder output = new StringBuilder ();
      
      int length = Int.size (string);
      
      if (min < 0) min = (length + min);
      
      if (max == 0)
        max = length;
      else if (max < 0)
        max = (length + max);
      
      if (length > 0)
        for (int i = min; i < max; ++i) {
          
          if (i > 0) output.append (preg);
          
          Object obj = string.get (i);
          
          if (obj instanceof Exception)
            output.append (((Exception) obj).getMessage ());
          else
            output.append (obj);
          
        }
      
      return output.toString ();
      
    }
    
    public static String implode (Map<?, ?> items) {
      return implode ("\n", items);
    }
    
    public static String implode (String preg, Map<?, ?> items) {
      
      String output = "";
      
      for (Object key : items.keySet ())
        output += key + ": " + items.get (key) + preg;
      
      return output;
      
    }
    
    public static boolean contains (Object value, Object[] items) {
      
      for (Object item : items)
        if (item.equals (value))
          return true;
      
      return false;
      
    }
    
    public static boolean contains (Object value, List<?> arr) {
      return arr.contains (value);
    }
    
    public static boolean contains (Object value, Map<?, ?> arr) {
      return arr.containsKey (value);
    }
    
    public static boolean contains (Object value, JSONArray arr) {
      
      for (int i = 0; i < Int.size (arr); ++i)
        if (arr.opt (i).equals (value))
          return true;
      
      return false;
      
    }
    
    public static int search (String value, String[] arr) {
      return java.util.Arrays.binarySearch (arr, value);
    }
    
    public static String[] concat (String[] first, String[]... rest) {
      
      int totalLength = Int.size (first);
      for (String[] array : rest) totalLength += Int.size (array);
      
      String[] result = java.util.Arrays.copyOf (first, totalLength);
      int offset = Int.size (first);
      
      for (String[] array : rest) {
        
        java.lang.System.arraycopy (array, 0, result, offset, Int.size (array));
        offset += Int.size (array);
        
      }
      
      return result;
    }
    
    public static String extend (String[] params) {
      return ((Int.size (params) > 1) ? params[1] : "");
    }
    
    public static Object extend (List<?> params) {
      return ((Int.size (params) > 1) ? params.get (1) : "");
    }
    
    public static String extend (String params) {
      
      if (params == null) params = "";
      return params;
      
    }
  
        /*public static String[] extend (String[] params) {
          return extend (params, Int.size (params));
        }*/
    
    public static String[] extend (String[] params, int num) {
      
      String[] output = new String[num];
      
      for (int i = 0; i < num; ++i) {
        
        if (i >= Int.size (params) || params[i] == null)
          output[i] = "";
        else
          output[i] = params[i];
        
      }
      
      return output;
      
    }
    
    public static JSONArray extend (JSONArray params, int num) {
      
      for (int i = 0; i < num; ++i)
        if (i >= Int.size (params))
          params.put (i, "");
      
      return params;
      
    }
    
    public static JSONArray extend (JSONArray params, JSONArray prefs) {
      
      for (int i = 0; i < Int.size (prefs); ++i)
        if (i >= Int.size (params))
          params.put (i, prefs.get (i));
      
      return params;
      
    }
    
    public static JSONArray extend (JSONArray params, JSONArray prefs, int start) {
      
      JSONArray output = new JSONArray ();
      
      for (int i = 0; i < Int.size (prefs); ++i) {
        
        if (i == (start - 1) || i >= Int.size (params))
          output.put (i, prefs.get (i));
        else
          output.put (i, params.get ((i == 0 ? i - 1 : i)));
        
      }
      
      return output;
      
    }
    
    public static JSONObject extend (JSONObject params, JSONObject prefs) {
      
      JSONArray keys = prefs.names ();
      JSONObject output = new JSONObject ();
      
      for (int i = 0; i < Int.size (keys); ++i) {
        
        String key = keys.getString (i);
        
        if (!params.has (key))
          output.put (key, prefs.get (key));
        else
          output.put (key, params.get (key));
        
      }
      
      return output;
      
    }
    
    public static JSONObject extend (JSONObject params, Map<?, ?> prefs) {
      
      for (Object key : prefs.keySet ()) {
        
        if (!params.has (key.toString ()))
          params.put (key.toString (), prefs.get (key));
        
      }
      
      return params;
      
    }
    
    public static JSONArray extend (JSONArray params, Object[] prefs) {
      
      for (int i = 0; i < Int.size (prefs); ++i)
        if (i >= Int.size (params))
          params.put (i, prefs[i]);
      
      return params;
      
    }
    
    public static List<String> toStringList (JSONArray item) {
      
      List<String> data = new ArrayList<> ();
      
      for (int i = 0; i < Int.size (item); ++i)
        data.add (item.getString (i));
      
      return data;
      
    }
    
    public static List<String> toStringList (org.jsonp.JSONArray item) {
      
      List<String> data = new ArrayList<> ();
      
      for (int i = 0; i < Int.size (item); ++i)
        data.add (item.getString (i));
      
      return data;
      
    }
    
    public static List<?> toList (String item) {
      
      List<Object> list = new ArrayList<> ();
      list.add (item);
      
      return list;
      
    }
    
    public static int getKey (String key, String[] array) {
      
      for (int i = 0; i < Int.size (array); ++i)
        if (array[i].equals (key)) return i;
      
      return -1;
      
    }
    
    public static int getKey (Object key, JSONArray array) {
      
      for (int i = 0; i < Int.size (array); ++i)
        if (array.get (i).equals (key)) return i;
      
      return -1;
      
    }
    
    public static int getKey (Object key, org.jsonp.JSONArray array) {
      
      for (int i = 0; i < Int.size (array); ++i)
        if (array.get (i).equals (key)) return i;
      
      return -1;
      
    }
    
    public static int getKey (Object key, List<?> array) {
      return array.indexOf (key);
    }
    
    public static String getKey (int i, JSONObject data) throws JSONException {
      return data.names ().getString (i);
    }
    
    public static String getKey (int i, org.jsonp.JSONObject data) throws JSONException {
      return data.names ().getString (i);
    }
    
    public static String[] strSplit (String str) {
      
      int length = Int.size (str);
      String[] output = new String[length];
      
      for (int i = 0; i < length; ++i)
        if ((i + 1) < length)
          output[i] = str.substring (i, (i + 1));
        else
          output[i] = str.substring (i);
      
      return output;
      
    }
    
    public static List<Object> arrayCopy (List<Object> from, List<Object> to) {
      
      from.addAll (to);
      return from;
      
    }
    
    public static List<?> compare (List<?> array1, List<?> array2) {
      
      List<Object> no = new ArrayList<> ();
      
      for (Object item : array1)
        if (!contains (item, array2))
          no.add (item);
      
      return no;
      
    }
    
    public static Map<String, Object> toMap (JSONObject data) throws JSONException {
      return toMap (data, new LinkedHashMap<String, Object> ());
    }
    
    public static Map<String, Object> toMap (JSONObject data, Map<String, Object> output) throws JSONException {
      
      JSONArray keys = data.names ();
      
      for (int i = 0; i < Int.size (keys); ++i) {
        
        String key = keys.getString (i);
        output.put (key, data.get (key));
        
      }
      
      return output;
      
    }
    
    public static String[] toStringArray (String item) {
      return new String[] {item};
    }
    
    public static Object[] toArray (List<?> items) {
      return items.toArray (new Object[0]);
    }
    
    public static String[] toStringArray (List<?> items) {
      
      String[] data = new String[Int.size (items)];
      
      for (int i = 0; i < Int.size (items); ++i)
        data[i] = items.get (i).toString ();
      
      return data;
      
    }
    
    public static Object[] toArray (JSONArray item) throws JSONException {
      
      Object[] strings = new Object[Int.size (item)];
      
      for (int i = 0; i < Int.size (item); ++i)
        strings[i] = item.get (i);
      
      return strings;
      
    }
    
    public static String[] toStringArray (JSONArray item) throws JSONException {
      
      String[] strings = new String[Int.size (item)];
      
      for (int i = 0; i < Int.size (item); ++i)
        strings[i] = String.valueOf (item.get (i));
      
      return strings;
      
    }
    
    public static String[] toStringArray (JSONObject items) throws JSONException {
      
      JSONArray keys = items.names ();
      String[] strings = new String[Int.size (keys)];
      
      for (int i = 0; i < Int.size (keys); ++i)
        strings[i] = String.valueOf (items.get (keys.getString (i)));
      
      return strings;
      
    }
    
    public static String[] toStringArray (JSONObject item, String[] keys) throws JSONException {
      
      String[] strings = new String[Int.size (keys)];
      
      for (int i = 0; i < Int.size (keys); ++i)
        strings[i] = String.valueOf (item.get (keys[i]));
      
      return strings;
      
    }
    
    public static String[] toStringArray (JSONObject item, JSONObject defs) throws JSONException {
      
      JSONArray keys = defs.names ();
      String[] strings = new String[Int.size (keys)];
      
      for (int i = 0; i < Int.size (keys); ++i)
        strings[i] = String.valueOf (item.get (keys.getString (i)));
      
      return strings;
      
    }
    
    public static String[] toStringArray (JSONObject item, Map<?, ?> prefs) throws JSONException {
      
      int i = 0;
      String[] strings = new String[Int.size (prefs)];
      
      for (Object key : prefs.keySet ()) {
        
        strings[i] = String.valueOf (item.get (key.toString ()));
        ++i;
        
      }
      
      return strings;
      
    }
    
    public static String[] toStringArray (JSONObject item, JSONArray keys) throws JSONException {
      
      String[] strings = new String[Int.size (keys)];
      
      for (int i = 0; i < Int.size (keys); ++i)
        strings[i] = String.valueOf (item.get (keys.getString (i)));
      
      return strings;
      
    }
    
    public static String[] toStringArray (Map<?, ?> items) {
      
      int i = 0;
      String[] strings = new String[Int.size (items)];
      
      for (Object key : items.keySet ()) {
        
        strings[i] = items.get (key).toString ();
        ++i;
        
      }
      
      return strings;
      
    }
    
    public static Integer[] toIntArray (List<?> items) {
      
      Integer[] data = new Integer[Int.size (items)];
      
      for (int i = 0; i < Int.size (items); ++i)
        data[i] = Int.intval (items.get (i));
      
      return data;
      
    }
    
    public static String[] getValues (Map<?, ?> item, String[] keys) {
      
      String[] strings = new String[Int.size (keys)];
      
      for (int i = 0; i < Int.size (keys); ++i)
        strings[i] = item.get (keys[i]).toString ();
      
      return strings;
      
    }
    
    public static String[] getKeys (Map<?, ?> item) {
      
      int i = 0;
      int length = Int.size (item);
      String[] strings = new String[length];
      
      for (Object key : item.keySet ()) {
        
        strings[i] = key.toString ();
        ++i;
        
      }
      
      return strings;
      
    }
    
    public static JSONArray findValue (int key, JSONArray array) throws JSONException {
      
      try {
        array.get (key);
      } catch (JSONException e) {
        array.put (new JSONArray ());
      }
      
      return array;
      
    }
    
    public static JSONArray findValue (String key, JSONObject array) throws JSONException {
      return (array.has (key) ? array.getJSONArray (key) : new JSONArray ());
    }
    
    public static String rand (String[] array) {
      return array[randKey (array)];
    }
    
    public static String[] rand (String[] array, int num) {
      
      String[] output = new String[num];
      for (int i = 0; i < num; ++i)
        output[i] = rand (array);
      
      return output;
      
    }
    
    public static int randKey (String[] array) {
      
      int count = Int.size (array);
      if (count > 0) count = (count - 1);
      
      return Int.rand (0, count);
      
    }
    
    public static Integer[] randKey (String[] array, int num) {
      
      Integer[] output = new Integer[num];
      for (int i = 0; i < num; ++i)
        output[i] = randKey (array);
      
      return output;
      
    }
    
    public static byte[] append (byte[]... arrays) throws IOException {
      
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream ();
      
      for (byte[] array : arrays)
        outputStream.write (array);
      
      return outputStream.toByteArray ();
      
    }
    
    public static byte[] toByteArray (JSONObject value) throws UnsupportedEncodingException {
      return toByteArray (value.toString ());
    }
    
    public static byte[] toByteArray (CharSequence value) throws UnsupportedEncodingException {
      return toByteArray (value, Strings.DEF_CHARSET);
    }
    
    public static byte[] toByteArray (CharSequence value, String charset) throws UnsupportedEncodingException {
      return ((value != null) ? value.toString ().getBytes (charset) : new byte[0]);
    }
    
    public static byte[] toByteArray (InputStream in) throws IOException {
      return toByteArray (in, new ByteArrayOutputStream ());
    }
    
    public static byte[] toByteArray (InputStream in, ByteArrayOutputStream out) throws IOException {
      return toByteArray (in, out, 2048);
    }
    
    public static byte[] toByteArray (InputStream in, int size) throws IOException {
      return toByteArray (in, new ByteArrayOutputStream (), size);
    }
    
    public static byte[] toByteArray (InputStream in, ByteArrayOutputStream out, int size) throws IOException {
      
      Streams.copy (in, out, size);
      return out.toByteArray ();
      
    }
    
    public static List<Object> toList (InputStream stream) throws IOException {
      return toList (stream, new ArrayList<> ());
    }
    
    public static List<Object> toList (InputStream stream, List<Object> output) throws IOException {
      return toList (stream, output, Strings.DEF_CHARSET);
    }
    
    public static List<Object> toList (InputStream stream, List<Object> output, String charset) throws IOException {
      return toList (stream, output, charset, Files.BUFFER_SIZE);
    }
    
    public static List<Object> toList (InputStream stream, List<Object> output, String charset, int buffer) throws IOException {
      
      BufferedReader br = Buffer.toBufferedReader (stream, charset, buffer);
      
      Object str;
      
      while ((str = br.readLine ()) != null) { output.add (str); }
      
      br.close ();
      
      return output;
      
    }
    
    public static List<String> toStringList (InputStream stream) throws IOException {
      return toStringList (stream, new ArrayList<String> ());
    }
    
    public static List<String> toStringList (InputStream stream, List<String> output) throws IOException {
      return toStringList (stream, output, Strings.DEF_CHARSET);
    }
    
    public static List<String> toStringList (InputStream stream, List<String> output, String charset) throws IOException {
      return toStringList (stream, output, charset, Files.BUFFER_SIZE);
    }
    
    public static List<String> toStringList (InputStream stream, List<String> output, String charset, int buffer) throws IOException {
      
      BufferedReader br = Buffer.toBufferedReader (stream, charset, buffer);
      
      String str;
      
      while ((str = br.readLine ()) != null) { output.add (str); }
      
      br.close ();
      
      return output;
      
    }
    
    public static String contains (String key, String[] array, String defValue) {
      
      if (!contains (key, array)) key = defValue;
      return key;
      
    }
    
    public static int contains (int key, Integer[] array, int defValue) {
      
      if (!contains (key, array)) key = defValue;
      return key;
      
    }
    
    public static List<?> add (String value, List<Object> array) {
      
      if (!contains (value, array)) array.add (value);
      return array;
      
    }
    
    public static List<Object> toList (Object array) {
      
      List<Object> result = new ArrayList<> ();
      
      if (!(array instanceof Object[])) {
        
        if (array instanceof int[])
          for (int value : (int[]) array) result.add (value);
        else if (array instanceof boolean[])
          for (boolean value : (boolean[]) array) result.add (value);
        else if (array instanceof long[])
          for (long value : (long[]) array) result.add (value);
        else if (array instanceof float[])
          for (float value : (float[]) array) result.add (value);
        else if (array instanceof double[])
          for (double value : (double[]) array) result.add (value);
        else if (array instanceof short[])
          for (short value : (short[]) array) result.add (value);
        else if (array instanceof byte[])
          for (byte value : (byte[]) array) result.add (value);
        else if (array instanceof char[])
          for (char value : (char[]) array) result.add (value);
        
      } else result = java.util.Arrays.asList ((Object[]) array);
      
      return result;
      
    }
    
    public static JSONArray concat (JSONArray array1, JSONArray array2) {
      
      for (int i = 0; i < Int.size (array1); ++i) {
        
        Object value = array1.get (i);
        if (!contains (value, array2)) array2.put (value);
        
      }
      
      return array2;
      
    }
    
    public static JSONArray toJSONArray (String item) {
      
      JSONArray array = new JSONArray ();
      array.put (item);
      
      return array;
      
    }
    
    public static JSONArray toJSONArray (Object[] params) {
      
      JSONArray array = new JSONArray ();
      for (Object param : params) array.put (param);
      
      return array;
      
    }
    
    public static JSONArray toJSONArray (List<?> items) {
      
      JSONArray array = new JSONArray ();
      for (Object item : items) array.put (item);
      
      return array;
      
    }
    
    public static JSONArray toJSONArray (Map<?, ?> params) {
      
      JSONArray array = new JSONArray ();
      for (Object key : params.keySet ()) array.put (params.get (key));
      
      return array;
      
    }
    
    public static JSONArray toJSONArray (JSONObject params) {
      
      JSONArray keys = params.names ();
      JSONArray array = new JSONArray ();
      
      for (int i = 0; i < Int.size (keys); ++i)
        array.put (params.get (keys.getString (i)));
      
      return array;
      
    }
    
    public static String[] explodeSent (String text) {
      return pregExplode ("(?i)\\s*(\\.|!|\\?|\\n|<br.*?>)\\s*", text);
    }
    
    public static boolean hasKey (int key, List<?> array) {
      
      try {
        
        array.get (key);
        return true;
        
      } catch (ArrayIndexOutOfBoundsException e) {
        return false;
      }
      
    }
    
    public static int startKey (int key) {
      return Int.correct (key, 0);
    }
    
    public static int prevKey (int key) {
      return startKey (key - 1);
    }
    
    public static int nextKey (int key, int count) {
      
      key = key + 1;
      if (key >= count) key = prevKey (count);
      
      return key;
      
    }
    
    public static int endKey (List<?> array) {
      return (Int.size (array) - 1);
    }
    
    public static int endKey (int[] array) {
      return (Int.size (array) - 1);
    }
    
    public static int endValue (int[] array) {
      return array[endKey (array)];
    }
    
    public static int endKey (long[] array) {
      return (Int.size (array) - 1);
    }
    
    public static long endValue (long[] array) {
      return array[endKey (array)];
    }
    
    public static int endKey (Object[] array) {
      return (Int.size (array) - 1);
    }
    
    public static String endValue (String[] array) {
      return array[endKey (array)];
    }
    
    public static int endKey (JSONArray array) {
      return (Int.size (array) - 1);
    }
    
    public static Object endValue (JSONArray array) {
      return array.get (endKey (array));
    }
    
    public static int endKey (JSONObject array) {
      return (Int.size (array) - 1);
    }
    
    public static Object endValue (JSONObject array) {
      
      JSONArray keys = array.names ();
      return array.get (keys.getString (endKey (keys)));
      
    }
    
    public static int endKey (org.jsonp.JSONArray array) {
      return (Int.size (array) - 1);
    }
    
    public static Object endValue (org.jsonp.JSONArray array) {
      return array.get (endKey (array));
    }
    
    public static int endKey (org.jsonp.JSONObject array) {
      return (Int.size (array) - 1);
    }
    
    public static Object endValue (org.jsonp.JSONObject array) {
      
      org.jsonp.JSONArray keys = array.names ();
      return array.get (keys.getString (endKey (keys)));
      
    }
  
        /*public static JSONObject sort (JSONObject array) {
  
          JSONArray keys = array.names ();
          Collections.sort (toStringList (keys));
  
          JSONObject array2 = new JSONObject ();
  
          for (int i = 0; i < Int.size (keys); i++) {
  
            String key = keys.getString (i);
            array2.put (key, array.get (key));
  
          }
  
          return array2;
  
        }*/
    
    private static int total = 0;
    
    public static Object getValue (int i, List<?> values) {
      
      int size = Int.size (values);
      
      try {
        return values.get ((i >= size) ? (i - total) : i);
      } catch (IndexOutOfBoundsException e) {
        
        total = i;
        return getValue (i, values);
        
      }
      
    }
    
    public static Object getKey (int key, Map<?, ?> values) {
      
      int i = 0;
      
      for (Object key2 : values.keySet ()) {
        
        if (i == key) return key2;
        ++i;
        
      }
      
      return null;
      
    }
    
    public static Object getKey (Object key, Map<?, ?> values) {
      
      for (Object key2 : values.keySet ()) {
        
        Object value = values.get (key2);
        if (key.equals (value)) return key2;
        
      }
      
      return null;
      
    }
    
    public static JSONObject toJSONObject (String data) {
      
      try {
        return new JSONObject (data);
      } catch (JSONException e) {
        return new JSONObject ();
      }
      
    }
    
    public static JSONObject toJSONObject (Map<?, ?> items) {
      return new JSONObject (items);
    }
    
    public static JSONObject toJSONObject (JSONArray params, String[] prefs) {
      
      JSONObject output = new JSONObject ();
      
      for (int i = 0; i < Int.size (prefs); ++i)
        output.put (prefs[i], params.get (i));
      
      return output;
      
    }
    
    public static JSONArray put (Object value, JSONArray data) throws JSONException {
      
      if (!contains (value, data)) data.put (value);
      return data;
      
    }
    
    public static JSONObject put (String key, Object value, JSONObject data) {
      
      try {
        data.put (key, value);
      } catch (JSONException e) {
        // ignore
      }
      
      return data;
      
    }
    
    public static List<?> order (List<?> array1, List<?> array2) {
      return order (array1, array2, new ArrayList<> ());
    }
    
    public static List<?> order (List<?> array1, List<?> array2, List<Object> output) {
      
      int i = 0;
      
      for (Object object : array2) {
        
        if (!contains (object, array1)) {
          
          output.add (array1.get (i));
          ++i;
          
        } else output.add (object);
        
      }
      
      return output;
      
    }
    
    public static JSONArray order (JSONArray array1, List<?> array2) {
      
      JSONArray output = new JSONArray ();
      
      for (Object object : array2)
        if (contains (object, array1))
          output.put (object);
      
      return output;
      
    }
    
    public static JSONObject getJSONObject (String key, JSONObject array) throws JSONException {
      
      JSONObject value;
      
      if (array.has (key))
        value = array.getJSONObject (key);
      else
        value = new JSONObject ();
      
      return value;
      
    }
    
    public static String getString (String key, JSONObject result) throws JSONException {
      return (result.has (key) ? result.getString (key) : "");
    }
    
    public static String[] getStringArray (String key, JSONObject result) throws JSONException {
      return (result.has (key) ? toStringArray (result.getJSONArray (key)) : new String[0]);
    }
    
    public static List<?> getStringList (String key, JSONObject result) throws JSONException {
      return (result.has (key) ? toStringList (result.getJSONArray (key)) : new ArrayList<> ());
    }
    
    public static JSONArray getJSONArray (String key, JSONObject result) throws JSONException {
      return (result.has (key) ? result.getJSONArray (key) : new JSONArray ());
    }
    
    public static List<?> unique (List<?> items) {
      
      List<Object> output = new ArrayList<> ();
      
      for (Object item : items)
        if (!contains (item, output))
          output.add (item);
      
      return output;
      
    }
    
    public static Object getPrev (Object uid, List<?> list) {
      
      int idx = getKey (uid, list);
      
      if (idx <= 0)
        return null;
      else
        return list.get (idx - 1);
      
    }
    
    public static Object getNext (Object uid, List<Object> list) {
      
      int idx = getKey (uid, list);
      
      if (idx < 0 || idx + 1 == Int.size (list))
        return null;
      else
        return list.get (idx + 1);
      
    }
    
  }