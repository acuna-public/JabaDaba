  package pro.acuna.jabadaba;
  /*
   Created by Acuna on 08.11.2018
  */
  
  import java.util.Calendar;
  import java.util.Date;
  
  public class Time {
    
    private Calendar calendar;
    
    public Time () {
      this (Locales.time (true));
    }
    
    public Time (long time) {
      
      calendar = Calendar.getInstance ();
      calendar.setTime (new Date (time));
      
    }
    
    /*public Time addMS (int time) {
      
    }
    
    public Time addSecond (int num) {
      addMS (1000 * num);
    }
    
    public Time addMinute (int num) {
      addSecond (60 * num);
    }
    
    public Time addHour (int num) {
      addMinute (60 * num);
    }*/
    
    public long getTime () {
      return calendar.getTimeInMillis ();
    }
    
    public Time addDay (int num) {
      
      calendar.add (Calendar.DATE, num);
      return this;
      
    }
    
    public Time addWeek (int num) {
      return addDay (7 * num);
    }
    
    public Time addMonth (int num) {
      return addWeek (4 * num);
    }
    
    public Time addYear (int num) {
      return addMonth (12 * num);
    }
    
  }