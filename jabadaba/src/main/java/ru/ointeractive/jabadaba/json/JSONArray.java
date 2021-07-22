	package ru.ointeractive.jabadaba.json;
	/*
   Created by Acuna on 26.05.2020
  */
	
	// Using raw text JSON, not ArrayList, use it when ArrayList behaviour is weird
	
	import ru.ointeractive.jabadaba.Int;
	import ru.ointeractive.jabadaba.Objects;
	
	public class JSONArray {
		
		protected String q = "\"";
		protected String output = "[";
		protected int i = 0;
		
		public void add (int key, Object value) {
			add (value); // TODO
		}
		
		public void add (Object value) {
			
			if (i > 0) output += ",";
			output += prepValue (value);
			i++;
			
		}
		
		protected String prepValue (Object value) {
			
			String val = value.toString ();
			
			if (val.equals ("") || (!Int.isNumeric (val) && val.charAt (0) != '[' && val.charAt (0) != '{' && !Objects.isBool (val)))
				val = q + val + q;
			
			return val;
			
		}
		
		@Override
		public String toString () {
			
			output += "]";
			return output;
			
		}
		
	}