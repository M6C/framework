package framework.ressource.bean;

import framework.ressource.util.UtilEvalJava;
import framework.ressource.util.UtilRequest;
import framework.ressource.util.UtilString;
import framework.ressource.util.UtilVector;

import java.io.Serializable;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  not attributable
 * @version  1.0
 */

public class BeanServlet implements Serializable {

  private static final long serialVersionUID = -7030460465167239123L;

  private String name = null;
  private String servlet = null;
  private String target = null;
  private String targetError = null;
  private String authentification = null;
  private Vector listBean = new Vector();
  private Vector listForward = new Vector();
  private Vector listForwardError = new Vector();

  public BeanServlet() {
  }

  public void addBean(BeanData bean) {
    getListBean().addElement(bean);
  }

  public void addForward(BeanForward bean) {
    getListForward().addElement(bean);
  }

  public void addForwardError(BeanForwardError bean) {
    getListForwardError().addElement(bean);
  }

  public void addBean(String name, String className, String service) {
    getListBean().addElement(new BeanData(name, className, service));
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
 * @return  the servlet
 * @uml.property  name="servlet"
 */
public String getServlet() {
    return servlet;
  }

  /**
 * @param servlet  the servlet to set
 * @uml.property  name="servlet"
 */
public void setServlet(String servlet) {
    this.servlet = servlet;
  }

  /**
 * @return  the target
 * @uml.property  name="target"
 */
public String getTarget() {
    return target;
  }

  /**
 * @param target  the target to set
 * @uml.property  name="target"
 */
public void setTarget(String target) {
    this.target = target;
  }

  /**
 * @return  the targetError
 * @uml.property  name="targetError"
 */
public String getTargetError() {
    return targetError;
  }

  /**
 * @param targetError  the targetError to set
 * @uml.property  name="targetError"
 */
public void setTargetError(String targetError) {
    this.targetError = targetError;
  }

  /**
 * @return  the authentification
 * @uml.property  name="authentification"
 */
public String getAuthentification() {
    return authentification;
  }

  /**
 * @param authentification  the authentification to set
 * @uml.property  name="authentification"
 */
public void setAuthentification(String authentification) {
    this.authentification = authentification;
  }

  /**
 * @return  the listBean
 * @uml.property  name="listBean"
 */
public Vector getListBean() {
    return listBean;
  }

  /**
 * @return  the listForwardError
 * @uml.property  name="listForwardError"
 */
public Vector getListForwardError() {
    return listForwardError;
  }

  /**
 * @return  the listForward
 * @uml.property  name="listForward"
 */
public Vector getListForward() {
    return listForward;
  }

  /**
 * @param listBean  the listBean to set
 * @uml.property  name="listBean"
 */
public void setListBean(Vector listBean) {
    this.listBean = listBean;
  }

  /**
 * @param listForwardError  the listForwardError to set
 * @uml.property  name="listForwardError"
 */
public void setListForwardError(Vector listForwardError) {
    this.listForwardError = listForwardError;
  }

  /**
 * @param listForward  the listForward to set
 * @uml.property  name="listForward"
 */
public void setListForward(Vector listForward) {
    this.listForward = listForward;
  }

  /**
   * Redirection
   */
  public String getForwardTarget(HttpServletRequest request) {
    return getForwardTarget(request, listForward, target);
  }

  /**
   * Redirection
   */
  public String getForwardTargetError(HttpServletRequest request) {
    return getForwardTarget(request, listForwardError, targetError);
  }

  /**
   * Redirection
   */
  protected String getForwardTarget(HttpServletRequest request, Vector pListForward, String pDefaultTarget) {
    String ret = null;
    int size = UtilVector.safeSize(pListForward);
    for( int i=0 ; (ret==null) && (i<size) ; i++ ) {
      BeanForward beanF = (BeanForward)UtilVector.safeGetElementAt(pListForward, i);
      if ((beanF!=null)&&(UtilString.isNotEmpty(beanF.getTarget()))) {
        if (UtilString.isNotEmpty(beanF.getCondition())) {
          String szReplaceNullBy = UtilString.isNotEmpty(beanF.getReplaceNullBy()) ? beanF.getReplaceNullBy() : "";
          String szCondition = UtilRequest.replaceParamByRequestValue(beanF.getCondition(), request, request.getSession(), szReplaceNullBy);
         if (UtilEvalJava.safeResolveBooleanExpression(szCondition))
           ret = beanF.getTarget();
        }
        else
          ret = beanF.getTarget();
      }
    }

    if (ret==null)
      ret = pDefaultTarget;

    if (UtilString.isNotEmpty(ret)) { // Redirige vers la page de destination
      if (ret.startsWith("#")) {
        // La target est un parametre de la request
        ret = request.getParameter(ret.substring(1));
      }
    }
    return ret;
  }
}
