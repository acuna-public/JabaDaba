# JabaDaba
JabaDaba is a powerful wrapper for Java which helps to make your code pretty<br>
<br>

**Usage**

Use needed methods for necessary operations, for example:<br>
<br>

    Files.write (Object object, String|File file)
    Files.write (Object object, String|File file, boolean append)
    Files.write (Object object, String|File file, false, int charset)
    Files.write (Object object, String|File file, false, "utf-8")
    
Write stringified `Object` to file. Put `append` to *true* if you want to write at the end of existing file.<br>
<br>

    Files.write (List<?> array, String|File file)
    Files.write (List<?> array, String|File file, boolean append)
    Files.write (List<?> array, String|File file, false, String charset)
    Files.write (List<?> array, String|File file, false, "utf-8")
    
Write `List` to file. Put `append` to *true* if you want to write at the end of existing file.<br>
<br>

    String Files.read (String|File|InputStream input)		
    String Files.read (String|File|InputStream input, String charset)		
    String Files.read (String|File|InputStream input, "utf-8", int buffer)		
    String Files.read (String|File|InputStream input, "utf-8", 4096)		

Read file or stream to `String` where `buffer` is buffer size.<br>
<br>

    List<String> Files.read (String|File|InputStream input, List<String> output = new ArrayList<> ())
    
Read file or stream to `List<String>`.<br>
<br>

    Object[] Arrays.toArray (JSONArray item)
    
Convert `JSONArray` to `Object` array.

Etc.

Official documentation will be soon.
