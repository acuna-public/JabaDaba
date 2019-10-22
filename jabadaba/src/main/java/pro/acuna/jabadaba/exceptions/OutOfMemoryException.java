  package pro.acuna.jabadaba.exceptions;
  /*
   Created by Acuna on 27.01.2019
  */
  
  public class OutOfMemoryException extends Exception {
    
    public OutOfMemoryException (Throwable e) {
      super (e);
    }
    
    @Override
    public Exception getCause () {
      return (Exception) super.getCause ();
    }
    
    @Override
    public String getMessage () {
      return "Out of memory";
    }
    
    @Override
    public String toString () {
      return getClass () + ": " + getMessage ();
    }
    
  }