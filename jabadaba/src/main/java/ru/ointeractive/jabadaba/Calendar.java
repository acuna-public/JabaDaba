	package ru.ointeractive.jabadaba;
	
	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.GregorianCalendar;
	import java.util.Locale;
	
	public class Calendar extends GregorianCalendar {
		
		public Calendar () {
			this (Locales.time ());
		}
		
		public Calendar (Calendar calendar) {
			this (calendar.getTimeInMillis ());
		}
		
		public Calendar (long time) {
			setTime (time);
		}
		
		public Calendar addNewDay () {
			return addNewDay (1);
		}
		
		public Calendar addNewDay (int day) {
			
			add (Calendar.DATE, day);
			return this;
			
		}
		
		public void setTime (long time) {
			setTime (new Date (time));
		}
		
		public boolean isSameDay (Calendar dstCal) {
			
			return isSameDay (
				
				get (Calendar.YEAR),
				get (Calendar.MONTH),
				get (Calendar.DAY_OF_MONTH),
				
				dstCal.get (Calendar.YEAR),
				dstCal.get (Calendar.MONTH),
				dstCal.get (Calendar.DAY_OF_MONTH)
				
			);
			
		}
		
		private boolean isSameDay (int lastYear, int lastMonth, int lastDay, int year, int month, int day) {
			return (lastYear == year && lastMonth == month && lastDay == day);
		}
		
		@Override
		public String toString () {
			return Locales.date (2, getTimeInMillis ());
		}
		
		public String getDate (String format) {
			return getDate (format, Locale.getDefault ());
		}
		
		public String getDate (String format, Locale locale) {
			
			SimpleDateFormat format1 = new SimpleDateFormat (format, locale);
			return format1.format (getTime ());
			
		}
		
	}