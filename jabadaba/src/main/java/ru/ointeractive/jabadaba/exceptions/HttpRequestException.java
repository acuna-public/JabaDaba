  package ru.ointeractive.jabadaba.exceptions;
  /*
   Created by Acuna on 01.02.2019
  */
  
  public class HttpRequestException extends Exception {
    
    private int code;
    private String content, mess, query;
    
    public HttpRequestException (Exception e) {
      super (e);
    }
    
    HttpRequestException (String mess) {
      super (mess);
    }
    
    public HttpRequestException (Exception e, String content, int code, String mess, String query) {
      
      super (e);
      
      this.code = code;
      this.mess = mess;
      this.content = content;
      this.query = query;
      
    }
    
    @Override
    public String getMessage () {
      
      if (content == null || content.equals ("")) content = super.getMessage ();
      return content;
      
    }
    
    public int getHTTPCode () {
      return code;
    }
    
    public String getHTTPMessage () {
      return mess;
    }
    
    public String getHTTPQuery () {
      return query;
    }
    
    @Override
    public Exception getCause () {
      return (Exception) super.getCause ();
    }
    
  }