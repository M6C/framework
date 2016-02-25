package framework.action;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.beandata.BeanGenerique;
import framework.ressource.Constante;
import framework.ressource.FrmWrkMsg;
import framework.ressource.FrmWrkServlet;
import framework.ressource.bean.BeanData;
import framework.ressource.bean.BeanServlet;
import framework.service.SrvGenerique;
import framework.trace.Trace;
import framework.ressource.util.UtilAction;
import framework.ressource.util.UtilString;
import javax.servlet.ServletConfig;
import framework.ressource.FrmWrkConfig;
import java.net.*;

/**
 * 
Title: 


 * 
Description: 


 * 
Copyright: Copyright (c) 2002


 * 
Company: 


 * @author unascribed
 * @version 1.0
 */

public class ActionGenerique extends HttpServlet {

  private final static String SERVLET_FILE = "servlet_file";
  private final static String CONFIG_FILE = "config_file";

  //Initialize global variables
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    String szConfigFile = getInitParameter(CONFIG_FILE);
    if (UtilString.isNotEmpty(szConfigFile))
      try {FrmWrkConfig.setup(getServletContext().getRealPath(szConfigFile));}catch (Exception ex){}
    String szServletFile = getInitParameter(SERVLET_FILE);
    if (UtilString.isNotEmpty(szServletFile))
      try {FrmWrkServlet.setup(getServletContext().getRealPath(szServletFile));}catch (Exception ex){}
  }

  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession();
    // Lecture en requette du nom de l'ev?nement
    String szEvent = request.getParameter(Constante.CST_REQUEST_PARAM_EVENT);
    if ( szEvent!=null ) {
      // Lecture du Bean de la servlet
      BeanServlet beanServlet = FrmWrkServlet.get(szEvent);
      for( int i=0 ; i<beanServlet.getListBean().size() ; i++ ) {
        try {
          // Boucle sur tous les bean a instancier
          BeanData beanData = (BeanData) beanServlet.getListBean().elementAt(i);
          // Scope du bean
          String szScope = beanData.getScope();
          BeanGenerique bean = null;
          if (BeanData.CST_SCOPE_SESSION.equalsIgnoreCase(szScope))
            // Recherche du Bean de donn?e dans la session
            bean = (BeanGenerique) session.getAttribute(beanData.getName());
          else
            // Recherche du Bean de donn?e dans la request
            bean = (BeanGenerique) request.getAttribute(beanData.getName());

          if (bean == null) {
            // Initialise un nouveau Bean de donn?e
            bean = newBean(beanData);

            if (BeanData.CST_SCOPE_SESSION.equalsIgnoreCase(szScope)) {
              Trace.DEBUG(this, "store bean: '" + beanData.getName() + "' class: " + bean.getClass().getName() + " size: " + bean.getSize() + " in scope: " + BeanData.CST_SCOPE_SESSION);
              // Mets le Bean de donn?e en session
              session.setAttribute(beanData.getName(), bean);
            }
            else {
              Trace.DEBUG(this, "store bean: '" + beanData.getName() + "' class: " + bean.getClass().getName() + " size: " + bean.getSize() + " in scope: " + BeanData.CST_SCOPE_REQUEST);
              // attach bean object to request:
              request.setAttribute(beanData.getName(), bean);
            }
          }
          else
            bean.setBeanData(beanData);
          if (UtilString.isNotEmpty(bean.getService()))
            Trace.DEBUG(this, Constante.CST_REQUEST_PARAM_EVENT+": '"+szEvent+"' service: '"+bean.getService()+"'");

          // Execution du service ( Chargement du Bean )
          executeService(request, response, beanData, bean);
        }
        catch (Exception ex) {
        	Trace.ERROR(this, ex);
          Trace.ERROR(this, FrmWrkMsg.getMessage(1, 1));
          if (UtilString.isNotEmpty(beanServlet.getTargetError())) {
            // Redirige vers la page d'erreur
            request.getRequestDispatcher(beanServlet.getTargetError()).forward(request, response);
            //response.sendRedirect(beanServlet.getTargetError());
          }
        }
      }
    }
  }

  public BeanGenerique newBean(BeanData beanData) throws Exception {
BeanGenerique ret = null;
    // R?cup?ration du ClassLoader actuel ? la place du Class.forName
    // pour une meilleur gestion de la m?moire
    ClassLoader classloader = framework.ressource.util.UtilReflect.getContextClassLoader();//this.getClass().getClassLoader();
    try {
    	return UtilAction.newBean(beanData);
    }
    catch (ClassNotFoundException ex) {
      Trace.ERROR(this, ex.getMessage());
    }
    catch (IllegalAccessException ex) {
      Trace.ERROR(this, ex.getMessage());
    }
    catch (InstantiationException ex) {
      Trace.ERROR(this, ex.getMessage());
    }
    return ret;
  }

  public void executeService(HttpServletRequest request, HttpServletResponse response, BeanData beanData, BeanGenerique bean) throws Exception {
// Execution du service ( Chargement du Bean )
  UtilAction.executeService(request, response, beanData, bean);
}

  	public SrvGenerique newService(BeanData beanData) throws Exception {
SrvGenerique ret = null;
try {
return UtilAction.newService(beanData);
} catch (ClassNotFoundException ex) {
Trace.ERROR(this, ex.getMessage());
} catch (IllegalAccessException ex) {
Trace.ERROR(this, ex.getMessage());
} catch (InstantiationException ex) {
Trace.ERROR(this, ex.getMessage());
} finally {
// La class service n'est pas d?fini donc on cr?er un nouveau
// service
if (ret == null)
ret = newService();
}
return ret;
  	}

  public SrvGenerique newService() throws Exception {
return SrvGenerique.getSingleton();
  }

  public void showRequest(HttpServletRequest request) throws Exception {
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
