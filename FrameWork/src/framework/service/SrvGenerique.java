/*
 * Cr�� le 26 juil. 2004
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package framework.service;

import framework.beandata.BeanGenerique;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author  rocada  Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :  Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class SrvGenerique {

	protected static SrvGenerique singleton = null;

	public void execute(HttpServletRequest request, HttpServletResponse response, BeanGenerique bean) throws Exception{
		execute((ServletRequest)request, (ServletResponse)response, bean);
	}

	public void execute(ServletRequest request, ServletResponse response, BeanGenerique bean) throws Exception {
	}

	/**
	 * @return  the singleton
	 * @uml.property  name="singleton"
	 */
	public static SrvGenerique getSingleton() {
		if (singleton==null)
			singleton = new SrvGenerique();
	  return singleton;
	}
}
