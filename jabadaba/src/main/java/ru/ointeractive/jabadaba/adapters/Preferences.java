	package ru.ointeractive.jabadaba.adapters;
  /*
   Created by Acuna on 28.04.2018
  */
	
	import org.jsonp.JSONObject;
	
	import java.io.IOException;
	
	import ru.ointeractive.jabadaba.Crypto;
	import ru.ointeractive.jabadaba.Files;
	import ru.ointeractive.jabadaba.exceptions.OutOfMemoryException;
	
	public class Preferences extends ru.ointeractive.jabadaba.adapter.Preferences {
		
		private JSONObject prefs;
		private String name;
		
		public Preferences (String name) {
			
			try {
				
				this.name = name;
				prefs = new JSONObject (Files.read (name));
				
			} catch (IOException | OutOfMemoryException e) {
				// empty
			}
			
		}
		
		@Override
		public String getString (String key, String value, boolean encrypted) throws Crypto.DecryptException {
			return prefs.optString (key, value);
		}
		
		@Override
		public int getInt (String key, int value) {
			return prefs.optInt (key, value);
		}
		
		@Override
		public long getLong (String key, long value) {
			return prefs.optLong (key, value);
		}
		
		@Override
		public float getFloat (String key, float value) {
			return prefs.optFloat (key, value);
		}
		
		@Override
		public boolean getBool (String key, boolean value) {
			return prefs.optBoolean (key, value);
		}
		
		@Override
		public void put (String key, Object value, boolean encrypt) throws Crypto.EncryptException {
			
			if (value instanceof Integer)
				prefs.put (key, (int) value);
			else if (value instanceof Long)
				prefs.put (key, (long) value);
			else if (value instanceof Float)
				prefs.put (key, (float) value);
			else if (value instanceof Double)
				prefs.put (key, Double.doubleToRawLongBits ((double) value));
			else if (value instanceof Boolean)
				prefs.put (key, (boolean) value);
			else {
				
				String str = String.valueOf (value);
				prefs.put (key, str);
				
			}
			
		}
		
		@Override
		public void apply () {
			
			try {
				Files.write (prefs, name);
			} catch (IOException e) {
				// empty
			}
			
		}
		
		@Override
		public boolean contains (String key) {
			return prefs.has (key);
		}
		
	}