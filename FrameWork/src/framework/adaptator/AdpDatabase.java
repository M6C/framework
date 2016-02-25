package framework.adaptator;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

import framework.beandata.BeanDatabase;
import framework.convoyeur.CvrData;
import framework.convoyeur.CvrField;
import framework.convoyeur.CvrList;
import framework.ressource.Constante;
import framework.ressource.bean.BeanData;
import framework.ressource.bean.BeanParameter;
import framework.ressource.util.UtilHibernate;
import framework.ressource.util.UtilReflect;
import framework.ressource.util.UtilString;
import framework.trace.Trace;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class AdpDatabase {

  public AdpDatabase() {
  }

  public static CvrData execute(BeanDatabase pBeanDB) throws SQLException {
	PreparedStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrData ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;


    try {
		if(szQuery!=null){
			Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:execute: " + szQuery + " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
			con = getConnection();
			stmt = con.prepareStatement(szQuery);
			if ( (vData != null) && (iNbParam > 0) && (iNbParam==vData.size())) {
			  for (int i = 0; i < vData.size(); i++) {
			    BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null :
			        (BeanParameter) vParam.elementAt(i);
			    setData(i + 1, stmt, param, vData.elementAt(i));
			  }
			}
			stmt.execute();
		}
    }
    catch (ClassNotFoundException e) {
      Trace.ERROR(e);
    }
    finally {
      if (res != null)
        res.close();
      if (stmt != null)
        stmt.close();
      if (con != null)
        con.close();
    }

    return ret;
  }

  public static CvrData executeStoredProcedureData(BeanDatabase pBeanDB) throws SQLException {
    CallableStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrData ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;


    try {
		if(szQuery!=null){
			Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeStoredProcedureData: " + szQuery + " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
			con = getConnection();
			stmt = con.prepareCall(szQuery);
			if ( (vData != null) && (iNbParam > 0)) {
			  for (int i = 0; i < vData.size(); i++) {
			    BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null :
			        (BeanParameter) vParam.elementAt(i);
			    setData(i + 1, stmt, param, vData.elementAt(i));
			  }
			  if ( stmt.execute() )
			  {
			    res = stmt.getResultSet();
			    if ( (res!=null)&&(res.next()) )
			      ret = readResultsetStoredProcedureData(res);
			  }
			/*
			        res = stmt.executeQuery();
			        if ( (res!=null)&&(res.next()) )
			          ret = readResultsetStoredProcedureData(res);
			 */
			}
			else if ( (iNbParam==0) && ((vData==null)||(vData.size()==0)) )
			{
			  res = stmt.executeQuery();
			  if (res.next())
			    ret = readResultsetStoredProcedureData(stmt, pBeanDB.getBeanData());
			}
		}
    }
    catch (ClassNotFoundException e) {
      Trace.ERROR(e);
    }
    finally {
      if (res != null)
        res.close();
      if (stmt != null)
        stmt.close();
      if (con != null)
        con.close();
    }

    return ret;
  }

  public static CvrList executeStoredProcedureList(BeanDatabase pBeanDB) throws SQLException {
    CallableStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrList ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
		try {
			Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeStoredProcedureList: " + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
			con = getConnection();
			stmt = con.prepareCall(szQuery);
			if ( (vData != null) && (iNbParam>0))
			{
			  for (int i = 0; i<iNbParam ; i++) {
			    BeanParameter param = ( (vParam==null) || (vParam.size()==0) ) ? null : (BeanParameter)vParam.elementAt(i);
			    Object data = ( (vData!=null) && (i<vData.size()) ) ? vData.elementAt(i) : null;
			    setData(i + 1, stmt, param, data);
			  }
			  if ( stmt.execute() )
			  {
			    res = stmt.getResultSet();
			    ret = readResultsetStoredProcedureList(res);
			  }
			}
			else if ( (iNbParam==0) && ((vData==null)||(vData.size()==0)) )
			{
			  if ( stmt.execute() )
			  {
			    res = stmt.getResultSet();
			    ret = readResultsetStoredProcedureList(stmt, pBeanDB.getBeanData());
			  }
			}
		}
		catch (ClassNotFoundException e) {
		  Trace.ERROR(e);
		}
		finally {
		  if (res != null)
			res.close();
		  if (stmt != null)
			stmt.close();
		  if (con != null)
			con.close();
		}
	}
    return ret;
  }

  public static CvrData executeQueryData(BeanDatabase pBeanDB) throws SQLException {
    PreparedStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrData ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
	    try {
	      Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeQueryList:" + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
	      con = getConnection();
	      stmt = con.prepareStatement(szQuery);
	      if ( (vData != null) && (iNbParam > 0)) {
	        for (int i = 0; ((i<iNbParam) && (i<vData.size())) ; i++) {
	          BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null : (BeanParameter) vParam.elementAt(i);
	          setData(i + 1, stmt, param, vData.elementAt(i));
	        }
	        res = stmt.executeQuery();
	        if ( (res!=null)&&(res.next()) )
	          ret = readResultsetData(res);
	      }
	      else if ( (iNbParam==0) && ((vData==null)||(vData.size()==0)) )
	      {
	        res = stmt.executeQuery();
	        if (res.next())
	          ret = readResultsetData(res);
	      }
	    }
	    catch (ClassNotFoundException e) {
	      Trace.ERROR(e);
	    }
	    finally {
	      if (res != null)
	        res.close();
	      if (stmt != null)
	        stmt.close();
	      if (con != null)
	        con.close();
		}
	}

    return ret;
  }

  public static CvrData executeQueryData(String pQuery) throws SQLException {
    Statement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrData ret = null;

	if(pQuery!=null){
	    try {
	      Trace.DEBUG("AdpObject: executeQueryData:" + pQuery);
	      con = getConnection();
	      stmt = con.createStatement();
	      res = stmt.executeQuery(pQuery);
	      if ( (res!=null)&&(res.next()) )
	        ret = readResultsetData(res);
	    }
	    catch (ClassNotFoundException e) {
	      Trace.ERROR(e);
	    }
	    finally {
	      if (res != null)
	        res.close();
	      if (stmt != null)
	        stmt.close();
	      if (con != null)
	        con.close();
		}
	}
    return ret;
  }

  public static CvrData executeQueryData(String pQuery, Vector pParameter) throws SQLException {
    PreparedStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrData ret = null;

	if(pQuery!=null){
	    Trace.DEBUG("AdpObject: executeQueryData:" + pQuery + " parameter:" + ((pParameter==null) ? "null" : pParameter.toString()));
	    if ( (pParameter != null) && (pParameter.size() > 0)) {
	      try {
	        con = getConnection();
	        stmt = con.prepareStatement(pQuery);
	        for (int i = 0; i < pParameter.size(); i++) {
	          stmt.setObject(i + 1, pParameter.elementAt(i));
	        }
	        res = stmt.executeQuery();
	        if ( (res!=null)&&(res.next()) )
	          ret = readResultsetData(res);
	      }
	      catch (ClassNotFoundException e) {
	        Trace.ERROR(e);
	      }
	      finally {
	        if (res != null)
	          res.close();
	        if (stmt != null)
	          stmt.close();
	        if (con != null)
	          con.close();
	      }
	    }
	    else
	      ret = executeQueryData(pQuery);
	}
    return ret;
  }

  public static CvrList executeQueryList(BeanDatabase pBeanDB) throws SQLException {
    PreparedStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrList ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
	    try {
	      Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeQueryList:" + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
	      con = getConnection();
	      stmt = con.prepareStatement(szQuery);
	      if ( (vData != null) && (iNbParam > 0)) {
	        for (int i = 0; i < vData.size(); i++) {
	          BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null : (BeanParameter) vParam.elementAt(i);
	          setData(i + 1, stmt, param, vData.elementAt(i));
	        }
	        res = stmt.executeQuery();
	        ret = readResultsetList(res);
	      }
	      else if ( (iNbParam==0) && ((vData==null)||(vData.size()==0)) )
	      {
	        res = stmt.executeQuery();
	        ret = readResultsetList(res);
	      }
	    }
	    catch (ClassNotFoundException e) {
	      Trace.ERROR(e);
	    }
	    finally {
	      if (res != null)
	        res.close();
	      if (stmt != null)
	        stmt.close();
	      if (con != null)
	        con.close();
		}
	}

    return ret;
  }

  public static CvrList executeQueryList(String pQuery) throws SQLException {
    Statement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrList ret = null;

	if(pQuery!=null){
	    Trace.DEBUG("AdpObject: executeQueryList:" + pQuery);
	    try {
	      con = getConnection();
	      stmt = con.createStatement();
	      res = stmt.executeQuery(pQuery);
	      ret = readResultsetList(res);
	    }
	    catch (ClassNotFoundException e) {
	      Trace.ERROR(e);
	    }
	    finally {
	      if (res != null)
	        res.close();
	      if (stmt != null)
	        stmt.close();
	      if (con != null)
	        con.close();
		}
	}

    return ret;
  }

  public static CvrList executeQueryList(String pQuery, Vector pParameter) throws SQLException {
    PreparedStatement stmt = null;
    Connection con = null;
    ResultSet res = null;
    CvrList ret = null;

	if(pQuery!=null){
	    Trace.DEBUG("AdpObject: executeQueryList:" + pQuery + " parameter:" + ((pParameter==null) ? "null" : pParameter.toString()));
	    if ( (pParameter != null) && (pParameter.size() > 0)) {
	      try {
	        con = getConnection();
	        stmt = con.prepareStatement(pQuery);
	        if (pParameter != null) {
	          for (int i = 0; i < pParameter.size(); i++) {
	            stmt.setObject(i + 1, pParameter.elementAt(i));
	          }
	        }
	        res = stmt.executeQuery();
	        ret = readResultsetList(res);
	      }
	      catch (ClassNotFoundException e) {
	        Trace.ERROR(e);
	      }
	      finally {
	        if (res != null)
	          res.close();
	        if (stmt != null)
	          stmt.close();
	        if (con != null)
	          con.close();
	      }
		}
	}

    return ret;
  }

  public static int executeUpdate(BeanDatabase pBeanDB) throws SQLException {
    CallableStatement stmt = null;
    Connection con = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int ret = 0;
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
	    Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeUpdate:" + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
	    try
	    {
	      con = getConnection();
	      stmt = con.prepareCall(szQuery);
	      if ( (vData != null) && (iNbParam > 0))
	      {
	        for (int i = 0; i < vData.size(); i++)
	        {
	          BeanParameter param = (vParam == null) ? null : (BeanParameter) vParam.elementAt(i);
	          setData(i + 1, stmt, param, vData.elementAt(i));
	        }
	      }
	      ret = stmt.executeUpdate();
	    }
	    catch (ClassNotFoundException e) {
	      Trace.ERROR(e);
	    }
	    finally {
	      if (stmt != null)
	        stmt.close();
	      if (con != null)
	        con.close();
		}
	}
    return ret;
  }

  public static int executeUpdate(String pQuery, Vector pParameter) throws SQLException {
    PreparedStatement stmt = null;
    Connection con = null;
    int ret = 0;

    Trace.DEBUG("AdpObject: executeUpdate:" + pQuery + " parameter:" + ((pParameter==null) ? "" : pParameter.toString()));

	if(pQuery!=null){
	    if ( (pParameter != null) && (pParameter.size() > 0)) {
	      try {
	        con = getConnection();
	        stmt = con.prepareStatement(pQuery);
	        for (int i = 0; i < pParameter.size(); i++) {
	          stmt.setObject(i + 1, pParameter.elementAt(i));
	        }
	        ret = stmt.executeUpdate();
	      }
	      catch (ClassNotFoundException e) {
	        Trace.ERROR(e);
	      }
	      finally {
	        if (stmt != null)
	          stmt.close();
	        if (con != null)
	          con.close();
	      }
		}
	}
    return ret;
  }

  protected static void setData(int index,CallableStatement stmt, BeanParameter bean, Object data) throws SQLException {
    if (bean != null) {
      if (BeanParameter.CST_PUT_OUT.equals(bean.getInOutPut())) {
        stmt.registerOutParameter(index, bean.getSqlType());
      }
      else {
        // Initialise les donnees avec la valeur par defaut si besoin est
        if ( (data==null)&&(bean.getDefaultData()!=null)&&(!"".equals(bean.getDefaultData())) )
          data = bean.getDefaultData();

        if (bean.getType() == null) {
          if (data == null)
            stmt.setNull(index, bean.getSqlType());
          else
            stmt.setObject(index, data);
        }
        else {
          if (data == null)
            stmt.setNull(index, bean.getSqlType());
          else
          {
            int iType = bean.getSqlType();
            switch( iType )
            {
              case java.sql.Types.INTEGER:
              case java.sql.Types.BIT:
                stmt.setObject(index, new Integer(data.toString()), iType);
                break;

              case java.sql.Types.REF:
              case java.sql.Types.VARCHAR:
              default:
                stmt.setObject(index, data, iType);
            }
          }
        }
      }
    }
    else if (data != null) {
      stmt.setObject(index, data);
    }
  }

  public static void executeHql(BeanDatabase pBeanDB) throws SQLException {
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
	    try {
	      Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeHqlList:" + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
			Session session = UtilHibernate.openSession();
			Query query = session.createQuery(szQuery);
			if ( (vData != null) && (iNbParam > 0)) {
				for (int i = 0; i < vData.size(); i++) {
					BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null : (BeanParameter) vParam.elementAt(i);
					setData(i + 1, query, param, vData.elementAt(i));
				}
			}
			query.executeUpdate();
	    }
	    finally {
			UtilHibernate.closeSession();
		}
	}
  }

  public static CvrList executeHqlList(BeanDatabase pBeanDB) throws SQLException {
    CvrList ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
	    try {
	      Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeHqlList:" + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
			Session session = UtilHibernate.openSession();
			Query query = session.createQuery(szQuery);
			if ( (vData != null) && (iNbParam > 0)) {
				for (int i = 0; i < vData.size(); i++) {
					BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null : (BeanParameter) vParam.elementAt(i);
					setData(i + 1, query, param, vData.elementAt(i));
				}
			}
			ret = new CvrList(query.list());
	    }
	    finally {
			UtilHibernate.closeSession();
		}
	}

    return ret;
  }

  public static Object executeHqlData(BeanDatabase pBeanDB) throws SQLException {
	  Object ret = null;
    String szQuery = pBeanDB.getQuery();
    Vector vData = pBeanDB.getParameterData();
    Vector vParam = pBeanDB.getParameter();
    int iNbParam = ( (pBeanDB.getBeanData()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery()!=null)&&
                     (pBeanDB.getBeanData().getBeanQuery().getParameterCount()!=null) ) ?
        pBeanDB.getBeanData().getBeanQuery().getParameterCount().intValue() : 0;

	if(szQuery!=null){
	    try {
	      Trace.DEBUG("CLASS:framework.adaptator.AdpDatabase MSG:executeHqlData:" + szQuery +  " parameter:" + ( (vParam == null) ? "null" : vParam.toString()) + " data:" + ( (vData == null) ? "null" : vData.toString()));
			Session session = UtilHibernate.openSession();
			Query query = session.createQuery(szQuery);
			if ( (vData != null) && (iNbParam > 0)) {
				for (int i = 0; i < vData.size(); i++) {
					BeanParameter param = ( (vParam == null) || (vParam.size() == 0)) ? null : (BeanParameter) vParam.elementAt(i);
					setData(i + 1, query, param, vData.elementAt(i));
				}
			}
			ret = query.uniqueResult();
	    }
	    finally {
			UtilHibernate.closeSession();
		}
	}

    return ret;
  }

  protected static void setData(int index, PreparedStatement stmt, BeanParameter bean, Object data) throws SQLException {
    if (bean != null) {
      if (bean.getType() == null) {
        if (data == null)
          stmt.setNull(index, bean.getSqlType());
        else
          stmt.setObject(index, data);
      }
      else {
        if (data == null)
          stmt.setNull(index, bean.getSqlType());
        else
          stmt.setObject(index, data, bean.getSqlType());
      }
    }
    else if (data != null) {
      stmt.setObject(index, data);
    }
  }

  protected static void setData(int index, Query query, BeanParameter bean, Object data) throws SQLException {
    if (bean != null) {
      if (UtilString.isNotEmpty(bean.getType()) && UtilString.isNotEmpty(bean.getClassName())) {
  		try {
			Class cls = UtilReflect.loadClass(bean.getClassName());
			if (cls!=null) {
				Type type = (Type)UtilReflect.safeInvokeField(cls, bean.getType());
				query.setParameter(bean.getName(), data, type);
				query.setParameter(bean.getName(), data);
			}
		} catch (ClassNotFoundException e) {
			Trace.ERROR(e, e);
		} 
      }
      else {
     	query.setParameter(bean.getName(), data);
      }
    }
    else if (data != null) {
       	query.setParameter(index, data);
    }
  }

  protected static CvrData readResultsetData(ResultSet res) throws SQLException {
    CvrData ret = new CvrData();
    ResultSetMetaData rsmd = res.getMetaData();
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      String szName = rsmd.getColumnLabel(i);
      CvrField field = new CvrField();
      field.setName(szName);
      field.setData(res.getObject(i));
      field.setType(rsmd.getColumnType(i));
      field.setTypeName(rsmd.getColumnTypeName(i));
      ret.set(szName, field);
    }
    return ret;
  }

  protected static CvrList readResultsetList(ResultSet res) throws SQLException {
    CvrList ret = null;
    while (res.next()) {
      if (ret == null)
        ret = new CvrList();
      ret.add(readResultsetData(res));
    }
    return ret;
  }

  protected static CvrData readResultsetStoredProcedureData(ResultSet res) throws SQLException {
    CvrData ret = new CvrData();
    ResultSetMetaData rsmd = res.getMetaData();
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      String szName = rsmd.getColumnLabel(i);
      CvrField field = new CvrField();
      field.setName(szName);
      field.setData(res.getObject(i));
      field.setType(rsmd.getColumnType(i));
      field.setTypeName(rsmd.getColumnTypeName(i));
      ret.set(szName, field);
    }
    return ret;
  }

  protected static CvrList readResultsetStoredProcedureList(ResultSet res) throws SQLException {
    CvrList ret = null;
    if ( res!=null )
    {
      while (res.next()) {
        if (ret == null)
          ret = new CvrList();
        ret.add(readResultsetStoredProcedureData(res));
      }
    }
    return ret;
  }

  protected static CvrList readResultsetStoredProcedureList(CallableStatement stmt, BeanData beanData) throws
      SQLException {
    CvrList ret = null;
    Vector listParam = beanData.getParameter();
    for (int i=0; i < listParam.size(); i++) {
      BeanParameter param = (BeanParameter) listParam.elementAt(i);
      if (param.equals(BeanParameter.CST_PUT_OUT)) {
        Object obj = stmt.getObject(param.getIndex().intValue());
        if (obj != null) {
          int j = 0;
          j++;
        }
      }
    }
    return ret;
  }

  protected static CvrData readResultsetStoredProcedureData(CallableStatement stmt, BeanData beanData) throws SQLException {
    CvrData ret = new CvrData();
    Vector listParam = beanData.getParameter();
    for (int i=0; i < listParam.size(); i++) {
      BeanParameter param = (BeanParameter)listParam.elementAt(i);
      if (param.equals(BeanParameter.CST_PUT_OUT))
      {
        Object obj = stmt.getObject(param.getIndex().intValue());
        if ( obj!=null )
        {
          int j=0;
          j++;
/*
          CvrField field = new CvrField();
          field.setName(szName);
          field.setData(res.getObject(i));
          field.setType(rsmd.getColumnType(i));
          field.setTypeName(rsmd.getColumnTypeName(i));
          ret.set(szName, field);
*/
        }
      }
    }
    return ret;
  }
  protected static Connection getConnection() throws SQLException, ClassNotFoundException {
//      framework.ressource.util.UtilReflect.loadClass(Constante.DB_CONNECT_DRIVER);
    Class.forName(Constante.DB_CONNECT_DRIVER);
    return DriverManager.getConnection(Constante.DB_CONNECT_URL +
                                      Constante.DB_CONNECT_FILE,
                                      Constante.DB_CONNECT_USER,
                                      Constante.DB_CONNECT_PASSWORD);
  }
}
