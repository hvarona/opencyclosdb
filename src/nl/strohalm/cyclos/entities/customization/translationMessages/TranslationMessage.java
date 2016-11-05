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
package nl.strohalm.cyclos.entities.customization.translationMessages;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nl.strohalm.cyclos.entities.Entity;

/**
 * A resource bundle key
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "translation_messages")
public class TranslationMessage extends Entity {

    private static final long serialVersionUID = -5301317219230694326L;
    private String key;
    private String value;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "msg_key", unique = true, length = 100, nullable = false)
    public String getKey() {
        return key;
    }

    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getId() + " - " + key;
    }
}
