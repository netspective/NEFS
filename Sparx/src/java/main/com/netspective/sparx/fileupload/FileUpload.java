/*
* @author                        : Neelesh
* @Version                       : 1.0
*
* Development Environment        : Oracle9i JDeveloper
* Name of the File               : FileUpload.java
* Creation/Modification History  :
*
* Neelesh        05-Apr-2002      Created
*
*/
package com.netspective.sparx.fileupload;

// Java util classes
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

// Java IO classes
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple file upload utility . The class uploads the file from a multipart 
 * encoded form to a given directory. 
 */
public class FileUpload {
   private Log log = LogFactory.getLog(FileUpload.class);
  // The multipart request
  private HttpServletRequest req;

  // Upload dir
  private String dir ;

  // Name of the file being uploaded currently
  private String fileName;

  // Table of form fields v/s values
  private Hashtable map = new Hashtable();

  // The boundary
  private String boundary = "--";
  
  private byte[] buff= new byte[100*1024];
  
  // Input stream from the request
  private ServletInputStream in;
  
  // Name of the form field
  private String paramName;

  // Content disposition name
  private String contentDisp;

  // tempFIle prefix
  private String prefix;

  // request variable for file
  private String uploadFileArg;

  /**
   * The constructor takes the request and upload dir path as paramters and 
   * uploads all the files in the request to the dir.
   * @param <b>r</b> - multipart encoded HttpServletRequest 
   * @param <b>uploadDir</b> - The directory to which the files should be uploaded
   * @throws <b>IOException</b>  - If there was a problem reading the stream or
   * writing to the file.
   */
  public FileUpload(HttpServletRequest r,String uploadDir, String prefix, String uploadFileArg) throws IOException {
    req = r;
   	dir = uploadDir;
    this.prefix = prefix;
    this.uploadFileArg = uploadFileArg;
    upload();
  }

  /**
   * The method reads the next line from the servlet input stream into a byte 
   * array and returns a String form of the array.Returns null if it reaches the 
   * end of the stream
   * @return <b>String</b> The next line in the stream
   * @throws <b>IOException</b>  - If there was a problem reading the stream or
   */
  private String readLine() throws IOException {
    int len = 0;
    String line = null;
    len= in.readLine(buff,0,buff.length);
    if (len<0) return null;
    line = new String(buff,0,len, "ISO-8859-1");

    return line;
  }

  /**
   * The method loops through the lines in the input stream and calls 
   * writeToFile() if it encounters a file and readParam() if it encounters a
   * form field
   * @throws <b>IOException</b>  - If there was a problem reading the stream or
   */
  public void upload() throws IOException{
      log.debug("upload() - processing request");
    // Set the boundary string
    setBoundary();
  
   	// The request stream
    in = req.getInputStream();
    int len = 0;

 	  // the first line is the boundary , ignore it
	   String line = readLine();
    while((line= readLine())!=null) {
        log.debug("from request: " + line);
      // set dispostion, param name and filename
      setHeaders(line);

      // skip next line
      line=readLine();

      // if there is a content-type specified, skip next line too,
      // and this is a file, so upload it to the file system
      if(line.toLowerCase().startsWith("content-type")){
         line=readLine();
         writeToFile();
         continue;
      } else {
         // its  a form field, read it.
         readParam();
      }
    }
  }

 /**
  * Sets the boundary string for this request
  */
  private  void setBoundary()throws IOException{
    String temp = req.getContentType();
    int index = temp.indexOf("boundary=");
    boundary += temp.substring(index+9,temp.length());
  }


 /**
  * Reads the form field and puts it in to a table
  */
  private void readParam() throws IOException{
    String line  = null;
    StringBuffer buf=  new StringBuffer();
    while (!(line=readLine()).startsWith(boundary)){
       buf.append(line);
    }
    line = buf.substring(0,buf.length()-2);
    if(map.containsKey(paramName)) {
       Object existingValue = map.get(paramName);
       List valueList = null;
       if( existingValue instanceof List) {
          valueList = (List)existingValue;
       } else {
          valueList=  new ArrayList();
          valueList.add(existingValue);
       }
       valueList.add(line);
       map.put(paramName,valueList);
    }
    map.put(paramName,line);
  }
 
