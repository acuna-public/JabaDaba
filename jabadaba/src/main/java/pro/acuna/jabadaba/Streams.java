	package pro.acuna.jabadaba;
	/*
	 Created by Acuna on 21.07.2018
	*/
	
	import java.io.BufferedInputStream;
	import java.io.BufferedOutputStream;
	import java.io.ByteArrayInputStream;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.io.FileOutputStream;
	import java.io.InputStream;
	import java.io.OutputStream;
	
	public class Streams {
		
		public static InputStream toInputStream (File file) throws IOException {
			return toInputStream (file, Files.BUFFER_SIZE);
		}
		
		public static InputStream toInputStream (File file, int bufferSize) throws IOException {
			
			FileInputStream stream = new FileInputStream (file);
			return new BufferedInputStream (stream, bufferSize);
			
		}
		
		public static InputStream toInputStream (Object string) {
			return new ByteArrayInputStream (string.toString ().getBytes ());
		}
		
		public static InputStream toInputStream (Object string, String charset) throws IOException {
			return new ByteArrayInputStream (string.toString ().getBytes (charset));
		}
		
		public static OutputStream toOutputStream (File file) throws IOException {
			
			FileOutputStream stream = new FileOutputStream (file);
			return new BufferedOutputStream (stream);
			
		}
		
		public static void copy (InputStream in, OutputStream out) throws IOException {
			copy (in, out, Files.BUFFER_SIZE);
		}
		
		public static void copy (InputStream in, OutputStream out, int size) throws IOException {
			
			byte[] buffer = new byte[size];
			
			int length;
			while ((length = in.read (buffer, 0, size)) > 0)
			out.write (buffer, 0, length);
			
		}
		
	}