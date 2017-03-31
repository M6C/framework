package framework.beandata;

import framework.ressource.bean.BeanData;
import framework.ressource.bean.BeanParameter;
import framework.ressource.util.UtilRequest;
import framework.ressource.util.UtilString;
import framework.trace.Trace;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  unascribed
 * @version  1.0
 */

public class BeanGenerique implements Serializable {

  protected Integer index = new Integer(0);
  protected BeanData beanData = null;
  protected Vector parameterData = null;


  /**
   * Recherche les paramètres en requette.
   * @param request
   * @return la liste des critères en requette
   */
  //public void loadParameter(HttpServletRequest request){
  public void loadParameter(ServletRequest request){
	if ( (getParameter()!=null)&&(getParameter().size()>0) )
		setParameterData(loadBeanParameter(request));
  }

  /**
   * Recherche les paramètres en requette.
   * @param request
   * @return la liste des critères en requette
   */
  //protected Vector loadBeanParameter(HttpServletRequest request) {
  protected Vector loadBeanParameter(ServletRequest request) {
//	  Trace.DEBUG(this, "loadBeanParameter START");
	Vector ret = getParameterData();
	if ( (getParameter()!=null) && (getParameter().size()>0) ){
	  for(int i=0 ; i<getParameter().size() ; i++ )
	  { // Parcoure tous les beans parametre
		BeanParameter beanParam = (BeanParameter)getParameter().elementAt(i);
		if ( (beanParam!=null) && (beanParam.getName()!=null) &&
				!"FALSE".equals(beanParam.getSynchronize()))
		{ // Recupere la donnée correspondant au bean parametre
		  String szParamName = beanParam.getName();
          String szParamType = beanParam.getType();
		  Object szData = null;
		  if ( (beanParam.getBean()!=null)&&(!"".equals(beanParam.getBean())) )
			// Le paramètre est une propriete d'un bean déja mis en requette
			szData = getBeanParameter(request, beanParam.getBean(), szParamName);
		  else {
	            if (UtilString.isEqualsIgnoreCase(szParamType, "LIST")) {
	              String[] listValue = request.getParameterValues(szParamName);
	              if ((listValue!=null)&&(listValue.length>0)) {
	                szData = new ArrayList();
	                for (int j=0 ; j<listValue.length ; ((List)szData).add(listValue[j++]));
	              }
	            }
	            else
	              // Le paramètre se trouve dans la requette
	              szData = request.getParameter(beanParam.getName());
	              if (szData==null)
		              // Le paramètre se trouve dans la requette
		              szData = request.getAttribute(beanParam.getName());
	          	}

//		  if (szData != null) {
			if (ret == null) {
			  ret = new Vector(getParameter().size());
			  ret.setSize(getParameter().size());
			}
			if (!"".equals(szData)) { // Teste si un index est defini pour le parametre
	            if (UtilString.isEqualsIgnoreCase(szParamType, "INTEGER")) {
	            	try {
	            		szData = new Integer(szData.toString());
	            	} catch(Exception ex) {
	            		ex.printStackTrace();
	            	}
	            }
	            else if (UtilString.isEqualsIgnoreCase(szParamType, "BOOLEAN")) {
	            	try {
	            		szData = new Boolean(szData.toString());
	            	} catch(Exception ex) {
	            		ex.printStackTrace();
	            	}
	            }
			}
			if (beanParam.getIndex() != null) {
				// Insere le critere dans la liste
				ret.setElementAt(szData, beanParam.getIndex().intValue());
			} else {
				// Ajoute le critere à la liste
				ret.setElementAt(szData, i);
			}
//		  }
		}
	  }
	  if (ret == null)
		ret = new Vector(getParameter().size());
	  ret.setSize(getParameter().size());
	}
//	  Trace.DEBUG(this, "loadBeanParameter END");
	return ret;
  }


