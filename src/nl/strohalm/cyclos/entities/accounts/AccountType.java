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
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.transactions.PaymentFilter;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * Represents an account type
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "account_types")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subclass")
public abstract class AccountType extends Entity implements Comparable<AccountType> {

    public static enum LimitType {

        LIMITED, UNLIMITED;
    }

    public static enum Nature implements StringValuedEnum {

        MEMBER("M"), SYSTEM("S");
        private final String value;

        private Nature(final String value) {
            this.value = value;
        }

        public Class<? extends AccountType> getType() {
            return this == SYSTEM ? SystemAccountType.class : MemberAccountType.class;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public static enum Relationships implements Relationship {

        CURRENCY("currency"), FROM_TRANSFER_TYPES("fromTransferTypes"), TO_TRANSFER_TYPES("toTransferTypes"), PAYMENT_FILTERS("paymentFilters");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -3362966482758151026L;

    private String description;
    private String name;
    private Currency currency;
    private Collection<TransferType> fromTransferTypes;
    private Collection<TransferType> toTransferTypes;
    private Collection<PaymentFilter> paymentFilters;

    @Override
    public int compareTo(final AccountType o) {
        final int compareByName = name.compareTo(o.name);
        final int compareById = getId().compareTo(o.getId());
        return compareByName == 0 ? compareById : compareByName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency_id", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    @Column
    public String getDescription() {
        return description;
    }

    @OneToMany(targetEntity = TransferType.class)
    @JoinColumn(name = "from_account_type_id")
    public Collection<TransferType> getFromTransferTypes() {
        return fromTransferTypes;
    }

    @Transient
    public abstract LimitType getLimitType();

    @Column(nullable = false, length = 100)
    @Override
    public String getName() {
        return name;
    }

    @Transient
    public abstract Nature getNature();

    @OneToMany(targetEntity = PaymentFilter.class)
    @JoinColumn(name = "account_type_id")
    public Collection<PaymentFilter> getPaymentFilters() {
        return paymentFilters;
    }

    @OneToMany(targetEntity = TransferType.class)
    @JoinColumn(name = "to_account_type_id")
    public Collection<TransferType> getToTransferTypes() {
        return toTransferTypes;
    }

    @Transient
    public boolean isLimited() {
        return true;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setFromTransferTypes(final Collection<TransferType> fromTransferTypes) {
        this.fromTransferTypes = fromTransferTypes;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPaymentFilters(final Collection<PaymentFilter> paymentFilters) {
        this.paymentFilters = paymentFilters;
    }

    public void setToTransferTypes(final Collection<TransferType> toTransferTypes) {
        this.toTransferTypes = toTransferTypes;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }
}
