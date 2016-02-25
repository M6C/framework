package framework.action;

//http://www.developpez.net/forums/showthread.php?t=20217
//http://java.developpez.com/faq/java/?page=graphique_general_images#GRAPHIQUE_IMAGE_capture_ecran

import java.awt.*;

import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.*;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGEncodeParam;

import framework.ressource.util.UtilString;
import framework.ressource.util.image.Quantize;

import java.util.Calendar;

public class ActionScreenShoot extends ActionImageReader {
    
    protected Image getImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Image ret = null;
        String x = request.getParameter("x");
        String y = request.getParameter("y");
        String width = request.getParameter("width");
        String height = request.getParameter("height");
        String screenWidth = request.getParameter("screenWidth");
        String screenHeight = request.getParameter("screenHeight");

        Rectangle screenRect = null;

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect2 = new Rectangle(screenSize);

        if (UtilString.isNotEmpty(x) &&
            UtilString.isNotEmpty(y)) {
	        try {
	        	int iX = Integer.parseInt(x);
	        	int iY = Integer.parseInt(y);
	        	if (UtilString.isNotEmpty(screenWidth) &&
                    UtilString.isNotEmpty(screenHeight)) {
	        		/**
	        		 * Réajustement des coordonnées X et Y par rapport 
	        		 * au format d'affichage demandé et réel du bureau
	        		 */
                	int iScreenWidth = Integer.parseInt(screenWidth);
                	int iScreenHeight = Integer.parseInt(screenHeight);
                    try {
        	            if (UtilString.isNotEmpty(width) &&
    		                UtilString.isNotEmpty(height)) {
    			        	int iWidth = Integer.parseInt(width);
    			        	int iHeight = Integer.parseInt(height);
	                        iX -= (iWidth/2);//(int)(iWidth*((float)((float)iScreenWidth/(float)screenRect2.width))/2);
	                        iY -= (iHeight/2);//(int)(iHeight*((float)((float)iScreenHeight/(float)screenRect2.height))/2);
        	            }
                        iX = (int)(iX/((float)((float)iScreenWidth/(float)screenRect2.width)));
                        iY = (int)(iY/((float)((float)iScreenHeight/(float)screenRect2.height)));
                    }
                    catch (Exception ex) {
                    	ret = null;
                    }
	        	}
	            if (UtilString.isNotEmpty(width) &&
	                UtilString.isNotEmpty(height)) {
		        	int iWidth = Integer.parseInt(width);
		        	int iHeight = Integer.parseInt(height);
                    screenRect = new Rectangle(iX, iY, iWidth, iHeight);
	            } else {
	               screenRect = new Rectangle(new Point(iX, iY));
	            }
	        }
	        catch (Exception ex) {
	            screenRect = null;
	        }
	    }
        else {
            screenRect = screenRect2;
        }
        if (screenRect !=null) {
        	ret = new Robot().createScreenCapture(screenRect);
        }
        return ret;
    }

	protected Image scaleImage(HttpServletRequest request, HttpServletResponse response, Image sourceImage, int width, int height) {
		Image ret = null;
        String screenWidth = request.getParameter("screenWidth");
        String screenHeight = request.getParameter("screenHeight");
    	if (UtilString.isNotEmpty(screenWidth) &&
            UtilString.isNotEmpty(screenHeight)) {
    		/**
    		 * Réajustement des de l'image du bureau par rapport 
    		 * au format d'affichage demandé et réel du bureau
    		 */
        	int iScreenWidth = Integer.parseInt(screenWidth);
        	int iScreenHeight = Integer.parseInt(screenHeight);
            try {
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Dimension screenSize = toolkit.getScreenSize();
                Rectangle screenRect2 = new Rectangle(screenSize);

	        	int iWidth = (int)(width*((float)((float)iScreenWidth/(float)screenRect2.width)));
	        	int iHeight = (int)(height*((float)((float)iScreenHeight/(float)screenRect2.height)));
                ret = super.scaleImage(request, response, sourceImage, iWidth, iHeight);
            }
            catch (Exception ex) {
            	ret = null;
            }
        }
    	else {
        	ret = super.scaleImage(request, response, sourceImage, width, height);
    	}
		return ret;
	}
}