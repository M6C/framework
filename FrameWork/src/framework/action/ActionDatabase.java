package framework.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import framework.beandata.BeanDatabase;
import framework.beandata.BeanGenerique;
import framework.ressource.bean.BeanData;
import framework.service.SrvDatabase;
import framework.service.SrvGenerique;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ActionDatabase extends ActionGenerique {

	public void executeService(HttpServletRequest request, HttpServletResponse response, BeanData beanData, BeanGenerique bean) throws Exception{
		// Execution du service ( Chargement du Bean )
		SrvGenerique service = newService(beanData);
		if ((service instanceof SrvDatabase)&&(bean instanceof BeanDatabase)){
			((SrvDatabase)service).executeBeforeQuery(request, response, (BeanDatabase)bean);
			service.execute(request, response, bean);
			((SrvDatabase)service).executeAfterQuery(request, response, (BeanDatabase)bean);
		}
		else
			service.execute(request, response, bean);
	}
}
