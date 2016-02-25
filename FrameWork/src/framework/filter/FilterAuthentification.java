/*
 * Cramp;eacute;amp;eacute; le 1 damp;eacute;c. 2004
 *
 * Pour changer le modèle de ce fichier gamp;eacute;namp;eacute;ramp;eacute;, allez à :
 * Fenêtre&gt;Pramp;eacute;famp;eacute;rences&gt;Java&gt;Gamp;eacute;namp;eacute;ration de code&gt;Code et commentaires
 */
package framework.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import framework.beandata.BeanGenerique;
import framework.ressource.bean.BeanServlet;
import framework.ressource.FrmWrkServlet;
import framework.action.ActionServlet;
import framework.ressource.util.UtilString;
import framework.ressource.FrmWrkConfig;
import framework.trace.Trace;
import java.net.MalformedURLException;

/**
 * @author rocada
 *
 * Pour changer le modèle de ce commentaire de type gamp;eacute;namp;eacute;ramp;eacute;, allez à :
 * Fenêtre&gt;Pramp;eacute;famp;eacute;rences&gt;Java&gt;Gamp;eacute;namp;eacute;ration de code&gt;Code et commentaires
 */
public class FilterAuthentification implements Filter {

  private FilterConfig filterConfig = null;

  private final static String URL_LOGOUT = "?event=Index";
  private final static String CONFIG_FILE = "config_file";
  private final static String SERVLET_FILE = "servlet_file";

  /* (non-Javadoc)
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  public void init(FilterConfig arg0) throws ServletException {
    filterConfig = arg0;

    String szConfigFile = filterConfig.getInitParameter(CONFIG_FILE);
    if (UtilString.isNotEmpty(szConfigFile))
      try {FrmWrkConfig.setup(filterConfig.getServletContext().getRealPath(szConfigFile));}catch (Exception ex){ex.printStackTrace();}
    String szServletFile = filterConfig.getInitParameter(SERVLET_FILE);
    if (UtilString.isNotEmpty(szServletFile))
      try {FrmWrkServlet.setup(filterConfig.getServletContext().getRealPath(szServletFile));}catch (Exception ex){ex.printStackTrace();}
  }

  /* (non-Javadoc)
   * @see javax.servlet.Filter#destroy()
   */
  public void destroy() {
  }

  /* (non-Javadoc)
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    BeanServlet bean = null;
    HttpServletRequest req = (HttpServletRequest) request;
    String event = request.getParameter("event");
    /****** Check event ******/
    if ( (event != null) && (!event.equals(""))) {
      bean = FrmWrkServlet.get(event);
      if (bean != null) {
        // Lecture du nom du paramètre qui indique si l'evenement à besoin d'une authentification
        String szOutputName = filterConfig.getInitParameter("OutputName");
        if (UtilString.isNotEmpty(szOutputName)) {
          // Récupere la valeur de l'authentification ou "true" par défaut
          String szOutputValue = (UtilString.isNotEmpty(bean.getAuthentification())) ? bean.getAuthentification() : Boolean.TRUE.toString();
          // Initialise le parametre d'authentification
          filterConfig.getServletContext().setAttribute(szOutputName, szOutputValue);
        }
      }
      /****** Continue Filter chain ******/
      chain.doFilter(request, response);
    }
  }
}
