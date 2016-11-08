package nl.strohalm.cyclos;

import nl.strohalm.cyclos.dao.ApplicationDAO;
import nl.strohalm.cyclos.dao.ApplicationDAOImpl;
import nl.strohalm.cyclos.utils.database.DatabaseUtil;

/**
 *
 * @author henry
 */
public class testMain {

    public static void main(String[] args) {
        DatabaseUtil.setIsHibernate(true);
        DatabaseUtil.setDbType(DatabaseUtil.MARIADB);
        DatabaseUtil.setHost("localhost");
        DatabaseUtil.setDbPort("3307");
        DatabaseUtil.setUsername("root");
        DatabaseUtil.setPassword("1234");
        ApplicationDAO appDAO = new ApplicationDAOImpl();
        System.out.println(appDAO.read().getVersion());

        //System.exit(1);
    }
}
