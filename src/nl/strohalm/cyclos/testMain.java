package nl.strohalm.cyclos;

import nl.strohalm.cyclos.dao.ApplicationDAO;
import nl.strohalm.cyclos.dao.ApplicationDAOImpl;

/**
 *
 * @author henry
 */
public class testMain {

    public static void main(String[] args) {

        ApplicationDAO appDAO = new ApplicationDAOImpl();
        System.out.println(appDAO.read().getVersion());

        System.out.println("Fin de la prueba");

        //System.exit(1);
    }
}
