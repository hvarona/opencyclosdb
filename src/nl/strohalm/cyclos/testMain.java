package nl.strohalm.cyclos;

import java.util.Calendar;
import nl.strohalm.cyclos.entities.Application;
import nl.strohalm.cyclos.entities.access.OperatorUser;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author henry
 */
public class testMain {

    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setProperty("hibernate.hbm2ddl.auto", "update");
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        config.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3307/CyclosDB");
        config.setProperty("hibernate.connection.username", "root");
        config.setProperty("hibernate.connection.password", "1234");
        config.setProperty("hibernate.connection.driver.class", "com.mysql.jdbc.Driver");

        config.addAnnotatedClass(nl.strohalm.cyclos.entities.Application.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.IndexOperation.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.access.User.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.members.Element.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.members.Member.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.members.Operator.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.members.Contact.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.access.AdminUser.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.access.Channel.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.access.OperatorUser.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.access.LoginHistoryLog.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.groups.Group.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.groups.SystemGroup.class);
        config.addAnnotatedClass(nl.strohalm.cyclos.entities.groups.MemberGroup.class);

        Session session = config.buildSessionFactory().openSession();
        //Transaction tx = session.beginTransaction();
        /*Application app = new Application();
        app.setPasswordHash(Application.PasswordHash.SHA2_SALT);
        Calendar cal = Calendar.getInstance();
        cal.set(2015, 1, 2);
        app.setAccountStatusEnabledSince(cal);
        app.setLastIndexRebuidingTime(Calendar.getInstance());
        app.setOnline(true);
        app.setVersion("test");
        session.save(app);*/
 /*OperatorUser user = new OperatorUser();
        user.setId(1L);
        user.setLastLogin(Calendar.getInstance());
        user.setPassword("");
        user.setSalt("12345");
        session.save(user);

        tx.commit();*/

//        session.flush();
        session.close();

        System.out.println("Fin de la prueba");

        System.exit(1);
        //<mapping class="com.javatpoint.Employee"/>  
    }
}
