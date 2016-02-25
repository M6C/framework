package framework.ressource;

import framework.ressource.util.UtilPackage;
import framework.ressource.util.UtilString;
import framework.trace.Trace;
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

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  unascribed
 * @version  1.0
 */

public class FrmWrkConfig {

  private static boolean init = false;

  public static String CST_FILE_XML_INITIALISE = "FrmWrk_Config.xml";

  public static final String CST_FILE_XML_INITIALISE_ELEMENT_DATABASE_CONNECT = "DATABASE-CONNECT";
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_DRIVER = "DRIVER";
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_URL = "URL";
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_FILE = "FILE";
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_USER = "USER";
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_PASSWORD = "PASSWORD";

  public static final String CST_REQUEST_PARAM_EVENT = "event";

  public static final String CST_CLASS_PATH = UtilPackage.getPackageClassPath();

  /**
   * @todo Initialiser ces constantes par un fichier XML
   */
  // FrmWrkConfigs pour la base de donnée
  public static String DB_CONNECT_DRIVER = "com.borland.datastore.jdbc.DataStoreDriver";
  public static String DB_CONNECT_URL = "jdbc:borland:dslocal:";
  public static String DB_CONNECT_FILE = "D:\\Dev\\Travaux\\Java\\JBuilder8\\VideoFutureDB\\VideoFuture.jds";
  public static String DB_CONNECT_USER = "user";
  public static String DB_CONNECT_PASSWORD = "";

  // FrmWrkConfigs pour les fichiers XML
  public static String XML_FRMWRK_SERVLET = "FrmWrk_Servlet.xml";
  // FrmWrkConfigs pour les fichiers XML
  public static String XML_FRMWRK_MENU = "FrmWrk_Menu.xml";

  // FrmWrkConfigs JDK

  protected FrmWrkConfig() {
  }

  public static void setup() {
    init = false;
    setup(CST_FILE_XML_INITIALISE);
  }

