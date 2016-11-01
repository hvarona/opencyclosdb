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
package nl.strohalm.cyclos.entities.accounts.transactions;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import nl.strohalm.cyclos.entities.accounts.AccountOwner;
import nl.strohalm.cyclos.entities.accounts.AccountType;
import nl.strohalm.cyclos.entities.accounts.SystemAccountOwner;
import nl.strohalm.cyclos.entities.accounts.fees.account.AccountFeeLog;
import nl.strohalm.cyclos.entities.customization.fields.PaymentCustomField;
import nl.strohalm.cyclos.entities.customization.fields.PaymentCustomFieldValue;
import nl.strohalm.cyclos.entities.members.Element;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.utils.CustomFieldsContainer;
import nl.strohalm.cyclos.utils.FormatObject;
import nl.strohalm.cyclos.utils.StringValuedEnum;

/**
 * An invoice sent to a member or to the system
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "invoices")
public class Invoice extends Entity implements CustomFieldsContainer<PaymentCustomField, PaymentCustomFieldValue> {

    public static enum Relationships implements Relationship {

        ACCOUNT_FEE_LOG("accountFeeLog"), DESTINATION_ACCOUNT_TYPE("destinationAccountType"), FROM_MEMBER("fromMember"), TO_MEMBER("toMember"), SENT_BY("sentBy"), PERFORMED_BY("performedBy"), TRANSFER_TYPE("transferType"), TRANSFER("transfer"), PAYMENTS("payments"), SCHEDULED_PAYMENT("scheduledPayment"), CUSTOM_VALUES("customValues");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static enum Status implements StringValuedEnum {

        OPEN("O"), ACCEPTED("A"), DENIED("D"), CANCELLED("C"), EXPIRED("E");
        private final String value;

        private Status(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final long serialVersionUID = -2221939158739233962L;

    private AccountFeeLog accountFeeLog;
    private Calendar date;
    private String description;
    private AccountType destinationAccountType;
    private Member fromMember;
    private Status status = Status.OPEN;
    private Member toMember;
    private TransferType transferType;
    private Transfer transfer;
    private BigDecimal amount;
    private Element sentBy;
    private Element performedBy;
    private List<InvoicePayment> payments;
    private ScheduledPayment scheduledPayment;
    private Collection<PaymentCustomFieldValue> customValues;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    @ManyToOne(targetEntity = AccountFeeLog.class)
    @JoinColumn(name = "account_fee_log_id", nullable = false)
    public AccountFeeLog getAccountFeeLog() {
        return accountFeeLog;
    }

    @Column(nullable = false, updatable = false, precision = 15, scale = 6)
    public BigDecimal getAmount() {
        return amount;
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

    @OneToMany(targetEntity = InvoicePayment.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_id")
    @Override
    public Collection<PaymentCustomFieldValue> getCustomValues() {
        return customValues;
    }

    @Column(nullable = false, updatable = false)
    public Calendar getDate() {
        return date;
    }

    @Column(updatable = false)
    public String getDescription() {
        return description;
    }

    @ManyToOne(targetEntity = AccountType.class)
    @JoinColumn(name = "dest_type_id", nullable = false)
    public AccountType getDestinationAccountType() {
        return destinationAccountType;
    }

    @Transient
    public AccountOwner getFrom() {
        return isFromSystem() ? SystemAccountOwner.instance() : fromMember;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "from_member_id", updatable = false)
    public Member getFromMember() {
        return fromMember;
    }

    @Transient
    public Payment getPayment() {
        if (transfer == null) {
            return scheduledPayment;
        }
        return transfer;
    }

    @OneToMany(targetEntity = InvoicePayment.class)
    @JoinColumn(name = "invoice_id")
    public List<InvoicePayment> getPayments() {
        return payments;
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "performed_by_id")
    public Element getPerformedBy() {
        return performedBy;
    }

    @ManyToOne(targetEntity = ScheduledPayment.class)
    @JoinColumn(name = "scheduled_payment_id")
    public ScheduledPayment getScheduledPayment() {
        return scheduledPayment;
    }

    @ManyToOne(targetEntity = Element.class)
    @JoinColumn(name = "sent_by_id")
    public Element getSentBy() {
        return sentBy;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Status getStatus() {
        return status;
    }

    @Transient
    public AccountOwner getTo() {
        return isToSystem() ? SystemAccountOwner.instance() : toMember;
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "to_member_id", updatable = false)
    public Member getToMember() {
        return toMember;
    }

    @ManyToOne(targetEntity = Transfer.class)
    @JoinColumn(name = "transfer_id", nullable = false)
    public Transfer getTransfer() {
        return transfer;
    }

    @ManyToOne(targetEntity = TransferType.class)
    @JoinColumn(name = "transfer_type_id", nullable = false)
    public TransferType getTransferType() {
        return transferType;
    }

    @Transient
    public boolean isFromSystem() {
        return fromMember == null;
    }

    @Transient
    public boolean isOpen() {
        return status == Status.OPEN;
    }

    @Transient
    public boolean isToSystem() {
        return toMember == null;
    }

    public void setAccountFeeLog(final AccountFeeLog taxLog) {
        accountFeeLog = taxLog;
    }

    public void setAmount(final BigDecimal value) {
        amount = value;
    }

    public void setCustomValues(final Collection<PaymentCustomFieldValue> customValues) {
        this.customValues = customValues;
    }

    public void setDate(final Calendar date) {
        this.date = date;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setDestinationAccountType(final AccountType destinationAccountType) {
        this.destinationAccountType = destinationAccountType;
    }

    public void setFrom(final AccountOwner owner) {
        fromMember = (owner instanceof Member) ? (Member) owner : null;
    }

    public void setFromMember(final Member fromMember) {
        this.fromMember = fromMember;
    }

    public void setPayment(final Payment payment) {
        if (payment instanceof Transfer) {
            setTransfer((Transfer) payment);
        } else {
            setScheduledPayment((ScheduledPayment) payment);
        }
    }

    public void setPayments(final List<InvoicePayment> payments) {
        this.payments = payments;
    }

    public void setPerformedBy(final Element performedBy) {
        this.performedBy = performedBy;
    }

    public void setScheduledPayment(final ScheduledPayment scheduledPayment) {
        this.scheduledPayment = scheduledPayment;
    }

    public void setSentBy(final Element sentBy) {
        this.sentBy = sentBy;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public void setTo(final AccountOwner owner) {
        toMember = (owner instanceof Member) ? (Member) owner : null;
    }

    public void setToMember(final Member toMember) {
        this.toMember = toMember;
    }

    public void setTransfer(final Transfer transfer) {
        this.transfer = transfer;
    }

    public void setTransferType(final TransferType transferType) {
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        return getId() + " - amount: " + FormatObject.formatObject(amount) + ", from: " + getFrom() + ", to: " + getTo();
    }

    @Override
    protected void appendVariableValues(final Map<String, Object> variables, final LocalSettings localSettings) {
        if (destinationAccountType == null) {
            variables.put("amount", localSettings.getNumberConverter().toString(amount));
        } else {
            variables.put("amount", localSettings.getUnitsConverter(destinationAccountType.getCurrency().getPattern()).toString(amount));
        }
        variables.put("date", localSettings.getDateConverter().toString(date));
        variables.put("description", description);
        variables.put("desc", description);
    }
}
