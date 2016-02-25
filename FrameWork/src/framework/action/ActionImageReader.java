package framework.action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;
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
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import framework.ressource.util.UtilString;
import framework.ressource.util.image.Quantize;
import framework.trace.Trace;

public class ActionImageReader extends HttpServlet {

  //Initialize global variables
  public void init() throws ServletException {
  }

  //Process the HTTP Get request
  public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// Use a ServletOutputStream because we may pass binary information
	ServletOutputStream out = response.getOutputStream();

	// Get the file to view
	String contentType = request.getParameter("contentType");
	String name = request.getParameter("name");
	String scale = request.getParameter("scale");
	String width = request.getParameter("width");
	String height = request.getParameter("height");
	String maxSize = request.getParameter("maxSize");
	String resizeSmaller = request.getParameter("resizeSmaller");
    String maxColor = request.getParameter("maxColor");
    String qualityRate = request.getParameter("qualityRate");

	try
	{
	  float fScale = 0.0f;
	  int targetWidth=0;
	  int targetHeight=0;
	  boolean isResize = true;
	  
	  if (UtilString.isNotEmpty(resizeSmaller)){
		  try {
			  isResize = Boolean.getBoolean(resizeSmaller);
		  }
		  catch(Exception ex) {
		  }
	  }

	  // Get a path to the image to resize.
	  // ImageIcon is a kluge to make sure the image is fully
	  // loaded before we proceed.
//          Image sourceImage = new ImageIcon(Toolkit.getDefaultToolkit().getImage(request.getPathTranslated())).getImage();
          Image sourceImage = getImage(request, response);

	  // Calculate the target width and height
      if (isResize) {
		  fScale = parseScale(scale);
		  targetWidth = parseWidth(sourceImage, width);
		  targetHeight = parseHeight(sourceImage, height);
      } else {
		  int iWidth = sourceImage.getWidth(null);
		  int iHeight = sourceImage.getHeight(null);
		  targetWidth = parseWidth(sourceImage, width);
		  targetHeight = parseHeight(sourceImage, height);
		  if ((iWidth<targetWidth)&&(iHeight<targetHeight)) {
			  targetWidth = iWidth;
			  targetHeight = iHeight;
		  }
      }
	  if (fScale>0.0f) {
		targetWidth = (int)(targetWidth*fScale);
		targetHeight = (int)(targetHeight*fScale);
	  }
	  BufferedImage resizedImage = scaleImageBuffer(request, response, sourceImage,targetWidth,targetHeight);

	  if (resizedImage!=null) {

		  if (UtilString.isNotEmpty(maxColor)){
              try {
                resizedImage = Quantize.quantize(resizedImage , Integer.parseInt(maxColor));
              }
              catch (Exception ex) {
              }
          }

		  JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
          if (UtilString.isNotEmpty(qualityRate)){
              JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(resizedImage); 
              //param.setQuality(0.10f, false); // 90% quality JPEG
              //param.setQuality(((Integer.parseInt(qualityRate)(float))/100f)(float), false); // 90% quality jpeg
              //param.setQuality(Float.parseFloat("0."+qualityRate.replace('.', '')), false); // 90% quality jpeg
              float fQualityRate = (float)Float.parseFloat(qualityRate)/100f;
              param.setQuality(fQualityRate, false); // 90% quality jpeg
              encoder.setJPEGEncodeParam(param);
            }
		  encoder.encode(resizedImage);
	
		  // Get and set the type of the file
		  // String contentType = getServletContext().getMimeType(file);
		  // Output the finished image straight to the response as a JPEG!
		  contentType = (contentType!=null) ? contentType : "image/jpeg";
		  response.setContentType(contentType);
		  response.setHeader("Content-Disposition", "attachment; filename=" + name);
	  }

	  // Return the file
	  // returnFile(file, out);
	}
	catch (FileNotFoundException e) {
	  out.println("File not found");
	}
	catch (IOException e) {
	  out.println("Problem sending file: " + e.getMessage());
	}
	catch(Exception e) {
	  response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	  Trace.ERROR(this, e);
	}
  }

  //Clean up resources
  public void destroy() {
  }
  
  protected Image getImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ServletOutputStream out = response.getOutputStream();
		String fileName = request.getParameter("file");
		String name = request.getParameter("name");
		String maxSize = request.getParameter("maxSize");

		// No file, nothing to view
		if (fileName == null) {
		  out.println("No file to view");
		  throw new Exception("No file to view");
		}
		
		File rootDirectory = getRootDirectory(request, response);
		if (rootDirectory!=null) {
			if (!rootDirectory.exists()) {
				  out.println("Root Directory don't exist : " + rootDirectory.getPath());
				  throw new Exception("Root Directory don't exist : " + rootDirectory.getPath());
				}
			if (!rootDirectory.isDirectory()) {
				  out.println("Root Directory is not a directory : " + rootDirectory.getPath());
				  throw new Exception("Root Directory is not a directory : " + rootDirectory.getPath());
				}
		}

		File file = (rootDirectory==null) ? new File(fileName) : new File(rootDirectory, fileName);
		if (!file.exists()) {
		  out.println("File don't exist : " + fileName);
		  throw new Exception("File don't exist : " + fileName);
		}

		if (file.isDirectory()) {
		  out.println("The specified path is a directory");
		  throw new Exception("The specified path is a directory");
		}
		
		if (UtilString.isNotEmpty(maxSize)) {
			long lLength = file.length();
			long lMaxSize = Long.parseLong(maxSize);
			if (lLength>lMaxSize) {
				  out.println("The size file ("+lLength+") is over " + lMaxSize);
				  throw new Exception("The size file ("+lLength+") is over " + lMaxSize);
			}
		}

		if ((name==null)||(name.equals("")))
		  name = file.getName();
        return new ImageIcon(file.getCanonicalPath()).getImage();
  }
  
  protected File getRootDirectory(HttpServletRequest request, HttpServletResponse response) {
	  return null;
  }

  private float parseScale(String value) {
	float ret = 0f;
	if ((value!=null)&&(!"".equals(value))) {
	  if (value.endsWith("%"))
		value = value.substring(0, value.length()-1);
	  ret = Float.parseFloat(value)/100;
	}
	return ret;
  }

  private int parseWidth(Image sourceImage, String value) {
	return parseInt(sourceImage, value, sourceImage.getWidth(null));
  }

  private int parseHeight(Image sourceImage, String value) {
	return parseInt(sourceImage, value, sourceImage.getHeight(null));
  }

  private int parseInt(Image sourceImage, String value, int defaultInt) {
	int ret = defaultInt;
	if ((value!=null)&&(!"".equals(value))) {
	  if (value.endsWith("%"))
		ret *= Integer.parseInt(value.substring(0, value.length()-1))/100;
	  else
		ret = Integer.parseInt(value);
	}
	return ret;
  }

  protected Image scaleImage(HttpServletRequest request, HttpServletResponse response, Image sourceImage, int width, int height) {
	ImageFilter filter = new ReplicateScaleFilter(width,height);
	ImageProducer producer = new FilteredImageSource(sourceImage.getSource(),filter);
	return Toolkit.getDefaultToolkit().createImage(producer);
  }

  protected BufferedImage scaleImageBuffer(HttpServletRequest request, HttpServletResponse response, Image sourceImage, int width, int height) {
	return this.toBufferedImage(scaleImage(request, response, sourceImage, width, height));
  }

  private BufferedImage toBufferedImage(Image image) {
	image = new ImageIcon(image).getImage();
	int width = image.getWidth(null);
	int height = image.getHeight(null);
	BufferedImage bufferedImage = null;
	if (width>0 && height>0) {
		bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.createGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0,image.getWidth(null),image.getHeight(null));
		g.drawImage(image,0,0,null);
		g.dispose();
	}
	return bufferedImage;
  }

  // Send the contents of the file to the output stream
  private static void returnFile(File file, OutputStream out) throws FileNotFoundException, IOException {
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
