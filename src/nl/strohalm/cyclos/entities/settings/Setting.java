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
package nl.strohalm.cyclos.entities.settings;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * Contains the value of a single setting
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "settings")
public class Setting extends Entity {

    public static enum Type implements StringValuedEnum {
        ACCESS("access"), ALERT("alert"), LOCAL("local"), LOG("log"), MAIL("mail"), MAIL_TRANSLATION("mailTranslation"), MESSAGE("message");
        private final String value;

        private Type(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 8376447152912773532L;

    private String name;
    private Type type;
    private String value;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(nullable = false, length = 100)
    @Override
    public String getName() {
        return name;
    }

    @Enumerated(EnumType.STRING)
    public Type getType() {
        return type;
    }

    @Column(length = 4096)
    public String getValue() {
        return value;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getId() + " - " + type + "." + name + "=" + value;
    }
}
