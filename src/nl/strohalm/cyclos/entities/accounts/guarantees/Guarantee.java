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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.accounts.loans.Loan;
import nl.strohalm.cyclos.entities.customization.fields.PaymentCustomField;
import nl.strohalm.cyclos.entities.customization.fields.PaymentCustomFieldValue;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.services.accounts.guarantees.GuaranteeFeeVO;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;
import nl.strohalm.cyclos.utils.Period;
import nl.strohalm.cyclos.utils.StringValuedEnum;
import nl.strohalm.cyclos.utils.guarantees.GuaranteesHelper;

@javax.persistence.Entity
@Table(name = "guarantees")
public class Guarantee extends Entity implements CustomFieldsContainer<PaymentCustomField, PaymentCustomFieldValue> {

    public static enum Relationships implements Relationship {

        CERTIFICATION("certification"), GUARANTEE_TYPE("guaranteeType"), LOAN("loan"), PAYMENT_OBLIGATIONS("paymentObligations"), LOGS("logs"), BUYER("buyer"), SELLER("seller"), ISSUER("issuer"), CUSTOM_VALUES("customValues");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    public static enum Status implements StringValuedEnum {

        PENDING_ISSUER("PI"), PENDING_ADMIN("PA"), ACCEPTED("A"), REJECTED("R"), WITHOUT_ACTION("WA"), CANCELLED("C");
        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = 3906916142405683801L;

    private Status status;
    private BigDecimal amount;
    private GuaranteeFeeVO creditFeeSpec;
    private GuaranteeFeeVO issueFeeSpec;
    private Period validity;
    private Calendar registrationDate;
    private Certification certification;
    private GuaranteeType guaranteeType;
    private Loan loan;
    private Collection<PaymentObligation> paymentObligations;
    private Collection<GuaranteeLog> logs;
    private Member buyer;
    private Member seller;

    /**
     * If the guarantee type's model is 'with payment obligation' this issuer
     * must be equals to the issuer of the certification.
     */
    private Member issuer;

    private Collection<PaymentCustomFieldValue> customValues;

    /**
     * Change the guarantee's status and adds a new guarantee log to it
     *
     * @param status the new guarantee's status
     * @param by the author of the change
     * @return the new GuaranteeLog added to this Guarantee
     */
    public GuaranteeLog changeStatus(final Status status, final Element by) {
        setStatus(status);

        if (logs == null) {
            logs = new ArrayList();
        }
        final GuaranteeLog log = getNewLog(status, by);
        logs.add(log);

        return log;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "amount", precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "buyer_id")
    public Member getBuyer() {
        return buyer;
    }

    @ManyToOne(targetEntity = Certification.class)
    @JoinColumn(name = "certification_id")
    public Certification getCertification() {
        return certification;
    }

    @Transient
    public BigDecimal getCreditFee() {
        return isNullFee(creditFeeSpec) ? null : GuaranteesHelper.calculateFee(validity, amount, creditFeeSpec);
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "fee", column = @Column(name = "credit_fee")),
        @AttributeOverride(name = "type", column = @Column(name = "credit_fee_type"))})
    public GuaranteeFeeVO getCreditFeeSpec() {
        return creditFeeSpec;
    }

    @Transient
    @Override
    public Class<PaymentCustomField> getCustomFieldClass() {
        return PaymentCustomField.class;
    }

    @Transient
    @Override
    public Class<PaymentCustomFieldValue> getCustomFieldValueClass() {
        return PaymentCustomFieldValue.class;
    }

    @OneToMany(targetEntity = PaymentCustomFieldValue.class)
    @JoinColumn(name = "guarantee_id")
    @Override
    public Collection<PaymentCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @ManyToOne(targetEntity = GuaranteeType.class)
    @JoinColumn(name = "guarantee_type_id")
    public GuaranteeType getGuaranteeType() {
        return guaranteeType;
    }

