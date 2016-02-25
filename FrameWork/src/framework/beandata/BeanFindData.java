package framework.beandata;

import framework.convoyeur.CvrData;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  not attributable
 * @version  1.0
 */

public class BeanFindData extends BeanFindList {

  private CvrData data = null;

  public BeanFindData() {
  }

  /**
 * @return  the data
 * @uml.property  name="data"
 */
public CvrData getData() {
    return data;
  }
  /**
 * @param data  the data to set
 * @uml.property  name="data"
 */
public void setData(CvrData data) {
    this.data = data;
  }
  public Object get(Object pKey)
  {
    return ((pKey==null)||(getData()==null)) ? null : getData().get(pKey);
  }
  public Integer getSize() {
    return new Integer(((getData()==null)||(getData().getList()==null)) ? 0 : getData().getList().size());
  }
  public boolean hasMoreElements() {
    return ((getData()!=null)&&(getData().getList()!=null)&&(getIndex()!=null)&&
            (getIndex().intValue()<getData().getList().size()));
  }
}
