package framework.ressource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class FrmWrkMsg {

  // Messages d'erreur géneral
  public static final String ERR_INIT_BEAN = "Erreur d'initialisation du Bean";

  // Messages d'erreur du popup film
  public static final String ERR_POPUP_FILM_INTROUVABLE = "Le film séléctionné est introuvable";

  // Messages d'erreur pour la convertion de pages Html en Jsp
  public static final String ERR_CONVERTER_HTML_TO_JSP_CHEMIN_OBLIGATOIRE = "Les chemins doivent être renseigné";
  public static final String ERR_CONVERTER_HTML_TO_JSP_CHEMIN_INEXISTANT = "Les chemins n'existent pas";
  public static final String ERR_CONVERTER_HTML_TO_JSP_CHEMIN_INCOHERENT = "Les chemins sont incoherent";

  public FrmWrkMsg() {
  }
  public static String getMessage(String idMsg) {
    return idMsg;
  }
  public static String getMessage(int idMsg, int idLanguage)
  {
    String ret = "";
    switch (idMsg)
    {
      case 1:
        ret = "Erreur d'initialisation du Bean";
        break;
    }
    return ret;
  }
}
