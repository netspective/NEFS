/*
* @author                        : Reghu
* @Version                       : 1.0
*
* Development Environment        :  Oracle9i JDeveloper 
* Name of the File               :  FileUpload.java
* Creation/Modification History  :
*
* Reghu    21-Jan-2002     Created
*
*/

package com.netspective.sparx.fileupload;

// Java IO classes
import java.io.IOException;

// Servlet related classes
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;

/**
 * A wrapper class for the HttpServletRequest to handle multipart/form-data 
 * requests, the kind of requests that support file uploads. 
 */
public class FileUploadWrapper extends HttpServletRequestWrapper {

  // The file upload utility class instance
  FileUpload mreq = null;
  
  /**
   * Returns the value of a request parameter as a String, or null if 
   * the parameter does not exist.The method overrides parent method.
   * @param <b>name</b> The name of the parameter
   * @return <b>String </b> The value of the paramter
   */
  public String getParameter( String name ) {
    return mreq.getParameter( name );
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
    return mreq.getParameterNames();
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
    return mreq.getParameterValues( name );
  }
  
  /**
   * The constructor for the HttpServletRequestWrapper
   * @param <b>request </b>The original request
   * @param <b>dir</b> file upload directory path
   * @throws <b>IOException</b> If there was a failure while instantiating
   * the FileUpload object
   */
  public FileUploadWrapper( HttpServletRequest req, String dir,String prefix, String uploadFileArg )
              throws IOException {
    super( req );
    mreq = new FileUpload( req, dir, prefix, uploadFileArg );
  }
}
