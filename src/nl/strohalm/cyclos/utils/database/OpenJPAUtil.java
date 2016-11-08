package nl.strohalm.cyclos.utils.database;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.apache.openjpa.persistence.EntityManagerFactoryImpl;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author hvarona
 */
public class OpenJPAUtil {

    private static EntityManager em;
    private static final Properties configuration = new Properties();
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
    public static final int H2DATABASEEMBEDDED = 6;
    public static final int H2DATABASEMEMORY = 7;
    private static EntityManagerFactoryImpl emFactory = null;

    private static void rebuildSessionFactory() {
        switch (dbType) {
            case (MSSQL):
                configuration.setProperty("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.SQLServerDictionary");
                configuration.setProperty("openjpa.ConnectionDriverName", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                break;
            case (MYSQL):
                configuration.setProperty("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.MySQLDictionary");
                configuration.setProperty("openjpa.ConnectionDriverName", "com.mysql.jdbc.Driver");
                break;
            case (POSTGRESQL):
                configuration.setProperty("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.PostgresDictionary");
                configuration.setProperty("openjpa.ConnectionDriverName", "org.postgresql.Driver");
                configuration.setProperty("hibernate.connection.sslfactory", "org.postgresql.ssl.NonValidatingFactory");
                break;
            case (POSTGRESQLSSLNOVEF):
                configuration.setProperty("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.PostgresDictionary");
                configuration.setProperty("openjpa.ConnectionDriverName", "org.postgresql.Driver");
                configuration.setProperty("ssl", "true");
                break;
            case (POSTGRESQLSSL):
                configuration.setProperty("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.PostgresDictionary");
                configuration.setProperty("openjpa.ConnectionDriverName", "org.postgresql.Driver");
                configuration.setProperty("ssl", "true");
                break;
            case (H2DATABASE):
            case (H2DATABASEEMBEDDED):
            case (H2DATABASEMEMORY):
                configuration.setProperty("openjpa.jdbc.DBDictionary", "org.apache.openjpa.jdbc.sql.H2Dictionary");
                configuration.setProperty("openjpa.ConnectionDriverName", "org.h2.Driver");
            default:
        }
        configuration.setProperty("openjpa.ConnectionURL", serverUrl);
        configuration.setProperty("openjpa.ConnectionUserName", user);
        configuration.setProperty("openjpa.ConnectionPassword", pwd);

        StringBuilder entitiesList = new StringBuilder("org.apache.openjpa.persistence.jdbc.PersistenceMappingFactory(types=");
        for (Class cl : DatabaseUtil.CLASSES_LIST) {
            entitiesList.append(cl.getCanonicalName()).append(";");
        }
        entitiesList.append(")");
        configuration.setProperty("openjpa.MetaDataFactory", entitiesList.toString());
        emFactory = (EntityManagerFactoryImpl) Persistence.createEntityManagerFactory("openJPADB", configuration);
        
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
                break;
            case (H2DATABASEEMBEDDED):
                url.append("h2:");
                url.append(dbName);
                break;
            case (H2DATABASEMEMORY):
                url.append("h2:mem:");
                url.append(dbName);
            default:
                break;
        }

        OpenJPAUtil.serverUrl = url.toString();
    }

    public static void setDbType(int dbType) {
        OpenJPAUtil.dbType = dbType;
    }

    public static void setUser(String user) {
        OpenJPAUtil.user = user;
    }

    public static void setPwd(String pwd) {
        OpenJPAUtil.pwd = pwd;
    }

    public static EntityManager getEntityManager() {
        if (emFactory == null) {
            rebuildSessionFactory();
        }
        EntityManager answer = emFactory.createEntityManager();
        return answer;

    }

    public static EntityManager getCurrentEntityManager() {
        if (em == null || !em.isOpen()) {
            if (emFactory == null) {
                rebuildSessionFactory();
            }
            em = (emFactory != null) ? emFactory.createEntityManager() : null;
        }
        return em;
    }
}
