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
import framework.trace.Trace;

import java.util.Calendar;

public class ActionMouseEvent extends HttpServlet {
    

      //Initialize global variables
      public void init() throws ServletException {
      }
    
      //Process the HTTP Get request
      public synchronized void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String x = request.getParameter("x");
            String y = request.getParameter("y");
            String screenWidth = request.getParameter("screenWidth");
            String screenHeight = request.getParameter("screenHeight");
            String event = request.getParameter("event");
            String button = request.getParameter("button");
            String nbClick = request.getParameter("nbClick");
            String debug = request.getParameter("debug");
            Robot robot = new Robot();

            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            Rectangle screenRect2 = new Rectangle(screenSize);

            int iNbClick = 1;
            int iButton = InputEvent.BUTTON1_MASK;

            if (UtilString.isNotEmpty(x) &&
                UtilString.isNotEmpty(y)) {
                try {
                    int iX = Integer.parseInt(x);
                    int iY = Integer.parseInt(y);
                    if (UtilString.isNotEmpty(debug))
                    	System.out.println("x:" + x +" y:" + y);
                    if (UtilString.isNotEmpty(screenWidth) &&
                        UtilString.isNotEmpty(screenHeight)) {
                            //System.out.println("screenWidth:" + screenWidth +" screenHeight:" + screenHeight);
                            /**
                             * Ramp;eacute;ajustement des coordonnamp;eacute;es X et Y par rapport 
                             * au format d'affichage demandamp;eacute; et ramp;eacute;el du bureau
                             */
                            int iScreenWidth = Integer.parseInt(screenWidth);
                            int iScreenHeight = Integer.parseInt(screenHeight);
                            try {
                                iX = (int)(iX/((float)((float)iScreenWidth/(float)screenRect2.width)));
                                iY = (int)(iY/((float)((float)iScreenHeight/(float)screenRect2.height)));
                            }
                            catch (Exception ex) {}
                        }
                    robot.mouseMove(iX, iY);
                }
                catch (Exception ex) {
                }
            }

            if (UtilString.isNotEmpty(event)) {

                //Nombre de click
                if (UtilString.isNotEmpty(nbClick)) {
                    try {
                    iNbClick = Integer.parseInt(nbClick);
                    } catch (Exception ex) {iNbClick=1;}
                }

                //Bouton
                if (UtilString.isNotEmpty(button)) {
                    try {
                        iButton = Integer.parseInt(button);
                        if (iButton==2)
                            iButton = InputEvent.BUTTON2_MASK;
                        else if (iButton==3)
                            iButton = InputEvent.BUTTON3_MASK;
                        else
                           iButton = InputEvent.BUTTON1_MASK;
                    } catch (Exception ex) {iButton = InputEvent.BUTTON1_MASK;}
                }

                for (int i=0 ; i<iNbClick ; i++) {
                    if (UtilString.isNotEmpty(debug))
                    	System.out.println("event:" + event +" button:" + button +" [" + Integer.toString(i) + "]");
                    if (UtilString.isBeginByIgnoreCase(event, "PRESS")) {
                        robot.mousePress(iButton);
                    }
                    else if (UtilString.isBeginByIgnoreCase(event, "RELEASE")) {
                        robot.mouseRelease(iButton);
                    }
                    else if (UtilString.isBeginByIgnoreCase(event, "CLICK")) {
                        robot.mousePress(iButton);
                        robot.mouseRelease(iButton);
                    }
                }
            }
        } catch (AWTException e) {
            // TODO Auto-generated catch block
            Trace.ERROR(this, e);
        }
      }

      //Clean up resources
      public void destroy() {
      }
}
