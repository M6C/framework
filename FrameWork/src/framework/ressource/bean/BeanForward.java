package framework.ressource.bean;

import java.io.Serializable;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  non attribuable
 * @version  1.0
 */

public class BeanForward implements Serializable {

  private static final long serialVersionUID = 1325335520888095011L;

  private String name = null;
  private String condition;
  private String target;
  private String ReplaceNullBy;

  public BeanForward() {
  }
  /**
 * @return  the name
 * @uml.property  name="name"
 */
public String getName() {
    return name;
  }

  /**
 * @return  the target
 * @uml.property  name="target"
 */
public String getTarget() {
    return target;
  }

  /**
 * @return  the condition
 * @uml.property  name="condition"
 */
public String getCondition() {
    return condition;
  }

  /**
 * @return  the replaceNullBy
 * @uml.property  name="replaceNullBy"
 */
public String getReplaceNullBy() {
    return ReplaceNullBy;
  }

  /**
 * @param name  the name to set
 * @uml.property  name="name"
 */
public void setName(String name) {
    this.name = name;
  }

  /**
 * @param target  the target to set
 * @uml.property  name="target"
 */
public void setTarget(String target) {
    this.target = target;
  }

  /**
 * @param condition  the condition to set
 * @uml.property  name="condition"
 */
public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
 * @param replaceNullBy  the replaceNullBy to set
 * @uml.property  name="replaceNullBy"
 */
public void setReplaceNullBy(String ReplaceNullBy) {
    this.ReplaceNullBy = ReplaceNullBy;
  }
}
