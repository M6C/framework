package framework.service;

import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.adaptator.AdpDatabase;
import framework.beandata.BeanDatabase;
import framework.beandata.BeanGenerique;
import framework.convoyeur.CvrData;
import framework.convoyeur.CvrList;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SrvDatabase extends SrvGenerique {

  public void executeBeforeQuery(HttpServletRequest request, HttpServletResponse response, BeanDatabase bean) throws Exception {
  }

  public void executeAfterQuery(HttpServletRequest request, HttpServletResponse response, BeanDatabase bean) throws Exception {
  }
  public void execute(ServletRequest request, ServletResponse response, BeanGenerique bean) throws Exception {
	  if (bean instanceof BeanDatabase) {
	    AdpDatabase.execute((BeanDatabase)bean);
	  }
  }

  public static CvrData executeStoredProcedureData(BeanDatabase pBeanDB) throws SQLException {
    return AdpDatabase.executeStoredProcedureData(pBeanDB);
  }

  public static CvrList executeStoredProcedureList(BeanDatabase pBeanDB) throws SQLException {
    return AdpDatabase.executeStoredProcedureList(pBeanDB);
  }

  public CvrData executeQueryData(BeanDatabase pBeanDB) throws SQLException
  {
    return AdpDatabase.executeQueryData(pBeanDB);
  }

  public static CvrData executeQueryData(String pQuery) throws SQLException {
    return AdpDatabase.executeQueryData(pQuery);
  }

  public static CvrData executeQueryData(String pQuery, Vector pParameter) throws SQLException {
    return AdpDatabase.executeQueryData(pQuery, pParameter);
  }

  public CvrList executeQueryList(BeanDatabase pBeanDB) throws SQLException {
    return AdpDatabase.executeQueryList(pBeanDB);
  }

  public static CvrData executeQueryList(String pQuery) throws SQLException {
    return AdpDatabase.executeQueryData(pQuery);
  }

  public static CvrData executeQueryList(String pQuery, Vector pParameter) throws SQLException {
    return AdpDatabase.executeQueryData(pQuery, pParameter);
  }

  public int executeUpdate(BeanDatabase pBeanDB) throws SQLException {
    return AdpDatabase.executeUpdate(pBeanDB);
  }

  public CvrList executeHqlList(BeanDatabase pBeanDB) throws SQLException {
    return AdpDatabase.executeHqlList(pBeanDB);
  }

  public Object executeHqlData(BeanDatabase pBeanDB) throws SQLException {
    return AdpDatabase.executeHqlData(pBeanDB);
  }

  public void executeHql(BeanDatabase pBeanDB) throws SQLException {
    AdpDatabase.executeHql(pBeanDB);
  }
}