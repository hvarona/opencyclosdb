package nl.strohalm.cyclos.utils.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author hvarona
 */
public class HibernateUtil {

    private static Session session_;
    private static final Configuration configuration = new Configuration();
    private static String serverUrl = "jdbc:mysql://localhost:3307/CyclosDB?autoReconnect=true";
    private static String user = "root";
    private static String pwd = "1234";
    private static int dbType = 1;
    public static final int MSSQL = 0;
    public static final int MYSQL = 1;
    public static final int MARIADB = 1;
    public static final int POSTGRESQL = 2;
    public static final int POSTGRESQLSSL = 3;
    public static final int POSTGRESQLSSLNOVEF = 4;
    public static final int H2DATABASE = 5;
    private static SessionFactory sessionFactory = null;

    private static void rebuildSessionFactory() {
        switch (dbType) {
            case (MSSQL):
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
                configuration.setProperty("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                break;
            case (MYSQL):
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
                break;
            case (POSTGRESQL):
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                configuration.setProperty("hibernate.connection.sslfactory", "org.postgresql.ssl.NonValidatingFactory");
                break;
            case (POSTGRESQLSSLNOVEF):
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                configuration.setProperty("hibernate.connection.ssl", "true");
                configuration.setProperty("hibernate.connection.sslfactory", "org.postgresql.ssl.NonValidatingFactory");
                break;
            case (POSTGRESQLSSL):
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
                configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
                configuration.setProperty("hibernate.connection.ssl", "true");
                break;
            case (H2DATABASE):
                configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
                configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
            default:
        }
        configuration.setProperty("hibernate.connection.url", serverUrl);
        configuration.setProperty("hibernate.connection.username", user);
        configuration.setProperty("hibernate.connection.password", pwd);

        configuration.setProperty("hibernate.connection.autoReconnect", "true");
        configuration.setProperty("hibernate.connection.autoReconnectForPools", "true");
        configuration.setProperty("hibernate.connection.is-connection-validation-required", "true");

        configuration.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        configuration.setProperty("hibernate.cache.use_query_cache", "false");
        configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        configuration.setProperty("hibernate.connection.aggressive_release", "false");

        /*configuration.setProperty("hibernate.c3p0.acquire_increment", "3");
        configuration.setProperty("hibernate.c3p0.initialPoolSize", "5");
        configuration.setProperty("hibernate.c3p0.maxPoolSize", "15");
        configuration.setProperty("hibernate.c3p0.minPoolSize", "3");
        configuration.setProperty("hibernate.c3p0.breakAfterAcquireFailure", "false");
        configuration.setProperty("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");*/
        configuration.setProperty("hibernate.show_sql", "false");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        for (Class cl : DatabaseUtil.CLASSES_LIST) {
            configuration.addAnnotatedClass(cl);
        }

        sessionFactory = configuration.buildSessionFactory();
    }

    public static void setServerUrl(String serverAddress, String serverPort, String dbName) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:");
        switch (dbType) {
            case (MSSQL):
                url.append("sqlserver://");
                url.append(serverAddress);
                url.append(":");
                url.append(serverPort);
                url.append(";DatabaseName=");
                url.append(dbName);
                url.append(";SelectMethod=cursor");
                break;
            case (MYSQL):
                url.append("mysql://");
                url.append(serverAddress);
                url.append(":");
                url.append(serverPort);
                url.append("/");
                url.append(dbName);
                url.append("?autoReconnect=true");
                break;
            case (POSTGRESQLSSL):
            case (POSTGRESQLSSLNOVEF):
            case (POSTGRESQL):
                url.append("postgresql://");
                url.append(serverAddress);
                url.append(":");
                url.append(serverPort);
                url.append("/");
                url.append(dbName);
                break;
            case (H2DATABASE):
                url.append("h2tcp://");
                url.append(serverAddress);
                url.append(":");
                url.append(serverPort);
                url.append("/");
                url.append(dbName);
            default:
                break;
        }

        HibernateUtil.serverUrl = url.toString();
    }

    public static void setDbType(int dbType) {
        HibernateUtil.dbType = dbType;
    }

    public static void setUser(String user) {
        HibernateUtil.user = user;
    }

    public static void setPwd(String pwd) {
        HibernateUtil.pwd = pwd;
    }

    public static Session getSession() throws HibernateException {
        if (sessionFactory == null) {
            rebuildSessionFactory();
        }
        Session session = sessionFactory.openSession();
        return session;

    }

    public static Session getCurrentSession() throws HibernateException {
        if (session_ == null || !session_.isOpen()) {
            if (sessionFactory == null) {
                rebuildSessionFactory();
            }
            session_ = (sessionFactory != null) ? sessionFactory.openSession()
                    : null;
        }
        return session_;
    }
}
