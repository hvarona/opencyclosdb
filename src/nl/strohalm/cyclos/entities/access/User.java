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
package nl.strohalm.cyclos.entities.access;

import java.util.Calendar;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * An user is the entity that contains login / password / transaction
 *
 * @author luis
 */
@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "subclass", discriminatorType = DiscriminatorType.STRING)
public abstract class User extends Entity {

    public static enum Relationships implements Relationship {
        ELEMENT("element"), LOGIN_HISTORY("loginHistory");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static enum TransactionPasswordStatus implements StringValuedEnum {
        ACTIVE("A"), PENDING("P"), BLOCKED("B"), NEVER_CREATED("N");

        private final String value;

        private TransactionPasswordStatus(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        public boolean isGenerationAllowed() {
            return this == PENDING || this == NEVER_CREATED;
        }
    }

    private static final long serialVersionUID = 545429548353183777L;
    private Element element;
    private String salt;
    private String username;
    private Calendar lastLogin;
    private String password;
    private Calendar passwordDate;
    private Calendar passwordBlockedUntil;
    private String transactionPassword;
    private TransactionPasswordStatus transactionPasswordStatus = TransactionPasswordStatus.NEVER_CREATED;
    private Collection<LoginHistoryLog> loginHistory;

    @Id
    @Override
    public Long getId() {
        return super.getId();
    }

    @OneToOne(targetEntity = nl.strohalm.cyclos.entities.members.Element.class, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    public Element getElement() {
        return element;
    }

    @Column(name = "last_login")
    public Calendar getLastLogin() {
        return lastLogin;
    }

    @OneToMany(targetEntity = nl.strohalm.cyclos.entities.access.LoginHistoryLog.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    public Collection<LoginHistoryLog> getLoginHistory() {
        return loginHistory;
    }

    @Column(length = 64)
    public String getPassword() {
        return password;
    }

    @Column(name = "password_blocked_until")
    public Calendar getPasswordBlockedUntil() {
        return passwordBlockedUntil;
    }

    @Column(name = "password_date")
    public Calendar getPasswordDate() {
        return passwordDate;
    }

    @Column(length = 32)
    public String getSalt() {
        return salt;
    }

    @Column(length = 64, name = "transaction_password")
    public String getTransactionPassword() {
        return transactionPassword;
    }

    @Column(nullable = false, name = "transaction_password_status")
    @Enumerated(EnumType.STRING)
    public TransactionPasswordStatus getTransactionPasswordStatus() {
        return transactionPasswordStatus;
    }

    @Column(length = 64, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setElement(final Element element) {
        this.element = element;
    }

    public void setLastLogin(final Calendar lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLoginHistory(final Collection<LoginHistoryLog> loginHistory) {
        this.loginHistory = loginHistory;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public void setPasswordBlockedUntil(final Calendar passwordBlockedUntil) {
        this.passwordBlockedUntil = passwordBlockedUntil;
    }

    public void setPasswordDate(final Calendar passwordGeneratedAt) {
        passwordDate = passwordGeneratedAt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    public void setTransactionPassword(final String transactionPassword) {
        this.transactionPassword = transactionPassword;
    }

    public void setTransactionPasswordStatus(final TransactionPasswordStatus transactionPasswordStatus) {
        this.transactionPasswordStatus = transactionPasswordStatus;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return element == null ? getId() + " - " + getUsername() : element.toString();
    }
}
