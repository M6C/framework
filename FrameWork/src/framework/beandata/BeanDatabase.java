package framework.beandata;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import framework.ressource.bean.BeanParameter;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public abstract class BeanDatabase extends BeanGenerique {

  public String getCritereValue(String name)
  {
    String ret = "";
    if( (getBeanData()!=null)&&(getBeanData().getParameter()!=null)&&
        (getParameterData()!=null)&&(getParameterData().size()>0) )
    {
      Vector listParam = getBeanData().getParameter();
      for( int i=0 ; i<listParam.size() ; i++ )
      {
        BeanParameter beanParam = (BeanParameter)listParam.elementAt(i);
        if( (beanParam!=null)&&(beanParam.getName()!=null) )
        {
          if(beanParam.getName().equals(name))
          {
            int iIndex = beanParam.getIndex().intValue();
            if (iIndex<getParameter().size())
              ret = (String)getParameterData().elementAt(iIndex);
            ret = (ret!=null) ? ret : "";
            break;
          }
        }
      }
    }
    return ret;
  }

  /**
   * Recherche les paramètres en requette par le nom du bean.
   * Les parametres en requete doivent être formater de la façon suivante:
   * crt_"nom-du-bean"_"index-du-critere-dans-le-select"
   *
   * @param request
   * @param szBeanName nom du bean
   * @return la liste des critères en requette
   */
  protected Vector loadBeanParameter(HttpServletRequest request, String szBeanName) {
//    Trace.DEBUG(this, "loadBeanParameter START");
    Vector ret = null;
    if (szBeanName != null) {
      /**
       * @todo Formater les parametres en requete HTTP de la façon
       * suivante: crt_"nom-du-bean"_"index-du-critere-dans-le-select"
       */
      Enumeration enumAttrName = request.getParameterNames();
      while (enumAttrName.hasMoreElements()) { // Parcoure tous les parametres en requete
        String szAttrName = (String) enumAttrName.nextElement();
        if ( (szAttrName != null) && (szAttrName.length() >= szBeanName.length())) { // Recupere les noms des parametres
          String szPrefix = szAttrName.substring(0, szBeanName.length());
          if (szPrefix.equalsIgnoreCase(szBeanName)) { // Teste si c'est un parametre de critere
            String obj = request.getParameter(szAttrName);
            if (obj != null)
            { // Teste si il y a un objet attache au parametre
              if (ret == null) ret = new Vector();
              int index = getIndexFromStr(szAttrName);
              if (index >= 0)
              {
                if ( ret.size()<=index ) ret.setSize(index+1);
                // Insere le critere dans la liste
                ret.setElementAt(obj, index);
              }
              else
                // Ajoute le critere à la liste
                ret.addElement(obj);
            }
          }
        }
      }
    }
//    Trace.DEBUG(this, "loadBeanParameter END");
    return ret;
  }

  protected int getIndexFromStr(String pStr) {
    int ret = -1;
    /*
        int iCount=0;
        StringTokenizer stz = new StringTokenizer(pStr, "_");
        while(stz.hasMoreTokens()&&(iCount<2))
          stz.nextToken();
        if(stz.hasMoreTokens()||(iCount==2))
          ret = Integer.parseInt(stz.nextToken());
     */
    int iLastIndex = pStr.lastIndexOf("_");
    if ( (iLastIndex >= 0) && (iLastIndex < (pStr.length() - 1))) {
      String szSub = pStr.substring(iLastIndex + 1);
      try {
        ret = Integer.parseInt(szSub);
      }
      catch (NumberFormatException ex) {
        ret = -1;
      }
    }

    return ret;
  }
}
