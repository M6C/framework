//http://forum.clubic.com/forum2.php3?config=&post=14860&cat=13&cache=&sondage=&owntopic=0&p=1&trash=&subcat=71

package framework.action;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import org.w3c.dom.Document;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.ressource.util.UtilFileUpload;

public class ActionFileUpload extends HttpServlet {

  private String pathTmp;

  /**
   * Initialize global variables
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    pathTmp = getInitParameter("Temporary_Directory");
    if (pathTmp==null) pathTmp = "../";
  }

  /**
   * Process the HTTP Post request
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();

    int size = (req.getParameter("size") != null) ? Integer.parseInt(req.getParameter("size")) : 5;
    try {
      // Blindly take it on faith this is a multipart/form-data request

      // Construct a MultipartRequest to help read the information.
      // Pass in the request, a directory to saves files to, and the
      // maximum POST size we should attempt to handle.
      UtilFileUpload multi = new UtilFileUpload(req, pathTmp, size * 1024 * 1024);

      // if a subpath is defined in the request in a 'subpath' parameter,
      // move the uploaded file from '[pathTmp]' to '[subpath]'.
      String directory = multi.getParameter("path");

      if (directory != null) {// && !directory.equals("")) {
        if (!directory.endsWith(File.separator))
          directory += File.separator;

        File filePathMain = getFileRootSource(req, multi);
        File repertoireDefinitif = (filePathMain==null) ? new File(directory) : new File(filePathMain, directory);
        if (!repertoireDefinitif.exists())
          repertoireDefinitif.mkdirs();

        Enumeration files = multi.getFileNames();
        while (files.hasMoreElements()) {
          String name = (String)files.nextElement();
          String filename = multi.getFilesystemName(name);
          File f = multi.getFile(name);
          if (f != null)
            //f.renameTo( (filePathMain==null) ? new File(directory + filename) : new File(filePathMain, directory + filename));
          	f.renameTo(new File(repertoireDefinitif, filename));
        }
      }
      out.println("<html>");
      out.println("<body>");
      out.println("File successfully uploaded");
      out.println("</body></html>");
      out.close();
    } catch (Exception e) {
      out.println("<HTML>");
      out.println("<HEAD><TITLE>UploadFile error</TITLE></HEAD>");
      out.println("<BODY>");
      out.println("<H2>UploadFile Error</H2>");
      out.println("<PRE>");
      e.printStackTrace(out);
      out.println("</PRE>");
      out.println("</BODY></HTML>");
      out.close();
    }
  }

  protected File getFileRootSource(HttpServletRequest req, UtilFileUpload multi) {
    return null;
  }

  /**
   * Get Servlet information
   * @return java.lang.String
   */
  public String getServletInfo() {
    return "UploadFile Information";
  }
}
