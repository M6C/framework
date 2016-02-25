package framework.ressource.bean;

import framework.trace.Trace;

import java.io.Serializable;
import java.util.Vector;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  not attributable
 * @version  1.0
 */

public class BeanData implements Serializable {

	private static final long serialVersionUID = -820072141798128981L;

  public final static String CST_SCOPE_SESSION = "session";
  public final static String CST_SCOPE_REQUEST = "request";

  private String name = null;
  private BeanQuery beanQuery = null;
  private String className = null;
  private String service = null;
  private Vector parameter = new Vector();
  private String scope = null;

  public BeanData() {
  }

  public BeanData(String name, String className, String service) {
    setName(name);
    setClassName(className);
    setService(service);
  }

  public void addParameter(Object obj) {
    getParameter().addElement(obj);
  }

  public void setParameterAt(Object obj, int index) {
    try
    {
      if( getParameter().size()<=index )
        getParameter().setSize(index+1);
      getParameter().setElementAt(obj, index);
    }
    catch( ArrayIndexOutOfBoundsException ex )
    {
      Trace.ERROR(this, ex.getMessage());
    }
  }

  /**
 * @return  the name
 * @uml.property  name="name"
 */
public String getName() {
    return name;
  }

  /**
 * @param name  the name to set
 * @uml.property  name="name"
 */
public void setName(String name) {
    this.name = name;
  }

  /**
 * @return  the className
 * @uml.property  name="className"
 */
public String getClassName() {
    return className;
  }

  /**
 * @param className  the className to set
 * @uml.property  name="className"
 */
public void setClassName(String className) {
    this.className = className;
  }

  /**
 * @return  the service
 * @uml.property  name="service"
 */
public String getService() {
    return service;
  }

  /**
 * @param service  the service to set
 * @uml.property  name="service"
 */
public void setService(String service) {
    this.service = service;
  }

  /**
 * @return  the parameter
 * @uml.property  name="parameter"
 */
public Vector getParameter() {
    return parameter;
  }

  /**
 * @param parameter  the parameter to set
 * @uml.property  name="parameter"
 */
public void setParameter(Vector parameter) {
    this.parameter = parameter;
  }
  /**
 * @return  the beanQuery
 * @uml.property  name="beanQuery"
 */
public BeanQuery getBeanQuery() {
    return beanQuery;
  }
  /**
 * @param beanQuery  the beanQuery to set
 * @uml.property  name="beanQuery"
 */
public void setBeanQuery(BeanQuery beanQuery) {
    this.beanQuery = beanQuery;
  }
  /**
 * @return  the scope
 * @uml.property  name="scope"
 */
public String getScope() {
    return scope;
  }
  /**
 * @param scope  the scope to set
 * @uml.property  name="scope"
 */
public void setScope(String scope) {
    this.scope = scope;
  }
}