  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 17.07.2017
  */
  
  import org.json.JSONArray;
  import org.jsoup.Connection;
  import org.jsoup.Jsoup;
  import org.jsoup.nodes.Document;
  import org.jsoup.nodes.Element;
  import org.jsoup.parser.Parser;
  
  import java.io.File;
  import java.io.FileOutputStream;
  import java.io.IOException;
  import java.io.InputStream;
  import java.io.OutputStream;
  import java.io.UnsupportedEncodingException;
  import java.net.MalformedURLException;
  import java.net.URL;
  import java.net.URLDecoder;
  import java.net.URLEncoder;
  import java.text.ParseException;
  import java.util.ArrayList;
  import java.util.HashMap;
  import java.util.LinkedHashMap;
  import java.util.List;
  import java.util.Map;
  import java.util.Properties;
  
  import pro.acuna.jabadaba.exceptions.HttpRequestException;
  import pro.acuna.jabadaba.exceptions.OutOfMemoryException;
  
  public class Net {
    
    public static final int defTimeout = 30000;
    
    public static final int HTTP_CODE_OK = 200;
    
    public static final String URL_PROTOCOL = "protocol";
    public static final String URL_DOMAIN = "domain";
    public static final String URL_AUTHORITY = "authority";
    public static final String URL_PORT = "port";
    public static final String URL_PATH = "path";
    public static final String URL_CANONICAL = "canonical";
    public static final String URL_FILE = "file";
    public static final String URL_QUERY = "query";
    public static final String URL_ANCHOR = "ref";
    
    public static Map<String, String> parseUrl (String mUrl) throws MalformedURLException {
      
      Map<String, String> output = new HashMap<> ();
      
      URL url = new URL (mUrl); // http://example.com:80/docs/books/tutorial/index.html?name=networking#anchor
      
      output.put (URL_PROTOCOL, url.getProtocol ()); // http
      output.put (URL_DOMAIN, url.getHost ()); // example.com
      output.put (URL_AUTHORITY, url.getAuthority ()); // example.com:80
      output.put (URL_PORT, String.valueOf (url.getPort ())); // 80
      output.put (URL_PATH, url.getPath ()); // /docs/books/tutorial/index.html
      
      output.put (URL_CANONICAL, Files.getPath (output.get ("path"))); // /docs/books/tutorial
      
      String str = url.getFile ();
      List<String> file = Arrays.explode ("?", str);
      
      if (Int.size (file) > 0) {
        
        int pos = file.get (0).lastIndexOf ("/");
        str = file.get (0).substring ((pos + 1));
        
      }
      
      output.put (URL_FILE, str); // index.html
      output.put (URL_QUERY, url.getQuery ()); // name=networking
      output.put (URL_ANCHOR, url.getRef ()); // anchor
      
      return output;
      
    }
    
    public static String urlQueryEncode (Map<String, ?> query) throws UnsupportedEncodingException {
      return urlQueryEncode (query, true);
    }
    
    public static String urlQueryEncode (Map<String, ?> query, boolean question) throws UnsupportedEncodingException {
      
      List<String> data = new ArrayList<> ();
      
      for (String key : query.keySet ()) {
        
        Object object = query.get (key);
        
        if (object instanceof String[]) {
          
          String[] values = (String[]) object;
          
          for (String value : values)
            data.add (key + "[]=" + value);
          
        } else if (object instanceof JSONArray) {
          
          JSONArray values = (JSONArray) object;
          
          for (int i = 0; i < Int.size (values); ++i)
            data.add (key + "[]=" + values.getString (i));
          
        } else data.add (key + "=" + urlEncode (object.toString ()));
        
      }
      
      return ((question && Int.size (query) > 0) ? "?" : "") + Arrays.implode ("&", data);
      
    }
    
    public static Map<String, Object> urlQueryDecode (String url) {
      return queryDecode (url, "&", "=");
    }
    
    public static Map<String, Object> explodeCookies (String url) {
      return queryDecode (url, ";", "=");
    }
    
    public static Map<String, Object> queryDecode (String url, String sep1, String sep2) {
      
      Map<String, Object> output = new LinkedHashMap<> ();
      
      if (url != null) {
        
        List<String> items = Arrays.explode (sep1, url);
        
        for (String item : items) {
          
          if (!item.equals ("")) {
            
            List<String> values = Arrays.explode (sep2, item);
            output.put (Strings.trim (values.get (0)), (Int.size (values) > 1 ? values.get (1) : ""));
            
          }
          
        }
        
      }
      
      return output;
      
    }
    
    public static Map<String, String> decodeHTTPQuery (String query) throws UnsupportedEncodingException {
      
      Map<String, String> output = new HashMap<> ();
      
      query = query.replace ("&amp;", "&");
      List<String> parts1 = Arrays.explode ("&", query);
      
      for (String part : parts1) {
        
        List<String> parts2 = Arrays.explode ("=", part);
        output.put (parts2.get (0), URLDecoder.decode (Arrays.extend (parts2).toString (), Strings.DEF_CHARSET));
        
      }
      
      return output;
      
    }
    
    public static String getUserAgent () {
      return getUserAgent ("JabaDaba");
    }
    
    private static String getUserAgent (String name) {
      
      Map<String, String> locale = Locales.getLocaleData ();
      Properties prop = java.lang.System.getProperties ();
      
      return name + "/" + System.version + " (" + prop.getProperty ("os.name") + " " + prop.getProperty ("os.version") + " (" + prop.getProperty ("os.arch") + "); " + locale.get (Locales.COUNTRY) + "; " + locale.get ("lang") + ") " + prop.getProperty ("java.vm.vendor") + " " + prop.getProperty ("java.vm.name") + " " + prop.getProperty ("java.vm.version") + "/" + prop.getProperty ("java.class.version");
      
    }
    
    public static InputStream getStream (String url) throws HttpRequestException, OutOfMemoryException {
      return getStream (url, getUserAgent ());
    }
    
    public static InputStream getStream (String url, String userAgent) throws HttpRequestException, OutOfMemoryException {
      return getStream (url, userAgent, "");
    }
    
    public static InputStream getStream (String url, String userAgent, String type) throws HttpRequestException, OutOfMemoryException {
      return request (url, userAgent).getInputStream (type);
    }
    
    public static InputStream getStream (URL url, String userAgent, String type) throws HttpRequestException, OutOfMemoryException {
      return request (url, userAgent).getInputStream (type);
    }
    
    public static String getContent (String url) throws HttpRequestException, OutOfMemoryException {
      return getContent (url, getUserAgent ());
    }
    
    public static String getContent (String url, String userAgent) throws HttpRequestException, OutOfMemoryException {
      return getContent (url, userAgent, "");
    }
    
    public static String getContent (String url, String userAgent, String type) throws HttpRequestException, OutOfMemoryException {
      return request (url, userAgent).getContent (type);
    }
    
    public static List<String> getList (String url) throws HttpRequestException, OutOfMemoryException {
      return getList (url, getUserAgent ());
    }
    
    public static List<String> getList (String url, String userAgent) throws HttpRequestException, OutOfMemoryException {
      
      try {
        return Arrays.toStringList (getStream (url, userAgent), new ArrayList<String> ());
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    public static boolean isUrl (String content) {
      return isUrl (content, "http|https|ftp");
    }
    
    public static boolean isUrl (String content, String protos) {
      return content.matches ("^(" + protos + ")://([\\w.\\-]+)([^\\s/?.#]+\\.?)+(/[^\\s]*)?");
    }
    
    public static Document toHTML (File file) throws IOException, OutOfMemoryException {
      return toHTML (Files.read (file));
    }
    
    public static Document toHTML (Element elem) {
      return toHTML (elem.html ());
    }
    
    public static Document toHTML (String html) {
      return Jsoup.parse (html, "", Parser.xmlParser ());
    }
    
    public static Document toHTML (URL url) throws IOException {
      return toHTML (url, getUserAgent ());
    }
    
    public static Document toHTML (URL url, String userAgent) throws IOException {
      return toHTML (url, userAgent, "");
    }
    
    public static Document toHTML (URL url, String userAgent, String referrer) throws IOException {
      
      Connection connect = Jsoup.connect (url.toString ());
      
      if (!userAgent.equals ("")) connect.userAgent (userAgent);
      if (!referrer.equals ("")) connect.referrer (referrer);
      
      Document doc = connect.get ();
      
      doc.outputSettings (new Document.OutputSettings ().prettyPrint (false));
      
      return doc;
      
    }
    
    public interface ProgressListener {
      
      void onStart (long size);
      void onProgress (long length, long size);
      void onError (int code, String result);
      void onFinish (int code, String result);
      
    }
    
    public static void download (String url, String fileName) throws HttpRequestException, OutOfMemoryException {
      download (url, fileName, getUserAgent ());
    }
    
    public static void download (String url, String fileName, String userAgent) throws HttpRequestException, OutOfMemoryException {
      download (url, new File (fileName), userAgent);
    }
    
    public static void download (URL url, String fileName, String userAgent) throws HttpRequestException, OutOfMemoryException {
      download (url, new File (fileName), userAgent);
    }
    
    public static void download (URL url, File file, String userAgent) throws HttpRequestException, OutOfMemoryException {
      download (url.toString (), file, userAgent);
    }
    
    public static void download (String url, File fileName, String userAgent) throws HttpRequestException, OutOfMemoryException {
      download (url, fileName, userAgent, null);
    }
    
    public static void download (String url, File fileName, String userAgent, ProgressListener listener) throws HttpRequestException, OutOfMemoryException {
      download (url, fileName, userAgent, listener, -1);
    }
    
    public static void download (String url, File fileName, String userAgent, ProgressListener listener, long length) throws HttpRequestException, OutOfMemoryException {
      download (url, fileName, userAgent, listener, length, defTimeout);
    }
    
    public static void download (InputStream inputStream, OutputStream outputStream, ProgressListener listener) throws IOException {
      download (inputStream, outputStream, listener, -1);
    }
    
    public static void download (InputStream inputStream, OutputStream outputStream, ProgressListener listener, long length) throws IOException {
      
      if (length < 0) length = inputStream.available ();
      if (listener != null) listener.onStart (length);
      
      byte[] buffer = new byte[Files.BUFFER_SIZE];
      
      long total = 0, percentDone = -1;
      int bytesRead;
      
      while ((bytesRead = inputStream.read (buffer)) > 0) {
        
        total += bytesRead;
        
        if (listener != null && percentDone != total) {
          
          percentDone = total;
          listener.onProgress (percentDone, length);
          
        }
        
        outputStream.write (buffer, 0, bytesRead);
        
      }
      
      if (listener != null) {
        
        if (total > 0)
          listener.onFinish (0, "");
        else
          listener.onError (0, "");
        
      }
      
    }
    
    public static void download (String url, File fileName, String userAgent, ProgressListener listener, long length, int timeout) throws HttpRequestException, OutOfMemoryException {
      
      try {
        
        HttpRequest request = request (url, userAgent, timeout);
        int responseCode = request.getCode ();
        String result = request.getMessage ();
        
        if (length < 0) length = request.getLength ();
        if (listener != null) listener.onStart (length);
        
        if (request.isOK ()) {
          
          Files.makeDir (Files.getPath (fileName));
          
          InputStream inputStream = request.getInputStream ();
          OutputStream outputStream = new FileOutputStream (fileName);
          
          byte[] buffer = new byte[Files.BUFFER_SIZE];
          
          long total = 0, percentDone = -1;
          int bytesRead;
          
          while ((bytesRead = inputStream.read (buffer)) != -1) {
            
            total += bytesRead;
            
            if (listener != null && percentDone != total) {
              
              percentDone = total;
              listener.onProgress (percentDone, length);
              
            }
            
            outputStream.write (buffer, 0, bytesRead);
            
          }
          
          if (listener != null) {
            
            if (total > 0)
              listener.onFinish (responseCode, result);
            else
              listener.onError (responseCode, result);
            
          }
          
          //inputStream.close ();
          //outputStream.close ();
          
        } else throw new IOException (url + ": " + result);
        
        request.disconnect ();
        
      } catch (IOException e) {
        throw new HttpRequestException (e);
      }
      
    }
    
    public static String urlEncode (String url) throws UnsupportedEncodingException {
      return URLEncoder.encode (url, Strings.DEF_CHARSET);
    }
    
    public static HttpRequest request (URL url, String userAgent) throws HttpRequestException {
      return request (url, userAgent, defTimeout);
    }
    
    public static HttpRequest request (URL url, String userAgent, int timeout) throws HttpRequestException {
      return request (url.toString (), userAgent, timeout);
    }
    
    public static HttpRequest request (String url, String userAgent) throws HttpRequestException {
      return request (url, userAgent, defTimeout);
    }
    
    public static HttpRequest request (String url, String userAgent, int timeout) throws HttpRequestException {
      return new HttpRequest (HttpRequest.METHOD_GET, url, userAgent, timeout);
    }
    
    private static String[] randomUserAgentBrowserOS () {
      
      List<Integer[]> keys2 = new ArrayList<> ();
      
      List<List<String[]>> values1 = new ArrayList<> ();
      
      List<String[]> values2 = new ArrayList<> ();
      
      values2.add (new String[] {"chrome", "win"});
      values2.add (new String[] {"chrome", "mac"});
      values2.add (new String[] {"chrome", "linux"});
      
      keys2.add (new Integer[] {89, 9, 2});
      values1.add (values2);
      
      values2 = new ArrayList<> ();
      
      values2.add (new String[] {"iexplorer", "win"});
      
      keys2.add (new Integer[] {100});
      values1.add (values2);
      
      values2 = new ArrayList<> ();
      
      values2.add (new String[] {"firefox", "win"});
      values2.add (new String[] {"firefox", "mac"});
      values2.add (new String[] {"firefox", "linux"});
      
      keys2.add (new Integer[] {83, 16, 1});
      values1.add (values2);
      
      values2 = new ArrayList<> ();
      
      values2.add (new String[] {"safari", "win"});
      values2.add (new String[] {"safari", "mac"});
      values2.add (new String[] {"safari", "linux"});
      
      keys2.add (new Integer[] {95, 4, 1});
      values1.add (values2);
      
      values2 = new ArrayList<> ();
      
      values2.add (new String[] {"opera", "win"});
      values2.add (new String[] {"opera", "mac"});
      values2.add (new String[] {"opera", "linux"});
      
      keys2.add (new Integer[] {91, 6, 3});
      values1.add (values2);
      
      int sum = 0;
      int rand = Int.rand (1, 100);
      
      Integer[] keys1 = {34, 32, 25, 7, 2};
      
      for (int i = 0; i < Int.size (keys1); ++i) {
        
        sum += keys1[i];
        
        if (rand <= sum) {
          
          sum = 0;
          rand = Int.rand (1, 100);
          
          Integer[] keys = keys2.get (i);
          
          for (int i2 = 0; i2 < Int.size (keys); ++i2) {
            
            sum += keys[i2];
            if (rand <= sum) return values1.get (i).get (i2);
            
          }
          
        }
        
      }
      
      return values1.get (0).get (0);
      
    }
    
    public static final String TYPE_DESKTOP = "desktop";
    public static final String TYPE_MOBILE = "mobile";
    
    public static String getRandomUserAgent () {
      return getRandomUserAgent (TYPE_DESKTOP);
    }
    
    public static String getRandomUserAgent (String type) {
      return getRandomUserAgent (type, new String[] {"en-US"});
    }
    
    public static String getRandomUserAgent (String type, String[] lang) {
      
      String agent = "";
      
      switch (type) {
        
        case TYPE_DESKTOP: {
          
          String nt_version = Int.rand (5, 6) + "." + Int.rand (0, 1);
          String osx_version = "10_" + Int.rand (5, 7) + "_" + Int.rand (0, 9);
          
          String[] getBrowserOS = randomUserAgentBrowserOS ();
          
          String data = "", ver = "", version = "", version2 = "", extra;
          String browser = getBrowserOS[0];
          String os = getBrowserOS[1];
          
          Map<String, String[]> proc = new HashMap<> ();
          
          proc.put ("linux", new String[] {"i686", "x86_64"});
          proc.put ("mac", new String[] {"Intel", "PPC", "U; Intel", "U; PPC"});
          proc.put ("win", new String[] {"foo"});
          
          switch (browser) {
            
            default: {
              
              extra = Arrays.rand (new String[] {
                
                "",
                "; .NET CLR 1.1." + Int.rand (4320, 4325),
                "; WOW64",
                
                });
              
              version = Int.rand (7, 9) + ".0";
              version2 = Int.rand (3, 5) + "." + Int.rand (0, 1);
              
              data = "(compatible; MSIE " + version + "; Windows NT " + nt_version + "; Trident/" + version2 + ")";
              
              agent = "Mozilla/5.0 " + data;
              
              break;
              
            }
            
            case "firefox": {
              
              try {
                
                ver = Arrays.rand (new String[] {
                  
                  "Gecko/" + Locales.date ("yyyyMMdd", Int.rand (Locales.toTime ("2011-01-01", "yyyy-MM-dd"), Locales.time (), true)) + " Firefox/" + Int.rand (5, 7) + ".0",
                  "Gecko/" + Locales.date ("yyyyMMdd", Int.rand (Locales.toTime ("2011-01-01", "yyyy-MM-dd"), Locales.time (), true)) + " Firefox/" + Int.rand (5, 7) + ".0.1",
                  "Gecko/" + Locales.date ("yyyyMMdd", Int.rand (Locales.toTime ("2010-01-01", "yyyy-MM-dd"), Locales.time (), true)) + " Firefox/3.6." + Int.rand (1, 20),
                  "Gecko/" + Locales.date ("yyyyMMdd", Int.rand (Locales.toTime ("2010-01-01", "yyyy-MM-dd"), Locales.time (), true)) + " Firefox/3.8",
                  
                  });
                
              } catch (ParseException e) {
                // empty
              }
              
              switch (os) {
                
                default:
                  data = "(Windows NT " + nt_version + "; " + Arrays.rand (lang) + "; rv:1.9." + Int.rand (0, 2) + ".20) " + ver;
                  break;
                
                case "linux":
                  data = "(X11; Linux " + Arrays.rand (proc.get (os)) + "; rv:" + Int.rand (5, 7) + ".0) " + ver;
                  break;
                
                case "mac":
                  data = "(Macintosh; " + Arrays.rand (proc.get (os)) + " Mac OS X " + osx_version + " rv:" + Int.rand (2, 6) + ".0) " + ver;
                  break;
                
              }
              
              agent = "Mozilla/5.0 " + data;
              
              break;
              
            }
            
            case "safari": {
              
              version = Int.rand (531, 535) + "." + Int.rand (1, 50) + "." + Int.rand (1, 7);
              
              if (Int.rand (0, 1) == 0)
                ver = Int.rand (4, 5) + "." + Int.rand (0, 1);
              else
                ver = Int.rand (4, 5) + ".0." + Int.rand (1, 5);
              
              switch (os) {
                
                default:
                  data = "(Windows; U; Windows NT " + nt_version + ") AppleWebKit/" + version + " (KHTML, like Gecko) Version/" + ver + " Safari/" + version;
                  break;
                
                case "mac":
                  data = "(Macintosh; U; " + Arrays.rand (proc.get (os)) + " Mac OS X " + osx_version + " rv:" + Int.rand (2, 6) + ".0; " + Arrays.rand (lang) + ") AppleWebKit/" + version + " (KHTML, like Gecko) Version/" + ver + " Safari/" + version;
                  break;
                
                case "iphone":
                  data = "(iPod; U; CPU iPhone OS " + Int.rand (3, 4) + "_" + Int.rand (0, 3) + " like Mac OS X; " + Arrays.rand (lang) + ") AppleWebKit/" + version + " (KHTML, like Gecko) Version/" + Int.rand (3, 4) + ".0.5 Mobile/8B" + Int.rand (111, 119) + " Safari/6" + version;
                  break;
                
              }
              
              agent = "Mozilla/5.0 " + data;
              
              break;
              
            }
            
            case "opera": {
              
              extra = Arrays.rand (new String[] {
                
                "",
                "; .NET CLR 1.1." + Int.rand (4320, 4325) + ".0",
                "; WOW64",
                
                });
              
              version = "2.9." + Int.rand (160, 190);
              version2 = Int.rand (10, 12) + ".00";
              
              switch (os) {
                
                default:
                  data = "(Windows NT " + nt_version + "; U; " + Arrays.rand (lang) + ") Presto/" + version + " Version/" + version2;
                  break;
                
                case "linux":
                  data = "(X11; Linux " + Arrays.rand (proc.get (os)) + "; U; " + Arrays.rand (lang) + ") Presto/" + version + " Version/" + version2;
                  break;
                
              }
              
              agent = "Opera/" + Int.rand (8, 9) + "." + Int.rand (10, 99) + " " + data;
              
              break;
              
            }
            
            case "chrome": {
              
              version = Int.rand (531, 536) + Int.rand (0, 2) + "";
              version2 = Int.rand (13, 15) + ".0." + Int.rand (800, 899) + ".0";
              
              switch (os) {
                
                default:
                  data = "(Windows NT " + nt_version + ") AppleWebKit/" + version + " (KHTML, like Gecko) Chrome/" + version2 + " Safari/" + version;
                  break;
                
                case "linux":
                  data = "(X11; Linux " + Arrays.rand (proc.get (os)) + ") AppleWebKit/" + version + " (KHTML, like Gecko) Chrome/" + version2 + " Safari/" + version;
                  break;
                
                case "mac":
                  data = "(Macintosh; U; " + Arrays.rand (proc.get (os)) + " Mac OS X " + osx_version + ") AppleWebKit/" + version + " (KHTML, like Gecko) Chrome/" + version2 + " Safari/" + version;
                  break;
                
              }
              
              agent = "Mozilla/5.0 " + data;
              
              break;
              
            }
            
          }
          
        }
        
        case TYPE_MOBILE: {
          
          String[] agents = {
            
            "Mozilla/5.0 (Linux; U; Android 4.4; en-us) AppleWebKit/999+ (KHTML, like Gecko) Safari/999.9",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-cn; HTC_IncredibleS_S710e Build/GRJ90) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; en-us; HTC Vision Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 7.0; fr-fr; HTC Desire Build/GRJ22) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-tw; HTC_Pyramid Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-tw; HTC_Pyramid Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari",
            "Mozilla/5.0 (Linux; U; Android 6.0; zh-tw; HTC Pyramid Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; ko-kr; LG-LU3000 Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; en-us; HTC_DesireS_S510e Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; en-us; HTC_DesireS_S510e Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile",
            "Mozilla/5.0 (Linux; U; Android 6.0; de-de; HTC Desire Build/GRI40) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 6.0; de-ch; HTC Desire Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 7.1; fr-lu; HTC Legend Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 7.1; en-sa; HTC_DesireHD_A9191 Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 5.1; fr-fr; HTC_DesireZ_A7272 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 5.1; en-gb; HTC_DesireZ_A7272 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            "Mozilla/5.0 (Linux; U; Android 5.1; en-ca; LG-P505R Build/FRG83) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1",
            
            };
          
          agent = Arrays.rand (agents);
          
        }
        
      }
      
      return agent;
      
    }
    
    public static boolean isUrlEmpty (String str) {
      return Strings.hasEnd ("://", str);
    }
    
    public static Map<Integer, String> httpCodes () {
      
      Map<Integer, String> codes = new HashMap<> ();
      
      codes.put (100, "Continue");
      codes.put (101, "Switching Protocols");
      codes.put (102, "Processing");
      codes.put (105, "Name Not Resolved");
      
      codes.put (HTTP_CODE_OK, "OK");
      codes.put (201, "Created");
      codes.put (202, "Accepted");
      codes.put (203, "Non-Authoritative Information");
      codes.put (204, "No Content");
      codes.put (205, "Reset Content");
      codes.put (206, "Partial Content");
      codes.put (207, "Multi-Status");
      codes.put (226, "IM Used");
      
      codes.put (300, "Multiple Choices");
      codes.put (301, "Moved Permanently");
      codes.put (302, "Moved Temporarily");
      codes.put (302, "Found");
      codes.put (303, "See Other");
      codes.put (304, "Not Modified");
      codes.put (305, "Use Proxy");
      codes.put (307, "Temporary Redirect");
      
      codes.put (400, "Bad Request");
      codes.put (401, "Unauthorized");
      codes.put (402, "Payment Required");
      codes.put (403, "Forbidden");
      codes.put (404, "Not Found");
      codes.put (405, "Method Not Allowed");
      codes.put (406, "Not Acceptable");
      codes.put (407, "Proxy Authentication Required");
      codes.put (408, "Request Timeout");
      codes.put (409, "Conflict");
      codes.put (410, "Gone");
      codes.put (411, "Length Required");
      codes.put (412, "Precondition Failed");
      codes.put (413, "Request Entity Too Large");
      codes.put (414, "Request-URI Too Large");
      codes.put (415, "Unsupported Media Type");
      codes.put (416, "Requested Range Not Satisfiable");
      codes.put (417, "Expectation Failed");
      codes.put (422, "Unprocessable Entity");
      codes.put (423, "Locked");
      codes.put (424, "Failed Dependency");
      codes.put (425, "Unordered Collection");
      codes.put (426, "Upgrade Required");
      codes.put (428, "Precondition Required");
      codes.put (429, "Too Many Requests");
      codes.put (431, "Request Header Fields Too Large");
      codes.put (434, "Requested host unavailable");
      codes.put (449, "Retry With");
      codes.put (451, "Unavailable For Legal Reasons");
      codes.put (456, "Unrecoverable Error");
      
      codes.put (500, "Internal Server Error");
      codes.put (501, "Not Implemented");
      codes.put (502, "Bad Gateway");
      codes.put (503, "Service Unavailable");
      codes.put (504, "Gateway Timeout");
      codes.put (505, "HTTP Version Not Supported");
      codes.put (506, "Variant Also Negotiates");
      codes.put (507, "Insufficient Storage");
      codes.put (508, "Loop Detected");
      codes.put (509, "Bandwidth Limit Exceeded");
      codes.put (510, "Not Extended");
      codes.put (511, "Network Authentication Required");
      
      return codes;
      
    }
    
    public static String prepUrl (String url) {
      return Strings.addStart ("https:", url);
    }
    
    public static boolean isOK (int code) {
      return (code == HTTP_CODE_OK);
    }
    
  }