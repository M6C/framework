package framework.convoyeur;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  unascribed
 * @version  1.0
 */

public class CvrData implements Serializable {

  Hashtable list = new Hashtable();

  public CvrData() {
  }

  public CvrData(Object pKey, Object pValue) {
	set(pKey, pValue);
  }

  public Object get(Object pKey)
  {
    return (pKey==null) ? null : getList().get(pKey);
  }

  public CvrField getField(Object pKey)
  {
    return (CvrField)get(pKey);
  }

  public String getFieldValue(Object pKey)
  {
    String ret = "";
    CvrField cvrField  = (CvrField)get(pKey);
    if ( (cvrField!=null) && (cvrField.getData()!=null) )
      ret = cvrField.getData().toString();
    return ret;
  }

  public void set(Object pKey, Object pValue)
  {
    // Supprime l'ancienne valeur
    if ( getList().containsKey(pKey) )
      getList().remove(pKey);
    // Ajout l'Objet
    getList().put(pKey, pValue);
  }

  /**
 * @return  the list
 * @uml.property  name="list"
 */
public Hashtable getList() {
    return list;
  }
  /**
 * @param list  the list to set
 * @uml.property  name="list"
 */
public void setList(Hashtable list) {
    this.list = list;
  }
  public Vector getVectorFieldData()
  {
    Vector ret = new Vector();
    for (Enumeration e = getList().elements() ; e.hasMoreElements() ;)
    {
      Object obj = e.nextElement();
      if ( obj instanceof CvrField )
      {
        ret.addElement(((CvrField)obj).getData());
      }
    }
    return ret;
  }

  public int getColumnCount()
  {
    int ret = 0;
    if ( getList() != null )
        ret = getList().size();
    return ret;
  }

  public String toString()
  {
    StringBuffer ret = new StringBuffer();
    Enumeration eKey = getList().keys();
    Enumeration eVal = getList().elements();
    while (eKey.hasMoreElements() && eVal.hasMoreElements())
    {
      Object key = eKey.nextElement();
      Object val = eVal.nextElement();
      ret.append("\r\n->KEY ");
      ret.append((key!=null) ? key.toString() : "").append(" :\r\n");
      ret.append("DATA :\r\n");
      ret.append((val!=null) ? val.toString() : "");
      ret.append("------\r\n\r\n");
    }
    return ret.toString();
  }
}
