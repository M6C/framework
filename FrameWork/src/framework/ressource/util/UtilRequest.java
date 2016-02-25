package framework.ressource.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import framework.convoyeur.CvrField;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UtilRequest {
  private UtilRequest() {
  }

  /**
   * Remplace dans le texte les paramètres separamp;eacute; par des '#' par les valeurs recuperamp;eacute; en request
   *
   * Exemple:
   *          - "?IDPARAM1=#R$BeanName.BeanParam1#;IDPARAM2=#R$BeanName.BeanParam2#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1#" par la valeur de la methode "getBeanParam1()" du bean
   *               "#R$BeanName.BeanParam2#" par la valeur de la methode "getBeanParam2()" du bean
   *          - "?IDPARAM1=#S$Param1#"
   *               Recherche dans la session la valeur du parametre "#S$Param1#" par sa valeur en request
   *          - "?IDPARAM1=#S$BeanName.BeanParam1.toInteger#"
   *               Recherche dans la session le bean "BeanName" et remplace
   *               "#S$BeanName.BeanParam1.toInteger()#" par la valeur de la methode "toInteger()" de la methode "getBeanParam1()" du bean
   *          - "?IDPARAM1=#R$BeanName:ID_PARAM#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName:ID_PARAM#" par la valeur de la methode get("ID_PARAM") du bean
   *          - "?IDPARAM1=#R$BeanName.BeanParam1:ID_PARAM#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1:ID_PARAM#" par la valeur de la methode get("ID_PARAM") de la methode "getBeanParam1()" du bean
   *          - "?IDPARAM1=#R$BeanName.BeanParam1:ID_PARAM<encoding=UTF-8>#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1:ID_PARAM#" par la valeur de la methode get("ID_PARAM") de la methode "getBeanParam1()" du bean
   *               Le tout encode au format "UTF-8"
   *          - "?IDPARAM1=#R$BeanName.BeanParam1:ID_PARAM<decoding=UTF-8>#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1:ID_PARAM#" par la valeur de la methode get("ID_PARAM") de la methode "getBeanParam1()" du bean
   *               Le tout decode au format "UTF-8" Peut etre ('HTML', 'HTMLSpecialChars', 'HTMLEntities' ou tout formar d'encodage)
   *
   * @param text Texte ou doivent être recherchamp;eacute; les paramètres
   * @param request Requête de la servlet
   * @return La chaine Texte avec chaque paramètre remplacamp;eacute; par leur valeur
   */
  public static String replaceParamByRequestValue(String text, ServletRequest request, HttpSession session, String replaceNullBy) {
    return replaceParamByRequestValue(text, '#', request, session, replaceNullBy);
  }
  /**
   * Remplace dans le texte les paramètres separamp;eacute; par des samp;eacute;parateurs par les valeurs recuperamp;eacute; en request
   *
   * Exemple:
   *          - "/servlet?event=action;IDPARAM1=#R$BeanName.BeanParam1#;IDPARAM2=#R$BeanName.BeanParam2#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1#" par la valeur de la methode "getBeanParam1()" du bean
   *               "#R$BeanName.BeanParam2#" par la valeur de la methode "getBeanParam2()" du bean
   *          - "/servlet?event=action;IDPARAM1=#S$Param1#"
   *               Recherche dans la session la valeur du parametre "#Param1#" par sa valeur en request
   *          - "/servlet?event=action;IDPARAM1=#S$BeanName.BeanParam1.toInteger#"
   *               Recherche dans la session le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1.toInteger()#" par la valeur de la methode "toInteger()" de la methode "getBeanParam1()" du bean
   *          - "/servlet?event=action;IDPARAM1=#R$BeanName:ID_PARAM#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName:ID_PARAM#" par la valeur de la methode get("ID_PARAM") du bean
   *          - "/servlet?event=action;IDPARAM1=#R$BeanName.BeanParam1:ID_PARAM#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1:ID_PARAM#" par la valeur de la methode get("ID_PARAM") de la methode "getBeanParam1()" du bean
   *          - "?IDPARAM1=#R$BeanName.BeanParam1:ID_PARAM<encoding=UTF-8>#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1:ID_PARAM#" par la valeur de la methode get("ID_PARAM") de la methode "getBeanParam1()" du bean
   *               Le tout encode au format "UTF-8"
   *          - "?IDPARAM1=#R$BeanName.BeanParam1:ID_PARAM<decoding=UTF-8>#"
   *               Recherche dans la request le bean "BeanName" et remplace
   *               "#R$BeanName.BeanParam1:ID_PARAM#" par la valeur de la methode get("ID_PARAM") de la methode "getBeanParam1()" du bean
   *               Le tout decode au format "UTF-8" Peut etre ('HTML', 'HTMLSpecialChars', 'HTMLEntities' ou tout formar d'encodage)
   *
   * @param text Texte ou doivent être recherchamp;eacute; les paramètres
   * @param separator Separateur des paramètre(# dans les exemples ci-dessus)
   * @param request Requête de la servlet
   * @return La chaine Texte avec chaque paramètre remplacamp;eacute; par leur valeur
   */
  public static String replaceParamByRequestValue(String text, char separator, ServletRequest request, HttpSession session, String replaceNullBy) {
    StringBuffer ret = new StringBuffer(UtilString.isEmpty(text) ? "" : text);
    if (UtilString.isNotEmpty(text)){
	    int iDeb=0, iFin=0, iPos=0;
	    while(iPos!=-1)
	    { // Recherche la première occurence du separateur
	      iDeb = ret.toString().indexOf(separator, iPos);
        // Test si le separteur n'est pas précédé d'un caractere spécial
        if ((iDeb>0)&&(ret.toString().charAt(iDeb-1)=='\\')) {
          // Refait la recherche
          iDeb = ret.toString().indexOf(separator, iDeb+1);
        }
	      // Recherche la deuxième occurence du separateur
        // iFin = (iDeb<0) ? iDeb : ret.toString().indexOf(separator, iDeb+1);
        iFin = ret.toString().indexOf(separator, iDeb+1);
        // Test si le separteur n'est pas précédé d'un caractere spécial
        if ((iFin>0)&&(ret.toString().charAt(iFin-1)=='\\')) {
          // Refait la recherche
          iFin = ret.toString().indexOf(separator, iFin+1);
        }
	      // Test si il y a au moins deux occurences du separateur
        if((iDeb>-1)&&(iFin>-1)) {
        	// Recupere le contenu des separateurs
	        String str = ret.substring(iDeb+1, iFin);
	        // Test si le contenu n'est pas vide
	        if (UtilString.isNotEmpty(str)) {
	          // Recupere la site des chaines separes par des points
	          String[] listElement = UtilString.split(str, '.');
	          // Test si la liste n'est pas vide
	          if ( (listElement!=null)&&(listElement.length>0) ) {
                    String encoding = null;
                    String decoding = null;
	            Object value = null;
	            try {
	              // Boucle sur les parametres
	              for (int i = 0; i < listElement.length; i++) {
                        String element = listElement[i];
                        int iInf = element.indexOf('<');
                        int iSup = element.indexOf('>');
                        while ((iInf>-1)&&(iSup>-1)) {
                          String actionName = null, actionValue = null;
                          int iEgu = element.indexOf('=', iInf);
                          if ((iEgu>iInf)&&(iEgu<iSup)) {
                            actionName = element.substring(iInf+1, iEgu);
                            actionValue = element.substring(iEgu+1, iSup);
                            if ("encoding".equalsIgnoreCase(actionName))
                              encoding = actionValue;
                            else if ("decoding".equalsIgnoreCase(actionName))
                              decoding = actionValue;
                          }
                          else
                            actionName = element.substring(iInf+1, iSup);
                          // Reconstruit l'element sans l'action
                          element = element.substring(0, iInf)+element.substring(iSup+1);
                          iInf = element.indexOf('<');
                          iSup = element.indexOf('>');
                        }
	                value = findValue(request, session, value, element);
	                if (value==null)
	                	break;
	              }
	            }
	            catch (Exception ex) {
	              ex.printStackTrace();
	            }
	            // Test si on a trouver une valeur de type Champ
	            value = ((value != null)&&(value instanceof CvrField)) ? ((CvrField)value).getData() : value;
	            // Remplace les null par la chaine une autre chaine
	            value = ((value==null)&&(replaceNullBy!=null)) ? replaceNullBy : value;
	            if (value!=null) {
                      String szValue = value.toString();
                      if (UtilString.isNotEmpty(decoding)) {
                        try {
                          szValue = java.net.URLDecoder.decode(szValue, decoding);
                        } catch (Exception ex) {}
                      }
                      if (UtilString.isNotEmpty(encoding))
                        try {
                          if ("HTML".equalsIgnoreCase(encoding))
                            szValue = UtilEncoder.encodeHTML(szValue);
                          else if ("HTMLSpecialChars".equalsIgnoreCase(encoding))
                            szValue = UtilEncoder.encodeHTMLSpecialChars(szValue);
                          else if ("HTMLEntities".equalsIgnoreCase(encoding))
                            szValue = UtilEncoder.encodeHTMLEntities(szValue);
                          else
                            szValue = java.net.URLEncoder.encode(szValue, encoding);
                        } catch (Exception ex) {}
	              // Remplace le parametre par ça valeur
	              ret.replace(iDeb, iFin + 1, szValue);
	              iFin = iDeb + szValue.length();
	            } else {
	              // Remplace le parametre par ça valeur
	              ret.replace(iDeb, iFin + 1, "");
	              iFin = iDeb;
	            }
	          }
	        }
	      }//if( (iDeb!=-1)&&(iFin!=-1) )
	      iPos=iFin;
	    }//while(iPos!=-1)
	  }
    return UtilString.replaceAll(ret.toString(), "\\#", "#");
  }
  public static Object findObject(ServletRequest request, HttpSession session, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
    Object ret = null;
    if (UtilString.isNotEmpty(name)) {
      // Recupere la site des chaines separes par des points
      String[] listName = UtilString.split(name, '.');
      // Test si la liste n'est pas vide
      if ( (listName!=null)&&(listName.length>0) ) {
        Object value = null;
        try {
          // Boucle sur les parametres
          for (int i = 0; i < listName.length; i++) {
            ret = findValue(request, session, ret, listName[i]);
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
    return ret;
  }
  public static Object findValue(ServletRequest request, HttpSession session, Object bean, String element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IllegalArgumentException {
    Object ret = null;
    // Test si l'element est valide
    if (UtilString.isNotEmpty(element)) {
      int index = element.indexOf(':');
      if (index > -1) {
        String name = element.substring(0, index);
        bean = (bean == null) ? getAttribute(name, request, session) : UtilReflect.safeInvokeMethod(bean, "get", new Class[] {Object.class} , new Object[] {name});
        element = element.substring(index+1);
      }
      else
        bean = (bean == null) ? getAttribute(element, request, session) : bean;
      if (bean != null) {
    	Class[] listClass = null;
    	Object[] listParam = null;
    	int i1 = element.indexOf('(');
    	int i2 = element.indexOf(')');
    	if (i1>0 && i2>i1) {
    		String param = element.substring(i1+1, i2);
    		listParam = UtilString.split(param, ',');
    		int size = UtilSafe.safeListSize(listParam);
    		if (size>0) {
    			ArrayList alClass = new ArrayList();
    			ArrayList alParam = new ArrayList();
    			Object value = null;
	    		for (int i=0 ; i<size ; i++) {
	    			param = (String)listParam[i];
	    			if (UtilString.isNotEmpty(param) && param.charAt(0) == '%' && param.charAt(param.length()-1) == '%') {
	    				value = UtilRequest.findObject(request, session, param.substring(1, param.length()-1));
	    			}
	    			else {
	    				value = param;
	    			}
    				alClass.add((value!=null) ? value.getClass() : value);
    				alParam.add(value);
	    		}
	    		//listClass = new Class[alClass.size()];
	    		//alClass.toArray(listClass);
	    		listParam = alParam.toArray();
    		}
    		element = element.substring(0, i1);
    	}
        // Recherche la valeur directement par le nom
        ret = UtilReflect.safeInvokeMethod(bean, element, listClass, listParam);
        if (ret == null) {
          // Si la valeur n'est pas trouve recupere la valeur en passant par les accesseurs
          ret = UtilReflect.safeInvokeMethod(bean, "get" + formatNameFistUp(element), listClass, listParam);
          if (ret == null) {
            // Recupere la valeur du champ dans la base
            ret = UtilReflect.safeInvokeMethod(bean, "get", new Class[] {Object.class} , new Object[] {element});
            if (ret == null) {
              // Ramp;eacute;cupere la valeur dans la request
//              ret = getAttribute(element, request, session);
              // Retourne le bean
              ret = bean;
            }
          }
        }
      }
    }
    return ret;
  }
  public static Object getAttribute(String text, ServletRequest request, HttpSession session) {
    Object ret = null;
    if(text.indexOf("$")==1) {
      String scope = text.substring(0, 2);
      if(scope.equalsIgnoreCase("S$"))
        // Ramp;eacute;cupere l'object en session
        ret = session.getAttribute(text.substring(2));
      else {
        // Ramp;eacute;cupere l'object en request
        ret = request.getAttribute(text.substring(2));
        if (ret==null)
          ret = request.getParameter(text.substring(2));
      }
    }
    else {
      ret = request.getAttribute(text);
      if (ret==null)
        ret = request.getParameter(text);
    }
    return ret;
  }
  protected static String formatNameFistUp(String pName) {
	String ret = null;
	if (pName!=null)
	  ret=((pName.length()>1) ? (pName.substring(0, 1).toUpperCase()+pName.substring(1)) : pName.toUpperCase());
	return ret;
  }
}
