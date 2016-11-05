package nl.strohalm.cyclos;

import java.util.Calendar;
import nl.strohalm.cyclos.dao.ApplicationDAO;
import nl.strohalm.cyclos.dao.ApplicationDAOImpl;
import nl.strohalm.cyclos.entities.Application;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author henry
 */
public class testMain {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSession();
        session.close();

        ApplicationDAO appDAO = new ApplicationDAOImpl();
        System.out.println(appDAO.read().getVersion());

        System.out.println("Fin de la prueba");

        //System.exit(1);
    }
}
