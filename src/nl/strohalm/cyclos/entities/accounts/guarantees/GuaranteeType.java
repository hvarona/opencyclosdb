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
package nl.strohalm.cyclos.entities.accounts.guarantees;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.entities.accounts.transactions.TransferType;
import nl.strohalm.cyclos.services.accounts.guarantees.GuaranteeTypeFeeVO;
import nl.strohalm.cyclos.utils.StringValuedEnum;
import nl.strohalm.cyclos.utils.TimePeriod;

/**
 * A guarantee type
 *
 * @author Jefferson Magno
 */
@javax.persistence.Entity
@Table(name = "guarantee_types")
public class GuaranteeType extends Entity {

    public static enum AuthorizedBy implements StringValuedEnum {

        ISSUER("I"), ADMIN("A"), BOTH("B"), NONE("N");
        private final String value;

        private AuthorizedBy(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum FeePayer implements StringValuedEnum {

        BUYER("B"), SELLER("S");
        private final String value;

        private FeePayer(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum FeeType implements StringValuedEnum {

        FIXED("F"), PERCENTAGE("P"), VARIABLE_ACCORDING_TO_TIME("V");
        private final String value;

        private FeeType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static enum Model implements StringValuedEnum {

        WITH_PAYMENT_OBLIGATION("PO"), WITH_BUYER_ONLY("BO"), WITH_BUYER_AND_SELLER("BS");
        private final String value;

        private Model(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum Relationships implements Relationship {

        CURRENCY("currency"), LOAN_TRANSFER_TYPE("loanTransferType"), CREDIT_FEE_TRANSFER_TYPE("creditFeeTransferType"), ISSUE_FEE_TRANSFER_TYPE("issueFeeTransferType"), FORWARD_TRANSFER_TYPE("forwardTransferType");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private static final long serialVersionUID = -8522901316173399683L;

    private String name;
    private String description;
    private Model model;
    private AuthorizedBy authorizedBy;
    private boolean enabled;
    // TODO: the loan re-payment's setup will be supported in a second stage
    // private boolean allowLoanPaymentSetup;
    private TimePeriod pendingGuaranteeExpiration;
    private TimePeriod paymentObligationPeriod;
    private Currency currency;
    private GuaranteeTypeFeeVO creditFee;
    private GuaranteeTypeFeeVO issueFee;
    private FeePayer creditFeePayer;
    private FeePayer issueFeePayer;
    private TransferType loanTransferType;
    private TransferType creditFeeTransferType;
    private TransferType issueFeeTransferType;
    private TransferType forwardTransferType;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "authorized_by", nullable = false)
    public AuthorizedBy getAuthorizedBy() {
        return authorizedBy;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "fee", column = @Column(name = "credit_fee")),
        @AttributeOverride(name = "type", column = @Column(name = "credit_fee_type")),
        @AttributeOverride(name = "readonly", column = @Column(name = "credit_fee_readonly"))})
    public GuaranteeTypeFeeVO getCreditFee() {
        return creditFee;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_fee_payer", nullable = false)
    public FeePayer getCreditFeePayer() {
        return creditFeePayer;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "credit_fee_transfer_type_id")
    public TransferType getCreditFeeTransferType() {
        return creditFeeTransferType;
    }

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency_id", nullable = false)
    public Currency getCurrency() {
        return currency;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "forward_transfer_type_id")
    public TransferType getForwardTransferType() {
        return forwardTransferType;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "fee", column = @Column(name = "issue_fee")),
        @AttributeOverride(name = "type", column = @Column(name = "issue_fee_type")),
        @AttributeOverride(name = "readonly", column = @Column(name = "issue_fee_readonly"))})
    public GuaranteeTypeFeeVO getIssueFee() {
        return issueFee;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_fee_payer", nullable = false)
    public FeePayer getIssueFeePayer() {
        return issueFeePayer;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "issue_fee_transfer_type_id")
    public TransferType getIssueFeeTransferType() {
        return issueFeeTransferType;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "loan_transfer_type_id", nullable = false)
    public TransferType getLoanTransferType() {
        return loanTransferType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "model", nullable = false)
    public Model getModel() {
        return model;
    }

    @Column(name = "name", length = 100, nullable = false)
    @Override
    public String getName() {
        return name;
    }

    @Transient
    public TimePeriod getPaymentObligationPeriod() {
        return paymentObligationPeriod;
    }

    @Column(name = "payment_obligation_period_number")
    public int getPaymentObligationPeriodNumber() {
        return paymentObligationPeriod.getNumber();
    }

    @Column(name = "payment_obligation_period_field")
    public TimePeriod.Field getPaymentObligationPeriodField() {
        return paymentObligationPeriod.getField();
    }

    @Transient
    public TimePeriod getPendingGuaranteeExpiration() {
        return pendingGuaranteeExpiration;
    }

    @Column(name = "pending_guarantee_expiration_number")
    public int getPendingGuaranteeExpirationNumber() {
        return pendingGuaranteeExpiration.getNumber();
    }

    @Column(name = "pending_guarantee_expiration_field")
    public TimePeriod.Field getPendingGuaranteeExpirationField() {
        return pendingGuaranteeExpiration.getField();
    }

    @Column(name = "enabled", nullable = false)
    public boolean isEnabled() {
        return enabled;
    }

    public void setAuthorizedBy(final AuthorizedBy authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public void setCreditFee(final GuaranteeTypeFeeVO creditFee) {
        this.creditFee = creditFee;
    }

    public void setCreditFeePayer(final FeePayer creditFeePayer) {
        this.creditFeePayer = creditFeePayer;
    }

    public void setCreditFeeTransferType(final TransferType usageFeeTransferType) {
        creditFeeTransferType = usageFeeTransferType;
    }

    public void setCurrency(final Currency currency) {
        this.currency = currency;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public void setForwardTransferType(final TransferType forwardTransferType) {
        this.forwardTransferType = forwardTransferType;
    }

    public void setIssueFee(final GuaranteeTypeFeeVO issueFee) {
        this.issueFee = issueFee;
    }

    public void setIssueFeePayer(final FeePayer issueFeePayer) {
        this.issueFeePayer = issueFeePayer;
    }

    public void setIssueFeeTransferType(final TransferType issueFeeTransferType) {
        this.issueFeeTransferType = issueFeeTransferType;
    }

    public void setLoanTransferType(final TransferType loanTransferType) {
        this.loanTransferType = loanTransferType;
    }

    public void setModel(final Model model) {
        this.model = model;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPaymentObligationPeriod(final TimePeriod paymentObligationPeriod) {
        this.paymentObligationPeriod = paymentObligationPeriod;
    }

    public void setPaymentObligationPeriodNumber(int number) {
        paymentObligationPeriod.setNumber(number);
    }

    public void setPaymentObligationPeriodField(TimePeriod.Field field) {
        paymentObligationPeriod.setField(field);
    }

    public void setPendingGuaranteeExpiration(final TimePeriod pendingGuaranteeExpiration) {
        this.pendingGuaranteeExpiration = pendingGuaranteeExpiration;
    }

    public void setPendingGuaranteeExpirationNumber(int number) {
        pendingGuaranteeExpiration.setNumber(number);
    }

    public void setPendingGuaranteeExpirationField(TimePeriod.Field field) {
        pendingGuaranteeExpiration.setField(field);
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

}
