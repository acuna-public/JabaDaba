  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 20.07.2017
  */
  
  import java.io.BufferedReader;
  import java.io.DataOutputStream;
  import java.io.IOException;
  import java.io.InputStreamReader;
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.List;
  import java.util.Map;
  
  public class Console {
    
    public int sleep = 0;
    private String shell = "";
    public static final String sh = "sh", su = "su";
    public String prefix = "";
    
    private Process process;
    
    private Listener listener;
    private ProcListener procListener;
    private CompleteListener completeListener;
    
    public static class ConsoleException extends Exception {
      
      ConsoleException (Exception e) {
        super (e);
      }
      
      ConsoleException (List<?> e) {
        super (Arrays.implode (e));
      }
      
      ConsoleException (String e) {
        super (e);
      }
      
      @Override
      public Exception getCause () {
        return (Exception) super.getCause ();
      }
      
    }
    
    public interface Listener {
      
      void onExecute (String line, int i);
      void onSuccess (String line, int i);
      void onError (String line, int i);
      
    }
    
    public interface ProcListener {
      void onProcess (String line, int i);
    }
    
    public interface CompleteListener {
      
      void onSuccess (String line, int i);
      void onError (String line, int i);
      void onComplete (int code);
      
    }
    
    public Console () {}
    
    public Console (Listener listener) {
      addListener (listener);
    }
    
    public Console (ProcListener listener) {
      addListener (listener);
    }
    
    public Console (CompleteListener listener) {
      addListener (listener);
    }
    
    public Console addListener (Listener listener) {
      
      this.listener = listener;
      return this;
      
    }
    
    public Console addListener (ProcListener listener) {
      
      procListener = listener;
      return this;
      
    }
    
    public Console addListener (CompleteListener listener) {
      
      completeListener = listener;
      return this;
      
    }
    
    private Map<String, String> replaces = new HashMap<> ();
    
    public Console replace (String find, String replace) {
      
      replaces.put (find, replace);
      return this;
      
    }
    
    public Console shell (String shell) throws ConsoleException {
      return shell (shell, true);
    }
    
    public Console shell (String shell, boolean external) throws ConsoleException {
      
      if (external) this.shell = shell;
      
      try {
        process = Runtime.getRuntime ().exec (shell);
      } catch (IOException e) {
        throw new ConsoleException (e);
      }
      
      return this;
      
    }
    
    private int i = 0;
    private DataOutputStream os;
    
    public synchronized void open () {
      
      if (!shell.equals (""))
        os = new DataOutputStream (process.getOutputStream ());
      
    }
    
    public synchronized Console add (List<String> cmds) throws ConsoleException {
      
      for (String cmd : cmds)
        add (cmd);
      
      return this;
      
    }
    
    public synchronized Console add (String[] cmds) throws ConsoleException {
      
      for (String cmd : cmds)
        add (cmd);
      
      return this;
      
    }
    
    public synchronized Console add (String cmd) throws ConsoleException {
      
      for (String key : replaces.keySet ())
        cmd = cmd.replace (key, replaces.get (key));
      
      try {
        
        if (!shell.equals ("")) {
          
          cmd = (!prefix.equals ("") ? prefix + " " : "") + cmd;
          os.writeBytes (cmd + "\n");
          
        } else shell (cmd, false);
        
        if (sleep > 0) Thread.sleep (sleep);
        
        if (listener != null)
          listener.onExecute (cmd, i);
        
        ++i;
        
        return this;
        
      } catch (IOException | InterruptedException e) {
        throw new ConsoleException (e);
      }
      
    }
    
    public synchronized List<String> query (String cmd) throws ConsoleException {
      
      open ();
      add (cmd);
      
      List<String> response = process ();
      
      close ();
      
      return response;
      
    }
    
    public synchronized List<String> query (List<String> cmds) throws ConsoleException {
      
      open ();
      add (cmds);
      
      List<String> response = process ();
      
      close ();
      
      return response;
      
    }
    
    public synchronized List<String> query (String[] cmds) throws ConsoleException {
      
      open ();
      add (cmds);
      
      List<String> response = process ();
      
      close ();
      
      return response;
      
    }
    
    public synchronized List<String> process () throws ConsoleException {
      
      i = 0;
      
      List<String> response = new ArrayList<> ();
      
      try {
        
        int i = 0;
        String line;
        
        if (!shell.equals (""))
          os.writeBytes ("exit\n");
        
        BufferedReader inResult = new BufferedReader (new InputStreamReader (process.getInputStream ()));
        
        while ((line = inResult.readLine ()) != null) {
          
          if (listener != null)
            listener.onSuccess (line, i);
          else if (procListener != null)
            procListener.onProcess (line, i);
          else if (completeListener != null)
            completeListener.onSuccess (line, i);
          else
            response.add (line);
          
          ++i;
          
        }
        
        i = 0;
        BufferedReader inError = new BufferedReader (new InputStreamReader (process.getErrorStream ()));
        
        while ((line = inError.readLine ()) != null) {
          
          if (listener != null)
            listener.onError (line, i);
          else if (procListener != null)
            procListener.onProcess (line, i);
          else if (completeListener != null)
            completeListener.onError (line, i);
          else
            response.add (line);
          
          ++i;
          
        }
        
        inResult.close ();
        inError.close ();
        
        if (completeListener != null)
          completeListener.onComplete (process.waitFor ());
        
      } catch (IOException | InterruptedException e) {
        throw new ConsoleException (e);
      }
      
      return response;
      
    }
    
    public void close () throws ConsoleException {
      
      if (os != null) {
        
        try {
          
          os.flush ();
          os.close ();
          
        } catch (IOException e) {
          throw new ConsoleException (e);
        }
        
      }
      
    }
    
  }