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
package nl.strohalm.cyclos.entities.accounts.fees.account;

import java.math.BigDecimal;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.MemberAccount;
import nl.strohalm.cyclos.utils.FormatObject;

/**
 * Holds an amount to be charged by an account fee over transactioned volume.
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "account_fee_amounts")
public class AccountFeeAmount extends Entity {

    public static enum Relationships implements Relationship {
        ACCOUNT("account"), ACCOUNT_FEE("accountFee");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = 5536949224747161556L;
    private Calendar date;
    private BigDecimal availableBalance;
    private BigDecimal amount;
    private MemberAccount account;
    private AccountFee accountFee;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = MemberAccount.class)
    @JoinColumn(name = "account_id")
    public MemberAccount getAccount() {
        return account;
    }

    @ManyToOne(targetEntity = AccountFee.class)
    @JoinColumn(name = "account_fee_id")
    public AccountFee getAccountFee() {
        return accountFee;
    }

    @Column(nullable = false, precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
    }

    @Column(name = "available_balance", nullable = false, precision = 15, scale = 6)
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    @Column(nullable = false)
    public Calendar getDate() {
        return date;
    }

    public void setAccount(final MemberAccount account) {
        this.account = account;
    }

    public void setAccountFee(final AccountFee accountFee) {
        this.accountFee = accountFee;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setAvailableBalance(final BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getId() + ", date = " + FormatObject.formatObject(date) + ", account = " + account + ", amount = " + FormatObject.formatObject(amount);
    }

}
