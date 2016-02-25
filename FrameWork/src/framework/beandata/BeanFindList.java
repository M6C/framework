package framework.beandata;

import framework.convoyeur.CvrData;
import framework.convoyeur.CvrField;
import framework.convoyeur.CvrList;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author  unascribed
 * @version  1.0
 */

public class BeanFindList extends BeanDatabase implements Collection {

  public static final String CST_CRITERE_PREFIX = "crt_";

  private CvrList list = null;

  public BeanFindList() {
  }

  public void loadParameter(HttpServletRequest request)
  {
//    Trace.DEBUG(this, "loadParameter START");
    Vector lstParam = null;
    if ( (getParameter()!=null)&&(getParameter().size()>0) )
      lstParam = loadBeanParameter(request);
    else if ( getName()!=null )
    {
      String szBeanName = CST_CRITERE_PREFIX + getName();
      lstParam = loadBeanParameter(request, szBeanName);
    }
      setParameterData(lstParam);
//    Trace.DEBUG(this, "loadParameter END");
  }

  /**
 * @return  the list
 * @uml.property  name="list"
 */
public CvrList getList() {
    return list;
  }
  /**
 * @param list  the list to set
 * @uml.property  name="list"
 */
public void setList(CvrList list) {
    this.list = list;
  }
  public Object get(Object pKey)
  {
    Object ret = null;
    if ((pKey!=null)&&(getList()!=null)&&
        (getIndex().intValue()<getList().size())) {
      // Récupere l'element de la liste
      ret = getList().get(getIndex().intValue());
      if (ret!=null) {
        // Récupere le champs
        ret = ((CvrData)ret).get(pKey);
        if (ret != null)
          // Récupere les données
          ret = ((CvrField)ret).getData();
      }
    }
    return ret;
  }
  public boolean hasMoreElements() {
    return ((getList()!=null)&&(getIndex()!=null)&&
            (getIndex().intValue()<getList().size()));
  }
  public Integer getSize() {
    return new Integer((getList()==null) ? 0 : getList().size());
  }

	public boolean add(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().addAll(c);
	}
	
	public void clear() {
		if (getList()!=null)
			getList().clear();
	}
	
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().contains(o);
	}
	
	public boolean containsAll(Collection c) {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().containsAll(c);
	}
	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().isEmpty();
	}
	
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return (getList()==null) ? null : getList().iterator();
	}
	
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().remove(o);
	}
	
	public boolean removeAll(Collection c) {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().removeAll(c);
	}
	
	public boolean retainAll(Collection c) {
		// TODO Auto-generated method stub
		return (getList()==null) ? false : getList().retainAll(c);
	}
	
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return (getList()==null) ? null : getList().toArray();
	}
	
	public Object[] toArray(Object[] a) {
		// TODO Auto-generated method stub
		return (getList()==null) ? null : getList().toArray(a);
	}

	public int size() {
		// TODO Auto-generated method stub
		return getSize().intValue();
	}
}
