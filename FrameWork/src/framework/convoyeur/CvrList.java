package framework.convoyeur;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  unascribed
 * @version  1.0
 */

public class CvrList implements Serializable, Collection {

  Vector list = new Vector();

  public CvrList() {
  }

  public CvrList(Collection list) {
	  this.list.addAll(list);
  }
  /**
 * @return  the list
 * @uml.property  name="list"
 */
public Vector getList() {
    return list;
  }
  /**
 * @param list  the list to set
 * @uml.property  name="list"
 */
public void setList(Vector list) {
    this.list = list;
  }
  public boolean add(Object pObj) {
    getList().addElement(pObj);
    return true;
  }

  public void add(Object pKey, Object pValue)
  {
    if( pKey instanceof Vector )
    {
      if ( pValue instanceof Vector )
      {
        for(int i=0 ; i<((Vector)pKey).size() ; i++ )
        {
          Object key = ((Vector)pKey).elementAt(i);
          for(int j=0 ; j<((Vector)pValue).size() ; j++ ) {
			add(new CvrData(key, ((Vector)pValue).elementAt(j)));
          }
        }
      }
      else
      {
        for(int i=0 ; i<((Vector)pKey).size() ; i++ ) {
			add(new CvrData(((Vector)pKey).elementAt(i), pValue));
        }
      }
    }
    else {
      if ( pValue instanceof Vector ) {
        for(int j=0 ; j<((Vector)pValue).size() ; j++ ) {
			add(new CvrData(pKey, ((Vector)pValue).elementAt(j)));
        }
      }
      else {
		add(new CvrData(pKey, pValue));
      }
    }
  }
  public Object remove(int i)
  {
    return getList().remove(i);
  }
  public Object get(int i)
  {
    return getList().get(i);
  }
  public Vector getVectorFieldData()
  {
    Vector ret = new Vector();
    for( int i=0 ; i<getList().size() ; i++ )
    {
      Object obj = getList().elementAt(i);
      if ( obj instanceof CvrData )
      {
        ret.addElement(((CvrData)obj).getVectorFieldData());
      }
    }
    return ret;
  }
  public int getColumnCount()
  {
    int ret = 0;
    if ( getList() != null )
    {
      Object item = getList().elementAt(0);
      if ( (item!=null)&&(item instanceof CvrData) )
        ret = ((CvrData)item).getColumnCount();
    }
    return ret;
  }
  public int getRowCount()
  {
    return (getList()==null) ? 0 : getList().size();
  }
  public int size()
  {
    return (getList()==null) ? 0 : getList().size();
  }

	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return getList().addAll(c);
	}
	
	public void clear() {
		getList().clear();
	}
	
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return getList().contains(o);
	}
	
	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return getList().containsAll(c);
	}
	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return getList().isEmpty();
	}
	
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return getList().iterator();
	}
	
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return getList().remove(o);
	}
	
	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return getList().removeAll(c);
	}
	
	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return getList().retainAll(c);
	}
	
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return getList().toArray();
	}
	
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return getList().toArray(a);
	}
}
