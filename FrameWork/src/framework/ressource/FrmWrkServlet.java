package framework.ressource;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import framework.ressource.bean.BeanData;
import framework.ressource.bean.BeanForward;
import framework.ressource.bean.BeanForwardError;
import framework.ressource.bean.BeanParameter;
import framework.ressource.bean.BeanQuery;
import framework.ressource.bean.BeanServlet;
import framework.ressource.util.UtilString;
import framework.trace.Trace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class FrmWrkServlet {

  private static boolean init = false;

  protected static Hashtable tableServlet = null;
  protected static Hashtable tableBean = null;
  protected static Hashtable tableQuery = null;
  protected static Hashtable tableForward = null;
  protected static Hashtable tableForwardError = null;

  // Servlet action
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_SERVLET = "SERVLET";
  // Initialisation de la ou des redirections de la servlet
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_FORWARD = "FORWARD";
  // Initialisation de la ou des redirections de la servlet en cas d'erreur
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_FORWARDERROR = "FORWARDERROR";
  // Bean qui va être initialisamp;eacute;
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_BEAN = "BEAN";
  // Requête SQL qui va être executamp;eacute; pour initialisamp;eacute; le bean
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_BEAN_QUERY = "QUERY";
  // Paramêtre à recuperer en "session" ou "request" pour être passamp;eacute; à la requête SQL
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_BEAN_PARAMETER = "PARAM";
/* Non utilise : Voir si il est possible de rendre dynamique l'appel aux methodes de service metier
  // Initialisation de definition de methode
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE = "METHODE";
  // Argument de la methode
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE_ARGUMENT = "ARGUMENT";
  // Retour de la methode
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE_RETURN = "RETURN";

  // Identifiant logic de la methode, utilisamp;eacute; pour identifier la methode dans le reste du fichier
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE_ID = "Id";
  // Nom de la methode
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE_NAME = "Name";
  // Class d'un argument de la methode
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE_ARGUMENT_CLASS = "Class";
  // Class de retour de la methode
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_METHODE_RETURN_CLASS = "Class";
*/

  // Nom de la servlet
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_NAME = "Name";
  // Class de la servlet a instancier (ex: framework.action.ActionDatabase)
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_CLASS = "Class";
  // Page de destination après execution normale de la servlet
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_TARGET = "Target";
  // Page de destination en cas d'erreur lors de l'execution de la servlet
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_TARGET_ERROR = "TargetError";
  // Indique si la l'accès à l'action requier une authentification (Value: "true" "false") [optional : by default "true"]
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_AUTHENTIFICATION = "Authentification";

  // Nom du bean. Utilisamp;eacute; comme nom du bean en session ou en requête
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_NAME = "Name";
  // Class du bean a instancier (ex: framework.beandata.BeanFindList)
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_CLASS = "Class";
  // Nom du service qui va être executer après la cramp;eacute;ation du bean [optional]
  // (ex: videofuture.home.list.service.SrvHomeList)
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_SERVICE = "Service";
  // Portamp;eacute; du bean. (Valeur possible: "session", "request") [optional : par defaut "request"]
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_SCOPE = "Scope";

  // Nom du paramêtre SQL. Utilisamp;eacute; pour recuperer la valeur soit en "session"
  // soit en "request" soit à partir d'un bean
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_NAME = "Name";
  // Nom du bean où doit être ramp;eacute;cuperer la valeur du paramêtre [optional]
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_BEAN = "Bean";
  // Index du parametre dans la requête SQL [optional]
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_INDEX = "Index";
  // Type de donnamp;eacute;e du paramêtre (Valeur possible: "STRING", "INT", "FLAG", "CURSOR")
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_TYPE = "Type";
  // Type de paramêtre d'entrêe ou de sortie [optional]
  // (Valeur possbile: "IN", "OUT") par defaut "IN"
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_INOUT = "InOut";
  // Class de sortie du paramêtre dans le cas d'un paramêtre "IN" [optional]
  // (ex: framework.convoyeur.CvrList)
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_CLASS = "Class";
  // Valeur par damp;eacute;faut du paramêtre si inexistant [optional] par defaut "null"
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_DEFAULT = "Default";
  // Indique si le parametre est synchronise avec une valeur la requette [optional] par defaut "TRUE"
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_SYNCHRONIZE = "Synchronize";

  // Nom de la requête
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_QUERY_NAME = "Name";
  // Nombre de paramêtre en entramp;eacute;e dans la requête [optional] par defaut "0"
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_QUERY_PARAMETERCOUNT = "ParameterCount";
  // Type de requête SQL à executer (Valeur possible: "REQUEST", "STORED_PROCEDURE")
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_QUERY_TYPE = "Type";

  // Nom de la target
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARD_NAME = "Name";
  // Condition de redirection
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARD_CONDITION = "Condition";
  // Url de redirection
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARD_TARGET = "Target";

  // Nom de la target error
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_NAME = "Name";
  // Condition de redirection
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_CONDITION = "Condition";
  // Url de redirection
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_TARGET = "Target";
  // Url de redirection
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_REPLACENULLBY = "ReplaceNullBy";

  public FrmWrkServlet() {
  }

  public static BeanServlet get(String key) {
    return (tableServlet!=null) ? (BeanServlet)tableServlet.get(key) : null;
  }

  public static void setup() {
    init = false;
    setup(Constante.XML_FRMWRK_SERVLET);
  }

  public static void setup(String fileNameXmlServlet) {
    Constante.XML_FRMWRK_SERVLET = fileNameXmlServlet;
    if (UtilString.isNotEmpty(fileNameXmlServlet) && !init) {
      init = true;
      Trace.OUT("FRAMEWORK SERVLET SETUP FILE:" + Constante.XML_FRMWRK_SERVLET + " - START");
      try {
        // Cramp;eacute;ation des outils de Parse du fichier XML
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        // Lecture et Parse du Fichier XMLS
        Document document = docBuilder.parse(new File(Constante.XML_FRMWRK_SERVLET));
        // Ramp;eacute;cupamp;eacute;ration du 1ère Element du Fichier XML
        Element root = document.getDocumentElement();
        root.normalize();
        Hashtable aTableServlet = new Hashtable();
        Hashtable aTableBean = new Hashtable();
        Hashtable aTableQuery = new Hashtable();
        Hashtable aTableForward = new Hashtable();
        Hashtable aTableForwardError = new Hashtable();

        /**
         *  Deplacement vers le premier amp;eacute;lamp;eacute;ment QUERY
         */
        NodeList listChild = root.getChildNodes();
        if ( (listChild != null) && (listChild.item(0) != null)) {
          BeanQuery beanQuery = null;
          for (int i = 0; i < listChild.getLength(); i++) {
            Node item = listChild.item(i);
            // Ramp;eacute;cuperation de chaques element de la liste
            if ( (item != null) && (item.getNodeType() == Element.ELEMENT_NODE) &&
                (CST_FILE_XML_INITIALISE_ELEMENT_BEAN_QUERY.equals(item.getNodeName()))) {
              beanQuery = getQuery(item);
              aTableQuery.put(beanQuery.getName(), beanQuery);
            }
          }

          /**
           *  Deplacement vers le premier amp;eacute;lamp;eacute;ment FORWARD
           */
          BeanForward beanForward = null;
          for (int i = 0; i < listChild.getLength(); i++) {
            Node item = listChild.item(i);
            // Ramp;eacute;cuperation de chaques element de la liste
            if ( (item != null) && (item.getNodeType() == Element.ELEMENT_NODE) &&
                (CST_FILE_XML_INITIALISE_ELEMENT_FORWARD.equals(item.getNodeName()))) {
              beanForward = getForward(item);
              aTableForward.put(beanForward.getName(), beanForward);
            }
          }

          /**
           *  Deplacement vers le premier amp;eacute;lamp;eacute;ment FORWARD ERROR
           */
          BeanForwardError beanForwardError = null;
          for (int i = 0; i < listChild.getLength(); i++) {
            Node item = listChild.item(i);
            // Ramp;eacute;cuperation de chaques element de la liste
            if ( (item != null) && (item.getNodeType() == Element.ELEMENT_NODE) &&
                (CST_FILE_XML_INITIALISE_ELEMENT_FORWARDERROR.equals(item.
                getNodeName()))) {
              beanForwardError = getForwardError(item);
              aTableForwardError.put(beanForwardError.getName(), beanForwardError);
            }
          }

          /**
           *  Deplacement vers le premier amp;eacute;lamp;eacute;ment BEAN
           */
          BeanData beanData = null;
          for (int i = 0; i < listChild.getLength(); i++) {
            Node item = listChild.item(i);
            // Ramp;eacute;cuperation de chaques element de la liste
            if ( (item != null) && (item.getNodeType() == Element.ELEMENT_NODE) &&
                (CST_FILE_XML_INITIALISE_ELEMENT_BEAN.equals(item.getNodeName()))) {
              beanData = getBean(item, aTableQuery);
              aTableBean.put(beanData.getName(), beanData);
            }
          }
        }

        /**
         *  Deplacement vers le premier element SERVLET
         */
        NodeList app = root.getElementsByTagName(CST_FILE_XML_INITIALISE_ELEMENT_SERVLET);
        if ( (app != null) && (app.item(0) != null)) { // ramp;eacute;cupère la liste des Enfants de l'amp;eacute;lamp;eacute;ment SERVLET
          BeanServlet bean = null;
          for (int i = 0; i < app.getLength(); i++) {
            Node item = app.item(i);
            // Ramp;eacute;cuperation de chaques element de la liste
            if ( (item != null) && (item.getNodeType() == Element.ELEMENT_NODE)) {
              bean = new BeanServlet();
              bean.setName(Constante.getNodeAttribute(item, CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_NAME));
              bean.setServlet(Constante.getNodeAttribute(item, CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_CLASS));
              bean.setTarget(Constante.getNodeAttribute(item, CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_TARGET));
              bean.setTargetError(Constante.getNodeAttribute(item,
                  CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_TARGET_ERROR));
              String authentification = Constante.getNodeAttribute(item,
                  CST_FILE_XML_INITIALISE_ATTRIBUTE_SERVLET_AUTHENTIFICATION);
              bean.setAuthentification( (authentification == null) ? Boolean.TRUE.toString() : authentification);
              NodeList childList = item.getChildNodes();
              for (int j = 0; j < childList.getLength(); j++) { // Ramp;eacute;cupere la liste des beans de la servlet
                Node child = childList.item(j);
                if (child.getNodeType() == Element.ELEMENT_NODE) { // Ne s'occupe que des Types ELEMENT
                  String szItemName = child.getNodeName().toUpperCase();
                  if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_BEAN)) { // Ne s'occupe que des amp;eacute;lamp;eacute;ments BEAN
                    BeanData beanData = getBean(child, aTableQuery);
                    if (UtilString.isNotEmpty(beanData.getName()) && (aTableBean.get(beanData.getName()) != null))

                      // Si la class du bean n'est pas renseignamp;eacute; mais que le nom
                      // l'est on considère que le bean fait partie d'une liste
                      // de bean gamp;eacute;neraux
                      beanData = (BeanData) aTableBean.get(beanData.getName());
                      // Ajout le bean à la servlet
                    bean.addBean(beanData);
                  } //if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_BEAN))
                  else if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_FORWARD)) {
                    // Ne s'occupe que des amp;eacute;lamp;eacute;ments FORWARD
                    BeanForward beanForward = getForward(child);
                    if (UtilString.isNotEmpty(beanForward.getName()) && UtilString.isEmpty(beanForward.getTarget()))

                      // Si la destination du Forward n'est pas renseignamp;eacute; mais que
                      // le nom l'est on considère que le Forward fait partie
                      // d'une liste de Forward gamp;eacute;neraux
                      beanForward = (BeanForward) aTableForward.get(beanForward.getName());
                      // Ajout le bean à la servlet
                    bean.addForward(beanForward);
                  } //if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_FORWARD))
                  else if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_FORWARDERROR)) {
                    // Ne s'occupe que des amp;eacute;lamp;eacute;ments FORWARD ERROR
                    BeanForwardError beanForwardError = getForwardError(child);
                    if (UtilString.isNotEmpty(beanForwardError.getName()) &&
                        UtilString.isEmpty(beanForwardError.getTarget()))

                      // Si la destination du Forward n'est pas renseignamp;eacute; mais
                      // que le nom l'est on considère que le Forward d'erreur
                      // fait partie d'une liste de Forward d'erreur gamp;eacute;neraux
                      beanForwardError = (BeanForwardError) aTableForwardError.get(beanForwardError.getName());
                      // Ajout le bean à la servlet
                    bean.addForwardError(beanForwardError);
                  } //if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_FORWARDERROR))
                  else
                    Trace.DEBUG("FRAMEWORK SERVLET CONNECT SETUP ELEMENT [" + i + "] NAME:" + szItemName + " NO SETUP");
                } //if (child.getNodeType() == Element.ELEMENT_NODE)
              } //for (int i=0;i<app.getLength();i++)
              aTableServlet.put(bean.getName(), bean);
            } //if ((item!=null)&&(item.getNodeType() == Element.ELEMENT_NODE))
          } //for (int i=0;i<app.getLength();i++)
          // Initialise les tables
          tableServlet = aTableServlet;
          tableBean = aTableBean;
          tableQuery = aTableQuery;
          tableForward = aTableForward;
          tableForwardError = aTableForwardError;

        } //if ( (app!=null)&&(app.item(0)!=null) )
        else {
          Trace.ERROR("FRAMEWORK SERVLET CONNECT SETUP NO ELEMENT");
          Trace.ERROR("FRAMEWORK SERVLET CONNECT SETUP USING CONNECT VALUES");
        }

      }
      catch (ParserConfigurationException ex) {
        Trace.ERROR(ex.getMessage());
      }
      catch (SAXException ex) {
        Trace.ERROR(ex.getMessage());
      }
      catch (IOException ex) {
        Trace.ERROR(ex.getMessage());
      }
      finally {
        Trace.OUT("FRAMEWORK SERVLET SETUP FILE:" + Constante.XML_FRMWRK_SERVLET + " - END");
      }
    }
  }

  protected static BeanData getBean(Node child) {
    return getBean(child, tableQuery);
  }

  protected static BeanData getBean(Node child, Hashtable table) {
    BeanData ret = new BeanData();
    ret.setName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_NAME));
    ret.setClassName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_CLASS));
    ret.setService(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_SERVICE));
    ret.setScope(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_SCOPE));
    if ( child.hasChildNodes() )
    { // Initialise la liste des paramètres de la requête
      NodeList paramList = child.getChildNodes();
      for (int k = 0; k < paramList.getLength(); k++)
      { // Ramp;eacute;cupere la liste des parametres du bean
        Node param = paramList.item(k);
        if (param != null) {
          if (param.getNodeType() == Element.ELEMENT_NODE)
          { // Ne s'occupe que des Types ELEMENT
            String szParamName = param.getNodeName().toUpperCase();
            if (szParamName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_BEAN_PARAMETER))
            { // Ne s'occupe que des amp;eacute;lamp;eacute;ments PARAMETER
              BeanParameter beanParam = getParameter(param);
              if ( UtilString.isNotEmpty(beanParam.getName()) ) {
                String szParameterIndex = Constante.getNodeAttribute(param, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_INDEX);
                if ( (szParameterIndex != null) && (!"".equals(szParameterIndex))) {
                  beanParam.setIndex(new Integer(szParameterIndex));
                  ret.setParameterAt(beanParam, beanParam.getIndex().intValue());
                }//if ( (szParameterIndex != null) && (!"".equals(szParameterIndex)))
                else
                  ret.addParameter(beanParam);
              }//if ( (beanParam.getName()!=null) && (!"".equals(beanParam.getName())) )
            }//if (szParamName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_BEAN_PARAMETER))
            else if (szParamName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_BEAN_QUERY))
            { // Ne s'occupe que des amp;eacute;lamp;eacute;ments QUERY
              BeanQuery query = getQuery(param);
              if (UtilString.isNotEmpty(query.getName()))
                // Si le nom de la query n'est pas renseignamp;eacute; mais on considère
                // que la query fait partie d'une liste de query gamp;eacute;nerales
                query = (BeanQuery)table.get(query.getName());
              ret.setBeanQuery(query);
            }//else if (szParamName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_BEAN_QUERY))
          }//if (param.getNodeType() == Element.ELEMENT_NODE)
        }//if (param != null)
      }//for (int k = 0; k < paramList.getLength(); k++)
    }//if ( child.hasChildNodes() )
    return ret;
  }
  protected static BeanParameter getParameter(Node child) {
    BeanParameter ret = new BeanParameter();
    ret.setName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_NAME));
    ret.setBean(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_BEAN));
    ret.setInOutPut(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_INOUT));
    ret.setType(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_TYPE));
    ret.setClassName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_CLASS));
    ret.setDefaultData(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_DEFAULT));
    ret.setSynchronize(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_PARAMETER_SYNCHRONIZE));
    return ret;
  }
  protected static BeanQuery getQuery(Node child) {
    BeanQuery ret = new BeanQuery();
    ret.setName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_QUERY_NAME));
    ret.setQuery(Constante.getNodeValue(child));
    ret.setParameterCount(Constante.getNodeAttributeInteger(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_QUERY_PARAMETERCOUNT));
    ret.setType(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_BEAN_QUERY_TYPE));
    return ret;
  }
  protected static BeanForward getForward(Node child) {
    BeanForward ret = new BeanForward();
    ret.setName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARD_NAME));
    ret.setCondition(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARD_CONDITION));
    ret.setTarget(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARD_TARGET));
    ret.setReplaceNullBy(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_REPLACENULLBY));
    return ret;
  }
  protected static BeanForwardError getForwardError(Node child) {
    BeanForwardError ret = new BeanForwardError();
    ret.setName(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_NAME));
    ret.setCondition(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_CONDITION));
    ret.setTarget(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_TARGET));
    ret.setReplaceNullBy(Constante.getNodeAttribute(child, CST_FILE_XML_INITIALISE_ATTRIBUTE_FORWARDERROR_REPLACENULLBY));
    return ret;
  }
  
  public static BeanData getBean(String name) {
	  return (BeanData)tableBean.get(name);
  }
}
