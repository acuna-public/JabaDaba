	package ru.ointeractive.jabadaba.json;
	/*
   Created by Acuna on 26.05.2020
  */
	
	public class JSONObject extends JSONArray {
		
		protected String output = "{";
		
		public void add (String key, Object value) {
			
			if (i > 0) output += ",";
			output += q + key + q + ":" + prepValue (value);
			i++;
			
		}
		
		public Object get (String key) {
			return null;
		}
		
		@Override
		public String toString () {
			
			output += "}";
			return output;
			
		}
		
	}