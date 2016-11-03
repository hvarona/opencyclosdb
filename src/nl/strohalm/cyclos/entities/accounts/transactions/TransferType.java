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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import nl.strohalm.cyclos.entities.Entity;
import nl.strohalm.cyclos.entities.Relationship;
import nl.strohalm.cyclos.entities.access.Channel;
import nl.strohalm.cyclos.entities.accounts.AccountType;
import nl.strohalm.cyclos.entities.accounts.Currency;
import nl.strohalm.cyclos.entities.accounts.fees.account.AccountFee;
import nl.strohalm.cyclos.entities.accounts.fees.transaction.TransactionFee;
import nl.strohalm.cyclos.entities.accounts.fees.transaction.TransactionFee.ChargeType;
import nl.strohalm.cyclos.entities.accounts.loans.LoanParameters;
import nl.strohalm.cyclos.entities.customization.fields.PaymentCustomField;
import nl.strohalm.cyclos.entities.groups.Group;
import nl.strohalm.cyclos.entities.members.Member;
import nl.strohalm.cyclos.entities.members.Reference.Level;
import nl.strohalm.cyclos.utils.StringValuedEnum;
import nl.strohalm.cyclos.utils.TimePeriod;
import nl.strohalm.cyclos.utils.TimePeriod.Field;

import org.apache.commons.collections.CollectionUtils;

/**
 * Every transfer has a type, which may contain fees and other relevant data
 *
 * @author luis
 */
@javax.persistence.Entity
@Table(name = "transfer_types")
public class TransferType extends Entity {

    /**
     * A transfer type context represents where it can be performed
     *
     * @author luis
     */
    @Embeddable
    public static class Context implements Serializable {

        private static final long serialVersionUID = -7966654322680432255L;

        public static Context payment() {
            final Context context = new Context();
            context.setPayment(true);
            return context;
        }

        public static Context self() {
            final Context context = new Context();
            context.setSelfPayment(true);
            return context;
        }

        private boolean payment = false;
        private boolean selfPayment = false;

        public Context() {
        }

        @Column(name = "allowed_payment")
        public boolean isPayment() {
            return payment;
        }

        @Column(name = "allowed_self_payment")
        public boolean isSelfPayment() {
            return selfPayment;
        }

        public void setPayment(final boolean payment) {
            this.payment = payment;
        }

        public void setSelfPayment(final boolean selfPayment) {
            this.selfPayment = selfPayment;
        }
    }

    public static enum Direction {

        FROM, TO, BOTH
    }

    public static enum Relationships implements Relationship {

        FROM("from"), GROUPS("groups"), GROUPS_AS_MEMBER("groupsAsMember"), TO("to"), TRANSACTION_FEES("transactionFees"), GENERATED_BY_TRANSACTION_FEES("generatedByTransactionFees"), GENERATED_BY_ACCOUNT_FEES("generatedByAccountFees"), PAYMENT_FILTERS("paymentFilters"), AUTHORIZATION_LEVELS("authorizationLevels"), CUSTOM_FIELDS("customFields"), LINKED_CUSTOM_FIELDS("linkedCustomFields"), CHANNELS("channels");
        private final String name;

