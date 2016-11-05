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
package nl.strohalm.cyclos.entities.accounts.fees.transaction;

import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.transactions.Transfer;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.entities.groups.MemberGroup;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.utils.Amount;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * A fee is applied on transfers
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "transaction_fees")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "subclass")
public abstract class TransactionFee extends Entity {

    public static enum ChargeType implements StringValuedEnum {

        FIXED("F"), PERCENTAGE("P"), A_RATE("A"), D_RATE("D"), MIXED_A_D_RATES("M");

        public static ChargeType from(final Amount.Type type) {
            if (type == null) {
                return null;
            }
            switch (type) {
                case FIXED:
                    return FIXED;
                case PERCENTAGE:
                    return PERCENTAGE;
            }
            return null;
        }

        private final String value;

        private ChargeType(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        public Amount.Type toAmountType() {
            switch (this) {
                case FIXED:
                    return Amount.Type.FIXED;
                case PERCENTAGE:
                case A_RATE:
                case D_RATE:
                case MIXED_A_D_RATES:
                    return Amount.Type.PERCENTAGE;
                default:
                    return null;
            }
        }
    }

    public static enum Nature implements StringValuedEnum {

        SIMPLE("S"), BROKER("B");

        private final String value;

        private Nature(final String value) {
            this.value = value;
        }

