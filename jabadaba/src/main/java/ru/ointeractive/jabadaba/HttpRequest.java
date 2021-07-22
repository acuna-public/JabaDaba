	package ru.ointeractive.jabadaba;
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
	import java.net.Authenticator;
	import java.net.HttpURLConnection;
	import java.net.InetSocketAddress;
	import java.net.PasswordAuthentication;
	import java.net.ProtocolException;
	import java.net.Proxy;
	import java.net.URL;
	import java.net.URLEncoder;
	import java.util.LinkedHashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.zip.GZIPInputStream;
	
	import ru.ointeractive.jabadaba.exceptions.HttpRequestException;
	import ru.ointeractive.jabadaba.exceptions.OutOfMemoryException;
	
	public class HttpRequest {
		
		private HttpURLConnection conn;
		private int code = -1;
		private boolean isConnected = false;
		
		private final Map<String, Object> headers = new LinkedHashMap<> ();
		
		public static final String METHOD_GET = "GET";
		public static final String METHOD_POST = "POST";
		public static final String METHOD_HEAD = "HEAD";
		public static final String METHOD_OPTIONS = "OPTIONS";
		public static final String METHOD_PUT = "PUT";
		public static final String METHOD_DELETE = "DELETE";
		public static final String METHOD_TRACE = "TRACE";
		public static final String METHOD_PATCH = "PATCH";
		
		public static class Options {
		
		}
		
		public HttpRequest () {
		}
		
		public HttpRequest (String method, String url, String useragent) throws HttpRequestException {
			this (method, url, useragent, defTimeout);
		}
		
		public HttpRequest (String method, String url, Map<String, Object> params) throws HttpRequestException {
			
			this (method, url, Net.getUserAgent ());
			setParams (params);
			
		}
		
		private String method, userAgent = Net.getUserAgent ();
		public static final int defTimeout = 30000;
		private int timeout;
		public URL url;
		public String mUrl;
		protected Map<String, Object> params = new LinkedHashMap<> ();
		
		private HttpRequest connect () throws HttpRequestException {
			
			try {
				
				url = new URL (mUrl + (params != null ? Net.urlQueryEncode (params) : ""));
				
				if (!isConnected) {
					
					if (proxy == null)
						conn = (HttpURLConnection) url.openConnection ();
					else
						conn = (HttpURLConnection) url.openConnection (proxy);
					
					for (String key : headers.keySet ())
						conn.setRequestProperty (key, headers.get (key).toString ());
					
					isConnected = true;
					
					setUserAgent (userAgent);
					
					conn.setConnectTimeout (timeout);
					conn.setReadTimeout (timeout);
					conn.setInstanceFollowRedirects (true);
					
					String[] allowedMethods = new String[] {METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE, METHOD_HEAD, METHOD_OPTIONS, METHOD_TRACE};
					
					if (!Arrays.contains (method, allowedMethods)) {
						
						setMethod (METHOD_POST);
						setHeader ("X-HTTP-Method-Override", method);
						
					} else setMethod (method);
					
				}
				
				return this;
				
			} catch (IOException e) {
				throw new HttpRequestException (e);
			}
			
		}
		
		public HttpRequest setParams (Map<String, Object> params) {
			
			this.params = params;
			return this;
			
		}
		
		public HttpRequest (String method, String url) throws HttpRequestException {
			this (method, url, Net.getUserAgent (), defTimeout);
		}
		
		public HttpRequest (String method, String url, String userAgent, int timeout) throws HttpRequestException {
			
			super ();
			
			this.method = method;
			this.userAgent = userAgent;
			this.timeout = timeout;
			this.mUrl = url;
			
		}
		
		public HttpRequest setParam (String key, Object value) {
			
			params.put (key, value);
			return this;
			
		}
		
		public HttpRequest setUserAgent (String userAgent) throws HttpRequestException {
			
			this.userAgent = userAgent;
			if (userAgent == null) userAgent = Net.getUserAgent ();
			
			return setHeader ("User-Agent", userAgent);
			
		}
		
		public String getUserAgent () {
			return userAgent;
		}
		
		private String cookies;
		
		public HttpRequest setCookies (String cookies) throws HttpRequestException {
			
			this.cookies = cookies;
			
			connect ();
			conn.setRequestProperty ("Cookie", cookies);
			
			return this;
			
		}
		
		public HttpRequest isJSON (boolean yes) throws HttpRequestException {
			
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
		
		public HttpRequest setReferrer (URL referrer) throws HttpRequestException {
			return setReferrer (referrer.toString ());
		}
		
		public HttpRequest setReferrer (String referrer) throws HttpRequestException {
			return setHeader ("referer", referrer);
		}
		
		public static HttpRequest get (String url, Map<String, Object> params) throws HttpRequestException {
			return get (url).setUserAgent (Net.getUserAgent ()).setParams (params);
		}
		
		public static HttpRequest post (String url, Map<String, Object> params) throws HttpRequestException {
			return post (url).setUserAgent (Net.getUserAgent ()).setParams (params);
		}
		
		public static HttpRequest get (String url, String userAgent) throws HttpRequestException {
			return get (url).setUserAgent (userAgent);
		}
		
		public static HttpRequest get (String url) throws HttpRequestException {
			return new HttpRequest (url, url).connect ();
		}
		
		public static HttpRequest get (URL url, String userAgent) throws HttpRequestException {
			return new HttpRequest (METHOD_GET, url.toString (), userAgent, defTimeout).connect ();
		}
		
		public static HttpRequest post (String url) throws HttpRequestException {
			return new HttpRequest (METHOD_POST, url);
		}
		
		public static HttpRequest head (String url) throws HttpRequestException {
			return head (url, Net.getUserAgent ());
		}
		
		public static HttpRequest head (String url, String userAgent) throws HttpRequestException {
			return new HttpRequest (METHOD_HEAD, url).setUserAgent (userAgent);
		}
		
		public HttpRequest setMethod (String method) throws HttpRequestException {
			
			try {
				conn.setRequestMethod (method);
			} catch (ProtocolException e) {
				throw new HttpRequestException (e);
			}
			
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
		
		public HttpRequest setHeaders (Map<String, Object> data) throws HttpRequestException {
			
			for (String key : data.keySet ())
				setHeader (key, data.get (key));
			
			return this;
			
		}
		
		public HttpRequest setHeader (String key, Object value) throws HttpRequestException {
			
			headers.put (key, value);
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
				
				if (content == null)
					content = Strings.toString (getInputStream (type));
				
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
				
				connect ();
				
				InputStream is;
				
				if (type.equals (""))
					type = Files.getExtension (url.toString ());
				
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
		
		public String getMethod () {
			return method;
		}
		
		public String debug () throws HttpRequestException {
			
			StringBuilder output = new StringBuilder ("curl -X " + getMethod () + " \"" + getURL () + "\"\n");
			
			for (String key : headers.keySet ())
				output.append ("    --header \"" + key + ": " + headers.get (key) + "\"\n");
			
			if (data != null)
				output.append ("    --data \"" + data.toString ().replace ("\"", "\\\"") + "\"\n");
			
			if (cookies != null)
				output.append ("    --cookie \"" + cookies.replace ("\"", "\\\"") + "\"");
			
			return output.toString ();
			
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
			
			connect ();
			
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
			
			connect ();
			
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
			
			connect ();
			
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
			
			connect ();
			
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
		
		public HttpRequest setContentLength (long length) throws HttpRequestException {
			
			setHeader ("Content-Length", length);
			return this;
			
		}
		
		public HttpRequest setContentType (String type) throws HttpRequestException {
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
		
		public static abstract class Auth {
			
			public abstract String toString ();
			
		}
		
		public static class Bearer extends Auth {
			
			private String token;
			
			public Bearer setToken (String token) {
				
				this.token = token;
				return this;
				
			}
			
			@Override
			public String toString () {
				return "Bearer " + token;
			}
			
		}
		
		public HttpRequest setAuth (Auth auth) throws HttpRequestException {
			
			setHeader ("Authorization", auth.toString ());
			return this;
			
		}
		
		private Proxy proxy;
		
		public HttpRequest setProxy (String[] proxies) {
			
			String proxy = proxies[Int.rand (0, Int.size (proxies) - 1)];
			
			final String[] parts = proxy.split ("@");
			String[] addr;
			
			if (Int.size (parts) > 1) { // C паролем
				
				addr = parts[1].split (":");
				
				Authenticator.setDefault (new Authenticator () {
					
					@Override
					public PasswordAuthentication getPasswordAuthentication () {
						
						String[] cred = parts[0].split (":");
						return new PasswordAuthentication (cred[0], cred[1].toCharArray ());
						
					}
					
				});
				
			} else addr = parts[0].split (":");
			
			this.proxy = new Proxy (Proxy.Type.HTTP, new InetSocketAddress (addr[0], Integer.valueOf (addr[1])));
			
			return this;
			
		}
		
	}