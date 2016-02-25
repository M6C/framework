package framework.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.ressource.util.UtilFileUpload;

public class ActionFileReader extends HttpServlet {

  //Initialize global variables
  public void init() throws ServletException {
  }

  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Use a ServletOutputStream because we may pass binary information
    ServletOutputStream out = response.getOutputStream();

    // Get the file to view
    String fileName = request.getParameter("file");
    String contentType = request.getParameter("contentType");
    String name = request.getParameter("name");

    // No file, nothing to view
    if (fileName == null) {
      out.println("No file to view");
      return;
    }

    // Decodage du nom du fichier
    try {
      fileName = java.net.URLDecoder.decode(fileName, "UTF-8");
    } catch (Exception ex) {}

    File filePathMain = getFileRootSource(request);
    File file = (filePathMain==null) ? new File(fileName) : new File(filePathMain, fileName);
    if (!file.exists()) {
      out.println("File don't exist: " + fileName);
      return;
    }

    if (file.isDirectory()) {
      out.println("The specified path is a directory: " + fileName);
      return;
    }

    if ((name==null)||(name.equals("")))
      name = file.getName();


    // Get and set the type of the file
    // String contentType = getServletContext().getMimeType(file);
    contentType = (contentType!=null) ? contentType : file.toURL().openConnection().getContentType();
    response.setContentType(contentType);
    response.setHeader("Content-Disposition", "attachment; filename=" + name);
    response.setHeader("Content-Length", Long.toString(file.length()));

    // Return the file
    try {
      returnFile(file, out);
    }
    catch (FileNotFoundException e) {
      out.println("File not found: " + fileName);
    }
    catch (IOException e) {
      out.println("Problem sending file: " + fileName + " -> " + e.getMessage());
    }
  }

  protected File getFileRootSource(HttpServletRequest req) {
    return null;
  }

  //Clean up resources
  public void destroy() {
  }

  // Send the contents of the file to the output stream
  public static void returnFile(File file, OutputStream out) throws FileNotFoundException, IOException {
    // A FileInputStream is for bytes
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      byte[] buf = new byte[4 * 1024]; // 4K buffer
      int bytesRead;
      while ( (bytesRead = fis.read(buf)) != -1) {
        out.write(buf, 0, bytesRead);
      }
    }
    finally {
      if (fis != null) fis.close();
    }
  }
}
