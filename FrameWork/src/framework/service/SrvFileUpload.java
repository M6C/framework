//http://forum.clubic.com/forum2.php3?config=&post=14860&cat=13&cache=&sondage=&owntopic=0&p=1&trash=&subcat=71

package framework.service;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.beandata.BeanGenerique;
import framework.ressource.util.UtilFileUpload;

public class SrvFileUpload extends SrvGenerique {

  //public void execute(HttpServletRequest request, HttpServletResponse response, BeanGenerique bean) throws Exception{
  public void execute(ServletRequest request, ServletResponse response, BeanGenerique bean) throws Exception{
    String racine = request.getParameter("path");
    String pSize = request.getParameter("size");
    if ((racine!=null)&&(pSize!=null)) {
      int size = (pSize == null) ? 5 : Integer.parseInt(pSize);

      response.setContentType("text/html");
      PrintWriter out = response.getWriter();

      try {
        // Blindly take it on faith this is a multipart/form-data request

        // Construct a MultipartRequest to help read the information.
        // Pass in the request, a directory to saves files to, and the
        // maximum POST size we should attempt to handle.
        UtilFileUpload multi = new UtilFileUpload(request, racine, size * 1024 * 1024);

        // if a subpath is defined in the request in a 'subpath' parameter,
        // move the uploaded file from '[racine]' to '[racine]/[subpath]'.
        String directory = multi.getParameter("subpath");

        if (directory != null && !directory.equals("")) {
          if (!racine.endsWith(File.separator))
            racine += File.separator;
          if (!directory.endsWith(File.separator))
            directory += File.separator;

          File repertoireDefinitif = new File(racine + directory);
          if (!repertoireDefinitif.exists())
            repertoireDefinitif.mkdirs();

          out.println("<html>");
          out.println("<body>");
          Enumeration files = multi.getFileNames();
          while (files.hasMoreElements()) {
            String name = (String) files.nextElement();
            String filename = racine + directory + multi.getFilesystemName(name);
            out.println("File Name:" + filename);out.flush();
            File f = multi.getFile(name);
            if (f != null)
              f.renameTo(new File(filename));
          }
        }
        out.println("File successfully uploaded");
        out.println("</body></html>");
        out.close();
      }
      catch (Exception e) {
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
  }

  /**
   * Process the HTTP Post request
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  }

  /**
   * Get Servlet information
   * @return java.lang.String
   */
  public String getServletInfo() {
    return "UploadFile Information";
  }
}
