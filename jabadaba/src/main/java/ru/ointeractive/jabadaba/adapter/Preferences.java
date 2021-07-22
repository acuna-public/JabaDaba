	package ru.ointeractive.jabadaba.adapter;
  /*
   Created by Acuna on 09.03.2021
  */
	
	import org.json.JSONArray;
	import org.json.JSONException;
	import org.json.JSONObject;
	
	import java.util.HashMap;
	import java.util.HashSet;
	import java.util.Map;
	import java.util.Set;
	
	import ru.ointeractive.jabadaba.Crypto;
	import ru.ointeractive.jabadaba.Int;
	
	public abstract class Preferences {
		
		public Map<String, Object> defPrefs = new HashMap<> ();
		
		public Preferences () {}
		
		public void setDefPref (String key, Object value) {
			defPrefs.put (key, value);
		}
		
		protected Object defValue (String key, Object defVal) {
			
			Object value = defPrefs.get (key);
			if (value == null) value = defVal;
			
			return value;
			
		}
		
		public String getString (String key) {
			return get (key, defValue (key, "").toString ());
		}
		
		public String get (String key, String value) {
			return get (key, value, false);
		}
		
		public String getString (String key, String value) {
			return get (key, value, false);
		}
		
		public String get (String key, String value, boolean encrypted) {
			
			try {
				return getString (key, value, encrypted);
			} catch (Crypto.DecryptException e) {
				return null;
			}
			
		}
		
		public abstract String getString (String key, String value, boolean encrypted) throws Crypto.DecryptException;
		
		public int getInt (String key) {
			return getInt (key, (int) defValue (key, 0));
		}
		
		public int get (String key, int value) {
			return getInt (key, value);
		}
		
		public abstract int getInt (String key, int value);
		
		public long getLong (String key) {
			return getLong (key, (long) defValue (key, (long) 0));
		}
		
		public long get (String key, long value) {
			return getLong (key, value);
		}
		
		public abstract long getLong (String key, long value);
		
		public float get (String key, float value) {
			return getFloat (key, value);
		}
		
		public float getFloat (String key) {
			return getFloat (key, (float) defValue (key, 0f));
		}
		
		public abstract float getFloat (String key, float value);
		
		public boolean getBool (String key) {
			return getBool (key, (boolean) defValue (key, false));
		}
		
		public boolean get (String key, boolean value) {
			return getBool (key, value);
		}
		
		public double getDouble (String key, long def) {
			return Double.longBitsToDouble (get (key, def));
		}
		
		public abstract boolean getBool (String key, boolean value);
		
		public Set<String> getStringSet (String key, Set<String> value) { // TODO
			return getStringSet (key);
		}
		
		public Set<String> getStringSet (String key) { // SUPPORT 11
			
			String str = getString (key, null);
			
			try {
				
				JSONArray data = new JSONArray (str);
				
				Set<String> result = new HashSet<> ();
				
				for (int i = 0; i < Int.size (data); i++)
					result.add (data.getString (i));
				
				return result;
				
			} catch (JSONException e) {
				return null;
			}
			
		}
		
		/*public Set<String> get (String key, Set<String> value) {
			return getStringSet (key, value);
		}*/
		
		public boolean get (String key, Map<String, Boolean> items) {
			return get (key, items.get (key));
		}
		
    /*public JSONArray getJSONArray (String key) throws JSONException {
      return new JSONArray (getString (key, defValue (key, new JSONArray ()).toString ()));
    }*/
		
		public JSONArray get (String key, JSONArray value) throws JSONException {
			return new JSONArray (get (key, value.toString ()));
		}
		
		public JSONObject get (String key, JSONObject value) throws JSONException {
			return new JSONObject (get (key, value.toString ()));
		}
		
		public void put (String key, Object value) {
			
			try {
				put (key, value, false);
			} catch (Crypto.EncryptException e) {
				// empty
			}
			
		}
		
		public Object set (String key, Object value) {
			
			try {
				set (key, value, false);
			} catch (Crypto.EncryptException e) {
				// empty
			}
			
			return value;
			
		}
		
		public abstract void put (String key, Object value, boolean encrypt) throws Crypto.EncryptException;
		
		public void set (Map<?, ?> value) {
			
			try {
				set (value, false);
			} catch (Crypto.EncryptException e) {
				// empty
			}
			
		}
		
		public void set (Map<?, ?> value, boolean encrypt) throws Crypto.EncryptException {
			
			for (Object key : value.keySet ())
				put (key.toString (), value, encrypt);
			
			apply ();
			
		}
		
		public void set (String key, Object value, boolean encrypt) throws Crypto.EncryptException {
			
			put (key, value, encrypt);
			apply ();
			
		}
		
		public abstract void apply ();
		public abstract boolean contains (String string);
		
	}