        private Relationships(final String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    public static enum TransactionHierarchyVisibility implements StringValuedEnum {

        ADMIN("A"), BROKER("B"), MEMBER("M");
        private final String value;

        private TransactionHierarchyVisibility(final String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        public boolean isVisibleTo(final Group.Nature groupNature) {
            switch (groupNature) {
                case ADMIN:
                    return true;
                case BROKER:
                    return this != TransactionHierarchyVisibility.ADMIN;
                case MEMBER:
                case OPERATOR:
                    return this == TransactionHierarchyVisibility.MEMBER;
            }
            return false;
        }
    }

    private static final long serialVersionUID = -6248433842449336187L;

    private String name;
    private String description;
    private String confirmationMessage;
    private Context context = new Context();
    private boolean priority;
    private boolean conciliable;
    private AccountType from;
    private AccountType to;
    private BigDecimal maxAmountPerDay;
    private BigDecimal minAmount;
    private LoanParameters loan;
    private Collection< TransactionFee> transactionFees;
    private Collection< TransactionFee> generatedByTransactionFees;
    private Collection< AccountFee> generatedByAccountFees;
    private Collection< Group> groups;
    private Collection< Group> groupsAsMember;
    private Collection<PaymentFilter> paymentFilters;
    private boolean requiresAuthorization;
    private Collection<AuthorizationLevel> authorizationLevels;
    private boolean allowsScheduledPayments;
    private boolean requiresFeedback;
    private boolean reserveTotalAmountOnScheduling;
    private boolean allowCancelScheduledPayments;
    private boolean allowBlockScheduledPayments;
    private boolean showScheduledPaymentsToDestination;
    private boolean allowSmsNotification;
    private Calendar feedbackEnabledSince;
    private TimePeriod feedbackExpirationTime;
    private TimePeriod feedbackReplyExpirationTime;
    private String defaultFeedbackComments;
    private Level defaultFeedbackLevel;
    private Collection<Channel> channels;
    private Member fixedDestinationMember;
    private Collection<PaymentCustomField> customFields;
    private Collection<PaymentCustomField> linkedCustomFields;
    private String transferListenerClass;
    private TransactionHierarchyVisibility transactionHierarchyVisibility = TransactionHierarchyVisibility.MEMBER;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return super.getId();
    }

    /**
     * gets all the transaction fees based on a-rate, being the fees with
     * ChargeType A_RATE or MIXED_A_D_RATES
     *
     * @return a Collection with A-rated TransactionFees
     * @see #getRatedFees()
     */
    @Transient
    public Collection< TransactionFee> getARatedFees() {
        final List<TransactionFee> result = new ArrayList<TransactionFee>(transactionFees.size());
        for (final TransactionFee fee : transactionFees) {
            if (fee.getChargeType() == ChargeType.A_RATE || fee.getChargeType() == ChargeType.MIXED_A_D_RATES) {
                result.add(fee);
            }
        }
        return result;
    }

    @OneToMany(targetEntity = AuthorizationLevel.class)
    @JoinColumn(name = "type_id")
    public Collection<AuthorizationLevel> getAuthorizationLevels() {
        return authorizationLevels;
    }

    @ManyToMany(targetEntity = Channel.class)
    @JoinTable(name = "transfer_types_channels",
            joinColumns = @JoinColumn(name = "transfer_type_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id"))
    public Collection<Channel> getChannels() {
        return channels;
    }

    @Column(name = "confirmation_message")
    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    @Embedded
    public Context getContext() {
        return context;
    }

    @Transient
    public Currency getCurrency() {
        try {
            return from.getCurrency();
        } catch (final Exception e) {
            return null;
        }
    }

    @OneToMany(targetEntity = PaymentCustomField.class)
    @JoinColumn(name = "transfer_type_id")
    public Collection<PaymentCustomField> getCustomFields() {
        return customFields;
    }

    @Column(name = "default_feedback_comments")
    public String getDefaultFeedbackComments() {
        return defaultFeedbackComments;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "default_feedback_level")
    public Level getDefaultFeedbackLevel() {
        return defaultFeedbackLevel;
    }

    @Column(length = 100, nullable = false)
    public String getDescription() {
        return description;
    }

    /**
     * gets all the transaction fees based on d-rate, that is: having ChargeType
     * = D_RATE
     *
     * @return a Collection with TransactionFees
     * @see #getRatedFees()
     */
    @Transient
    public Collection< TransactionFee> getDRatedFees() {
        final List<TransactionFee> result = new ArrayList<TransactionFee>(transactionFees.size());
        for (final TransactionFee fee : transactionFees) {
            if (fee.getChargeType() == ChargeType.D_RATE || fee.getChargeType() == ChargeType.MIXED_A_D_RATES) {
                result.add(fee);
            }
        }
        return result;
    }

    @Column(name = "feedback_enabled_since")
    public Calendar getFeedbackEnabledSince() {
        return feedbackEnabledSince;
    }

    @Transient
    public TimePeriod getFeedbackExpirationTime() {
        return feedbackExpirationTime;
    }

    @Column(name = "feedback_expiration_time_number")
    public int getFeedbackExperationTimeNumber() {
        return feedbackExpirationTime.getNumber();
    }

    @Column(name = "feedback_expiration_time_field")
    @Enumerated(EnumType.STRING)
    public Field getFeedbackExperationTimeField() {
        return feedbackExpirationTime.getField();
    }

    @Transient
    public TimePeriod getFeedbackReplyExpirationTime() {
        return feedbackReplyExpirationTime;
    }

    @Column(name = "feedback_reply_expiration_time_number")
    public int getFeedbackReplyExperationTimeNumber() {
        return feedbackReplyExpirationTime.getNumber();
    }

    @Column(name = "feedback_reply_expiration_time_field")
    @Enumerated(EnumType.STRING)
    public Field getFeedbackReplyExperationTimeField() {
        return feedbackReplyExpirationTime.getField();
    }

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "fixed_destination_member_id")
    public Member getFixedDestinationMember() {
        return fixedDestinationMember;
    }

