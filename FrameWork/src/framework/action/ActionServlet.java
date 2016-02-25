package framework.action;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.ressource.Constante;
import framework.ressource.FrmWrkServlet;
import framework.ressource.bean.BeanForward;
import framework.ressource.bean.BeanServlet;
import framework.ressource.util.UtilEvalJava;
import framework.ressource.util.UtilRequest;
import framework.ressource.util.UtilString;
import framework.ressource.util.UtilVector;
import framework.trace.Trace;
import javax.servlet.ServletConfig;
import framework.ressource.FrmWrkConfig;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ActionServlet extends HttpServlet {

  private final static String CONTENT_TYPE = "text/html";
  private final static String CONFIG_FILE = "config_file";
  private final static String SERVLET_FILE = "servlet_file";
  public static String SECURITY_XML = "security_xml";
  public static String SECURITY_XSL = "security_xsl";
  public static String WORKSPACE_SECURITY_XML = "FrameWork_Security.xml";
  public static String WORKSPACE_SECURITY_XSL = "FrameWork_Security.xsl";

  //Initialize global variables
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    String szConfigFile = getInitParameter(CONFIG_FILE);
    if (UtilString.isNotEmpty(szConfigFile))
      try {FrmWrkConfig.setup(getServletContext().getRealPath(szConfigFile));}catch (Exception ex){}
    String szServletFile = getInitParameter(SERVLET_FILE);
    if (UtilString.isNotEmpty(szServletFile))
      try {FrmWrkServlet.setup(getServletContext().getRealPath(szServletFile));}catch (Exception ex){}
    String szSecurityXml = getInitParameter(SECURITY_XML);
    if (UtilString.isNotEmpty(szSecurityXml))
      try {WORKSPACE_SECURITY_XML = getServletContext().getRealPath(szSecurityXml);}catch (Exception ex){}
    String szSecurityXsl = getInitParameter(SECURITY_XSL);
    if (UtilString.isNotEmpty(szSecurityXsl))
      try {WORKSPACE_SECURITY_XSL = getServletContext().getRealPath(szSecurityXsl);}catch (Exception ex){}
  }
  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String szEvent = request.getParameter(Constante.CST_REQUEST_PARAM_EVENT);
    String redirect = null;
    if ( szEvent!=null )
    { // Lecture du nom de la servlet
      BeanServlet bean = FrmWrkServlet.get(szEvent);
      if ( bean!=null ) {
        String servletClass = ((bean.getServlet()!=null)&&(!bean.getServlet().equals(""))) ? bean.getServlet() : "framework.action.ActionGenerique";
        Trace.DEBUG(this, Constante.CST_REQUEST_PARAM_EVENT+": '"+szEvent+"' servlet: '"+servletClass+"'");
        boolean ok = false;
        // Recuperation des informations de la servlet
        try {
          // Récupération du ClassLoader actuel à la place du Class.forName
          // pour une meilleur gestion de la mémoire
          ClassLoader classloader = framework.ressource.util.UtilReflect.getContextClassLoader();//this.getClass().getClassLoader();
          // Récupération de la servlet
          Class classe = classloader.loadClass(servletClass);
          // Initilialisation des paramètres à passer à la methode de la servlet
          Class param1 = javax.servlet.ServletRequest.class;
          Class param2 = javax.servlet.ServletResponse.class;
          // Récupération de la methode service de la servlet
          Method method = classe.getMethod("service", new Class[] {param1, param2});
          if (method!=null) {
            // Invocation de la methode run de la servlet
            method.invoke(classe.newInstance(), new Object[] {request, response});
            // Redirection
            redirect = bean.getForwardTarget(request);
            ok = true;
          }
          else
            Trace.ERROR(this, "No method 'service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)' found in class: '" + classe.getName() + "'");
        }
        catch (Throwable t) {
        	Trace.ERROR(this, t);
            throw new ServletException(t);
        }
        finally {
          if (!response.isCommitted()) {
            // Redirection finale
            if (!ok)
              redirect = bean.getForwardTargetError(request);
            if (UtilString.isNotEmpty(redirect)) {
              RequestDispatcher rd = request.getRequestDispatcher(redirect);
              if (rd!=null) {
                  Trace.DEBUG(this, "Redirect to : '" + redirect + "'");
            	  rd.forward(request, response);
              }
              else {
            	  Trace.DEBUG(this, "Redirect : '" + redirect + "' not found");
              }
            }
            else
              Trace.DEBUG(this, "No redirection");
          }
          else
            // Redirection impossible requete déja commité
            Trace.DEBUG(this, "Don't redirect, target all ready commited: '" + redirect + "'");
        }
      }
      else
        Trace.ERROR(this, Constante.CST_REQUEST_PARAM_EVENT+": '"+szEvent+"' not found");
    }
  }

  protected void showRequest(HttpServletRequest request)
  {
    StringBuffer sbAttr = new StringBuffer("REQUEST ATTRIBUTES:");
    Enumeration enumAttr = request.getAttributeNames();
    while (enumAttr.hasMoreElements())
    {
      sbAttr.append(enumAttr.nextElement().toString());
      sbAttr.append(" ");
    }
    Trace.OUT(this, sbAttr.toString());

    StringBuffer sbParam = new StringBuffer("REQUEST PARAMETRES:");
    Enumeration enumParam = request.getParameterNames();
    while (enumParam.hasMoreElements())
    {
      sbParam.append(enumParam.nextElement().toString());
      sbParam.append(" ");
    }
    Trace.OUT(this, sbParam.toString());

    HttpSession session = request.getSession();
    StringBuffer sbSessionAttr = new StringBuffer("SESSION ATTRIBUTES:");
    Enumeration enumSessionAttr = session.getAttributeNames();
    while (enumSessionAttr.hasMoreElements())
    {
      sbSessionAttr.append(enumSessionAttr.nextElement().toString());
      sbSessionAttr.append(" ");
    }
    Trace.OUT(this, sbSessionAttr.toString());

  }
  //Clean up resources
  public void destroy() {
  }
}
