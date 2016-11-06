/*
    This file is part of Cyclos (www.cyclos.org).
    A project of the Social Trade Organisation (www.socialtrade.org).

    Cyclos is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Cyclos is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Cyclos; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 */
package nl.strohalm.cyclos.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import nl.strohalm.cyclos.utils.database.HibernateUtil;

import nl.strohalm.cyclos.entities.Application;
import nl.strohalm.cyclos.entities.exceptions.DaoException;
import nl.strohalm.cyclos.utils.JDBCWrapper;
import nl.strohalm.cyclos.utils.database.DatabaseUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

/**
 * Implementation for application dao
 *
 * @author luis
 */
public class ApplicationDAOImpl /*extends BaseDAOImpl<Application>*/ implements ApplicationDAO {

    private static final Log LOG = LogFactory.getLog(ApplicationDAOImpl.class);

    public ApplicationDAOImpl() {
        //super(Application.class);
    }

    @Override
    public Application read() {
        EntityManager em = DatabaseUtil.getCurrentEntityManager();
        CriteriaQuery<Application> cq = em.getCriteriaBuilder().createQuery(Application.class);
        cq.from(Application.class);
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public void shutdownDBIfNeeded() {
        Session session = HibernateUtil.getSession();
        try {
            // Way2 - using doReturningWork method
            Connection connection = session.doReturningWork(new ReturningWork<Connection>() {
                @Override
                public Connection execute(Connection conn) throws SQLException {
                    return conn;
                }
            });
            try {
                String dbName = connection.getMetaData().getDatabaseProductName();
                if (dbName.startsWith("HSQL")) {
                    new JDBCWrapper(connection).execute("SHUTDOWN");
                    LOG.info("Shutdown on HSQL Database was successful");
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            LOG.warn("Error shutting down database connection", e);
        }
    }

    @Override
    public <T extends Application> T update(T entity) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Application> T update(T entity, boolean flush) throws DaoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
