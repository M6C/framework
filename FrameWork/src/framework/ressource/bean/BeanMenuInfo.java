package framework.ressource.bean;

import java.io.Serializable;


/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  not attributable
 * @version  1.0
 */

public class BeanMenuInfo implements Serializable {

  private static final long serialVersionUID = -1667880019627703706L;

  private String name = null;
  private String text = null;
  private String target = null;
  private Integer level = null;
  private String styleTD = null;
  private String styleA = null;
  private String styleFONT = null;

  public BeanMenuInfo() {
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
 * @return  the text
 * @uml.property  name="text"
 */
public String getText() {
    return text;
  }
  /**
 * @param text  the text to set
 * @uml.property  name="text"
 */
public void setText(String text) {
    this.text = text;
  }
  /**
 * @return  the level
 * @uml.property  name="level"
 */
public Integer getLevel() {
    return level;
  }

  /**
 * @return  the styleTD
 * @uml.property  name="styleTD"
 */
public String getStyleTD() {
    return styleTD;
  }

  /**
 * @return  the styleA
 * @uml.property  name="styleA"
 */
public String getStyleA() {
    return styleA;
  }

  /**
 * @return  the styleFONT
 * @uml.property  name="styleFONT"
 */
public String getStyleFONT() {
    return styleFONT;
  }

  /**
 * @param level  the level to set
 * @uml.property  name="level"
 */
public void setLevel(Integer level) {
    this.level = level;
  }

  /**
 * @param styleTD  the styleTD to set
 * @uml.property  name="styleTD"
 */
public void setStyleTD(String styleTD) {
    this.styleTD = styleTD;
  }

  /**
 * @param styleA  the styleA to set
 * @uml.property  name="styleA"
 */
public void setStyleA(String styleA) {
    this.styleA = styleA;
  }

  /**
 * @param styleFONT  the styleFONT to set
 * @uml.property  name="styleFONT"
 */
public void setStyleFONT(String styleFONT) {
    this.styleFONT = styleFONT;
  }
}
