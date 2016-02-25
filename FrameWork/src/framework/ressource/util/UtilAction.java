package framework.ressource.util;

import java.util.Enumeration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import framework.beandata.BeanGenerique;
import framework.ressource.bean.BeanData;
import framework.service.SrvGenerique;
import framework.trace.Trace;

public class UtilAction {

	public static BeanGenerique newBean(BeanData beanData) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		BeanGenerique ret = null;
		// Récupération du ClassLoader actuel à la place du Class.forName
		// pour une meilleur gestion de la mémoire
		ClassLoader classloader = framework.ressource.util.UtilReflect.getContextClassLoader();//this.getClass().getClassLoader();
		String beanClass = ((beanData.getClassName()!=null)&&(!beanData.getClassName().equals(""))) ? beanData.getClassName() : "framework.beandata.BeanGenerique";
		// Récupération du bean
		Class classe = classloader.loadClass(beanClass);
		// Instanciation du bean
		ret = (BeanGenerique) classe.newInstance();
		// initialise le bean avec c'est informations
		ret.setBeanData(beanData);
		return ret;
	}

	//public static void executeService(HttpServletRequest request, HttpServletResponse response, BeanData beanData, BeanGenerique bean) throws Exception {
	public static void executeService(HttpServletRequest request, HttpServletResponse response, BeanData beanData, BeanGenerique bean) throws InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
        // Chargement du Bean de donnée avec les paramètres de la requette
        bean.loadParameter(request);
		// Execution du service ( Chargement du Bean )
		newService(beanData).execute(request, response, bean);
	}

	//public static void executeService(HttpServletRequest request, HttpServletResponse response, BeanData beanData, BeanGenerique bean) throws Exception {
	public static void executeService(ServletRequest request, ServletResponse response, BeanData beanData, BeanGenerique bean) throws InstantiationException, IllegalAccessException, ClassNotFoundException, Exception {
        // Chargement du Bean de donnée avec les paramètres de la requette
        bean.loadParameter(request);
		// Execution du service ( Chargement du Bean )
		newService(beanData).execute(request, response, bean);
	}

	public static SrvGenerique newService(BeanData beanData) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		SrvGenerique ret = null;
		String serviceName = beanData.getService();
		if ((serviceName != null) && (!"".equals(serviceName))) {
			// Récupération du ClassLoader actuel à la place du Class.forName
			// pour une meilleur gestion de la mémoire
			ClassLoader classloader = framework.ressource.util.UtilReflect.getContextClassLoader(); //this.getClass().getClassLoader();
			// Récupération du bean
			Class classe = classloader.loadClass(serviceName);
			// Instanciation du bean
			ret = (SrvGenerique) classe.newInstance();
		}
		return ret;
	}
}
