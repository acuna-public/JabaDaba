	package pro.acuna.jabadaba;
  /*
   Created by Acuna on 17.07.2017
  */
	
	import java.io.BufferedInputStream;
	import java.io.BufferedReader;
	import java.io.ByteArrayOutputStream;
	import java.io.File;
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.Formatter;
	import java.util.HashMap;
	import java.util.Map;
	import java.util.regex.MatchResult;
	import java.util.regex.Matcher;
	import java.util.regex.Pattern;
	
	import pro.acuna.jabadaba.exceptions.OutOfMemoryException;
	
	public class Strings {
		
		public static final String SUMB_DIGITS = "0123456789";
		public static final String SUMB_SPECIAL = "!?@#~$%^&*№+=;:«»[]";
		public static final String SUMB_SPECIAL_2 = ",\"'/()—";
		public static final String SUMB_LETTERS_LOW = "abcdefghijklmnopqrstuvwxyz";
		public static final String SUMB_LETTERS_UP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		public static final String DEF_CHARSET = "UTF-8";
		public static final String LS = java.lang.System.getProperty ("line.separator");
		
		public static String toString (InputStream stream) throws IOException, OutOfMemoryException {
			return toString (stream, DEF_CHARSET);
		}
		
		public static String toString (InputStream stream, String charset) throws IOException, OutOfMemoryException {
			return toString (stream, charset, Files.BUFFER_SIZE);
		}
		
		public static String toString (InputStream stream, String charset, int buffer) throws IOException, OutOfMemoryException {
			return toStringBuilder (stream, charset, buffer).toString ();
		}
		
		public static StringBuilder toStringBuilder (InputStream stream, String charset, int buffer) throws IOException, OutOfMemoryException {
			
			try {
				
				stream = new BufferedInputStream (stream);
				
				StringBuilder output = new StringBuilder ();
				BufferedReader br = Buffer.toBufferedReader (stream, charset, buffer);
				
				String str;
				
				while ((str = br.readLine ()) != null) {
					
					output.append (str);
					output.append (LS);
					
				}
				
				return output;
				
			} catch (OutOfMemoryError e) {
				throw new OutOfMemoryException (e);
			}
			
		}
		
		public static String toString (ByteArrayOutputStream stream) throws IOException {
			return toString (stream, DEF_CHARSET);
		}
		
		public static String toString (ByteArrayOutputStream stream, String charset) throws IOException {
			return stream.toString (charset);
		}
		
		public static boolean pregMatch (String pattern, String content) {
			return content.matches (pattern);
		}
		
		public static String spaces = ""
																	+ "\\u0009" // CHARACTER TABULATION
																	+ "\\u000A" // LINE FEED (LF)
																	+ "\\u000B" // LINE TABULATION
																	+ "\\u000C" // FORM FEED (FF)
																	+ "\\u000D" // CARRIAGE RETURN (CR)
																	+ "\\u0020" // SPACE
																	+ "\\u0085" // NEXT LINE (NEL)
																	+ "\\u00A0" // NO-BREAK SPACE
																	+ "\\u1680" // OGHAM SPACE MARK
																	+ "\\u180E" // MONGOLIAN VOWEL SEPARATOR
																	+ "\\u2000" // EN QUAD
																	+ "\\u2001" // EM QUAD
																	+ "\\u2002" // EN SPACE
																	+ "\\u2003" // EM SPACE
																	+ "\\u2004" // THREE-PER-EM SPACE
																	+ "\\u2005" // FOUR-PER-EM SPACE
																	+ "\\u2006" // SIX-PER-EM SPACE
																	+ "\\u2007" // FIGURE SPACE
																	+ "\\u2008" // PUNCTUATION SPACE
																	+ "\\u2009" // THIN SPACE
																	+ "\\u200A" // HAIR SPACE
																	+ "\\u2028" // LINE SEPARATOR
																	+ "\\u2029" // PARAGRAPH SEPARATOR
																	+ "\\u202F" // NARROW NO-BREAK SPACE
																	+ "\\u205F" // MEDIUM MATHEMATICAL SPACE
																	+ "\\u3000" // IDEOGRAPHIC SPACE
		;
		
		public static String trim (String str) {
			return str.replaceAll ("^[" + spaces + "]", "");
		}
		
		public static String trimStart (String str) {
			return trimStart (" ", str);
		}
		
		public static String trimEnd (String str) {
			return trimEnd (" ", str);
		}
		
		public static String trimStart (String symb, File file) {
			return trimStart (symb, file.getAbsolutePath ());
		}
		
		public static String trimStart (String symb, String str) {
			
			if (str != null && Int.size (str) > 0) {
				
				int symbLength = Int.size (symb);
				
				if (symbLength > 0) {
					
					if (Int.size (str) > symbLength && hasStart (symb, str)) // Символ вначале строки
						str = str.substring (symbLength, Int.size (str));
					else if (Int.size (str) < symbLength)
						str = trimStart (str, symb);
					else if (str.equals (symb) && Int.size (str) == symbLength)
						str = "";
					
				}
				
			}
			
			return str;
			
		}
		
		public static String trimEnd (String symb, File file) {
			return trimEnd (symb, file.getAbsolutePath ());
		}
		
		public static String trimEnd (String symb, String str) {
			
			if (str != null) {
				
				int length = (Int.size (str) - Int.size (symb));
				
				if (Int.size (str) > 0 && hasEnd (symb, str))
					str = getStart (length, str);
				
			}
			
			return str;
			
		}
		
		public static String trim (String symb, File file) {
			return trim (symb, file.getAbsolutePath ());
		}
		
		public static String trim (String symb, String str) {
			
			str = trimEnd (symb, str);
			str = trimStart (symb, str);
			
			return str;
			
		}
		
		public static String getStart (int num, String str) {
			return str.substring (0, num);
		}
		
		public static boolean hasStart (String start, String str) {
			return (getStart (Int.size (start), str).equals (start));
		}
		
		public static String getEnd (int num, String str) {
			
			int length = Int.size (str);
			return str.substring (length - num, length);
			
		}
		
		public static boolean hasEnd (String end, String str) {
			return (getEnd (Int.size (end), str).equals (end));
		}
		
		public static String addStart (String symb, String str) {
			
			StringBuilder output = new StringBuilder ();
			
			if (Int.size (str) > 0) {
				
				if (!hasStart (symb, str)) {
					
					output.append (symb);
					output.append (str);
					
				}
				
			} else output.append (symb);
			
			return output.toString ();
			
		}
		
		public static String addEnd (String symb, String str) {
			
			StringBuilder output = new StringBuilder ();
			
			if (Int.size (str) > 0) {
				
				if (!hasEnd (symb, str)) {
					
					output.append (str);
					output.append (symb);
					
				}
				
			} else output.append (symb);
			
			return output.toString ();
			
		}
		
		public static String add (String symb, String str) {
			
			str = addStart (symb, str);
			str = addEnd (symb, str);
			
			return str;
			
		}
		
		public static String suffix (long num, String[] lang) {
			return suffix ((int) num, lang);
		}
		
		public static String suffix (int num, String[] lang) {
			
			String str = String.valueOf (num);
			
			int num1 = (num <= 9 ? num : Integer.parseInt (str.substring (1)));
			int num2 = (num <= 99 ? num : Integer.parseInt (str.substring (2)));
			
			return suffix (num1, num2, lang);
			
		}
		
		private static String suffix (int num1, int num2, String[] lang) {
			
			String word = "";
			
			if (Int.size (lang) == 3) {
				
				if (num2 >= 10 && num2 <= 20)
					word = lang[2]; // Малолетки и просто ппц какие старые (10-20 и 110-120) - лет
				else if (num1 == 1) word = lang[0]; // год
				else if (num1 >= 2 && num1 <= 4) word = lang[1]; // года
				else word = lang[2]; // Все остальные возрасты - лет
				
			} else if (Int.size (lang) == 2) {
				
				if (num1 == 1) word = lang[0];
				else word = lang[1];
				
			}
			
			return word;
			
		}
		
		public static String prepBr (String str) {
			return prepBr (str, "<br/>").replaceAll ("\\s*\\n", "");
		}
		
		public static String prepBr (String str, String replace) {
			
			for (String find : Arrays.brArray)
				str = str.replace (find, replace);
			
			return str;
			
		}
		
		public static String br2nl (String str) {
			return prepBr (str, "\n");
		}
		
		public static String repeat (String str, int num) {
			
			StringBuilder output = new StringBuilder ();
			
			for (int i = 0; i < num; ++i)
				output.append (str);
			
			return output.toString ();
			
		}
		
		public static String ucfirst (String str) {
			
			if (str != null && !str.equals (""))
				str = Character.toUpperCase (str.charAt (0)) + str.substring (1);
			
			return str;
			
		}
		
		public static boolean lcontains (String what, String where) {
			return (Int.size (where) >= Int.size (what) && where.substring (0, Int.size (what)).equals (what));
		}
		
		public static String cut (String string, int length) {
			return cut (string, length, "");
		}
		
		public static String cut (String string, int length, String end) { // Резалка строк (обрезает до length длины)
			
			if (length > 0 && Int.size (string) > length)
				string = string.substring (0, length) + end;
			
			return string;
			
		}
		
		public static Map<String, String> htmlSpecialChars () {
			
			Map<String, String> chars = new HashMap<> ();
			
			chars.put ("#34", "\"");
			chars.put ("#36", "$");
			chars.put ("#38", "&");
			chars.put ("#39", "'");
			chars.put ("#44", ",");
			chars.put ("#45", "-");
			chars.put ("#46", ".");
			chars.put ("#8218", "‚");
			chars.put ("#47", "/");
			chars.put ("#92", "\\");
			chars.put ("#60", "<");
			chars.put ("#62", ">");
			chars.put ("#64", "@");
			chars.put ("#96", "`");
			chars.put ("#40", "(");
			chars.put ("#41", ")");
			chars.put ("#123", "{");
			chars.put ("#125", "}");
			chars.put ("#124", "|");
			chars.put ("#8211", "–");
			chars.put ("#8212", "—");
			chars.put ("#160", " ");
			chars.put ("#171", "«");
			chars.put ("#187", "»");
			chars.put ("#8220", "“");
			chars.put ("#8221", "”");
			chars.put ("#8222", "„");
			chars.put ("#169", "©");
			chars.put ("#174", "®");
			chars.put ("#8482", "™");
			chars.put ("#8195", "		");
			chars.put ("#8226", "•");
			
			chars.put ("nbsp", chars.get ("#160"));
			chars.put ("amp", chars.get ("#38"));
			chars.put ("lt", chars.get ("#60"));
			chars.put ("gt", chars.get ("#62"));
			chars.put ("quot", chars.get ("#34"));
			chars.put ("laquo", chars.get ("#171"));
			chars.put ("raquo", chars.get ("#187"));
			chars.put ("copy", chars.get ("#169"));
			chars.put ("reg", chars.get ("#174"));
			chars.put ("emsp", chars.get ("#8195"));
			chars.put ("bull", chars.get ("#8226"));
			
			return chars;
			
		}
		
		public static String htmlSpecialCharsDecode (String str) {
			
			Map<String, String> chars = htmlSpecialChars ();
			
			for (String key : new String[] {"amp", "#38"})
				str = str.replace ("&" + key + ";", chars.get (key));
			
			str = str.replaceAll ("&\\s*(#?)\\s*([0-9]+)\\s*;", "&$1$2;");
			
			for (String key : chars.keySet ())
				str = str.replace ("&" + key + ";", chars.get (key));
			
			str = str.replace (" \n", "\n");
			
			return str;
			
		}
		
		public static String toLower (String str) {
			return str.toLowerCase ();
		}
		
		public static String toString (byte[] bytes) {
			
			Formatter formatter = new Formatter ();
			for (byte b : bytes) formatter.format ("%02x", b);
			
			String result = formatter.toString ();
			
			formatter.close ();
			
			return result;
			
		}
		
		public static String yearCopyright (int year) {
			
			int newYear = Integer.parseInt (Locales.date ("yyyy"));
			
			String output = String.valueOf (year);
			if (newYear > year) output += " - " + newYear;
			
			return output;
			
		}
		
		public static boolean isEmpty (Object string) {
			return (string == null || string.toString ().isEmpty ());
		}
		
		public static String slice (String str1, String str2) {
			return slice (str1, str2, true);
		}
		
		public static String slice (String str1, String str2, boolean revert) {
			
			int start = 0;
			boolean right = (Int.size (str2) > Int.size (str1));
			
			for (int i = 0; i < Int.size ((right ? str1 : str2)); ++i) {
				
				String letter1 = str1.substring (i, (i + 1));
				String letter2 = str2.substring (i, (i + 1));
				
				if (letter1.equals (letter2))
					++start;
				
			}
			
			if (start > 0) str1 = getStart (start, str1);
			if (revert) str1 = trimStart (str1, str2);
			
			return str1;
			
		}
		
		public interface Callback {
			String match (MatchResult matchResult);
		}
		
		public static String replace (String regex, Callback callback, String string) {
			
			StringBuffer resultString = new StringBuffer ();
			Matcher regexMatcher = Pattern.compile (regex).matcher (string);
			
			while (regexMatcher.find ()) {
				regexMatcher.appendReplacement (resultString, callback.match (regexMatcher));
			}
			
			regexMatcher.appendTail (resultString);
			
			return resultString.toString ();
			
		}
		
		public static String translit (String str) {
			
			Map<String, String> letters = new HashMap<> ();
			
			letters.put ("\u0410", "A");
			letters.put ("\u0411", "B");
			letters.put ("\u0412", "V");
			letters.put ("\u0413", "G");
			letters.put ("\u0414", "D");
			letters.put ("\u0415", "E");
			letters.put ("\u0401", "Yo");
			letters.put ("\u0416", "Zh");
			letters.put ("\u0417", "Z");
			letters.put ("\u0418", "I");
			letters.put ("\u0419", "J");
			letters.put ("\u041a", "K");
			letters.put ("\u041b", "L");
			letters.put ("\u041c", "M");
			letters.put ("\u041d", "N");
			letters.put ("\u041e", "O");
			letters.put ("\u041f", "P");
			letters.put ("\u0420", "R");
			letters.put ("\u0421", "S");
			letters.put ("\u0422", "T");
			letters.put ("\u0423", "U");
			letters.put ("\u0424", "F");
			letters.put ("\u0425", "H");
			letters.put ("\u0426", "C");
			letters.put ("\u0427", "Ch");
			letters.put ("\u0428", "Sh");
			letters.put ("\u0429", "Sch");
			letters.put ("\u042a", "");
			letters.put ("\u042b", "Y");
			letters.put ("\u042c", "");
			letters.put ("\u042d", "Ye");
			letters.put ("\u042e", "Yu");
			letters.put ("\u042f", "Ya");
			letters.put ("\u0407", "Yi");
			letters.put ("\u0404", "Ye");
			
			letters.put ("\u0430", "a");
			letters.put ("\u0431", "b");
			letters.put ("\u0432", "v");
			letters.put ("\u0433", "g");
			letters.put ("\u0434", "d");
			letters.put ("\u0435", "e");
			letters.put ("\u0451", "yo");
			letters.put ("\u0436", "zh");
			letters.put ("\u0437", "z");
			letters.put ("\u0438", "i");
			letters.put ("\u0439", "j");
			letters.put ("\u043a", "k");
			letters.put ("\u043b", "l");
			letters.put ("\u043c", "m");
			letters.put ("\u043d", "n");
			letters.put ("\u043e", "o");
			letters.put ("\u043f", "p");
			letters.put ("\u0440", "r");
			letters.put ("\u0441", "s");
			letters.put ("\u0442", "t");
			letters.put ("\u0443", "u");
			letters.put ("\u0444", "f");
			letters.put ("\u0445", "h");
			letters.put ("\u0446", "c");
			letters.put ("\u0447", "ch");
			letters.put ("\u0448", "sh");
			letters.put ("\u0449", "sch");
			letters.put ("\u044a", "");
			letters.put ("\u044b", "y");
			letters.put ("\u044c", "");
			letters.put ("\u044d", "ye");
			letters.put ("\u044e", "yu");
			letters.put ("\u044f", "ya");
			letters.put ("\u0457", "i");
			letters.put ("\u0454", "ie");
			
			for (String key : letters.keySet ())
				str = str.replace (key, letters.get (key));
			
			return str;
			
		}
		
		public static String altName (String str) {
			
			Map<String, String> chars = htmlSpecialChars ();
			
			for (String key : chars.keySet ())
				str = str.replace ("&" + chars.get (key) + ";", "");
			
			str = toLower (str).replaceAll ("\\s+", "-");
			
			return str;
			
		}
		
		public static String toCamelCase (String word) {
			
			char first = Character.toUpperCase (word.charAt (0));
			
			if (word.length () == 1)
				return Character.toString (first);
			else
				return first + word.substring (1).toLowerCase ();
			
		}
		
		public static String toCamelCase (String word, String split) {
			
			StringBuilder sb = new StringBuilder (word.length ());
			
			for (String s : word.split (split)) {
				
				if (sb.length () > 0)
					sb.append (split);
				
				sb.append (toCamelCase (s));
				
			}
			
			return sb.toString ();
			
		}
		
	}