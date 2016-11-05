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
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.utils.FormatObject;

/**
 * Contains an amount reservation (if amount is positive) or reservation return
 * (if negative) for an account
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "amount_reservations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subclass")
public abstract class AmountReservation extends Entity {

    private static final long serialVersionUID = -890389268867857491L;
    private Account account;
    private Calendar date;
    private BigDecimal amount;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Account.class)
    @JoinColumn(name = "account_id", nullable = false)
    public Account getAccount() {
        return account;
    }

    @Column
    public BigDecimal getAmount() {
        return amount;
    }

    @Column(nullable = false)
    public Calendar getDate() {
        return date;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getId() + ", Account: " + account + ", Date: " + FormatObject.formatObject(date) + ", Amount: " + amount;
    }

}
