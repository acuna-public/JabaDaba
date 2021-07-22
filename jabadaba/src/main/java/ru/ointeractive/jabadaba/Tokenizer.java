	package ru.ointeractive.jabadaba;
	/*
   Created by Acuna on 24.05.2020
  */
  
	import org.jsonp.JSONArray;
	import org.jsonp.JSONObject;
	
	import java.io.BufferedReader;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.io.InputStreamReader;
	import java.io.Reader;
	import java.io.StringReader;
	
	public abstract class Tokenizer {
    
    protected InputStream stream;
    protected Reader reader;
    protected String string;
    protected StringBuilder buffer = new StringBuilder ();
    protected char ch;
    protected long length;
    protected JSONObject bracketsNum = new JSONObject ();
    
    protected String spaces = " \n\t", quotes = "\"'";
    
    public Tokenizer (File file) throws IOException {
      this (new FileInputStream (file));
    }
    
    public Tokenizer (String string) throws IOException {
    	this (new StringReader (string));
    }
    
    public Tokenizer (InputStream stream) throws IOException {
    	this (new InputStreamReader (stream));
    }
    
    public Tokenizer (Reader reader) throws IOException {
    	
      this.reader = new BufferedReader (reader);
      next ();
      
    }
    
    protected final void next () throws IOException {
     
    	length = reader.read ();
    	ch = (char) length;
    	
    }
    
    protected final boolean read () {
    	return (length > 0);
    }
    
    protected final boolean isChar (String string) {
      return (string.indexOf (ch) > -1);
    }
    
    protected final boolean isSpace () {
      return isChar (spaces);
    }
    
    protected final boolean isQuote () {
      return isChar (quotes);
    }
    
    protected final void trimSpace () throws IOException {
      while (isSpace ()) next ();
    }
    
    public abstract JSONArray process () throws IOException;
    
    protected void getBlock (char start) {
    	getBlock (String.valueOf (start));
    }
    
    protected final int getBlock (String key) {
			return bracketsNum.optInt (key, 0);
    }
    
		protected void setBlock (String key) {
			
			int num = getBlock (key);
			
			if (num > 0)
				num--;
			else
				num++;
			
			bracketsNum.put (key, num);
			
		}
		
    public void close () throws IOException {
    	if (stream != null) stream.close ();
    }
    
  }