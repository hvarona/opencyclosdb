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
package nl.strohalm.cyclos.entities.alerts;

import java.util.Calendar;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.access.User;

/**
 * An application error descriptor
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "error_log_entries")
public class ErrorLogEntry extends Entity {

    public static enum Relationships implements Relationship {
        PARAMETERS("parameters"), LOGGED_USER("loggedUser");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -6444236896453745675L;
    private Calendar date;
    private String path;
    private Map<String, String> parameters;
    private String stackTrace;
    private User loggedUser;
    private boolean removed;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "date", nullable = false)
    public Calendar getDate() {
        return date;
    }

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "logged_user_id")
    public User getLoggedUser() {
        return loggedUser;
    }

    @ElementCollection
    @MapKeyColumn(name = "name", length = 100)
    @Column(name = "value")
    @CollectionTable(name = "error_log_entry_parameters",
            joinColumns = @JoinColumn(name = "error_log_entry_id"))
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Column(name = "path", nullable = false, length = 200)
    public String getPath() {
        return path;
    }

    @Column(name = "stack_trace")
    public String getStackTrace() {
        return stackTrace;
    }

    @Column(name = "removed", nullable = false)
    public boolean isRemoved() {
        return removed;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setLoggedUser(final User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setPath(final String path) {
        this.path = path;
    }

    public void setRemoved(final boolean removed) {
        this.removed = removed;
    }

    public void setStackTrace(final String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return getId() + " - " + path;
    }

}
