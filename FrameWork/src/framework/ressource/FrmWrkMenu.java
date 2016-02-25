package framework.ressource;

import framework.ressource.bean.BeanMenuInfo;
import framework.ressource.util.UtilString;
import framework.trace.Trace;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Lecture du fichier XML de menu        * Forme du fichier XML <ROOT> <MENU Name="Nom_du_menu" Text="Libelle_a_afficher" Target="Chemin_vers_l_adresse_de_redirection" Level="Niveau_du_menu" /> <MENU Name="AFFICHER" Text="Afficher" Target="/videofuture?event=afficherQuelqueChose" Level="1" > <MENU Name="MODIFIER" Text="Modifier" Target="/videofuture?event=modifierQuelqueChose" Level="2" > <MENU Name="VALIDER" Text="Valider" Target="/videofuture?event=validerQuelqueChose" Level="3" > ... Normalement le nombre de niveau est illimité ... </MENU> </MENU> </MENU> </ROOT> Chaque imbrication de menu correspond au niveau réel du menu La variable Level de chaque entrée est là pour indication mais n'est pas réellement utilisé Contenu de listMenuInfo Chaque ligne de listMenuInfo correspond à un niveau de menu "Level" Exemple: listMenuInfo.elementAt(0)   = Menu de niveau 0 listMenuInfo.elementAt(1)   = Menu de niveau 1 listMenuInfo.elementAt(2)   = Menu de niveau 2 Chaque Niveau de listMenuInfo contiend une HashTable La HashTable contiend une liste de menu Chaque Element dans la HashTable est identifié par une clé (key) qui est le chemin d'imbrication parent du menu Exemple avec l'extrait précédent, pour le Menu "VALIDER" la clé sera AFFICHER.MODIFIER Chaque Element dans la HashTable contiend une sous HashTable. Chaque Element de la sous HashTable sont identifié par une clé (key) qui est le nom de la page Chaque Element de la sous HashTable contiend des "framework.ressource.bean.BeanMenuInfo" avec toutes les données du Tag XML <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  non attribuable
 * @version  1.0
 */

public class FrmWrkMenu {

  protected static Vector listMenuInfo = null;

  public static final String CST_FILE_XML_INITIALISE_ELEMENT_MENU = "MENU";
  public static final String CST_FILE_XML_INITIALISE_ELEMENT_BEAN = "BEAN";

  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_NAME = "Name";
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_TEXT = "Text";
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_TARGET = "Target";
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_LEVEL = "Level";
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_STYLE_TD = "styleTD";
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_STYLE_FONT = "styleFONT";
  public static final String CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_STYLE_A = "styleA";

  private static int menuLevel = 0;

  static
  {
    menuLevel = 0;
    setup();
  }

  public FrmWrkMenu() {
  }

  public static void setup()
  {
    Trace.OUT("FRAMEWORK MENU SETUP FILE:"+Constante.XML_FRMWRK_MENU+" - START");
    try
    {
      // Création des outils de Parse du fichier XML
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      // Lecture et Parse du Fichier XMLS
      Document document = docBuilder.parse(new File(Constante.XML_FRMWRK_MENU));
      // Récupération du 1ère Element du Fichier XML
      Element root = document.getDocumentElement();
      root.normalize();
      listMenuInfo = new Vector();
      getListMenu(root);
    }
    catch( ParserConfigurationException ex )
    {
      Trace.ERROR(ex.getMessage());
    }
    catch( SAXException ex )
    {
      Trace.ERROR(ex.getMessage());
    }
    catch( IOException ex )
    {
      Trace.ERROR(ex.getMessage());
    }
    finally
    {
      if ((listMenuInfo==null)||(listMenuInfo.size()==0)) {
        Trace.ERROR("FRAMEWORK MENU CONNECT SETUP NO ELEMENT");
        Trace.ERROR("FRAMEWORK MENU CONNECT SETUP USING CONNECT VALUES");
      }
      Trace.OUT("FRAMEWORK MENU SETUP FILE:"+Constante.XML_FRMWRK_MENU+" - END");
    }
  }
  public static void getListMenu(Element pElement)
  {
    getListMenu(pElement, "");
  }
  public static void getListMenu(Element pElement, String pParentName)
  {
    NodeList app = pElement.getElementsByTagName(CST_FILE_XML_INITIALISE_ELEMENT_MENU);
    menuLevel++;
    if ( (app!=null)&&(app.item(0)!=null)&&(app.getLength()>0) )
    { // récupère la liste des Enfants de l'élément SERVLET
      Hashtable listMenu = new Hashtable();
      BeanMenuInfo bean = null;
      for (int i=0;i<app.getLength();i++)
      {
        Element item = (Element)app.item(i);
        // Récuperation de chaques element de la liste
        if ((item!=null)&&(Node.ELEMENT_NODE==item.getNodeType()))
        {
          Element element = (Element)item;
          bean = new BeanMenuInfo();
          bean.setName(Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_NAME));
          bean.setText(Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_TEXT));
          bean.setTarget(Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_TARGET));
          bean.setStyleA(Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_STYLE_A));
          bean.setStyleFONT(Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_STYLE_FONT));
          bean.setStyleTD(Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_STYLE_TD));
          String szLevel = Constante.getNodeAttribute(element, CST_FILE_XML_INITIALISE_ATTRIBUTE_MENU_LEVEL);
          bean.setLevel(new Integer( (UtilString.isNotEmpty(szLevel)) ? szLevel : "0" ));
          listMenu.put(bean.getName(), bean);
          String szParentName = ("".equals(pParentName)) ? bean.getName() : pParentName+"."+bean.getName();
          getListMenu(item, szParentName);
        }//if(item!=null)
      }//for (int i=0;i<app.getLength();i++)
      addMenuList(listMenu, pParentName, menuLevel);
    }//if ( (app!=null)&&(app.item(0)!=null) )
    menuLevel--;
  }
  protected static void addMenuList(Hashtable pListMenu, String pName, int pLevel)
  {
    if ( (pListMenu!=null)&&(pListMenu.size()>0) )
    {
      Hashtable listMenu = null;
      if (pLevel<listMenuInfo.size())
      {
        listMenu=(Hashtable)listMenuInfo.elementAt(pLevel);
        if (listMenu==null)
        {
          listMenu = new Hashtable();
          listMenuInfo.setElementAt(listMenu, pLevel);
        }
      }
      else
      {
        listMenu = new Hashtable();
        listMenuInfo.addElement(listMenu);
      }
      listMenu.put(pName, pListMenu);
    }
  }
  /**
 * @return  the listMenuInfo
 * @uml.property  name="listMenuInfo"
 */
public static Vector getListMenuInfo() {
    return listMenuInfo;
  }
  /**
 * @param listMenuInfo  the listMenuInfo to set
 * @uml.property  name="listMenuInfo"
 */
public static void setListMenuInfo(Vector pListMenuInfo) {
    listMenuInfo = pListMenuInfo;
  }
}