  /**
   * Recherche le parametre dans un bean mis en requette ou en session
   * @param request
   * @param szBeanName Nom du bean à rechercher
   * @param szParamName Nom du parametre dans le bean
   * @return la valeur du parametre
   */
  //public Object getBeanParameter(HttpServletRequest request, String szBeanName, String szParamName)
  public Object getBeanParameter(ServletRequest request, String szBeanName, String szParamName)
  {
	Object ret = null;
	try {
		HttpSession session = ((HttpServletRequest)request).getSession();
		/*
		Object beanObj = request.getAttribute(szBeanName);
		if (beanObj==null)
		  beanObj = session.getAttribute(szBeanName);
		*/
		Object beanObj = UtilRequest.findObject(request, session, "R$"+szBeanName);
		if (beanObj==null)
			beanObj = UtilRequest.findObject(request, session, "S$"+szBeanName);
		
		if( beanObj!=null ) {
			if ( beanObj instanceof BeanFindData ) {
				BeanFindData beanFindData = (BeanFindData)beanObj;
				if ( beanFindData.getData()!=null ) {
					ret = beanFindData.getData().getFieldValue(szParamName);
				}
			}
			else {
				ret = UtilRequest.findValue(request, session, beanObj, szParamName);
			}
		}
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		Trace.ERROR(this, e);
	} catch (NoSuchMethodException e) {
		// TODO Auto-generated catch block
		Trace.ERROR(this, e);
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		Trace.ERROR(this, e);
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		Trace.ERROR(this, e);
	}
	
	return ret;
  }

  public Object get(Object name){
    Object ret = null;
    try {
      ret = getParameterDataByName( (String) name);
    }
    catch (Exception ex){}
    return ret;
  }

  public Object getParameterDataByName(String name) {
    Object ret = null;
    if ( (beanData != null) && (parameterData != null)) {
      Vector parameter = beanData.getParameter();
      if ( (parameter != null) && (parameter.size() <= parameterData.size())) {
        int paramSize = parameter.size();
        BeanParameter beanParam = null;
        for (int i = 0; i < paramSize; i++) {
          beanParam = (BeanParameter) parameter.elementAt(i);
          if ( (beanParam != null) && (beanParam.getName().equals(name))) {
            ret = parameterData.elementAt(i);
            if (ret==null)
            	ret = beanParam.getDefaultData();
            break;
          }
        }
      }
    }
    return ret;
  }
  
  public void setParameter(String name, Object data) {
	  BeanParameter bean = new BeanParameter();
	  bean.setName(name);
	  boolean find = false;
	  Vector parameter = beanData.getParameter();
	  if (parameterData == null)
		  parameterData = new Vector();
	  if ( (name != null) && (data != null) ) {
		  int paramSize = parameter.size();
		  if (parameterData.size()!=paramSize)
			  parameterData.setSize(paramSize);
		  BeanParameter beanParam = null;
		  for (int i = 0; i < paramSize; i++) {
			  beanParam = (BeanParameter) parameter.elementAt(i);
			  if ( (beanParam != null) && (beanParam.getName().equals(name))) {
				  parameterData.setElementAt(data, i);
				  find = true;
				  break;
			  }
		  }
	  }
	  if (!find) {
		  parameter.add(bean);
		  parameterData.add(data);
	  }
}

  public Integer getSize(){
  	return new Integer(0);
  }

  public boolean hasMoreElements(){
  	return false;
  }

  public String getService() {
    return (getBeanData()!=null) ? getBeanData().getService() : null;
  }
  public void setService(String service) {
    if (getBeanData()!=null)
      getBeanData().setService(service);
  }
  public Vector getParameter() {
    return (getBeanData()!=null) ? getBeanData().getParameter() : null;
  }
  public void setParameter(Vector parameter) {
    if (getBeanData()!=null)
      getBeanData().setParameter(parameter);
  }
  public String getQuery() {
    return ( (getBeanData()!=null)&&(getBeanData().getBeanQuery()!=null) ) ? getBeanData().getBeanQuery().getQuery() : null;
  }
  public void setQuery(String query) {
    if (getBeanData()!=null)
      getBeanData().getBeanQuery().setQuery(query);
  }
  public String getName() {
    return (getBeanData()!=null) ? getBeanData().getName() : null;
  }
  public void setName(String name) {
    if (getBeanData()!=null)
      getBeanData().setName(name);
  }
  /**
 * @return  the beanData
 * @uml.property  name="beanData"
 */
public BeanData getBeanData() {
    return beanData;
  }
  /**
 * @param beanData  the beanData to set
 * @uml.property  name="beanData"
 */
public void setBeanData(BeanData beanData) {
    this.beanData = beanData;
  }
  /**
 * @return  the parameterData
 * @uml.property  name="parameterData"
 */
public Vector getParameterData() {
    return parameterData;
  }
  /**
 * @param parameterData  the parameterData to set
 * @uml.property  name="parameterData"
 */
public void setParameterData(Vector parameterData) {
    this.parameterData = parameterData;
  }
  /**
 * @return  the index
 * @uml.property  name="index"
 */
public Integer getIndex() {
    return index;
  }
  /**
 * @param index  the index to set
 * @uml.property  name="index"
 */
public void setIndex(Integer index) {
    this.index = index;
  }
}
