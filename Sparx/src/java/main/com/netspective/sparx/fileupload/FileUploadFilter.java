/*
* @author                        : Reghu
* @Version                       : 1.0
*
* Development Environment        :  Oracle9i JDeveloper 
* Name of the File               :  FileUploadFilter.java
* Creation/Modification History  :
*
* Reghu    21-Jan-2002     Created
* 
*/
package com.netspective.sparx.fileupload;

// Java IO classes
import java.io.IOException;
import java.io.File;

// Servlet related classes
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletResponse;

// Servlet Filter classes
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netspective.commons.script.BeanScript;

/**
 * This class is implemented as a Servlet Filter class to upload files to server
 * The filter detects multipart form data requests and uploads any file to an 
 * upload directory. The upload directory is specified relative the webapp root
 * in web.xml as an init param to the filter
 */
public final class FileUploadFilter implements Filter {

  private Log log = LogFactory.getLog(FileUploadFilter.class);
  // Absolute path to the upload directort
  private String uploadDir = null;
  private String uploadPrefix = null;
  private String uploadFileArg = null;
  private static final String UPLOAD_DIR="uploadDir";
  private static final String UPLOAD_PREFIX="uploadPrefix";
  private static final String UPLOAD_PREFIX_DEFAULT="sparxUpload-";
  private static final String UPLOAD_FILE_ARG="uploadFileArg";
  public static final String UPLOAD_FILE_ARG_DEFAULT="sparxUpload";

  /**
   * This method is called by the server before the filter goes into service, 
   * and here it determines the file upload directory.
   * @param <b>config</b> The filter config passed by the servlet engine
   * @throws <b>ServletException</b> 
   */   
  public void init( FilterConfig config )
              throws ServletException {

    // Get the name of the upload directory.
//    try {
////      java.net.URL uploadDirURL = config.getServletContext().
////                         getResource( config.getInitParameter( "uploadDir" ) );
//        File f = new File(config.getInitParameter( UPLOAD_DIR ));
//      log.info("uploadDir is: " + uploadDirURL.toString());
//      uploadDir = uploadDirURL.getFile();
//
//    } catch( java.net.MalformedURLException ex ) {
//      throw new ServletException( ex.getMessage() );
//    }
    uploadDir = config.getInitParameter( UPLOAD_DIR );
    log.debug("uploadDir from context: " + uploadDir);

    // If upload directory parameter is null, assign a temp directory
    if( uploadDir == null ) {
      File tempdir = (File)config.getServletContext().
                                getAttribute( "javax.servlet.context.tempdir" );
      if( tempdir != null ) {
        uploadDir = tempdir.toString();
      } else {
        throw new ServletException( "Error in FileUploadFilter : No upload "+
                                    "directory found: set an uploadDir init "+
                                    "parameter or ensure the " +
                                    "javax.servlet.context.tempdir directory "+
                                    "is valid" );
      }
    }
    uploadPrefix = config.getInitParameter( UPLOAD_PREFIX );
    if (uploadPrefix == null)
        uploadPrefix = UPLOAD_PREFIX_DEFAULT;
    log.info("uploadPrefix is: " + uploadPrefix);

     uploadFileArg = config.getInitParameter( UPLOAD_FILE_ARG);
    if (uploadFileArg == null)
        uploadFileArg = UPLOAD_FILE_ARG_DEFAULT;
    log.info("uploadFileArg is: " + uploadFileArg);

  }

  /**
   * This method performs the actual filtering work .In its doFilter() method, 
   * each filter receives the current request and response, as well as a 
   * FilterChain containing the filters that still must be processed. Here 
   * the doFilter() method examines the request content type, and if it is a 
   * multipart/form-data request, wraps the request with a FileUpload class. 
   * @param <b> request </b> The servlet request
   * @param <b> response </b> The servlet response
   * @param <b> chain </b> Object representing the chain of all filters
   * @throws <b>ServletException</b> 
   * @throws <b>IOException</b>
   */
  public void doFilter( ServletRequest request, 
                        ServletResponse response, 
                        FilterChain chain )
                        throws IOException,ServletException {
                        
    HttpServletRequest req = (HttpServletRequest)request;
    
    // Get the content type from the request
    String content = req.getHeader( "Content-Type" );
    
    // If the content type is not a multipart/form-data , continue
    if( content == null || !content.startsWith( "multipart/form-data" ) ) {
      chain.doFilter( request, response );
    } else {
      log.debug("Procesing multipart request");
      FileUploadWrapper load = new FileUploadWrapper( req, uploadDir,uploadPrefix, uploadFileArg );
      chain.doFilter( load, response );
    }
  }

  /**
   * This method is called Called after the filter has been taken 
   * out of service.
   */
  public void destroy() {
  }
}