    @Transient
    public BigDecimal getIssueFee() {
        return isNullFee(issueFeeSpec) ? null : GuaranteesHelper.calculateFee(validity, amount, issueFeeSpec);
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "fee", column = @Column(name = "issue_fee")),
        @AttributeOverride(name = "type", column = @Column(name = "issue_fee_type"))})
    public GuaranteeFeeVO getIssueFeeSpec() {
        return issueFeeSpec;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "issuer_id")
    public Member getIssuer() {
        return issuer;
    }

    @ManyToOne(targetEntity = Loan.class)
    @JoinColumn(name = "loan_id")
    public Loan getLoan() {
        return loan;
    }

    @OneToMany(targetEntity = GuaranteeLog.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "guarantee_id")
    public Collection<GuaranteeLog> getLogs() {
        return logs;
    }

    @Transient
    public GuaranteeLog getNewLog(final Status status, final Element by) {
        final GuaranteeLog log = new GuaranteeLog();
        log.setGuarantee(this);
        log.setDate(Calendar.getInstance());
        log.setStatus(status);
        log.setBy(by);

        // TODO: should I add the created log to the logs' collection?
        return log;
    }

    @OneToMany(targetEntity = PaymentObligation.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "guarantee_id")
    public Collection<PaymentObligation> getPaymentObligations() {
        return paymentObligations;
    }

    @Column(name = "registration_date", nullable = false)
    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "seller_id")
    public Member getSeller() {
        return seller;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public Status getStatus() {
        return status;
    }

    @Transient
    public Period getValidity() {
        return validity;
    }

    @Column(name = "begin_date")
    public Calendar getBeginDate() {
        return validity.getBegin();
    }

    @Column(name = "end_date")
    public Calendar getEndDate() {
        return validity.getEnd();
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public void setBuyer(final Member buyer) {
        this.buyer = buyer;
    }

    public void setCertification(final Certification certification) {
        this.certification = certification;
    }

    public void setCreditFeeSpec(final GuaranteeFeeVO creditFeeSpec) {
        this.creditFeeSpec = creditFeeSpec;
    }

    @Override
    public void setCustomValues(final Collection<PaymentCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setGuaranteeType(final GuaranteeType guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public void setIssueFeeSpec(final GuaranteeFeeVO issueFeeSpec) {
        this.issueFeeSpec = issueFeeSpec;
    }

    public void setIssuer(final Member issuer) {
        this.issuer = issuer;
    }

    public void setLoan(final Loan loan) {
        this.loan = loan;
    }

    public void setLogs(final Collection<GuaranteeLog> logs) {
        this.logs = logs;
    }

    public void setPaymentObligations(final Collection<PaymentObligation> paymentObligations) {
        this.paymentObligations = paymentObligations;
    }

    public void setRegistrationDate(final Calendar issueDate) {
        registrationDate = issueDate;
    }

    public void setSeller(final Member seller) {
        this.seller = seller;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setValidity(final Period validity) {
        this.validity = validity;
    }

    public void setBeginDate(Calendar begin) {
        validity.setBegin(begin);
    }

    public void setEndDate(Calendar end) {
        validity.setEnd(end);
    }

    @Override
    public String toString() {
        return "G: " + getId() + " - " + status;
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        final String pattern = getGuaranteeType().getCurrency().getPattern();
        variables.put("amount", localSettings.getUnitsConverter(pattern).toString(getAmount()));
        variables.put("buyer_member", getBuyer().getName());
        variables.put("buyer_login", getBuyer().getUsername());
        if (getSeller() != null) {
            variables.put("seller_member", getSeller().getName());
            variables.put("seller_login", getSeller().getUsername());
        }
        if (getIssuer() != null) {
            variables.put("issuer_member", getIssuer().getName());
            variables.put("issuer_login", getIssuer().getUsername());
        }
    }

    private boolean isNullFee(final GuaranteeFeeVO feeSpec) {
        return feeSpec == null || feeSpec.getType() == null || feeSpec.getFee() == null;
    }

}
