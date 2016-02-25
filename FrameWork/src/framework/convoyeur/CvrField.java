package framework.convoyeur;

import java.io.Serializable;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  unascribed
 * @version  1.0
 */

public class CvrField implements Serializable {

  private String name;
  private int type;
  private Object data;
  private String typeName;
  public CvrField() {
  }
  /**
 * @return  the typeName
 * @uml.property  name="typeName"
 */
public String getTypeName() {
    return typeName;
  }

  public String toString()
  {
    StringBuffer ret = new StringBuffer();
    ret.append("NAME: ").append((getName()!=null) ? getName() : "").append("\r\n");
    ret.append("DATA: ").append((getData()!=null) ? getData() : "").append("\r\n");
    ret.append("TYPE: ").append(getType()).append("\r\n");
    ret.append("TYPE NAME: ").append((getTypeName()!=null) ? getTypeName() : "").append("\r\n");
    return ret.toString();
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
 * @param data  the data to set
 * @uml.property  name="data"
 */
public void setData(Object data) {
    this.data = data;
  }
  /**
 * @return  the data
 * @uml.property  name="data"
 */
public Object getData() {
    return data;
  }
  /**
 * @param type  the type to set
 * @uml.property  name="type"
 */
public void setType(int type) {
    this.type = type;
  }
  /**
 * @return  the type
 * @uml.property  name="type"
 */
public int getType() {
    return type;
  }
  /**
 * @param typeName  the typeName to set
 * @uml.property  name="typeName"
 */
public void setTypeName(String typeName) {
    this.typeName = typeName;
  }
}
