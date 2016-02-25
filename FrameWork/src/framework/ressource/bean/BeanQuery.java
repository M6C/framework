package framework.ressource.bean;

import java.io.Serializable;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  non attribuable
 * @version  1.0
 */

public class BeanQuery implements Serializable {

  private static final long serialVersionUID = 3882946606653058174L;

  public final static String CST_QUERY = "QUERY";
  public final static String CST_EXECUTE = "EXECUTE";
  public final static String CST_STORED_PROCEDURE = "STORED_PROCEDURE";
  public final static String CST_HIBERNATE = "HIBERNATE";

  private String name = null;
  private Integer parameterCount = null;
  private String query = null;
  private String type = null;

  public BeanQuery() {
  }
  /**
 * @return  the parameterCount
 * @uml.property  name="parameterCount"
 */
public Integer getParameterCount() {
    return parameterCount;
  }
  /**
 * @param parameterCount  the parameterCount to set
 * @uml.property  name="parameterCount"
 */
public void setParameterCount(Integer parameterCount) {
    this.parameterCount = parameterCount;
  }
  /**
 * @return  the query
 * @uml.property  name="query"
 */
public String getQuery() {
    return query;
  }
  /**
 * @param query  the query to set
 * @uml.property  name="query"
 */
public void setQuery(String query) {
    this.query = query;
  }
  /**
 * @return  the type
 * @uml.property  name="type"
 */
public String getType() {
    return type;
  }

  /**
 * @return  the name
 * @uml.property  name="name"
 */
public String getName() {
    return name;
  }

  /**
 * @param type  the type to set
 * @uml.property  name="type"
 */
public void setType(String type) {
    this.type = type;
  }

  /**
 * @param name  the name to set
 * @uml.property  name="name"
 */
public void setName(String name) {
    this.name = name;
  }
}