  /**
   * Sets the content disposition, param name and file name fields
   * @param <b>line</b> the content-disposition line
   */
  public  void setHeaders( String line){
    StringTokenizer tokens =  new StringTokenizer(line,";",false);
    String token = tokens.nextToken();
    String temp  = token.toLowerCase();
    int index    = temp.indexOf("content-disposition=");

    contentDisp = token.substring(index+21,token.length());
    token =  tokens.nextToken();
    temp = token.toLowerCase();
    index = token.indexOf("name=");
    paramName = token.substring(index+6,token.lastIndexOf('"'));
    fileName = null;

    if (tokens.hasMoreTokens()) {
        token =  tokens.nextToken();
        temp = token.toLowerCase();
        index = token.indexOf("filename=");
        fileName = token.substring(index+10,token.length());
        index = fileName.lastIndexOf('/');
        if(index<0) {
          index = fileName.lastIndexOf('\\');
        }
        if(index <0) {
          fileName = fileName.substring(0,fileName.lastIndexOf('"'));
        } else {
          fileName = fileName.substring(index+1,fileName.lastIndexOf('"'));
        }
    }
  }

  /**
   * Reads the file content from the stream and writes it to the local file system
   * @throws <b>IOException</b>  - If there was a problem reading the stream 
   */
  private void writeToFile() throws IOException{
     // Open an o/p stream
     File tmpFile = File.createTempFile(prefix,"upload", new File(dir));
     tmpFile.deleteOnExit();
     log.debug("Setting: " + uploadFileArg + " to: " + tmpFile.getPath());
     map.put(uploadFileArg, tmpFile.getPath());
     //FileOutputStream out = new FileOutputStream (dir+File.separator+fileName);
     FileOutputStream out = new FileOutputStream (tmpFile);

     // this flag checks if \r\n needs to be written to the file
     // the servlet output stream appends these characters at the end of the 
     // last line of the content, which should be skipped
     // so in the loop, all \r\n but for the last line are written to the file
     boolean writeCR = false;
     int len = 0;
     String line = null;
     map.put(paramName,fileName);

     // for each line
     while((len=in.readLine(buff,0,buff.length))>-1) {
       line =  new String(buff,0,len);

        // if end of content, break
        if (line.startsWith(boundary)) break;
        if(writeCR){
          writeCR=false;
          out.write('\r');
          out.write('\n');
        }
        if(len>2 && buff[len-2]=='\r' && buff[len-1]=='\n') {
          writeCR=true;
          out.write(buff,0,len-2);
        } else {
          out.write(buff,0,len);
        }
      }
			out.close();
   }

  /**
   * Returns the value of a request parameter as a String, or null if 
   * the parameter does not exist.The method overrides parent method.
   * @param <b>name</b> The name of the parameter
   * @return <b>String </b> The value of the paramter
   */
  public String getParameter( String name ) {
      log.debug("calling getParameter for :" + name);
    Object val = map.get(name);
      if (val == null)
        return null;
      log.debug("val is a: " + val.getClass().getName());
    if(val instanceof String) {
      return (String)val;
    } else {
      List vals = (List)val;
      return (String)vals.get(0);
    }
  }
  
  /**
   * Returns an Enumeration of String objects containing the names of the 
   * parameters contained in this request. If the  request has no parameters, 
   * the method returns an empty Enumeration.The method overrides parent method.
   * @return <b>Enumeration </b>of String objects, each String containing the 
   * name of a request parameter; or an empty Enumeration if the request has 
   * no parameters
   */
  public java.util.Enumeration getParameterNames() {
    return map.keys();
  }

  /**
   * Returns an array of String objects containing all of the values the given 
   * request parameter has, or null if the parameter does not exist.
   * The method overrides parent method.
   * @param <b>name</b> the name of the parameter whose value is requested
   * @return <b><String[]</b> an array of String objects containing the 
   * parameter's values
   */
  public String[] getParameterValues( String name ) {
    Object val = map.get(name);
    if(val instanceof String){
      return new String[]{(String)val};
    } else {
      List vals = (List)val;
      return (String[])vals.toArray(new String[vals.size()]);
    }
  }
}