  public static void setup(String fileNameXmlConfig) {
    CST_FILE_XML_INITIALISE = fileNameXmlConfig;
    if (UtilString.isNotEmpty(fileNameXmlConfig) && !init) {
      init = true;
      String szFileInit = null;
      try {
        File fileInit = new File(fileNameXmlConfig);
        szFileInit = fileInit.getCanonicalPath();
        Trace.DEBUG("FRAMEWORK SETUP FILE:" + szFileInit + " - START");
        // Création des outils de Parse du fichier XML
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        // Lecture et Parse du Fichier XMLS
        Document document = docBuilder.parse(fileInit);
        // Récupération du 1ère Element du Fichier XML
        Element root = document.getDocumentElement();
        root.normalize();
        /**
         *  Deplacement vers l'élément _DATABASE CONNECT
         */
        NodeList app = root.getElementsByTagName(CST_FILE_XML_INITIALISE_ELEMENT_DATABASE_CONNECT);
        if ( (app != null) && (app.item(0) != null)) { // récupère la liste des Enfants de l'élément CONNECT
          app = app.item(0).getChildNodes();
          for (int i = 0; i < app.getLength(); i++) {
            Node item = app.item(i);
            // Récuperation de chaques element de la liste
            if ( (item != null) && (item.getNodeType() == Element.ELEMENT_NODE)) { // Ne s'occupe que des Types ELEMENT
              String szItemName = item.getNodeName().toUpperCase();
              if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_DRIVER)) {
                DB_CONNECT_DRIVER = getNodeValue(item);
//                Trace.DEBUG("FRAMEWORK DATABASE CONNECT SETUP DB_CONNECT_DRIVER:" + DB_CONNECT_DRIVER);
              }
              else if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_URL)) {
                DB_CONNECT_URL = getNodeValue(item);
//                Trace.DEBUG("FRAMEWORK DATABASE CONNECT SETUP DB_CONNECT_URL:" + DB_CONNECT_URL);
              }
              else if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_FILE)) {
                DB_CONNECT_FILE = getNodeValue(item);
//                Trace.DEBUG("FRAMEWORK DATABASE CONNECT SETUP DB_CONNECT_FILE:" + DB_CONNECT_FILE);
              }
              else if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_USER)) {
                DB_CONNECT_USER = getNodeValue(item);
//                Trace.DEBUG("FRAMEWORK DATABASE CONNECT SETUP DB_CONNECT_USER:" + DB_CONNECT_USER);
              }
              else if (szItemName.equalsIgnoreCase(CST_FILE_XML_INITIALISE_ELEMENT_PASSWORD)) {
                DB_CONNECT_PASSWORD = getNodeValue(item);
//                Trace.DEBUG("FRAMEWORK DATABASE CONNECT SETUP DB_CONNECT_PASSWORD:" + DB_CONNECT_PASSWORD);
              }
              else
                Trace.DEBUG("FRAMEWORK DATABASE CONNECT SETUP ELEMENT [" + i + "] NAME:" + szItemName + " NO SETUP");
            } //if ( (item!=null)&&(item.getNodeType()==Element.ELEMENT_NODE) )
          } //for (int i=0;i<app.getLength();i++)
        } //if ( (app!=null)&&(app.item(0)!=null) )
        else {
          Trace.ERROR("FRAMEWORK DATABASE CONNECT SETUP NO ELEMENT");
          Trace.ERROR("FRAMEWORK DATABASE CONNECT SETUP USING DEFAULT VALUES");
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
        Trace.DEBUG("FRAMEWORK SETUP FILE:" + (szFileInit == null ? fileNameXmlConfig : szFileInit) + " - END");
      }
    }
  }

  private static void setupListElemnt(Element root, String parent, Hashtable list) {
    NodeList app = root.getElementsByTagName(parent);
    if ( (app!=null)&&(app.item(0)!=null) )
    { // récupère la liste des Enfants de l'élément DEFAULT-PATH
      app = app.item(0).getChildNodes();
      for (int i=0;i<app.getLength();i++) {
        Node item = app.item(i);
        // Récuperation de chaques element de la liste
        if ( (item!=null)&&(item.getNodeType()==Element.ELEMENT_NODE) )
        { // Ne s'occupe que des Types ELEMENT
          String szItemName = item.getNodeName().toUpperCase();
          String szItemValue = getNodeValue(item);
          list.put(szItemName, szItemValue);
          Trace.DEBUG("FRAMEWORK FRMWRK "+parent+" "+szItemName+":" + szItemValue);
        }//if ( (item!=null)&&(item.getNodeType()==Element.ELEMENT_NODE) )
      }//for (int i=0;i<app.getLength();i++)
    }//if ( (app!=null)&&(app.item(0)!=null) )
    else
    {
      Trace.ERROR("FRAMEWORK FRMWRK "+parent+" NO ELEMENT");
    }
  }

  private String getSubTagAttribute(Node node, String subTagName, String attribute) {
    String ret = "";
    if (node != null) {
      NodeList children = node.getChildNodes();
      for (int innerLoop = 0; innerLoop < children.getLength(); innerLoop++) {
        Node child = children.item(innerLoop);
        if ( (child != null) && (child.getNodeName() != null) && child.getNodeName().equals(subTagName)) {
          if (child instanceof Element) {
            String str = ((Element)child).getAttribute(attribute);
            if (str!=null)
              ret = str.trim();
          }
        }
      } // end inner loop
    }
    return ret;
  }
  public static String getSubTagValue(Node node, String subTagName) {
      String ret = "";
      if (node != null) {
        NodeList  children = node.getChildNodes();
        for (int innerLoop =0; innerLoop < children.getLength(); innerLoop++) {
          Node  child = children.item(innerLoop);
          if ((child != null) && (child.getNodeName() != null) && child.getNodeName().equals(subTagName) ) {
            Node grandChild = child.getFirstChild();
            if (grandChild.getNodeValue() != null)
              ret = grandChild.getNodeValue().trim();
          }
        } // end inner loop
      }
      return ret;
  }
  public static String getNodeValue(Node node)
  {
    String ret = "";
    Node child = node.getFirstChild();
    if ( (child!=null)&&(child.getNodeType()==Element.TEXT_NODE)&&(child.getNodeValue()!=null) )
      ret = child.getNodeValue().trim();
    return ret;
  }
  public static String getNodeAttribute(Node node, String attribute)
  {
    String ret = "";
    if ( (node.getNodeType()==Element.ELEMENT_NODE) && (node instanceof Element) )
    {
      Element nodeElement = (Element)node;
      if ( (nodeElement != null) && (nodeElement.getAttribute(attribute) != null))
        ret = nodeElement.getAttribute(attribute).trim();
    }
    return ret;
  }
  public static Integer getNodeAttributeInteger(Node node, String attribute)
  {
    Integer ret = new Integer("0");
    String szElement = getNodeAttribute(node, attribute);
    if ( (szElement != null) && (!"".equals(szElement)))
      ret = new Integer(szElement.trim());
    return ret;
  }

  /**
 * @return  the init
 * @uml.property  name="init"
 */
public boolean isInit() {
    return init;
  }
}
