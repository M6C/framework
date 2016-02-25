package framework.service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.beandata.BeanDatabase;
import framework.beandata.BeanFindList;
import framework.beandata.BeanGenerique;
import framework.ressource.bean.BeanQuery;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public class SrvFindList extends SrvDatabase {

	//public void execute(HttpServletRequest request, HttpServletResponse response, BeanGenerique bean) throws Exception {
	public void execute(ServletRequest request, ServletResponse response, BeanGenerique bean) throws Exception {
	if ( bean instanceof BeanFindList ) {
		BeanQuery beanQuery = bean.getBeanData().getBeanQuery();
		if (beanQuery!=null){
			if ( BeanQuery.CST_STORED_PROCEDURE.equals(beanQuery.getType()) )
				((BeanFindList)bean).setList(super.executeStoredProcedureList((BeanDatabase)bean));
			else if ( BeanQuery.CST_QUERY.equals(beanQuery.getType()) )
				((BeanFindList)bean).setList(super.executeQueryList((BeanDatabase)bean));
			else if ( BeanQuery.CST_HIBERNATE.equals(beanQuery.getType()) )
				((BeanFindList)bean).setList(super.executeHqlList((BeanDatabase)bean));
		}
	}
  }
	
  public static SrvGenerique getSingleton() {
    if ( singleton==null )
      singleton = new SrvFindList();
    return singleton;
  }
}