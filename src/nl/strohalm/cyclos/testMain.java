package nl.strohalm.cyclos;

import org.hibernate.Session;

/**
 *
 * @author henry
 */
public class testMain {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSession();
        session.close();

        System.out.println("Fin de la prueba");

        //System.exit(1);
    }
}
