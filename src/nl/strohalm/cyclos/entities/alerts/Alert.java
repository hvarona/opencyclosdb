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
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;

/**
 * An alert
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "alerts")
@DiscriminatorColumn(name = "subclass")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Alert extends Entity {

    public static enum Type {
        SYSTEM, MEMBER;

        public Class<? extends Alert> getEntityType() {
            return this == SYSTEM ? SystemAlert.class : MemberAlert.class;
        }
    }

    private static final long serialVersionUID = -9026118891761458811L;

    protected String arg0;
    protected String arg1;
    protected String arg2;
    protected String arg3;
    protected String arg4;
    protected Calendar date;
    protected String key;
    protected boolean removed;

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "arg0")
    public String getArg0() {
        return arg0;
    }

    @Column(name = "arg1")
    public String getArg1() {
        return arg1;
    }

    @Column(name = "arg2")
    public String getArg2() {
        return arg2;
    }

    @Column(name = "arg3")
    public String getArg3() {
        return arg3;
    }

    @Column(name = "arg4")
    public String getArg4() {
        return arg4;
    }

    @Column(name = "date", nullable = false)
    public Calendar getDate() {
        return date;
    }

    @Column(name = "msg_key", nullable = false)
    public String getKey() {
        return key;
    }

    @Column(name = "removed", nullable = false)
    public boolean isRemoved() {
        return removed;
    }

    public void setArg0(final String arg0) {
        this.arg0 = arg0;
    }

    public void setArg1(final String arg1) {
        this.arg1 = arg1;
    }

    public void setArg2(final String arg2) {
        this.arg2 = arg2;
    }

    public void setArg3(final String arg3) {
        this.arg3 = arg3;
    }

    public void setArg4(final String arg4) {
        this.arg4 = arg4;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setDescription(final String key, final String... args) {
        this.key = key;
        arg0 = null;
        arg1 = null;
        arg2 = null;
        arg3 = null;
        arg4 = null;
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                switch (i) {
                    case 0:
                        arg0 = args[i];
                        break;
                    case 1:
                        arg1 = args[i];
                        break;
                    case 2:
                        arg2 = args[i];
                        break;
                    case 3:
                        arg3 = args[i];
                        break;
                    case 4:
                        arg4 = args[i];
                        break;
                }
            }
        }
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setRemoved(final boolean removed) {
        this.removed = removed;
    }

    @Override
    public String toString() {
        return getId() + " - " + key;
    }
}
