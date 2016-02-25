package framework.service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.beandata.BeanDatabase;
import framework.beandata.BeanFindData;
import framework.beandata.BeanFindList;
import framework.beandata.BeanGenerique;
import framework.ressource.bean.BeanData;
import framework.ressource.bean.BeanQuery;
import framework.trace.Trace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public class SrvFindData extends SrvDatabase {

  //public void execute(HttpServletRequest request, HttpServletResponse response, BeanGenerique bean) throws Exception{
  public void execute(ServletRequest request, ServletResponse response, BeanGenerique bean) throws Exception{
    if ( bean instanceof BeanFindData ) {
      BeanQuery beanQuery = bean.getBeanData().getBeanQuery();
      if (beanQuery!=null){
    	if ( BeanQuery.CST_STORED_PROCEDURE.equals(beanQuery.getType()) )
			((BeanFindData)bean).setData(super.executeStoredProcedureData((BeanDatabase)bean));
		else if ( BeanQuery.CST_QUERY.equals(beanQuery.getType()) )
			((BeanFindData)bean).setData(super.executeQueryData((BeanDatabase)bean));
		else if ( BeanQuery.CST_HIBERNATE.equals(beanQuery.getType()) ) {
			Object object = super.executeHqlData((BeanDatabase)bean);
			BeanData beanData = bean.getBeanData();
		    String szScope = beanData.getScope();
            Trace.DEBUG(this, "store bean: '" + beanData.getName() + "' class: " + (object==null ? "null" : object.getClass().toString()) + " in scope: " + szScope);
            if (BeanData.CST_SCOPE_SESSION.equalsIgnoreCase(szScope)) {
				if (request instanceof HttpServletRequest) {
					HttpSession session = ((HttpServletRequest)request).getSession();
					// Mets le Bean de donnée en session
					session.setAttribute(beanData.getName(), object);
				}
            }
            else {
              // attach bean object to request:
              request.setAttribute(beanData.getName(), object);
            }
		}
	  }
    }
  }

  public static SrvGenerique getSingleton() {
    if ( singleton==null )
      singleton = new SrvFindData();
    return singleton;
  }
}