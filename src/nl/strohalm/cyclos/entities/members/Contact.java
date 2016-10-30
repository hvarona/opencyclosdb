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
package nl.strohalm.cyclos.entities.members;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;

/**
 * A member contact
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "contacts")
public class Contact extends Entity {

    public static enum Relationships implements Relationship {
        CONTACT("contact"), OWNER("owner");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 9128456589118721030L;

    private Member contact;
    private String notes;
    private Member owner;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    @ManyToOne (targetEntity = nl.strohalm.cyclos.entities.members.Member.class)
    @JoinColumn (name = "contact_id")
    public Member getContact() {
        return contact;
    }

    @Column
    public String getNotes() {
        return notes;
    }

    @ManyToOne (targetEntity = nl.strohalm.cyclos.entities.members.Member.class)
    @JoinColumn (name = "owner_id")
    public Member getOwner() {
        return owner;
    }

    public void setContact(final Member contact) {
        this.contact = contact;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

    public void setOwner(final Member owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return getId() + " - " + contact + " contact of " + owner;
    }
}
