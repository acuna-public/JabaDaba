  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 17.07.2017
  */
  
  import java.lang.System;
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.util.ArrayList;
  import java.util.Calendar;
  import java.util.Date;
  import java.util.LinkedHashMap;
  import java.util.List;
  import java.util.Locale;
  import java.util.Map;
  import java.util.TimeZone;
  
  public class Locales {
    
    public static final String LANG = "lang";
    public static final String LANG_DISPLAY = "lang_display";
    public static final String LANG_NAME = "lang_name";
    public static final String LANG_ISO3 = "lang_iso3";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_DISPLAY = "country_display";
    public static final String COUNTRY_ISO3 = "country_iso3";
    public static final String LOCALE = "locale";
    
    public static String[] formats = new String[] {
      
      "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ", // ISO 8601
      "dd.MM.yyyy HH:mm:ss",
      "dd.MM.yyyy",
      "d MMMM yyyy HH:mm",
      "d MMMM yyyy HH:mm:ss",
      "d MMMM yyyy",
      "yyyy/MM/dd - HH:mm:ss",
      "yyyyMMddHHmmss",
      "yyyy.MM.dd",
      "dd.MM.yy HH:mm",
      
    };
    
    public static String date () {
      return date (0);
    }
    
    public static String date (boolean isOffset) {
      return date (0, time (isOffset));
    }
    
    public static String date (int type) {
      return date (type, time ());
    }
    
    public static String date (int type, boolean isOffset) {
      return date (type, time (isOffset));
    }
    
    public static String date (int type, Date date) {
      return date (type, toTime (date));
    }
    
    public static String date (int type, long time) {
      return date (type, time, true);
    }
    
    public static String date (int type, long time, boolean offset) {
      return date (formats[type], time, offset);
    }
    
    public static String date (String format) {
      return date (format, time ());
    }
    
    public static String date (String format, long time) {
      return date (format, time, true);
    }
    
    public static String date (String format, long time, boolean isOffset) {
      return date (format, time (time, isOffset), Locale.getDefault ());
    }
    
    public static String date (String format, long date, Locale locale) {
      
      SimpleDateFormat sdf = new SimpleDateFormat (format, locale);
      sdf.setTimeZone (TimeZone.getTimeZone ("UTC"));
      
      return sdf.format (date);
      
    }
    
    public static long time () {
      return time (true);
    }
    
    public static long time (boolean isOffset) {
      return time (System.currentTimeMillis (), isOffset);
    }
    
    public static long time (long time, boolean isOffset) {
      
      long offset = 0;
      
      if (isOffset) {
        
        Calendar cal = Calendar.getInstance ();
        offset = cal.getTimeZone ().getOffset (time);
        
      }
      
      return time (time, offset);
      
    }
    
    public static long time (long time, long offset) {
      return (time + offset);
    }
    
    public static Date toDate (String date, int type) throws ParseException {
      return toDate (date, formats[type]);
    }
    
    public static Date toDate (String date, String pattern) throws ParseException {
      return toDate (date, pattern, Locale.getDefault ());
    }
    
    public static Date toDate (String date, int type, Locale locale) throws ParseException {
      return toDate (date, formats[type], locale);
    }
    
    public static Date toDate (String date, String pattern, Locale locale) throws ParseException {
      
      SimpleDateFormat sdf = new SimpleDateFormat (pattern, locale);
      return sdf.parse (date);
      
    }
    
    public static String toTimeStamp (String date, int type) throws ParseException {
      return toTimeStamp (date, formats[type]);
    }
    
    public static String toTimeStamp (String date, String pattern) throws ParseException {
      return toTimeStamp (date, pattern, Locale.getDefault ());
    }
    
    public static String toTimeStamp (String date, String pattern, Locale locale) throws ParseException {
      
      long time = toTime (date, pattern, locale);
      return date (0, time);
      
    }
    
    public static long toTime (String date, int type) throws ParseException {
      return toTime (date, formats[type]);
    }
    
    public static long toTime (String date, String pattern) throws ParseException {
      return toTime (date, pattern, Locale.getDefault ());
    }
    
    public static long toTime (String date, String pattern, Locale locale) throws ParseException {
      
      Date dateObject = toDate (date, pattern, locale);
      return toTime (dateObject);
      
    }
    
    public static long toTime (Date date) {
      return time (date.getTime (), false);
    }
    
    public static Locale getLocale () {
      return Locale.getDefault ();
    }
    
    public static Locale getLocale (String lang) {
      return new Locale (lang);
    }
    
    public static Locale getLocale (String lang, String country) {
      return new Locale (lang, country);
    }
    
    public static Locale getLocale (String lang, String country, String variant) {
      return new Locale (lang, country, variant);
    }
    
    public static List<Locale> addLocale (Locale locale, List<Locale> locales) {
      
      if (!locales.contains (locale))
        locales.add (locale);
      
      return locales;
      
    }
    
    public static List<Locale> getLocales () {
      return getLocales (new ArrayList<Locale> ());
    }
    
    public static List<Locale> getLocales (List<Locale> locales) {
      
      addLocale (Locale.ENGLISH, locales);
      addLocale (getLocale (), locales);
      
      return locales;
      
    }
    
    public static Map<String, String> getLocaleData () {
      return getLocaleData (getLocale ());
    }
    
    public static Map<String, String> getLocaleData (String lang) {
      return getLocaleData (getLocale (lang));
    }
    
    public static Map<String, String> getLocaleData (Locale locale) {
      
      Map<String, String> output = new LinkedHashMap<> ();
      
      output.put (LANG, locale.getLanguage ()); // en
      output.put (LANG_DISPLAY, Strings.ucfirst (locale.getDisplayLanguage ())); // English
      output.put (LANG_NAME, Strings.ucfirst (locale.getDisplayName ())); // English (United States)
      output.put (LANG_ISO3, locale.getISO3Language ()); // eng
      
      output.put (COUNTRY, locale.getCountry ()); // US
      output.put (COUNTRY_DISPLAY, locale.getDisplayCountry ()); // United States
      output.put (COUNTRY_ISO3, locale.getISO3Country ()); // USA
      
      output.put (LOCALE, locale.toString ()); // en_US
      
      return output;
      
    }
    
    public static Map<String, Map<String, String>> getLocalesData () {
      return getLocalesData (getLocales ());
    }
    
    public static Map<String, Map<String, String>> getLocalesData (List<?> locales) {
      return getLocalesData (locales, new LinkedHashMap<String, Map<String, String>> ());
    }
    
    public static Map<String, Map<String, String>> getLocalesData (List<?> locales, Map<String, Map<String, String>> langs) {
      
      for (int i = 0; i < Int.size (locales); ++i) {
        
        Object locale = locales.get (i);
        
        Map<String, String> data;
        
        if (locale instanceof java.util.Locale)
          data = getLocaleData ((java.util.Locale) locale);
        else
          data = getLocaleData ((String) locale);
        
        langs.put (data.get (LANG), data);
        
      }
      
      return langs;
      
    }
    
    public static String getLang () {
      return getLocaleData ().get (LANG);
    }
    
    public static List<String> getLangTitles (List<?> locales) {
      
      List<String> output = new ArrayList<> ();
      
      Map<String, Map<String, String>> loc = getLocalesData (locales);
      
      for (String key : loc.keySet ()) {
        
        Map<String, String> locale = loc.get (key);
        output.add (locale.get (LANG_DISPLAY));
        
      }
      
      return output;
      
    }
    
  }