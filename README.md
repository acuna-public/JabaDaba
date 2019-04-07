# JabaDaba
JabaDaba is a powerful wrapper for Java<br>
<br>

**Usage**

Use needed methods for necessary operations, for example:<br>
<br>

    Files.write (Object object, String|File file[, boolean append = false, String charset = "utf-8"])
    
Write stringified `Object` to file. Put `append` to true if you want to write at the end of existing file.<br>
<br>

    Files.write (List<?> array, String|File file[, boolean append = false, String charset = "utf-8"])
    
Write `ArrayList` to file. Put `append` to true if you want to write at the end of existing file.<br>
<br>

    String Files.read (String|File|InputStream input[, String charset = "utf-8", int buffer = 4096])		

Read file or stream to String where `buffer` is buffer size.<br>
<br>

    List<String> Files.read (String|File|InputStream input, List<String> output = new ArrayList<> ())
    
Read file or stream to `ArrayList`.<br>
<br>

    Object[] Arrays.toArray (JSONArray item)
    
Convert `JSONArray` to `Object` array.

Etc.

Official documentation will be soon.