    @ManyToOne(targetEntity = AccountType.class)
    @JoinColumn(name = "from_account_type_id")
    public AccountType getFrom() {
        return from;
    }

    @OneToMany(targetEntity = AccountFee.class)
    @JoinColumn(name = "transfer_type_id")
    public Collection< AccountFee> getGeneratedByAccountFees() {
        return generatedByAccountFees;
    }

    @OneToMany(targetEntity = TransactionFee.class)
    @JoinColumn(name = "generated_type_id")
    public Collection< TransactionFee> getGeneratedByTransactionFees() {
        return generatedByTransactionFees;
    }

    @ManyToMany(targetEntity = Group.class)
    @JoinTable(name = "transfer_types_channels",
            joinColumns = @JoinColumn(name = "transfer_type_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Collection< Group> getGroups() {
        return groups;
    }

    @ManyToMany(targetEntity = Group.class)
    @JoinTable(name = "groups_transfer_types_as_member",
            joinColumns = @JoinColumn(name = "transfer_type_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    public Collection< Group> getGroupsAsMember() {
        return groupsAsMember;
    }

    @ManyToMany(targetEntity = PaymentCustomField.class)
    @JoinTable(name = "transfer_types_linked_custom_fields",
            joinColumns = @JoinColumn(name = "transfer_type_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id"))
    public Collection<PaymentCustomField> getLinkedCustomFields() {
        return linkedCustomFields;
    }

    @Embedded
    public LoanParameters getLoan() {
        return loan;
    }

    @Column(name = "max_amount_per_day")
    public BigDecimal getMaxAmountPerDay() {
        return maxAmountPerDay;
    }

    @Column(name = "min_amount")
    public BigDecimal getMinAmount() {
        return minAmount;
    }

    @Column(length = 100, nullable = false)
    @Override
    public String getName() {
        return name;
    }

    @ManyToMany(targetEntity = PaymentFilter.class)
    @JoinTable(name = "transfer_types_payment_filters",
            joinColumns = @JoinColumn(name = "transfer_type_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_filter_id"))
    public Collection<PaymentFilter> getPaymentFilters() {
        return paymentFilters;
    }

    /**
     * gets all the transaction fees which do use a-rate or d-rate
     *
     * @return a Collection with TransactionFees using a-rate or d-rate
     * @see #getRatedFees()
     */
    @Transient
    public Collection< TransactionFee> getRatedFees() {
        final List<TransactionFee> result = new ArrayList<TransactionFee>(transactionFees.size());
        for (final TransactionFee fee : transactionFees) {
            if (fee.getChargeType() == ChargeType.A_RATE || fee.getChargeType() == ChargeType.D_RATE || fee.getChargeType() == ChargeType.MIXED_A_D_RATES) {
                result.add(fee);
            }
        }
        return result;
    }

    @ManyToOne(targetEntity = AccountType.class)
    @JoinColumn(name = "to_account_type_id")
    public AccountType getTo() {
        return to;
    }

    @OneToMany(targetEntity = TransactionFee.class)
    @JoinColumn(name = "original_type_id")
    public Collection< TransactionFee> getTransactionFees() {
        return transactionFees;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_hierarchy_visibility")
    public TransactionHierarchyVisibility getTransactionHierarchyVisibility() {
        return transactionHierarchyVisibility;
    }

    @Column(name = "transfer_listener_class")
    public String getTransferListenerClass() {
        return transferListenerClass;
    }

    /**
     * @return true if there are any TransactionFees on this TT.
     */
    public boolean hasTransactionFees() {
        return !CollectionUtils.isEmpty(transactionFees);
    }

    @Column(name = "allow_block_sched", nullable = false)
    public boolean isAllowBlockScheduledPayments() {
        return allowBlockScheduledPayments;
    }

    @Column(name = "allow_cancel_sched", nullable = false)
    public boolean isAllowCancelScheduledPayments() {
        return allowCancelScheduledPayments;
    }

    @Column(name = "allow_sms_notification", nullable = false)
    public boolean isAllowSmsNotification() {
        return allowSmsNotification;
    }

    @Column(name = "allows_scheduled_payments", nullable = false)
    public boolean isAllowsScheduledPayments() {
        return allowsScheduledPayments;
    }

    @Column(nullable = false)
    public boolean isConciliable() {
        return conciliable;
    }

    @Transient
    public boolean isFromMember() {
        return !isFromSystem();
    }

    @Transient
    public boolean isFromSystem() {
        return from.getNature() == AccountType.Nature.SYSTEM;
    }

    /**
     * returns true if the TransferType has TransactionFees based on a-rate.
     */
    @Transient
    public boolean isHavingAratedFees() {
        if (getARatedFees().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * returns true if the TransferType has TransactionFees based on d-rate.
     */
    @Transient
    public boolean isHavingDratedFees() {
        if (getDRatedFees().size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * returns true if the TransferType has TransactionFees based on a-rate or
     * d-rate.
     */
    @Transient
    public boolean isHavingRatedFees() {
        if (getRatedFees().size() == 0) {
            return false;
        }
        return true;
    }

    @Transient
    public boolean isLoanType() {
        return loan != null && loan.getType() != null;
    }

    @Column(nullable = false)
    public boolean isPriority() {
        return priority;
    }

    @Column(name = "requires_authorization", nullable = false)
    public boolean isRequiresAuthorization() {
        return requiresAuthorization;
    }

    @Column(name = "requires_feedback", nullable = false)
    public boolean isRequiresFeedback() {
        return requiresFeedback;
    }

    @Column(name = "reserve_total_on_sched", nullable = false)
    public boolean isReserveTotalAmountOnScheduling() {
        return reserveTotalAmountOnScheduling;
    }

    @Column(name = "show_sched_to_dest", nullable = false)
    public boolean isShowScheduledPaymentsToDestination() {
        return showScheduledPaymentsToDestination;
    }

    @Transient
    public boolean isToMember() {
        return !isToSystem();
    }

    @Transient
    public boolean isToSystem() {
        return to.getNature() == AccountType.Nature.SYSTEM;
    }

    public void setAllowBlockScheduledPayments(final boolean allowBlockScheduledPayments) {
        this.allowBlockScheduledPayments = allowBlockScheduledPayments;
    }

    public void setAllowCancelScheduledPayments(final boolean allowCancelScheduledPayments) {
        this.allowCancelScheduledPayments = allowCancelScheduledPayments;
    }

    public void setAllowSmsNotification(final boolean allowSmsNotification) {
        this.allowSmsNotification = allowSmsNotification;
    }

    public void setAllowsScheduledPayments(final boolean allowsScheduledPayments) {
        this.allowsScheduledPayments = allowsScheduledPayments;
    }

    public void setAuthorizationLevels(final Collection<AuthorizationLevel> authorizationLevels) {
        this.authorizationLevels = authorizationLevels;
    }

    public void setChannels(final Collection<Channel> channels) {
        this.channels = channels;
    }

    public void setConciliable(final boolean conciliable) {
        this.conciliable = conciliable;
    }

    public void setConfirmationMessage(final String confirmationMessage) {
        this.confirmationMessage = confirmationMessage;
    }

    public void setContext(final Context context) {
        this.context = context;
    }

    public void setCustomFields(final Collection<PaymentCustomField> customFields) {
        this.customFields = customFields;
    }

    public void setDefaultFeedbackComments(final String defaultFeedbackComments) {
        this.defaultFeedbackComments = defaultFeedbackComments;
    }

    public void setDefaultFeedbackLevel(final Level defaultFeedbackLevel) {
        this.defaultFeedbackLevel = defaultFeedbackLevel;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setFeedbackEnabledSince(final Calendar feedbackEnabledSince) {
        this.feedbackEnabledSince = feedbackEnabledSince;
    }

    public void setFeedbackExpirationTime(final TimePeriod feedbackExpirationTime) {
        this.feedbackExpirationTime = feedbackExpirationTime;
    }

    public void setFeedbackExperationTimeNumber(int number) {
        feedbackExpirationTime.setNumber(number);
    }

    public void setFeedbackExperationTimeField(Field field) {
        feedbackExpirationTime.setField(field);
    }

    public void setFeedbackReplyExpirationTime(final TimePeriod feedbackReplyExpirationTime) {
        this.feedbackReplyExpirationTime = feedbackReplyExpirationTime;
    }

    public void setFeedbackReplyExperationTimeNumber(int number) {
        feedbackReplyExpirationTime.setNumber(number);
    }

    public void setFeedbackReplyExperationTimeField(Field field) {
        feedbackReplyExpirationTime.setField(field);
    }

    public void setFixedDestinationMember(final Member fixedDestinationMember) {
        this.fixedDestinationMember = fixedDestinationMember;
    }

    public void setFrom(final AccountType from) {
        this.from = from;
    }

    public void setGeneratedByAccountFees(final Collection< AccountFee> generatedByAccountFees) {
        this.generatedByAccountFees = generatedByAccountFees;
    }

    public void setGeneratedByTransactionFees(final Collection< TransactionFee> generatedByTransactionFees) {
        this.generatedByTransactionFees = generatedByTransactionFees;
    }

    public void setGroups(final Collection< Group> groups) {
        this.groups = groups;
    }

    public void setGroupsAsMember(final Collection< Group> groupsAsMember) {
        this.groupsAsMember = groupsAsMember;
    }

    public void setLinkedCustomFields(final Collection<PaymentCustomField> linkedCustomFields) {
        this.linkedCustomFields = linkedCustomFields;
    }

    public void setLoan(final LoanParameters loan) {
        this.loan = loan;
        if (loan != null) {
            loan.setOriginalTransferType(this);
        }
    }

    public void setMaxAmountPerDay(final BigDecimal maxAmountPerDay) {
        this.maxAmountPerDay = maxAmountPerDay;
    }

    public void setMinAmount(final BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPaymentFilters(final Collection<PaymentFilter> paymentFilters) {
        this.paymentFilters = paymentFilters;
    }

    public void setPriority(final boolean priority) {
        this.priority = priority;
    }

    public void setRequiresAuthorization(final boolean requiresAuthorization) {
        this.requiresAuthorization = requiresAuthorization;
    }

    public void setRequiresFeedback(final boolean requiresFeedback) {
        this.requiresFeedback = requiresFeedback;
    }

    public void setReserveTotalAmountOnScheduling(final boolean reserveTotalAmountOnScheduling) {
        this.reserveTotalAmountOnScheduling = reserveTotalAmountOnScheduling;
    }

    public void setShowScheduledPaymentsToDestination(final boolean showScheduledPaymentsToDestination) {
        this.showScheduledPaymentsToDestination = showScheduledPaymentsToDestination;
    }

    public void setTo(final AccountType to) {
        this.to = to;
    }

    public void setTransactionFees(final Collection< TransactionFee> transactionFees) {
        this.transactionFees = transactionFees;
    }

    public void setTransactionHierarchyVisibility(final TransactionHierarchyVisibility transactionHierarchyVisibility) {
        this.transactionHierarchyVisibility = transactionHierarchyVisibility;
    }

    public void setTransferListenerClass(final String transferListenerClass) {
        this.transferListenerClass = transferListenerClass;
    }

    @Override
    public String toString() {
        return getId() + " - " + name;
    }

}