        public Class<? extends TransactionFee> getFeeClass() {
            if (this == SIMPLE) {
                return SimpleTransactionFee.class;
            } else {
                return BrokerCommission.class;
            }
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public static enum Relationships implements Relationship {

        GENERATED_TRANSFER_TYPE("generatedTransferType"), ORIGINAL_TRANSFER_TYPE("originalTransferType"), TRANSFERS("transfers"), FROM_GROUPS("fromGroups"), TO_GROUPS("toGroups"), FROM_FIXED_MEMBER("fromFixedMember");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static enum Subject implements StringValuedEnum {

        SYSTEM("sys"), SOURCE("src"), SOURCE_BROKER("sbr"), DESTINATION("dst"), DESTINATION_BROKER("dbr"), FIXED_MEMBER("mem");
        private final String value;

        private Subject(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = -3840663900697716307L;
    private String name;
    private String description;
    private Subject payer;
    private boolean enabled;
    private TransferType originalTransferType;
    private TransferType generatedTransferType;
    private ChargeType chargeType;
    private BigDecimal value;
    private BigDecimal maxFixedValue;
    private BigDecimal maxPercentageValue;
    private BigDecimal initialAmount;
    private BigDecimal finalAmount;
    private boolean deductAmount;
    private Collection<Transfer> transfers;
    private boolean fromAllGroups = true;
    private Collection<MemberGroup> fromGroups;
    private boolean toAllGroups = true;
    private Collection<MemberGroup> toGroups;
    private Member fromFixedMember;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Transient
    public Amount getAmount() {
        if (chargeType == null || value == null) {
            return null;
        }
        return new Amount(value, chargeType.toAmountType());
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "amount_type", nullable = false)
    public ChargeType getChargeType() {
        return chargeType;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @Column(name = "final_amount", precision = 15, scale = 6)
    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "from_member_id", updatable = false)
    public Member getFromFixedMember() {
        return fromFixedMember;
    }

    @ManyToMany(targetEntity = MemberGroup.class)
    @JoinTable(name="groups_from_transaction_fees",
            joinColumns = @JoinColumn(name="transaction_fee_id"),
            inverseJoinColumns = @JoinColumn(name="group_id"))
    public Collection<MemberGroup> getFromGroups() {
        return fromGroups;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "generated_type_id", nullable = false)
    public TransferType getGeneratedTransferType() {
        return generatedTransferType;
    }

    @Column(name = "initial_amount", precision = 15, scale = 6)
    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    @Column(name = "max_fixed_value", precision = 15, scale = 6)
    public BigDecimal getMaxFixedValue() {
        return maxFixedValue;
    }

    @Column(name = "max_percentage_value", precision = 15, scale = 6)
    public BigDecimal getMaxPercentageValue() {
        return maxPercentageValue;
    }

    @Column(name = "name", nullable = false, length = 100)
    @Override
    public String getName() {
        return name;
    }

    @Transient
    public abstract Nature getNature();

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "original_type_id", nullable = false)
    public TransferType getOriginalTransferType() {
        return originalTransferType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "payer", nullable = false)
    public Subject getPayer() {
        return payer;
    }

    @ManyToMany(targetEntity = MemberGroup.class)
    @JoinTable(name="groups_to_transaction_fees",
            joinColumns = @JoinColumn(name="transaction_fee_id"),
            inverseJoinColumns = @JoinColumn(name="group_id"))
    public Collection<MemberGroup> getToGroups() {
        return toGroups;
    }

    @OneToMany(targetEntity = Transfer.class)
    @JoinColumn(name = "fee_id", updatable = false)
    public Collection<Transfer> getTransfers() {
        return transfers;
    }

    @Column(name = "value", precision = 15, scale = 6)
    public BigDecimal getValue() {
        return value;
    }

    @Column(name = "deduct_amount", nullable = false)
    public boolean isDeductAmount() {
        return deductAmount;
    }

    @Column(name = "enabled", nullable = false)
    public boolean isEnabled() {
        return enabled;
    }

    @Column(name = "from_all_groups", nullable = false)
    public boolean isFromAllGroups() {
        return fromAllGroups;
    }

    @Transient
    public boolean isFromMember() {
        return !isFromSystem();
    }

    @Transient
    public boolean isFromSystem() {
        return generatedTransferType.isFromSystem();
    }

    @Column(name = "to_all_groups", nullable = false)
    public boolean isToAllGroups() {
        return toAllGroups;
    }

    public void setAmount(final Amount amount) {
        if (amount == null) {
            value = null;
            chargeType = null;
        } else {
            value = amount.getValue();
            chargeType = ChargeType.from(amount.getType());
        }
    }

    /**
     * for rates, the amount needs to be set without resetting the charge type.
     * So call this one only in case of rates.
     *
     * @throws IllegalArgumentException in case the method is called outside of
     * a A- or D-rated context, that is: when the chargeType is not related to
     * A- or D-rate.
     */
    public void setAmountForRates(final Amount amount) {
        if (chargeType != ChargeType.A_RATE && chargeType != ChargeType.D_RATE && chargeType != ChargeType.MIXED_A_D_RATES) {
            throw new IllegalArgumentException("TransactionFee.setAmountForRates can only be called in case of a charge type related to A-rate or D-rate.");
        }
        if (amount == null) {
            value = null;
        } else {
            value = amount.getValue();
        }
    }

    public void setChargeType(final ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public void setDeductAmount(final boolean deductAmount) {
        this.deductAmount = deductAmount;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setFinalAmount(final BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public void setFromAllGroups(final boolean fromAllGroups) {
        this.fromAllGroups = fromAllGroups;
    }

    public void setFromFixedMember(final Member fromFixedMember) {
        this.fromFixedMember = fromFixedMember;
    }

    public void setFromGroups(final Collection<MemberGroup> fromGroups) {
        this.fromGroups = fromGroups;
    }

    public void setGeneratedTransferType(final TransferType generatedTransferType) {
        this.generatedTransferType = generatedTransferType;
    }

    public void setInitialAmount(final BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public void setMaxFixedValue(final BigDecimal maxFixedValue) {
        this.maxFixedValue = maxFixedValue;
    }

    public void setMaxPercentageValue(final BigDecimal maxPercentageValue) {
        this.maxPercentageValue = maxPercentageValue;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setOriginalTransferType(final TransferType originalTransferType) {
        this.originalTransferType = originalTransferType;
    }

    public void setPayer(final Subject payer) {
        this.payer = payer;
    }

    public void setToAllGroups(final boolean toAllGroups) {
        this.toAllGroups = toAllGroups;
    }

    public void setToGroups(final Collection<MemberGroup> toGroups) {
        this.toGroups = toGroups;
    }

    public void setTransfers(final Collection<Transfer> transfers) {
        this.transfers = transfers;
    }

    public void setValue(final BigDecimal value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }
}
