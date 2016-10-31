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

import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.fees.account.AccountFee;

/**
 * Type for member accounts
 *
 * @author luis
 */
@Entity
@DiscriminatorValue(value = "M")
public class MemberAccountType extends AccountType {

    public static enum Relationships implements Relationship {

        ACCOUNTS("accounts"), SETTINGS("settings"), ACCOUNT_FEES("accountFees");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -8890748132404619378L;
    private Collection<MemberAccount> accounts;
    private Collection<AccountFee> accountFees;
    private Collection<MemberGroupAccountSettings> settings;

    @OneToMany(targetEntity = MemberAccount.class)
    @JoinColumn(name = "account_type_id")
    public Collection<AccountFee> getAccountFees() {
        return accountFees;
    }

    @OneToMany(targetEntity = MemberAccount.class)
    @JoinColumn(name = "type_id")
    public Collection<MemberAccount> getAccounts() {
        return accounts;
    }

    @Transient
    @Override
    public LimitType getLimitType() {
        return LimitType.LIMITED;
    }

    @Transient
    @Override
    public Nature getNature() {
        return Nature.MEMBER;
    }

    @OneToMany(targetEntity = MemberGroupAccountSettings.class)
    @JoinColumn(name = "type_id")
    public Collection<MemberGroupAccountSettings> getSettings() {
        return settings;
    }

    public void setAccountFees(final Collection<AccountFee> accountFees) {
        this.accountFees = accountFees;
    }

    public void setAccounts(final Collection<MemberAccount> accounts) {
        this.accounts = accounts;
    }

    public void setSettings(final Collection<MemberGroupAccountSettings> settings) {
        this.settings = settings;
    }
}
