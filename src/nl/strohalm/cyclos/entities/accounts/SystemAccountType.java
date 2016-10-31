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
package nl.strohalm.cyclos.entities.accounts;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.external.ExternalAccount;
import nl.strohalm.cyclos.entities.groups.AdminGroup;

/**
 * Type for system accounts
 *
 * @author luis
 */
@Entity
@DiscriminatorValue(value = "S")
public class SystemAccountType extends AccountType {

    public static enum Relationships implements Relationship {

        ACCOUNT("account"), VIEWED_BY_GROUPS("viewedByGroups"), EXTERNAL_ACCOUNTS("externalAccounts");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -3356210066125175997L;
    private SystemAccount account;
    private BigDecimal creditLimit;
    private BigDecimal upperCreditLimit;
    private Collection<AdminGroup> viewedByGroups;
    private Collection<ExternalAccount> externalAccounts;

    @ManyToOne(targetEntity = SystemAccount.class)
    @JoinColumn(name = "system_account_id")
    public SystemAccount getAccount() {
        return account;
    }

    @Transient
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    @OneToMany(targetEntity = ExternalAccount.class)
    @JoinColumn(name = "system_account_id")
    public Collection<ExternalAccount> getExternalAccounts() {
        return externalAccounts;
    }

    @Transient
    @Override
    public LimitType getLimitType() {
        return account.getCreditLimit() == null ? LimitType.UNLIMITED : LimitType.LIMITED;
    }

    @Transient
    @Override
    public Nature getNature() {
        return Nature.SYSTEM;
    }

    @Transient
    public BigDecimal getUpperCreditLimit() {
        return upperCreditLimit;
    }

    @ManyToMany(targetEntity = AdminGroup.class)
    @JoinTable(name = "admin_view_account_information",
            joinColumns = @JoinColumn(name = "account_type_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Collection<AdminGroup> getViewedByGroups() {
        return viewedByGroups;
    }

    @Transient
    @Override
    public boolean isLimited() {
        return account.getCreditLimit() != null;
    }

    public void setAccount(final SystemAccount account) {
        this.account = account;
        if (account != null) {
            setCreditLimit(account.getCreditLimit());
            setUpperCreditLimit(account.getUpperCreditLimit());
        }
    }

    public void setCreditLimit(final BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setExternalAccounts(final Collection<ExternalAccount> externalAccounts) {
        this.externalAccounts = externalAccounts;
    }

    public void setUpperCreditLimit(final BigDecimal upperCreditLimit) {
        this.upperCreditLimit = upperCreditLimit;
    }

    public void setViewedByGroups(final Collection<AdminGroup> viewedByGroups) {
        this.viewedByGroups = viewedByGroups;
    }

}
