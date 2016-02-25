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

public class SrvHbnExecute extends SrvDatabase {

  //public void execute(HttpServletRequest request, HttpServletResponse response, BeanGenerique bean) throws Exception{
  public void execute(ServletRequest request, ServletResponse response, BeanGenerique bean) throws Exception{
    if ( bean instanceof BeanFindData ) {
      BeanQuery beanQuery = bean.getBeanData().getBeanQuery();
      if (beanQuery!=null){
		super.executeHql((BeanDatabase)bean);
	  }
    }
  }

  public static SrvGenerique getSingleton() {
    if ( singleton==null )
      singleton = new SrvHbnExecute();
    return singleton;
  }
}