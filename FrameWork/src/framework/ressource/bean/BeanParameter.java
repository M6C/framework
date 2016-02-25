package framework.ressource.bean;

import java.io.Serializable;


/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  not attributable
 * @version  1.0
 */

public class BeanParameter implements Serializable {

  private static final long serialVersionUID = -982343435942299865L;

  public static final String CST_SQL_TYPE_STRING = "STRING";
  public static final String CST_SQL_TYPE_VARCHAR = "VARCHAR";
  public static final String CST_SQL_TYPE_INT = "INT";
  public static final String CST_SQL_TYPE_INTEGER = "INTEGER";
  public static final String CST_SQL_TYPE_FLAG = "FLAG";
  public static final String CST_SQL_TYPE_CURSOR = "CURSOR";

  public static final String CST_PUT_IN = "IN";
  public static final String CST_PUT_OUT = "OUT";

  private String name = null;
  private String bean = null;
  private Integer index = null;
  private String type = null;
  private String inOutPut = null;
  private String className = null;
  private String defaultData = null;
  private String synchronize = null;

  public BeanParameter() {
  }

  public BeanParameter(String name, Integer index, String type, String inOutPut) {
    setName(name);
    setIndex(index);
    setType(type);
    setInOutPut(inOutPut);
  }

  public int getSqlType() {
    int ret = java.sql.Types.NULL;
    if ( (getType() != null) && (!"".equals(getType()))) {
      if (getType().equalsIgnoreCase(CST_SQL_TYPE_STRING))
          ret = java.sql.Types.VARCHAR;
      else if (getType().equalsIgnoreCase(CST_SQL_TYPE_VARCHAR))
          ret = java.sql.Types.VARCHAR;
      else if (getType().equalsIgnoreCase(CST_SQL_TYPE_INT))
          ret = java.sql.Types.INTEGER;
      else if (getType().equalsIgnoreCase(CST_SQL_TYPE_INTEGER))
          ret = java.sql.Types.INTEGER;
      else if (getType().equalsIgnoreCase(CST_SQL_TYPE_FLAG))
        ret = java.sql.Types.BIT;
      else if (getType().equalsIgnoreCase(CST_SQL_TYPE_CURSOR))
        ret = java.sql.Types.REF;
    }
    return ret;
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

  /**
 * @return  the type
 * @uml.property  name="type"
 */
public String getType() {
    return type;
  }

  /**
 * @param type  the type to set
 * @uml.property  name="type"
 */
public void setType(String type) {
    this.type = type;
  }

  /**
 * @return  the inOutPut
 * @uml.property  name="inOutPut"
 */
public String getInOutPut() {
    return inOutPut;
  }

  /**
 * @param inOutPut  the inOutPut to set
 * @uml.property  name="inOutPut"
 */
public void setInOutPut(String inOutPut) {
    this.inOutPut = inOutPut;
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
 * @param bean  the bean to set
 * @uml.property  name="bean"
 */
public void setBean(String bean) {
    this.bean = bean;
  }
  /**
 * @return  the bean
 * @uml.property  name="bean"
 */
public String getBean() {
    return bean;
  }
  /**
 * @return  the defaultData
 * @uml.property  name="defaultData"
 */
public String getDefaultData() {
	    return defaultData;
	  }
  /**
 * @param defaultData  the defaultData to set
 * @uml.property  name="defaultData"
 */
public void setDefaultData(String defaultData) {
    this.defaultData = defaultData;
  }
  /**
 * @return  the synchronize
 * @uml.property  name="synchronize"
 */
public String getSynchronize() {
    return synchronize;
  }
  /**
 * @param synchronize  the synchronize to set
 * @uml.property  name="synchronize"
 */
public void setSynchronize(String synchronize) {
    this.synchronize = synchronize;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object.
   * @todo Implement this java.lang.Object method
   */
  public String toString() {
    return ((getName()==null) ? "null" : getName());
  }
}
