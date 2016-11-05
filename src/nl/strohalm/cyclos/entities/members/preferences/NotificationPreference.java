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
package nl.strohalm.cyclos.entities.members.preferences;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.members.messages.Message;

/**
 * Notification Preference Entity
 *
 * @author Jefferson Magno
 * @author jeancarlo
 */
@javax.persistence.Entity
@Table(name = "notification_preferences")
public class NotificationPreference extends Entity {

    public static enum Relationships implements Relationship {
        MEMBER("member");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 5666584487265542893L;
    private Member member;
    private Message.Type type;
    private boolean message;
    private boolean email;
    private boolean sms;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "member_id", updatable = false)
    public Member getMember() {
        return member;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public Message.Type getType() {
        return type;
    }

    @Column(name = "is_email", nullable = false)
    public boolean isEmail() {
        return email;
    }

    @Column(name = "is_message", nullable = false)
    public boolean isMessage() {
        return message;
    }

    @Column(name = "is_sms", nullable = false)
    public boolean isSms() {
        return sms;
    }

    public void setEmail(final boolean email) {
        this.email = email;
    }

    public void setMember(final Member member) {
        this.member = member;
    }

    public void setMessage(final boolean message) {
        this.message = message;
    }

    public void setSms(final boolean sms) {
        this.sms = sms;
    }

    public void setType(final Message.Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return getId() + ", type: " + getType() + ", member: " + getMember() + ", isMsg: " + message + ", isSMS: " + sms + ", isEmail: " + email;
    }
}
