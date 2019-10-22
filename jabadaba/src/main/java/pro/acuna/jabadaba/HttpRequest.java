  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 04.01.2019
  */
  
  import java.io.BufferedOutputStream;
  import java.io.DataOutputStream;
  import java.io.File;
  import java.io.FileInputStream;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.OutputStream;
  import java.net.HttpURLConnection;
  import java.net.ProtocolException;
  import java.net.URL;
  import java.net.URLEncoder;
  import java.util.HashMap;
  import java.util.LinkedHashMap;
  import java.util.List;
  import java.util.Map;
  import java.util.zip.GZIPInputStream;
  
  import pro.acuna.jabadaba.exceptions.HttpRequestException;
  import pro.acuna.jabadaba.exceptions.OutOfMemoryException;
  
  public class HttpRequest {
    
    private HttpURLConnection conn;
    private int code = -1;
    private boolean isConnected = false;
    
    private Map<String, Object> headers = new LinkedHashMap<> ();
    
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_TRACE = "TRACE";
    
    public HttpRequest (String method, String url) throws HttpRequestException {
      this (method, url, Net.getUserAgent ());
    }
    
    public HttpRequest (String method, String url, String useragent) throws HttpRequestException {
      this (method, url, new LinkedHashMap<String, Object> (), useragent);
    }
    
    public HttpRequest (String method, String url, String useragent, int timeout) throws HttpRequestException {
      this (method, url, new LinkedHashMap<String, Object> (), useragent, timeout);
    }
    
    public HttpRequest (String method, String url, Map<String, Object> params) throws HttpRequestException {
      this (method, url, params, Net.getUserAgent ());
    }
    
    public HttpRequest (String method, String url, Map<String, Object> params, String userAgent) throws HttpRequestException {
      this (method, url, params, userAgent, Net.defTimeout);
    }
    
    public HttpRequest (String method, String mUrl, Map<String, Object> params, String userAgent, int timeout) throws HttpRequestException {
      
      try {
        
        URL  url = new URL (mUrl + (params != null ? Net.urlQueryEncode (params) : ""));
        init (method, url, userAgent, timeout);
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    public HttpRequest (String method, URL url, String userAgent) throws HttpRequestException {
      this (method, url, userAgent, Net.defTimeout);
    }
    
    public HttpRequest (String method, URL url, String userAgent, int timeout) throws HttpRequestException {
      init (method, url, userAgent, timeout);
    }
    
    private HttpRequest init (String method, URL url, String userAgent, int timeout) throws HttpRequestException {
      
      try {
        
        this.method = method;
        
        conn = (HttpURLConnection) url.openConnection ();
        
        setUserAgent (userAgent);
        
        conn.setConnectTimeout (timeout);
        conn.setReadTimeout (timeout);
        conn.setInstanceFollowRedirects (true);
        
        String[] allowedMethods = new String[] {METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE, METHOD_HEAD, METHOD_OPTIONS, METHOD_TRACE};
        
        if (!Arrays.contains (method, allowedMethods)) {
          
          setMethod (METHOD_POST);
          setHeader ("X-HTTP-Method-Override", method);
          
        } else setMethod (method);
        
        return this;
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    public HttpRequest setUserAgent (String userAgent) {
      
      if (userAgent == null) userAgent = Net.getUserAgent ();
      return setHeader ("User-Agent", userAgent);
      
    }
    
    private String cookies;
    
    public HttpRequest setCookies (String cookies) {
      
      this.cookies = cookies;
      conn.setRequestProperty ("Cookie", cookies);
      
      return this;
      
    }
    
    public HttpRequest isJSON (boolean yes) {
      
      if (yes) {
        
        setContentType ("application/json; charset=UTF-8");
        //setHeader ("Accept", "application/json");
        
      }
      
      return this;
      
    }
    
    public HttpRequest setOutput (boolean yes) {
      
      if (yes) conn.setDoOutput (yes);
      return this;
      
    }
    
    public HttpRequest setReferrer (String referrer) {
      return setHeader ("referer", referrer);
    }
    
    public static HttpRequest get (String url) throws HttpRequestException {
      return get (url, new HashMap<String, Object> ());
    }
    
    public static HttpRequest get (String url, String userAgent) throws HttpRequestException {
      return get (url).setUserAgent (userAgent);
    }
    
    public static HttpRequest get (String url, Map<String, Object> params) throws HttpRequestException {
      return new HttpRequest (METHOD_GET, url, params);
    }
    
    public static HttpRequest get (URL url, String userAgent) throws HttpRequestException {
      return new HttpRequest (METHOD_GET, url, userAgent);
    }
    
    public static HttpRequest post (String url) throws HttpRequestException {
      return post (url, new HashMap<String, Object> ());
    }
    
    public static HttpRequest post (String url, Map<String, Object> params) throws HttpRequestException {
      return new HttpRequest (METHOD_POST, url, params);
    }
    
    public HttpRequest setMethod (String method) throws HttpRequestException {
      
      try {
        conn.setRequestMethod (method);
      } catch (ProtocolException e) {
        throw new HttpRequestException (e);
      }
      
      return this;
      
    }
    
    public HttpRequest connect () throws HttpRequestException {
      
      //try {
      
      if (!isConnected) {
        
        //conn.connect ();
        isConnected = true;
        
      }
      
      //}
      
      return this;
      
    }
    
    public boolean isOK () throws HttpRequestException {
      return (getCode () == Net.HTTP_CODE_OK);
    }
    
    public String getMessage () throws HttpRequestException {
      return getMessage (getCode ());
    }
    
    public static String getMessage (int code) {
      
      String mess = Net.httpCodes ().get (code);
      return (mess != null ? mess : String.valueOf (code));
      
    }
    
    public URL getURL () throws HttpRequestException {
      
      connect ();
      return conn.getURL ();
      
    }
    
    public int getCode () throws HttpRequestException {
      
      if (code == -1) {
        
        connect ();
        
        try {
          code = conn.getResponseCode ();
        } catch (IOException e) {
          throw new HttpRequestException (e);
        }
        
      }
      
      return code;
      
    }
    
    public String getMessageCode () throws HttpRequestException {
      return getURL () + " " + getMessageCode (getCode ());
    }
    
    private String getMessageCode (int code) {
      return code + " " + getMessage (code);
    }
    
    public HttpRequest setHeaders (Map<String, Object> data) {
      
      for (String key : data.keySet ())
        setHeader (key, data.get (key));
      
      return this;
      
    }
    
    public HttpRequest setHeader (String key, Object value) {
      
      headers.put (key, value);
      conn.setRequestProperty (key, value.toString ());
      
      return this;
      
    }
    
    public String getHeader (String key) {
      return conn.getHeaderField (key);
    }
    
    public String getContent () throws HttpRequestException, OutOfMemoryException {
      return getContent ("");
    }
    
    private String content;
    
    public String getContent (String type) throws HttpRequestException, OutOfMemoryException {
      
      try {
        
        if (content == null) content = Strings.toString (getInputStream (type));
        return content;
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    public InputStream getInputStream () throws HttpRequestException, OutOfMemoryException {
      return getInputStream ("");
    }
    
    public InputStream getInputStream (String type) throws HttpRequestException, OutOfMemoryException {
      
      try {
        
        InputStream is;
        if (type.equals ("")) type = Files.getExtension (getURL ().toString ());
        
        switch (type) {
          
          default:
            is = conn.getInputStream ();
            break;
          
          case "gz": {
            
            setHeader ("Accept", "gzip, deflate");
            
            is = conn.getInputStream ();
            is = new GZIPInputStream (is);
            
            break;
            
          }
          
        }
        
        return is;
        
      } catch (IOException e) {
        return conn.getErrorStream ();
      } catch (OutOfMemoryError e) {
        throw new OutOfMemoryException (e);
      }
      
    }
    
    private String method;
    
    public String getMethod () {
      return method;
    }
    
    public String debug () throws HttpRequestException {
      
      String output = "curl -X " + getMethod () + " \"" + getURL () + "\"\n";
      
      for (String key : headers.keySet ())
        output += "    --header \"" + key + ": " + headers.get (key) + "\"\n";
      
      if (data != null)
        output += "    --data \"" + data.toString ().replace ("\"", "\\\"") + "\"\n";
      
      if (cookies != null)
        output += "    --cookie \"" + cookies.replace ("\"", "\\\"") + "\"";
      
      return output;
      
    }
    
    private BufferedOutputStream setOutputStream () throws HttpRequestException {
      return new BufferedOutputStream (getOutputStream (), Files.BUFFER_SIZE);
    }
    
    private OutputStream outputStream;
    
    public OutputStream getOutputStream () throws HttpRequestException {
      
      try {
        
        if (outputStream == null) outputStream = conn.getOutputStream ();
        return outputStream;
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    private Net.ProgressListener listener;
    
    public HttpRequest setListener (Net.ProgressListener listener) {
      
      this.listener = listener;
      return this;
      
    }
    
    public HttpRequest send (InputStream inputStream) throws HttpRequestException {
      
      conn.setDoOutput (true);
      
      try {
        
        OutputStream outputStream = setOutputStream ();
        
        Net.download (inputStream, outputStream, new Net.ProgressListener () {
          
          @Override
          public void onStart (long size) {
            if (listener != null) listener.onStart (size);
          }
          
          @Override
          public void onProgress (long length, long size) {
            if (listener != null) listener.onProgress (length, size);
          }
          
          @Override
          public void onError (int code, String result) {
            if (listener != null) listener.onError (code, result);
          }
          
          @Override
          public void onFinish (int code, String result) {
            if (listener != null) listener.onFinish (code, result);
          }
          
        });
        
        outputStream.flush ();
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
      return this;
      
    }
    
    public HttpRequest send (File file) throws HttpRequestException {
      
      try {
        
        setHeader ("Connection", "Keep-Alive");
        //setHeader ("Content-Type", this.getMimeType (imagePath));
        setHeader ("Content-Disposition", "attachment; filename=\"" + file + "\";");
        
        InputStream is = new FileInputStream (file);
        DataOutputStream dos = new DataOutputStream (getOutputStream ());
        
        int bytesAvailable = is.available ();
        int bufferSize = Math.min (bytesAvailable, Files.BUFFER_SIZE);
        
        byte[] buffer = new byte[bufferSize];
        
        long bytesRead = is.read (buffer, 0, bufferSize);
        
        while (bytesRead > 0) {
          
          dos.write (buffer, 0, bufferSize);
          bytesAvailable = is.available ();
          
          bufferSize = Math.min (bytesAvailable, Files.BUFFER_SIZE);
          bytesRead = is.read (buffer, 0, bufferSize);
          
        }
        
        dos.flush ();
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
      return this;
      
    }
    
    private Object data;
    
    public HttpRequest send (Object value) throws HttpRequestException, OutOfMemoryException {
      
      data = value;
      conn.setDoOutput (true);
      
      try {
        
        OutputStream os = getOutputStream ();
        os.write (Arrays.toByteArray (String.valueOf (value)));
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      } catch (OutOfMemoryError e) {
        throw new OutOfMemoryException (e);
      }
      
      return this;
      
    }
    
    public HttpRequest send (Map<String, Object> data) throws HttpRequestException {
      
      try {
        
        StringBuilder postData = new StringBuilder ();
        
        for (String key : data.keySet ()) {
          
          if (Int.size (postData) > 0) postData.append ('&');
          
          postData.append (URLEncoder.encode (key, Strings.DEF_CHARSET));
          postData.append ('=');
          postData.append (URLEncoder.encode (String.valueOf (data.get (key)), Strings.DEF_CHARSET));
          
        }
        
        byte[] dataBytes = Arrays.toByteArray (postData, Strings.DEF_CHARSET);
        
        setContentType ("application/x-www-form-urlencoded");
        setContentLength (Int.size (dataBytes));
        
        getOutputStream ().write (dataBytes);
        
        return this;
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    public HttpRequest setContentLength (long length) {
      
      setHeader ("Content-Length", length);
      return this;
      
    }
    
    public HttpRequest setContentType (String type) {
      return setHeader ("Content-Type", type);
    }
    
    public String getStatus () throws HttpRequestException {
      return getCode () + " " + getMessage ();
    }
    
    public long getLength () throws HttpRequestException {
      
      connect ();
      return conn.getContentLength ();
      
    }
    
    public HttpRequest disconnect () throws HttpRequestException {
      
      connect ();
      conn.disconnect ();
      
      return this;
      
    }
    
    public Map<String, String> getCookies () {
      
      Map<String, String> output = new LinkedHashMap<> ();
      
      List<String> cookiesHeader = conn.getHeaderFields ().get ("Cookies");
      
      if (cookiesHeader != null) {
        
        for (String cookieHeader : cookiesHeader)
          output.put ("111", cookieHeader);
        
      }
      
      return output;
      
    }
    
    public static Map<String, String> parseCookies (String cookies) {
      
      Map<String, String> output = new LinkedHashMap<> ();
      
      List<String> parts = Arrays.explode (";", cookies);
      
      for (String part : parts) {
        
        List<String> parts2 = Arrays.explode ("=", part);
        output.put (parts2.get (0).trim (), parts2.get (1).trim ());
        
      }
      
      return output;
      
    }
    
    /*@Override
    public String toString () {
      return getContent ();
    }*/
    
  }